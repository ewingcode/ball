package com.ewing.order.weixin.wxpay.api.unifiedorders.vo;

import com.ewing.order.weixin.wxpay.vo.XmlFieldVo;
import com.ewing.order.weixin.wxpay.vo.annotation.XmlField;

/**
 * 公众号支付.统一下单.响应参数
 * 
 * @author Joeson Chan<chenxuegui1234@163.com>
 * @since 2016年1月24日
 *
 */
public class UnifiedOrdersResDto implements XmlFieldVo{

    /**
     * 返回状态码 - SUCCESS/FAIL 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
     */
    @XmlField(value = "return_code")
    private String returnCode;

    /**
     * 返回信息 - 如非空，为错误原因
     */
    @XmlField(value = "return_msg")
    private String returnMsg;

    /*********************** 以下字段在return_code为SUCCESS的时候有返回 ***************************/
    /**
     * 公众账号ID - 调用接口提交的公众账号ID
     */
    @XmlField(value = "appid")
    private String appId;

    /**
     * 商户号 - 调用接口提交的商户号
     */
    @XmlField(value = "mch_id")
    private String mchId;

    /**
     * 设备号 - 调用接口提交的终端设备号
     */
    @XmlField(value = "device_info")
    private String deviceInfo;

    /**
     * 随机字符串 - 微信返回的随机字符串
     */
    @XmlField(value = "nonce_str")
    private String nonceStr;

    /**
     * 签名 - 微信返回的签名
     */
    @XmlField(value = "sign")
    private String sign;

    /**
     * 业务结果 - SUCCESS/FAIL
     */
    @XmlField(value = "result_code")
    private String resultCode;

    /**
     * 错误代码 - 请查看UnifiedOrdersErrorCode
     */
    @XmlField(value = "err_code")
    private String errCode;

    /**
     * 错误代码描述 - 错误返回的信息描述
     */
    @XmlField(value = "err_code_des")
    private String errCodeDes;

    /***************************** 以下字段在return_code 和result_code都为SUCCESS的时候有返回 *******************/
    /**
     * 交易类型 - 调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP
     */
    @XmlField(value = "trade_type")
    private String tradeType;

    /**
     * 预支付交易会话标识 - 微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
     */
    @XmlField(value = "prepay_id")
    private String prepayId;

    /**
     * 二维码链接 - trade_type为NATIVE是有返回，可将该参数值生成二维码展示出来进行扫码支付
     */
    @XmlField(value = "code_url")
    private String codeUrl;

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

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

}
