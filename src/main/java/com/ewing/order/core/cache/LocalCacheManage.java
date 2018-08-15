package com.ewing.order.core.cache;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.ewing.order.core.redis.CacheConfig;
import com.ewing.order.core.redis.CacheLogger;
import com.ewing.order.core.redis.RedisException;
import com.ewing.order.util.ThreadLocalPool;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import redis.clients.jedis.Tuple;

/**
 * 本地缓存操作
 * 
 * @author tanson lam
 * @create 2016年11月13日
 * 
 */

@SuppressWarnings("unchecked")
public class LocalCacheManage {
	protected final static Logger logger = CacheLogger.logger;
	private static ThreadLocal<ConcurrentMap<String, Object>> tempCacheMap = ThreadLocalPool
			.createThreadLocal();
	/**
	 * 本地缓存map
	 */
	private ConcurrentMap<String, Object> LOCALCACHEMAP = Maps.newConcurrentMap();
	/**
	 * 设置空值，加快查询
	 */
	private ConcurrentMap<String, Object> NULLCACHEMAP = Maps.newConcurrentMap();

	private static final String NULLVALUE_SUFFIXKEY = "_NULLKEY";
	private AtomicBoolean autoRefresh = new AtomicBoolean(false);
	private final AbstractCache<?> abstractCache;

	public LocalCacheManage(AbstractCache<?> abstractCache) {
		this.abstractCache = abstractCache;
	}

	private ConcurrentMap<String, Object> getCacheMap() {
		if (tempCacheMap.get() != null)
			return tempCacheMap.get();
		return LOCALCACHEMAP;
	}

	public void usingTmpCache() {
		ConcurrentMap<String, Object> temp = Maps.newConcurrentMap();
		tempCacheMap.set(temp);
	}

	public void temp2LocalCache() {
		try {
			ConcurrentMap<String, Object> tempMap = tempCacheMap.get();
			if (MapUtils.isNotEmpty(tempMap)) {
				LOCALCACHEMAP = tempMap;
			}
		} finally {
			tempCacheMap.remove();
		}
	}

	/**
	 * 启动实体缓存的监听器
	 * 
	 * @param refreshAction
	 * @param interval
	 */
	public void initEntityCacheRefresher() {
		if (!autoRefresh.getAndSet(true)) {
			Thread localRefreshThread = new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						try {
							if (CacheConfig.getLocalCacheOpen()
									&& CacheConfig.getLocalRefreshOpen()) {
								CacheLogger.debug("refresh local cache for:"
										+ abstractCache.getClass().getSimpleName());
								usingTmpCache();
								boolean ret = abstractCache.initAllEntity2LocalCache();
								if (ret)
									temp2LocalCache();
							}
						} catch (Exception e1) {
							logger.error(e1.getMessage(), e1);
						}
						try {
							TimeUnit.MILLISECONDS.sleep(CacheConfig.getLocalRefreshTime());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
			localRefreshThread.setName(
					"LOCALREFRESH_" + abstractCache.getClass().getSimpleName() + "_THREAD");
			localRefreshThread.start();
		}
	}

	/**
	 * 设置缓存值
	 * 
	 * @param key
	 * @param object
	 * @throws RedisException
	 */
	public final void setCache(String key, Object object) {
		if (StringUtils.isEmpty(key))
			return;

		getCacheMap().put(key, object);
	}

	/**
	 * 批量设置缓存值，
	 * 
	 * @param cacheModel
	 * @throws RedisException
	 */
	public final <T> void batchSetCache(Map<String, T> cacheModel) {
		if (MapUtils.isEmpty(cacheModel))
			return;

		getCacheMap().putAll(cacheModel);
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
	public final <T> Map<String, T> batchHget(String key, String... fields) {
		ConcurrentMap<String, T> valueMap = (ConcurrentMap<String, T>) getCacheMap().get(key);
		if (MapUtils.isEmpty(valueMap) || ArrayUtils.isEmpty(fields))
			return Maps.newHashMap();

		Map<String, T> resultMap = Maps.newConcurrentMap();
		for (String field : fields) {
			resultMap.put(field, valueMap.get(key));
		}
		return resultMap;
	}

	/**
	 * 获取缓存值
	 * 
	 * @param key
	 * @throws RedisException
	 */

	public final <T> T getCache(final String key) {
		return (T) getCacheMap().get(key);
	}

	/**
	 * 获取哈希缓存值
	 * 
	 * @param key
	 * @throws RedisException
	 */
	public final <T> Map<String, T> hgetAllCache(final String key) {
		return (Map<String, T>) getCacheMap().get(key);
	}

	/**
	 * 删除指定哈希列表多个key
	 * 
	 * @param key
	 * @param fields
	 * @throws RedisException
	 */

	public final <T> long hdelCache(String key, String... fields) {
		ConcurrentMap<String, T> valueMap = (ConcurrentMap<String, T>) getCacheMap().get(key);
		if (MapUtils.isEmpty(valueMap) || ArrayUtils.isEmpty(fields))
			return 0;

		int ret = 0;
		for (String field : fields) {
			valueMap.remove(field);
			ret++;
		}

		if (valueMap.isEmpty())
			getCacheMap().remove(key);
		return ret;
	}

	/**
	 * 删除缓存值
	 * 
	 * @param key
	 * @throws RedisException
	 */
	public final void deleteCache(String key) {
		getCacheMap().remove(key);
	}

	/**
	 * 删除缓存值
	 * 
	 * @param cacheModel
	 * @throws RedisException
	 */
	public final void deleteCache(Map<String, ?> cacheModel) {
		if (MapUtils.isEmpty(cacheModel))
			return;

		for (String key : cacheModel.keySet()) {
			getCacheMap().remove(key);
		}
	}

	/**
	 * 批量设置多个哈希缓存值
	 * 
	 * @param valueMap
	 * @throws RedisException
	 */
	public <T> void batchHset(Map<String, Map<String, T>> valueMap) {
		if (MapUtils.isEmpty(valueMap))
			return;

		for (String key : valueMap.keySet()) {
			ConcurrentMap<String, T> innerValueMap = (ConcurrentMap<String, T>) getCacheMap()
					.get(key);
			if (innerValueMap == null) {
				innerValueMap = Maps.newConcurrentMap();
				ConcurrentMap<String, T> returnMap = (ConcurrentMap<String, T>) getCacheMap()
						.putIfAbsent(key, innerValueMap);
				if (returnMap != null)
					innerValueMap = returnMap;
			}
			innerValueMap.putAll(valueMap.get(key));
		}
	}

	/**
	 * 批量获取缓存
	 * 
	 * @param keys
	 * @return
	 * @throws RedisException
	 */
	public <T> Map<String, T> batchGet(final String... keys) {
		if (ArrayUtils.isEmpty(keys))
			return Maps.newHashMap();

		Map<String, T> resultMap = Maps.newConcurrentMap();
		for (String key : keys) {
			resultMap.put(key, (T) getCacheMap().get(key));
		}
		return resultMap;
	}

	public void setNullValue(String key) {
		NULLCACHEMAP.put(key + NULLVALUE_SUFFIXKEY, 1);
	}

	public Boolean hasNullValue(String key) {
		return NULLCACHEMAP.get(key + NULLVALUE_SUFFIXKEY) != null;
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
		if (MapUtils.isEmpty(scoreMembers))
			return 0l;
		TreeSet<Tuple> value = (TreeSet<Tuple>) LOCALCACHEMAP.get(key);
		if (value == null) {
			value = new TreeSet<Tuple>();
			TreeSet<Tuple> returnSet = (TreeSet<Tuple>) getCacheMap().putIfAbsent(key, value);
			if (returnSet != null)
				value = returnSet;
		}
		long opt = 0l;
		for (T t : scoreMembers.keySet()) {
			value.add(new Tuple(String.valueOf(t), scoreMembers.get(t)));
			opt++;
		}
		return opt;
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
		if (StringUtils.isEmpty(key) || member == null)
			return 0l;
		TreeSet<Tuple> value = (TreeSet<Tuple>) LOCALCACHEMAP.get(key);
		if (value == null)
			return 0l;
		Tuple removeOne = null;
		for (Tuple tuple : value) {
			if (tuple.getElement().equals(String.valueOf(member))) {
				removeOne = tuple;
			}
		}
		if (removeOne != null) {
			value.remove(removeOne);
			return 1l;
		}
		return 0l;
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
		if (StringUtils.isEmpty(key) || start == null || end == null || start > end)
			return Lists.newArrayList();
		TreeSet<Tuple> value = (TreeSet<Tuple>) LOCALCACHEMAP.get(key);
		if (value == null)
			return Lists.newArrayList();
		int valueLen = value.size() - 1;
		start = valueLen < start ? valueLen : start;
		end = valueLen < end ? valueLen : end;
		int i = 0;
		List<T> rList = Lists.newArrayList();
		for (Tuple t : value) {
			if (i >= start && i <= end) {
				rList.add((T) t.getElement());
			}
			i++;
		}
		return rList;
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
		if (StringUtils.isEmpty(key) || start == null || end == null || start > end)
			return Lists.newArrayList();
		TreeSet<Tuple> value = (TreeSet<Tuple>) LOCALCACHEMAP.get(key);
		if (value == null)
			return Lists.newArrayList();
		int valueLen = value.size() - 1;
		start = valueLen < start ? valueLen : start;
		end = valueLen < end ? valueLen : end;
		List<Tuple> tupleList = Lists.newArrayList();
		for (Tuple t : value) {
			tupleList.add(t);
		}
		Collections.reverse(tupleList);
		int i = 0;
		List<T> rList = Lists.newArrayList();
		for (Tuple t : tupleList) {
			if (i >= start && i <= end) {
				rList.add((T) t.getElement());
			}
			i++;
		}
		return rList;
	}

	/**
	 * 有序列表，获取列表大小
	 * 
	 * @param key
	 * @return
	 * @throws RedisException
	 */
	public Long zcard(String key) throws RedisException {
		if (StringUtils.isEmpty(key))
			return 0l;
		TreeSet<Tuple> value = (TreeSet<Tuple>) LOCALCACHEMAP.get(key);
		if (value == null)
			return 0l; 
		return Long.valueOf(value.size());
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
		return Long.valueOf(zrangeWithScores(key, min, max).size());
	}
}
