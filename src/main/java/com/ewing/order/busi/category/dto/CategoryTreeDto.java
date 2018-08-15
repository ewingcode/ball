package com.ewing.order.busi.category.dto;

import java.io.Serializable;

/**
 * 资源分类对象
 * 
 * @author tanson lam
 * @creation 2015年5月13日
 */
public class CategoryTreeDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer pId;
	private String name;

	public CategoryTreeDto(Integer id, Integer pId, String name) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
	}

	public CategoryTreeDto() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getpId() {
		return pId;
	}

	public void setpId(Integer pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
