package com.ewing.order.weixin.wxpay.api.refundquery.vo;

import com.ewing.order.weixin.wxpay.vo.annotation.XmlField;

/**
 * 查询订单返回结果
 * 
 * @author Joeson Chan<chenxuegui1234@163.com>
 * @since 2016年1月26日
 *
 */
public class RefundQueryResDto {

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
     * 错误描述
     */
    @XmlField(value = "err_code_des")
    private String errCodeDes;
    
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
     * 退款记录数 
     */
    @XmlField(value = "refund_count")
    private String refundCount;
    
    /**
     * 商户退款单号
     */
    @XmlField(value = "out_refund_no_$n")
    private String outRefundNo$n;
    
    /**
     * 微信退款单号 
     */
    @XmlField(value = "refundId$n")
    private String refundId$n;
    
    /**
     * ORIGINAL—原路退款  BALANCE—退回到余额
     */
    @XmlField(value = "refund_channel_$n")
    private String refundChannel$n;
    
    /**
     * 退款总金额,单位为分,可以做部分退款
     */
    @XmlField(value = "refund_fee_$n")
    private String refundFee$n;
    
    /**
     * 代金券或立减优惠退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金
     */
    @XmlField(value = "coupon_refund_fee_$n")
    private String couponRefundFee$n;
    
    /**
     * 代金券或立减优惠使用数量 ,$n为下标,从0开始编号
     */
    @XmlField(value = "coupon_refund_count_$n")
    private String couponRefundCount$n;
    
    /**
     * 批次ID ,$n为下标，$m为下标，从0开始编号
     */
    @XmlField(value = "coupon_refund_batch_id_$n_$m")
    private String couponRefundBatchId$n$m;
    
    /**
     * 代金券或立减优惠ID, $n为下标，$m为下标，从0开始编号
     */
    @XmlField(value = "coupon_refund_id_$n_$m")
    private String couponRefundId$n$m;
    
    /**
     * 退款状态： 
     * SUCCESS—退款成功 
     * FAIL—退款失败 
     * PROCESSING—退款处理中 
     * NOTSURE—未确定，需要商户原退款单号重新发起 
     * CHANGE—转入代发，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，资金回流到商户的现金帐号，需要商户人工干预，通过线下或者财付通转账的方式进行退款。
     */
    @XmlField(value = "refund_status_$n")
    private String refundStatus$n;
    
    /**
     *  取当前退款单的退款入账方
     *  1）退回银行卡：{银行名称}{卡类型}{卡尾号}
     *  2）退回支付用户零钱:支付用户零钱
     */
    @XmlField(value = "refund_recv_accout_$n")
    private String refundRecvAccout$n;

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

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
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

    public String getRefundCount() {
        return refundCount;
    }

    public void setRefundCount(String refundCount) {
        this.refundCount = refundCount;
    }

    public String getOutRefundNo$n() {
        return outRefundNo$n;
    }

    public void setOutRefundNo$n(String outRefundNo$n) {
        this.outRefundNo$n = outRefundNo$n;
    }

    public String getRefundId$n() {
        return refundId$n;
    }

    public void setRefundId$n(String refundId$n) {
        this.refundId$n = refundId$n;
    }

    public String getRefundChannel$n() {
        return refundChannel$n;
    }

    public void setRefundChannel$n(String refundChannel$n) {
        this.refundChannel$n = refundChannel$n;
    }

    public String getRefundFee$n() {
        return refundFee$n;
    }

    public void setRefundFee$n(String refundFee$n) {
        this.refundFee$n = refundFee$n;
    }

    public String getCouponRefundFee$n() {
        return couponRefundFee$n;
    }

    public void setCouponRefundFee$n(String couponRefundFee$n) {
        this.couponRefundFee$n = couponRefundFee$n;
    }

    public String getCouponRefundCount$n() {
        return couponRefundCount$n;
    }

    public void setCouponRefundCount$n(String couponRefundCount$n) {
        this.couponRefundCount$n = couponRefundCount$n;
    }

    public String getCouponRefundBatchId$n$m() {
        return couponRefundBatchId$n$m;
    }

    public void setCouponRefundBatchId$n$m(String couponRefundBatchId$n$m) {
        this.couponRefundBatchId$n$m = couponRefundBatchId$n$m;
    }

    public String getCouponRefundId$n$m() {
        return couponRefundId$n$m;
    }

    public void setCouponRefundId$n$m(String couponRefundId$n$m) {
        this.couponRefundId$n$m = couponRefundId$n$m;
    }

    public String getRefundStatus$n() {
        return refundStatus$n;
    }

    public void setRefundStatus$n(String refundStatus$n) {
        this.refundStatus$n = refundStatus$n;
    }

    public String getRefundRecvAccout$n() {
        return refundRecvAccout$n;
    }

    public void setRefundRecvAccout$n(String refundRecvAccout$n) {
        this.refundRecvAccout$n = refundRecvAccout$n;
    }
    

}
