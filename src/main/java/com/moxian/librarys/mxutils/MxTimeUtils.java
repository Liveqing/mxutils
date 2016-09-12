package com.moxian.librarys.mxutils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间转换工具类
 * 
 * @author moxian-hhj
 * 
 */
public class MxTimeUtils {

	public static final long UNIT_MS_DAY = 24 * 60 * 60 * 1000;

	/**
	 * TimeFormatType 时间格式
	 */
	public enum TimeFormatType {
		/** yyyy-MM-dd HH:mm:ss */
		TIME_FOEMAT_STANDARD,
		/** yy-MM-dd HH:mm:ss */
		TIME_FOEMAT_NORMAL,
		/** yyyy-MM-dd */
		TIME_FOEMAT_Y_M_D,
		/** yyyy */
		TIME_FOEMAT_Y_Y,
		/** yy-MM */
		TIME_FOEMAT_Y_M,
		/** MM-dd */
		TIME_FOEMAT_M_D,
		/** HH:mm:ss */
		TIME_FOEMAT_H_M_S,
		/** HH:mm */
		TIME_FOEMAT_H_M,
		/** mm:ss */
		TIME_FOEMAT_M_S,
		/** yyyy/MM/dd HH:mm */
		TIME_FOEMAT_NOT_S,
		/** yyyy/MM/dd */
		TIME_FOEMAT_NOT_TIME,
		/** yyyy/MM/dd HH:mm:ss */
		TIME_FOEMAT,
		/** yyyy/MM */
		Y_M,
		/** dd */
		D,
		/** E */
		E
	}

	/**
	 * 获取时间格式
	 * 
	 * @param formatType
	 * @return String
	 */
	private static String getTimeFormat(TimeFormatType formatType) {
		String formatStr = "";
		switch (formatType) {
		case TIME_FOEMAT_STANDARD:
			formatStr = "yyyy-MM-dd HH:mm:ss";
			break;
		case TIME_FOEMAT_NORMAL:
			formatStr = "yy-MM-dd HH:mm:ss";
			break;
		case TIME_FOEMAT_Y_M_D:
			formatStr = "yyyy-MM-dd";
			break;
		case TIME_FOEMAT_Y_Y :
			formatStr = "yyyy";
			break;
		case TIME_FOEMAT_Y_M:
			formatStr = "yyyy-MM";
			break;
		case TIME_FOEMAT_M_D:
			formatStr = "MM-dd";
			break;
		case TIME_FOEMAT_H_M_S:
			formatStr = "HH:mm:ss";
			break;
		case TIME_FOEMAT_H_M:
			formatStr = "HH:mm";
			break;
		case TIME_FOEMAT_M_S:
			formatStr = "mm:ss";
			break;
		case TIME_FOEMAT_NOT_S:
			formatStr = "yyyy/MM/dd HH:mm";
			break;
		case TIME_FOEMAT_NOT_TIME:
			formatStr = "yyyy/MM/dd";
			break;
		case TIME_FOEMAT:
			formatStr = "yyyy/MM/dd HH:mm:ss";
			break;
		case Y_M:
			formatStr = "yyyy/MM";
			break;
		case D:
			formatStr = "dd";
			break;
		case E:
			formatStr = "E";
			break;
		}
		return formatStr;
	}

	/**
	 * 时间格式转换
	 * 
	 * @param time
	 *            标准时间格式yyyy-MM-dd HH:mm:ss
	 * @param formatType
	 *            时间格式类型
	 * @return 返回指定的时间格式
	 */
	public static String timeFormatStandardToSimple(String time,
													TimeFormatType formatType) {
		SimpleDateFormat f = new SimpleDateFormat(
				getTimeFormat(TimeFormatType.TIME_FOEMAT_STANDARD));
		SimpleDateFormat formater = new SimpleDateFormat(
				getTimeFormat(formatType));
		Date date = null;
		try {
			date = f.parse(time);
			return formater.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 *  时间格式转换
	* @Title: timeFormatStandardToSimple 
	* @param: Date mDate,
	* 		  TimeFormatType formatType
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @return String
	 */
	public static String timeFormatStandardToSimple(Date mDate, TimeFormatType formatType)
	{
		if (null == mDate) {
			return "";
		}
		SimpleDateFormat formater = new SimpleDateFormat(
				getTimeFormat(formatType));
		return formater.format(mDate);
	}
	
	/**
	 * 时间戳转换指定格式的时间 需要HH:mm格式，需要将timestamp/1000
	 * 
	 * @param timestamp
	 *            单位秒
	 * @param formatType
	 *            时间格式类型
	 * @return 返回指定的时间格式
	 */
	public static String timeFormatStandardToSimple(long timestamp,
													TimeFormatType formatType) {
		SimpleDateFormat f = new SimpleDateFormat(getTimeFormat(formatType));

		String dateString = f.format(new Date(timestamp * 1000));
		return dateString;
	}

	public static String timeFormatStandardToSimple(long timestamp,
													String pattern) {
		SimpleDateFormat f = new SimpleDateFormat(pattern);
		String dateString = f.format(new Date(timestamp * 1000));
		return dateString;
	}
	

	/**
	 * 获取当前时间
	 * 
	 * @param formatType
	 *            时间格式类型
	 * @return 返回指定的时间格式
	 */
	public static String getCurrentTime(TimeFormatType formatType) {
		SimpleDateFormat f = new SimpleDateFormat(getTimeFormat(formatType));
		return f.format(new Date(System.currentTimeMillis()));
	}

	/**
	 * @param time
	 *            标准时间格式yyyy-MM-dd HH:mm:ss
	 * @return 与当前时间差的一个描述(比如:刚刚，几秒前，几分钟前，...)
	 */
	public static String getLastUpdateTimeDesc(String time) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				getTimeFormat(TimeFormatType.TIME_FOEMAT_STANDARD));
		try {
			String desc = "";
			Date d = simpleDateFormat.parse(time);
			Date n = new Date();
			long delay = n.getTime() - d.getTime();
			long secondsOfHour = 60 * 60;
			long secondsOfDay = secondsOfHour * 24;
			long secondsOfTwoDay = secondsOfDay * 2;
			long secondsOfThreeDay = secondsOfDay * 3;
			// 相差的秒数
			long delaySeconds = delay / 1000;
			if (delaySeconds < 10) {
				desc = "刚刚";
			} else if (delaySeconds <= 60) {
				desc = delaySeconds + "秒前";
			} else if (delaySeconds < secondsOfHour) {
				desc = (delaySeconds / 60) + "分前";
			} else if (delaySeconds < secondsOfDay) {
				desc = (delaySeconds / 60 / 60) + "小时前";
			} else if (delaySeconds < secondsOfTwoDay) {
				desc = "一天前";

			} else if (delaySeconds < secondsOfThreeDay) {
				desc = "两天前";

			}
			return desc;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}


	/**
	 * 获取月份有多少天数
	 * 
	 * @param year
	 * @param month
	 *            Java月份从0开始算
	 * @return int
	 */
	public static int getActualMaximum(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);// Java月份从0开始算
		return cal.getActualMaximum(Calendar.DATE);
	}

	/**
	 * 获取当前日期
	 * 
	 * @return int[]
	 */
	public static int[] getCurrentDate() {
		int[] array = new int[3];
		// 获取系统时间
		Calendar calendar = Calendar.getInstance();
		array[0] = calendar.get(Calendar.YEAR);
		array[1] = calendar.get(Calendar.MONTH);
		array[2] = calendar.get(Calendar.DAY_OF_MONTH);
		return array;
	}

	/**
	 * 得到几天前的时间
	 * 
	 * @param d
	 * @param day
	 * @param format
	 * @return String
	 */
	public static String getDateBefore(Date d, int day, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format); // 格式化日期
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		String beforeDate = sdf.format(now.getTime());
		return beforeDate;
	}

	/**
	 * 得到几天后的时间
	 * 
	 * @param d
	 * @param day
	 * @param format
	 * @return String
	 */
	public static String getDateAfter(Date d, int day, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format); // 格式化日期
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		String afterDate = sdf.format(now.getTime());
		return afterDate;
	}

	/**
	 * 两个时间对比
	 * 
	 * @param DATE1
	 * @param DATE2
	 * @return
	 */
	public static int compare_date(String DATE1, String DATE2) {

		DateFormat df = new SimpleDateFormat(
				);
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				//dt1 在dt2前
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				//dt1在dt2后
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 两个时间的月份是否一致
	 *
	 * @param Date1
	 * @param Date2
	 * @return
	 */
	public static boolean compareDateMonth(String Date1, String Date2){
        boolean result = false;
        SimpleDateFormat sdf = new SimpleDateFormat(getTimeFormat(TimeFormatType.TIME_FOEMAT_Y_M));//格式化为年月
        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();
        try {
            date1.setTime(sdf.parse(Date1));
            date2.setTime(sdf.parse(Date2));
            if (date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH)){
                result = true;
                return result;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

		return result;
	}

    /**
     * 获取时间的月份
     * @param Date
     * @return
     * @throws ParseException
     */
    public static int getMonthDate(String Date){
        int month = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(getTimeFormat(TimeFormatType.TIME_FOEMAT_Y_M));//格式化为年月
        Calendar date1 = Calendar.getInstance();
        try {
            date1.setTime(sdf.parse(Date));
            month = date1.get(Calendar.MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return month + 1;
    }

	/**
	 * 与当前系统时间对比
	 * 
	 * @param DATE1
	 * @param mFormatType
	 * @return -1 1 0
	 */
	@SuppressLint("SimpleDateFormat")
	public static int compareDate(String DATE1, TimeFormatType mFormatType) {//PswWrongLimitTimeCtrl.startLimitTime()按照这个方法改
		DateFormat df = new SimpleDateFormat(
				getTimeFormat(mFormatType));
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = new Date();
			// System.out.println("dt1.getTime()="+dt1.getTime() +
			// "dt2.getTime()=" + dt2.getTime());
			if (dt1.getTime() > dt2.getTime()) {
				return -1;

			} else if (dt1.getTime() < dt2.getTime()) {
				return 1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	@SuppressLint("SimpleDateFormat")
	public static long conversionDateToLong(String dateStr, TimeFormatType type) {
		SimpleDateFormat sdf = new SimpleDateFormat(getTimeFormat(type));
		Date date = null;
		try {
			date = sdf.parse(dateStr.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (date != null) {
			return date.getTime();
		} else {
			return 0;
		}
	}
	
	public static long conversionServiceTimeToLong(String time){
		SimpleDateFormat sdf = new SimpleDateFormat(getTimeFormat(TimeFormatType.TIME_FOEMAT_STANDARD));
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (date != null) {
			return date.getTime();
		} else {
			return 0;
		}
	}
}