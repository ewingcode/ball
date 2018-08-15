package com.ewing.order.ball.shared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewing.order.util.GsonUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 *
 * @author tansonlam
 * @create 2018年7月21日
 */
public class XMLBean {
	private static Logger log = LoggerFactory.getLogger(XMLBean.class);

	@SuppressWarnings("unchecked")
	public <T> T fromResp(String xmlStr) {
		try {
			XStream xstream = new XStream(new DomDriver("UTF-8"));
			xstream.processAnnotations(this.getClass());
			return (T) xstream.fromXML(xmlStr);
		} catch (Exception e) {
			String errmsg = "fail to parse:" + xmlStr;
			log.error(errmsg);
			log.error(e.getMessage(), e);
			return null;
		}
	}

	public String toString() {
		return GsonUtil.getGson().toJson(this);
	}
}
