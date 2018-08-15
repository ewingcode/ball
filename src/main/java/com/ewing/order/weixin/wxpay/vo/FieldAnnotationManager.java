package com.ewing.order.weixin.wxpay.vo;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.ewing.order.weixin.wxpay.vo.entity.EntityInfo;
import com.ewing.order.weixin.wxpay.vo.entity.FieldInfo;
import com.google.common.collect.Maps;

/**
 * 
 * @author Joeson Chan<chenxuegui1234@163.com>
 * @since 2016年1月26日
 *
 */
public abstract class FieldAnnotationManager {
    
    protected static volatile FieldAnnotationManager instance = null;

    protected static Map<String, EntityInfo> entityInfoMap = Maps.newConcurrentMap();

    /**
     * 获取属性FieldInfo
     * 
     * @param className
     * @param fieldName
     * @author Joeson
     */
    public FieldInfo getFieldInfo(String className, String field) {
        if (StringUtils.isEmpty(className) || StringUtils.isEmpty(field)) {
            return new FieldInfo();
        }

        EntityInfo entityInfo = null;
        try{
          entityInfo = this.getEntityInfo(className);
        }catch(Exception e){
          e.printStackTrace();
          return null;
        }
        return null == entityInfo ? null : entityInfo.getFieldName(field);
    }

    /**
     * 获取对象EntityInfo
     * 
     * @author Joeson
     * @throws ClassNotFoundException 
     */
    public EntityInfo getEntityInfo(String className) throws ClassNotFoundException {
        if (StringUtils.isEmpty(className)) {
            return new EntityInfo();
        }

        EntityInfo entityInfo = entityInfoMap.get(className);
        if (null == entityInfo) {
            // 解析className
            entityInfo = reloadEntityInfo(className);
            entityInfoMap.put(className, entityInfo);
        }
        return null == entityInfo ? new EntityInfo() : entityInfo;
    }
    
    protected abstract EntityInfo reloadEntityInfo(String className) throws ClassNotFoundException;

}
