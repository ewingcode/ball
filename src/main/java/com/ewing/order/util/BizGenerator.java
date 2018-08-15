package com.ewing.order.util;

import java.util.UUID;

/**
 * 业务流水工具类
 * 
 * @author tanson lam
 * @createDate 2015年2月12日
 * 
 */
public class BizGenerator {
    /**
     * 生成业务流水号
     * 
     * @return
     */
    public static String generateBizNum() {
        String a = (String.valueOf(System.currentTimeMillis()));
        String d = (String.valueOf(Math.random())).substring(2, 10);
        return "EWING-" + a + d;
    }

    public static String generateUUID() {
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }
}
