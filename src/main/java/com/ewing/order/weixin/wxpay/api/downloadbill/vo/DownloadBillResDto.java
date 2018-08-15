package com.ewing.order.weixin.wxpay.api.downloadbill.vo;

import com.ewing.order.weixin.wxpay.vo.annotation.XmlField;

/**
 * 下载对账单返回结果
 * 
 * @author Joeson Chan<chenxuegui1234@163.com>
 * @since 2016年1月26日
 *
 */
public class DownloadBillResDto {

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
     * 结果信息描述
     */
    @XmlField(value = "err_code_des")
    private String errCodeDes;

    /****************************** 以下字段在return_code 和result_code都为SUCCESS的时候有返回 **************************/

    /**
     * 微信支付分配的终端设备号
     */
    @XmlField(value = "device_info")
    private String deviceInfo;

    /**
     * 用户在商户appid下的唯一标识
     */
    @XmlField(value = "openid")
    private String openId;

    /**
     * 用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
     */
    @XmlField(value = "is_subscribe")
    private String isSubscribe;

    /**
     * 调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP，MICROPAY
     */
    @XmlField(value = "trade_type")
    private String tradeType;

    /**
     * SUCCESS—支付成功 REFUND—转入退款 NOTPAY—未支付 CLOSED—已关闭 REVOKED—已撤销（刷卡支付） USERPAYING--用户支付中 PAYERROR--支付失败(其他原因，如银行返回失败)
     */
    @XmlField(value = "trade_state")
    private String tradeState;

    /**
     * 银行类型，采用字符串类型的银行标识
     */
    @XmlField(value = "bank_type")
    private String bankType;

    /**
     * 总金额 订单总金额，单位为分
     */
    @XmlField(value = "total_fee")
    private String totalFee;

    /**
     * 货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY
     */
    @XmlField(value = "fee_type")
    private String feeType;

    /**
     * 现金支付金额订单现金支付金额
     */
    @XmlField(value = "cash_fee")
    private String cashFee;

    /**
     * 货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY
     */
    @XmlField(value = "cash_fee_type")
    private String cashFeeType;

    /**
     * “代金券或立减优惠”金额<=订单总金额，订单总金额-“代金券或立减优惠”金额=现金支付金额
     */
    @XmlField(value = "coupon_fee")
    private Integer couponFee;

    /**
     * 代金券或立减优惠使用数量
     */
    @XmlField(value = "coupon_count")
    private Integer couponCount;

    /**
     * 代金券或立减优惠批次ID ,$n为下标，从0开始编号
     */
    @XmlField(value = "coupon_batch_id_$n")
    private String couponBatchId$n;

    /**
     * 代金券或立减优惠ID, $n为下标，从0开始编号
     */
    @XmlField(value = "coupon_id_$n")
    private String couponId$n;

    /**
     * 单个代金券或立减优惠支付金额, $n为下标，从0开始编号
     */
    @XmlField(value = "coupon_fee_$n")
    private String couponFee$n;

    /**
     * 微信支付订单号
     */
    @XmlField(value = "transaction_id")
    private String transactionId;

    /**
     * 商户系统的订单号，与请求一致。
     */
    @XmlField(value = "out_trade_no")
    private String outTradeNo;

    /**
     * 附加数据，原样返回
     */
    @XmlField(value = "attach")
    private String attach;

    /**
     * 订单支付时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010
     */
    @XmlField(value = "time_end")
    private String timeEnd;

    /**
     * 对当前查询订单状态的描述和下一步操作的指引
     */
    @XmlField(value = "trade_state_desc")
    private String tradeStateDesc;

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

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(String isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradeState() {
        return tradeState;
    }

    public void setTradeState(String tradeState) {
        this.tradeState = tradeState;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
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

    public String getCashFeeType() {
        return cashFeeType;
    }

    public void setCashFeeType(String cashFeeType) {
        this.cashFeeType = cashFeeType;
    }

    public Integer getCouponFee() {
        return couponFee;
    }

    public void setCouponFee(Integer couponFee) {
        this.couponFee = couponFee;
    }

    public Integer getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(Integer couponCount) {
        this.couponCount = couponCount;
    }

    public String getCouponBatchId$n() {
        return couponBatchId$n;
    }

    public void setCouponBatchId$n(String couponBatchId$n) {
        this.couponBatchId$n = couponBatchId$n;
    }

    public String getCouponId$n() {
        return couponId$n;
    }

    public void setCouponId$n(String couponId$n) {
        this.couponId$n = couponId$n;
    }

    public String getCouponFee$n() {
        return couponFee$n;
    }

    public void setCouponFee$n(String couponFee$n) {
        this.couponFee$n = couponFee$n;
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

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getTradeStateDesc() {
        return tradeStateDesc;
    }

    public void setTradeStateDesc(String tradeStateDesc) {
        this.tradeStateDesc = tradeStateDesc;
    }

}
