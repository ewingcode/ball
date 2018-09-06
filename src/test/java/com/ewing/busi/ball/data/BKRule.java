package com.ewing.busi.ball.data;

import java.util.Map;

import com.ewing.order.util.GsonUtil;
import com.google.common.collect.Maps;

public class BKRule {

	public static void setRe() {
		Map<String, String> paramMap = Maps.newHashMap();
		paramMap.put("RADIO_RE", "10");
		paramMap.put("RADIO_RE_COMPARE", "<=");
		paramMap.put("BUYSIDE", "C");
		paramMap.put("MAXEACHMATCH", "1");
		paramMap.put("MONEYEACHMATCH", "50");
		paramMap.put("BEFORE_SE_NOW", "4");
		System.out.println("买大球：" + GsonUtil.getGson().toJson(paramMap));

	}
	
	public static void setAutoOu() { 
		Map<String, String> paramMap = Maps.newHashMap();
		paramMap.put("ALL_AND_QUARTZ_INTERVAL", "0.025");
		paramMap.put("ADDMAX_ALL_AND_QUARTZ_INTERVAL", "0.02");
		paramMap.put("BUYSIDE", "AUTO");
		paramMap.put("SQ_NOW", "Q4");
		paramMap.put("MIN_TIME_HIGH_SCORE_EACHSEC", "5");
		paramMap.put("EXCLUDE_LEAGUE", "美式足球,篮网球,3X3");
		System.out.println("买大球：" + GsonUtil.getGson().toJson(paramMap));

	}

	public static void main(String[] args) {
		//setRe();
		setAutoOu();
	}
}
