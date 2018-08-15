package com.ewing.order.busi.shop.action;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ewing.order.busi.shop.ddl.ShopTable;
import com.ewing.order.busi.shop.dto.ShopTableStatusReq;
import com.ewing.order.busi.shop.dto.ShopTableStatusResp;
import com.ewing.order.busi.shop.service.ShopTableService;
import com.ewing.order.core.web.base.BaseRest;
import com.ewing.order.core.web.common.RestResult;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 餐桌接口
 * 
 * @author tanlam
 * @createDate 2016年1月25日
 */
@RestController
public class ShopTableRest extends BaseRest {
	@Resource
	private ShopTableService shopTableService;

	@ApiOperation(value = "获取餐桌信息", notes = "")
	@ApiImplicitParams({ @ApiImplicitParam(name = "shopId", value = "商铺ID", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "tableId", value = "餐桌ID", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "customerId", value = "客户ID", required = true, dataType = "Integer") })
	@RequestMapping(value = "/table/get.op", method = RequestMethod.POST)
	@ResponseBody
	public RestResult<ShopTableStatusResp> getTable() {
		ShopTableStatusReq req = requestJson2Obj(ShopTableStatusReq.class);
		checkRequired(req.getTableId(), "tableId");
		checkRequired(req.getShopId(), "shopId");
		checkRequired(req.getCustomerId(), "customerId");
		ShopTableStatusResp shopTableStatusResp = new ShopTableStatusResp();
		ShopTable shopTable = shopTableService.findOne(req.getShopId(), req.getTableId());
		if (shopTable != null) {
			shopTableStatusResp.setStatus(shopTable.getStatus());
			shopTableStatusResp.setTableId(shopTable.getId());
			shopTableStatusResp.setTableName(shopTable.getTableName());
		}
		return RestResult.successResult(shopTableStatusResp);
	}

}
