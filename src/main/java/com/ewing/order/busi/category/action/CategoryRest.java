package com.ewing.order.busi.category.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ewing.order.busi.category.dto.CategoryTreeDto;
import com.ewing.order.busi.category.service.CategoryService;
import com.ewing.order.core.web.base.BaseRest;
import com.ewing.order.core.web.common.RequestJson;
import com.ewing.order.core.web.common.RestResult;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 分类接口
 * 
 * @author tanson lam
 * @creation 2017年1月11日
 * 
 */
@RestController
public class CategoryRest extends BaseRest {
	@Resource
	private CategoryService categoryService;

	public interface InputParameter {
		public final static String SHOPID = "shopId";
	}

	/**
	 * 获取商铺的商品分类
	 * @throws Exception 
	 */
	@ApiOperation(value = "获取商铺的商品分类", notes = "")
	@ApiImplicitParams({ @ApiImplicitParam(name = "shopId", value = "商铺ID", required = true, dataType = "Integer") })
	@RequestMapping(value = "/category/get.op", method = RequestMethod.POST)
	@ResponseBody
	public RestResult<List<CategoryTreeDto>> queryCatagoryTree() throws Exception {
		RequestJson requestJson = getRequestJson();
		Integer shopId = requestJson.getInteger(InputParameter.SHOPID);
		checkRequired(shopId, InputParameter.SHOPID);
		List<CategoryTreeDto> categoryList = categoryService.queryCatagoryTree(shopId);
		return RestResult.successResult(categoryList);
	}

}
