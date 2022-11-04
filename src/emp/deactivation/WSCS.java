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

public class WSCS {

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
	
		

	public static boolean update(String EMP_ID) throws Exception {
		
		boolean action = false;

		String updateQuery = "UPDATE WSCS.WSCS_USER SET DATE_EXPIRY=SYSDATE WHERE STAFF_NO in(" + EMP_ID + ")";

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

		String selectQuery = "SELECT STAFF_NO,USER_ID from WSCS.WSCS_USER where STAFF_NO in (" + EMP_ID + ")";

		try {
			ResultSet rs = executeQuery(selectQuery);
			boolean action = update(EMP_ID);
			while (rs.next()) {
				int STAFF_ID = rs.getInt("STAFF_NO");
				String UserName = rs.getString("USER_ID");
				String empidstr = Integer.toString(STAFF_ID);
//				String result = empidstr + "," + UserName + ",WSCS" + ",Admin," + Tdate + "," + action;
				String result = ""+","+""+","+"Termination,"+UserName+","+empidstr + "," + UserName +",NA"+",WSCS" + ",System," + Tdate + "," + action+","+"";
				System.out.println(result);
				list.add(result);

			}

			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (list.size() >= 1) {
			System.out.println("ID match in WSCS");
			deactFile.writeDeacList(list, filename);
		} else {
			System.out.println("No Activation List for WSCS");
		}
		conn.close();
	}

}
