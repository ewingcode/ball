package com.ewing.order.ball.qiutan.ft;

import java.io.Serializable;

import com.ewing.order.ball.shared.XMLBean;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("c")
public class BkTodayResp extends XMLBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XStreamAlias("m")
	private BkTodayM m;
	@XStreamAlias("s")
	private String s;

	public String getS() {
		return s;
	}

	public void setS(String s) {
		this.s = s;
	}

	public BkTodayM getM() {
		return m;
	}

	public void setM(BkTodayM m) {
		this.m = m;
	}

}
