package com.ewing.order.ball.event;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;

import com.ewing.order.ball.dto.BetInfoDto;
import com.ewing.order.ball.logger.BetStrategyErrorLogger;
import com.ewing.order.ball.logger.BetStrategyLogger;
import com.ewing.order.busi.ball.ddl.BetLog;
import com.ewing.order.common.exception.BusiException;
import com.ewing.order.util.BeanCopy;
import com.ewing.order.util.GsonUtil;

/**
 * 投注策略池
 * 
 * @author tansonlam
 * @create 2018年7月30日
 */
public class BetStrategyPool {
	private static Logger log = BetStrategyLogger.logger;

	/**
	 * 单个赛局的投注策略
	 */
	private List<BetStrategy> betStrategys = new CopyOnWriteArrayList<>();

	private BetStrategyContext betStrategyContext = new BetStrategyContext();

	private final static int lockTimeOutInSec = 5;

	private Lock betLock = new ReentrantLock();

	private BetRuleParser betRuleParser;

	private AtomicBoolean isLoad = new AtomicBoolean(false); 

	private WeakHashMap<String, Timestamp> gameMap = new WeakHashMap<>();

	public BetStrategyPool() {

	}

	public BetStrategyContext getBetStrategyContext() {
		return betStrategyContext;
	}

	 

	public void setBetStrategyContext(BetStrategyContext betStrategyContext) {
		this.betStrategyContext = betStrategyContext;
	}

	public void loadBetStrategyConf() {
		if (isLoad.get())
			return;
		isLoad.set(true);
		betRuleParser = new BetRuleParser(betStrategyContext.getAccount(),
				betStrategyContext.getGtype(), betStrategyContext.getPtype(),
				betStrategyContext.getUid(), betStrategyContext.getBetRuleService());
		checkNewBetStrategyConf(); 

	}
	
	public void checkNewBetStrategyConf(){
		List<BetStrategy> betStrategyList = betRuleParser.hasNewBetStrategy();
		reload(betStrategyList);
	}

	/**
	 * 加载投注策略
	 * 
	 * @param betStrategys
	 * @param basicBetStrategy
	 */
	public void reload(List<BetStrategy> betStrategys) {
		if (betStrategys == null)
			return;
		if (CollectionUtils.isEmpty(betStrategys)) {
			this.betStrategys.clear();
		} else {
			this.betStrategys = betStrategys;
			for (BetStrategy b : betStrategys) {
				b.setBetStrategyContext(betStrategyContext);
			}
		}
	}

	/**
	 * 添加投注策略
	 * 
	 * @param betStrategy
	 */
	public void addBetStrategy(BetStrategy betStrategy) {
		betStrategy.setBetStrategyContext(betStrategyContext);
		for (BetStrategy b : betStrategys) {
			if (b.getStrategyName().equals(betStrategy.getStrategyName())) {
				betStrategys.remove(b);
			}
		}
		betStrategys.add(betStrategy);
		Collections.sort(betStrategys, new Comparator<BetStrategy>() {
			@Override
			public int compare(BetStrategy o1, BetStrategy o2) {
				return o1.getLevel().compareTo(o2.getLevel());
			}

		});
	}

	private void refreshGameMapTime(Integer ruleId, String gId, Timestamp lastUpdate) {
		gameMap.put(ruleId + "_" + gId, lastUpdate);
	}

	private boolean isNewBallEvent(Integer ruleId, String gId, Timestamp lastUpdate) {
		return gameMap.get(ruleId + "_" + gId) == null
				|| gameMap.get(ruleId + "_" + gId).before(lastUpdate);
	}

	/**
	 * 执行全自动的下注策略，不针对某场比赛
	 */
	public void runAutoStratgeys() {
		List<BetInfoDto> betList = this.getBetStrategyContext().getBetInfoList();
		if(CollectionUtils.isEmpty(betList)){
			return;
		}
		// 处理全局规则设置
		for (BetStrategy betStrategyTemplate : betStrategys) {
			if (betStrategyTemplate.getgId() != null)
				continue;
			if (!betStrategyTemplate.getIseff()) {
				continue;
			}
			
			for (BetInfoDto betInfoDto : betList) {
				BetStrategy betStrategy = BetStrategyRegistCenter
						.newBetStrategy(betStrategyTemplate.getClass().getSimpleName());
				betStrategy.setMoney(betStrategyTemplate.getMoney());
				betStrategy.setIsTest(betStrategyTemplate.getIsTest());
				betStrategy.setIsCover(betStrategyTemplate.getIsCover());
				betStrategy.setContinuePlanMoney(betStrategyTemplate.getContinuePlanMoney());
				betStrategy.setWinRule(betStrategyTemplate.getWinRule());
				betStrategy.setMaxEachday(betStrategyTemplate.getMaxEachday());
				betStrategy.setContinueMaxMatch(betStrategyTemplate.getContinueMaxMatch());
				betStrategy.initParam(betStrategyTemplate.getParamMap());
				betStrategy.setLevel(betStrategyTemplate.getLevel());
				betStrategy.setBetStrategyName(betStrategyTemplate.getStrategyName());
				betStrategy.setIseff(betStrategyTemplate.getIseff());
				betStrategy.setgId(betStrategyTemplate.getgId());
				betStrategy.setUid(betStrategyTemplate.getUid());
				betStrategy.setRuleId(betStrategyTemplate.getRuleId());
				betStrategy.setBetStrategyContext(betStrategyContext);
				BallEvent ballEvent = new BallEvent(betInfoDto.getGid(), betInfoDto);
				if (!isNewBallEvent(betStrategy.getRuleId(), ballEvent.getGameId(),
						betInfoDto.getLastUpdate())) {
					// log.info("球赛信息没有变化，球赛ID：" + ballEvent.getGameId());
					continue;
				}
				log.info("执行全局投注规则比较，规则ID：" + betStrategy.getRuleId() + ",球赛ID："
						+ ballEvent.getGameId());
				refreshGameMapTime(betStrategy.getRuleId(), ballEvent.getGameId(),
						betInfoDto.getLastUpdate());
				if (betStrategy.isSatisfy(ballEvent)) {
					try {
						if (betLock.tryLock(lockTimeOutInSec, TimeUnit.SECONDS)) {
							Object betResp = betStrategy.betNow(ballEvent);
							if (betResp != null) {
								BetLog betLog = new BetLog();
								BeanCopy.copy(betLog, betResp, true);
								betLog.setBet_rule_id(betStrategy.getRuleId());
								betLog.setBallAccount(this.getBetStrategyContext().getBallAccount());
								betStrategyContext.getBetLogService()
										.save(betStrategyContext.getAccount(), betLog);
								//对追加的策略，在批次投注表追加投注日志明细
								if (betStrategy.getContinueMaxMatch() != null
										&& betStrategy.getBwContinue() != null && betLog.getCode().equals("560")) {
									betStrategy.getBetStrategyContext().getBwContinueService()
											.addNewMatch(betStrategy.getBwContinue(),
													betLog.getId(),
													Float.valueOf(betLog.getGold()));
								}
							}
						}
					} catch (Exception e) {
						BetStrategyErrorLogger.logger.error(e.getMessage(), e);
					} finally {
						betLock.unlock();
					}

					return;
				}
			}
		}
	}

	/**
	 * 半自动的下注策略，针对指定比赛
	 */
	public void runHalfAutoStratgeys() {
		log.info(String.format("betStrategyList size:%s ,%s ,%s ,%s", betStrategys.size(),
				betStrategyContext.getAccount(), betStrategyContext.getGtype(),
				betStrategyContext.getPtype()));
		List<BetInfoDto> betList = this.getBetStrategyContext().getBetInfoList();
		// 处理单场规则设置
		for (BetStrategy betStrategy : betStrategys) {
			if (betStrategy.getgId() == null)
				continue;
			if (!betStrategy.getIseff()) {
				continue;
			}
			for (BetInfoDto betInfoDto : betList) {
				BallEvent ballEvent = new BallEvent(betInfoDto.getGid(), betInfoDto);
				if (ballEvent.getGameId().equals(betStrategy.getgId())) {
					if (!isNewBallEvent(betStrategy.getRuleId(), ballEvent.getGameId(),
							betInfoDto.getLastUpdate())) {
						// log.info("球赛信息没有变化，球赛ID：" + ballEvent.getGameId());
						continue;
					}
					log.info("执行单场投注规则比较，规则ID：" + betStrategy.getRuleId() + ",球赛ID："
							+ ballEvent.getGameId());
					refreshGameMapTime(betStrategy.getRuleId(), ballEvent.getGameId(),
							betInfoDto.getLastUpdate());
					Boolean isSatisfy = false;
					try {
						isSatisfy = betStrategy.isSatisfy(ballEvent);
					} catch (BusiException e1) {
						log.info(e1.getMessage());
						betStrategyContext.getBetRuleService().updateDesc(betStrategy.getRuleId(),
								e1.getMessage());
					}
					if (isSatisfy) {
						try {
							if (betLock.tryLock(lockTimeOutInSec, TimeUnit.SECONDS)) {
								log.info("准备下注，球赛：" + GsonUtil.getGson().toJson(ballEvent));
								Object betResp = betStrategy.betNow(ballEvent);
								if (betResp != null) {
									log.info("成功下注，球赛ID：" + ballEvent.getGameId());
									// 设置为失效
									betStrategy.set2Ineff();
									BetLog betLog = new BetLog();
									BeanCopy.copy(betLog, betResp, true);
									betLog.setBet_rule_id(betStrategy.getRuleId());
									betStrategyContext.getBetLogService()
											.save(betStrategyContext.getAccount(), betLog);
									betStrategyContext.getBetRuleService().update2Success(
											betStrategy.getRuleId(), betLog.getId());
								} else {
									log.info("下注失败，球赛ID：" + ballEvent.getGameId());
								}
							}
						} catch (InterruptedException e) {
							BetStrategyErrorLogger.logger.error(e.getMessage(), e);
						} finally {
							betLock.unlock();
						}
					}
				}
			}
		}

	}

}
