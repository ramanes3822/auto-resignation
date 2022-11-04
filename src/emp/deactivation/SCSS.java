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

public class SCSS {

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

	public static boolean update(String EMP_ID) {

		boolean action = false;

		String updateQuery = "UPDATE SCSS.SCSS_SYSUSER SET SYS_USERSTATUS='T', SYS_UPDATEDBY='1', "
				+ "SYS_DATEUPDATE=SYSDATE WHERE SYS_STAFFNO in(" + EMP_ID + ") AND SYS_USERSTATUS='A'";

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

//		String selectQuery = "SELECT SYS_STAFFNO,SYS_NAME FROM SCSS.SCSS_SYSUSER where SYS_STAFFNO in (" + EMP_ID
//				+ ") AND SYS_USERSTATUS='A'";
		
		String selectQuery = "SELECT SU.SYS_STAFFNO,SL.LGN_LOGINNAME,SU.SYS_NAME,SL.LAST_LOGIN_DATE FROM SCSS_SYSUSER SU,SCSS.SCSS_LOGIN SL WHERE SU.SYS_STAFFNO IN (" + EMP_ID
				+ ") AND SU.SYS_USERID_SEQ=SL.USR_USERID AND SU.SYS_USERSTATUS='A'";

		try {
			ResultSet rs = executeQuery(selectQuery);
			boolean action = update(EMP_ID);
			while (rs.next()) {
				int STAFF_ID = rs.getInt("SYS_STAFFNO");
				String UserName = rs.getString("SYS_NAME");
				String empidstr = Integer.toString(STAFF_ID);
				Date LAST_LOGIN_TIME = rs.getDate("LAST_LOGIN_TIME");
				String LastloginID = rs.getString("LGN_LOGINNAME");
				String lastLogin = LAST_LOGIN_TIME == null ? "" : FormatDate.dateFormat.format(LAST_LOGIN_TIME);
//				String result = empidstr + "," + UserName + ",SCSS" + ",Admin," + Tdate + "," + action;
				String result = ""+","+""+",Termination,"+LastloginID+","+empidstr + "," + UserName +","+lastLogin+",SCSS" + ",System," + Tdate + "," + action+","+"";
				System.out.println(result);
				list.add(result);

			}

			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (list.size() >= 1) {
			System.out.println("ID match in SCSS");
			deactFile.writeDeacList(list, filename);
		} else {
			System.out.println("No Activation List for SCSS");
		}
		conn.close();
	}

}
