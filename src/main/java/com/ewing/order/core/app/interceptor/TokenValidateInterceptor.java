package com.ewing.order.core.app.interceptor;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ewing.order.core.app.bean.UserInfo;
import com.ewing.order.core.app.session.SessionControl;
import com.ewing.order.core.oauth.JwtClaim;
import com.ewing.order.core.oauth.JwtUtil;
import com.ewing.order.core.web.common.RestResult;
import com.ewing.order.util.JsonUtils;

/**
 * Token验证拦截器
 * 
 * @author tanson lam
 * @creation 2017年1月7日
 * 
 */
public class TokenValidateInterceptor implements HandlerInterceptor {
	private static Logger logger = LoggerFactory.getLogger(TokenValidateInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token = StringUtils.defaultIfBlank(request.getHeader("Authorization"), "");
		// 验证token是否有效
		if (StringUtils.isEmpty(token)) {
			outResult(response, "Illegal access token.");
			return false;
		}

		try {
			JwtClaim jwtClaim = JwtUtil.parseToken(token);
			if (jwtClaim == null) {
				outResult(response, "wrong token.");
				return false;
			}
			set4UserInfo(jwtClaim, request);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			outResult(response, "wrong access token.");
			return false;
		}
		return true;
	}

	/**
	 * 设置用户信息
	 * 
	 * @param jwtClaim
	 * @param request
	 */
	private void set4UserInfo(JwtClaim jwtClaim, HttpServletRequest request) {
		SessionControl.setUserInfo(request, new UserInfo(null, jwtClaim.getUserName()));
	}

	private void outResult(HttpServletResponse response, String outMessage) throws IOException {
		response.setContentType("application/json");
		ServletOutputStream out = response.getOutputStream();
		RestResult<?> restfulResult = RestResult.errorResult(outMessage);
		String outMsg = JsonUtils.toJson(restfulResult);
		out.write(outMsg.getBytes("utf-8"));
		out.flush();
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
