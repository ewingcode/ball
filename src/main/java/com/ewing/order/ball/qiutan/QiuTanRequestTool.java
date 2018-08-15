package com.ewing.order.ball.qiutan;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewing.order.ball.qiutan.ft.BkRate;
import com.ewing.order.ball.qiutan.ft.BkTodayH;
import com.ewing.order.ball.qiutan.ft.BkTodayResp;
import com.ewing.order.common.exception.BusiException;
import com.ewing.order.util.HttpUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 *
 * @author tansonlam
 * @create 2018年8月3日
 */
public class QiuTanRequestTool {
	private static Logger log = LoggerFactory.getLogger(QiuTanRequestTool.class);

	/**
	 * 获取篮球今日赛况
	 * 
	 */
	public static BkTodayResp getBkToday() {
		Map<String, String> map = Maps.newConcurrentMap();
		map.put("Host", "lq3.win007.com");
		map.put("Referer", "http://lq3.win007.com/nba.htm");
		map.put("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 UBrowser/6.2.3964.2 Safari/537.36");
		map.put("Accept", "*/*");
		map.put("Content-Type", "text/xml");
		map.put("Accept", "*/*");
		String resp = HttpUtils.request(
				"http://lq3.win007.com/NBA/today2.xml?t=" + System.currentTimeMillis() / 1000,
				"GET", null, map, "gbk");
		System.out.println(resp);
		if (HttpUtils.isErrorResp(resp))
			throw new BusiException("登陆出错！");
		resp = resp.replace("<h>", "<h><content>").replace("</h>", "</content></h>");
		BkTodayResp bkTodayResp = new BkTodayResp();
		return bkTodayResp.fromResp(resp);
	}

	/**
	 * 获取篮球比赛水位信息
	 * 
	 * @param gameId
	 * @return
	 */
	public static List<BkRate> getBKGameRate(String gameId) {
		String resp = HttpUtils.request("http://nba.win007.com/jsData/analyOdds/" + gameId + ".js?"
				+ System.currentTimeMillis() / 1000, "GET", null, null, "utf-8");

		resp = resp.replace("var oddsData='", "").replace("'.split('^')", "");
		String[] rates = StringUtils.split(resp, "^");
		List<BkRate> rateList = Lists.newArrayList();
		for (String rate : rates) {
			try {
				BkRate bkRate = new BkRate();
				String[] s = StringUtils.split(rate, ";");
				bkRate.setId(new String(s[0].getBytes(), "utf-8"));
				bkRate.setName(s[1]);
				String[] r = StringUtils.split(s[2], ",");
				bkRate.setIor_RH(getContent4Array(r, 0));
				bkRate.setRatio(getContent4Array(r, 1));
				bkRate.setIor_RC(getContent4Array(r, 2));
				bkRate.setN_ior_RH(getContent4Array(r, 3));
				bkRate.setN_ratio(getContent4Array(r, 4));
				bkRate.setN_ior_RC(getContent4Array(r, 5));
				bkRate.setIor_OUH(getContent4Array(r, 6));
				bkRate.setRatio_ou(getContent4Array(r, 7));
				bkRate.setIor_OUC(getContent4Array(r, 8));
				bkRate.setN_ior_OUH(getContent4Array(r, 9));
				bkRate.setN_ratio_ou(getContent4Array(r, 10));
				bkRate.setN_ior_OUC(getContent4Array(r, 11));
				rateList.add(bkRate);
			} catch (Exception e) {
				log.error(rate, e);
				e.printStackTrace();
			}
		}
		/*
		 * Collections.sort(rateList, new Comparator<BkRate>() {
		 * 
		 * @Override public int compare(BkRate o1, BkRate o2) { try { return
		 * Integer.valueOf(o1.getId().trim()).compareTo(Integer.valueOf(o2.getId
		 * ().trim())); } catch (NumberFormatException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); return 0; } }
		 * 
		 * });
		 */
		return rateList;
	}

	private static String getContent4Array(String[] array, int index) {
		if (array.length > index)
			return array[index];
		return null;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(Integer.valueOf("3"));
		BkTodayResp bkTodayResp = getBkToday();
		for (BkTodayH bkTodayH : bkTodayResp.getM().getH()) {
			bkTodayH.toParse();
			List<BkRate> rateList = getBKGameRate(bkTodayH.getgId());
			log.info(bkTodayH.toString());
			for (BkRate bkRate : rateList) {
				log.info(bkRate.toString());
			}
		}

		System.out.println(System.currentTimeMillis() / 1000);
	}
}
