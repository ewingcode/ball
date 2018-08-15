package com.ewing.order.busi.res.dto;

/**
 * 产品详情
 * 
 * @author tansonlam
 * @createDate 2016年1月26日
 * 
 */
public class ProductDetailResp implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 产品详情
	 */
	private ProductDetailDto productDetail;

	public ProductDetailDto getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(ProductDetailDto productDetail) {
		this.productDetail = productDetail;
	}

}
