package com.ewing.order.weixin.wxpay.api.applyrefund.constants;

/**
 * 关闭订单错误码
 * 
 * @author Joeson Chan<chenxuegui1234@163.com>
 * @since 2016年1月26日
 *
 */
public enum ApplyRefundErrorCode {
    
    ORDERPAID("ORDERPAID", "订单已支付", "订单已支付，不能发起关单",  "订单已支付不能发起关单，请当作已支付的正常交易"), 
    SYSTEMERROR("SYSTEMERROR", "系统错误", "系统错误", "系统异常，请重新调用该API"), 
    ORDERNOTEXIST("ORDERNOTEXIST", "订单不存在", "订单系统不存在此订单", "不需要关单，当作未提交的支付的订单"), 
    ORDERCLOSED("ORDERCLOSED", "订单已关闭", "订单已关闭，无法重复关闭",  "订单已关闭，无需继续调用"), 
    SIGNERROR("SIGNERROR", "签名错误",  "参数签名结果不正确", "请检查签名参数和方法是否都符合签名算法要求"), 
    REQUIRE_POST_METHOD("REQUIRE_POST_METHOD", "请使用post方法", "未使用post传递参数",  "请检查请求参数是否通过post方法提交"), 
    XML_FORMAT_ERROR("XML_FORMAT_ERROR", "XML格式错误", "XML格式错误", "请检查XML参数格式是否正确"); 

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
    
    ApplyRefundErrorCode(String code,String des,String cause,String deal){
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
