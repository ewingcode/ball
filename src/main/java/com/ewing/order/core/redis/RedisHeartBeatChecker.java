package com.ewing.order.core.redis;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;

import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * redis的心跳检测
 * 
 * @author tanson lam
 * @create 2016年9月28日
 */

public class RedisHeartBeatChecker {
	private static Logger log = CacheLogger.logger;

	private final static AtomicInteger jedisConnectionCounter = new AtomicInteger(
			0);
	private final static Integer MAX_FAIL_COUNT = 3;

	private final static RedisHeartBeatChecker singleIns = new RedisHeartBeatChecker();

	private AtomicBoolean isStarted = new AtomicBoolean(false);
	/**
	 * 当发生redis故障切换后对缓存全部更新
	 */
	private static Runnable switchOverAction;

	public static RedisHeartBeatChecker getSingle() {
		return singleIns;
	}

	public static void setSwitchOverAction(Runnable switchOverActionImpl) {
		switchOverAction = switchOverActionImpl;
	}

	static class MonitorThread extends Thread {

		public MonitorThread() {
			super("RedisHeartBeatChecker");
		}

		@Override
		public void run() {
			while (true) {
				heartbeat();
			}
		}

		public void heartbeat() {
			if (CacheConfig.getRedisOpen()) {
				try {
					RedisManage.getInstance().touchAllShards();
					try {
						if (jedisConnectionCounter.get() > 0) {
							jedisConnectionCounter.set(0);
							if (switchOverAction != null)
								switchOverAction.run();
						}
					} catch (Exception e) {
						log.error("found error while switchOverAction,", e);
					}
				} catch (Throwable e) {
					try {
						if (ExceptionUtils.indexOfThrowable(e,
								JedisConnectionException.class) > -1) {
							log.error("found error in RedisHeartBeatChecker "
									+ e.getMessage());
							if (jedisConnectionCounter.get() < 100) {
								jedisConnectionCounter.incrementAndGet();
							}
						}

					} catch (Exception e1) {
					}
				}
			}
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {

			}
		}
	}

	public static Boolean isHealthConnection() {
		return !(jedisConnectionCounter.get() >= MAX_FAIL_COUNT);
	}

	public void startMonitor() {
		try {
			if (!isStarted.getAndSet(true)) {
				try {
					new MonitorThread().start();
				} catch (Throwable e) {
					log.error(e.getMessage(), e);
					isStarted.set(false);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		RedisHeartBeatChecker.getSingle().startMonitor();
	}
}
