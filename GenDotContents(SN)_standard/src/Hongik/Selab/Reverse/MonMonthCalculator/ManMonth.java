package Hongik.Selab.Reverse.MonMonthCalculator;

import java.util.ArrayList;
import java.util.List;


public class ManMonth {
	private List<DateData> dateList = null;
	private List<DateData> outDateList = new ArrayList<DateData>();
	private int projectMonths = 0;

	public ManMonth(String projectStartDate, String projectEndDate, List<DateData> dates)
	{
		DateData projectDate = new DateData(projectStartDate, projectEndDate);
		projectMonths = projectDate.getConuntMonth();

		//System.out.println(projectMonths);
		dateList = dates;

		int year = projectDate.getStartYear();
		int month = projectDate.getStartMonth();
		//System.out.println(year);
		for(int i=0; i < projectMonths; i++)
		{
			final int maximumDay = DateUtil.getMaximumDay(year, month);
			//System.out.println(year+":"+month+":"+maximumDay);
			//create
			int startDay = 32;
			int endDay = 0;
			for(DateData date : dateList)
			{
				//System.out.println(date.getStartDate());
				if( date.getEndTime() < DateUtil.getTime(year, month, 1) ||
						date.getStartTime() > DateUtil.getTime(year, month, maximumDay))
				{
					//startDay = 0;
					//endDay = 0;
				}
				else 
				{
					if(date.getStartTime() < DateUtil.getTime(year, month, 1))
					{
						startDay = 1;
						//System.out.println("s"+startDay);
					}
					if(date.getEndTime() > DateUtil.getTime(year, month, maximumDay))
					{
						endDay = maximumDay;
						//System.out.println("e"+endDay);
					}
					if(month == date.getStartMonth() && year == date.getStartYear() )
						startDay = Math.min(startDay, date.getStartDay());
					if(month == date.getEndMonth() && year == date.getEndYear())
						endDay = Math.max(endDay, date.getEndDay());
					//System.out.println(startDay+"~"+endDay);
				}
			}
			if(startDay == 32 || endDay == 0)
			{
				DateData newDate = new DateData(DateUtil.getStringDate(year, month, 1),
						DateUtil.getStringDate(year, month, 0));
				//System.out.println("out of scope: "+newDate.getManMonth() );
				outDateList.add(newDate);
			}
			else
			{
				DateData newDate = new DateData(DateUtil.getStringDate(year, month, startDay),
						DateUtil.getStringDate(year, month, endDay));
				//System.out.println(startDay+"~"+endDay+":"+newDate.getManMonth() );
				outDateList.add(newDate);
			}
			month++;
			if(month > 12)
			{
				year++;
				month = 1;
			}
		}

	}
	public List<DateData> getManMonth()
	{
		return outDateList;
	}
}
