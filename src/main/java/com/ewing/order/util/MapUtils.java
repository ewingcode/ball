package com.ewing.order.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewing.order.weixin.wxpay.vo.annotation.XmlField;
import com.google.common.collect.Maps;

public class MapUtils {

  private static Logger logger = LoggerFactory.getLogger(MapUtils.class);

  public static <T> T get(Map<String, T> map, String key) {
    if (isEmpty(map) || StringUtils.isEmpty(key)) {
      return null;
    }

    return map.get(key);
  }


  public static <T, U> Integer getInt(Map<T, U> map, String key) {
    if (isEmpty(map) || StringUtils.isEmpty(key)) {
      return null;
    }

    return map.containsKey(key) ? IntegerUtils.parseInt(map.get(key).toString()) : null;
  }

  public static <T, U> boolean getBoolean(Map<T, U> map, String key) {
    if (isEmpty(map) || StringUtils.isEmpty(key)) {
      return false;
    }

    return map.containsKey(key) ? Boolean.parseBoolean(map.get(key).toString()) : false;
  }


  public static <T, U> boolean isEmpty(Map<T, U> map) {
    return org.apache.commons.collections.MapUtils.isEmpty(map);
  }


  public static <T> Map<String, Object> toMap(Object t, Class<T> clazz) {
    if (null == t || null == clazz) {
      return Maps.newHashMapWithExpectedSize(0);
    }

    Map<String, Object> map = new HashMap<String, Object>();
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      try {
        map.put(field.getName(), field.get(t));
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
      }
    }

    return map;
  }

  public static <T> Map<String, Object> toXmlFieldMap(Object t, Class<T> clazz) {
    if (null == t || null == clazz) {
      return Maps.newHashMapWithExpectedSize(0);
    }

    Map<String, Object> map = new HashMap<String, Object>();
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      XmlField xmlField = field.getAnnotation(XmlField.class);
      try {
        String name = null != xmlField && StringUtils.isNotEmpty(xmlField.value())
            ? xmlField.value() : field.getName();
        map.put(name, field.get(t));
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
      }
    }

    return map;
  }


  /**
   * @TESTME
   * @param map
   * @param clazz
   * @author Joeson
   */
  public static <T> T toObject(Map<String, String> map, Class<T> clazz) {
    if (isEmpty(map) || null == clazz) {
      return null;
    }

    T t = null;
    try {
      t = clazz.newInstance();
      BeanUtils.populate(t, map);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }

    return t;
  }

}
