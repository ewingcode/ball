package com.ewing.order.weixin.wxpay;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewing.order.common.prop.WeixinProp;
import com.ewing.order.util.BizGenerator;
import com.ewing.order.util.DateUtil;
import com.ewing.order.weixin.wxpay.api.unifiedorders.vo.UnifiedOrdersReqParam;
import com.ewing.order.weixin.wxpay.constants.TradeType;


public class WxPayVoUtils {

  /**
   * 默认生成的该类的LOG记录器，使用slf4j组件。避免产生编译警告，使用protected修饰符。
   */
  protected final static Logger LOG = LoggerFactory.getLogger(WxPayVoUtils.class);

  /**
   * 
   * @param msg 商品或支付单简要描述：商品、属性 Ipad mini 16G 白色
   * @param detail 商品或支付单简要描述：商品、属性 Ipad mini 16G 白色
   * @param bizId
   * @param cost
   * @param clientIp
   * @param createTime
   * @param goodsTag 商品标记，代金券或立减优惠功能的参数
   * @param resourceId
   * @param openId
   * @return created by Joeson on 2016年6月14日 上午10:03:34
   */
  public static UnifiedOrdersReqParam initUnifiedOrdersReq(String msg, String detail, String bizId,
      Integer cost, String clientIp, Date createTime, String goodsTag, Integer resourceId,
      String openId) {
    UnifiedOrdersReqParam param = new UnifiedOrdersReqParam();
    param.setAppId(WeixinProp.appId);
    param.setMchId(WeixinProp.mchId);
    param.setDeviceInfo("WEB");
    param.setNonceStr(BizGenerator.generateUUID());
    param.setBody(msg);
    param.setDetail(detail);
    param.setAttach(bizId);
    param.setOutTradeNo(bizId);
    param.setFeeType("CNY");
    param.setTotalFee("" + cost);
    param.setSpBillCreateIp("120.25.210.50");
    param.setTimeStart(DateUtil.format(createTime, DateUtil.TIME_FORMAT1));
    param.setTimeExpire(
        DateUtil.format(DateUtil.getNextDate(createTime, 365), DateUtil.TIME_FORMAT1));
//    param.setGoodsTag(goodsTag);
    param.setNotifyUrl(WeixinProp.payNotifyUrl);
    param.setTradeType(TradeType.JSAPI.name());
    param.setProductId(String.valueOf(resourceId));
    param.setLimitPay("no_credit");
    param.setOpenId(openId);
    return param;
  }
}
