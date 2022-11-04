package emp.deactivation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import emp.config.Config;
import emp.config.DeactivationFile;
import emp.config.FormatDate;
import oracle.jdbc.pool.OracleDataSource;

public class HRMS {

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
		getDBConnection(p.getProperty("hrmsUrl"), p.getProperty("hrmsUname"), p.getProperty("hrmsPass"));
		Statement stmt = conn.createStatement();
		conn.setAutoCommit(false);
		return stmt.executeQuery(selectQuery);
	}

	public static boolean update(String EMP_ID) {

		boolean action = false;

		String updateQuery = "UPDATE HRMS.HRM_USER SET  STATUS='I', UPDATE_BY='0', DATETIME_UPDATE=SYSDATE"
				+ " WHERE EMP_ID in(" + EMP_ID + ") AND STATUS='A'";

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

		String selectQuery = "select HU.EMP_ID,HE.EMP_NAME from "
				+ "HRMS.HRM_USER HU,HRMS.HRM_EMP HE WHERE HU.EMP_ID=HE.EMP_ID AND HU.EMP_ID IN (" + EMP_ID
				+ ") AND HU.STATUS='A'";

		try {
			ResultSet rs = executeQuery(selectQuery);
			boolean action = update(EMP_ID);
			while (rs.next()) {
				int STAFF_ID = rs.getInt("EMP_ID");
				String UserName = rs.getString("EMP_NAME");
				String empidstr = Integer.toString(STAFF_ID);
				String result = ""+","+""+",Termination,"+""+","+empidstr + "," + UserName +",NA"+",HRMS" + ",System," + Tdate + "," + action+","+"";
				System.out.println(result);
				list.add(result);

			}

			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (list.size() >= 1) {
			System.out.println("ID match in HRMS");
			deactFile.writeDeacList(list, filename);
		} else {
			System.out.println("No Activation List for HRMS");
		}
		conn.close();
	}

}
