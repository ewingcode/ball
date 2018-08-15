package com.ewing.order.busi.demo.dto;

import java.io.Serializable;

import com.ewing.order.core.web.common.RestRequest;

/**
 *
 * @author tanson lam
 * @creation 2017年1月7日
 * 
 */
public class DemoUserReq extends RestRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userName;

	public DemoUserReq() {
		super();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
