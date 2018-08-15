package com.ewing.order.weixin.wxpay.api.closeorder.vo;

import com.ewing.order.weixin.wxpay.vo.annotation.XmlField;


/**
 * 查询订单返回结果
 * 
 * @author Joeson Chan<chenxuegui1234@163.com>
 * @since 2016年1月26日
 *
 */
public class CloseOrderResDto {

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
     * 错误码
     */
    @XmlField(value = "err_code")
    private String errCode;

    /**
     * 结果信息描述
     */
    @XmlField(value = "err_code_des")
    private String errCodeDes;

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
    

}
