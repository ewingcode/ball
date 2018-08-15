package com.ewing.order.core.cache.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ewing.order.core.cache.AbstractCache;

@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD })
/**
 * 注释用于基础数据信息更新后再自动触发缓存更新，需要配{@link BaseInfoCacheAdvice}进行AOP拦截使用。
 * 
 * @author tansonlam 2016年7月13日
 *
 */
public @interface BaseInfoCache {
	/**
	 * 定义缓存更新时触发的缓存策略类
	 */
	@SuppressWarnings("rawtypes")
	public abstract Class<? extends AbstractCache> cacheClass();

	/**
	 * 是否需要商铺ID,默认为true
	 */
	public abstract boolean needShopId() default true;

	/**
	 * 定义商铺ID的属性名称，更新缓存策略必须的字段，如果为空，则查默认的配置的属性名称
	 */
	public abstract String shopIdFieldName() default "";

	/**
	 * 定义实体对象ID的属性名称，更新缓存策略必须的字段，如果为空，则查默认的配置的属性名称
	 */
	public abstract String entityIdFieldName() default "";

}
