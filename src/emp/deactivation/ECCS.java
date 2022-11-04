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

public class ECCS {

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
		getDBConnection(p.getProperty("eccsUrl"), p.getProperty("eccsUname"), p.getProperty("eccsPass"));
		Statement stmt = conn.createStatement();
		conn.setAutoCommit(false);
		return stmt.executeQuery(selectQuery);
	}
			

	public static boolean update(String EMP_ID) throws Exception {
		
		boolean action = false;

		String updateQuery = "update ECCS.ECCS_USER set ACCOUNT_DELETED = 1, USER_LAST_MODIFIED_BY = 'ff8080814c666318014c6664a31a0003', "
				+ "LAST_MODIFIED_TIME =sysdate where LOGIN_ID IN("+EMP_ID+") AND ACCOUNT_DELETED = 0";

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

		String selectQuery = "SELECT LOGIN_ID,USER_NAME,LAST_LOGIN_TIME FROM ECCS.ECCS_USER WHERE LOGIN_ID IN("+EMP_ID+") AND ACCOUNT_DELETED = 0";

		try {
			ResultSet rs = executeQuery(selectQuery);
			boolean action = update(EMP_ID);
			while (rs.next()) {
				int STAFF_ID = rs.getInt("LOGIN_ID");
				String UserName = rs.getString("USER_NAME");
				String empidstr = Integer.toString(STAFF_ID);
				Date LAST_LOGIN_TIME = rs.getDate("LAST_LOGIN_TIME");
				String LastloginID = rs.getString("LOGIN_ID");
				String lastLogin = LAST_LOGIN_TIME == null ? "" : FormatDate.dateFormat.format(LAST_LOGIN_TIME);
//				String result = empidstr + "," + UserName + ",ECCS" + ",Admin," + Tdate + "," + action;
				String result = ""+","+""+",Termination,"+LastloginID+","+empidstr + "," + UserName +","+lastLogin+",ECCS" + ",System," + Tdate + "," + action+","+"";
				System.out.println(result);
				list.add(result);

			}

			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (list.size() >= 1) {
			System.out.println("ID match in ECCS");
			deactFile.writeDeacList(list, filename);
		} else {
			System.out.println("No Activation List for ECCS");
		}

		conn.close();
	}

}
