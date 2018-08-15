package com.ewing.order.busi.demo.action;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ewing.order.busi.demo.dto.DemoUserReq;
import com.ewing.order.busi.demo.dto.DemoUserResp;
import com.ewing.order.core.web.base.BaseRest;
import com.ewing.order.core.web.common.PageBean;
import com.ewing.order.core.web.common.RestResult;
import com.google.common.collect.Lists;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * Demo
 * 
 * @author tanson lam
 * @creation 2017年1月7日
 * 
 */
@RestController
public class DemoRest extends BaseRest {
	// 通用参数
	public interface InputParam {
		public static final String userName = "userName";
	}

	@ApiOperation(value = "[demo]获取用户列表", notes = "")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userName", value = "用户名称", required = true, dataType = "String") })
	@RequestMapping(value = "/busi/demolist.op", method = RequestMethod.POST)
	@ResponseBody
	public RestResult<List<DemoUserResp>> queryUserList() throws Exception {
		DemoUserReq demoUserReq = requestJson2Obj(DemoUserReq.class);
		checkRequired(demoUserReq.getUserName(), InputParam.userName);
		List<DemoUserResp> rList = Lists.newArrayList();
		for (int i = 0; i < 10; i++) {
			rList.add(new DemoUserResp(Long.valueOf(i), "" + i));
		}
		return RestResult.successResult(rList);
	}

	@RequestMapping(value = "/open/getWarehouseByAccount.op", method = RequestMethod.POST)
	@ResponseBody
	public RestResult<List<DemoUserResp>> queryWarehouseList() throws Exception {
		DemoUserReq demoUserReq = requestJson2Obj(DemoUserReq.class);
		checkRequired(demoUserReq.getUserName(), InputParam.userName);
		List<DemoUserResp> rList = Lists.newArrayList();
		for (int i = 0; i < 10; i++) {
			rList.add(new DemoUserResp(Long.valueOf(i), "" + i));
		}
		return RestResult.successResult(rList);
	}

	@RequestMapping(value = "/busi/pageQueryWarehouseArea.op", method = RequestMethod.POST)
	@ResponseBody
	public RestResult<PageBean<DemoUserResp>> pageQueryWarehouseArea() throws Exception {
		DemoUserReq demoUserReq = requestJson2Obj(DemoUserReq.class);
		Integer total = 20;
		List<DemoUserResp> rList = Lists.newArrayList();
		for (int i = 0; i < total; i++) {
			rList.add(new DemoUserResp(Long.valueOf(i), "" + i));
		}

		Integer page = demoUserReq.getPage();
		Integer pageSize = demoUserReq.getPageSize();
		PageBean<DemoUserResp> pageBeanList = PageBean.newPageBean(page, pageSize, total, rList);
		return RestResult.successResult(pageBeanList);
	}

}
