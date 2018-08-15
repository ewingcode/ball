package com.ewing.order.core.jpa.util;

import javax.persistence.Column;

/**
 * @author tanson lam 2014年9月2日
 * 
 */
public class Count {
	@Column(name = "count")
	private Integer count;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
