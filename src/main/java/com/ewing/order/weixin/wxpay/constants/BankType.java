package com.ewing.order.weixin.wxpay.constants;

public enum BankType {

    ICBC_DEBIT("工商银行（借记卡）"),  
    ICBC_CREDIT ("工商银行（信用卡）"),  
    ABC_DEBIT ("农业银行（借记卡）"),
    ABC_CREDIT ("农业银行 （信用卡） "),
    PSBC_DEBIT ("邮政储蓄（借记卡） "),
    PSBC_CREDIT ("邮政储蓄 （信用卡） "),
    CCB_DEBIT ("建设银行（借记卡）"),
    CCB_CREDIT ("建设银行 （信用卡）"),
    CMB_DEBIT ("招商银行（借记卡）"),
    CMB_CREDIT ("招商银行（信用卡） "),
    COMM_DEBIT ("交通银行（借记卡） "),
    BOC_CREDIT ("中国银行（信用卡） "),
    SPDB_DEBIT ("浦发银行（借记卡）"),
    SPDB_CREDIT ("浦发银行 （信用卡） "),
    GDB_DEBIT ("广发银行（借记卡）"),
    GDB_CREDIT ("广发银行（信用卡） "),
    CMBC_DEBIT ("民生银行（借记卡） "),
    CMBC_CREDIT ("民生银行（信用卡） "),
    PAB_DEBIT ("平安银行（借记卡）"),
    PAB_CREDIT ("平安银行（信用卡）"),
    CEB_DEBIT ("光大银行（借记卡）"),
    CEB_CREDIT ("光大银行（信用卡）"),
    CIB_DEBIT ("兴业银行 （借记卡）"),
    CIB_CREDIT ("兴业银行（信用卡）"),
    CITIC_DEBIT ("中信银行（借记卡）"),
    CITIC_CREDIT ("中信银行（信用卡） "),
    SDB_CREDIT ("深发银行（信用卡） "),
    BOSH_DEBIT ("上海银行（借记卡）"),
    BOSH_CREDIT ("上海银行 （信用卡）"),
    CRB_DEBIT ("华润银行（借记卡）"),
    HZB_DEBIT ("杭州银行（借记卡）"),
    HZB_CREDIT ("杭州银行（信用卡）"),
    BSB_DEBIT ("包商银行（借记卡）"),
    BSB_CREDIT ("包商银行 （信用卡） "),
    CQB_DEBIT ("重庆银行（借记卡）"),
    SDEB_DEBIT ("顺德农商行 （借记卡）"), 
    SZRCB_DEBIT ("深圳农商银行（借记卡） "),
    HRBB_DEBIT ("哈尔滨银行（借记卡）"),
    BOCD_DEBIT ("成都银行（借记卡）"),
    GDNYB_DEBIT ("南粤银行 （借记卡）"),
    GDNYB_CREDIT ("南粤银行 （信用卡） "),
    GZCB_CREDIT ("广州银行（信用卡）"),
    JSB_DEBIT ("江苏银行（借记卡）"),
    JSB_CREDIT ("江苏银行（信用卡） "),
    NBCB_DEBIT ("宁波银行（借记卡）"),
    NBCB_CREDIT ("宁波银行（信用卡）"),
    NJCB_DEBIT ("南京银行（借记卡）"),
    QDCCB_DEBIT ("青岛银行（借记卡）"),
    ZJTLCB_DEBIT ("浙江泰隆银行（借记卡）"),  
    XAB_DEBIT ("西安银行（借记卡）"),
    CSRCB_DEBIT ("常熟农商银行 （借记卡）"),  
    QLB_DEBIT ("齐鲁银行（借记卡）"),
    LJB_DEBIT ("龙江银行（借记卡）"),
    HXB_DEBIT ("华夏银行（借记卡）"),
    CS_DEBIT ("测试银行借记卡快捷支付 （借记卡）"),  
    AE_CREDIT ("AE（信用卡）"),
    JCB_CREDIT ("JCB（信用卡） "),
    MASTERCARD_CREDIT ("MASTERCARD（信用卡）"),  
    VISA_CREDIT("VISA（信用卡）");
    
    private String msg;
    
    BankType(String msg){
        this.msg = msg;
    }
    
    
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    
    
    

    
}
