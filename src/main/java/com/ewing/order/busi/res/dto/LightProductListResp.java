package com.ewing.order.busi.res.dto;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 轻量级的产品信息，产品列表使用
 * 
 * @author tanson lam
 * @creation 2016年1月25日
 */
public class LightProductListResp implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer categoryId;

	private String categoryName;

	private List<LightProductInfo> productList;

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

	public List<LightProductInfo> getProductList() {
		return productList;
	}

	public void setProductList(List<LightProductInfo> productList) {
		this.productList = productList;
	}

	public void addProduct(LightProductInfo lightProductInfo) { 
		if (productList == null)
			productList = Lists.newArrayList();
		productList.add(lightProductInfo);
	}

}
