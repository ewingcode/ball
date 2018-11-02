package com.ewing.order.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author tanson lam
 * @create 2016年9月5日
 */
public class HttpUtils {
	private static Logger log = LoggerFactory.getLogger(HttpUtils.class);

	/**
	 * 是否错误的返回码
	 * 
	 * @param response
	 * @return
	 */
	public static Boolean isErrorResp(String response) {
		return response == null || response.equals("-1");
	}

	public static String request(String url, String method, Map<String, String> params,
			Map<String, String> headerAttribute) {
		return request(url, method, params, headerAttribute, null);
	}

	public static String request2(String url, String method, String params,
			Map<String, String> headerAttribute, String charsetName) {

		HttpURLConnection conn = null;
		BufferedOutputStream bos = null;
		InputStream is = null;

		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setConnectTimeout(1000 * 60);
			conn.setReadTimeout(1000 * 60);
			conn.setDefaultUseCaches(false);

			conn.setInstanceFollowRedirects(true);
			conn.setRequestMethod(method);
			conn.setDoInput(true);
			conn.setDoOutput(true);

			if (null != headerAttribute) {
				// 设置HttpRequest请求头的属性
				for (Map.Entry<String, String> entry : headerAttribute.entrySet()) {
					conn.addRequestProperty(entry.getKey(), entry.getValue());
				}
			}

			if (null != params) {
				// 构建请求参数列格式a=aaa&b=bbb&c=ccc
				StringBuilder sb = new StringBuilder("");
				 sb.append(params);
				log.info("http request:"+params);
				String body = sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";

				bos = new BufferedOutputStream(conn.getOutputStream());
				bos.write(body.getBytes());
				bos.flush();
				bos.close();
			}

			Integer responseCode = conn.getResponseCode();
			log.debug("[code:" + responseCode + "] : request - > " + url);

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),
					null != charsetName ? charsetName : "utf8"));
			String line = "";
			String result = "";
			while (null != (line = br.readLine())) {
				result += line;
			}

			return result; 
		} catch (Exception e) {
			// 网络异常返回-1
			log.error(e.getMessage(), e);
			log.debug("request url[" + url + "] error, msg: " + e.getMessage());
			return "-1";
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (conn != null)
				conn.disconnect();
		}
	}
	/**
	 * <pre>
	 * Http请求调用处理
	 * </pre>
	 * 
	 * @param url
	 *            请求命令行
	 * @param method
	 *            POST/GET/...
	 * @param params
	 *            (POST可以设计的请求体)，如果为GET，则该系数为空
	 * @param charsetName
	 *            字符集，默认utf8
	 * @param headerAttribute
	 *            请求头一些属性键对
	 * @return 返回响应体，如果发生异常的时候反悔null，网络异常时候返回"-1"
	 */
	public static String request(String url, String method, Map<String, String> params,
			Map<String, String> headerAttribute, String charsetName) {

		HttpURLConnection conn = null;
		BufferedOutputStream bos = null;
		InputStream is = null;

		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setConnectTimeout(1000 * 60);
			conn.setReadTimeout(1000 * 60);
			conn.setDefaultUseCaches(false);

			conn.setInstanceFollowRedirects(true);
			conn.setRequestMethod(method);
			conn.setDoInput(true);
			conn.setDoOutput(true);

			if (null != headerAttribute) {
				// 设置HttpRequest请求头的属性
				for (Map.Entry<String, String> entry : headerAttribute.entrySet()) {
					conn.addRequestProperty(entry.getKey(), entry.getValue());
				}
			}

			if (null != params) {
				// 构建请求参数列格式a=aaa&b=bbb&c=ccc
				StringBuilder sb = new StringBuilder("");
				for (Map.Entry<String, String> entry : params.entrySet()) {
					sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
				}
				//log.info("http request:"+sb.toString());
				String body = sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";

				bos = new BufferedOutputStream(conn.getOutputStream());
				bos.write(body.getBytes());
				bos.flush();
				bos.close();
			}

			Integer responseCode = conn.getResponseCode();
			//log.debug("[code:" + responseCode + "] : request - > " + url);

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),
					null != charsetName ? charsetName : "utf8"));
			String line = "";
			String result = "";
			while (null != (line = br.readLine())) {
				result += line;
			}

			return result; 
		} catch (Exception e) {
			// 网络异常返回-1
			log.error(e.getMessage(), e);
			log.debug("request url[" + url + "] error, msg: " + e.getMessage());
			return "-1";
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (conn != null)
				conn.disconnect();
		}
	}
	public static String requestJson(String url, String method, String json,
			Map<String, String> headerAttribute ){
		return requestJson(url, method, json, headerAttribute,"UTF-8");
	}
	/**
	 * <pre>
	 * Http请求调用处理
	 * </pre>
	 * 
	 * @param url
	 *            请求命令行
	 * @param method
	 *            POST/GET/...
	 * @param params
	 *            (POST可以设计的请求体)，如果为GET，则该系数为空
	 * @param charsetName
	 *            字符集，默认utf8
	 * @param headerAttribute
	 *            请求头一些属性键对
	 * @return 返回响应体，如果发生异常的时候反悔null，网络异常时候返回"-1"
	 */
	public static String requestJson(String url, String method, String json,
			Map<String, String> headerAttribute, String charsetName) {
		HttpURLConnection conn = null;
		BufferedOutputStream bos = null;
		InputStream is = null;

		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setConnectTimeout(1000 * 60);
			conn.setReadTimeout(1000 * 60);
			conn.setDefaultUseCaches(false);

			conn.setInstanceFollowRedirects(true);
			conn.setRequestMethod(method);
			conn.setDoInput(true);
			conn.setDoOutput(true);

			if (null != headerAttribute) {
				// 设置HttpRequest请求头的属性
				for (Map.Entry<String, String> entry : headerAttribute.entrySet()) {
					conn.addRequestProperty(entry.getKey(), entry.getValue());
				}
			}

			if (null != json) {
				bos = new BufferedOutputStream(conn.getOutputStream());
				bos.write(json.getBytes());
				bos.flush();
				bos.close();
			}

			Integer responseCode = conn.getResponseCode();
			log.debug("[code:" + responseCode + "] : request - > " + url);

			// 以20X表示
			if (responseCode.toString().startsWith("20")) {
				int contentLength = conn.getHeaderFieldInt("Content-Length", -1);

				is = conn.getInputStream();

				AutoExpandByteBuffer bb = new AutoExpandByteBuffer();
				byte[] chuck = new byte[1024 * 4];
				int len = -1;
				while ((len = is.read(chuck)) != -1) {
					bb.put(chuck, 0, len);
					if (contentLength != -1 && bb.avaliable() == contentLength)
						break;
				}

				if (bb.avaliable() < contentLength) {
					throw new RuntimeException("reponse body incomplete.");
				}

				return bb.asString(null != charsetName ? charsetName : "gb2312");
			}

			byte[] b = new byte[4096];
			conn.getErrorStream().read(b);
			log.debug("ErrorStream:" + new String(b));
			return null;

		} catch (Exception e) {
			// 网络异常返回-1
			log.debug("request url[" + url + "] error, msg: " + e.getMessage());
			return "-1";
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (conn != null)
				conn.disconnect();
		}
	}

}
