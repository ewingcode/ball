package com.ewing.order.core.redis;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * redis连接信息
 * 
 * @author tanson lam
 * @create 2016年7月18日
 */
@Component
@ConfigurationProperties(prefix = "redis")
public class CacheConfig {
	public static String password;
	public static Boolean redisOpen;
	public static String sentinelUrl;
	public static String masters;
	public static Boolean statlogOpen;
	public static Boolean debugOpen;
	public static Long localRefreshTime;
	public static Boolean localCacheOpen;
	public static Boolean localRefreshOpen;
	public static Long localCacheWaitQueueTimeOut;
	public static Long localCacheWaitQueueCheckTime;
	private static Integer CLIENT_MAX_CONNECTION = 300;
	private static Integer CLIENT_IDLE_CONNECTION = 25;
	private static Integer MAX_TRY_GETCONNECTION = 2;

	public static boolean isLocalCacheOpen() {
		return localCacheOpen;
	}

	public static Long getLocalCacheWaitQueueCheckTime() {
		return localCacheWaitQueueCheckTime;
	}

	public static void setLocalCacheWaitQueueCheckTime(Long localCacheWaitQueueCheckTime) {
		CacheConfig.localCacheWaitQueueCheckTime = localCacheWaitQueueCheckTime;
	}

	public static Long getLocalCacheWaitQueueTimeOut() {
		return localCacheWaitQueueTimeOut;
	}

	public static void setLocalCacheWaitQueueTimeOut(Long localCacheWaitQueueTimeOut) {
		CacheConfig.localCacheWaitQueueTimeOut = localCacheWaitQueueTimeOut;
	}

	public static Boolean getLocalCacheOpen() {
		return localCacheOpen;
	}

	public static void setLocalCacheOpen(Boolean localCacheOpen) {
		CacheConfig.localCacheOpen = localCacheOpen;
	}

	public static Boolean getLocalRefreshOpen() {
		return localRefreshOpen;
	}

	public static void setLocalRefreshOpen(Boolean localRefreshOpen) {
		CacheConfig.localRefreshOpen = localRefreshOpen;
	}

	public static Boolean getRedisOpen() {
		return redisOpen;
	}

	public static void setRedisOpen(Boolean redisOpen) {
		CacheConfig.redisOpen = redisOpen;
	}

	public static Long getLocalRefreshTime() {
		return localRefreshTime;
	}

	public static void setLocalRefreshTime(Long localRefreshTime) {
		CacheConfig.localRefreshTime = localRefreshTime;
	}

	public static Integer getClientMaxConnection() {
		return CLIENT_MAX_CONNECTION;
	}

	public static Integer getClientIdleConnection() {
		return CLIENT_IDLE_CONNECTION;
	}

	public static Integer getMaxTryGetconnection() {
		return MAX_TRY_GETCONNECTION;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		CacheConfig.password = password;
	}

	public static Boolean getStatlogOpen() {
		return statlogOpen;
	}

	public static void setStatlogOpen(Boolean statlogOpen) {
		CacheConfig.statlogOpen = statlogOpen;
	}

	public static Boolean getDebugOpen() {
		return debugOpen;
	}

	public static void setDebugOpen(Boolean debugOpen) {
		CacheConfig.debugOpen = debugOpen;
	}

	public static void setSentinelUrl(String sentinelUrl) {
		CacheConfig.sentinelUrl = sentinelUrl;
	}

	public static void setMasters(String masters) {
		CacheConfig.masters = masters;
	}

	/**
	 * 获取主从分组名称
	 * 
	 * @return
	 * @throws Exception
	 */
	public Set<String> getMasters() throws Exception {
		Set<String> masterSet = new HashSet<String>();
		if (StringUtils.isEmpty(masters))
			throw new Exception("no found configuration of redis's masters.");
		String[] masterArray = masters.split(",");
		for (String masterName : masterArray)
			masterSet.add(masterName);
		return masterSet;
	}

	/**
	 * 获取Sentinels连接信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public Set<String> getSentinelUrl() throws Exception {
		Set<String> sentinelSet = new HashSet<String>();
		if (StringUtils.isEmpty(sentinelUrl))
			throw new RedisException("no found configuration of redis's sentinels.");
		String[] sentinelArray = sentinelUrl.split(",");
		for (String sentinel : sentinelArray)
			sentinelSet.add(sentinel);
		return sentinelSet;
	}

	/**
	 * 获取Redis连接密码
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getPwd() throws Exception {
		return password;
	}

	/**
	 * 缓存日志开关
	 * 
	 * @return
	 */
	public static Boolean isStatLogOpen() {
		return statlogOpen;
	}

	/**
	 * DEBUG开关
	 * 
	 * @return
	 */
	public static Boolean isDebugOpen() {
		return debugOpen;
	}

}
