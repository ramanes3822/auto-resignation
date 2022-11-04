package emp.deactivation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import emp.config.Config;
import emp.config.DeactivationFile;
import emp.config.FormatDate;
import oracle.jdbc.pool.OracleDataSource;

public class WDC {

	static String Tdate = FormatDate.Tdate;
	static Connection conn;

	public static void getDBConnection(String url, String username, String password) throws Exception {

		OracleDataSource ds;
		ds = new OracleDataSource();
		ds.setURL(url);
		conn = ds.getConnection(username, password);

	}

	public static ResultSet executeQuery(String selectQuery) throws Exception {
		Properties p = Config.properties();
		getDBConnection(p.getProperty("scssUrl"), p.getProperty("scssUname"), p.getProperty("scssPass"));
		Statement stmt = conn.createStatement();
		conn.setAutoCommit(false);
		return stmt.executeQuery(selectQuery);
	}

	public static String getAdminID() throws Exception {

		String idQuery = "SELECT USER_ID FROM APP_USER WHERE USERNAME='ADMIN'";
		ResultSet result = executeQuery(idQuery);
		result.next();
		String adminID = result.getString("USER_ID");
		return adminID;

	}

	public static boolean update(String EMP_ID) throws Exception {

		boolean action = false;

		String updateQuery = "UPDATE APP_USER SET STATUS='I', UPDATE_BY='" + getAdminID()
				+ "', DATETIME_UPDATE=SYSDATE WHERE STAFF_CODE IN (" + EMP_ID + ")  AND STATUS='A'";

		try {
			action = executeQuery(updateQuery) != null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return action;
	}

	public static void deactivate(String EMP_ID, String filename) throws Exception {

		List<String> list = new ArrayList<String>();

		DeactivationFile deactFile = new DeactivationFile();

		String selectQuery = "select STAFF_CODE,USERNAME,LAST_LOGIN_DATE from APP_USER where STAFF_CODE in (" + EMP_ID
				+ ") AND STATUS='A'";

		try {
			ResultSet rs = executeQuery(selectQuery);
			boolean action = update(EMP_ID);
			while (rs.next()) {
				int STAFF_ID = rs.getInt("STAFF_ID");
				String UserName = rs.getString("USER_NAME");
				String empidstr = Integer.toString(STAFF_ID);
				Date LAST_LOGIN_DATE = rs.getDate("LAST_LOGIN_DATE");
				String lastLogin = LAST_LOGIN_DATE == null ? "" : FormatDate.dateFormat.format(LAST_LOGIN_DATE);

				String result = "" + "," + "" + "," + "Termination" + "," + UserName + "," + empidstr + "," + UserName
						+ "," + lastLogin + ",WDC" + ",System," + Tdate + "," + action + "," + "";
				System.out.println(result);
				list.add(result);

			}

			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (list.size() >= 1) {
			System.out.println("ID match in WDC");
			deactFile.writeDeacList(list, filename);
		} else {
			System.out.println("No Activation List for WDC");
		}
		conn.close();
	}

}
