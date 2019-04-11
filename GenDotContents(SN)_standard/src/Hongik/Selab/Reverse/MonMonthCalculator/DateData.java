package Hongik.Selab.Reverse.MonMonthCalculator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class DateData {
	private String strStartDate;
	private String strEndDate;
	private int[] startDate = new int[3];
	private int[] endDate = new int[3];
	public DateData(String start, String end)
	{
		strStartDate = start;
		strEndDate = end;

		makeData(strStartDate, startDate);
		makeData(strEndDate, endDate);
	}
	private void makeData(String strDate,int[] date)
	{
		String[] startList = strDate.split("-");
		if(startList.length == 3)
		{
			for(int i=0; i < 3; i++)
			{
				//System.out.println(date[i]+startList[i]);
				date[i] = Integer.parseInt(startList[i]);
			}
		}
	}
	public String getStartDate()
	{
		return strStartDate;
	}
	
	public String getStartmonth()
	{
		return startDate[0]+"-"+startDate[1];
	}
	
	public String getEndDate()
	{
		return strEndDate;
	}
	public int	getStartYear()
	{
		return startDate[0];
	}
	public int getStartMonth()
	{
		return startDate[1];
	}
	public int getStartDay()
	{
		return startDate[2];
	}
	public int	getEndYear()
	{
		return endDate[0];
	}
	public int getEndMonth()
	{
		return endDate[1];
	}
	public int getEndDay()
	{
		return endDate[2];
	}


	public long getStartTime()
	{
		return DateUtil.getTime(strStartDate);
	}
	public long getEndTime()
	{
		return DateUtil.getTime(strEndDate);
	}
	public double getManMonth()
	{
		//System.out.println( getMaximumDay(getStartYear(), getStartMonth()) );
		return (double)getConuntDay()/(double)DateUtil.getMaximumDay(getStartYear(), getStartMonth());
	}
	public int getConuntDay()
	{
		//Date date1 = new Date(startDate[0], startDate[1], startDate[2]);
		//Date date2 = new Date(endDate[0], endDate[1], endDate[2]);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	    Date date1;
	    Date date2;
	    long diffDays = 0;
		try {
			date1 = formatter.parse(strStartDate);
			date2 = formatter.parse(strEndDate);
			long diff = date2.getTime() - date1.getTime();
			diffDays = diff / (24 * 60 * 60 * 1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return (int) diffDays+1;
	}
	public int getConuntMonth()
	{
		//Date date1 = new Date(startDate[0], startDate[1], startDate[2]);
		//Date date2 = new Date(endDate[0], endDate[1], endDate[2]);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	    Date date1;
	    Date date2;
	    int year = 0;
	    int month = 0;
	    long diffDays = 0;
		try {
			date1 = formatter.parse(strStartDate);
			date2 = formatter.parse(strEndDate);

			year = date2.getYear() - date1.getYear();
			month = date2.getMonth() - date1.getMonth() + 1;
			//System.out.println("m:"+month);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return month + year*12;
	}
}
