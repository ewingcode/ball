package com.ewing.order.weixin.wxpay.vo.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang.StringUtils;

/**
 * <pre>
 * 作为全局日志展示用的注解
 * 由于全局日志存库表，只是存储了部分的属性名称，可以通过注解{@link Comment}获取对应字段的属性名，
 * </pre>
 * 
 * @author chenxuegui1234@163.com
 * @createDate 2016年4月28日
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE, ElementType.FIELD })
public @interface Comment{

    /**
     * 属性名
     */
    public abstract String name() default StringUtils.EMPTY;

    /**
     * 描述
     */
    public abstract String description() default StringUtils.EMPTY;
    
}
