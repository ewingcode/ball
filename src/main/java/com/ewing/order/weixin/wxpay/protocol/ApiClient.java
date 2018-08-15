package com.ewing.order.weixin.wxpay.protocol;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.KeyStore;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewing.order.common.prop.WeixinProp;
import com.ewing.order.util.HttpClientSendPost;
import com.google.common.collect.Maps;

/**
 * 微信支付请求客户端工具
 * 
 * @author Joeson Chan<chenxuegui1234@163.com>
 * @since 2016年1月24日
 *
 */
@SuppressWarnings("deprecation")
public class ApiClient {

  private static Logger logger = LoggerFactory.getLogger(ApiClient.class);

  public static <T> T post(String url, Map<String, Object> params, Class<T> clazz)
      throws Exception {
    String content = post(url, params);
    if (StringUtils.isEmpty(content)) {
      return null;
    }

    T t = null;
    try {
      logger.info("content : " + content);
      t = XmlMsgHelper.toObject(content, clazz);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }

    return t;
  }

  public static String post(String url, Map<String, Object> params) throws Exception {
    if (StringUtils.isEmpty(url) || MapUtils.isEmpty(params)) {
      return StringUtils.EMPTY;
    }

    String sign = SignGenerator.createSign(new TreeMap<>(params), WeixinProp.payKey);
    params.put("sign", sign);
    return post(url, XmlMsgHelper.map2Xml(params));
  }
  
  public static String certPost(String url, String params, boolean certEncrypt) {
    // @TODO 配置证书文件以及商户ID
    File certOfP12 = null;
    String clientId = null;

    try {
      return certPost(certOfP12, clientId, url, params);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return StringUtils.EMPTY;
    }
  }

  /**
   * post 支持wx支付安全
   * 
   * @param certOfP12 p12证书文件
   * @param clientId 商户ID
   * @throws Exception
   * @author Joeson
   * @return
   */
  private static String post(String url, String params) throws Exception {
    logger.info("params : " + params);
    return HttpClientSendPost.sendXMLDataByPost(url, params);
  }
  
  
  /**
   * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
   * @param strxml
   * @return
   * @throws JDOMException
   * @throws IOException
   */
  public static Map<String,String> doXMLParse(String strxml) throws Exception {
      if(null == strxml || "".equals(strxml)) {
          return null;
      }
      
      Map<String,String> m = Maps.newHashMap();
      InputStream in = String2Inputstream(strxml);
      SAXBuilder builder = new SAXBuilder();
      Document doc = builder.build(in);
      Element root = doc.getRootElement();
      List<?> list = root.getChildren();
      Iterator<?> it = list.iterator();
      while(it.hasNext()) {
          Element e = (Element) it.next();
          String k = e.getName();
          String v = "";
          List<?> children = e.getChildren();
          if(children.isEmpty()) {
              v = e.getTextNormalize();
          } else {
              v = getChildrenText(children);
          }
          
          m.put(k, v);
      }
      
      //关闭流
      in.close();
      
      return m;
  }
  
  public static InputStream String2Inputstream(String str) {
    return new ByteArrayInputStream(str.getBytes());
}
  
  /**
   * 获取子结点的xml
   * @param children
   * @return String
   */
  public static String getChildrenText(List<?> children) {
      StringBuffer sb = new StringBuffer();
      if(!children.isEmpty()) {
          Iterator<?> it = children.iterator();
          while(it.hasNext()) {
              Element e = (Element) it.next();
              String name = e.getName();
              String value = e.getTextNormalize();
              List<?> list = e.getChildren();
              sb.append("<" + name + ">");
              if(!list.isEmpty()) {
                  sb.append(getChildrenText(list));
              }
              sb.append(value);
              sb.append("</" + name + ">");
          }
      }
      
      return sb.toString();
  }

  /**
   * post 支持wx支付安全
   * 
   * @param certOfP12 p12证书文件
   * @param clientId 商户ID
   * @throws Exception
   * @author Joeson
   * @return
   */
  private static String certPost(File certOfP12, String clientId, String url, String params)
      throws Exception {
    // 指定读取证书格式为PKCS12
    KeyStore keyStore = KeyStore.getInstance("PKCS12");
    // 读取本机存放的PKCS12证书文件
    FileInputStream instream = new FileInputStream(certOfP12);
    try {
      // 指定PKCS12的密码(商户ID)
      keyStore.load(instream, clientId.toCharArray());
    } finally {
      instream.close();
    }

    // Trust own CA and all self-signed certs
	SSLContext sslcontext =
        SSLContexts.custom().loadKeyMaterial(keyStore, clientId.toCharArray()).build();
    // 指定TLS版本
    SSLConnectionSocketFactory sslsf =
        new SSLConnectionSocketFactory(sslcontext, new String[] {"TLSv1"}, null,
            SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    // 设置httpclient的SSLSocketFactory
    CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
    try {
      HttpUriRequest post =
          RequestBuilder.post().setUri(new URI(url)).setEntity(new StringEntity(params)).build();

      CloseableHttpResponse response = httpclient.execute(post);
      HttpEntity entity = response.getEntity();
      String content = IOUtils.toString(entity.getContent());
      EntityUtils.consume(entity);

      logger.info(String.format("url[%s],clientId[%s], content[%s]", url, clientId, content));
      return content;
    } finally {
      httpclient.close();
    }
  }
}
