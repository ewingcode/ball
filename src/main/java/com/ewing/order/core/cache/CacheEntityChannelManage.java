package com.ewing.order.core.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;

import com.ewing.order.core.redis.CacheConfig;
import com.ewing.order.core.redis.CacheLogger;
import com.ewing.order.core.redis.RedisManage;
import com.ewing.order.util.JsonUtils;
import com.google.common.collect.Maps;

import redis.clients.jedis.JedisPubSub;

/**
 * 缓存实体对象更新消息管理类
 * 
 * @author tanson lam
 * @create 2016年11月13日
 * 
 */
public class CacheEntityChannelManage {
	protected final static Logger logger = CacheLogger.logger;
	private final static String UPDATE_CHANNEL_SUFFIX = "UPDATE_CHANNEL";
	private final static String FLUSH_ALL_SUFFIX = "FLUSH_ALL";
	private final AbstractCache<?> abstractCache;
	private final WaitUpdateCacheManage waitUpdateCacheManage;
	private AtomicBoolean isInitListener = new AtomicBoolean(false);

	public CacheEntityChannelManage(AbstractCache<?> abstractCache) {
		this.abstractCache = abstractCache;
		this.waitUpdateCacheManage = new WaitUpdateCacheManage();
	}

	protected String buildChannelKey(String key, String suffix) {
		StringBuffer sb = new StringBuffer();
		if (!key.endsWith("_"))
			key += "_";
		sb.append(key).append(suffix);
		return sb.toString();
	}

	public void sendFlushAll(String entityPrefixKey) {
		try {
			RedisManage.getInstance().publish(buildChannelKey(entityPrefixKey, FLUSH_ALL_SUFFIX), "FLUSHNOW");
		} catch (Exception e) {
			logger.error("fail to sendFlushAll  for entityPrefixKey" + entityPrefixKey, e);
		}
	}

	public void sendBeforeInfo(String entityPrefixKey, List<CacheEntityUpdateInfo> updateInfoList) {
		for (CacheEntityUpdateInfo updateInfo : updateInfoList) {
			String content = "";
			try {
				updateInfo.setEntityNoticeEvent(EntityNoticeEvent.BEFORE);
				content = JsonUtils.toJson(updateInfo);
				RedisManage.getInstance().publish(buildChannelKey(entityPrefixKey, UPDATE_CHANNEL_SUFFIX),
						JsonUtils.toJson(updateInfo));

			} catch (Exception e) {
				logger.error("fail to sendBeforeInfo content:" + content, e);
			}
		}
	}

	public void sendAfterInfo(String entityPrefixKey, List<CacheEntityUpdateInfo> updateInfoList) {
		for (CacheEntityUpdateInfo updateInfo : updateInfoList) {
			String content = "";
			try {
				updateInfo.setEntityNoticeEvent(EntityNoticeEvent.AFTER);
				content = JsonUtils.toJson(updateInfo);
				RedisManage.getInstance().publish(buildChannelKey(entityPrefixKey, UPDATE_CHANNEL_SUFFIX),
						JsonUtils.toJson(updateInfo));
			} catch (Exception e) {
				logger.error("fail to sentAfterInfo content:" + content, e);
			}
		}
	}

	public void initChannelListener() {
		if (!isInitListener.getAndSet(true)) {
			// initBeforeUpdateListener();
			initUpdateListener();
			initFlushAllListener();
			waitUpdateCacheManage.initDelayCheck(abstractCache);
		}
	}

	private void initFlushAllListener() {
		final String flushAllChannel = buildChannelKey(abstractCache.prefixKey(), FLUSH_ALL_SUFFIX);
		Thread flushAllThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						RedisManage.getInstance().subscribe(flushAllChannel, new JedisPubSub() {
							public void onMessage(String channel, String message) {
								try {
									CacheLogger.debug("receive channel " + channel + " message:" + message);
									CacheLogger.debug("receive flush command and refresh local cache for:"
											+ abstractCache.getClass().getSimpleName());
									abstractCache.getLocalCacheManage().usingTmpCache();
									boolean ret = abstractCache.initAllEntity2LocalCache();
									if (ret)
										abstractCache.getLocalCacheManage().temp2LocalCache();
								} catch (Exception e) {
									logger.error("fail to receive channel" + channel + " message:" + message, e);
								}
							}
						});
					} catch (Exception e) {
						logger.error("Lost connection to " + flushAllChannel + ". Sleeping 10000ms and retrying.");
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}

			}
		});
		flushAllThread.setName("CACHE_" + flushAllChannel + "_THREAD");
		flushAllThread.start();
	}

	private void initUpdateListener() {
		final String afterChannel = buildChannelKey(abstractCache.prefixKey(), UPDATE_CHANNEL_SUFFIX);
		Thread afterThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						RedisManage.getInstance().subscribe(afterChannel, new JedisPubSub() {
							public void onMessage(String channel, String message) {
								try {
									CacheLogger.debug("receive channel " + channel + " message:" + message);
									CacheEntityUpdateInfo afterUpdateInfo = JsonUtils.jsonToObject(message,
											CacheEntityUpdateInfo.class);
									EntityNoticeEvent entityNoticeEvent = afterUpdateInfo.getEntityNoticeEvent();
									if (entityNoticeEvent == null
											|| EntityNoticeEvent.AFTER.equals(entityNoticeEvent)) {
										CacheEntityUpdateInfo beforeUpdateInfo = waitUpdateCacheManage
												.getAndClearBeforeUpdateInfo(afterUpdateInfo);
										abstractCache.refreshEntity2LocalCache(beforeUpdateInfo, afterUpdateInfo);
									}
									if (EntityNoticeEvent.BEFORE.equals(entityNoticeEvent)) {
										waitUpdateCacheManage.addUpdateInfo(
												JsonUtils.jsonToObject(message, CacheEntityUpdateInfo.class));

									}
								} catch (Exception e) {
									logger.error("fail to receive channel" + channel + " message:" + message, e);
								}
							}
						});
					} catch (Exception e) {
						logger.error("Lost connection to " + afterChannel + ". Sleeping 10000ms and retrying.");
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}

			}
		});
		afterThread.setName("CACHE_" + afterChannel + "_THREAD");
		afterThread.start();
	}

	static class WaitUpdateCacheManage {
		private Map<String, CacheEntityUpdateInfo> waitUpdateMap = Maps.newConcurrentMap();

		private AtomicBoolean isStarted = new AtomicBoolean(false);

		public void addUpdateInfo(CacheEntityUpdateInfo cacheEntityUpdateInfo) {
			cacheEntityUpdateInfo.setReceiveTime(System.currentTimeMillis());
			waitUpdateMap.put(cacheEntityUpdateInfo.getPrimaryKey(), cacheEntityUpdateInfo);
		}

		public CacheEntityUpdateInfo getAndClearBeforeUpdateInfo(CacheEntityUpdateInfo cacheEntityUpdateInfo) {
			return waitUpdateMap.remove(cacheEntityUpdateInfo.getPrimaryKey());
		}

		public void initDelayCheck(final AbstractCache<?> abstractCache) {
			if (!isStarted.getAndSet(true)) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						while (true) {
							try {
								for (String key : waitUpdateMap.keySet()) {
									CacheEntityUpdateInfo updateInfo = waitUpdateMap.get(key);
									if (abstractCache != null
											&& updateInfo.isTimeOut(CacheConfig.getLocalCacheWaitQueueTimeOut())) {
										logger.warn("process timeout cache update info:" + updateInfo);
										abstractCache.refreshEntity2LocalCache(updateInfo.getShopId(),
												updateInfo.getEntityId(), updateInfo.getCacheKeys());
										waitUpdateMap.remove(key);
									}
								}

							} catch (Exception e) {
								logger.error(e.getMessage(), e);
							}

							try {
								TimeUnit.MILLISECONDS.sleep(CacheConfig.getLocalCacheWaitQueueCheckTime());
							} catch (InterruptedException e) {
								logger.error(e.getMessage(), e);
							}

						}
					}
				}).start();
			}
		}
	}

}
