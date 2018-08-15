package com.ewing.order.weixin.wxpay.vo.entity;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author chenxuegui1234@163.com
 * @createDate 2015年8月28日
 *
 */
public class FieldInfo {

    /** field **/
    public String field;

    /** xml **/
    public String name;

    public String description;

    public FieldInfo() {

    }

    public FieldInfo(String field, String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static boolean isEmpty(FieldInfo fieldInfo) {
        if (null == fieldInfo || StringUtils.isEmpty(fieldInfo.name)) {
            return true;
        }

        return false;
    }

}
