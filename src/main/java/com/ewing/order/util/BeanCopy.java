package com.ewing.order.util;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * 
 * 
 * @author tanson lam
 * @create 2016年9月30日
 */
public class BeanCopy {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(BeanCopy.class);

	/**
	 * copy列表属性
	 * 
	 * @param list
	 * @param clazz
	 * @return
	 * @author Joeson
	 */
	public static <T, Z> List<T> copy(List<Z> list, Class<T> clazz) {
		if (CollectionUtils.isEmpty(list)) {
			return Lists.newArrayList();
		}

		List<T> dtoList = Lists.newArrayList();
		for (Object t : list) {
			try {
				T dto = clazz.newInstance();
				BeanUtils.copyProperties(dto, t);
				dtoList.add(dto);
			} catch (Exception e) {
				logger.error("copy exception: " + e.getMessage(), e);
				continue;
			}
		}

		return dtoList;
	}

	/**
	 * copy
	 * 
	 * @param dest
	 * @param orig
	 * @param ignoreNull
	 *            忽略null,即orig为空的字段不做copy
	 * @author Joeson
	 */
	public static void copy(Object dest, Object orig, boolean ignoreNull) {
		if (null == dest || null == orig) {
			return;
		}

		Class<?> destClz = dest.getClass();
		Class<?> origClz = orig.getClass();

		Field[] destFields = destClz.getDeclaredFields();
		Field[] origFields = origClz.getDeclaredFields();

		for (Field origField : origFields) {
			try {
				String fieldName = origField.getName();
				origField.setAccessible(true);
				if (!ignoreNull || null != origField.get(orig)) {
					for (Field destField : destFields) {
						if (destField.getName().equals(origField.getName())
								&& destField.getType().equals(
										origField.getType())) {
							destField.setAccessible(true);
							Object o = origField.get(orig);
							BeanUtils.copyProperty(dest, fieldName, o);
							break;
						}
					}
				}
			} catch (Exception e) {
				logger.error("copyNonNull exception.", e);
				throw new RuntimeException("copyNonNull exception.."
						+ e.getMessage());
			}
		}
	}
}
