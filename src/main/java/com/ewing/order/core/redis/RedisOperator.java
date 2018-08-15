package com.ewing.order.core.redis;

import java.util.List;
import java.util.Map;

public interface RedisOperator {

	/**
	 * 获取单个对象缓存,随机从主从集群获取，如果在从库获取失败会重试到主库获取数据。
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public <T> T get(String key) throws RedisException;

	/**
	 * 从主是获取单个对象缓存
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public <T> T getByMaster(String key) throws RedisException;

	/**
	 * 删除对象缓存
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Long del(String key) throws RedisException;

	/**
	 * 取消缓存KEY的失效时间
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public void persist(String key) throws RedisException;

	/**
	 * 设置指定缓存KEY的失效时间
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 * @throws Exception
	 */
	public void expire(String key, Integer seconds) throws RedisException;

	/**
	 * 设置单个对象缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @throws RedisException
	 */
	public <T> String set(String key, T value) throws RedisException;

	/**
	 * 设置单个对象缓存
	 * 
	 * @param key
	 * @param value
	 * @param expireSeconds
	 *            生效时间，单位秒
	 * @return
	 * @throws RedisException
	 */
	public <T> String set(String key, T value, Integer expireSeconds) throws RedisException;

	/**
	 * 设置单个对象缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @throws RedisException
	 */
	public <T> void batchSet(Map<String, T> valueMap) throws RedisException;

	/**
	 * 设置单个对象缓存，如果已经有值则不设置
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @throws RedisException
	 */
	public <T> void batchSetnx(Map<String, T> valueMap) throws RedisException;

	/**
	 * 使用哈希设置缓存值
	 * 
	 * @param key
	 * @param score
	 * @param member
	 * 
	 * @return
	 * @throws RedisException
	 */
	public <T> Long hset(String key, String field, T value) throws RedisException;

	/**
	 * 获取所有哈希列表所有值
	 * 
	 * @param key
	 * @return
	 * @throws RedisException
	 */
	public <T> Map<String, T> hgetAll(String key) throws RedisException;

	/**
	 * 删除对象缓存
	 * 
	 * @param key
	 * @param fields
	 * @return
	 * @throws Exception
	 */
	public Long hdel(String key, String... fields) throws RedisException;

	/**
	 * 批量设置哈希缓存值
	 * 
	 * @param key
	 * @param fieldValue
	 * @return
	 * @throws RedisException
	 */
	/*
	 * public <T> String hmset(String key, Map<String, T> fieldValue) throws
	 * RedisException;
	 */

	/**
	 * 批量设置多个哈希缓存值
	 * 
	 * @param valueMap
	 * @throws RedisException
	 */
	public <T> void batchHset(Map<String, Map<String, T>> valueMap) throws RedisException;

	/**
	 * 获取哈希设置缓存值
	 * 
	 * @param key
	 * @param field
	 * 
	 * @return
	 * @throws RedisException
	 */
	public <T> T hget(String key, String field) throws RedisException;

	/**
	 * 批量获取哈希设置缓存值
	 * 
	 * @param key
	 * @param fields
	 * 
	 * @return
	 * @throws RedisException
	 */
	public <T> Map<String, T> batchHget(String key, String... fields) throws RedisException;

	/**
	 * 从主节点实时获取哈希设置缓存值
	 * 
	 * @param key
	 * @param field
	 * @return
	 * @throws RedisException
	 */
	public <T> T hgetByMaster(String key, String field) throws RedisException;

	/**
	 * 批量获取缓存
	 * 
	 * @param keys
	 * @return
	 * @throws RedisException
	 */
	public <T> Map<String, T> batchGet(String... keys) throws RedisException;

	/**
	 * 查询緩存键值总数
	 * 
	 * @param prefixKey
	 * @return
	 */
	public Long countByPrefixKey(String prefixKey);

	/**
	 * 设置緩存键值失效时间
	 * 
	 * @param prefixKey
	 * @return
	 */
	public Long expireByPrefixKey(String prefixKey, int seconds);

	/**
	 * 获取分布式锁
	 * 
	 * @param lockKey
	 *            竞争获取锁key
	 * @param acquireTimeoutInMS
	 *            获取锁超时时间
	 * @param lockTimeoutInMS
	 *            锁的超时时间
	 * @return
	 */

	public DistributedLockResp acquireLock(String lockKey, long acquireTimeoutInMS, long lockTimeoutInMS);

	/**
	 * 解锁
	 * 
	 * @param lockKey
	 */
	public void releaseLock(String lockKey);

	/**
	 * 有序列表，按照score排序，支持批量增加
	 * 
	 * @param key
	 * @param scoreMembers
	 *            Value->分数
	 * @return
	 * @throws RedisException
	 */
	public <T> long zadd(String key, Map<T, Double> scoreMembers) throws RedisException;

	/**
	 * 有序列表，指定删除列表中成员
	 * 
	 * @param key
	 * @param scoreMembers
	 *            Value->分数
	 * @return
	 * @throws RedisException
	 */
	public <T> long zrem(String key, T member) throws RedisException;

	/**
	 * 有序列表，按分数升序获取数据
	 * 
	 * @param key
	 * @param start
	 *            开始
	 * @param end
	 *            结束
	 * @return list
	 * @throws RedisException
	 */
	public <T> List<T> zrangeWithScores(String key, Long start, Long end) throws RedisException;

	/**
	 * 有序列表，按分数降序获取数据
	 * 
	 * @param key
	 * @param start
	 *            开始
	 * @param end
	 *            结束
	 * @return list
	 * @throws RedisException
	 */
	public <T> List<T> zrevrangeWithScores(String key, Long start, Long end) throws RedisException;
	
	/**
	 * 有序列表，获取列表大小
	 * @param key
	 * @return
	 * @throws RedisException
	 */
	public Long zcard(String key) throws RedisException;
	
	/**
	 * 有序列表，获取分数内列表的大小
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 * @throws RedisException
	 */
	public Long zcount(String key, Long min, Long max) throws RedisException;
}
