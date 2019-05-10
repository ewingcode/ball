package com.ewing.order.ball;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ewing.order.ball.dto.BetInfoDto;
import com.ewing.order.ball.event.BallSource;
import com.ewing.order.ball.event.BetInfoListener;
import com.ewing.order.ball.event.BetStrategyPool;
import com.ewing.order.ball.event.WrapDataCallBack;
import com.ewing.order.ball.login.LoginResp;
import com.ewing.order.ball.shared.GtypeStatus;
import com.ewing.order.ball.shared.PtypeStatus;
import com.ewing.order.ball.util.RequestTool;
import com.ewing.order.busi.ball.service.BetAutoBuyService;
import com.ewing.order.busi.ball.service.BetBillService;
import com.ewing.order.busi.ball.service.BetLogService;
import com.ewing.order.busi.ball.service.BetRuleService;
import com.ewing.order.busi.ball.service.BwContinueService;
import com.google.common.collect.Maps;

/**
 *
 * @author tansonlam
 * @create 2018年7月20日
 */
@Component
public class BallMember {
	private static Logger log = LoggerFactory.getLogger(BallMember.class);

	@Resource
	private BetBillService betBillService;
	@Resource
	private BetLogService betLogService;
	@Resource
	private BetRuleService betRuleService;
	@Resource
	private BetAutoBuyService betAutoBuyService;
	@Resource
	private BwContinueService bwContinueService;
	@Resource
	private BallAutoBet ballAutoBet;
	private static Map<String, Timer> heartBeatTimers = Maps.newConcurrentMap();

	private static Map<String, String> activeAccounts = Maps.newConcurrentMap();

	public LoginResp login(String account, String pwd) {
		LoginResp loginResp = RequestTool.login(account, pwd);
		log.info("login successfully.");
		return loginResp;
	}

	public void addBkListener(Boolean isAuto, String account, String uid,String ballAccount) {
		activeAccounts.put(account, account);
		this.addBkRollListener(isAuto, account, uid ,ballAccount);
		//this.addBkTodayListener(isAuto, account, uid);
		//this.startHeartBeat(isAuto, account,  uid);
	}

	public void stopBkListener(String account) {
		activeAccounts.remove(account);
		BallSource.getBKRoll().stopBallListener(account);
		//BallSource.getBKCurrent().stopBallListener(account);
//		Timer timer = heartBeatTimers.get(account);
//		if (timer != null)
//			timer.cancel();
	}

	public Boolean isActiveAccount(String account) {
		return activeAccounts.containsKey(account);
	}

	/**
	 * 添加滚球篮球的监听器
	 */
	public void addBkTodayListener(Boolean isAuto, String account, String uid) {
		BetStrategyPool betStrategyPool = new BetStrategyPool();
		betStrategyPool.getBetStrategyContext().setBetLogService(betLogService)
				.setBwContinueService(bwContinueService)
				.setBetRuleService(betRuleService).setAccount(account).setUid(uid)
				.setGtype(GtypeStatus.BK).setPtype(PtypeStatus.TODAY)
				.setBetInfoList(new WrapDataCallBack<List<BetInfoDto>>() {
					@Override
					public List<BetInfoDto> getData() {
						return BetCollector.CollectDataPool.getBkTodayList();
					}
				});
		betStrategyPool.loadBetStrategyConf();
		BetInfoListener betInfoListener = new BetInfoListener(isAuto, account, betStrategyPool);
		BallSource.getBKCurrent().addBallListener(betInfoListener);
	}

	/**
	 * 添加滚球篮球的监听器
	 */
	public void addBkRollListener(Boolean isAuto, String account, String uid,String ballAccount) {
		BetStrategyPool betStrategyPool = new BetStrategyPool();
		betStrategyPool.getBetStrategyContext().setBetLogService(betLogService)
				.setBallAccount(ballAccount)
				.setBwContinueService(bwContinueService)
				.setBetRuleService(betRuleService).setAccount(account).setUid(uid)
				.setGtype(GtypeStatus.BK).setPtype(PtypeStatus.ROLL)
				.setBetInfoList(new WrapDataCallBack<List<BetInfoDto>>() {
					@Override
					public List<BetInfoDto> getData() {
						return BetCollector.CollectDataPool.getBkRollList();
					}
				});
		betStrategyPool.loadBetStrategyConf();
		BetInfoListener betInfoListener = new BetInfoListener(isAuto, account, betStrategyPool);
		BallSource.getBKRoll().addBallListener(betInfoListener);
	}

	/**
	 * 登陆后的心跳，保持在线连接
	 */
	public void startHeartBeat(final boolean isAuto, final String account,
			final String uid) {
		Timer timer = new Timer();
		heartBeatTimers.put(account, timer);
		timer.schedule(new TimerTask() {
			public void run() {
				try { 
					RequestTool.getLeaguesCount(uid);
				} catch (Exception e) {
					if (e != null && !StringUtils.isEmpty(e.getMessage())
							&& e.getMessage().indexOf(RequestTool.ErrorCode.doubleLogin) > -1) {
						log.error("stop listener for account:" + account, e); 
						ballAutoBet.stop(account); 
					}
				}
			}
		}, 1000, 20000);
	}
}
