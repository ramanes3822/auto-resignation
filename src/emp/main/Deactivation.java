package emp.main;

import java.io.File;

import emp.config.FormatDate;
import emp.config.SendMail;
import emp.deactivation.CBAS;
import emp.deactivation.ECCS;
import emp.deactivation.ETP;
import emp.deactivation.HRMS;
import emp.deactivation.SCSS;
import emp.deactivation.WDC;
import emp.deactivation.WSCS;

public class Deactivation {

	public static void deactivationProcess(String id, String filename) throws Exception {

		System.out.println("*****************Starting deactivation process ****************");
		System.out.println("Time: " + FormatDate.Tdate);

		System.out.println("Reading Staff id");
		System.out.println(id);
		
		ETP.deactivate(id, filename);
		CBAS.deactivate(id, filename);
		SCSS.deactivate(id, filename);
		WSCS.deactivate(id, filename);
		WDC.deactivate(id, filename);
		HRMS.deactivate(id, filename);
		ECCS.deactivate(id, filename);

		File file = new File(filename);
		if (file.exists()) {
			System.out.println("sending file " + "deactList_" + FormatDate.ddMMyyyy + ".csv");
			SendMail.sendDeactivationMail(filename);

		} else {
			System.out.println("No Emp ID's were match for the deactivation process!");
			SendMail.noMatchfordeactivation();
		}

		System.out.println("*****************End****************");

	}

}
