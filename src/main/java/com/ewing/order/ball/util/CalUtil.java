package com.ewing.order.ball.util;

import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;

import com.ewing.order.busi.ball.ddl.BetRollInfo;

public class CalUtil {
	
	private static final DecimalFormat fnum = new DecimalFormat("##0.0000"); 
	public static void main(String[] args) {
		
	}
	public static Float computeScoreSec4Quartz(BetRollInfo betRollInfo) {
		if (betRollInfo == null || StringUtils.isEmpty(betRollInfo.getT_count()))
			return 0f;
		Integer costTime = 600 - Integer.valueOf(betRollInfo.getT_count());
		Float rate = 0f;
		if (betRollInfo.getSe_now().equals("Q1")) {
			rate =  Float.valueOf(betRollInfo.getSc_Q1_total()) / costTime;
		} else if (betRollInfo.getSe_now().equals("Q2")) {
			rate =  Float.valueOf(betRollInfo.getSc_Q2_total()) / costTime;
		} else if (betRollInfo.getSe_now().equals("Q3")) {
			rate =  Float.valueOf(betRollInfo.getSc_Q3_total()) / costTime;
		} else if (betRollInfo.getSe_now().equals("Q4")) {
			rate =  Float.valueOf(betRollInfo.getSc_Q4_total()) / costTime;
		}
		return Float.valueOf(fnum.format(rate)); 
	}

	public static Float computeScoreSec4Alltime(BetRollInfo betRollInfo) {
		if (betRollInfo == null || StringUtils.isEmpty(betRollInfo.getT_count()))
			return 0f;
		Integer costTime = 0;
		if (betRollInfo.getSe_now().equals("Q1")) {
			costTime = +600;
		} else if (betRollInfo.getSe_now().equals("Q2")) {
			costTime += 1200;
		} else if (betRollInfo.getSe_now().equals("Q3")) {
			costTime += 1800;
		} else if (betRollInfo.getSe_now().equals("Q4")) {
			costTime += 2400;
		}
		costTime = costTime - Integer.valueOf(betRollInfo.getT_count());
		Float rate =  Integer.valueOf(betRollInfo.getSc_total()) / (costTime * 1f);
		return Float.valueOf(fnum.format(rate)); 
	}

}
