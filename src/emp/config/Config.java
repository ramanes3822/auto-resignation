package emp.config;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

public class Config {
	
	public static Properties properties() throws Exception {
		
		String currentDir = System.getProperty("user.dir");

		//File propertiesFile = new File("C:\\Users\\Ramanes\\Documents\\ResignList\\db.properties.txt");
		File propertiesFile = new File(currentDir+"/config.txt");
		FileReader reader = new FileReader(propertiesFile);
		Properties p = new Properties();
		p.load(reader);
		return p;
	}
	
	

}
