package com.ewing.busi.ball.data;

import com.ewing.order.ball.bill.DailyBillResp;
import com.ewing.order.ball.bill.HistoryBillResp;
import com.ewing.order.ball.bill.TodayBillResp;
import com.ewing.order.ball.login.LoginResp;
import com.ewing.order.ball.util.RequestTool;
import com.ewing.order.common.prop.BallmatchProp;
import com.ewing.order.util.GsonUtil;

public class LoginTest {
	public static void main(String[] args) {
		BallmatchProp.url = "http://199.26.97.192/";
		while (true) {
			LoginResp loginResp = RequestTool.login("tansonLAM83", "523123ZX");
			System.out.println(GsonUtil.getGson().toJson(loginResp));
			TodayBillResp todayBillResp = RequestTool.getTodayWagers(loginResp.getUid());
			System.out.println(GsonUtil.getGson().toJson(todayBillResp));
			DailyBillResp dailyBillResp =RequestTool.getDailyBill(loginResp.getUid(), "2018-09-22");
			System.out.println(GsonUtil.getGson().toJson(dailyBillResp));
			HistoryBillResp historyBillResp =RequestTool.getHistoryData(loginResp.getUid());
			System.out.println(GsonUtil.getGson().toJson(historyBillResp));
			
			
			try {
				Thread.currentThread().sleep(5000l);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
