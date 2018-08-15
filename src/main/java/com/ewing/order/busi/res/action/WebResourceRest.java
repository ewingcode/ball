package com.ewing.order.busi.res.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ewing.order.busi.category.service.CategoryService;
import com.ewing.order.busi.res.dto.CategoryProductInfoReq;
import com.ewing.order.busi.res.dto.CategoryProductInfoResp;
import com.ewing.order.busi.res.dto.LightProductListResp;
import com.ewing.order.busi.res.dto.ProductDetailReq;
import com.ewing.order.busi.res.dto.ProductDetailResp;
import com.ewing.order.busi.res.service.WebResourceService;
import com.ewing.order.core.web.base.BaseRest;
import com.ewing.order.core.web.common.RequestJson;
import com.ewing.order.core.web.common.RestResult;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 商品接口
 * 
 * @author tanlam
 * @createDate 2016年1月25日
 */
@RestController
public class WebResourceRest extends BaseRest {
	@Resource
	private WebResourceService webResourceService;
	@Resource
	private CategoryService categoryService;

	/**
	 * 商铺下商品列表
	 * 
	 * @throws Exception
	 */
	@ApiOperation(value = "商铺下商品列表", notes = "")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "shopId", value = "商铺ID", required = true, dataType = "Integer") })
	@RequestMapping(value = "/resource/queryByShop.op", method = RequestMethod.POST)
	@ResponseBody
	public RestResult<List<LightProductListResp>> queryByShop() throws Exception {
		RequestJson requestJson = getRequestJson();
		Integer shopId = requestJson.getInteger("shopId");
		checkRequired(shopId, "shopId");
		List<LightProductListResp> list = webResourceService.queryByShopId(shopId);
		return RestResult.successResult(list);
	}

	/**
	 * 分类下的商品列表
	 * 
	 * @throws Exception
	 */
	@ApiOperation(value = "获取分类下的商品信息", notes = "")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "shopId", value = "商铺ID", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "categoryId", value = "分类ID", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "pageSize", value = "每页数据大小", required = true, dataType = "Integer") })
	@RequestMapping(value = "/resource/queryByCategory.op", method = RequestMethod.POST)
	@ResponseBody
	public RestResult<List<CategoryProductInfoResp>> queryByCategory() throws Exception {
		CategoryProductInfoReq req = requestJson2Obj(CategoryProductInfoReq.class);
		checkRequired(req.getCategoryId(), "categoryId");
		checkRequired(req.getShopId(), "shopId");
		checkRequired(req.getPage(), "page");
		checkRequired(req.getPageSize(), "pageSize");
		List<CategoryProductInfoResp> list = webResourceService.queryByCategory(req);
		return RestResult.successResult(list);
	}

	/**
	 * 获取商品的详细信息
	 * 
	 * @throws Exception
	 */
	@ApiOperation(value = "获取商品详细信息", notes = "")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "shopId", value = "商铺ID", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "resourceId", value = "商品ID", required = true, dataType = "Integer") })
	@RequestMapping(value = "/resource/detail.op", method = RequestMethod.POST)
	@ResponseBody
	public RestResult<ProductDetailResp> detail() throws Exception {
		ProductDetailReq req = requestJson2Obj(ProductDetailReq.class);
		checkRequired(req.getResourceId(), "resourceId");
		checkRequired(req.getShopId(), "shopId");
		ProductDetailResp productDetailResp = webResourceService.getProductDetail(req.getShopId(),
				req.getResourceId());
		return RestResult.successResult(productDetailResp);
	}

}
