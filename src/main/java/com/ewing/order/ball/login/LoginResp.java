package com.ewing.order.ball.login;

import java.io.Serializable;

import com.thoughtworks.xstream.XStream;

/**
 * 登陆返回信息
 * 
 * <pre>
 * <?xml version="1.0" encoding="utf-8"?> 
<serverresponse>
  <status>200</status>
  <msg>100</msg>
  <code_message/>
  <username>cdag6095</username>
  <mid>19706631</mid>
  <uid>r7hd4xnrm19706631l699387</uid>
  <ltype>3</ltype>
  <currency>RMB</currency>
  <odd_f>H,M,I,E</odd_f>
  <domain/>
  <t_link/>
</serverresponse>
 * 
 * 
 * </pre>
 *
 * @author tansonlam
 * @create 2018年7月20日
 */
public class LoginResp implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private String status;
	private String msg;
	private String code_message;
	private String username;
	private String mid;
	private String uid;
	private String ltype;
	private String currency;
	private String odd_f;
	private String domain;
	private String t_link;
	private String account;

	public LoginResp() {

	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode_message() {
		return code_message;
	}

	public void setCode_message(String code_message) {
		this.code_message = code_message;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getLtype() {
		return ltype;
	}

	public void setLtype(String ltype) {
		this.ltype = ltype;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getOdd_f() {
		return odd_f;
	}

	public void setOdd_f(String odd_f) {
		this.odd_f = odd_f;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getT_link() {
		return t_link;
	}

	public void setT_link(String t_link) {
		this.t_link = t_link;
	}

	public static LoginResp fromResp(String xmlStr) {
		XStream xstream = new XStream();
		xstream.alias("serverresponse", LoginResp.class);
		return (LoginResp) xstream.fromXML(xmlStr);
	}

}
