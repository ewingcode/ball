package com.ewing.order.ball.event;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewing.order.busi.ball.ddl.BetLog;
import com.ewing.order.util.BeanCopy;

/**
 * 投注策略池
 * 
 * @author tansonlam
 * @create 2018年7月30日
 */
public class BetStrategyPool {
	private static Logger log = LoggerFactory.getLogger(BetStrategyPool.class);

	/**
	 * 单个赛局的投注策略
	 */
	private List<BetStrategy> betStrategys = new CopyOnWriteArrayList<>();

	private BetStrategyContext betStrategyContext = new BetStrategyContext();

	private final static int lockTimeOutInSec = 5;

	private Lock betLock = new ReentrantLock();

	private Object reloadLock = new Object();

	private BetRuleParser betRuleParser;

	private AtomicBoolean isLoad = new AtomicBoolean(false);

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
				betStrategyContext.getUid(), betStrategyContext.getBetRuleService());
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					List<BetStrategy> betStrategyList = betRuleParser.hasNewBetStrategy();
					reload(betStrategyList);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}, 1000, 60000);

	}

	/**
	 * 加载投注策略
	 * 
	 * @param betStrategys
	 * @param basicBetStrategy
	 */
	public void reload(List<BetStrategy> betStrategys) {
		if (CollectionUtils.isEmpty(betStrategys))
			return;

		synchronized (reloadLock) {
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

	public void runStratgeys(BallEvent ballEvent) {
		synchronized (reloadLock) {
			// 处理单场规则设置
			for (BetStrategy betStrategy : betStrategys) {
				if (betStrategy.gameId() == null)
					continue;
				if (ballEvent.getGameId().equals(betStrategy.gameId())) {
					if (betStrategy.isSatisfy(ballEvent)) {
						try {
							if (betLock.tryLock(lockTimeOutInSec, TimeUnit.SECONDS)) {
								Object betResp = betStrategy.betNow(ballEvent);
								if (betResp != null) {
									BetLog betLog = new BetLog();
									BeanCopy.copy(betLog, betResp, true);
									betStrategyContext.getBetLogService()
											.save(betStrategyContext.getAccount(), betLog);
								}
							}
						} catch (InterruptedException e) {
							log.error(e.getMessage(), e);
						} finally {
							betLock.unlock();
						}
					}
					return;
				}
			}

			// 处理全局规则设置
			for (BetStrategy betStrategy : betStrategys) {
				if (betStrategy.gameId() != null)
					continue;

				if (betStrategy.isSatisfy(ballEvent)) {
					try {
						if (betLock.tryLock(lockTimeOutInSec, TimeUnit.SECONDS)) {
							Object betResp = betStrategy.betNow(ballEvent);
							if (betResp != null) {
								BetLog betLog = new BetLog();
								BeanCopy.copy(betLog, betResp, true);
								betStrategyContext.getBetLogService()
										.save(betStrategyContext.getAccount(), betLog);
							}
						}
					} catch (InterruptedException e) {
						log.error(e.getMessage(), e);
					} finally {
						betLock.unlock();
					}

					return;
				}
			}
		}
	}

}
