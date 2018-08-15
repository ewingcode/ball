package com.ewing.order.util;

import java.lang.reflect.Field;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import javassist.CtClass;

/**
 * 
 * @author tanson lam
 * @creation 2016年8月12日
 * 
 */
public class ReflectUtil {

	/**
	 * 反射获取对象指定属性名称的值
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static Object getReflectValue(Object obj, String fieldName)
			throws IllegalArgumentException, IllegalAccessException {
		Field field = getReflectField(obj.getClass(), fieldName);
		if (field == null)
			return null;
		field.setAccessible(true);
		return field.get(obj);
	}

	public static Field getReflectField(Class<?> clazz, String fieldName) {
		if (clazz == null || clazz.equals(Object.class) || clazz.isPrimitive()
				|| StringUtils.isEmpty(fieldName))
			return null;
		Field field = null;
		try {
			field = clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
		} catch (SecurityException e) {
		}
		if (field == null) {
			field = getReflectField(clazz.getSuperclass(), fieldName);
		}
		return field;
	}

	public static Boolean hasRelflectField(Class<?> clazz, String fieldName) {
		if (clazz == null || clazz.equals(Object.class) || clazz.isPrimitive()
				|| StringUtils.isEmpty(fieldName))
			return null;
		Field field = null;
		try {
			field = clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
		} catch (SecurityException e) {
		}
		if (field == null) {
			field = getReflectField(clazz.getSuperclass(), fieldName);
		}
		return field != null;
	}

	public static Boolean isSameParamTypes(CtClass[] ctParamClass, CtClass[] ctParamClass2) {
		if (ArrayUtils.isEmpty(ctParamClass) && ArrayUtils.isEmpty(ctParamClass2))
			return true;
		if (ctParamClass.length != ctParamClass2.length) {
			return false;
		}
		for (int i = 0; i < ctParamClass.length; i++) {
			if (!ctParamClass[i].getName().equals(ctParamClass2[i].getName()))
				return false;
		}
		return true;
	}
}
