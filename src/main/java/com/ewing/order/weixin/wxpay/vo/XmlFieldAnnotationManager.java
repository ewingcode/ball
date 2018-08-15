package com.ewing.order.weixin.wxpay.vo;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.ewing.order.weixin.wxpay.api.paycallback.vo.PayNotifyCallBackReqParam;
import com.ewing.order.weixin.wxpay.api.paycallback.vo.PayNotifyCallBackResDto;
import com.ewing.order.weixin.wxpay.api.unifiedorders.vo.UnifiedOrdersReqParam;
import com.ewing.order.weixin.wxpay.api.unifiedorders.vo.UnifiedOrdersResDto;
import com.ewing.order.weixin.wxpay.vo.annotation.XmlField;
import com.ewing.order.weixin.wxpay.vo.entity.EntityInfo;
import com.ewing.order.weixin.wxpay.vo.entity.FieldInfo;
import com.google.common.collect.Maps;

/**
 * 获取注解@XmlField的相关信息的类
 * 
 * @author Joeson Chan<chenxuegui1234@163.com>
 * @since 2016年1月26日
 *
 */
public class XmlFieldAnnotationManager extends FieldAnnotationManager { 

    private static final Object lock = new Object();

    /**
     * class simple name 到Class对象的映射
     */
    private static Map<String, Class<? extends Object>> classMap = Maps.newHashMap();

    private XmlFieldAnnotationManager() throws ClassNotFoundException {
        @SuppressWarnings("unchecked")
        Class<? extends Object>[] clazzs = new Class[] { PayNotifyCallBackReqParam.class, PayNotifyCallBackResDto.class,
                UnifiedOrdersReqParam.class, UnifiedOrdersResDto.class};
        
        for(Class<? extends Object> clazz : clazzs){ 
            EntityInfo entityInfo = load(clazz.getName());
            entityInfoMap.put(clazz.getName(), entityInfo);
            classMap.put(clazz.getName(), clazz);
        }
    }

    /**
     * 获取Instance实例
     * @throws ClassNotFoundException 
     */
    public static XmlFieldAnnotationManager getInstace() {
        if (null == instance) {
            synchronized (lock) {
                if (null == instance) {
                    try {
                      instance = new XmlFieldAnnotationManager();
                    } catch (ClassNotFoundException e) {
                      // TODO Auto-generated catch block
                      e.printStackTrace();
                      return null;
                    }
                }
            }
        }

        return (XmlFieldAnnotationManager) instance;
    }
    
    private static EntityInfo load(String clazzName) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(clazzName);
        Field[] fields = clazz.getDeclaredFields();

        EntityInfo entityInfo = new EntityInfo();
        for (Field field : fields) {
            field.setAccessible(true);
            XmlField xmlField = field.getAnnotation(XmlField.class);
            String xmlFieldStr = null != xmlField ? xmlField.value() : StringUtils.EMPTY;
            
            FieldInfo fieldInfo = new FieldInfo();
            fieldInfo.name = xmlFieldStr;
            fieldInfo.field = field.getName();

            //<key,value> => <xmlValue: fieldInfo>
            entityInfo.addField(StringUtils.isNotEmpty(xmlFieldStr) ? xmlFieldStr : field.getName(), fieldInfo);
        }
        
        
        return entityInfo;
    }

    /**
     * 解析AuditEntity属性
     * @throws ClassNotFoundException 
     */
    @SuppressWarnings("unused")
    private void parseAuditEntity(List<String> classNameList) throws ClassNotFoundException {
        if(CollectionUtils.isEmpty(classNameList)){
            return;
        }

        for (String className : classNameList) {
            // 解析className
            EntityInfo info = reloadEntityInfo(className);
            entityInfoMap.put(className, info);
        }

    }

    @Override
    protected EntityInfo reloadEntityInfo(String className) throws ClassNotFoundException {
        if(StringUtils.isEmpty(className)){
            return null;
        }

        return load(className);
    }
    
}
