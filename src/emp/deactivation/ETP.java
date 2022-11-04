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

public class ETP {

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
		getDBConnection(p.getProperty("etpUrl"), p.getProperty("etpUname"), p.getProperty("etpPass"));
		Statement stmt = conn.createStatement();
		conn.setAutoCommit(false);
		return stmt.executeQuery(selectQuery);
	}
	
	
	public static String getAdminID() throws Exception {
		
		String idQuery = "SELECT ID FROM ETP.ETP_USER WHERE LOGIN_ID='ADMIN'";
		ResultSet result=executeQuery(idQuery);
		result.next();
		String adminID = result.getString("ID");
		return adminID;
		
	}
		

	public static boolean update(String EMP_ID) throws Exception {
		
		boolean action = false;

		String updateQuery = "UPDATE ETP.ETP_USER SET ACCOUNT_DELETED=1,ACCOUNT_ACTIVE=0,LAST_MODIFIED_BY='"+getAdminID()+"',"
				+ "LAST_MODIFIED_TIME=SYSDATE WHERE IS_WESTPORTS_USER=1 AND STAFF_ID IS NOT NULL AND "
				+ "STAFF_ID in(" + EMP_ID + ") AND ACCOUNT_DELETED=0";

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

		String selectQuery = "select STAFF_ID,LOGIN_ID,USER_NAME,LAST_LOGIN_TIME from ETP.ETP_USER where IS_WESTPORTS_USER=1 AND STAFF_ID IS NOT NULL AND "
				+ "STAFF_ID in (" + EMP_ID + ") AND ACCOUNT_DELETED=0";

		try {
			ResultSet rs = executeQuery(selectQuery);
			boolean action = update(EMP_ID);
			while (rs.next()) {
				int STAFF_ID = rs.getInt("STAFF_ID");
				String UserName = rs.getString("USER_NAME");
				String empidstr = Integer.toString(STAFF_ID);
				Date LAST_LOGIN_TIME = rs.getDate("LAST_LOGIN_TIME");
				String LastloginID = rs.getString("LOGIN_ID");
				String lastLogin = LAST_LOGIN_TIME == null ? "" : FormatDate.dateFormat.format(LAST_LOGIN_TIME);
				//String result = empidstr + "," + UserName + ",CBAS" + ",Admin," + Tdate + "," + action;
				String result = ""+","+""+",Termination,"+LastloginID+","+empidstr + "," + UserName +","+lastLogin+",ETP" + ",System," + Tdate + "," + action+","+"";
				System.out.println(result);
				list.add(result);

			}

			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (list.size() >= 1) {
			System.out.println("ID match in ETP");
			deactFile.writeDeacList(list, filename);
		} else {
			System.out.println("No Activation List for ETP");
		}
		conn.close();
	}

}
