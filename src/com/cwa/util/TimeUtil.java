package com.cwa.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class TimeUtil {
	public static long currentSystemTime() {
		return System.currentTimeMillis();
	}

	public static long getTodayStartTime() {
		Calendar currentDate = new GregorianCalendar();
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		return currentDate.getTime().getTime();
	}

	public static int getIntervalNum(int cdTime,int hour) {
		Calendar cal = Calendar.getInstance();
		TimeZone timeZone = cal.getTimeZone();
		long time = System.currentTimeMillis() + timeZone.getRawOffset()-hour*3600000;
		double dayNum = Math.ceil(time / ((cdTime * 1000)));
		return (int) dayNum;
	}

	
	public static int getIntervalNum(int cdTime, long time,int hour) {
		Calendar cal = Calendar.getInstance();
		TimeZone timeZone = cal.getTimeZone();
		long timeTemp = time + timeZone.getRawOffset()-hour*3600000;
		double dayNum = Math.ceil(timeTemp / ((cdTime * 1000)));
		return (int) dayNum;
	}

	
	public static void main(String[] args) {
		
		long t2=dataAndTimeChange("2015-2-10 18:50:01");
		
		int t=TimeUtil.getIntervalNum(3600,0);
		int t1=TimeUtil.getIntervalNum(3600,t2,0);
		System.out.println(t);
		System.out.println(t1);
		System.out.println(t2);
		
		
	}
	public static String getDate(String pattern, String id) {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setTimeZone(TimeZone.getTimeZone(id));
		String snow = sdf.format(now);
		return snow;
	}

	public static long dataChange(String data) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = sdf.parse(data, pos);
		return strtodate.getTime();
	}

	public static long dataAndTimeChange(String data) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = sdf.parse(data, pos);
		return strtodate.getTime();
	}

	public static int getServiceDate(int moth, int cdTime, String serviceCreatDate) {
		long currentTime = System.currentTimeMillis();
		long serviceCreatTime = TimeUtil.dataChange(serviceCreatDate);
		long dayNum = ((currentTime - serviceCreatTime) / (cdTime * 1000)) % moth;
		return (int) dayNum;
	}

	public static int getServiceMoth(int moth, int cdTime, String serviceCreatDate) {
		long currentTime = System.currentTimeMillis();
		long serviceCreatTime = TimeUtil.dataChange(serviceCreatDate);
		long dayNum = ((currentTime - serviceCreatTime) / (cdTime * 1000)) / moth;
		return (int) dayNum;
	}

	public static int getVersionId(String serviceCreatDate, int week, long time) {
		int versionId = 1;
		long currentTime = System.currentTimeMillis();
		long serviceCreatTime = TimeUtil.dataChange(serviceCreatDate);
		int weekDay = TimeUtil.getWeekOfDate(serviceCreatTime);
		int currentWeekDay = TimeUtil.getWeekOfDate(currentTime);
		int subDay = (week - weekDay) >= 0 ? (week - weekDay) : (week - weekDay + 7);
		long dayNum = ((currentTime - serviceCreatTime) / (86400 * 1000));
		versionId = (dayNum - subDay) >= 0 ? (int) ((dayNum - subDay) / 7) + 2 : 1;
		long currentDayTime = currentTime - TimeUtil.getTodayStartTime();
		if (currentWeekDay == week) {
			if (currentDayTime < time) {
				versionId -= 1;
			}
		}
		return (int) versionId;
	}

	public static int getWeekOfDate(long dateTime) {
		Calendar cal = Calendar.getInstance();
		Date date = new Date(dateTime);
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK);
		return w;
	}

	public static long getTaskTime(String strs) {
		String[] taskTimes = strs.split(" ");
		long taskTime = 0;
		taskTime = (Integer.parseInt(taskTimes[0]) + Integer.parseInt(taskTimes[1]) * 60 + Integer.parseInt(taskTimes[0]) * 3600) * 1000;
		return taskTime;
	}

	public static int getTaskWeek(String strs) {
		String[] taskTimes = strs.split(" ");
		return Integer.parseInt(taskTimes[5]);
	}

}
