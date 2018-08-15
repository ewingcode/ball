package com.ewing.order.busi.weixin.dto;

public enum PayType {
  
  WXPAY(0),ALIPAY(1);
  
  private int value;
  
  PayType(int value){
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
