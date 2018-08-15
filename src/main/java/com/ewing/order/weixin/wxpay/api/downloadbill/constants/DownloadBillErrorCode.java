package com.ewing.order.weixin.wxpay.api.downloadbill.constants;

/**
 * 下载对账单错误码
 * 
 * @author Joeson Chan<chenxuegui1234@163.com>
 * @since 2016年1月26日
 *
 */
public enum DownloadBillErrorCode {
    
    SYSTEMERROR("SYSTEMERROR", "接口返回错误", "系统超时",  "请尝试再次查询。"),
    INVALID_TRANSACTIONID("INVALID_TRANSACTIONID", "无效transaction_id" , "请求参数未按指引进行填写",  "参数错误，请重新检查"),
    PARAM_ERROR("PARAM_ERROR", " 参数错误" , "请求参数未按指引进行填写",  "参数错误，请重新检查");
    
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
    
    DownloadBillErrorCode(String code,String des,String cause,String deal){
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
