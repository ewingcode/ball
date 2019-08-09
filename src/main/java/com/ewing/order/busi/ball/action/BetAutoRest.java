package com.ewing.order.busi.ball.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ewing.order.ball.BallMember;
import com.ewing.order.ball.BetCollector;
import com.ewing.order.ball.event.strategy.BKRollAutoSideStrategy2;
import com.ewing.order.ball.shared.BetRuleStatus;
import com.ewing.order.ball.shared.GtypeStatus;
import com.ewing.order.ball.shared.PtypeStatus;
import com.ewing.order.busi.ball.ddl.BetAutoBuy;
import com.ewing.order.busi.ball.ddl.BetRule;
import com.ewing.order.busi.ball.dto.BetAutoBuyDto;
import com.ewing.order.busi.ball.service.BetAutoBuyService;
import com.ewing.order.busi.ball.service.BetRulePoolService;
import com.ewing.order.busi.ball.service.BetRuleService;
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
public class BetAutoRest extends BaseRest {
	@Resource
	private BetAutoBuyService betAutoBuyService;
	@Resource
	private BallMember ballMember;
	@Resource
	private BetRuleService betRuleService;
	@Resource
	private BetRulePoolService betRulePoolService;
	@Resource
	private BetCollector betCollector;
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
		String phone = requestJson.getString("phone");
		String money = requestJson.getString("money");
		String iseff = requestJson.getString("iseff");
		String isTest = requestJson.getString("isTest");
		String continueMaxMatch = requestJson.getString("continueMaxMatch");
		String continueStartLostnum = requestJson.getString("continueStartLostnum"); 
		String stopWingold = requestJson.getString("stopWingold"); 
		String stopLosegold = requestJson.getString("stopLosegold");  
		String continuePlanMoney = requestJson.getString("continuePlanMoney"); 
		String ruleName = requestJson.getString("ruleName"); 
		String maxEachDay = requestJson.getString("maxEachDay"); 
		String winRule = requestJson.getString("winRule"); 
		checkRequired(account, "account");
		checkRequired(iseff, "iseff");
		betAutoBuyService.updateIsEff(account, iseff, phone, money,Integer.valueOf(isTest),
				Integer.valueOf(continueMaxMatch),Integer.valueOf(continueStartLostnum),Float.valueOf(stopWingold),Float.valueOf(stopLosegold),continuePlanMoney,ruleName,Integer.valueOf(maxEachDay),winRule);
		return RestResult.successResult(true);
	}
	
	@RequestMapping(value = "/ballauto/active.op", method = RequestMethod.GET)
	@ResponseBody
	public RestResult<Boolean> activeAccount() throws Exception { 
		String account = request.getParameter("account"); 
		String isAllow = request.getParameter("isallow"); 
		checkRequired(account, "account"); 
		checkRequired(isAllow, "isAllow"); 
		betAutoBuyService.activeAccount(account,isAllow);
		return RestResult.successResult(true);
	}
	
	@RequestMapping(value = "/ballauto/status.op", method = RequestMethod.GET)
	@ResponseBody
	public RestResult<Boolean> changeAccountStatus() throws Exception { 
		String account = request.getParameter("account"); 
		String accountPwd = request.getParameter("accountPwd");  
		checkRequired(account, "account"); 
		checkRequired(accountPwd, "accountPwd"); 
		BetAutoBuy betAutoBuy = betAutoBuyService.find(account);
		if(!betAutoBuy.getPwd().equals(accountPwd)){
			return  RestResult.errorResult("密码不正确");
		}
		String action =betAutoBuy.getIseff().equals("1")?"0" :"1";
		betAutoBuyService.changeAccountStatus(account,action);
		return RestResult.successResult(true);
	}
	
	@RequestMapping(value = "/ballauto/param.op", method = RequestMethod.GET)
	@ResponseBody
	public RestResult<Boolean> changeParam() throws Exception { 
		String collectTime = request.getParameter("collectTime"); 
		String maxMatchEachDay = request.getParameter("maxMatchEachDay"); 
		checkRequired(collectTime, "collectTime"); 
		checkRequired(maxMatchEachDay, "maxMatchEachDay"); 
		BetCollector.rollDataCollectTime = Long.valueOf(collectTime);
		betCollector.restartTimer();
		BKRollAutoSideStrategy2.SYSMAXEACHDAY = Integer.valueOf(maxMatchEachDay);
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
		 List<BetRule> ruleList = betRuleService.findRule(account, BetRuleStatus.NOTSUCCESS, GtypeStatus.BK, PtypeStatus.ROLL);
		 List<String> ruleNameList = betRulePoolService.findAllRuleName();
		if (betAutoBuy != null) {
			BetAutoBuyDto dto = new BetAutoBuyDto();
			BeanCopy.copy(dto, betAutoBuy, true);
			dto.setRuleNameList(ruleNameList);
			if(CollectionUtils.isNotEmpty(ruleList)){
				dto.setMoney(ruleList.get(0).getMoney());
				dto.setContinueMaxMatch(ruleList.get(0).getContinueMaxMatch()==null?"0":ruleList.get(0).getContinueMaxMatch().toString());
				dto.setContinueStartLostnum(ruleList.get(0).getContinueStartLostnum()==null?"1":ruleList.get(0).getContinueStartLostnum().toString());
				dto.setContinuePlanMoney(ruleList.get(0).getContinuePlanMoney());
				dto.setStopWingold(ruleList.get(0).getStopWingold()==null?"0":String.valueOf(ruleList.get(0).getStopWingold().intValue()));
				dto.setStopLosegold(ruleList.get(0).getStopLosegold()==null?"0":String.valueOf(ruleList.get(0).getStopLosegold().intValue()));
				dto.setIsTest(ruleList.get(0).getIsTest()==null?"0":ruleList.get(0).getIsTest().toString());
			    dto.setRuleName(ruleList.get(0).getName());  
			    dto.setWinRule(ruleList.get(0).getWinRule());
			    dto.setMaxEachDay(ruleList.get(0).getMaxEachday()==null?"3":ruleList.get(0).getMaxEachday().toString());
			} 
			// 如果不是活跃中的用户则设置为失效用户，让前台可以更新用户状态来激活自动下注
			dto.setIseff(ballMember.isActiveAccount(account) ? IsEff.EFFECTIVE : IsEff.INEFFECTIVE);

			return RestResult.successResult(dto);
		}else{
			BetAutoBuyDto dto = new BetAutoBuyDto();
			dto.setRuleName(ruleNameList.get(0));
			dto.setRuleNameList(betRulePoolService.findAllRuleName());
			return RestResult.successResult(dto);
		}
		
	} 
}
