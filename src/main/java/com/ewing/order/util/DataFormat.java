package com.ewing.order.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataFormat {
	protected static final String sDefaultDateFormat = "yyyy-MM-dd HH:mm:ss";

	// 日期时间全格式 24小时制
	public static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static String DATETIME_FORMAT2 = "yyyyMMddHHmmss";

	public static String DATE_ZEROTIME_FORMAT = "yyyy-MM-dd 00:00:00";

	public static String DATE_ZEROTIME_FORMAT2 = "yyyyMMdd000000";

	public static String DATE_FULLTIME_FORMAT = "yyyy-MM-dd 23:59:59";

	public static String DATE_FULLTIME_FORMAT2 = "yyyyMMdd235959";

	// 日期时间全格式 12小时制
	public static String DATETIME12_FORMAT = "yyyy-MM-dd hh:mm:ss";

	public static String DATETIME12_FORMAT2 = "yyyyMMddhhmmss";

	// 日期全格式
	public static String DATE_FORMAT = "yyyy-MM-dd";

	public static String DATE_FORMAT2 = "yyyyMMdd";

	public static String DATE_FORMAT_CN = "yyyy年MM月dd日";

	// 年月
	public static String YEAR_MONTH_FORMAT = "yyyy-MM";

	public static String YEAR_MONTH_FORMAT2 = "yyyyMM";

	public static String YEAR_MONTH_FORMAT_CN = "yyyy年MM月";

	// 某年某月的第一天
	public static String YEAR_MONTH_FIRSTDAY = "yyyy-MM-01";

	// 年、月、日
	public static String YEAR_FORMAT = "yyyy";

	public static String MONTH_FORMAT = "MM";

	public static String DAY_FORMAT = "dd";

	// 时间全格式 24小时制
	public static String TIME_FORMAT = "HH:mm:ss";

	public static String TIME_FORMAT2 = "HHmmss";

	// 时间全格式 12小时制
	public static String TIME12_FORMAT = "hh:mm:ss";

	public static String TIME12_FORMAT2 = "hhmmss";

	// 营业cs版本日期时间格式
	public static String DATETIME_SLASH_FORMAT = "yyyy/MM/dd HH:mm:ss";

	public static String DATE_SLASH_FORMAT = "yyyy/MM/dd";

	private static final Logger log = LoggerFactory.getLogger(DataFormat.class);

	public DataFormat() {
	}

	public static String cvtDateString(String strDate, String strOldFormat, String strNewFormat) {
		try {
			SimpleDateFormat sdfOld = new SimpleDateFormat(strOldFormat);
			SimpleDateFormat sdfNew = new SimpleDateFormat(strNewFormat);
			Date date = sdfOld.parse(strDate);
			return sdfNew.format(date);
		} catch (Exception ex) {
			return "";
		}

	}

	public static Date stringToDate(String inputDate, String pattern) {
		if (inputDate == null || inputDate.equalsIgnoreCase(""))
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = sdf.parse(inputDate, new ParsePosition(0));
		} catch (Exception ex) {
			SimpleDateFormat sdfs = new SimpleDateFormat(DATE_SLASH_FORMAT);
			date = sdfs.parse(inputDate, new ParsePosition(0));
			return date;
		}
		return date;
	}

	public static Date stringToDate(String inputDate) {
		if (inputDate == null || inputDate.equalsIgnoreCase(""))
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Date date = null;
		try {
			date = sdf.parse(inputDate, new ParsePosition(0));
		} catch (Exception ex) {
			SimpleDateFormat sdfs = new SimpleDateFormat(DATE_SLASH_FORMAT);
			date = sdfs.parse(inputDate, new ParsePosition(0));
			return date;
		}
		return date;
	}

	public static String DateToString(java.util.Date time) {
		return time.toString();
	}

	public static String DateToString(java.util.Date time, String strPattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(strPattern);
		return sdf.format(time);
	}

	/**
	 * 根据yyyy-mm格式的月份字符串得到yyyy-mm-dd|yyyy-mm-dd格式的月开始日期和月结束日期字符串。
	 * 
	 * @param strMon
	 *            String
	 * @return String
	 */
	public static String monToStartEndDate(String strMon) {
		if (strMon == null || strMon.length() != 7 || strMon.charAt(4) != '-')
			return "";

		int iYear = 0;
		int iMonth = 0;

		try {
			iYear = Integer.parseInt(strMon.substring(0, 4));
			iMonth = Integer.parseInt(strMon.substring(5, 7));
		} catch (Exception exp) {
			return "";
		}
		if (iYear < 1970 || iYear > 2999 || iMonth < 1 || iMonth > 12)
			return "";

		String strStartDate = strMon + "-01";
		String strNextMonStartDate = new String();

		if (iMonth == 12)
			strNextMonStartDate = "" + (iYear + 1) + "-01-01";
		else {
			String strNextMon = new String();
			if (10 > (iMonth + 1))
				strNextMon = "0" + (iMonth + 1);
			else
				strNextMon = "" + (iMonth + 1);
			strNextMonStartDate = strMon.substring(0, 4) + "-" + strNextMon + "-01";
		}

		String strEndDate = DateToString(
				new java.sql.Date((stringToDate(strNextMonStartDate)).getTime() - 86400000));

		return strStartDate + "|" + strEndDate;

	}

	/**
	 * <p>
	 * 判断日期格式是否合法
	 * </p>
	 * 
	 * @param s
	 * @param cTimeFormat
	 * @return
	 */
	public static boolean isValidDate(String s, java.text.SimpleDateFormat cTimeFormat) {
		try {
			if (cTimeFormat == null) {
				cTimeFormat = new java.text.SimpleDateFormat(sDefaultDateFormat);
			}
			cTimeFormat.parse(s);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 返回两个时间相差的月份
	 * 
	 * @param g1
	 * @param g2
	 * @return
	 */
	public static int getDiffMonths(Calendar g1, Calendar g2) {
		int elapsed = 0;
		GregorianCalendar gc1, gc2;
		if (g2.after(g1)) {
			gc2 = (GregorianCalendar) g2.clone();
			gc1 = (GregorianCalendar) g1.clone();
		} else {
			gc2 = (GregorianCalendar) g1.clone();
			gc1 = (GregorianCalendar) g2.clone();
		}

		gc1.clear(Calendar.MILLISECOND);
		gc1.clear(Calendar.SECOND);
		gc1.clear(Calendar.MINUTE);
		gc1.clear(Calendar.HOUR_OF_DAY);
		gc1.clear(Calendar.DATE);
		gc2.clear(Calendar.MILLISECOND);
		gc2.clear(Calendar.SECOND);
		gc2.clear(Calendar.MINUTE);
		gc2.clear(Calendar.HOUR_OF_DAY);
		gc2.clear(Calendar.DATE);

		while (gc1.before(gc2)) {
			gc1.add(Calendar.MONTH, 1);
			elapsed++;
		}
		return elapsed;
	}

	/**
	 * 返回两个时间相差的小时
	 * 
	 * @param g1
	 * @param g2
	 * @return
	 * @throws ParseException
	 */
	public static int getDiffHour(String g1, String g2) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(DATETIME_FORMAT);
		Date fromDate2 = formatter.parse(g1);
		Date toDate2 = formatter.parse(g2);
		long from2 = fromDate2.getTime();
		long to2 = toDate2.getTime();
		int hours = (int) ((to2 - from2) / (1000 * 60 * 60));
		return hours;
	}

	/**
	 * 返回两个时间相差的小时
	 * 
	 * @param g1
	 * @return
	 * @throws ParseException
	 */
	public static int getDiffHourWithCurrent(String g1) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATETIME_FORMAT);
		int hours = 0;
		try {
			Date fromDate2 = formatter.parse(g1);
			Date toDate2 = new Date();
			long from2 = fromDate2.getTime();
			long to2 = toDate2.getTime();
			hours = (int) ((to2 - from2) / (1000 * 60 * 60));
		} catch (Exception e) {
			log.error("转换日期字符串格式时出错;" + e.getMessage());
		}
		return hours;
	}
	
	public static String addHour(String g1,int hour) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATETIME_FORMAT);
		String result = "";
		try {
			Date fromDate = formatter.parse(g1);
			Calendar cal = Calendar.getInstance();
			cal.setTime(fromDate);
			cal.add(Calendar.HOUR, hour);
			result = formatter.format(cal.getTime());
		} catch (Exception e) {
			log.error("转换日期字符串格式时出错;" + e.getMessage());
		}
		return result;
	}

 

	/**
	 * 把Calendar类型的日期转换成指定格式的字符串。
	 * 
	 * @param calendar
	 * @param strFormatTo
	 * @return
	 */
	public static String cvtFormattedCalendar(Calendar calendar, String strFormatTo) {
		if (calendar == null) {
			return "";
		}
		strFormatTo = strFormatTo.replace('/', '-');
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
			if (Integer.parseInt(formatter.format(calendar.getTime())) < 1900) {
				return "";
			} else {
				formatter = new SimpleDateFormat(strFormatTo);
				return formatter.format(calendar.getTime());
			}
		} catch (Exception e) {
			log.error("转换日期字符串格式时出错;" + e.getMessage());
			return "";
		}
	}

	/**
	 * 获得目前的时间
	 * 
	 * @return
	 */
	public static Calendar getCurrentCalendar() {
		return new GregorianCalendar();
	}

	/**
	 * 比较时间是否相等
	 * 
	 * @return
	 */
	public static boolean equalCalendar(Calendar calendar1, Calendar calendar2) {
		if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
				&& calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
				&& calendar1.get(Calendar.DATE) == calendar2.get(Calendar.DATE)) {
			return true;
		}
		return false;
	}

}
