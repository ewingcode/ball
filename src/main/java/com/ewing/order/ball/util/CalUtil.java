package com.ewing.order.ball.util;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.ewing.order.ball.dto.BetInfoDto;
import com.ewing.order.busi.ball.ddl.BetRollInfo;
import com.ewing.order.util.DataFormat;

public class CalUtil {

	private static final DecimalFormat fnum = new DecimalFormat("##0.0000");
	private static final Integer quartsSec = 600;
	private static final Integer nbaQuartsSec = 720;

	public static void main(String[] args) {
		System.out.println(getStartTimeOfWeek());
	}

	public static Integer getFixTcount(String tCount){
		int i = tCount.indexOf(".");
		return i >-1? Integer.valueOf(tCount.substring(0, i)):Integer.valueOf(tCount);
		 
	}
	public static Float computeScoreSec4Quartz(BetRollInfo betRollInfo) {
		if (betRollInfo == null || StringUtils.isEmpty(betRollInfo.getT_count()))
			return 0f;
		Integer costTime = getEachQuartz(betRollInfo) - getFixTcount(betRollInfo.getT_count());
		Float rate = 0f;
		if (betRollInfo.getSe_now().equals("Q1")) {
			rate = Float.valueOf(betRollInfo.getSc_Q1_total()) / costTime;
		} else if (betRollInfo.getSe_now().equals("Q2")) {
			rate = Float.valueOf(betRollInfo.getSc_Q2_total()) / costTime;
		} else if (betRollInfo.getSe_now().equals("Q3")) {
			rate = Float.valueOf(betRollInfo.getSc_Q3_total()) / costTime;
		} else if (betRollInfo.getSe_now().equals("Q4")) {
			rate = Float.valueOf(betRollInfo.getSc_Q4_total()) / costTime;
		}
		try {
			return Float.valueOf(fnum.format(rate));
		} catch (NumberFormatException e) {
			return 0f;
		}
	}

	public static Boolean isNba(String league) {
		return !StringUtils.isEmpty(league) && (league.indexOf("NBA") > -1 || league.indexOf("中国男子篮球职业联赛") > -1) ? true
				: false;
	}

	public static Integer getEachQuartz(BetRollInfo betRollInfo) {
		return isNba(betRollInfo.getLeague()) ? nbaQuartsSec : quartsSec;
	}
	
	public static Integer getEachQuartz(String leagueName) {
		return isNba(leagueName) ? nbaQuartsSec : quartsSec;
	}

	public static Float computeScoreSec4Alltime(BetRollInfo betRollInfo) {
		Integer each = getEachQuartz(betRollInfo);
		if (betRollInfo == null || StringUtils.isEmpty(betRollInfo.getT_count()))
			return 0f;
		Integer costTime = 0;
		if (betRollInfo.getSe_now().equals("Q1")) {
			costTime += each;
		} else if (betRollInfo.getSe_now().equals("Q2")) {
			costTime += each * 2;
		} else if (betRollInfo.getSe_now().equals("Q3")) {
			costTime += each * 3;
		} else if (betRollInfo.getSe_now().equals("Q4")) {
			costTime += each * 4;
		}
		costTime = costTime - getFixTcount(betRollInfo.getT_count());
		if (costTime == 0)
			return 0f;
		Float rate = Integer.valueOf(betRollInfo.getSc_total()) / (costTime * 1f);
		try {
			return Float.valueOf(fnum.format(rate));
		} catch (NumberFormatException e) {
			return 0f;
		}
	}
	
	public static Float computeWholeRate(BetInfoDto betInfoDto) {
		Integer each = getEachQuartz(betInfoDto.getLeague());
		Integer costTime = each * 4;
		  
		Float rate = Integer.valueOf(betInfoDto.getSc_total()) / (costTime * 1f);
		try {
			return Float.valueOf(fnum.format(rate));
		} catch (NumberFormatException e) {
			return 0f;
		}
	}

	public static Float computeScoreSecBefore4Q(BetRollInfo betRollInfo) {
		/*if(false)
		return computeScoreSec4Alltime(betRollInfo);*/
		Integer each = getEachQuartz(betRollInfo);
		if (betRollInfo == null || StringUtils.isEmpty(betRollInfo.getT_count()))
			return 0f;
		Integer costTime = each * 3;

		costTime = costTime - getFixTcount(betRollInfo.getT_count());
		if (costTime == 0)
			return 0f;
		Float rate = (Integer.valueOf(betRollInfo.getSc_total()) - Integer.valueOf(betRollInfo.getSc_Q4_total()))
				/ (costTime * 1f);
		try {
			return Float.valueOf(fnum.format(rate));
		} catch (NumberFormatException e) {
			return 0f;
		}
	}
	
	public static String getShortStartDayOfWeek() {
		return getStartDayOfWeek().replace("-", ""); 
	}
	
	public static String getStartDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		String nowDate = DataFormat.DateToString(cal.getTime(), DataFormat.DATE_FORMAT);
		Date endTimeOfWeek = DataFormat.stringToDate(nowDate + " 11:59:59",
				DataFormat.DATETIME_FORMAT);
		int reduceDay = 0;
		if (dayOfWeek == 1 || (dayOfWeek == 2 && cal.getTime().before(endTimeOfWeek))) {
			reduceDay = 5 + cal.get(Calendar.DAY_OF_WEEK);
		} else {
			reduceDay = cal.get(Calendar.DAY_OF_WEEK) - 2;
		}
		cal.add(Calendar.DATE, Math.negateExact(reduceDay));
		return DataFormat.DateToString(cal.getTime(), DataFormat.DATE_FORMAT);
	}  
	
	public static String getStartTimeOfWeek() {
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		String nowDate = DataFormat.DateToString(cal.getTime(), DataFormat.DATE_FORMAT);
		Date endTimeOfWeek = DataFormat.stringToDate(nowDate + " 11:59:59",
				DataFormat.DATETIME_FORMAT);
		int reduceDay = 0;
		if (dayOfWeek == 1 || (dayOfWeek == 2 && cal.getTime().before(endTimeOfWeek))) {
			reduceDay = 5 + cal.get(Calendar.DAY_OF_WEEK);
		} else {
			reduceDay = cal.get(Calendar.DAY_OF_WEEK) - 2;
		}
		cal.add(Calendar.DATE, Math.negateExact(reduceDay));
		return DataFormat.DateToString(cal.getTime(), DataFormat.DATE_FORMAT)+" 11:59:59";
	}  
}
