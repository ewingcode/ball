package com.ewing.order.core.cache;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;

import com.ewing.order.core.redis.CacheConfig;
import com.ewing.order.core.redis.CacheLogger;
import com.ewing.order.core.redis.RedisException;
import com.ewing.order.core.redis.RedisHeartBeatChecker;

/**
 * @author tansonlam 2016年7月14日
 * 
 */
public class FetchCacheManager {
	protected final static Logger logger = CacheLogger.logger;

	private static Boolean isEmptyResult(Object resultObj) {
		if (resultObj == null)
			return true;

		if (resultObj instanceof Collection) {
			if (CollectionUtils.isEmpty((Collection<?>) resultObj)) {
				return true;
			}
		} else if (resultObj instanceof Map) {
			if (MapUtils.isEmpty((Map<?, ?>) resultObj)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <EntityResult, CacheResult> CacheResult getData(AbstractCache cacheObj,
			FetchCacheOperatorInterface<EntityResult, CacheResult> cacheOperator)
			throws RedisException {
		long start = System.currentTimeMillis();
		String key=null;
		try {
			key = cacheOperator.localCacheKey() != null ? cacheOperator.localCacheKey() : "";
			if(!CacheConfig.redisOpen)
				return (CacheResult) cacheOperator.refreshEntity();
			
			// 如果使用了本地缓存，则直接返回
			if (CacheConfig.localCacheOpen && cacheObj.useLocalCache()) {
				try {
					return cacheOperator.getFromCache();
				} catch (Exception e) {
					logger.error("fail to get data from local cache ,try to fetch data from Redis."
							+ cacheOperator.localCacheKey(), e);
				}
			}

			// 假如redis的连接不是正常中，就直接查询数据返回
			if (!RedisHeartBeatChecker.isHealthConnection()) {
				logger.warn("redis is not healthy now , return entity data by querying the DB.");
				return (CacheResult) cacheOperator.refreshEntity();
			}
			

			if (cacheOperator.localCacheKey() != null
					&& LocalThreadCacheProxy.isUsingThreadLocalCache()) {
				Object nullObject = cacheObj
						.getNullValueInLocalThread(cacheOperator.getNullLocalCacheKey());
				if (nullObject != null) {
					CacheLogger.debug("local thread hit null value for key["
							+ cacheOperator.localCacheKey() + "]");
					return null;
				}
			}

			CacheResult cacheRes = null;
			try {
				cacheRes = cacheOperator.getFromCache();
			} catch (Throwable e) {
				logger.error("fail to get data from remote cache ,try to fetch data from DB." + key, e);
				return (CacheResult) cacheOperator.refreshEntity();
			}
			// 缓存不为空直接返回
			if (cacheRes != null && !isEmptyResult(cacheRes))
				return cacheRes;

			// 缓存为空的时候，先查看本地缓存是否有设置空值，有则直接返回 null
			if (cacheOperator.localCacheKey() != null
					&& cacheObj.getLocalCacheManage().hasNullValue(key)) {
				return null;
			}

			// 缓存值为空时候重新获取对象值，当查询结果不为空则设置缓存
			EntityResult entityRes = cacheOperator.refreshEntity();

			if (entityRes != null) {
				try {
					cacheOperator.refreshCache(entityRes);
				} catch (UnsupportedOperationException e) {
					// 设置缓存为查询线程，这样更新缓存的时候会使用setnx
					cacheObj.beginQueryThreadLocal();
					try {
						if (entityRes instanceof Collection) {
							cacheObj.updateCacheForQueryEntityList((List) entityRes);
						} else {
							cacheObj.updateCacheForQueryEntityList(Arrays.asList(entityRes));
						}
						// 假如更新缓存失败，那就直接从DB获取数据
					} catch (Throwable e1) {
						logger.error("fail to update data for cache ,try to fetch data from DB." + key,
								e1);
						return (CacheResult) cacheOperator.refreshEntity();
					} finally {
						cacheObj.cleanQueryThreadLocal();
					}
				}
				return (CacheResult) entityRes;
			}
			if (cacheOperator.localCacheKey() != null) {
				// 当查询数据为空后设置NULL值到本地缓存中
				cacheObj.getLocalCacheManage().setNullValue(key);

				if (LocalThreadCacheProxy.isUsingThreadLocalCache())
					cacheObj.setNullValueInLocalThread(cacheOperator.getNullLocalCacheKey());
			}
			CacheLogger.debug("no found data for key[" + key + "]");
			return null;
		}finally {
			CacheLogger.debug("cache[ "+key+" ] cost: "+(System.currentTimeMillis()-start));
		}
	}

}
