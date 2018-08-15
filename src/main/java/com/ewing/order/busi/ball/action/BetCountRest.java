package com.ewing.order.busi.ball.action;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ewing.order.ball.BetCollector;
import com.ewing.order.busi.ball.dto.GameCount;
import com.ewing.order.core.web.base.BaseRest;
import com.ewing.order.core.web.common.RestResult;

/**
 *
 * @author tansonlam
 * @create 2018年8月10日
 */
@RestController
public class BetCountRest extends BaseRest {

	 
	/**
	 * 滚球投注场数
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/ball/rollCount.op", method = RequestMethod.POST)
	@ResponseBody
	public RestResult<GameCount> getRollCount() throws Exception { 
		GameCount gameCount = new GameCount();
		gameCount.setBkCount(BetCollector.CollectDataPool.getBkRollList().size());
		gameCount.setFtCount(BetCollector.CollectDataPool.getFtRollList().size()); 
		return RestResult.successResult(gameCount);
	}
	
	/**
	 * 今日投注场数
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/ball/todayCount.op", method = RequestMethod.POST)
	@ResponseBody
	public RestResult<GameCount> getTodayCount() throws Exception { 
		GameCount gameCount = new GameCount();
		gameCount.setBkCount(BetCollector.CollectDataPool.getBkTodayList().size());
		gameCount.setFtCount(BetCollector.CollectDataPool.getFtTodayList().size()); 
		return RestResult.successResult(gameCount);
	}

}
