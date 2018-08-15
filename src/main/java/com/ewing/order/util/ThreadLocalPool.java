package com.ewing.order.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * ThreadLocal工具类
 * 
 * @author tanson lam
 * @create 2016年8月18日
 * 
 */
public class ThreadLocalPool {
	private final static Set<ThreadLocal<?>> threadLocalPool = Collections
			.synchronizedSet(new HashSet<ThreadLocal<?>>());

	public static <T> ThreadLocal<T> createThreadLocal() {
		ThreadLocal<T> threadLocal = new ThreadLocal<T>();
		threadLocalPool.add(threadLocal);
		return threadLocal;
	}

	public static void clean() {
		try {
			for (ThreadLocal<?> theadLocal : threadLocalPool) {
				theadLocal.remove();
			}
		} catch (Exception e) { 
			 
		}
	}
}