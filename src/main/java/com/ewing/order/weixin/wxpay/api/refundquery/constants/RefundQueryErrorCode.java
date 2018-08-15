package com.ewing.order.weixin.wxpay.api.refundquery.constants;

/**
 * 查询退款错误码
 * 
 * @author Joeson Chan<chenxuegui1234@163.com>
 * @since 2016年1月26日
 *
 */
public enum RefundQueryErrorCode {
    
    SYSTEMERROR("SYSTEMERROR","接口返回错误","系统超时","请尝试再次掉调用API。"),
    INVALID_TRANSACTIONID("INVALID_TRANSACTIONID","无效transaction_id","请求参数未按指引进行填写","请求参数错误，检查原交易号是否存在或发起支付交易接口返回失败"),
    PARAM_ERROR("PARAM_ERROR","参数错误","请求参数未按指引进行填写","请求参数错误，请检查参数再调用退款申请"),
    APPID_NOT_EXIST("APPID_NOT_EXIST","APPID不存在","参数中缺少APPID","请检查APPID是否正确"),
    MCHID_NOT_EXIST("MCHID_NOT_EXIST","MCHID不存在","参数中缺少MCHID","请检查MCHID是否正确"),
    APPID_MCHID_NOT_MATCH("APPID_MCHID_NOT_MATCH","appid和mch_id不匹配","appid和mch_id不匹配","请确认appid和mch_id是否匹配"),
    REQUIRE_POST_METHOD("REQUIRE_POST_METHOD","请使用post方法","未使用post传递参数","请检查请求参数是否通过post方法提交"),
    SIGNERROR("SIGNERROR","签名错误","参数签名结果不正确","请检查签名参数和方法是否都符合签名算法要求"),
    XML_FORMAT_ERROR("XML_FORMAT_ERROR","XML格式错误","XML格式错误","请检查XML参数格式是否正确");

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
    
    RefundQueryErrorCode(String code,String des,String cause,String deal){
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
