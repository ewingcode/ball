package com.ewing.order.core.app.session;

import javax.servlet.http.HttpServletRequest;

import com.ewing.order.core.app.bean.UserInfo;

public class SessionControl {
	private final static String USER_INFO_SESSION = "USER_INFO_SESSION";

	/**
	 * 获取登陆用户ID
	 * 
	 * @param request
	 * @return
	 * @throws SessionException
	 */
	public static Long getUserId(HttpServletRequest request)
			throws SessionException {
		return getUserInfo(request).getId();
	}

	/**
	 * 获取用户的 session.
	 * 
	 * @param request
	 * @return
	 * @throws SessionException
	 */
	public static UserInfo getUserInfo(HttpServletRequest request)
			throws SessionException {
		if (request == null)
			throw new IllegalArgumentException("request should not be null");
		Object userObject = request.getSession()
				.getAttribute(USER_INFO_SESSION);
		if (userObject == null) {
			throw new SessionException("user session is null");
		}
		return (UserInfo) userObject;
	}

	/**
	 * 设置用户的 session.
	 * 
	 * @param request
	 * @return
	 * @throws SessionException
	 */
	public static void setUserInfo(HttpServletRequest request, UserInfo userInfo)
			throws SessionException {
		if (request == null)
			throw new IllegalArgumentException("request should not be null");
		if (userInfo == null)
			throw new IllegalArgumentException("userinfo should not be null");
		request.getSession().setAttribute(USER_INFO_SESSION, userInfo);
	}

	/**
	 * 删除用户的 session.
	 * 
	 * @param request
	 * @return
	 * @throws SessionException
	 */
	public static void removeUserInfo(HttpServletRequest request)
			throws SessionException {
		if (request == null)
			throw new IllegalArgumentException("request should not be null");
		request.getSession().removeAttribute(USER_INFO_SESSION);
	}

}
