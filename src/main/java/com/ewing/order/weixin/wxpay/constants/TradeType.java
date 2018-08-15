package com.ewing.order.weixin.wxpay.constants;

/**
 * 交易类型
 * 
 * @author Joeson Chan<chenxuegui1234@163.com>
 * @since 2016年1月24日
 *
 */
public enum TradeType {

    /**
     * JSAPI--公众号支付
     */
    JSAPI,
    
    /**
     * 原生扫码支付
     */
    NATIVE,
    
    /**
     * app支付
     */
    APP;
    
    
}
