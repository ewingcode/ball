package com.ewing.order.ball.qiutan.ft;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("m")
public class BkTodayM {
	@XStreamImplicit
	@XStreamAlias("h")
	private List<BkTodayH> h; 
	
	public List<BkTodayH> getH() {
		return h;
	}

	
	public void setH(List<BkTodayH> h) {
		this.h = h;
	}
}
