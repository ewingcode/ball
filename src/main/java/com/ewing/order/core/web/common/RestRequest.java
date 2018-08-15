package com.ewing.order.core.web.common;

import java.io.Serializable;

/**
 * 
 * @author tansonlam
 * @create 2016年12月30日
 * 
 */
public class RestRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	private final static Integer DEFAULT_PAGE = 1;
	/**
	 * 实体ID
	 */
	private Long id;
	/**
	 * 请求页码
	 */
	private Integer page;
	/**
	 * 请求的每页的数据量
	 */
	private Integer pageSize;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPage() {
		if (page == null || page == 0)
			return DEFAULT_PAGE;
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		if (pageSize == null || pageSize == 0)
			return PageUtil.DEFAULT_PAGE_SIZE;
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
