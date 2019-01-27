package com.ewing.order.busi.ball.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ewing.order.busi.ball.dao.ReportDao;
import com.ewing.order.busi.ball.dto.TotalBillDto;
import com.ewing.order.core.web.base.BaseRest;
import com.ewing.order.core.web.common.RestResult;
import com.google.common.collect.Lists;

/**
 *
 * @author tansonlam
 * @create 2018年8月10日
 */
@RestController
public class ReportRest extends BaseRest {

	@Resource
	private ReportDao reportDao;
	@RequestMapping(value = "/report/win.op", method = RequestMethod.GET)
	@ResponseBody
	public RestResult<List<TotalBillDto>> win() throws Exception {
		String date=request.getParameter("date");
		List<TotalBillDto> totalList = reportDao.findTotalWin(date);
		return RestResult.successResult(totalList);
	}
	 
	@RequestMapping(value = "/report/windetail.op", method = RequestMethod.GET)
	@ResponseBody
	public RestResult<List<TotalBillDto>> windetail() throws Exception {
		String date=request.getParameter("date");
		List<TotalBillDto> totalList = reportDao.findTotalWinDetail(date);
		return RestResult.successResult(totalList);
	}
}