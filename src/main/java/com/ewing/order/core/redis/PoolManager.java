package com.ewing.order.core.redis;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;

/**
 * Redis连接池管理
 * 
 * @author tanson lam
 * @create 2016年7月18日
 */
public class PoolManager {
	private static Logger log = CacheLogger.logger;

	/**
	 * 连接池
	 */
	public ShardedJedisSentinelPool pool = null;

	private String currentSentinel;

	private Object lockObject = new Object();

	/**
	 * 初始化连接池
	 * 
	 * @return
	 * @throws Exception
	 */
	public ShardedJedisSentinelPool initPool() throws Exception {
		synchronized (lockObject) {
			log.info("begin to initialize the redis pool...");
			pool = newPool();
			return pool;
		}
	}

	public List<MasterSlaveHostAndPort> getCurrentHostMaster() {
		return pool.getCurrentHostMaster();
	}

	private ShardedJedisSentinelPool newPool() throws Exception {
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxIdle(CacheConfig.getClientIdleConnection());
		config.setMaxTotal(CacheConfig.getClientMaxConnection());
		config.setTestOnReturn(true);
		config.setTestWhileIdle(true);
		config.setMaxWaitMillis(2000);
		config.setTestOnBorrow(true);
		CacheConfig connectInfo = new CacheConfig();
		Set<String> masters = connectInfo.getMasters();
		Set<String> sentinels = connectInfo.getSentinelUrl();
		if (StringUtils.isEmpty(connectInfo.getPwd())) {
			return new ShardedJedisSentinelPool(masters, sentinels, config, 60000);
		} else {
			return new ShardedJedisSentinelPool(masters, sentinels, config, connectInfo.getPwd());
		}
	}

	public String getSentinel() {
		return currentSentinel;
	}

	/**
	 * 重构连接池,当前sentinel不同于配置中心的时候，才允许重新初始化pool
	 * 
	 * @throws Exception
	 */
	public ShardedJedisSentinelPool reloadPool() {
		synchronized (lockObject) {
			if (pool != null) {
				log.info("begin to destroy the redis pool...");
				pool.destroy();
			}

			try {
				log.info("begin to reload the redis pool...");
				pool = newPool();
				log.info("finish reload the redis pool...");
				return pool;
			} catch (Exception e) {
				log.info("error occur while reload redis's pool.");
				return null;
			}

		}
	}
}
