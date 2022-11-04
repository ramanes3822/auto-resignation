package emp.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FormatDate {
	
	

	static DateFormat sdfFmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    static DateFormat ddMMyyyyFmt = new SimpleDateFormat("ddMMyyyy");
    public static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    
    static Calendar c = Calendar.getInstance();
    static Date now = c.getTime();
    
    static Date prevDate=calendarSet();
    
    public static String Tdate = sdfFmt.format(now);
    public static String TdateMailfrmt = dateFormat.format(now);
    public static String YDate=sdfFmt.format(prevDate);
    public static String ddMMyyyy=ddMMyyyyFmt.format(now);
    
    public static  Date calendarSet() {
    	
		c.add(Calendar.DATE, -1);
		c.add(Calendar.HOUR, 0);
		c.add(Calendar.MINUTE, 0);
		c.add(Calendar.SECOND, 0);
		
		return c.getTime();
    }

}
