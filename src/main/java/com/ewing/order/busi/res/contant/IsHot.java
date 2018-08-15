package com.ewing.order.busi.res.contant;

/**
 * 是否热门推荐资源
 */
public enum IsHot {

    COMMON(0, "普通"),

    HOT(1, "热门");

    private int value;
    private String msg;

    IsHot(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public int getValue() {
        return value;
    }

    public String getMsg() {
        return msg;
    }

    public static IsHot fromValue(int value) {
        for (IsHot item : IsHot.values()) {
            if (item.value == value)
                return item;
        }
        throw new IllegalArgumentException("not found match value[" + value + "]");
    }
}
