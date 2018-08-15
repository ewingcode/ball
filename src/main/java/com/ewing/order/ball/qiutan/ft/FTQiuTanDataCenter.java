package com.ewing.order.ball.qiutan.ft;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewing.order.ball.qiutan.QiuTanRequestTool;

public class FTQiuTanDataCenter {
	private static Logger log = LoggerFactory.getLogger(FTQiuTanDataCenter.class);
	 
	public void collectBk() {
		BkTodayResp bkTodayResp = QiuTanRequestTool.getBkToday();
		for (BkTodayH bkTodayH : bkTodayResp.getM().getH()) {
			bkTodayH.toParse();
			List<BkRate> rateList = QiuTanRequestTool.getBKGameRate(bkTodayH.getgId());
			bkTodayH.setRateList(rateList);
		}

	}
}
