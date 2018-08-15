package com.ewing.order.weixin.wxpay.api.applyrefund.vo;

import com.ewing.order.weixin.wxpay.vo.annotation.XmlField;



/**
 * 查询订单返回结果
 * 
 * @author Joeson Chan<chenxuegui1234@163.com>
 * @since 2016年1月26日
 *
 */
public class ApplyRefundResDto {

    /**
     * SUCCESS/FAIL 此字段是通信标识，非交易标识，交易是否成功需要查看trade_state来判断
     */
    @XmlField(value = "return_code")
    private String returnCode;

    /**
     * 返回信息，如非空，为错误原因
     */
    @XmlField(value = "return_msg")
    private String returnMsg;

    /**************************** 以下字段在return_code为SUCCESS的时候有返回 *************************/
    /**
     * SUCCESS/FAIL
     */
    @XmlField(value = "result_code")
    private String resultCode;

    /**
     * 错误码
     */
    @XmlField(value = "err_code")
    private String errCode;
    
    /**
     * 微信分配的公众账号ID
     */
    @XmlField(value = "appid")
    private String appId;

    /**
     * 微信支付分配的商户号
     */
    @XmlField(value = "mch_id")
    private String mchId;
    
    /**
     * 微信支付分配的终端设备号，与下单一致
     */
    @XmlField(value = "device_info")
    private String deviceInfo;

    /**
     * 随机字符串，不长于32位。
     */
    @XmlField(value = "nonce_str")
    private String nonceStr;

    /**
     * 签名
     */
    @XmlField(value = "sign")
    private String sign;

    /**
     * 微信订单号
     */
    @XmlField(value = "transaction_id")
    private String transactionId;
    
    /**
     *  商户系统内部的订单号 
     */
    @XmlField(value = "out_trade_no")
    private String outTradeNo;
    
    /**
     * 商户退款单号
     */
    @XmlField(value = "out_refund_no")
    private String outRefundNo;
    
    /**
     * 微信退款单号
     */
    @XmlField(value = "refund_id")
    private String refundId;
    /**
     * 微信退款单号
     */
    @XmlField(value = "refund_channel")
    private String refundChannel;
    
    /**
     * 退款总金额,单位为分,可以做部分退款
     */
    @XmlField(value = "refund_fee")
    private String refundFee;
    
    /**
     * 订单总金额，单位为分，只能为整数
     */
    @XmlField(value = "total_fee")
    private String totalFee;
    
    /**
     * 订单金额货币类型，符合ISO 4217标准的三位字母代码，默认人民币
     */
    @XmlField(value = "fee_type")
    private String feeType;
    
    /**
     * 现金支付金额，单位为分，只能为整数
     */
    @XmlField(value = "cash_fee")
    private String cashFee;
    
    /**
     * 代金券或立减优惠退款金额=订单金额-现金退款金额，注意：立减优惠金额不会退回
     */
    @XmlField(value = "coupon_refund_fee")
    private String couponRefundFee;
    
    /**
     * 代金券或立减优惠使用数量
     */
    @XmlField(value = "coupon_refund_count")
    private String couponRefundCount;
    
    /**
     *  代金券或立减优惠ID 
     */
    @XmlField(value = "coupon_refund_id")
    private String couponRefundId;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getRefundChannel() {
        return refundChannel;
    }

    public void setRefundChannel(String refundChannel) {
        this.refundChannel = refundChannel;
    }

    public String getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(String refundFee) {
        this.refundFee = refundFee;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getCashFee() {
        return cashFee;
    }

    public void setCashFee(String cashFee) {
        this.cashFee = cashFee;
    }

    public String getCouponRefundFee() {
        return couponRefundFee;
    }

    public void setCouponRefundFee(String couponRefundFee) {
        this.couponRefundFee = couponRefundFee;
    }

    public String getCouponRefundCount() {
        return couponRefundCount;
    }

    public void setCouponRefundCount(String couponRefundCount) {
        this.couponRefundCount = couponRefundCount;
    }

    public String getCouponRefundId() {
        return couponRefundId;
    }

    public void setCouponRefundId(String couponRefundId) {
        this.couponRefundId = couponRefundId;
    }
    
    
}
