package com.ewing.order.ball;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.CRC32;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aliyuncs.utils.StringUtils;
import com.ewing.order.ball.bill.DailyBillResp;
import com.ewing.order.ball.login.LoginResp;
import com.ewing.order.ball.util.RequestTool;
import com.ewing.order.busi.ball.ddl.BetAutoBuy;
import com.ewing.order.busi.ball.ddl.BetBill;
import com.ewing.order.busi.ball.ddl.BwContinue;
import com.ewing.order.busi.ball.service.BetAutoBuyService;
import com.ewing.order.busi.ball.service.BetBillService;
import com.ewing.order.busi.ball.service.BwContinueService;
import com.ewing.order.common.contant.IsEff;
import com.ewing.order.common.prop.BallmatchProp;
import com.ewing.order.util.BeanCopy;
import com.ewing.order.util.DataFormat;
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
	private BetBillService betBillService;
	@Resource
	private BwContinueService bwContinueService;
	@Resource
	private BallMember ballMember;
	private long crc32RuleValue = 0l;

	private static Map<String, LoginResp> loginCache = Maps.newConcurrentMap();

	private static Map<String, String> loginPwdCache = Maps.newConcurrentMap();
	static int i = 0;

	/**
	 * 保存登录密码
	 */
	public void updateLoginPwdCache(String account, String pwd) {
		loginPwdCache.put(account, pwd);
	}

	/**
	 * 获取登录密码
	 */
	public String getPwd4Cache(String account) {
		return loginPwdCache.get(account);
	}

	@Scheduled(cron = "*/10 * * * * * ")
	public void init() {
		if (!BallmatchProp.allowrunautobet)
			return;
		List<BetAutoBuy> list = betAutoBuyService.findAll();
		if (list == null)
			return;
		for (BetAutoBuy betAutoBuy : list) {
			if (betAutoBuy.getIseff().equals(IsEff.EFFECTIVE)
					&& betAutoBuy.getIsallow().equals(IsEff.EFFECTIVE)) {
				if (getLoginResp(betAutoBuy.getAccount()) == null) {
					log.info("start autoBuy for " + betAutoBuy.getAccount());
					start(betAutoBuy.getAccount(), betAutoBuy.getPwd());
				}
			} else {
				stop(betAutoBuy.getAccount());
			}
		}
	}
	
	@Scheduled(cron = "*/10 * * * * * ")
	public void checkBwContinue() {
		if (!BallmatchProp.allowrunautobet)
			return;
		List<BwContinue> list = bwContinueService.findAllRunning();
		if (CollectionUtils.isEmpty(list))
			return;
		for (BwContinue bwContinue : list) {
			bwContinueService.updateByGameResult(bwContinue);
		}
	}

	@Scheduled(cron = "0 0/5 * * * * ")
	public void updateBill() {
		if (!BallmatchProp.allowrunautobet)
			return; 
		/*while (loginCache.isEmpty()) {
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
		List<BetAutoBuy> list = betAutoBuyService.findAll();
		if (list == null)
			return;
		for (BetAutoBuy betAutoBuy : list) {
			if (betAutoBuy.getIseff().equals(IsEff.EFFECTIVE)
					&& betAutoBuy.getIsallow().equals(IsEff.EFFECTIVE)) {
				LoginResp loginResp = getLoginResp(betAutoBuy.getAccount());
				if (loginResp != null) {
					updateAccountBill(betAutoBuy.getAccount(), loginResp.getUid());
				}
			}
		}
	}

	public void updateAccountBill(String account, String uid) {
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < 10; i++) {
			String date = DataFormat.DateToString(cal.getTime(), DataFormat.DATE_FORMAT);
			try {
				DailyBillResp dailyBillResp = RequestTool.getDailyBill(uid, date);
				if (dailyBillResp != null
						&& CollectionUtils.isNotEmpty(dailyBillResp.getWagers())) {
					List<BetBill> betBillList = BeanCopy.copy(dailyBillResp.getWagers(),
							BetBill.class);
					betBillService.saveBill(account, date, betBillList);
				}
			} catch (Exception e) {
				log.error("获取历史账单出错,account:" + account + ",日期:" + date);
			}
			cal.add(Calendar.DATE, -1);
		}
	}

	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < 10; i++) {
			String date = DataFormat.DateToString(cal.getTime(), DataFormat.DATE_FORMAT);
			System.out.println(date);
			cal.add(Calendar.DATE, -1);
		}

	}

	public List<BetAutoBuy> hasNewBetAccount() {
		List<BetAutoBuy> list = betAutoBuyService.findAll();
		log.info("hasNewBetAccount:" + list.size());
		if (CollectionUtils.isEmpty(list)) {
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
		if (loginResp != null && !StringUtils.isEmpty(loginResp.getUid())) {
			loginCache.put(account, loginResp);
			updateLoginPwdCache(account, pwd);
			ballMember.addBkListener(true, account, pwd, loginResp.getUid());
			betAutoBuyService.updateLoginIn(account);
		}
	}

	public void stop(String account) {
		if (getLoginResp(account) == null)
			return;
		log.info("stop autoBuy for " + account);
		loginCache.remove(account);
		ballMember.stopBkListener(account);
		betAutoBuyService.updateLoginOut(account);
	}

	public LoginResp getLoginResp(String account) {
		return loginCache.get(account);
	}
}
