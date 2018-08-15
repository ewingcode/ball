/**
 * 
 */
package com.ewing.order.busi.weixin.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nutz.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ewing.order.busi.weixin.dto.WxWebAuthorizeReq;
import com.ewing.order.busi.weixin.service.WxAuthService;
import com.ewing.order.core.web.base.BaseRest;
import com.ewing.order.core.web.common.RestResult;
import com.ewing.order.weixin.dto.WebAuthorizationDto;
import com.ewing.order.weixin.dto.WebAuthorizationUserInfo;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author 47652
 *
 * @since 2017年2月8日
 */
@RestController
public class WxAuthRest extends BaseRest {

	private static final Logger logger = LoggerFactory.getLogger(WxAuthRest.class);

	@Resource(name = "wxAuthService")
	private WxAuthService wxAuthService;

	/**
	 * 获取用户微信网页获取用户信息的第一步骤的code，并进行第二、三、四步骤，设置用户登陆信息
	 * 
	 * 
	 */
	@ApiOperation(value = "微信网页授权回调", notes = "")
	@ApiImplicitParams({ @ApiImplicitParam(name = "shopId", value = "商铺ID", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "tableId", value = "餐桌ID", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "state", value = "备注信息", required = true, dataType = "String") })
	@RequestMapping("/weixin/getWebAuthCode.op")
	@ResponseBody
	public RestResult<WebAuthorizationUserInfo> getWebAuthCode(HttpServletRequest request,
			@RequestParam("code") String code, @RequestParam("state") String state) {

		WxWebAuthorizeReq req = new WxWebAuthorizeReq(code, state);

		logger.info("第一步：微信返回的信息code【{}】", Json.toJson(req));

		WebAuthorizationDto webAuthorization = wxAuthService.getWebAuthorization(req);

		logger.info("第二步：微信返回accessToken【{}】", Json.toJson(webAuthorization));
		if (webAuthorization == null) {
			outErrorResult("登陆验证失败");
		}
		WebAuthorizationUserInfo userInfo = wxAuthService.getByOpenId(webAuthorization.getOpenId());

		if (userInfo == null) {
			userInfo = wxAuthService.getWxUserInfo(webAuthorization.getAccessToken(), webAuthorization.getOpenId());
			logger.info("第三步：微信返回用户信息【{}】", Json.toJson(userInfo));
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("userInfo", userInfo);

		return RestResult.successResult(userInfo);
	}

}
