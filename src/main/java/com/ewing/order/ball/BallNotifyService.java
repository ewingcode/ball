package com.ewing.order.ball;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aliyuncs.exceptions.ClientException;
import com.ewing.order.busi.ball.ddl.BetAutoBuy;
import com.ewing.order.busi.ball.ddl.BetInfo;
import com.ewing.order.busi.ball.ddl.BetLog;
import com.ewing.order.busi.ball.service.BetAutoBuyService;
import com.ewing.order.busi.ball.service.BetInfoService;
import com.ewing.order.busi.ball.service.BetLogService;
import com.ewing.order.common.prop.BallmatchProp;
import com.ewing.order.common.prop.SmsProp;
import com.ewing.order.util.GsonUtil;
import com.ewing.order.util.HttpUtils;
import com.ewing.order.util.SmsUtil;
import com.google.common.collect.Maps;

@Component
public class BallNotifyService {
	private static Logger log = LoggerFactory.getLogger(BallNotifyService.class);
	@Resource
	private BetLogService betLogService;
	@Resource
	private BetInfoService betInfoService;
	@Resource
	private BetAutoBuyService betAutoBuyService;

	@Scheduled(cron = "*/30 * * * * * ")
	public void init() {
		if (!BallmatchProp.allownotify)
			return;
		try {
			List<BetAutoBuy> accountList = betAutoBuyService.findAll();
			if (accountList == null)
				return;
			for (BetAutoBuy betAutoBuy : accountList) {
				if (StringUtils.isEmpty(betAutoBuy.getPhone())) {
					continue;
				}
				List<BetLog> betList = betLogService.findNotNofity(betAutoBuy.getAccount());
				if (betList == null)
					return;
				for (BetLog betLog : betList) {
					BetInfo betInfo = betInfoService.findByGameId(betLog.getGid());
					if (betInfo == null)
						continue;

					// 发送短信
					String side = betLog.getType().equals("C") ? "大" : "小";
					String league = betInfo.getLeague();
					String host = betInfo.getTeam_h();
					String client = betInfo.getTeam_c();
					Map<String, String> variables = Maps.newHashMap();
					variables.put("side", side);
					variables.put("leauge", league);
					variables.put("host", host);
					variables.put("client", client);
					try {
						log.info("短信通知，通知账号:" + betAutoBuy.getAccount() + ",内容:"
								+ GsonUtil.getGson().toJson(variables));
						SmsUtil.sendSms(SmsProp.ballTipTemplateCode, SmsProp.ballTipSignName,
								betAutoBuy.getPhone(), variables);
						betLogService.update2Notify(betLog.getId());
					} catch (ClientException e) {
						log.error(e.getMessage(), e);
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
