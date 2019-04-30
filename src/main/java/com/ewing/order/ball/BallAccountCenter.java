package com.ewing.order.ball;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ewing.order.ball.dto.BetInfoDto;
import com.ewing.order.ball.event.BallSource;
import com.ewing.order.ball.event.BetInfoListener;
import com.ewing.order.ball.event.BetStrategyPool;
import com.ewing.order.ball.event.WrapDataCallBack;
import com.ewing.order.ball.login.LoginResp;
import com.ewing.order.ball.shared.GtypeStatus;
import com.ewing.order.ball.shared.PtypeStatus;
import com.ewing.order.ball.util.RequestTool;
import com.ewing.order.busi.ball.ddl.BetAutoBuy;
import com.ewing.order.busi.ball.service.BetAutoBuyService;
import com.ewing.order.busi.ball.service.BetBillService;
import com.ewing.order.busi.ball.service.BetLogService;
import com.ewing.order.busi.ball.service.BetRuleService;
import com.ewing.order.busi.ball.service.BwContinueService;
import com.ewing.order.common.contant.IsEff;
import com.ewing.order.common.prop.BallmatchProp;
import com.ewing.order.util.HttpUtils;
import com.google.common.collect.Maps;

/**
 *
 * @author tansonlam
 * @create 2019年4月30日 
 */
@Component
public class BallAccountCenter {
	private static Logger log = LoggerFactory.getLogger(BallAccountCenter.class);
 
	@Resource
	private BetAutoBuyService betAutoBuyService; 
	@Resource
	private BallAutoBet ballAutoBet; 
 

	@Scheduled(cron = "*/20 * * * * * ")
	public void init() {
		if (!BallmatchProp.allowrunautobet)
			return;
		List<BetAutoBuy> list = betAutoBuyService.findAll();
		if (list == null)
			return; 
	 
		Map<String,String> loginMap = Maps.newConcurrentMap();
		for (BetAutoBuy betAutoBuy : list) {
			if (betAutoBuy.getIseff().equals(IsEff.EFFECTIVE)
					&& betAutoBuy.getIsallow().equals(IsEff.EFFECTIVE)) {
				loginMap.put(betAutoBuy.getBallAccount(), betAutoBuy.getPwd());
			}  
		}
		
		for(String ballAccount : loginMap.keySet()){
			try {
				String pwd = loginMap.get(ballAccount);
				Boolean doubleLogin = false;
				LoginResp loginResp = BallLoginCache.getLoginRespByBallAccount(ballAccount);
				if(loginResp!=null){
					try { 
						RequestTool.getLeaguesCount(loginResp.getUid());
					} catch (Exception e) {
						if (e != null && !StringUtils.isEmpty(e.getMessage())
								&& e.getMessage().indexOf(RequestTool.ErrorCode.doubleLogin) > -1) {
							log.error("double login account:" + ballAccount, e); 
							doubleLogin = true;
						}
					}
				}
				if(doubleLogin || loginResp ==null){
					  login(ballAccount,pwd);
				}
			} catch (Exception e) {
				log.error("error for "+ballAccount+" login.",e);
			}
		}
	}
	public void login(String account, String pwd) throws Exception {
		LoginResp loginResp = RequestTool.login(account, pwd);
		if (loginResp != null && !StringUtils.isEmpty(loginResp.getUid())) {
			BallLoginCache.cacheLoginResp(account, loginResp);
		}
		log.info("login successfully for ballaccount:"+account);
		 
	} 
}
