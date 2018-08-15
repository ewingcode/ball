package com.ewing.order.weixin.wxpay.api.refundquery.vo;

import com.ewing.order.weixin.wxpay.vo.annotation.XmlField;

/**
 * 查询订单请求参数
 * 
 * @author Joeson Chan<chenxuegui1234@163.com>
 * @since 2016年1月26日
 *
 */
public class RefundQueryReqParam {

    /**
     * 微信分配的公众账号ID（企业号corpid即为此appId）
     */
    @XmlField(value = "appid")
    private String appId;

    /**
     * 微信支付分配的商户号
     */
    @XmlField(value = "mch_id")
    private String mchId;

    /**
     * 终端设备号
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
     * 微信的订单号，优先使用
     */
    @XmlField(value = "transaction_id")
    private String transactionId;

    /**
     * 商户系统内部的订单号，当没提供transaction_id时需要传这个。
     */
    @XmlField(value = "out_trade_no")
    private String outTradeNo;

    /**
     * 商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
     */
    @XmlField(value = "out_refund_no")
    private String outRefundNo;

    /**
     * 微信生成的退款单号，在申请退款接口有返回
     */
    @XmlField(value = "refund_id")
    private String refundId;

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

}
