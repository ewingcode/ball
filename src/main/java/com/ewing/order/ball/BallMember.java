package com.ewing.order.ball;

import java.util.List;
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
import com.ewing.order.busi.ball.service.BetBillService;
import com.ewing.order.busi.ball.service.BetLogService;
import com.ewing.order.busi.ball.service.BetRuleService;
import com.ewing.order.util.HttpUtils;

/**
 *
 * @author tansonlam
 * @create 2018年7月20日
 */
@Component
public class BallMember {
	private static Logger log = LoggerFactory.getLogger(HttpUtils.class);

	@Resource
	private BetBillService betBillService;
	@Resource
	private BetLogService betLogService;
	@Resource
	private BetRuleService betRuleService;

	public LoginResp login(String account, String pwd) {
		LoginResp loginResp = RequestTool.login(account, pwd);
		log.info("login successfully.");
		return loginResp;
	}

	/**
	 * 添加今日足球的监听器
	 */
	public void addFtCurrentListener(String account, String uid) {
		BetStrategyPool betStrategyPool = new BetStrategyPool();

		// betStrategyPool.addBetStrategy(fTBasicBetStrategy1());
		// betStrategyPool.addBetStrategy(fTBasicBetStrategy2());
		betStrategyPool.getBetStrategyContext().setBetLogService(betLogService)
				.setBetRuleService(betRuleService).setAccount(account).setUid(uid);
		betStrategyPool.loadBetStrategyConf();
		BetInfoListener betInfoListener = new BetInfoListener(account, betStrategyPool);
		BallSource.getFTCurrent().addBallListener(betInfoListener);
	}

	public void addBkListener(String account, String uid) {
		this.addBkRollListener(account, uid);
		this.addBkTodayListener(account, uid);
		this.startHeartBeat(account, uid);
	}

	/**
	 * 添加滚球篮球的监听器
	 */
	public void addBkTodayListener(String account, String uid) {
		BetStrategyPool betStrategyPool = new BetStrategyPool();
		betStrategyPool.getBetStrategyContext().setBetLogService(betLogService)
				.setBetRuleService(betRuleService).setAccount(account).setUid(uid)
				.setGtype(GtypeStatus.BK).setPtype(PtypeStatus.TODAY)
				.setBetInfoList(new WrapDataCallBack<List<BetInfoDto>>() {
					@Override
					public List<BetInfoDto> getData() {
						return BetCollector.CollectDataPool.getBkTodayList();
					}
				});
		betStrategyPool.loadBetStrategyConf();
		BetInfoListener betInfoListener = new BetInfoListener(account, betStrategyPool);
		BallSource.getBKCurrent().addBallListener(betInfoListener);
	}

	/**
	 * 添加滚球篮球的监听器
	 */
	public void addBkRollListener(String account, String uid) {
		BetStrategyPool betStrategyPool = new BetStrategyPool();
		betStrategyPool.getBetStrategyContext().setBetLogService(betLogService)
				.setBetRuleService(betRuleService).setAccount(account).setUid(uid)
				.setGtype(GtypeStatus.BK).setPtype(PtypeStatus.ROLL)
				.setBetInfoList(new WrapDataCallBack<List<BetInfoDto>>() {
					@Override
					public List<BetInfoDto> getData() {
						return BetCollector.CollectDataPool.getBkRollList();
					}
				});
		betStrategyPool.loadBetStrategyConf();
		BetInfoListener betInfoListener = new BetInfoListener(account, betStrategyPool);
		BallSource.getBKRoll().addBallListener(betInfoListener);
	}

	/**
	 * 登陆后的心跳，保持在线连接
	 */
	public void startHeartBeat(final String account, final String uid) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					log.info("timer:" + Thread.currentThread().getName());
					RequestTool.getLeaguesCount(uid);
				} catch (Exception e) { 
					if (e != null && !StringUtils.isEmpty(e.getMessage())
							&& e.getMessage().indexOf(RequestTool.ErrorCode.doubleLogin) > -1) {
						log.error("stop listener for account:"+account, e);
						BallSource.getBKRoll().stopBallListener(account);
						BallSource.getBKCurrent().stopBallListener(account);
						timer.cancel();
					}
				}
			}
		}, 1000, 20000);
	}
}
