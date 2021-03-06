package com.ewing.order.ball;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aliyuncs.utils.StringUtils;
import com.ewing.order.ball.bill.DailyBillResp;
import com.ewing.order.ball.event.BallSource;
import com.ewing.order.ball.event.BetInfoListener;
import com.ewing.order.ball.login.LoginResp;
import com.ewing.order.ball.util.CalUtil;
import com.ewing.order.ball.util.RequestTool;
import com.ewing.order.busi.ball.dao.BetRuleDao;
import com.ewing.order.busi.ball.dao.ReportDao;
import com.ewing.order.busi.ball.ddl.BetAutoBuy;
import com.ewing.order.busi.ball.ddl.BetBill;
import com.ewing.order.busi.ball.ddl.BetRule;
import com.ewing.order.busi.ball.ddl.BwContinue;
import com.ewing.order.busi.ball.dto.TotalBillDto;
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
	@Resource
	private ReportDao reportDao;
	@Resource
	private BetRuleDao betRuleDao;

	private long crc32RuleValue = 0l;

	

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
				try {
					if (!ballMember.isActiveAccount(betAutoBuy.getAccount()) ) {
						log.info("start autoBuy for " + betAutoBuy.getAccount());
						start(betAutoBuy.getAccount(), betAutoBuy.getPwd(),betAutoBuy.getBallAccount());
					} 
				} catch (Exception e) {
					log.error(e.getMessage(),e);
				}
			} else {
				stop(betAutoBuy.getAccount());
			}
		}
	}
	
	
	@Scheduled(cron = "0 0/5 * * * * ")
	public void checkAccountHealthy() {
		if (!BallmatchProp.allowrunautobet)
			return;
		List<BetAutoBuy> list = betAutoBuyService.findAll();
		if (list == null)
			return; 
	 
	 
		for (BetAutoBuy betAutoBuy : list) {
			String account =betAutoBuy.getAccount();
			if (betAutoBuy.getIseff().equals(IsEff.EFFECTIVE)
					&& betAutoBuy.getIsallow().equals(IsEff.EFFECTIVE)) {
				try {
					if ( ballMember.isActiveAccount(betAutoBuy.getAccount()) ) { 
						try {
							LoginResp loginResp = BallLoginCache.getLoginResp(betAutoBuy.getAccount());
							if(loginResp ==null || StringUtils.isEmpty(loginResp.getUid())){
								break;
							}
							RequestTool.getLeaguesCount(loginResp.getUid());
						} catch (Exception e) {
							if (e != null && !StringUtils.isEmpty(e.getMessage())
									&& e.getMessage().indexOf(RequestTool.ErrorCode.doubleLogin) > -1) {
								log.error("stop listener for account:" + account, e); 
								stop(account); 
							}
						} 
					} 
				} catch (Exception e) {
					log.error(e.getMessage(),e);
				}
			}  
		}
	}
	
	@Scheduled(cron = "0/30 * * * * * ")
	public void checkAccountBetRule() {
		if (!BallmatchProp.allowrunautobet)
			return;
		Map<String,BetInfoListener> listeners = BallSource.getBKRoll().getAllListener();
		if(listeners.isEmpty()){
			return;
		}
		for(String account : listeners.keySet()){
			BetInfoListener betInfoListener = listeners.get(account);
			betInfoListener.getBetStrategyPool().checkNewBetStrategyConf();
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
		/*
		 * while (loginCache.isEmpty()) { try { TimeUnit.SECONDS.sleep(5); }
		 * catch (InterruptedException e) { e.printStackTrace(); } }
		 */
		List<BetAutoBuy> list = betAutoBuyService.findAll();
		if (list == null)
			return; 
		for (BetAutoBuy betAutoBuy : list) {
			if (betAutoBuy.getIsallow().equals(IsEff.EFFECTIVE)) { 
				LoginResp loginResp = BallLoginCache.getLoginResp(betAutoBuy.getAccount());
				if (loginResp != null) {
					updateAccountBill(betAutoBuy.getBallAccount(), loginResp.getUid());
				}
				 
			}
		}
	}

	/**
	 * 当设置了赢得最大金额和输的最大金额时候，自动停止下注
	 */
	@Scheduled(cron = "0 0/1 * * * * ")
	public void autoStopForGold() {
		if (!BallmatchProp.allowrunautobet)
			return;
		List<BetAutoBuy> list = betAutoBuyService.findAll();
		if (list == null)
			return; 
		Map<String, BetRule> allRuleMap = Maps.newConcurrentMap();
	 
		List<BetRule> allRule = betRuleDao.findAll();
		 
		if (CollectionUtils.isNotEmpty(allRule)) {
			for (BetRule betRule : allRule) {
				allRuleMap.put(betRule.getAccount(), betRule);
			}
		}
		for (BetAutoBuy betAutoBuy : list) {
			try {
				String account = betAutoBuy.getAccount(); 
				BetRule betRule = allRuleMap.get(betAutoBuy.getAccount());
				TotalBillDto totalBillDto = null ;
				List<TotalBillDto> totalWinList = reportDao.findOneAccountTotalWin(account,CalUtil.getStartDayOfWeek() );
				if(CollectionUtils.isNotEmpty(totalWinList)){
					totalBillDto = totalWinList.get(0);
				}
				//恢复由于规则停止的下注账户
				if(betAutoBuy.getIseff().equals(IsEff.INEFFECTIVE) && (betAutoBuy.getStopByrule()!=null && betAutoBuy.getStopByrule().equals(IsEff.EFFECTIVE))){
					if(totalBillDto==null || (totalBillDto.getTotalWin()==null || totalBillDto.getTotalWin() == 0 )){
						betAutoBuyService.recoverByRule(account);
						continue;
					}
				}
				//按照设置规则为账户停止下注
				if (betAutoBuy.getIseff().equals(IsEff.EFFECTIVE)
						&& betAutoBuy.getIsallow().equals(IsEff.EFFECTIVE)) {
					
					if (betRule == null || totalBillDto == null || totalBillDto.getTotalWin()==null) {
						continue;
					}
					if (totalBillDto.getTotalWin() < 0) {
						if (betRule.getStopLosegold() != null && betRule.getStopLosegold() > 0
								&& Math.abs(totalBillDto.getTotalWin())>=betRule.getStopLosegold()) {
							log.info("exceed stoplosegold:"+betRule.getStopLosegold()+",winTotal:"+totalBillDto.getTotalWin()+",start autoBuy for " + betAutoBuy.getAccount());
							betAutoBuyService.stopByRule(account); 
							stop(account);
						} 
					}else if(totalBillDto.getTotalWin() > 0){ 
						if (betRule.getStopWingold() != null && betRule.getStopWingold() > 0
								&& totalBillDto.getTotalWin()>=betRule.getStopWingold()) {
							log.info("exceed stopWingold:"+betRule.getStopWingold()+",winTotal:"+totalBillDto.getTotalWin()+",start autoBuy for " + betAutoBuy.getAccount());
							betAutoBuyService.stopByRule(account);
							stop(account);
						}

					}
				}
			} catch (Exception e) {
			     log.error("停止账户出错"+e.getMessage(),e);
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
				log.error("获取历史账单出错,account:" + account + ",日期:" + date,e);
			}
			cal.add(Calendar.DATE, -1);
		}
	}

	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		String nowDate = DataFormat.DateToString(cal.getTime(), DataFormat.DATE_FORMAT);
		Date endTimeOfWeek = DataFormat.stringToDate(nowDate + " 11:59:59",
				DataFormat.DATETIME_FORMAT);
		System.out.println(endTimeOfWeek);
		int reduceDay = 0;
		if (dayOfWeek == 1 || (dayOfWeek == 2 && cal.getTime().before(endTimeOfWeek))) {
			reduceDay = 5 + cal.get(Calendar.DAY_OF_WEEK);
		} else {
			reduceDay = cal.get(Calendar.DAY_OF_WEEK) - 2;
		}
		System.out.println(Math.negateExact(reduceDay));
		cal.add(Calendar.DATE, Math.negateExact(reduceDay));
		System.out.println(cal.getTime());

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

	public void start(String account, String pwd,String ballAccount) throws Exception { 
		BallLoginCache.account2BallAccountCache(account, ballAccount);
		LoginResp loginResp = BallLoginCache.getLoginResp(account);
		if(loginResp == null){ 
		     log.info("can not start autobet for account:"+account+",ballaccount need to login");
		}
		if (loginResp != null && !StringUtils.isEmpty(loginResp.getUid())) { 
			updateLoginPwdCache(account, pwd);
			ballMember.addBkListener(true, account, loginResp.getUid(),ballAccount);
			betAutoBuyService.updateLoginIn(account);
		} 
	}

	 
	public void stop(String account) { 
		if (BallLoginCache.getLoginResp(account) == null)
			return;
		if(!ballMember.isActiveAccount(account))
			return;
		log.info("stop autoBuy for " + account); 
		ballMember.stopBkListener(account);
		betAutoBuyService.updateLoginOut(account);
	}

	
}
