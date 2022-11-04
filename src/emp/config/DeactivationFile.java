package emp.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class DeactivationFile {

	private BufferedWriter bwOutFile;

	public void writeCSVHeader(String filename) throws Exception {

		FileWriter cname = new FileWriter(filename);
		bwOutFile = new BufferedWriter(cname);
		StringBuffer sbOutput = new StringBuffer();
		
		sbOutput.append("Ticket No,");
		sbOutput.append("Request Received Date/Time,");
		sbOutput.append("Type,");
		sbOutput.append("Login ID,");
		sbOutput.append("EMP_ID,");
		sbOutput.append("EMP_NAME,");
		sbOutput.append("Last Login Date,");
		sbOutput.append("App_Name,");
		sbOutput.append("ModifiedBy,");
		sbOutput.append("ModifiedTime,");
		sbOutput.append("Updated");
		sbOutput.append("Proof of revocation(Yes/No)");
		bwOutFile.append((CharSequence) sbOutput);
		bwOutFile.append((CharSequence) System.getProperty("line.separator"));

	}

	public void writeDeacList(List<String> records, String filename) throws Exception {

		File file = new File(filename);

		if (file.exists()) {

			try (FileWriter fw = new FileWriter(filename, true);
					BufferedWriter bwOutFile = new BufferedWriter(fw);
					PrintWriter pwOutFile = new PrintWriter(bwOutFile);) {

				for (String r : records) {
					pwOutFile.append((CharSequence) r);
					pwOutFile.append((CharSequence) System.getProperty("line.separator"));
					pwOutFile.flush();

				}
				pwOutFile.close();

			} catch (IOException i) {
				i.printStackTrace();
			}

		} else {
			System.out.println("Creating new Deactivation file!");
			writeCSVHeader(filename);
			for (String r : records) {
				bwOutFile.append((CharSequence) r);
				bwOutFile.append((CharSequence) System.getProperty("line.separator"));
				bwOutFile.flush();

			}
			bwOutFile.close();

		}

	}

}
