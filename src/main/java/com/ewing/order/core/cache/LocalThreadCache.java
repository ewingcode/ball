package com.ewing.order.core.cache;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.ewing.order.util.ThreadLocalPool;
import com.google.common.collect.Maps;

/**
 * 
 * @author tanson lam
 * @create 2016年11月13日
 * 
 */
public class LocalThreadCache {

	public static class NullBean implements Serializable {
		private static final long serialVersionUID = 1L;

	}

	public static NullBean newNullBean() {
		return new NullBean();
	}

	public static class LocalMap {

		private static ThreadLocal<Map<String, Object>> contextMap = ThreadLocalPool
				.createThreadLocal();

		public static Object get(String key) {
			Map<String, Object> args = contextMap.get();
			if (args == null) {
				return null;
			} else {
				return args.get(key);
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

		public static Map<String, Object> getAll() {
			return contextMap.get();
		}

		public static void putAll(Map<String, Object> map) {
			if (MapUtils.isEmpty(map))
				return;

			Map<String, Object> args = contextMap.get();
			if (args == null) {
				args = Maps.newHashMap();
				contextMap.set(args);
			}
			args.putAll(map);
		}
	}

	public static void clean() {
		LocalMap.clean();
	}
}