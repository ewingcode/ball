package com.ewing.order.busi.demo.dto;

import java.io.Serializable;

/**
 *
 * @author tanson lam
 * @creation 2017年1月7日
 * 
 */
public class DemoUserResp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String userName;

	public DemoUserResp() {
		super();
	}

	public DemoUserResp(Long id, String userName) {
		super();
		this.id = id;
		this.userName = userName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
