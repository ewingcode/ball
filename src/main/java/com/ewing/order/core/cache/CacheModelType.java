package com.ewing.order.core.cache;

/**
 * 缓存数据类型
 * 
 * @author tanson lam
 * @create 2016年9月19日
 * 
 */
public enum CacheModelType {
	/**
	 * 普通键值
	 */
	KEY_VALUE,
	/**
	 * Map
	 */
	MAP,
	/**
	 * Map保存数据索引结构是Map<ManyKey,ObjectId>
	 */
	MAP_INDEX, 
	/**
	 * 有序列表
	 */
	SORTSET;

}
