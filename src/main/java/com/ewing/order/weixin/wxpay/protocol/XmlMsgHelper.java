package com.ewing.order.weixin.wxpay.protocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ewing.order.util.JsonUtils;
import com.ewing.order.weixin.wxpay.api.unifiedorders.vo.UnifiedOrdersResDto;
import com.ewing.order.weixin.wxpay.vo.XmlFieldAnnotationManager;
import com.ewing.order.weixin.wxpay.vo.entity.FieldInfo;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;


/**
 * 
 * @author Joeson Chan<chenxuegui1234@163.com>
 * @since 2016年1月24日
 *
 */
public class XmlMsgHelper {

  private static Logger logger = LoggerFactory.getLogger(XmlMsgHelper.class);

  static XmlFieldAnnotationManager fieldXmlManager = XmlFieldAnnotationManager.getInstace();

  public static String map2Xml(Map<String, Object> params) {
    return map2Xml(params, "xml");
  }

  /**
   * 将内容转化为我们平台的对象，需要做一个映射，将@XmlField的value的key映射到具体 对象属性的值
   * 
   * @param content <xml><appid>234</appid><mch_id>asdf</mch_id></xml>
   * @param clazz 转化后的对象
   * @return
   * @author Joeson
   * @throws Exception
   */
  public static <T> T toObject(String content, Class<T> clazz) throws Exception {
    if (StringUtils.isEmpty(content) || null == clazz) {
      return null;
    }

    Map<String, String> map = parseXml(content);
    logger.info(JsonUtils.toJson(map));

    T t = null;
    try {
      t = clazz.newInstance();
    } catch (Exception e) {
      throw e;
    }
    for (Entry<String, String> entry : map.entrySet()) {
      FieldInfo fieldInfo = fieldXmlManager.getFieldInfo(clazz.getName(), entry.getKey());
      if(null == fieldInfo){
        continue;
      }
      
      try {
        Field field = clazz.getDeclaredField(fieldInfo.field);
        if (null == field) {
          continue;
        }

        field.setAccessible(true);
        field.set(t, entry.getValue());
      } catch (Exception e) {
        throw e;
      }
    }

    return t;
  }


  /**
   * 根据Map组装xml消息体，值对象仅支持基本数据类型、String、BigInteger、BigDecimal，以及包含元素为上述支持数据类型的Map
   * 
   * @param param
   * @param rootElement
   * @return
   * @author Joeson
   */
  public static String map2Xml(Map<String, Object> param, String rootElement) {
    Document doc = DocumentHelper.createDocument();
    Element body = DocumentHelper.createElement(rootElement);
    doc.add(body);
    buildXmlBody(body, param);
    String xml = doc.asXML();
    return StringUtils.isNotEmpty(xml)
        ? xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "").trim() : StringUtils.EMPTY;
  }

  @SuppressWarnings("unchecked")
  private static void buildXmlBody(Element body, Map<String, Object> vo) {
    if (vo != null) {
      Iterator<String> it = vo.keySet().iterator();
      while (it.hasNext()) {
        String key = (String) it.next();
        if (StringUtils.isNotEmpty(key)) {
          Object obj = vo.get(key);
          Element element = DocumentHelper.createElement(key);
          if (obj != null) {
            if (obj instanceof java.lang.String) {
              element.setText((String) obj);
            } else {
              if (obj instanceof Character || obj instanceof Boolean || obj instanceof Number
                  || obj instanceof BigInteger || obj instanceof BigDecimal) {
                Attribute attr = DocumentHelper.createAttribute(element, "type",
                    obj.getClass().getCanonicalName());
                element.add(attr);
                element.setText(String.valueOf(obj));
              } else if (obj instanceof Map) {
                Attribute attr = DocumentHelper.createAttribute(element, "type",
                    java.util.Map.class.getCanonicalName());
                element.add(attr);
                buildXmlBody(element, (Map<String, Object>) obj);
              } else {
              }
            }
          }
          body.add(element);
        }
      }
    }
  }

  /**
   * 将xml文本解析成map
   * 
   * @param content xml内容
   * @return
   * @throws ParserConfigurationException
   * @throws SAXException
   * @throws IOException
   * @author Joeson
   * @throws Exception
   */
  public static Map<String, String> parseXml(String content) throws Exception {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db;
    org.w3c.dom.Document doc;
    try {
      db = dbf.newDocumentBuilder();
      doc = db.parse(new ByteArrayInputStream(content.getBytes(Charsets.UTF_8)));
    } catch (Exception e) {
      throw e;
    }
    NodeList nodeList = doc.getChildNodes();

    if (nodeList.getLength() != 1) {
      return Maps.newHashMapWithExpectedSize(0);
    }

    Node xml = nodeList.item(0);
    NodeList paramNodeList = xml.getChildNodes();
    Map<String, String> map = Maps.newHashMap();
    for (int i = 0; i < paramNodeList.getLength(); i++) {
      Node node = paramNodeList.item(i);
      map.put(node.getNodeName(), node.getTextContent());
    }

    return map;
  }

  public static void main(String[] args) throws Exception {
    // Map<String, Object> map = new HashMap<String, Object>();
    // map.put("test1", "value1");
    // map.put("test2", "value2");
    // map.put("test3", "value3");
    //
    // System.out.println(map2Xml(map, "xml"));

    // String content =
    // "<xml><return_code>SUCCESS</return_code><return_msg>OK</return_msg><appid>wx2421b1c4370ec43b</appid><mch_id>10000100</mch_id><nonce_str>IITRi8Iabbblz1Jc</nonce_str><sign>7921E432F65EB8ED0CE9755F0E86D72F</sign><result_code>SUCCESS</result_code><prepay_id>wx201411101639507cbf6ffd8b0779950874</prepay_id><trade_type>JSAPI</trade_type></xml>";
    // Map<String, String> map = parseXml(content);
    //
    // System.out.println();

    // DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    // DocumentBuilder db = dbf.newDocumentBuilder();

    String content =
        "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg>"
            + "<appid><![CDATA[wx9602131681ce7e0d]]></appid>"
            + "<mch_id><![CDATA[1368568402]]></mch_id>"
            + "<device_info><![CDATA[WEB]]></device_info>"
            + "<nonce_str><![CDATA[NenvQqqjTtb02VDq]]></nonce_str>"
            + "<sign><![CDATA[322AE70F0F83F5C2577854FEA1F5B5AF]]></sign>"
            + "<result_code><![CDATA[SUCCESS]]></result_code>"
            + "<prepay_id><![CDATA[wx20160813191047c2671ae9fb0674650622]]></prepay_id>"
            + "<trade_type><![CDATA[JSAPI]]></trade_type>" + "</xml>";
    UnifiedOrdersResDto dto = toObject(content, UnifiedOrdersResDto.class);
    System.out.println(JsonUtils.toJson(dto));
  }
}
