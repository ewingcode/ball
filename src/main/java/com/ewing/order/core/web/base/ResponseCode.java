package com.ewing.order.core.web.base;

/**
 * 返回码
 * 
 * @author tansonlam
 * @create 2016年12月29日
 * 
 */
public class ResponseCode {

	/**
	 * 成功
	 */
	public static final int OK = 200;

	/**
	 * 重定向
	 */
	public static final int REDIRECT = 301;

	/**** 系统内部错误码 ***************************************************************************/

	/**
	 * 服务端内部异常
	 */
	public static final int INTERNAL_ERROR = 5000000;

	/**
	 * 未知错误
	 */
	public static final int UNKOWN_ERROR = 5000001;

	/**
	 * 系统参数无效:参数缺失/参数值无效
	 */
	public static final int PARAM_ILLEGAL = 5000002;
	/**
	 * 错误请求签名
	 */
	public static final int WRONG_SIGN = 5000003; 
	/**
	 * 没有权限
	 */
	public static final int NOAUTH_ERROR=5000004;
}
