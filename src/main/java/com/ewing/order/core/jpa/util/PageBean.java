package com.ewing.order.core.jpa.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageBean<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private static int DEFAULT_PAGE_SIZE = 15;
	private int pageSize = DEFAULT_PAGE_SIZE;
	private Integer page;
	private long totalPageCount;
	private long totalCount;
	private List<T> result;
	private String pageUrl;

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public PageBean(Integer page, Integer pageSize, long total, List<T> data) {
		this.page = page == null ? 1 : page;
		this.totalCount = total;
		this.pageSize = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
		this.result = data;
		this.totalPageCount = calTotalPageCount();
	}

	public static <T> PageBean<T> newPageBean(Integer page, Integer pageSize, long total, List<T> data) {
		return new PageBean<T>(page, pageSize, total, data);
	}

	public PageBean() {
		this.page = 0;
		this.totalPageCount = 0;
		this.pageSize = DEFAULT_PAGE_SIZE;
		this.result = new ArrayList<T>();
	}

	public long getTotalCount() {
		return this.totalCount;
	}

	public long calTotalPageCount() {
		if (this.totalCount % this.pageSize == 0L) {
			return this.totalCount / this.pageSize;
		}
		return this.totalCount / this.pageSize + 1L;
	}

	public long getTotalPageCount() {
		return this.totalPageCount;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public List<T> getResult() {
		return this.result;
	}

	public void setResult(List<T> data) {
		this.result = data;
	}

	public Integer getPage() {
		return page;
	}

	public long getCurrentPageNo() {
		return this.page / this.pageSize + 1L;
	}

	public boolean hasPreviousPage() {
		return getCurrentPageNo() > 1L;
	}

	public boolean hasNextPage() {
		return getCurrentPageNo() <= getTotalPageCount() - 1L;
	}

	protected static int getStartOfPage(int pageNo) {
		return getStartOfPage(pageNo, DEFAULT_PAGE_SIZE);
	}

	public static int getStartOfPage(int pageNo, int pageSize) {
		return (pageNo - 1) * pageSize;
	}

	public static <Entity, Dto> PageBean<Dto> copy2DtoPageBean(PageBean<Entity> entityPageBean, List<Dto> dtoList) {
		return new PageBean<Dto>(entityPageBean.getPage(), entityPageBean.getPageSize(), entityPageBean.getTotalCount(),
				dtoList);
	}
}