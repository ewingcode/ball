package com.ewing.order.weixin.wxpay.api.unifiedorders.vo;

import com.ewing.order.weixin.wxpay.vo.XmlFieldVo;
import com.ewing.order.weixin.wxpay.vo.annotation.XmlField;

/**
 * 公众号支付.统一下单.请求参数
 * 
 * @author Joeson Chan<chenxuegui1234@163.com>
 * @since 2016年1月24日
 *
 */
public class UnifiedOrdersReqParam implements XmlFieldVo{

    /**
     * 微信分配的公众账号ID（企业号corpid即为此appId）
     */
    @XmlField(value="appid")
    private String appId;

    /**
     * 微信支付分配的商户号
     */
    @XmlField(value="mch_id")
    private String mchId;

    /**
     * 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
     */
    @XmlField(value="device_info")
    private String deviceInfo;

    /**
     * 随机字符串，不长于32位。推荐随机数生成算法
     */
    @XmlField(value="nonce_str")
    private String nonceStr;

    /**
     * 商品或支付单简要描述
     */
    @XmlField(value="body")
    private String body;

    /**
     * 商品名称明细列表
     */
    @XmlField(value="detail")
    private String detail;

    /**
     * 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
     */
    @XmlField(value="attach")
    private String attach;

    /**
     * 商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
     */
    @XmlField(value="out_trade_no")
    private String outTradeNo;

    /**
     * 符合ISO 4217标准的三位字母代码，默认人民币：CNY
     */
    @XmlField(value="fee_type")
    private String feeType;

    /**
     * 订单总金额，单位为分
     */
    @XmlField(value="total_fee")
    private String totalFee;

    /**
     * APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
     */
    @XmlField(value="spbill_create_ip")
    private String spBillCreateIp;

    /**
     * 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010
     */
    @XmlField(value="time_start")
    private String timeStart;

    /**
     * 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010
     */
    @XmlField(value="time_expire")
    private String timeExpire;

    /**
     * 商品标记，代金券或立减优惠功能的参数
     */
//    @XmlField(value="goods_tag")
//    private String goodsTag;

    /**
     * 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
     */
    @XmlField(value="notify_url")
    private String notifyUrl;

    /**
     * 取值如下：JSAPI，NATIVE，APP
     */
    @XmlField(value="trade_type")
    private String tradeType;

    /**
     * trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
     */
    @XmlField(value="product_id")
    private String productId;

    /**
     * no_credit--指定不能使用信用卡支付
     */
    @XmlField(value="limit_pay")
    private String limitPay;

    /**
     * trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。 openId获取可通过 UserAPI.getFollower 或UserAPI中 网页授权获取用户基本信息（四个方法）
     */
    @XmlField(value="openid")
    private String openId;

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getSpBillCreateIp() {
        return spBillCreateIp;
    }

    public void setSpBillCreateIp(String spBillCreateIp) {
        this.spBillCreateIp = spBillCreateIp;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(String timeExpire) {
        this.timeExpire = timeExpire;
    }

//    public String getGoodsTag() {
//        return goodsTag;
//    }
//
//    public void setGoodsTag(String goodsTag) {
//        this.goodsTag = goodsTag;
//    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getLimitPay() {
        return limitPay;
    }

    public void setLimitPay(String limitPay) {
        this.limitPay = limitPay;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

}
