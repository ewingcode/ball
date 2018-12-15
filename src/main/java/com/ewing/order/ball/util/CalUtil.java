package com.ewing.order.ball.util;

import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;
import org.jboss.jandex.Main;

import com.ewing.order.busi.ball.ddl.BetRollInfo;

public class CalUtil {

	private static final DecimalFormat fnum = new DecimalFormat("##0.0000");
	private static final Integer quartsSec = 600;
	private static final Integer nbaQuartsSec = 720;

	public static void main(String[] args) {
		System.out.println(fnum.format(0f));
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

	private static Boolean isNba(String league) {
		return !StringUtils.isEmpty(league) && (league.indexOf("NBA") > -1 || league.indexOf("中国男子篮球职业联赛") > -1) ? true
				: false;
	}

	public static Integer getEachQuartz(BetRollInfo betRollInfo) {
		return isNba(betRollInfo.getLeague()) ? nbaQuartsSec : quartsSec;
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

	public static Float computeScoreSecBefore4Q(BetRollInfo betRollInfo) {
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

}
