package com.ewing.order.ball.bill;

import java.io.Serializable;
import java.util.List;

import com.ewing.order.ball.shared.XMLBean;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 *
 * @author tansonlam
 * @create 2018年8月13日
 */
@XStreamAlias("serverresponse")
public class TodayBillResp extends XMLBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String amout_gold;
	private Integer count;
	@XStreamImplicit
	@XStreamAlias("wagers")
	private List<Wagers> wagers;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAmout_gold() {
		return amout_gold;
	}

	public void setAmout_gold(String amout_gold) {
		this.amout_gold = amout_gold;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<Wagers> getWagers() {
		return wagers;
	}

	public void setWagers(List<Wagers> wagers) {
		this.wagers = wagers;
	}

}
