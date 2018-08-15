package com.ewing.busi.ball.data;

import java.util.Map;

import com.ewing.order.util.GsonUtil;
import com.google.common.collect.Maps;

public class FtRule {
	private static void smallBig() {

		Map<String, String> paramMap = Maps.newHashMap();
		paramMap.put("MAXEACHDAY", "10");
		paramMap.put("MAXEACHMATCH", "1");
		paramMap.put("BALL", "3");
		paramMap.put("BALL_COMPARE", ">=");
		paramMap.put("MINROUDIS", "0.2");
		paramMap.put("MINROUDIS_COMPARE", ">=");
		paramMap.put("MONEYEACHMATCH", "50");
		paramMap.put("BUYSIDE", "H");
		System.out.println("买小球："+GsonUtil.getGson().toJson(paramMap));
	}

	private static void bigBall() {

		Map<String, String> paramMap = Maps.newHashMap();
		paramMap.put("MAXEACHDAY", "10");
		paramMap.put("MAXEACHMATCH", "1");
		paramMap.put("BALL", "2");
		paramMap.put("BALL_COMPARE", "<=");
		paramMap.put("MINROUDIS", "-0.2");
		paramMap.put("MINROUDIS_COMPARE", "<=");
		paramMap.put("MONEYEACHMATCH", "50");
		paramMap.put("BUYSIDE", "C");
		System.out.println("买大球："+GsonUtil.getGson().toJson(paramMap));
	}

	public static void main(String[] args) {
		smallBig();
		bigBall();
	}
}
