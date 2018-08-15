package com.ewing.order.core.cache;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.ewing.order.util.ThreadLocalPool;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 对象列表上下文
 * 
 * @author tansonlam
 * @create 2016年11月18日
 * 
 */
public class EntityListContext<Entity> {

	public void setEntityList(String batchId, List<Entity> entityList) {
		if (StringUtils.isEmpty(batchId))
			throw new IllegalArgumentException("batchId cant not be null.");
		if (CollectionUtils.isEmpty(entityList))
			throw new IllegalArgumentException("entityList cant not be null.");
		LocalMap.put(batchId + "_cache", entityList);
	}

	@SuppressWarnings("unchecked")
	public List<Entity> getEntityList(String batchId) {
		if (StringUtils.isEmpty(batchId))
			return Lists.newArrayList();

		return (List<Entity>) LocalMap.get(batchId + "_cache");
	}

	public void removeEntityList(String batchId) {
		if (StringUtils.isEmpty(batchId))
			return;

		LocalMap.remove(batchId + "_cache");
	}

	public static class LocalMap<Entity> {

		private static ThreadLocal<Map<String, Object>> contextMap = ThreadLocalPool.createThreadLocal();

		public static Object get(String key) {
			Map<String, Object> args = contextMap.get();
			if (args == null) {
				return null;
			} else {
				return args.get(key);
			}
		}

		public static void remove(String key) {
			Map<String, Object> args = contextMap.get();
			if (args != null) {
				args.remove(key);
			}
		}

		public static void put(String key, Object value) {
			Map<String, Object> args = contextMap.get();
			if (args == null) {
				args = Maps.newHashMap();
				contextMap.set(args);
			}
			args.put(key, value);
		}

		@SuppressWarnings("unchecked")
		public static Map<String, Object> mapAttrs() {
			Map<String, Object> args = contextMap.get();
			if (args != null) {
				return MapUtils.unmodifiableMap(args);
			} else {
				return MapUtils.EMPTY_MAP;
			}
		}

		public static void clean() {
			Map<String, Object> args = contextMap.get();
			if (args != null) {
				args.clear();
			}
			contextMap.set(null);
		}

	}

	public static void clean() {
		LocalMap.clean();
	}

}
