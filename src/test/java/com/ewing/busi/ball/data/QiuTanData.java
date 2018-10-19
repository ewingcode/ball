package com.ewing.busi.ball.data;

import java.util.Map;

import com.ewing.order.util.HttpUtils;
import com.google.common.collect.Maps;

public class QiuTanData {
	public static void main(String[] args) {
		String r=HttpUtils.request("http://nba.win007.com/jsData/analyOdds/318390.js?1533259802000", "GET", null, null);
		System.out.println(r); 
		Map<String,String> map = Maps.newConcurrentMap();
		map.put("Host", "lq3.win007.com");
		map.put("Referer", "http://lq3.win007.com/nba.htm");
		map.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 UBrowser/6.2.3964.2 Safari/537.36");
		map.put("Accept", "*/*");
		map.put("Content-Type", "text/xml");
		map.put("Accept", "*/*");
	//	String r2=HttpUtils.request("http://lq3.win007.com/NBA/today2.xml?t=1533259802000", "GET", null, map,"gbk");
		//System.out.println(r2);
	}
}
