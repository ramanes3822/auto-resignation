package emp.config;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import emp.config.Config;

public class SendMail {

	static Properties props = new Properties();

	final static String username = "";
	final static String password = "";
	static String to="";
	static String cc="";

	static Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, password);
		}
	});

	static Message message = new MimeMessage(session);

	public static void emailConfig() throws Exception {

		Properties p = Config.properties();

		String from = p.getProperty("from");
		String to = p.getProperty("to");
		String cc = p.getProperty("cc");
		

		String host = p.getProperty("Host");

		props.put("mail.smtp.auth", "false");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "25");

		// Get the Session object.

		message.setFrom(new InternetAddress(from));

		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
		message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
	}
	
	
	public static void emailConfigForNoList() throws Exception {

		Properties p = Config.properties();

		String from = p.getProperty("from");
		String to = p.getProperty("toForNoList");
		String cc = p.getProperty("ccForNoList");
		

		String host = p.getProperty("Host");

		props.put("mail.smtp.auth", "false");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "25");

		// Get the Session object.

		message.setFrom(new InternetAddress(from));

		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
		message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
	}
	
	
	public static void sendDailyResignationList(String filename) throws Exception {
		Properties p = Config.properties();

		emailConfig();
		
		// Set Subject: header field
		message.setSubject(p.getProperty("sendDailyResignationList") + FormatDate.TdateMailfrmt);

		// Create the message part
		BodyPart messageBodyPart = new MimeBodyPart();

		// Now set the actual message
		messageBodyPart.setContent("Hi Team,<br><br>"
				+ "Enclosed is the daily resign list of an application user."
				+ "<BR>" + "This is an automated email. <B>Do not reply</B>" + "<br><br>" + "Thanks & Regards<BR>"
				+ "<H2 style=color:blue>Privasia</H2>", "text/html");

		// Create a multipar message
		Multipart multipart = new MimeMultipart();

		// Set text message part
		multipart.addBodyPart(messageBodyPart);

		// Part two is attachment
		messageBodyPart = new MimeBodyPart();
		String fileBodyName = FormatDate.ddMMyyyy + ".csv";
		DataSource source = new FileDataSource(filename);
		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(fileBodyName);
		multipart.addBodyPart(messageBodyPart);

		// Send the complete message parts
		message.setContent(multipart);

		// Send message
		Transport.send(message);

		System.out.println("Mail sent successfully to " + to);

	}

	public static void sendDeactivationMail(String filename) throws Exception {
		Properties p = Config.properties();
		emailConfig();
		// Set Subject: header field
		message.setSubject(p.getProperty("sendDeactivationMail")+ FormatDate.TdateMailfrmt);

		// Create the message part
		BodyPart messageBodyPart = new MimeBodyPart();

		// Now set the actual message
		messageBodyPart.setContent("Hi Team,<br><br>"
				+ "Enclosed is the list of application user access which has been deactivated based on resigned employee id."
				+ "<BR>" + "This is an automated email. <B>Do not reply</B>" + "<br><br>" + "Thanks & Regards<BR>"
				+ "<H2 style=color:blue>Privasia</H2>", "text/html");

		// Create a multipar message
		Multipart multipart = new MimeMultipart();

		// Set text message part
		multipart.addBodyPart(messageBodyPart);

		// Part two is attachment
		messageBodyPart = new MimeBodyPart();
		String fileBodyName = FormatDate.ddMMyyyy + ".csv";
		DataSource source = new FileDataSource(filename);
		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(fileBodyName);
		multipart.addBodyPart(messageBodyPart);

		// Send the complete message parts
		message.setContent(multipart);

		// Send message
		Transport.send(message);

		System.out.println("Mail sent successfully to " + to);

	}

	public static void sendNoResignationlist() throws Exception {
		Properties p = Config.properties();
		
		emailConfigForNoList();
		// Set Subject: header field
		message.setSubject(p.getProperty("sendNoResignationlist")+ FormatDate.TdateMailfrmt);

		// Create the message part
		BodyPart messageBodyPart = new MimeBodyPart();

		// Now set the actual message
		messageBodyPart.setContent("Hi Team,<br><br>"
				+ "No resignation list found for today."
				+ "<BR>" + "This is an automated email. <B>Do not reply</B>" + "<br><br>" + "Thanks & Regards<BR>"
				+ "<H2 style=color:blue>Privasia</H2>", "text/html");

		// Create a multipar message
		Multipart multipart = new MimeMultipart();

		// Set text message part
		multipart.addBodyPart(messageBodyPart);

		// Send the complete message parts
		message.setContent(multipart);

		// Send message
		Transport.send(message);

		System.out.println("Mail sent successfully to " + to);
	}
	
	
public static void noMatchfordeactivation() throws Exception {
	
	     Properties p = Config.properties();
		
	     emailConfigForNoList();
		// Set Subject: header field
		message.setSubject(p.getProperty("noMatchfordeactivation")+ FormatDate.TdateMailfrmt);

		// Create the message part
		BodyPart messageBodyPart = new MimeBodyPart();

		// Now set the actual message
		messageBodyPart.setContent("Hi Team,<br><br>"
				+ "Employee id not found in any application."
				+ "<BR>" + "This is an automated email. <B>Do not reply</B>" + "<br><br>" + "Thanks & Regards<BR>"
				+ "<H2 style=color:blue>Privasia</H2>", "text/html");

		// Create a multipar message
		Multipart multipart = new MimeMultipart();

		// Set text message part
		multipart.addBodyPart(messageBodyPart);

		// Send the complete message parts
		message.setContent(multipart);

		// Send message
		Transport.send(message);

		System.out.println("Mail sent successfully to " + to);
	}

}