package com.ewing.order.core.cache;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.collect.Lists;

/**
 * 
 * @author tanson lam
 * @create 2016年11月12日
 * 
 */
public class CacheEntityKey implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CacheModelType cacheModelType;
	private String key;
	private String[] fields;

	public CacheEntityKey(CacheModelType cacheModelType, String key) {
		super();
		this.cacheModelType = cacheModelType;
		this.key = key;
	}

	public CacheEntityKey(CacheModelType cacheModelType, String key,
			String[] fields) {
		super();
		this.cacheModelType = cacheModelType;
		this.key = key;
		this.fields = fields;
	}

	public CacheModelType getCacheModelType() {
		return cacheModelType;
	}

	public void setCacheModelType(CacheModelType cacheModelType) {
		this.cacheModelType = cacheModelType;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}

	public static List<CacheEntityKey> getRemoveKeys(
			List<CacheEntityKey> before, List<CacheEntityKey> after) {
		if (CollectionUtils.isEmpty(before))
			return Lists.newArrayList();
		if (CollectionUtils.isEmpty(after))
			return before;

		List<CacheEntityKey> removeKeys = Lists.newArrayList();
		for (CacheEntityKey beforeKey : before) {
			boolean found = false;
			for (CacheEntityKey afterKey : after) {
				if (afterKey.getKey().equals(beforeKey.getKey())) {
					found = true;
					break;
				}
			}
			if (!found)
				removeKeys.add(beforeKey);
		}
		return removeKeys;
	}

	public static List<CacheEntityKey> getRemoveKeysByUpdateInfo(
			List<CacheEntityUpdateInfo> beforeUpdateInfo,
			List<CacheEntityUpdateInfo> afterUpdateInfo) {
		List<CacheEntityKey> beforeCacheKey = Lists.newArrayList();
		List<CacheEntityKey> afterCacheKey = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(beforeUpdateInfo)) {
			for (CacheEntityUpdateInfo updateInfo : beforeUpdateInfo) {
				beforeCacheKey.addAll(updateInfo.getCacheKeys());
			}
		}

		if (CollectionUtils.isNotEmpty(afterUpdateInfo)) {
			for (CacheEntityUpdateInfo updateInfo : afterUpdateInfo) {
				afterCacheKey.addAll(updateInfo.getCacheKeys());
			}
		}
		return getRemoveKeys(beforeCacheKey, afterCacheKey);
	}

	@Override
	public String toString() {
		return "CacheEntityKey [cacheModelType=" + cacheModelType + ", key="
				+ key + ", fields=" + Arrays.toString(fields) + "]";
	}

}
