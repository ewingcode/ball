package com.ewing.order.ball.bill;

import java.io.Serializable;
import java.util.List;

import com.ewing.order.ball.shared.XMLBean;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("serverresponse")
public class DailyBillResp extends XMLBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String selDate;
	private String selGtype;
	private String allDateValue;
	private String allDateShow;
	@XStreamImplicit
	@XStreamAlias("wagers")
	private List<Wagers> wagers;

	public List<Wagers> getWagers() {
		return wagers;
	}

	public void setWagers(List<Wagers> wagers) {
		this.wagers = wagers;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSelDate() {
		return selDate;
	}

	public void setSelDate(String selDate) {
		this.selDate = selDate;
	}

	public String getSelGtype() {
		return selGtype;
	}

	public void setSelGtype(String selGtype) {
		this.selGtype = selGtype;
	}

	public String getAllDateValue() {
		return allDateValue;
	}

	public void setAllDateValue(String allDateValue) {
		this.allDateValue = allDateValue;
	}

	public String getAllDateShow() {
		return allDateShow;
	}

	public void setAllDateShow(String allDateShow) {
		this.allDateShow = allDateShow;
	}

}
