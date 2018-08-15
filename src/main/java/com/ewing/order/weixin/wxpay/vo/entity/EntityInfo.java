package com.ewing.order.weixin.wxpay.vo.entity;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Maps;

/**
 * {@link EntityInfo}记录Entity对象的field已经对应的属性名称
 * 
 * @author chenxuegui1234@163.com
 * @createDate 2015年8月28日
 *
 */
public class EntityInfo {

    private Map<String, FieldInfo> fieldNameMap = Maps.newHashMap();

    /**
     * 获取属性名称
     */
    public FieldInfo getFieldName(String field) {
        if (StringUtils.isEmpty(field)) {
            return new FieldInfo();
        }

        return fieldNameMap.get(field);
    }

    /**
     * 设置属性名称
     */
    public void addField(String field, FieldInfo name) {
        if (StringUtils.isEmpty(field) && FieldInfo.isEmpty(name)) {
            return;
        }

        fieldNameMap.put(field, name);
    }
    
    public Map<String, FieldInfo> getFieldNameMap(){
        return fieldNameMap;
    }
    
    
    
}