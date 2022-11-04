package emp.config;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

public class TotalResignationCsv {
	


	private BufferedWriter bwOutFile;
	

	public void writeCSVHeader(String filename) throws Exception {

		FileWriter cname = new FileWriter(filename);
		bwOutFile = new BufferedWriter(cname);
		StringBuffer sbOutput = new StringBuffer();

		sbOutput.append("EMP_ID,");
		sbOutput.append("EMP_NAME,");
		sbOutput.append("STATUS,");
		sbOutput.append("IC_NEW,");
		sbOutput.append("IC_OLD,");
		sbOutput.append("PASSPORT_NO,");
		sbOutput.append("DATE_OF_BIRTH,");
		sbOutput.append("EMP_REMARK,");
		sbOutput.append("DATE_JOIN,");
		sbOutput.append("DATE_CONFIRM,");
		sbOutput.append("DATE_EXIT,");
		sbOutput.append("EXIT_TYPE_CODE,");
		sbOutput.append("PROBATION_IN_MONTH,");
		sbOutput.append("SALARY,");
		sbOutput.append("EPF_NO,");
		sbOutput.append("INCOME_TAX_NO,");
		sbOutput.append("SOCSO_NO,");
		sbOutput.append("UPDATE_BY,");
		sbOutput.append("DATETIME_UPDATE,");
		sbOutput.append("ADD_BY,");
		sbOutput.append("DATETIME_ADD,");
		sbOutput.append("OLD_EMP_ID,");
		sbOutput.append("JOB_NAME,");
		sbOutput.append("DESIGNATION,");
		sbOutput.append("GRADE,");
		sbOutput.append("EMP_LEVEL,");
		sbOutput.append("EMP_TYPE,");
		sbOutput.append("SUB_DEPT,");
		sbOutput.append("DEPT,");
		sbOutput.append("EXIT_TYPE,");
		sbOutput.append("SAP_STATUS,");
		sbOutput.append("PANEL_CLINIC1,");
		sbOutput.append("PANEL_CLINIC2,");
		sbOutput.append("DEP_PANEL_CLINIC1,");
		sbOutput.append("DEP_PANEL_CLINIC2,");
		sbOutput.append("DEP_PANEL_CLINIC3");
		bwOutFile.append((CharSequence) sbOutput);
		bwOutFile.append((CharSequence) System.getProperty("line.separator"));

	}

    public void writeTotalResignation(List<String> records,String filename) throws Exception {
    	
    writeCSVHeader(filename);

		for (String r : records) {
			bwOutFile.append((CharSequence) r);
			bwOutFile.append((CharSequence) System.getProperty("line.separator"));
			bwOutFile.flush();
		}

	}

}
