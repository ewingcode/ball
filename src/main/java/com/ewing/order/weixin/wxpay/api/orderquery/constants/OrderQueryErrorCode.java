package com.ewing.order.weixin.wxpay.api.orderquery.constants;

/**
 * 订单查询错误码
 * 
 * @author Joeson Chan<chenxuegui1234@163.com>
 * @since 2016年1月26日
 *
 */
public enum OrderQueryErrorCode {
    
    ORDERNOTEXIST("ORDERNOTEXIST", "此交易订单号不存在" , "查询系统中不存在此交易订单号",  "该API只能查提交支付交易返回成功的订单,请商户检查需要查询的订单号是否正确"),  
    SYSTEMERROR("SYSTEMERROR", "系统错误", "后台系统返回错误",  "系统异常,请再调用发起查询");  


    /**
     * 名称
     */
    private String code;
    
    /**
     * 描述
     */
    private String des;
    
    /**
     * 原因
     */
    private String cause;
    
    /**
     * 解決方案
     */
    private String deal;
    
    OrderQueryErrorCode(String code,String des,String cause,String deal){
        this.code = code;
        this.des = des;
        this.cause = cause;
        this.deal = deal;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getDeal() {
        return deal;
    }

    public void setDeal(String deal) {
        this.deal = deal;
    }
}
