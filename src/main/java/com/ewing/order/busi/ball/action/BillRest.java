package com.ewing.order.busi.ball.action;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ewing.order.ball.bill.DailyBillResp;
import com.ewing.order.ball.bill.HistoryBillResp;
import com.ewing.order.ball.bill.TodayBillResp;
import com.ewing.order.ball.util.RequestTool;
import com.ewing.order.core.web.base.BaseRest;
import com.ewing.order.core.web.common.RequestJson;
import com.ewing.order.core.web.common.RestResult;

/**
 * 账单接口
 * 
 * @author tansonlam
 * @create 2018年8月10日
 */
@RestController
public class BillRest extends BaseRest {

	/**
	 * 获取历史投注记录
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/ball/getHistoryData.op", method = RequestMethod.POST)
	@ResponseBody
	public RestResult<HistoryBillResp> getHistoryData() throws Exception {
		RequestJson requestJson = getRequestJson();
		String uid = requestJson.getString("uid");
		checkRequired(uid, "uid");
		HistoryBillResp historyBillResp = RequestTool.getHistoryData(uid);
		return RestResult.successResult(historyBillResp);
	}

	/**
	 * 获取今日投注记录
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/ball/getTodayWagers.op", method = RequestMethod.POST)
	@ResponseBody
	public RestResult<TodayBillResp> getTodayWagers() throws Exception {
		RequestJson requestJson = getRequestJson();
		String uid = requestJson.getString("uid");
		checkRequired(uid, "uid");
		TodayBillResp odayBillResp = RequestTool.getTodayWagers(uid);
		return RestResult.successResult(odayBillResp);
	}

	/**
	 * 获取指定日期的投注记录
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/ball/getDailyBill.op", method = RequestMethod.POST)
	@ResponseBody
	public RestResult<DailyBillResp> getDailyBill() throws Exception {
		RequestJson requestJson = getRequestJson();
		String uid = requestJson.getString("uid");
		String date = requestJson.getString("date");
		checkRequired(uid, "uid");
		checkRequired(date, "date");
		DailyBillResp dailyBillResp = RequestTool.getDailyBill(uid, date);
		return RestResult.successResult(dailyBillResp);
	}

}
