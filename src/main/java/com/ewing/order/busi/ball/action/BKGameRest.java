package com.ewing.order.busi.ball.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ewing.order.ball.BetCollector;
import com.ewing.order.ball.RequestTool;
import com.ewing.order.ball.bk.bet.BetResp;
import com.ewing.order.ball.bk.bet.BkPreOrderViewResp;
import com.ewing.order.ball.dto.BetInfoDto;
import com.ewing.order.busi.ball.ddl.BetLog;
import com.ewing.order.busi.ball.dto.BkBetRequest;
import com.ewing.order.busi.ball.service.BetLogService;
import com.ewing.order.core.web.base.BaseRest;
import com.ewing.order.core.web.common.RequestJson;
import com.ewing.order.core.web.common.RestResult;
import com.ewing.order.util.BeanCopy;
import com.fasterxml.jackson.databind.util.BeanUtil;

/**
 *
 * @author tansonlam
 * @create 2018年8月10日
 */
@RestController
public class BKGameRest extends BaseRest {
	@Resource
	private BetLogService betLogService;

	/**
	 * 滚球投注列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/ball/bkRollGameList.op", method = RequestMethod.POST)
	@ResponseBody
	public RestResult<List<BetInfoDto>> bkRollGameList() throws Exception {
		return RestResult.successResult(BetCollector.CollectDataPool.getBkRollList());
	}

	/**
	 * 今日投注列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/ball/bkTodayGameList.op", method = RequestMethod.POST)
	@ResponseBody
	public RestResult<List<BetInfoDto>> bkTodayGameList() throws Exception {
		return RestResult.successResult(BetCollector.CollectDataPool.getBkTodayList());
	}

	private static Integer spreadValue = 1;
	private static Integer iorratioValue = 1;

	/**
	 * 投注钱获取赔率信息
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/ball/getbkPreOrderView.op", method = RequestMethod.POST)
	@ResponseBody
	public RestResult<BkPreOrderViewResp> getbkPreOrderView() throws Exception {
		boolean debug = false;
		if (debug) {
			BkPreOrderViewResp bkPreOrderViewResp = new BkPreOrderViewResp();
			bkPreOrderViewResp.setSpread(String.valueOf(spreadValue++));
			if (spreadValue == 20)
				spreadValue = 1;
			bkPreOrderViewResp.setIoratio(String.valueOf(iorratioValue++));
			if (iorratioValue == 20)
				iorratioValue = 1;
			bkPreOrderViewResp.setStrong("C");
			return RestResult.successResult(bkPreOrderViewResp);
		}
		RequestJson requestJson = getRequestJson();
		String uid = requestJson.getString("uid");
		String gid = requestJson.getString("gid");
		String gtype = requestJson.getString("gtype");
		String wtype = requestJson.getString("wtype");
		String side = requestJson.getString("side");
		checkRequired(uid, "uid");
		checkRequired(gid, "gid");
		checkRequired(gtype, "gtype");
		checkRequired(wtype, "wtype");
		checkRequired(side, "side");
		BkPreOrderViewResp bkPreOrderViewResp = RequestTool.getbkPreOrderView(uid, gid, gtype,
				wtype, side);

		return RestResult.successResult(bkPreOrderViewResp);
	}

	/**
	 * 投注
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/ball/bkbet.op", method = RequestMethod.POST)
	@ResponseBody
	public RestResult<BetResp> bkbet() throws Exception {
		BkBetRequest req = requestJson2Obj(BkBetRequest.class);
		checkRequired(req, "betRequest");
		checkRequired(req.getGolds(), "golds");
		checkRequired(req.getAccount(), "account");
		checkRequired(req.getUid(), "uid");
		checkRequired(req.getGid(), "gid");
		checkRequired(req.getGtype(), "gtype");
		checkRequired(req.getWtype(), "wtype");
		checkRequired(req.getSide(), "side");
		checkRequired(req.getIoratio(), "ioratio");
		checkRequired(req.getCon(), "con");
		checkRequired(req.getRatio(), "ratio");
		checkRequired(req.getTimestamp2(), "timestamp2");
		BkPreOrderViewResp bkPreOrderViewResp = new BkPreOrderViewResp();
		bkPreOrderViewResp.setTs(req.getTimestamp2());
		bkPreOrderViewResp.setRatio(req.getRatio());
		bkPreOrderViewResp.setIoratio(req.getIoratio());
		bkPreOrderViewResp.setCon(req.getCon());
		BetResp betResp = RequestTool.bkbet(req.getUid(), req.getGid(), req.getGtype(),
				req.getGolds(), req.getWtype(), req.getSide(), bkPreOrderViewResp);
		if (betResp != null) {
			BetLog betLog = new BetLog();
			BeanCopy.copy(betLog, betResp, true);
			betLogService.save(req.getAccount(), betLog);
		}

		return RestResult.successResult(betResp);
	}

}
