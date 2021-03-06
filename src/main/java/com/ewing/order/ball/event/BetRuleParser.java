package com.ewing.order.ball.event;

import java.util.HashMap;
import java.util.List;
import java.util.zip.CRC32;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.ewing.order.ball.shared.BetRuleStatus;
import com.ewing.order.busi.ball.ddl.BetRule;
import com.ewing.order.busi.ball.service.BetRuleService;
import com.ewing.order.common.contant.IsEff;
import com.ewing.order.common.exception.BusiException;
import com.ewing.order.util.GsonUtil;
import com.google.common.collect.Lists;

public class BetRuleParser {
	private static Logger log = LoggerFactory.getLogger(BetRuleParser.class);
	private String account;
	private String uid;
	private BetRuleService betRuleSerivce;
	private long crc32RuleValue = 0l;
	private String gtype;
	private String ptype;

	public BetRuleParser(String account, String gtype, String ptype, String uid,
			BetRuleService betRuleSerivce) {
		this.uid = uid;
		this.account = account;
		this.betRuleSerivce = betRuleSerivce;
		this.gtype = gtype;
		this.ptype = ptype;
	}

	public List<BetStrategy> parserRule(List<BetRule> ruleList) {
		if (CollectionUtils.isEmpty(ruleList))
			return null;

		List<BetStrategy> list = Lists.newArrayList();
		for (BetRule betRule : ruleList) {
			if (StringUtils.isEmpty(betRule.getImpl_code())) {
				throw new BusiException("投注规则器不能为空,ruleId:" + betRule.getId());
			}
			if (StringUtils.isEmpty(betRule.getParam())) {
				throw new BusiException("投注规则器的规则值不能为空,ruleId:" + betRule.getId());
			}
			BetStrategy betStrategy = BetStrategyRegistCenter
					.newBetStrategy(betRule.getImpl_code());
			betStrategy.setParamMap(GsonUtil.getGson().fromJson(betRule.getParam(), HashMap.class));
			betStrategy.setMoney(betRule.getMoney());
			betStrategy.setIsTest(betRule.getIsTest());
			betStrategy.setContinuePlanMoney(betRule.getContinuePlanMoney());
			betStrategy.setWinRule(betRule.getWinRule());
			betStrategy.setIsCover(betRule.getIsCover());
			betStrategy.setContinueMaxMatch(betRule.getContinueMaxMatch()); 
			betStrategy.setMaxEachday(betRule.getMaxEachday()); 
			betStrategy.setLevel(Integer.valueOf(betRule.getLevel()));
			betStrategy.setBetStrategyName(betRule.getName()); 
			betStrategy.setIseff(betRule.getIseff().equals(IsEff.EFFECTIVE));
			betStrategy.setgId(betRule.getGid()); 
			betStrategy.setRuleId(betRule.getId());
			betStrategy.initParam(GsonUtil.getGson().fromJson(betRule.getParam(), HashMap.class));
			list.add(betStrategy);
		}
		return list;
	}

	public List<BetStrategy> hasNewBetStrategy() {
		List<BetRule> ruleList = betRuleSerivce.findRule(account, BetRuleStatus.NOTSUCCESS, gtype,
				ptype);
		if (CollectionUtils.isEmpty(ruleList)){
			//log.info("已经没有投注策略，账号:" + account);
			return Lists.newArrayList();
		}
		long tmpcrc32RuleValue = computeCrc32(ruleList); 
		if (crc32RuleValue == 0l || crc32RuleValue != tmpcrc32RuleValue) {
			log.info("有新的投注策略，账号:" + account);
			crc32RuleValue = tmpcrc32RuleValue;
			return parserRule(ruleList);
		} else { 
			return null;
		}

	}

	private Long computeCrc32(List<BetRule> ruleList) {
		CRC32 crc32 = new CRC32();
		long tmpcrc32RuleValue = 0l;
		for (BetRule betRule : ruleList) {
			if (StringUtils.isEmpty(betRule.getImpl_code())) {
				throw new BusiException("投注规则器不能为空,ruleId:" + betRule.getId());
			}
			if (StringUtils.isEmpty(betRule.getParam())) {
				throw new BusiException("投注规则器的规则值不能为空,ruleId:" + betRule.getId());
			}
			crc32.update(betRule.toString().getBytes());
			tmpcrc32RuleValue += crc32.getValue();
		}
		return tmpcrc32RuleValue;
	}
}
