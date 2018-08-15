package com.ewing.order.core.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.ewing.order.core.app.service.BaseModelService;
import com.ewing.order.core.redis.CacheConfig;
import com.ewing.order.core.redis.CacheLogger;
import com.ewing.order.core.redis.RedisException;
import com.ewing.order.util.MD5Util;
import com.ewing.order.util.PageUtil;
import com.ewing.order.util.ReflectUtil;
import com.ewing.order.util.ThreadLocalPool;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 基础数据缓存更新策略的抽象类
 * 
 * @author tansonlam
 * @create 2016年8月5日
 */
public abstract class AbstractCache<Entity> {
	protected final static Logger logger = CacheLogger.logger;

	protected final Integer SORTLIST_EXPIRESECOND = 20 * 60;
	/**
	 * 防止多线程并发设置缓存
	 */
	protected static ThreadLocal<Boolean> queryThreadLocal = ThreadLocalPool.createThreadLocal();
	/**
	 * 本地缓存
	 */
	private LocalCacheManage localCache;
	/**
	 * 远程缓存
	 */
	private RemoteCacheManage remoteCacheManage;
	/**
	 * 推送更新消息给本地缓存更新
	 */
	private CacheEntityChannelManage cacheEntityChannelManage;
	/**
	 * 缓存失效时间
	 */
	protected final static Integer KEY_SHORTTIME_SECONDS = 120;

	private EntityListContext<Entity> entityListContext;
	@Resource
	protected BaseModelService baseModelService;

	public AbstractCache() {
		cacheEntityChannelManage = new CacheEntityChannelManage(this);
		localCache = new LocalCacheManage(this);
		remoteCacheManage = new RemoteCacheManage();
		entityListContext = new EntityListContext<Entity>();
	}

	protected LocalCacheManage getLocalCacheManage() {
		return localCache;
	}

	protected CacheEntityChannelManage getCacheEntityChannelManage() {
		return cacheEntityChannelManage;
	}

	protected List<Long> stringToList(String newvalue) {
		List<Long> ids = new ArrayList<Long>();
		newvalue = newvalue.replace("[", "");
		newvalue = newvalue.replace("]", "");
		String[] contents = newvalue.split("\\,");
		for (int i = 0; i < contents.length; i++) {
			try {
				Long contentId = Long.parseLong(contents[i].trim());
				ids.add(contentId);
			} catch (Exception e) {
				logger.error("分割字符串异常:", e);
			}
		}
		return ids;
	}

	public abstract String prefixKey();

	/**
	 * 初始化缓存
	 * 
	 * @throws Exception
	 */
	public abstract void initialCache() throws Exception;

	/**
	 * 更新缓存
	 * 
	 * @param shopId
	 * @param ids
	 * @return
	 */
	public abstract List<Entity> updateCacheData(Long shopId, List<Long> ids);

	/**
	 * 对比前后更新的KEY变化，删除不使用的KEY
	 * 
	 * @param removeKeys
	 */
	private void deleteUnuseKey4RemoteCache(Long shopId, List<Entity> entityList, String batchId) {
		if (CollectionUtils.isEmpty(entityList) || StringUtils.isEmpty(batchId))
			return;

		List<CacheEntityUpdateInfo> beforeUpdateInfo = wrapEntityUpdateInfo(shopId,
				entityListContext.getEntityList(batchId), batchId);
		List<CacheEntityUpdateInfo> afterUpdateInfo = wrapEntityUpdateInfo(shopId, entityList,
				batchId);
		List<CacheEntityKey> removeKeys = CacheEntityKey.getRemoveKeysByUpdateInfo(beforeUpdateInfo,
				afterUpdateInfo);
		// 删除不使用的KEY
		for (CacheEntityKey cacheEntityKey : removeKeys) {
			CacheModelType cacheModelType = cacheEntityKey.getCacheModelType();
			String key = cacheEntityKey.getKey();
			String[] fields = cacheEntityKey.getFields();
			try {
				if (CacheModelType.KEY_VALUE.equals(cacheModelType)) {
					remoteCacheManage.deleteCache(key);
				} else if (CacheModelType.MAP_INDEX.equals(cacheModelType)) {
					remoteCacheManage.hdelCache(key, fields);
				} else if (CacheModelType.MAP.equals(cacheModelType)) {
					remoteCacheManage.hdelCache(key, fields);
				} else if (CacheModelType.SORTSET.equals(cacheModelType)) {
					for (String id : fields)
						remoteCacheManage.zrem(key, id);
				}
			} catch (RedisException e) {
				throw new RedisException("fail to delete key for :" + cacheEntityKey.getKey());
			}
		}
	}

	/**
	 * 把原来设置了时间变短的key恢复永久时间
	 * 
	 * @param shopId
	 * @param batchId
	 */
	private void persistCache(Long shopId, String batchId) {
		List<CacheEntityUpdateInfo> beforeUpdateInfo = wrapEntityUpdateInfo(shopId,
				entityListContext.getEntityList(batchId), batchId);
		// 把原来设置了时间变短的key恢复永久时间
		if (CollectionUtils.isEmpty(beforeUpdateInfo))
			return;

		for (CacheEntityUpdateInfo updateInfo : beforeUpdateInfo) {
			if (CollectionUtils.isEmpty(updateInfo.getCacheKeys()))
				continue;

			for (CacheEntityKey cacheEntityKey : updateInfo.getCacheKeys()) {
				try {
					remoteCacheManage.persist(cacheEntityKey.getKey());
				} catch (RedisException e) {
					logger.error("fail to persist for :" + cacheEntityKey.getKey());
				}
			}

		}

	}

	/**
	 * 更新缓存
	 * 
	 * @param shopId
	 * @param ids
	 */
	public final void updateCache(Long shopId, List<Long> ids, String batchId) {
		try {
			// 更新远程缓存
			List<Entity> entityList = updateCacheData(shopId, ids);
			if (CollectionUtils.isNotEmpty(entityList)) {
				try {
					// 处理之后的缓存操作，删除不使用的key
					deleteUnuseKey4RemoteCache(shopId, entityList, batchId);
				} catch (Exception e) {
					logger.error("fail to deleteUnuseKey4RemoteCache shopId:" + shopId + " ids:"
							+ ids + " error:" + e.getMessage(), e);
				}
				// 发送实体更新各节点本地缓存的消息
				if (CacheConfig.localCacheOpen && useLocalCache() && batchId != null) {
					cacheEntityChannelManage.sendAfterInfo(prefixKey(),
							wrapEntityUpdateInfo(shopId, entityList, batchId));
				}
			} else {
				// 如果对象列表为空则直接删除
				deleteEntityCache(batchId);
				// 发送删除的更新消息到各节点本地缓存的消息
				if (CacheConfig.localCacheOpen && useLocalCache() && batchId != null) {
					cacheEntityChannelManage.sendAfterInfo(prefixKey(),
							wrapEntityUpdateInfoByIds(shopId, ids, batchId));
				}
			}
		} finally {
			try {
				// 把原来设置了时间变短的key恢复永久时间
				persistCache(shopId, batchId);
				entityListContext.removeEntityList(batchId);
			} catch (Exception e) {
				logger.error("shopId:" + shopId + " ids:" + ids + " error:" + e.getMessage(), e);
			}
		}
	}

	/**
	 * 查询对象
	 * 
	 * @param shopId
	 * @param ids
	 * @return
	 */
	public abstract List<Entity> queryEntitiyList(Long shopId, List<Long> ids);

	/**
	 * 是否使用本地缓存
	 * 
	 * @return
	 */
	public Boolean useLocalCache() {
		return false;
	}

	/**
	 * 查询所有实体对象
	 * 
	 * @return
	 */
	protected List<Entity> queryAllEntityList() throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 * 为对象集合转化成对象缓存KEY
	 * 
	 * @param shopId
	 * @param entityList
	 * @return
	 */
	private List<CacheEntityUpdateInfo> wrapEntityUpdateInfo(Long shopId, List<Entity> entityList,
			String batchId) {
		if (CollectionUtils.isEmpty(entityList))
			return Lists.newArrayList();
		List<CacheEntityUpdateInfo> updateInfo = Lists.newArrayList();
		for (Entity entity : entityList) {
			try {
				Long entityId = Long.valueOf(ReflectUtil.getReflectValue(entity, "id").toString());
				List<CacheEntityKey> cacheKeys = wrapEntityCacheKey(entity);
				updateInfo.add(new CacheEntityUpdateInfo(entityId, shopId, batchId, cacheKeys));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

		}
		return updateInfo;
	}

	private List<CacheEntityUpdateInfo> wrapEntityUpdateInfoByIds(Long shopId, List<Long> entityIds,
			String batchId) {
		if (CollectionUtils.isEmpty(entityIds))
			return Lists.newArrayList();
		List<CacheEntityUpdateInfo> updateInfo = Lists.newArrayList();
		for (Long entityId : entityIds) {
			try {
				updateInfo.add(new CacheEntityUpdateInfo(entityId, shopId, batchId,
						new ArrayList<CacheEntityKey>()));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

		}
		return updateInfo;
	}

	@SuppressWarnings("unchecked")
	private List<CacheEntityKey> wrapEntityCacheKey(Entity entity) {
		List<PreparedCacheModel> preparedCacheModels = wrap4CacheModelList(Arrays.asList(entity));
		List<CacheEntityKey> cacheKeys = Lists.newArrayList();
		for (PreparedCacheModel preparedCacheModel : preparedCacheModels) {

			if (preparedCacheModel.getCacheModelType() == null
					|| preparedCacheModel.getCacheData() == null
					|| preparedCacheModel.getCacheData().isEmpty())
				continue;
			if (CacheModelType.KEY_VALUE.equals(preparedCacheModel.getCacheModelType())) {
				Map<String, ?> map = preparedCacheModel.getCacheData();
				for (String key : map.keySet())
					cacheKeys.add(new CacheEntityKey(CacheModelType.KEY_VALUE, key));
			} else if (CacheModelType.MAP_INDEX.equals(preparedCacheModel.getCacheModelType())) {
				Map<String, List<Object>> indexMaps = (Map<String, List<Object>>) preparedCacheModel
						.getCacheData();
				for (String indexName : indexMaps.keySet()) {
					List<Object> entityList = indexMaps.get(indexName);
					List<String> fields = Lists.newArrayList();
					Map<String, Map<String, Object>> indexMap = Maps.newHashMap();
					Map<String, Object> valueMap = Maps.newHashMap();
					indexMap.put(indexName, valueMap);
					for (Object obj : entityList) {
						try {
							Object entityId = ReflectUtil.getReflectValue(obj, "id");
							if (entityId == null)
								continue;
							fields.add(entityId.toString());
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
						}
					}
					cacheKeys.add(new CacheEntityKey(CacheModelType.MAP_INDEX, indexName,
							fields.toArray(new String[fields.size()])));
				}
			} else if (CacheModelType.MAP.equals(preparedCacheModel.getCacheModelType())) {
				Map<String, Map<String, Object>> maps = (Map<String, Map<String, Object>>) preparedCacheModel
						.getCacheData();
				for (String key : maps.keySet()) {
					List<String> fields = Lists.newArrayList();
					for (String field : maps.get(key).keySet()) {
						fields.add(field);
					}
					cacheKeys.add(new CacheEntityKey(CacheModelType.MAP, key,
							fields.toArray(new String[fields.size()])));
				}
			} else if(CacheModelType.SORTSET.equals(preparedCacheModel.getCacheModelType())){
				Map<String, Map<Entity, Double>> scoreMembersMap = (Map<String, Map<Entity, Double>>) preparedCacheModel
						.getCacheData();
				if (MapUtils.isNotEmpty(scoreMembersMap)) {
					for (String key : scoreMembersMap.keySet()) { 
						List<String> fields = Lists.newArrayList();
						for (Entity obj : scoreMembersMap.get(key).keySet()) {
							try {
								Object entityId = ReflectUtil.getReflectValue(obj, "id");
								if (entityId == null)
									continue;
								fields.add(entityId.toString());
							} catch (Exception e) {
								logger.error(e.getMessage(), e);
							}
						}
						cacheKeys.add(new CacheEntityKey(CacheModelType.SORTSET, key,
								fields.toArray(new String[fields.size()])));
					}
				}
			}
		}
		return cacheKeys;
	}

	/**
	 * 初化全部实体对象到本地缓存
	 */
	public void initLocalCache() {
		if (!useLocalCache())
			return;
		// 初始化化本地缓存
		try {
			initAllEntity2LocalCache();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// 启动本地缓存自动刷新器
		localCache.initEntityCacheRefresher();
		// 启动对象变更消息监听
		cacheEntityChannelManage.initChannelListener();
	}

	protected boolean initAllEntity2LocalCache() throws Exception {
		List<Entity> allEntityList = queryAllEntityList();
		if (CollectionUtils.isEmpty(allEntityList))
			return false;

		logger.info(this.getClass().getName() + " init new local cache list size:"
				+ allEntityList.size());
		setEntity2LocalCache(allEntityList);
		return true;
	}

	/**
	 * 刷新实体对象到本地缓存，先比对更新前后已经不存在緩存KEY且进行删除操作，然后按照最后的缓存KEY到redis中获取值，
	 * 如果出现缓存获取失败，则直接查询db获取对象信息，刷新本地缓存
	 * 
	 * @param beforeUpdateInfo
	 * @param afterUpdateInfo
	 */
	protected void refreshEntity2LocalCache(CacheEntityUpdateInfo beforeUpdateInfo,
			CacheEntityUpdateInfo afterUpdateInfo) {
		if (afterUpdateInfo == null)
			return;

		List<CacheEntityKey> beforeKeys = beforeUpdateInfo != null ? beforeUpdateInfo.getCacheKeys()
				: new ArrayList<CacheEntityKey>();
		List<CacheEntityKey> afterKeys = afterUpdateInfo.getCacheKeys();
		// 比较是否有多余的key
		List<CacheEntityKey> removeKeys = CacheEntityKey.getRemoveKeys(beforeKeys, afterKeys);
		// 删除本地缓存KEY.
		deleteLocalCache(removeKeys);

		Long shopId = afterUpdateInfo.getShopId();
		Long entityId = afterUpdateInfo.getEntityId();
		boolean errInRedis = false;
		try {
			for (CacheEntityKey cacheEntityKey : afterKeys) {
				CacheModelType cacheModelType = cacheEntityKey.getCacheModelType();
				String key = cacheEntityKey.getKey();
				String[] fields = cacheEntityKey.getFields();
				if (CacheModelType.KEY_VALUE.equals(cacheModelType)) {
					Object cacheObject = remoteCacheManage.getCache(key);
					if (cacheObject == null)
						throw new RedisException("can not get value from redis for key:" + key);
					localCache.setCache(key, cacheObject);
				} else if (CacheModelType.MAP_INDEX.equals(cacheModelType)) {
					Map<String, Object> cacheObject = remoteCacheManage.batchHget(key, fields);
					if (MapUtils.isEmpty(cacheObject))
						throw new RedisException("can not get value from redis for key:" + key
								+ " fields:" + fields);
					Map<String, Map<String, Object>> indexMap = Maps.newConcurrentMap();
					indexMap.put(key, cacheObject);
					localCache.batchHset(indexMap);
				} else if (CacheModelType.MAP.equals(cacheModelType)) {
					Map<String, Object> cacheObject = remoteCacheManage.batchHget(key, fields);
					if (MapUtils.isEmpty(cacheObject))
						throw new RedisException("can not get value from redis for key:" + key
								+ " fields:" + fields);
					Map<String, Map<String, Object>> indexMap = Maps.newConcurrentMap();
					indexMap.put(key, cacheObject);
					localCache.batchHset(indexMap);
				}
			}
		} catch (Exception e) {
			errInRedis = true;
			logger.error(e.getMessage(), e);
		}

		if (errInRedis || CollectionUtils.isEmpty(afterKeys)) {
			refreshEntity2LocalCache(shopId, entityId, beforeKeys);
		}
	}

	/**
	 * 删除本地缓存
	 * 
	 * @param removeKeys
	 */
	private void deleteLocalCache(List<CacheEntityKey> removeKeys) {
		for (CacheEntityKey cacheEntityKey : removeKeys) {
			CacheModelType cacheModelType = cacheEntityKey.getCacheModelType();
			String key = cacheEntityKey.getKey();
			String[] fields = cacheEntityKey.getFields();
			if (CacheModelType.KEY_VALUE.equals(cacheModelType)) {
				localCache.deleteCache(key);
			} else if (CacheModelType.MAP_INDEX.equals(cacheModelType)) {
				localCache.hdelCache(key, fields);
			} else if (CacheModelType.MAP.equals(cacheModelType)) {
				localCache.hdelCache(key, fields);
			}
		}
	}

	public Boolean refreshEntity2LocalCache(Long shopId, Long entityId,
			List<CacheEntityKey> beforeKeys) {
		logger.info("try to refresh localcache for " + this.getClass().getSimpleName()
				+ " by query db. entityId:" + entityId + " shopId:" + shopId);
		List<Long> idList = Lists.newArrayList();
		idList.add(entityId);
		List<Entity> entity = queryEntitiyList(shopId, idList);
		if (CollectionUtils.isEmpty(entity) && CollectionUtils.isNotEmpty(beforeKeys)) {
			deleteLocalCache(beforeKeys);
			return false;
		}
		setEntity2LocalCache(entity);
		return true;
	}

	/**
	 * 初化全部实体对象到本地缓存
	 */
	@SuppressWarnings("unchecked")
	private void setEntity2LocalCache(List<Entity> entityList) {
		try {

			if (entityList == null || CollectionUtils.isEmpty(entityList)) {
				logger.warn("entityList for " + this.getClass().getSimpleName() + " is empty.");
				return;
			}

			List<PreparedCacheModel> preparedCacheModels = wrap4CacheModelList(entityList);
			for (PreparedCacheModel preparedCacheModel : preparedCacheModels) {
				if (preparedCacheModel.getCacheModelType() == null
						|| preparedCacheModel.getCacheData() == null
						|| preparedCacheModel.getCacheData().isEmpty())
					continue;
				if (CacheModelType.KEY_VALUE.equals(preparedCacheModel.getCacheModelType())) {
					// 设置本地缓存
					localCache.batchSetCache(preparedCacheModel.getCacheData());
				} else if (CacheModelType.MAP_INDEX
						.equals(preparedCacheModel.getCacheModelType())) {
					Map<String, List<Entity>> indexMaps = (Map<String, List<Entity>>) preparedCacheModel
							.getCacheData();
					Map<String, Map<String, Object>> cacheIndexMap = Maps.newHashMap();

					for (String indexName : indexMaps.keySet()) {
						Map<String, Object> valueMap = Maps.newHashMap();
						cacheIndexMap.put(indexName, valueMap);
						List<Entity> eList = indexMaps.get(indexName);
						for (Entity entity : eList) {
							try {
								Object entityId = ReflectUtil.getReflectValue(entity, "id");
								if (entityId == null)
									continue;
								valueMap.put(entityId + "", entityId);
								String entityKey = buildCacheKey(prefixKey(), entityId.toString());
								localCache.setCache(entityKey, entity);
							} catch (Exception e) {
								logger.error(e.getMessage(), e);
							}
						}
					}

					// 设置Index本地缓存
					localCache.batchHset(cacheIndexMap);
				} else if (CacheModelType.MAP.equals(preparedCacheModel.getCacheModelType())) {
					Map<String, Map<String, Object>> maps = (Map<String, Map<String, Object>>) preparedCacheModel
							.getCacheData();
					localCache.batchHset(maps);
				}
			}

		} catch (UnsupportedOperationException e) {
			logger.error("no implement for queryAllEntityList in " + this.getClass().getName());
		} catch (Exception e) {
			logger.error("occur error in initThreadLocalCache ", e);
		}
	}

	/**
	 * 为更新或者删除的对象进行缓存
	 * 
	 * @param entityList
	 */
	protected void updateCacheForEntityList(List<Entity> entityList) {
		if (entityList == null || entityList.isEmpty())
			return;
		set4PreparedCacheModel(wrap4CacheModelList(entityList), false);
	}

	/**
	 * 为查询结果对象进行缓存
	 * 
	 * @param entityList
	 */
	protected void updateCacheForQueryEntityList(List<Entity> entityList) {
		if (entityList == null || entityList.isEmpty())
			return;
		set4PreparedCacheModel(wrap4CacheModelList(entityList), true);
	}

	protected void beginQueryThreadLocal() {
		queryThreadLocal.set(true);
	}

	protected void cleanQueryThreadLocal() {
		queryThreadLocal.remove();
	}

	protected Boolean isQueryThread() {
		Boolean obj = queryThreadLocal.get();
		return obj != null && obj;
	}

	@SuppressWarnings("unchecked")
	public void set4PreparedCacheModel(List<PreparedCacheModel> preparedCacheModels,
			boolean isQuery) {
		if (preparedCacheModels == null || preparedCacheModels.isEmpty())
			return;
		for (PreparedCacheModel preparedCacheModel : preparedCacheModels) {
			if (preparedCacheModel.getCacheModelType() == null
					|| preparedCacheModel.getCacheData() == null
					|| preparedCacheModel.getCacheData().isEmpty())
				continue;
			if (CacheModelType.KEY_VALUE.equals(preparedCacheModel.getCacheModelType())) {
				if (isQueryThread()) {
					remoteCacheManage.batchSetnxCache(preparedCacheModel.getCacheData());
				} else {
					remoteCacheManage.batchSetCache(preparedCacheModel.getCacheData());
				}

			} else if (CacheModelType.MAP_INDEX.equals(preparedCacheModel.getCacheModelType())) {
				Map<String, List<Entity>> indexMaps = (Map<String, List<Entity>>) preparedCacheModel
						.getCacheData();
				addMapIndex(indexMaps);
			} else if (CacheModelType.MAP.equals(preparedCacheModel.getCacheModelType())) {
				Map<String, Map<String, Object>> maps = (Map<String, Map<String, Object>>) preparedCacheModel
						.getCacheData();
				remoteCacheManage.batchHset(maps);
			} else if (CacheModelType.SORTSET.equals(preparedCacheModel.getCacheModelType())) {
				// 只有查询的时候

				Map<String, Map<Entity, Double>> scoreMembersMap = (Map<String, Map<Entity, Double>>) preparedCacheModel
						.getCacheData();
				if (MapUtils.isNotEmpty(scoreMembersMap)) {
					for (String key : scoreMembersMap.keySet()) {
						// 只有在查询和非查询且列表不为空，才对列表进行更新，可能有并发的问题，暂时忽略
						if (isQuery || (!isQuery && remoteCacheManage.zcard(key) > 0)) {
							addSortSet(key, scoreMembersMap.get(key));
						}
						if (isQuery && preparedCacheModel.getExpireSeconds() != null
								&& preparedCacheModel.getExpireSeconds() > 0)
							remoteCacheManage.expire(key, preparedCacheModel.getExpireSeconds());
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void delete4PreparedCacheModel(List<PreparedCacheModel> preparedCacheModels) {
		if (preparedCacheModels == null || preparedCacheModels.isEmpty())
			return;
		for (PreparedCacheModel preparedCacheModel : preparedCacheModels) {
			if (preparedCacheModel.getCacheModelType() == null
					|| preparedCacheModel.getCacheData() == null
					|| preparedCacheModel.getCacheData().isEmpty())
				continue;
			if (CacheModelType.KEY_VALUE.equals(preparedCacheModel.getCacheModelType())) {
				remoteCacheManage.deleteCache(preparedCacheModel.getCacheData());
			} else if (CacheModelType.MAP_INDEX.equals(preparedCacheModel.getCacheModelType())) {
				Map<String, List<Entity>> indexMaps = (Map<String, List<Entity>>) preparedCacheModel
						.getCacheData();
				for (String indexName : indexMaps.keySet()) {
					delMapIndex(indexName, indexMaps.get(indexName));
				}
			} else if (CacheModelType.MAP.equals(preparedCacheModel.getCacheModelType())) {
				Map<String, Map<String, Object>> maps = (Map<String, Map<String, Object>>) preparedCacheModel
						.getCacheData();
				for (String key : maps.keySet()) {
					for (String field : maps.get(key).keySet()) {
						remoteCacheManage.hdelCache(key, field);
					}
				}
			}
		}
	}

	/**
	 * 获取准备更新的实体集合
	 * 
	 * @return
	 */
	private List<Entity> getUpdatingEntityList(String batchId) {
		return entityListContext.getEntityList(batchId);
	}

	/**
	 * 减少之前缓存值的时间
	 * 
	 * @throws RedisException
	 */
	@SuppressWarnings("unchecked")
	public void shortCacheTime(Long shopId, List<Long> ids, String batchId) throws RedisException {
		List<Entity> entityList = queryEntitiyList(shopId, ids);
		if (entityList == null || entityList.isEmpty())
			return;
		entityListContext.setEntityList(batchId, entityList);
		try {
			List<PreparedCacheModel> preparedCacheModels = wrap4CacheModelList(entityList);

			for (PreparedCacheModel preparedCacheModel : preparedCacheModels) {

				if (preparedCacheModel.getCacheModelType() == null
						|| preparedCacheModel.getCacheData() == null
						|| preparedCacheModel.getCacheData().isEmpty())
					continue;
				if (CacheModelType.KEY_VALUE.equals(preparedCacheModel.getCacheModelType())) {
					Map<String, ?> map = preparedCacheModel.getCacheData();
					for (String key : map.keySet())
						remoteCacheManage.expire(key, KEY_SHORTTIME_SECONDS);
				} else if (CacheModelType.MAP_INDEX
						.equals(preparedCacheModel.getCacheModelType())) {

					Map<String, List<Entity>> indexMaps = (Map<String, List<Entity>>) preparedCacheModel
							.getCacheData();
					for (String indexName : indexMaps.keySet()) {
						remoteCacheManage.expire(indexName, KEY_SHORTTIME_SECONDS);
					}

				} else if (CacheModelType.MAP.equals(preparedCacheModel.getCacheModelType())) {

					Map<String, Map<String, Object>> maps = (Map<String, Map<String, Object>>) preparedCacheModel
							.getCacheData();
					for (String key : maps.keySet()) {
						remoteCacheManage.expire(key, KEY_SHORTTIME_SECONDS);
					}
				} else if (CacheModelType.SORTSET.equals(preparedCacheModel.getCacheModelType())) {
					Map<String, Map<Object, Double>> maps = (Map<String, Map<Object, Double>>) preparedCacheModel
							.getCacheData();
					for (String key : maps.keySet()) {
						remoteCacheManage.expire(key, KEY_SHORTTIME_SECONDS);
					}
				}
			}
		} catch (Exception e) {
			String errMsg = "fail to shortCacheTime for shopId" + shopId + " ids:"
					+ Arrays.toString(ids.toArray());
			logger.error(errMsg, e);
		}
		// 发送实体更新消息
		if (CacheConfig.localCacheOpen && useLocalCache() && batchId != null)
			cacheEntityChannelManage.sendBeforeInfo(prefixKey(),
					wrapEntityUpdateInfo(shopId, entityList, batchId));
	}

	/**
	 * 定义缓存KEY，一个对象可能以多种不同的key进行缓存的需要定义在这里
	 * 
	 * @param entity
	 * @return
	 */
	protected abstract List<PreparedCacheModel> wrap4CacheModelList(List<Entity> entity);

	/**
	 * 当查询为空的时候，为了避免重复查询数据库和缓存，在本地线程中为该KEY设置NULL值。
	 */
	protected void setNullValueInLocalThread(String key) {
		LocalThreadCache.LocalMap.put(key, LocalThreadCache.newNullBean());
	}

	/**
	 * 当查询为空的时候，为了避免重复查询数据库和缓存，在本地线程中为该KEY设置NULL值。
	 */
	protected Object getNullValueInLocalThread(String key) {
		return LocalThreadCache.LocalMap.get(key);
	}

	/**
	 * 删除实体缓存
	 */
	protected final void deleteEntityCache(String batchId) {
		List<Entity> deleteEntities = getUpdatingEntityList(batchId);
		if (deleteEntities == null || deleteEntities.isEmpty())
			return;
		try {
			List<PreparedCacheModel> preparedCacheModels = wrap4CacheModelList(deleteEntities);
			if (preparedCacheModels != null)
				delete4PreparedCacheModel(preparedCacheModels);
		} catch (Exception e) {
			logger.error("fail to delete entity cache.", e);
		}
	}

	protected Map<String, Object> getCacheModelList(List<Entity> entityList) {
		throw new UnsupportedOperationException();
	}

	protected static String buildCacheKey(String key, Object... objects) {
		if (StringUtils.isEmpty(key) || objects == null)
			throw new RedisException("param for key is null,[key:" + key + ",params:"
					+ Arrays.toString(objects) + "]");

		StringBuffer sb = new StringBuffer();
		if (!key.endsWith("_"))
			key += "_";
		sb.append(key);
		if (objects != null && objects.length > 0) {
			for (int i = 0; i < objects.length; i++) {
				if (objects[i] == null)
					throw new RedisException("param for key is null,[key:" + key + ",params:"
							+ Arrays.toString(objects) + "]");
				sb.append(objects[i].toString());
				if (i < objects.length - 1)
					sb.append("_");
			}
		}
		return sb.toString();
	}

	/**
	 * 以map结构添加缓存的元数据
	 * 
	 * @param indexName
	 * @param entityIds
	 */
	protected void addMapIndex(String indexName, List<Entity> entityList) {
		Map<String, Map<String, Object>> indexMap = Maps.newHashMap();
		Map<String, Object> valueMap = Maps.newHashMap();
		indexMap.put(indexName, valueMap);
		for (Entity entity : entityList) {
			try {
				Object entityId = ReflectUtil.getReflectValue(entity, "id");
				if (entityId == null)
					continue;
				valueMap.put(entityId + "", entityId);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		remoteCacheManage.batchHset(indexMap);
	}

	/**
	 * 以map结构添加缓存的元数据
	 * 
	 * @param indexName
	 * @param entityIds
	 */
	protected void addSortSet(String indexKey, Map<Entity, Double> memberScoreMap) {
		Map<String, Double> valueMap = Maps.newHashMap();
		for (Entity entity : memberScoreMap.keySet()) {
			try {
				Object entityId = ReflectUtil.getReflectValue(entity, "id");
				if (entityId == null)
					continue;
				valueMap.put(entityId + "", memberScoreMap.get(entity));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		remoteCacheManage.zadd(indexKey, valueMap);
	}

	/**
	 * 以map结构添加缓存的元数据
	 * 
	 * @param indexName
	 * @param entityIds
	 */
	protected void addMapIndex(Map<String, List<Entity>> indexMaps) {
		Map<String, Map<String, Object>> indexMap = Maps.newHashMap();

		for (String indexName : indexMaps.keySet()) {
			Map<String, Object> valueMap = Maps.newHashMap();
			indexMap.put(indexName, valueMap);
			List<Entity> entityList = indexMaps.get(indexName);
			for (Entity entity : entityList) {
				try {
					Object entityId = ReflectUtil.getReflectValue(entity, "id");
					if (entityId == null)
						continue;
					valueMap.put(entityId + "", entityId);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		remoteCacheManage.batchHset(indexMap);
	}

	protected void delMapIndex(String indexName, List<Entity> entityList) {
		Map<String, Map<String, Object>> indexMap = Maps.newHashMap();
		Map<String, Object> valueMap = Maps.newHashMap();
		indexMap.put(indexName, valueMap);
		for (Entity entity : entityList) {
			try {
				Object entityId = ReflectUtil.getReflectValue(entity, "id");
				if (entityId == null)
					continue;
				remoteCacheManage.hdelCache(indexName, entityId.toString());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	protected void sortQueryKeys(List<String> queryKeys) {
		Collections.sort(queryKeys, new Comparator<String>() {
			public int compare(String o1, String o2) {
				return Integer.valueOf(String.valueOf(o2.hashCode()))
						.compareTo(Integer.valueOf(String.valueOf(o1.hashCode())));
			};
		});
	}

	/**
	 * 返回元数据map关联的实体数据
	 * 
	 * @param indexName
	 * @param prefixKey
	 */
	protected <T> Map<String, T> getMapValueByIndex(String indexName, String prefixKey) {
		Map<String, Object> indexMap = hgetAllCache(indexName);
		if (indexMap == null || indexMap.isEmpty())
			return Maps.newHashMap();
		List<String> queryKeys = new ArrayList<String>();
		for (String key : indexMap.keySet()) {
			queryKeys.add(buildCacheKey(prefixKey, key));
		}

		Collections.sort(queryKeys, new Comparator<String>() {
			public int compare(String o1, String o2) {
				return Integer.valueOf(String.valueOf(o2.hashCode()))
						.compareTo(Integer.valueOf(String.valueOf(o1.hashCode())));
			};

		});

		Map<String, T> dataMap = batchGet(
				queryKeys.toArray(queryKeys.toArray(new String[queryKeys.size()])));

		return dataMap;
	}

	/**
	 * 返回元数据有序列表关联的实体数据，支持分页
	 * 
	 * @param indexName
	 * @param prefixKey
	 * @param page
	 * @param pageSize
	 */
	protected <T> Map<String, T> getSortListByIndex(String indexName, String prefixKey,
			Integer page, Integer pageSize) {
		long start = PageUtil.getOffset(page, pageSize);
		long end = start + pageSize;
		List<T> indexList = zrangeWithScores(indexName, start, end);
		if (CollectionUtils.isEmpty(indexList))
			return Maps.newHashMap();
		List<String> queryKeys = new ArrayList<String>();
		for (T key : indexList) {
			queryKeys.add(buildCacheKey(prefixKey, key));
		}

		Collections.sort(queryKeys, new Comparator<String>() {
			public int compare(String o1, String o2) {
				return Integer.valueOf(String.valueOf(o2.hashCode()))
						.compareTo(Integer.valueOf(String.valueOf(o1.hashCode())));
			};

		});

		Map<String, T> dataMap = batchGet(
				queryKeys.toArray(queryKeys.toArray(new String[queryKeys.size()])));

		return dataMap;
	}

	/**
	 * 对象列表进行分类，返回map
	 * 
	 * @param entityList
	 * @return map
	 */
	protected Map<String, List<Entity>> entityList2GroupMap(List<Entity> entityList,
			GroupMapOperation<Entity> groupMapOperation) {
		if (entityList == null || entityList.isEmpty())
			return Maps.newHashMap();

		Map<String, List<Entity>> resultMap = Maps.newHashMap();
		for (Entity dto : entityList) {
			groupMapOperation.dtoGroup(dto, resultMap);
		}
		return resultMap;
	}

	protected static interface GroupMapOperation<Entity> {
		public void dtoGroup(Entity entity, Map<String, List<Entity>> resultMap);
	}

	/**
	 * 对象列表进行分类，返回map
	 * 
	 * @param entityList
	 * @return map
	 */
	protected Map<String, Map<Entity, Double>> entityList2SortSet(List<Entity> entityList,
			SortSetOperation<Entity> sortSetOperation) {
		if (entityList == null || entityList.isEmpty())
			return Maps.newHashMap();

		Map<String, Map<Entity, Double>> resultMap = Maps.newHashMap();
		for (Entity dto : entityList) {
			sortSetOperation.dtoGroup(dto, resultMap);
		}
		return resultMap;
	}

	protected static interface SortSetOperation<Entity> {
		public void dtoGroup(Entity entity, Map<String, Map<Entity, Double>> resultMap);
	}

	/**
	 * 查询緩存键值总数
	 * 
	 * @param prefixKey
	 * @return
	 */
	public Long countByPrefixKey() {
		if (StringUtils.isEmpty(prefixKey()))
			throw new IllegalArgumentException("prefix key must be declared.");
		return remoteCacheManage.countByPrefixKey(buildCacheKey(prefixKey(), "*"));
	}

	/**
	 * 设置緩存键值失效时间
	 * 
	 * @param prefixKey
	 * @return
	 */
	public Long expireByPrefixKey() {
		return expireByPrefixKey(10);
	}

	public Long expireByPrefixKey(int seconds) {
		if (StringUtils.isEmpty(prefixKey()))
			throw new IllegalArgumentException("prefix key must be declared.");
		return remoteCacheManage.expireByPrefixKey(buildCacheKey(prefixKey(), "*"), seconds);
	}

	public static String buildLocalMapKey(String key, String field) {
		return key + ":" + field;
	}

	public static String buildLocalManyKey(String... keys) {
		return MD5Util.MD5(Arrays.toString(keys));
	}

	/**
	 * 获取缓存值
	 * 
	 * @param key
	 * @throws RedisException
	 */
	protected final <T> T getCache(final String key) throws RedisException {
		if (CacheConfig.isLocalCacheOpen() && useLocalCache()) {
			return localCache.getCache(key);
		} else {
			return remoteCacheManage.getCache(key);
		}
	}

	protected final <T> Map<String, T> hgetAllCache(final String key) throws RedisException {
		if (CacheConfig.isLocalCacheOpen() && useLocalCache()) {
			return localCache.hgetAllCache(key);
		} else {
			return remoteCacheManage.hgetAllCache(key);
		}
	}

	protected final <T> List<T> zrangeWithScores(final String key, long start, long end)
			throws RedisException {
		/*if (CacheConfig.isLocalCacheOpen() && useLocalCache()) {
			return localCache.zrangeWithScores(key, start, end);
		} else {*/
			return remoteCacheManage.zrangeWithScores(key, start, end);
		//}
	}

	public <T> Map<String, T> batchGet(final String... keys) throws RedisException {
		if (CacheConfig.isLocalCacheOpen() && useLocalCache()) {
			return localCache.batchGet(keys);
		} else {
			return remoteCacheManage.batchGet(keys);
		}
	}
}
