package com.ewing.order.ball.util;

import org.apache.commons.lang.StringUtils;

import com.ewing.order.busi.ball.ddl.BetRollInfo;

public class CalUtil {
	public static Float computeScoreSec4Quartz(BetRollInfo betRollInfo) {
		if (betRollInfo == null || StringUtils.isEmpty(betRollInfo.getT_count()))
			return 0f;
		Integer costTime = 600 - Integer.valueOf(betRollInfo.getT_count());
		if (betRollInfo.getSe_now().equals("Q1")) {
			return Float.valueOf(betRollInfo.getSc_Q1_total()) / costTime;
		} else if (betRollInfo.getSe_now().equals("Q2")) {
			return Float.valueOf(betRollInfo.getSc_Q2_total()) / costTime;
		} else if (betRollInfo.getSe_now().equals("Q3")) {
			return Float.valueOf(betRollInfo.getSc_Q3_total()) / costTime;
		} else if (betRollInfo.getSe_now().equals("Q4")) {
			return Float.valueOf(betRollInfo.getSc_Q4_total()) / costTime;
		}
		return 0f;
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
		return Integer.valueOf(betRollInfo.getSc_total()) / (costTime * 1f);
	}

}
