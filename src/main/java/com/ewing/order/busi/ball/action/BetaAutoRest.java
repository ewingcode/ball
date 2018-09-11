package com.ewing.order.busi.ball.action;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ewing.order.ball.BallMember;
import com.ewing.order.busi.ball.ddl.BetAutoBuy;
import com.ewing.order.busi.ball.dto.BetAutoBuyDto;
import com.ewing.order.busi.ball.service.BetAutoBuyService;
import com.ewing.order.common.contant.IsEff;
import com.ewing.order.core.web.base.BaseRest;
import com.ewing.order.core.web.common.RequestJson;
import com.ewing.order.core.web.common.RestResult;
import com.ewing.order.util.BeanCopy;

/**
 *
 * @author tansonlam
 * @create 2018年8月10日
 */
@RestController
public class BetaAutoRest extends BaseRest {
	@Resource
	private BetAutoBuyService betAutoBuyService;
	@Resource
	private BallMember ballMember;

	/**
	 * 更新自動投注賬戶狀態
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/ballauto/updateStatus.op", method = RequestMethod.POST)
	@ResponseBody
	public RestResult<Boolean> updateStatus() throws Exception {
		RequestJson requestJson = getRequestJson();
		String account = requestJson.getString("account");
		String iseff = requestJson.getString("iseff");
		checkRequired(account, "account");
		checkRequired(iseff, "iseff");
		betAutoBuyService.updateIsEff(account, iseff);
		return RestResult.successResult(true);
	}

	/**
	 * 查看自动投注账户的状态
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/ballauto/findAccount.op", method = RequestMethod.POST)
	@ResponseBody
	public RestResult<BetAutoBuyDto> findAccount() throws Exception {
		RequestJson requestJson = getRequestJson();
		String account = requestJson.getString("account");
		checkRequired(account, "account");
		BetAutoBuy betAutoBuy = betAutoBuyService.find(account);
		if (betAutoBuy != null) {
			BetAutoBuyDto dto = new BetAutoBuyDto();
			BeanCopy.copy(dto, betAutoBuy, true);
			// 如果不是活跃中的用户则设置为失效用户，让前台可以更新用户状态来激活自动下注
			dto.setIseff(ballMember.isActiveAccount(account) ? IsEff.EFFECTIVE : IsEff.INEFFECTIVE);

			return RestResult.successResult(dto);
		}
		return RestResult.successResult(null);
	}

}
