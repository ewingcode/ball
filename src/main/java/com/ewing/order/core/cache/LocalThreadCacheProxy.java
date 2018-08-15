package com.ewing.order.core.cache;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;

import com.ewing.order.core.redis.CacheLogger;
import com.ewing.order.core.redis.RedisException;
import com.ewing.order.util.ThreadLocalPool;

/**
 * 
 * @author tanson lam
 * @create 2016年11月13日
 * 
 */
public class LocalThreadCacheProxy {
	protected final static Logger logger = CacheLogger.logger;

	private static final ThreadLocal<Boolean> useCacheThreadLocal = ThreadLocalPool.createThreadLocal();

	public static void useThreadLocalCache() {
		try {
			useCacheThreadLocal.set(true);
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 是否针对页面访问提供数据缓存复用
	 * 
	 * @return
	 */
	public static Boolean isUsingThreadLocalCache() {
		try {
			return useCacheThreadLocal.get() != null;
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	public static void removeUseThreadLocalCache() {
		try {
			useCacheThreadLocal.remove();
			ThreadLocalPool.clean();
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static Boolean isEmptyResult(Object resultObj) {
		try {
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
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	public static <EntityResult, CacheResult> CacheResult getData(RemoteCacheManage cacheOperatorPorxy,
			FetchCacheOperatorInterface<EntityResult, CacheResult> cacheOperator) throws RedisException {
		try {
			CacheResult cacheRes = cacheOperator.getFromCache();

			// 缓存不为空直接返回
			if (cacheRes != null) {
				CacheLogger.debug("local thread cache for key[" + cacheOperator.localCacheKey() + "] ishit:"
						+ (cacheRes != null));
				return cacheRes;
			}
			// 缓存值为空时候重新获取对象值，当查询结果不为空则设置缓存
			EntityResult entityRes = cacheOperator.refreshEntity();
			if (entityRes != null && !LocalThreadCacheProxy.isEmptyResult(entityRes)) {
				cacheOperator.refreshCache(entityRes);
				return cacheOperator.getFromCache();
			}
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			throw new RedisException(e.getMessage(), e);
		}
		return null;

	}
}
