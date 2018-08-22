package com.ewing.order.ball.event;

import java.util.HashMap;
import java.util.List;
import java.util.zip.CRC32;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

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
			betStrategy.initParam(GsonUtil.getGson().fromJson(betRule.getParam(), HashMap.class));
			betStrategy.setLevel(Integer.valueOf(betRule.getLevel()));
			betStrategy.setBetStrategyName(betRule.getName());
			betStrategy.setIseff(betRule.getIseff().equals(IsEff.EFFECTIVE));
			betStrategy.setgId(betRule.getGid());
			betStrategy.setUid(uid);
			betStrategy.setRuleId(betRule.getId());
			list.add(betStrategy);
		}
		return list;
	}

	public List<BetStrategy> hasNewBetStrategy() {
		List<BetRule> ruleList = betRuleSerivce.findRule(account, gtype, ptype);
		if (CollectionUtils.isEmpty(ruleList))
			return null;
		long tmpcrc32RuleValue = computeCrc32(ruleList);
		System.out.println(
				"tmpcrc32RuleValue:" + tmpcrc32RuleValue + ",crc32RuleValue:" + crc32RuleValue);
		if (crc32RuleValue == 0l || crc32RuleValue != tmpcrc32RuleValue) {
			log.info("hasNewBetStrategy for account:" + account);
			crc32RuleValue = tmpcrc32RuleValue;
			return parserRule(ruleList);
		}
		log.info("no betstrategy change for account:" + account);
		return null;

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
