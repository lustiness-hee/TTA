package Hongik.Selab.Reverse.MonMonthCalculator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {
	public static int getMaximumDay(int year, int month)
	{
		Calendar cal = Calendar.getInstance();
		cal.set ( year, month - 1, 1);
		return cal.getActualMaximum( Calendar.DATE );
	}

	public static long getTime(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    Date date = null;
		try {
			date = formatter.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime();
	}
	
	public static long getTime(int year, int month, int day) {
		return getTime(getStringDate(year, month, day));
	}
	
	public static String getStringDate(int year, int month, int day)
	{
		return year+"-"+month+"-"+day;
	}
}
