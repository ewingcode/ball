package com.ewing.order.busi.weixin.dto;

public enum PayStatus {

  APPLY("0"),SUCCESS("1"), FAIL("2");

  private String value;

  PayStatus(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}
