/**
 * 
 */
package com.ewing.order.busi.weixin.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ewing.order.busi.weixin.dao.PayHistoryDao;
import com.ewing.order.busi.weixin.ddl.PayHistory;
import com.ewing.order.busi.weixin.dto.BusiType;
import com.ewing.order.busi.weixin.dto.PayType;
import com.ewing.order.busi.weixin.dto.WxPayUnifiedOrdersDTO;
import com.ewing.order.common.prop.WeixinProp;
import com.ewing.order.util.UUIDUtils;
import com.ewing.order.weixin.wxpay.api.WxpayApi;
import com.ewing.order.weixin.wxpay.api.WxpayApiImpl;
import com.ewing.order.weixin.wxpay.api.unifiedorders.vo.UnifiedOrdersResDto;
import com.ewing.order.weixin.wxpay.protocol.SignGenerator;
import com.ewing.order.weixin.wxpay.vo.annotation.Comment;

/**
 * @author 47652
 *
 * @since 2017年2月15日
 */
@Component
public class WxPayService {

	@Resource
	private PayHistoryDao payHistoryDao;

	private WxpayApi payApi = new WxpayApiImpl();

	private Logger LOG = LoggerFactory.getLogger(WxPayService.class);

	/**
	 * 获取prePayId
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public String getprePayId(WxPayUnifiedOrdersDTO dto) {

		UnifiedOrdersResDto resDto = null;
		try {
			resDto = payApi.unifiedOrders(dto.getMsg(), dto.getDetail(), dto.getBizId(), dto.getCost(),
					dto.getClientIp(), new Date(), dto.getGoodsTag(), dto.getResourceId(), dto.getOpenId());
		} catch (Exception e) {
			LOG.info("调用微信统一下单接口发送异常", e);
		}

		PayHistory payHistory = new PayHistory();
		payHistory.setBizId(UUIDUtils.getBizCode());
		// payHistory.setBusiId(busiId);
		payHistory.setBusiType(BusiType.ORDER.getValue());
		payHistory.setComment(dto.getMsg());
		payHistory.setCreateTime(new Date());
		// payHistory.setCustomerId(dto.getUserId());
		payHistory.setUserId(dto.getUserId());
		payHistory.setPayFee(new BigDecimal(dto.getCost() / 100d));
		payHistory.setPayType(PayType.WXPAY.getValue());
		payHistory.setReturnCode(resDto.getResultCode());
		payHistory.setReturnMsg(resDto.getReturnMsg());
		payHistory.setPrePayId(resDto.getPrepayId());

		payHistoryDao.insert(payHistory);

		return resDto.getPrepayId();

	}

	/**
	 * 获取签名
	 * 
	 * @param params
	 * @return
	 */
	public String getSign(Map<String, Object> params) {
		if (params == null || params.isEmpty()) {
			return "";
		}
		return SignGenerator.createSign(new TreeMap<String, Object>(params), WeixinProp.payKey);

	}

}
