package com.ewing.order.weixin.wxpay.api;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewing.order.util.JsonUtils;
import com.ewing.order.util.MapUtils;
import com.ewing.order.weixin.wxpay.WxPayVoUtils;
import com.ewing.order.weixin.wxpay.api.applyrefund.vo.ApplyRefundReqParam;
import com.ewing.order.weixin.wxpay.api.applyrefund.vo.ApplyRefundResDto;
import com.ewing.order.weixin.wxpay.api.closeorder.vo.CloseOrderReqParam;
import com.ewing.order.weixin.wxpay.api.closeorder.vo.CloseOrderResDto;
import com.ewing.order.weixin.wxpay.api.downloadbill.vo.DownloadBillReqParam;
import com.ewing.order.weixin.wxpay.api.downloadbill.vo.DownloadBillResDto;
import com.ewing.order.weixin.wxpay.api.orderquery.vo.OrderQueryReqParam;
import com.ewing.order.weixin.wxpay.api.orderquery.vo.OrderQueryResDto;
import com.ewing.order.weixin.wxpay.api.payitilreport.vo.PayitilReportReqParam;
import com.ewing.order.weixin.wxpay.api.payitilreport.vo.PayitilReportResDto;
import com.ewing.order.weixin.wxpay.api.refundquery.vo.RefundQueryReqParam;
import com.ewing.order.weixin.wxpay.api.refundquery.vo.RefundQueryResDto;
import com.ewing.order.weixin.wxpay.api.unifiedorders.vo.UnifiedOrdersReqParam;
import com.ewing.order.weixin.wxpay.api.unifiedorders.vo.UnifiedOrdersResDto;
import com.ewing.order.weixin.wxpay.protocol.ApiClient;



/**
 * 支付接口的实现
 * 
 * @author Joeson Chan<chenxuegui1234@163.com>
 * @since 2016年1月24日
 *
 */
public class WxpayApiImpl implements WxpayApi {

  private static Logger logger = LoggerFactory.getLogger(WxpayApiImpl.class);

  @Override
  public UnifiedOrdersResDto unifiedOrders(String msg, String detail, String bizId, Integer cost,
      String clientIp, Date createTime, String goodsTag, Integer resourceId, String openId)
          throws Exception {
    UnifiedOrdersReqParam param = WxPayVoUtils.initUnifiedOrdersReq(msg, detail, bizId, cost,
        clientIp, createTime, goodsTag, resourceId, openId);
    logger.info(JsonUtils.toJson(param));
    return unifiedOrders(param);
  }

  private UnifiedOrdersResDto unifiedOrders(UnifiedOrdersReqParam param) throws Exception {
    Map<String, Object> paramsMap = MapUtils.toXmlFieldMap(param, UnifiedOrdersReqParam.class);
    logger.info("paramsMap : " + JsonUtils.toJson(paramsMap));
    UnifiedOrdersResDto result =
        ApiClient.post(unified_order_url, paramsMap, UnifiedOrdersResDto.class);

    logger.info(JsonUtils.toJson(result));
    return result;
  }

  @Override
  public OrderQueryResDto orderQuery(OrderQueryReqParam param) throws Exception {

    Map<String, Object> paramsMap = MapUtils.toMap(param, OrderQueryReqParam.class);

    OrderQueryResDto result = ApiClient.post(order_query_url, paramsMap, OrderQueryResDto.class);

    logger.info(JsonUtils.toJson(result));
    return result;
  }

  @Override
  public CloseOrderResDto orderQuery(CloseOrderReqParam param) throws Exception {
    Map<String, Object> paramsMap = MapUtils.toMap(param, CloseOrderReqParam.class);

    CloseOrderResDto result = ApiClient.post(order_query_url, paramsMap, CloseOrderResDto.class);

    logger.info(JsonUtils.toJson(result));
    return result;
  }

  @Override
  public ApplyRefundResDto applyRefund(ApplyRefundReqParam param) throws Exception {
    Map<String, Object> paramsMap = MapUtils.toMap(param, ApplyRefundReqParam.class);

    ApplyRefundResDto result = ApiClient.post(order_query_url, paramsMap, ApplyRefundResDto.class);

    logger.info(JsonUtils.toJson(result));
    return result;
  }

  @Override
  public RefundQueryResDto refundQuery(RefundQueryReqParam param) throws Exception {
    Map<String, Object> paramsMap = MapUtils.toMap(param, RefundQueryReqParam.class);

    RefundQueryResDto result = ApiClient.post(order_query_url, paramsMap, RefundQueryResDto.class);

    logger.info(JsonUtils.toJson(result));
    return result;
  }

  @Override
  public DownloadBillResDto downBill(DownloadBillReqParam param) throws Exception {
    Map<String, Object> paramsMap = MapUtils.toMap(param, DownloadBillReqParam.class);

    DownloadBillResDto result =
        ApiClient.post(order_query_url, paramsMap, DownloadBillResDto.class);
    logger.info(JsonUtils.toJson(result));
    return result;
  }

  @Override
  public PayitilReportResDto payitilReport(PayitilReportReqParam param) throws Exception {
    Map<String, Object> paramsMap = MapUtils.toMap(param, PayitilReportReqParam.class);

    PayitilReportResDto result =
        ApiClient.post(order_query_url, paramsMap, PayitilReportResDto.class);
    logger.info(JsonUtils.toJson(result));
    return result;
  }

}
