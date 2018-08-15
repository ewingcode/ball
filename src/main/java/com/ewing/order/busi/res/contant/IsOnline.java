package com.ewing.order.busi.res.contant;
/**
 * 资源上线状态 
 * 
 * @author tansonlam
 * @createDate 2016年1月26日
 *
 */
public enum IsOnline {

    /** 发布中 0 */
    PUBLICING(0, "发布中"),

    /** 上架 1 */
    ONLINE(1, "上架"),

    /** 下架 2 */
    OFFLINE(2, "下架");

    private int value;
    private String msg;

    IsOnline(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public int getValue() {
        return value;
    }

    public String getMsg() {
        return msg;
    }
}
