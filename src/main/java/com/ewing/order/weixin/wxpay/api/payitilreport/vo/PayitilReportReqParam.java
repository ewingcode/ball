package com.ewing.order.weixin.wxpay.api.payitilreport.vo;

import com.ewing.order.weixin.wxpay.vo.annotation.XmlField;

/**
 * 下载对账单请求参数
 * 
 * @author Joeson Chan<chenxuegui1234@163.com>
 * @since 2016年1月26日
 *
 */
public class PayitilReportReqParam {

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
     * 微信支付分配的终端设备号，商户自定义
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
     * 报对应的接口的完整URL，类似： https://api.mch.weixin.qq.com/pay/unifiedorder
     * 对于刷卡支付，为更好的和商户共同分析一次业务行为的整体耗时情况，对于两种接入模式，请都在门店侧对一次刷卡支付进行一次单独的整体上报，上报URL指定为： https://api.mch.weixin.qq.com/pay/micropay/total
     * 关于两种接入模式具体可参考本文档章节：刷卡支付商户接入模式
     * 其它接口调用仍然按照调用一次，上报一次来进行。
     */
    @XmlField(value = "interface_url")
    private String interfaceUrl;
    
    /**
     *  接口耗时情况，单位为毫秒 
     */
    @XmlField(value = "execute_time_")
    private String executeTime;
    
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
    
    /**
     * SUCCESS/FAIL
     */
    @XmlField(value = "result_code")
    private String resultCode;
    
    /**
     * ORDERNOTEXIST—订单不存在  SYSTEMERROR—系统错误
     */
    @XmlField(value = "err_code")
    private String errCode;
    
    /**
     * 结果信息描述
     */
    @XmlField(value = "err_code_des")
    private String errCodeDes;
    
    /**
     * 商户系统内部的订单号,商户可以在上报时提供相关商户订单号方便微信支付更好的提高服务质量
     */
    @XmlField(value = "out_trade_no")
    private String outTradeNo;
    
    /**
     * 发起接口调用时的机器IP  
     */
    @XmlField(value = "user_ip")
    private String userIp;
    
    /**
     * 系统时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010
     */
    @XmlField(value = "time")
    private String time;


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

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getInterfaceUrl() {
        return interfaceUrl;
    }

    public void setInterfaceUrl(String interfaceUrl) {
        this.interfaceUrl = interfaceUrl;
    }

    public String getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(String executeTime) {
        this.executeTime = executeTime;
    }

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

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    
    

}
