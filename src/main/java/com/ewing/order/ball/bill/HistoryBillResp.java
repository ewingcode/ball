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
public class HistoryBillResp extends XMLBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String msg;
	private String total_gold;
	private String total_vgold;
	private String total_winloss;
	private String total_winloss_calss;
	@XStreamImplicit
	@XStreamAlias("history")
	private List<HistoryData> history;
	@XStreamImplicit
	@XStreamAlias("time")
	private List<Time> time;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTotal_gold() {
		return total_gold;
	}

	public void setTotal_gold(String total_gold) {
		this.total_gold = total_gold;
	}

	public String getTotal_vgold() {
		return total_vgold;
	}

	public void setTotal_vgold(String total_vgold) {
		this.total_vgold = total_vgold;
	}

	public String getTotal_winloss() {
		return total_winloss;
	}

	public void setTotal_winloss(String total_winloss) {
		this.total_winloss = total_winloss;
	}

	public String getTotal_winloss_calss() {
		return total_winloss_calss;
	}

	public void setTotal_winloss_calss(String total_winloss_calss) {
		this.total_winloss_calss = total_winloss_calss;
	}

	public List<HistoryData> getHistory() {
		return history;
	}

	public void setHistory(List<HistoryData> history) {
		this.history = history;
	}

	public List<Time> getTime() {
		return time;
	}

	public void setTime(List<Time> time) {
		this.time = time;
	}

}
