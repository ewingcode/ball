/**
 * 
 */
package com.ewing.order.busi.weixin.action;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.nutz.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ewing.order.busi.weixin.dto.WxPayUnifiedOrdersDTO;
import com.ewing.order.busi.weixin.service.WxPayService;
import com.ewing.order.core.web.base.BaseRest;
import com.ewing.order.core.web.common.RestResult;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author 47652
 *
 * @since 2017年2月15日
 */
@RestController
public class WxPayRest extends BaseRest {

	@Resource
	private WxPayService wxPayService;

	private static final Logger logger = LoggerFactory.getLogger(WxPayRest.class);

	@RequestMapping("/weixin/wxPayCallback.op")
	@ResponseBody
	public String wxPayCallback(HttpServletRequest request) {

		return "<xml> <return_code><![CDATA[SUCCESS]]></return_code> <return_msg><![CDATA[OK]]></return_msg> </xml>";
	}

	@ApiOperation(value = "微信统一下单获取prepayid", notes = "")
	@ApiImplicitParams({ @ApiImplicitParam(name = "msg", value = "商品简单描述", required = true, dataType = "String"),
			@ApiImplicitParam(name = "detail", value = "商品详情", required = true, dataType = "String"),
			@ApiImplicitParam(name = "cost", value = "金额", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "clientIp", value = "客户端ip", required = true, dataType = "clientIp"),
			@ApiImplicitParam(name = "resourceId", value = "商品编码", required = true, dataType = "String") })
	@RequestMapping("/weixin/getPrepayId.op")
	@ResponseBody
	public RestResult<String> getPrepayId(WxPayUnifiedOrdersDTO dto) {
		String prePayId = "";
		try {
			prePayId = wxPayService.getprePayId(dto);

		} catch (Exception e) {
			logger.error("调用微信统一下单接口返回预支付id发生异常", e);
		}
		return RestResult.successResult(prePayId);

	}

	@ApiOperation(value = "微信业务的返回签名", notes = "")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "paramsJson", value = "参数json", required = true, dataType = "String") })
	@RequestMapping("/weixin/getWxServiceSign.op")
	@ResponseBody
	public RestResult<String> getWxServiceSign(String paramsJson) {
		@SuppressWarnings("unchecked")
		Map<String, Object> params = (Map<String, Object>) Json.fromJson(paramsJson);
		String sign = wxPayService.getSign(params);
		return RestResult.successResult(sign);
	}

}
