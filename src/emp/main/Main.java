package emp.main;

import java.text.ParseException;
import java.util.*;

import oracle.jdbc.pool.OracleDataSource;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;

import emp.config.Config;
import emp.config.FormatDate;
import emp.config.SendMail;
import emp.config.TotalResignationCsv;

public class Main {

	static Connection conn;

	public static void getDBConnection(String url, String username, String password) throws Exception {

		OracleDataSource ds;
		ds = new OracleDataSource();
		ds.setURL(url);
		conn = ds.getConnection(username, password);

	}

	public static void dailyResignation() throws Exception {

		TotalResignationCsv writeCsv = new TotalResignationCsv();
		Properties p = Config.properties();

		getDBConnection(p.getProperty("hrmsUrl"), p.getProperty("hrmsUname"), p.getProperty("hrmsPass"));

		List<String> list1 = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>();

		String hrmsQuery = "select * from HRM_EMP where date_exit is not null and DATETIME_UPDATE >= to_timestamp('"
				+ FormatDate.YDate + "','dd-MM-YYYY HH24:MI:ss')";
		
//		String hrmsQuery = "select * from HRM_EMP where date_exit is not null and DATETIME_UPDATE >= to_timestamp('"
//				+ FormatDate.YDate + "','dd-MM-YYYY HH24:MI:ss') AND DATE_EXIT <= to_timestamp('" + FormatDate.Tdate
//				+ "','dd-MM-YYYY HH24:MI:ss')";

		System.out.println("**************Starting Resignation script********************" + FormatDate.Tdate);

		String filename = p.getProperty("dailyResignOutputFile") + "deactList_" + FormatDate.ddMMyyyy + ".csv";
		String deactFile = p.getProperty("deactOutputFile") + "deactList_" + FormatDate.ddMMyyyy + ".csv";

		writeCsv.writeCSVHeader(filename);
		try {

			Statement st = conn.createStatement();

			ResultSet rs = st.executeQuery(hrmsQuery);

			while (rs.next()) {
				int empid = rs.getInt("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				String status = rs.getString("STATUS");
				String Icnew = rs.getString("IC_NEW");
				String icold = rs.getString("IC_OLD");
				String passno = rs.getString("PASSPORT_NO");
				Date dob = rs.getDate("DATE_OF_BIRTH");
				String emremark = rs.getString("EMP_REMARK");
				Date dateJoin = rs.getDate("DATE_JOIN");
				Date DateConfirm = rs.getDate("DATE_CONFIRM");
				Date DATE_EXIT = rs.getDate("DATE_EXIT");
				String EXIT_TYPE_CODE = rs.getString("EXIT_TYPE_CODE");
				String PROBATION_IN_MONTH = rs.getString("PROBATION_IN_MONTH");
				String SALARY = rs.getString("SALARY");
				String EPF_NO = rs.getString("EPF_NO");
				String INCOME_TAX_NO = rs.getString("INCOME_TAX_NO");
				String SOCSO_NO = rs.getString("SOCSO_NO");
				int UPDATE_BY = rs.getInt("UPDATE_BY");
				Date DATETIME_UPDATE = rs.getDate("DATETIME_UPDATE");
				String ADD_BY = rs.getString("ADD_BY");
				Date DATETIME_ADD = rs.getDate("DATETIME_ADD");
				String OLD_EMP_ID = rs.getString("OLD_EMP_ID");
				String JOB_NAME = rs.getString("JOB_NAME");
				String DESIGNATION = rs.getString("DESIGNATION");
				String GRADE = rs.getString("GRADE");
				String EMP_LEVEL = rs.getString("EMP_LEVEL");
				String EMP_TYPE = rs.getString("EMP_TYPE");
				String SUB_DEPT = rs.getString("SUB_DEPT");
				String DEPT = rs.getString("DEPT");
				String EXIT_TYPE = rs.getString("EXIT_TYPE");
				String SAP_STATUS = rs.getString("SAP_STATUS");
				String PANEL_CLINIC1 = rs.getString("PANEL_CLINIC1");
				String PANEL_CLINIC2 = rs.getString("PANEL_CLINIC2");
				String DEP_PANEL_CLINIC1 = rs.getString("DEP_PANEL_CLINIC1");
				String DEP_PANEL_CLINIC2 = rs.getString("DEP_PANEL_CLINIC2");
				String DEP_PANEL_CLINIC3 = rs.getString("DEP_PANEL_CLINIC3");
				String dobstr = FormatDate.dateFormat.format(dob);
				String dateJoinstr = dateJoin == null ? "" : FormatDate.dateFormat.format(dateJoin);
				String DateConfirmstr = DateConfirm == null ? "" : FormatDate.dateFormat.format(DateConfirm);
				String DATETIME_UPDATEstr = FormatDate.dateFormat.format(DATETIME_UPDATE);
				String DATETIME_ADDstr = FormatDate.dateFormat.format(DATETIME_ADD);
				String empidstr = Integer.toString(empid);
				String UPDATE_BYstr = Integer.toString(UPDATE_BY);
				String result = empidstr + "," + empName + "," + status + "," + Icnew + "," + icold + "," + passno + ","
						+ dobstr + "," + emremark + "," + dateJoinstr + "," + DateConfirmstr + "," + DATE_EXIT + ","
						+ EXIT_TYPE_CODE + "," + PROBATION_IN_MONTH + "," + SALARY + "," + EPF_NO + "," + INCOME_TAX_NO
						+ "," + SOCSO_NO + "," + UPDATE_BYstr + "," + DATETIME_UPDATEstr + "," + ADD_BY + ","
						+ DATETIME_ADDstr + "," + OLD_EMP_ID + "," + JOB_NAME + "," + DESIGNATION + "," + GRADE + ","
						+ EMP_LEVEL + "," + EMP_TYPE + "," + SUB_DEPT + "," + DEPT + "," + EXIT_TYPE + "," + SAP_STATUS
						+ "," + PANEL_CLINIC1 + "," + PANEL_CLINIC2 + "," + DEP_PANEL_CLINIC1 + "," + DEP_PANEL_CLINIC2
						+ "," + DEP_PANEL_CLINIC3;

				System.out.println(result);

				list1.add(result);
				list2.add("'" + empidstr + "'");

			}

			if (list1.size() >= 1) {
				String empId = list2.toString().replace("[", "").replace("]", "").replace(" ", "");
				writeCsv.writeTotalResignation(list1, filename);
				System.out.println("Staff id to be deactivate " + list2);
				System.out.println("Sending Daily resignation list mail! to "+p.getProperty("to"));
				SendMail.sendDailyResignationList(filename);
				Deactivation.deactivationProcess(empId, deactFile);
				

			} else {
				System.out.println("No resignation List found!");
				SendMail.sendNoResignationlist();
			}
			conn.close();
		} catch (Exception e) {
			System.out.println("Unable to connect to database" + e.getMessage());
			e.printStackTrace();
		}

	}

	public static void main(final String[] args) throws ParseException, Exception {
		dailyResignation();
	}
}