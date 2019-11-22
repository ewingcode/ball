package com.ewing.busi.ball.betway;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ewing.order.Door;
import com.ewing.order.ball.event.BetStrategyContext;
import com.ewing.order.ball.event.strategy.BKRollAutoSideStrategy2;
import com.ewing.order.busi.ball.service.BetLogService;
import com.ewing.order.busi.ball.service.BetRuleService;
import com.ewing.order.busi.ball.service.BwContinueService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Door.class)
public class BKRollAutoSideStrategy2Test {
	@Resource
	private BetLogService betLogService;
	@Resource
	private BetRuleService betRuleService;
	@Resource
	private BwContinueService bwContinueService;
	@Test
	public void test() {
		BKRollAutoSideStrategy2 strategy2 = new BKRollAutoSideStrategy2();
		BetStrategyContext betStrategyContext=new BetStrategyContext();
		betStrategyContext.setAccount("ts2LAM338-3").setBetLogService(betLogService).setBetRuleService(betRuleService).setBwContinueService(bwContinueService);
		strategy2.setBetStrategyContext(betStrategyContext);
		strategy2.setWinRule("2,1");
		//Integer num = strategy2.loseTotalAfterWinRule();
		Boolean allow = strategy2.allowBetInByWinRule();
		System.out.println(allow);
	}
}
