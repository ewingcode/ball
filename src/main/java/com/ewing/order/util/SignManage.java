package com.ewing.order.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewing.order.core.oauth.OAuthClientManage;
import com.ewing.order.core.oauth.OAuthException;
import com.ewing.order.core.web.base.ResponseCode;
import com.ewing.order.core.web.exception.BusinessException;

/**
 * 请求签名工具类
 * 
 * @author tansonlam
 * @create 2016年12月29日
 * 
 */
public class SignManage {

	protected final static Logger logger = LoggerFactory.getLogger(SignManage.class);

	private final static String secretKey = "123321";

	public static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
				'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	private static String sign(String encrypt, String content) throws BusinessException {
		try {
			content = java.net.URLEncoder.encode(content, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		if (StringUtils.isEmpty(encrypt))
			throw new BusinessException(ResponseCode.PARAM_ILLEGAL, "加密方式不能为空！");

		if (encrypt.equalsIgnoreCase(SignStrategy.md5.name())) {
			return MD5(content).toLowerCase();
		}

		throw new BusinessException(ResponseCode.PARAM_ILLEGAL, "不支持该类型的签名:" + encrypt);
	}

	public static Boolean verify(String encrypt, String clientId, String requestSign,
			String requestBody) throws BusinessException {
		if (StringUtils.isEmpty(encrypt))
			throw new BusinessException(ResponseCode.PARAM_ILLEGAL, "加密方式不能为空！");
		if (StringUtils.isEmpty(clientId))
			throw new BusinessException(ResponseCode.PARAM_ILLEGAL, "clientId不能为空！");
		if (StringUtils.isEmpty(requestSign))
			throw new BusinessException(ResponseCode.PARAM_ILLEGAL, "sign不能为空！");
		// String secretKey;
		/*
		 * try { secretKey = OAuthClientManage.getClientSecretKey(clientId); }
		 * catch (OAuthException e) { throw new
		 * BusinessException(ResponseCode.PARAM_ILLEGAL, "无法获取clientId[" +
		 * clientId + "]的密钥"); }
		 */
		String sign = sign(encrypt, (clientId + requestBody + secretKey).trim());
		if (requestSign.equals(sign)) {
			return true;
		}
		logger.info("签名校验不通过,系统签名:" + sign + ",外部签名:" + requestSign);
		throw new BusinessException(ResponseCode.WRONG_SIGN, "签名校验不通过");
	}
}
