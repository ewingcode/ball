package com.ewing.order.ball;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ewing.order.ball.login.LoginResp;
import com.ewing.order.busi.ball.ddl.BetAutoBuy;
import com.ewing.order.busi.ball.service.BetAutoBuyService;
import com.ewing.order.common.contant.IsEff;
import com.ewing.order.common.prop.BallmatchProp;
import com.ewing.order.util.HttpUtils;
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

	private static Map<String,LoginResp> loginCache = Maps.newConcurrentMap();
	
	@Scheduled(cron = "*/10 * * * * * ")
	public void init() {
		if (!BallmatchProp.allowautobet)
			return;
		List<BetAutoBuy> list = betAutoBuyService.findAll();
		for (BetAutoBuy betAutoBuy : list) {
			if (betAutoBuy.getIseff().equals(IsEff.EFFECTIVE)) {
				if (betAutoBuy.getIs_login().equals(IsEff.INEFFECTIVE)) {
					log.info("start autoBuy for " + betAutoBuy.getAccount());
					start(betAutoBuy.getAccount(), betAutoBuy.getPwd());
				}
			} else {
				stop(betAutoBuy.getAccount());
			}
		}
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
