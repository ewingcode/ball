package com.ewing.order.core.web.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ewing.order.core.app.bean.UserInfo;
import com.ewing.order.core.app.session.SessionControl;
import com.ewing.order.core.web.common.RequestJson;
import com.ewing.order.core.web.common.RestResult;
import com.ewing.order.util.HttpUtils;
import com.ewing.order.util.SignManage;
import com.google.gson.Gson;

/**
 * Rest接口的基础父类
 * 
 * @author tansonlam
 * @create 2016年12月28日
 * 
 */
public abstract class BaseRest {

	protected final static Logger logger = LoggerFactory.getLogger(BaseRest.class);

	protected static final Gson gson = new Gson();

	private final static String charset = "utf-8";

	@Autowired
	protected HttpServletRequest request;
	@Autowired
	protected HttpServletResponse response;

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
		this.response.setContentType("application/json;charset=UTF-8");
	}

	@ExceptionHandler({ Exception.class })
	public void exception(Exception e) {
		logger.error(e.getMessage(),e);
		outErrorResult(e.getMessage());
	}

	/**
	 * 返回错误json
	 * 
	 * @param errMessage
	 */
	protected void outErrorResult(String errMessage) {
		RestResult<String> restResult = RestResult.errorResult(errMessage);
		try {
			response.setContentType("application/json;charset=UTF-8");
			response.getOutputStream().write(gson.toJson(restResult).getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 获取登录用户信息
	 * 
	 * @return
	 */
	protected UserInfo getLoginUser() {
		return SessionControl.getUserInfo(request);
	}

	/**
	 * 获取登录用户ID
	 * 
	 * @return
	 */
	protected Long getLoginUserId() {
		return SessionControl.getUserId(request);
	}

	/**
	 * 获取请求的json
	 * 
	 * @return
	 */
	protected RequestJson getRequestJson() {
		String method = request.getMethod();

		if ("POST".equalsIgnoreCase(method)) {
			try {
				InputStream ins = request.getInputStream();
				if (ins != null) {
					String json = null; 
					json = IOUtils.toString(ins, "utf-8");
					if (StringUtils.isEmpty(json)) {
						Map<String, String[]> p = request.getParameterMap();
						for (String key : p.keySet()) {
							json = key;
						}
					} 
					RequestJson requestJson = new RequestJson(json);
					SignManage.verify(requestJson.getEncrypt(), requestJson.getClientId(),
							requestJson.getSign(), requestJson.getDataStr());
					return requestJson;
				}
			} catch (IOException e) {
				logger.error("fail to parse the request json.", e);
			}
		}
		return RequestJson.newEmpty();
	}

	/**
	 * 转换请求json字符到指定类
	 * 
	 * @param clazz
	 * @return
	 */
	protected <T> T requestJson2Obj(Class<T> clazz) {
		return getRequestJson().requestJson2Obj(clazz);
	}

	/**
	 * 检查对象是否为空
	 * 
	 * @param checkedObj
	 * @param name
	 */
	protected void checkRequired(Object checkedObj, String name) {
		if (checkedObj == null)
			throw new IllegalArgumentException(name + "不能为空！");
		if (checkedObj instanceof String) {
			if (StringUtils.isEmpty(checkedObj.toString()))
				throw new IllegalArgumentException(name + "不能为空！");
		}
	}

}
