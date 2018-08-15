package com.ewing.order.weixin.wxpay.api.payitilreport.vo;

import com.ewing.order.weixin.wxpay.vo.annotation.XmlField;

/**
 * 下载对账单返回结果
 * 
 * @author Joeson Chan<chenxuegui1234@163.com>
 * @since 2016年1月26日
 *
 */
public class PayitilReportResDto {

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
    
    


}
