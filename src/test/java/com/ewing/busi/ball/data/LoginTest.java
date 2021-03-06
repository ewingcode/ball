package com.ewing.busi.ball.data;

import com.ewing.order.ball.bk.bet.BetResp;
import com.ewing.order.ball.bk.bet.BkPreOrderViewResp;
import com.ewing.order.ball.login.LoginResp;
import com.ewing.order.ball.util.RequestTool;
import com.ewing.order.common.prop.BallmatchProp;
import com.ewing.order.util.GsonUtil;

public class LoginTest {
	public static void main(String[] args) {
		//1X016 赔率改变
		BallmatchProp.url = "http://199.26.100.188";
		String gid = "2868707";
		LoginResp loginResp = RequestTool.login("tansonLAM68", "523123ZX");
		System.out.println(GsonUtil.getGson().toJson(loginResp));
		String uid=loginResp.getUid();
		String gtype = "BK";// 篮球
		String showTyp = "FT";// 未开始的比赛
		String wtype = "ROU";
		String side = "C";
		String golds ="5";
		BkPreOrderViewResp bkPreOrderViewResp = RequestTool.getbkPreOrderView(loginResp.getUid(), gid, gtype, wtype, side);
		System.out.println(GsonUtil.getGson().toJson(bkPreOrderViewResp));
		//BetResp betResp = RequestTool.bkbet(uid, gid, gtype, golds, wtype, side, bkPreOrderViewResp);
		//System.out.println(GsonUtil.getGson().toJson(betResp));
	}
}
