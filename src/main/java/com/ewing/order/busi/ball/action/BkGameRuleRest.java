package com.ewing.order.busi.ball.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewing.order.ball.BetCollector;
import com.ewing.order.ball.dto.BetInfoDto;
import com.ewing.order.busi.ball.ddl.BetInfo;
import com.ewing.order.busi.ball.ddl.BetRule;
import com.ewing.order.busi.ball.service.BetInfoService;
import com.ewing.order.busi.ball.service.BetRuleService;
import com.ewing.order.common.contant.IsEff;
import com.ewing.order.core.web.base.BaseRest;
import com.ewing.order.core.web.common.RequestJson;
import com.ewing.order.core.web.common.RestResult;
import com.ewing.order.util.GsonUtil;
import com.google.common.collect.Maps;

/**
 *
 * @author tansonlam
 * @create 2018年8月20日
 */
public class BkGameRuleRest extends BaseRest {
	@Resource
	private BetRuleService betRuleService;
	@Resource
	private BetInfoService betInfoService;

	/**
	 * 让球的挂单
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/ball/addBkRule.op", method = RequestMethod.POST)
	@ResponseBody
	public RestResult<List<BetInfoDto>> addBkReSetRule() throws Exception {
		RequestJson requestJson = getRequestJson();
		String uid = requestJson.getString("uid");
		String gid = requestJson.getString("gid");
		String gtype = requestJson.getString("gtype");
		String ptype = requestJson.getString("ptype");
		String impl_code = requestJson.getString("impl_code");
		String account = requestJson.getString("account");
		String radio_re = requestJson.getString("radio_re");
		String radio_re_compare = requestJson.getString("radio_re_compare");
		String buyside = requestJson.getString("buyside");
		String maxeachmatch = requestJson.getString("maxeachmatch");
		String moneyeachmatch = requestJson.getString("moneyeachmatch");
		String before_se_now = requestJson.getString("before_se_now");
		checkRequired(uid, "uid");
		checkRequired(gid, "gid");
		checkRequired(gtype, "gtype");
		checkRequired(impl_code, "impl_code");
		checkRequired(ptype, "ptype");
		checkRequired(radio_re, "让球");
		checkRequired(radio_re_compare, "让球比较符号");
		checkRequired(buyside, "买入方");
		checkRequired(maxeachmatch, "买入次数");
		checkRequired(moneyeachmatch, "买入金额");
		Map<String, String> paramMap = Maps.newHashMap();
		paramMap.put("RADIO_RE", radio_re);
		paramMap.put("RADIO_RE_COMPARE", radio_re_compare);
		paramMap.put("BUYSIDE", buyside);
		paramMap.put("MAXEACHMATCH", maxeachmatch);
		paramMap.put("MONEYEACHMATCH", moneyeachmatch);
		paramMap.put("BEFORE_SE_NOW", before_se_now);
		String ruleJson = GsonUtil.getGson().toJson(paramMap);
		BetInfo betInfo = betInfoService.findByGameId(gid);
		checkRequired(betInfo, "比赛信息");
		BetRule betRule = new BetRule();
		betRule.setAccount(account);
		betRule.setGid(gid);
		betRule.setGtype(gtype);
		betRule.setPtype(ptype);
		betRule.setImpl_code(impl_code);
		betRule.setIseff(IsEff.EFFECTIVE);
		betRule.setParam(ruleJson);
		betRule.setLevel("3");
		betRule.setName("篮球 " + betInfo.getTeam_h() + " VS " + betInfo.getTeam_c());
		betRule.setTeam_c(betInfo.getTeam_c());
		betRule.setTeam_h(betInfo.getTeam_h());
		betRule.setLeague(betInfo.getLeague());
		betRuleService.addRule(betRule);
		return RestResult.successResult(BetCollector.CollectDataPool.getBkRollList());
	}
}
