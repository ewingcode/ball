package com.ewing.order.weixin.wxpay.api.paycallback.vo;

import com.ewing.order.weixin.wxpay.constants.ReturnCode;
import com.ewing.order.weixin.wxpay.vo.XmlFieldVo;
import com.ewing.order.weixin.wxpay.vo.annotation.XmlField;

/**
 * 支付结果通用通知 - 回调返回数据
 * 
 * @author Joeson Chan<chenxuegui1234@163.com>
 * @since 2016年1月24日
 *
 */
public class PayNotifyCallBackResDto implements XmlFieldVo{
  
  public static PayNotifyCallBackResDto suc(){
    return new PayNotifyCallBackResDto(ReturnCode.SUCCESS.name(), "");
  }
  
  public static PayNotifyCallBackResDto error(String msg){
    return new PayNotifyCallBackResDto(ReturnCode.FAIL.name(), msg);
  }
  
    
    /**
     * 返回状态码 - SUCCESS/FAIL SUCCESS表示商户接收通知成功并校验成功
     */
    @XmlField(value="return_code")
    private String returnCode;

    /**
     * 返回信息 - 返回信息，如非空，为错误原因 
     */
    @XmlField(value="return_msg")
    private String returnMsg;

    
    public PayNotifyCallBackResDto() {
      super();
    }

    public PayNotifyCallBackResDto(String returnCode, String returnMsg) {
      super();
      this.returnCode = returnCode;
      this.returnMsg = returnMsg;
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
    
    
}
