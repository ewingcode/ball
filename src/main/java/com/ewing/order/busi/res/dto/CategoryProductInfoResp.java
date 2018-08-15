package com.ewing.order.busi.res.dto;

/**
 * 轻量级的产品信息，产品列表使用
 * 
 * @author tanson lam
 * @creation 2016年1月25日
 */
public class CategoryProductInfoResp implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 产品ID
	 */
	private Integer id;
	/**
	 * 产品名称
	 */
	private String name;
	/**
	 * 分类ID
	 */
	private Integer categoryId;
	/**
	 * 分类名称
	 */
	private String categoryName;
	/**
	 * 产品简单描述
	 */
	private String shortDesc;
	/**
	 * 成本
	 */
	private Float cost;
	/**
	 * 价格
	 */
	private Float price;
	/**
	 * 图片URL
	 */
	private String image;

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Float getCost() {
		return cost;
	}

	public void setCost(Float cost) {
		this.cost = cost;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}
}
