package com.ewing.order.ball;

import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ewing.order.ball.event.BetStrategy;
import com.ewing.order.ball.login.LoginResp;
import com.ewing.order.ball.shared.BetRuleStatus;
import com.ewing.order.busi.ball.ddl.BetAutoBuy;
import com.ewing.order.busi.ball.ddl.BetRule;
import com.ewing.order.busi.ball.service.BetAutoBuyService;
import com.ewing.order.common.contant.IsEff;
import com.ewing.order.common.exception.BusiException;
import com.ewing.order.common.prop.BallmatchProp;
import com.ewing.order.util.HttpUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 *
 * @author tansonlam
 * @create 2018年7月20日
 */
@Component
public class BallAutoBet {
	private static Logger log = LoggerFactory.getLogger(HttpUtils.class);
	@Resource
	private BetAutoBuyService betAutoBuyService;
	@Resource
	private BallMember ballMember;
	private long crc32RuleValue = 0l;
	
	private static Map<String,LoginResp> loginCache = Maps.newConcurrentMap();
	
	@Scheduled(cron = "*/10 * * * * * ")
	public void init() {
		if (!BallmatchProp.allowrunautobet)
			return;
		List<BetAutoBuy> list = hasNewBetAccount();
		if(list==null)
			return;
		for (BetAutoBuy betAutoBuy : list) {
			if (betAutoBuy.getIseff().equals(IsEff.EFFECTIVE)) {
				if (betAutoBuy.getIs_login().equals(IsEff.INEFFECTIVE)) {
					log.info("start autoBuy for " + betAutoBuy.getAccount());
					start(betAutoBuy.getAccount(), betAutoBuy.getPwd());
				}
			} else {
				log.info("stop autoBuy for " + betAutoBuy.getAccount());
				stop(betAutoBuy.getAccount());
			}
		}
	}
	public List<BetAutoBuy> hasNewBetAccount() {
		List<BetAutoBuy> list = betAutoBuyService.findAll();
		if (CollectionUtils.isEmpty(list)){ 
			return Lists.newArrayList();
		}
		long tmpcrc32RuleValue = computeCrc32(list); 
		if (crc32RuleValue == 0l || crc32RuleValue != tmpcrc32RuleValue) { 
			crc32RuleValue = tmpcrc32RuleValue;
			return list;
		} else { 
			return null;
		}

	}

	private Long computeCrc32(List<BetAutoBuy> list) {
		CRC32 crc32 = new CRC32();
		long tmpcrc32RuleValue = 0l;
		for (BetAutoBuy betRule : list) { 
			crc32.update(betRule.toString().getBytes());
			tmpcrc32RuleValue += crc32.getValue();
		}
		return tmpcrc32RuleValue;
	}
	
	public void start(String account, String pwd) {
		LoginResp loginResp = ballMember.login(account, pwd);
		if (loginResp != null) {
			loginCache.put(account, loginResp);
			ballMember.addBkListener(true, account, pwd, loginResp.getUid());
			betAutoBuyService.updateLoginIn(account);
		}
	}

	public void stop(String account) { 
		loginCache.remove(account);
		ballMember.stopBkListener(account);
		betAutoBuyService.updateLoginOut(account);
	}
	
	public LoginResp getLoginResp(String account){
		return loginCache.get(account);
	}
}
