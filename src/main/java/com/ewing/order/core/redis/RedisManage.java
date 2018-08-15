package com.ewing.order.core.redis;

import org.slf4j.Logger;

/**
 * redis工具类
 * 
 * @author tansonlam
 * @createDate 2016年2月1日
 * 
 */
public class RedisManage extends RedisOperatorImpl implements RedisOperator {

	private static Logger log = CacheLogger.logger;

	private PoolManager poolManager;

	/**
	 * 单例实例
	 */
	private static RedisManage redisManage = null;

	private RedisManage() {
		try {
			poolManager = new PoolManager();
			ShardedJedisSentinelPool pool = poolManager.initPool();
			init(pool);
		} catch (Throwable e) {
			log.error("fail to init redisManage");

		}
	}

	public void reloadPool() {
		ShardedJedisSentinelPool newPool = poolManager.reloadPool();
		if (newPool != null) {
			pool = newPool;
		}
	}
	/**
	 * 获取单实例，延迟初始化
	 * @return
	 */
	public static RedisManage getInstance() {
		if (redisManage == null) {
			synchronized (RedisManage.class) {
				if (redisManage != null) {
					return redisManage;
				}
				redisManage = new RedisManage();
			}
		}
		return redisManage;
	}

}