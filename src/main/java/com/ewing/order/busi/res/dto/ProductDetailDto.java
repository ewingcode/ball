package com.ewing.order.busi.res.dto;

import java.util.List;

public class ProductDetailDto implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 产品ID
	 */
	private Integer id; 

	/**
	 * 资源名称
	 */ 
	private String name; 

	/**
	 * 资源分类id，category的主键
	 */

	private Integer categoryId;
	/**
	 * 分类名称
	 */
	private String categoryName;

	/**
	 * 资源标签id，多个标签以","分隔
	 */ 
	private String tagId;

	/**
	 * 简单描述
	 */ 
	private String shortDesc; 
	/**
	 * 详细描述
	 */ 
	private String longDesc;

	/**
	 * 产品成本价
	 */ 
	private Float cost;

	/**
	 * 产品价格
	 */ 
	private Float price;

	/**
	 * 产品单位
	 */ 
	private String unit;

	/**
	 * 产品重量
	 */ 
	private String weight; 

	/**
	 * 库存
	 */
	private Integer stockNum; 
	/**
	 * 是否热门推荐
	 */
	private Integer isHot;
	/**
	 * 是否上架 0:发布中 1:上架 2:下架
	 */
	private Integer isOnline; 
	/**
	 * 商品图片
	 */
	private List<String> images;

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
 
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
 

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public String getLongDesc() {
		return longDesc;
	}

	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}

	public Float getCost() {
		return cost;
	}

	public void setCost(Float cost) {
		this.cost = cost;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}
 

	public Integer getStockNum() {
		return stockNum;
	}

	public void setStockNum(Integer stockNum) {
		this.stockNum = stockNum;
	}
 

	public Integer getIsHot() {
		return isHot;
	}

	public void setIsHot(Integer isHot) {
		this.isHot = isHot;
	}

	public Integer getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(Integer isOnline) {
		this.isOnline = isOnline;
	}
 
}
