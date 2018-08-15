package com.ewing.order.core.cache;

import java.util.Map;

/**
 * 
 * @author tanson lam
 * @create 2016��9��19��
 * 
 */
public class PreparedCacheModel {
	private CacheModelType cacheModelType;
	private Map<String, ?> cacheData;
	private Integer expireSeconds;

	public PreparedCacheModel(CacheModelType cacheModelType, Map<String, ?> cacheData) {
		super();
		this.cacheModelType = cacheModelType;
		this.cacheData = cacheData;
	}

	public PreparedCacheModel(CacheModelType cacheModelType, Map<String, ?> cacheData,
			Integer expireSeconds) {
		super();
		this.cacheModelType = cacheModelType;
		this.cacheData = cacheData;
		this.expireSeconds = expireSeconds;
	}

	public Integer getExpireSeconds() {
		return expireSeconds;
	}

	public CacheModelType getCacheModelType() {
		return cacheModelType;
	}

	public Map<String, ?> getCacheData() {
		return cacheData;
	}

}
