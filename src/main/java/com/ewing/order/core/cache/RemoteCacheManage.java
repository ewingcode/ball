package com.ewing.order.core.cache;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewing.order.core.redis.DistributedLockResp;
import com.ewing.order.core.redis.RedisException;
import com.ewing.order.core.redis.RedisManage;
import com.ewing.order.core.redis.RedisOperator;

public class RemoteCacheManage {
	public final static Logger logger = LoggerFactory.getLogger(RemoteCacheManage.class);

	public RemoteCacheManage() {
	}

	private RedisOperator getRedisOperator() {
		return RedisManage.getInstance();
	}

	/**
	 * 批量设置缓存值，
	 * 
	 * @param cacheModel
	 * @throws RedisException
	 */
	public final <T> void batchSetCache(Map<String, T> cacheModel) throws RedisException {
		if (MapUtils.isEmpty(cacheModel))
			return;
		getRedisOperator().batchSet(cacheModel);
	}

	/**
	 * 设置单个对象缓存，如果已经有值则不设置
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @throws RedisException
	 */
	public final <T> void batchSetnxCache(Map<String, T> valueMap) throws RedisException {
		if (MapUtils.isEmpty(valueMap))
			return;
		getRedisOperator().batchSetnx(valueMap);
	}

	/**
	 * 批量获取哈希设置缓存值
	 * 
	 * @param key
	 * @param fields
	 * 
	 * @return
	 * @throws RedisException
	 */
	public final <T> Map<String, T> batchHget(String key, String... fields) throws RedisException {
		return getRedisOperator().batchHget(key, fields);
	}

	/**
	 * 设置缓存值
	 * 
	 * @param cacheModel
	 * @throws RedisException
	 */
	public final void setCache(Map<String, Object> cacheModel) throws RedisException {
		if (MapUtils.isEmpty(cacheModel))
			return;
		for (String key : cacheModel.keySet()) {
			getRedisOperator().set(key, cacheModel.get(key));
		}
	}

	/**
	 * 设置缓存值
	 * 
	 * @param key
	 * @param object
	 * @throws RedisException
	 */
	public final void setCache(String key, Object object) throws RedisException {
		getRedisOperator().set(key, object);
	}

	/**
	 * 设置缓存值
	 * 
	 * @param key
	 * @param object
	 * @param expireSeconds
	 *            失效时间，单位秒
	 * @throws RedisException
	 */
	public final void setCache(String key, Object object, Integer expireSeconds)
			throws RedisException {
		getRedisOperator().set(key, object, expireSeconds);
	}

	/**
	 * 获取缓存值
	 * 
	 * @param key
	 * @throws RedisException
	 */
	public final <T> T getCache(final String key) throws RedisException {
		return getRedisOperator().get(key);
	}

	/**
	 * 获取哈希缓存值
	 * 
	 * @param key
	 * @throws RedisException
	 */
	public final <T> Map<String, T> hgetAllCache(final String key) throws RedisException {
		return getRedisOperator().hgetAll(key);
	}

	/**
	 * 设置哈希缓存值
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @throws RedisException
	 */
	public final <T> long hsetCache(String key, String field, T value) throws RedisException {
		return getRedisOperator().hset(key, field, value);
	}

	/**
	 * 删除指定哈希列表多个key
	 * 
	 * @param key
	 * @param fields
	 * @throws RedisException
	 */
	public final <T> long hdelCache(String key, String... fields) throws RedisException {
		return getRedisOperator().hdel(key, fields);
	}

	/**
	 * 删除缓存值
	 * 
	 * @param key
	 * @throws RedisException
	 */
	public final void deleteCache(String key) throws RedisException {
		getRedisOperator().del(key);
	}

	/**
	 * 从主节点实时获取哈希设置缓存值
	 * 
	 * @param key
	 * @param fieldValue
	 * @return
	 * @throws RedisException
	 */
	public <T> T hgetByMaster(final String key, final String field) {
		return getRedisOperator().hgetByMaster(key, field);
	}

	/**
	 * 删除缓存值
	 * 
	 * @param cacheModel
	 * @throws RedisException
	 */
	public final void deleteCache(Map<String, ?> cacheModel) throws RedisException {
		if (MapUtils.isEmpty(cacheModel))
			return;
		for (String key : cacheModel.keySet()) {
			getRedisOperator().del(key);
		}
	}

	/**
	 * 批量设置多个哈希缓存值
	 * 
	 * @param valueMap
	 * @throws RedisException
	 */
	public <T> void batchHset(Map<String, Map<String, T>> valueMap) throws RedisException {
		if (MapUtils.isEmpty(valueMap))
			return;
		getRedisOperator().batchHset(valueMap);
	}

	/**
	 * 批量获取缓存
	 * 
	 * @param keys
	 * @return
	 * @throws RedisException
	 */
	public <T> Map<String, T> batchGet(final String... keys) throws RedisException {
		return getRedisOperator().batchGet(keys);
	}

	/**
	 * 查询緩存键值总数
	 * 
	 * @param prefixKey
	 * @return
	 */
	public Long countByPrefixKey(String prefixKey) {
		return getRedisOperator().countByPrefixKey(prefixKey);
	}

	/**
	 * 设置緩存键值失效时间
	 * 
	 * @param prefixKey
	 * @return
	 */
	public Long expireByPrefixKey(String prefixKey, int seconds) {
		return getRedisOperator().expireByPrefixKey(prefixKey, seconds);
	}

	/**
	 * 设置指定缓存KEY的失效时间
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 * @throws Exception
	 */
	public void expire(String key, Integer seconds) throws RedisException {
		getRedisOperator().expire(key, seconds);
	}

	/**
	 * 取消缓存KEY的失效时间
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public void persist(String key) throws RedisException {
		getRedisOperator().persist(key);
	}

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

	public DistributedLockResp acquireLock(String lockKey, long acquireTimeoutInMS,
			long lockTimeoutInMS) {
		return getRedisOperator().acquireLock(lockKey, acquireTimeoutInMS, lockTimeoutInMS);
	}

	/**
	 * 解锁
	 * 
	 * @param lockKey
	 */
	public void releaseLock(String lockKey) {
		getRedisOperator().releaseLock(lockKey);
	}

	/**
	 * 有序列表，按照score排序，支持批量增加
	 * 
	 * @param key
	 * @param scoreMembers
	 *            Value->分数
	 * @return
	 * @throws RedisException
	 */
	public <T> long zadd(String key, Map<T, Double> scoreMembers) throws RedisException {
		return getRedisOperator().zadd(key, scoreMembers);
	}

	/**
	 * 有序列表，指定删除列表中成员
	 * 
	 * @param key
	 * @param scoreMembers
	 *            Value->分数
	 * @return
	 * @throws RedisException
	 */
	public <T> long zrem(String key, T member) throws RedisException {
		return getRedisOperator().zrem(key, member);
	}

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
	public <T> List<T> zrangeWithScores(String key, Long start, Long end) throws RedisException {
		return getRedisOperator().zrangeWithScores(key, start, end);
	}

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
	public <T> List<T> zrevrangeWithScores(String key, Long start, Long end) throws RedisException {
		return getRedisOperator().zrevrangeWithScores(key, start, end);
	}

	/**
	 * 有序列表，获取列表大小
	 * 
	 * @param key
	 * @return
	 * @throws RedisException
	 */
	public Long zcard(String key) throws RedisException {
		return getRedisOperator().zcard(key);
	}

	/**
	 * 有序列表，获取分数内列表的大小
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 * @throws RedisException
	 */
	public Long zcount(String key, Long min, Long max) throws RedisException {
		return getRedisOperator().zcount(key, min, max);
	}
}
