package com.ewing.order.core.cache;

import com.ewing.order.core.redis.RedisException;

/**
 * 定义缓存操作
 * 
 * @author tansonlam 2016年7月14日
 * 
 */
public abstract class FetchCacheOperatorInterface<Result, CacheResult> {

	/**
	 * 重新获取对象
	 * 
	 * @return
	 */
	public abstract Result refreshEntity();

	/**
	 * 从缓存获取对象
	 * 
	 * @return
	 * @throws RedisException
	 */
	public abstract CacheResult getFromCache() throws RedisException;

	/**
	 * 刷新缓存
	 * 
	 * @param result
	 * @return
	 * @throws RedisException
	 */
	public void refreshCache(Result result) {
		throw new UnsupportedOperationException();
	};

	public abstract String localCacheKey();

	public String getNullLocalCacheKey() {
		return localCacheKey() == null ? null : "NULLVALUE_" + localCacheKey();
	}

}
