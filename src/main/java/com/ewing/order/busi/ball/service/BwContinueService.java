package com.ewing.order.busi.ball.service;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.ewing.order.ball.shared.BwBetDetail;
import com.ewing.order.ball.shared.BwBetDetailList;
import com.ewing.order.ball.shared.BwContinueAllowBet;
import com.ewing.order.ball.shared.BwContinueStatus;
import com.ewing.order.ball.shared.GameStatus;
import com.ewing.order.busi.ball.dao.BetInfoDao;
import com.ewing.order.busi.ball.dao.BetLogDao;
import com.ewing.order.busi.ball.dao.BetRuleDao;
import com.ewing.order.busi.ball.dao.BwContinueDao;
import com.ewing.order.busi.ball.dao.BwContinueDetailDao;
import com.ewing.order.busi.ball.ddl.BetInfo;
import com.ewing.order.busi.ball.ddl.BetLog;
import com.ewing.order.busi.ball.ddl.BetRule;
import com.ewing.order.busi.ball.ddl.BwContinue;
import com.ewing.order.busi.ball.ddl.BwContinueDetail;
import com.ewing.order.util.GsonUtil;
import com.google.common.collect.Lists;

@Component
public class BwContinueService {
	@Resource
	private BwContinueDao bwContinueDao;
	@Resource
	private BetRuleDao betRuleDao;
	@Resource
	private BetInfoDao betInfoDao;
	@Resource
	private BetLogDao betLogDao;
	@Resource
	private BwContinueDetailDao bwContinueDetailDao;
	
	@Transactional(rollbackOn={Exception.class})
	public int update2Cancel(String account){
		return bwContinueDao.update2Cancel(account);
	}
	
	@Transactional(rollbackOn={Exception.class})
	public void updateByGameResult(BwContinue bwContinue) {
		if (!bwContinue.getStatus().equals(BwContinueStatus.RUNNING))
			return;
		String bwStatus = BwContinueStatus.RUNNING;
		Integer allowBet = BwContinueAllowBet.NOTALLOW;
		Float winGold =0f;
		String betDetailStr = "";
		if (StringUtils.isNotEmpty(bwContinue.getBetDetail())) {
			BwBetDetailList bwBetDetailList = GsonUtil.getGson().fromJson(bwContinue.getBetDetail(),
					BwBetDetailList.class);
			for (BwBetDetail bwBetDetail : bwBetDetailList.getBwDetail()) {
				if (!bwBetDetail.getResult().equals(BwContinueStatus.RUNNING)) {
					continue;
				}
				BetLog betLog = betLogDao.findById(bwBetDetail.getBetLogId());
				BetInfo betInfo = betInfoDao.findByGameId(betLog.getGid());
				String status = BwContinueStatus.RUNNING; 
				if (betInfo.getStatus().equals(GameStatus.OVER)) {
					if (betLog.getType().equals("C") && Float.valueOf(betLog.getSpread()) < Float
							.valueOf(betInfo.getSc_total())) {
						status = BwContinueStatus.SUCCESS;
						winGold += Float.valueOf(betLog.getIoratio())*Float.valueOf(betLog.getGold());
					} else if (betLog.getType().equals("H") && Float
							.valueOf(betLog.getSpread()) > Float.valueOf(betInfo.getSc_total())) {
						status = BwContinueStatus.SUCCESS;
						winGold += Float.valueOf(betLog.getIoratio())*Float.valueOf(betLog.getGold());
					} else {
						status = BwContinueStatus.FAIL;
						winGold -= Float.valueOf(betLog.getGold());
					}
					bwBetDetail.setResult(status);
				}
			}
			betDetailStr = GsonUtil.getGson().toJson(bwBetDetailList);
			BwBetDetail bwBetDetail = bwBetDetailList.getBwDetail()
					.get(bwBetDetailList.getBwDetail().size() - 1);
			if (bwBetDetail.getResult().equals(BwContinueStatus.SUCCESS)) {
				bwStatus = BwContinueStatus.SUCCESS;
				bwContinueDao.updateStatus(winGold,bwStatus, allowBet, betDetailStr, bwContinue);
			} else if (bwBetDetail.getResult().equals(BwContinueStatus.FAIL)) {
				if (bwContinue.getTotalMatch() < bwContinue.getContinueMaxMatch()) {
					allowBet = BwContinueAllowBet.ALLOW;
				} else {
					bwStatus = BwContinueStatus.FAIL;
				}
				if(bwContinue.getAllowBet()==BwContinueAllowBet.ALLOW){
					return;
				}
				bwContinueDao.updateStatus(winGold,bwStatus, allowBet, betDetailStr, bwContinue);
			}
		}
		
	}
	@Transactional(rollbackOn={Exception.class})
	public BwContinue newBwContinue(String account, Integer betRuleId,Integer loseTotal) {
		BetRule betRule = betRuleDao.findById(betRuleId);
		if (betRule != null && betRule.getContinueMaxMatch() != null) {
			StringBuffer lostTotalPlanMoney=new StringBuffer();
			String continuePlanMoney = betRule.getContinuePlanMoney();
			for(int i=0;i<loseTotal;i++){
				lostTotalPlanMoney.append("0").append(",");
			}
			if(lostTotalPlanMoney.length()>0){
				continuePlanMoney =lostTotalPlanMoney.toString()+continuePlanMoney;
			}
			BwContinue bwContinue = new BwContinue();
			bwContinue.setAccount(account);
			bwContinue.setAllowBet(BwContinueAllowBet.ALLOW);
			bwContinue.setBetRuleId(betRuleId);
			bwContinue.setRatePoolMoney(betRule.getRatePoolMoney());
			bwContinue.setRateCurPoolMoney(betRule.getRatePoolMoney());
			bwContinue.setRateStopLosegold(betRule.getRateStopLosegold());
			bwContinue.setRateStopWingold(betRule.getStopWingold());
			bwContinue.setPoolRate(betRule.getPoolRate());
			bwContinue.setContinueMaxMatch(betRule.getContinueMaxMatch());
			if(loseTotal>0){
				bwContinue.setContinueMaxMatch(betRule.getContinueMaxMatch()+loseTotal);
				bwContinue.setContinuePlanMoney(continuePlanMoney);
			}
			bwContinue.setContinueStartLostnum(betRule.getContinueStartLostnum());  
			bwContinue.setStatus(BwContinueStatus.RUNNING);
			bwContinue.setTargetMoney(Float.valueOf(betRule.getMoney()));
			bwContinue.setTotalBetMoney(0f);
			bwContinue.setTotalMatch(0);
			bwContinueDao.save(bwContinue);
			return bwContinue;
		}
		return null;
	}
	 
	public BwContinue findRunning(String account, Integer betRuleId) {
		return bwContinueDao.findRunning(account, betRuleId); 
	}  

	public List<BwContinue> findAllRunning() {
		return bwContinueDao.findAllRunning();
	}
	
	@Transactional(rollbackOn={Exception.class})
	public int addNewMatch(BwContinue lastBwContinue, Integer betLogId, Float betMoney) {
		BetLog betLog = betLogDao.findById(betLogId);
		BwBetDetail bwBetDetail = new BwBetDetail();
		bwBetDetail.setResult(BwContinueStatus.RUNNING);
		bwBetDetail.setBetLogId(betLogId);
		BwBetDetailList bwBetDetailList = null;
		if (StringUtils.isNotEmpty(lastBwContinue.getBetDetail())) {
			bwBetDetailList = GsonUtil.getGson().fromJson(lastBwContinue.getBetDetail(),
					BwBetDetailList.class);
			bwBetDetailList.getBwDetail().add(bwBetDetail);
		} else {
			bwBetDetailList = new BwBetDetailList();
			bwBetDetailList.setBwDetail(Lists.newArrayList(bwBetDetail));
		}
		BwContinueDetail bwContinueDetail = new BwContinueDetail();
		bwContinueDetail.setAccount(lastBwContinue.getAccount());
		bwContinueDetail.setBeforePoolMoney(lastBwContinue.getRateCurPoolMoney()); 
		bwContinueDetail.setBetLogId(betLogId);
		bwContinueDetail.setBetMoney(betMoney);
		bwContinueDetail.setBetRate(Float.valueOf(betLog.getIoratio()));
		bwContinueDetail.setBwContinueId(lastBwContinue.getId());
		bwContinueDetail.setPollRate(lastBwContinue.getPoolRate()); 
		bwContinueDetail.setResult(BwContinueStatus.RUNNING);
		bwContinueDetail.setSeq(lastBwContinue.getTotalMatch());
		bwContinueDetailDao.save(bwContinueDetail);
		return bwContinueDao.addNewMatch(lastBwContinue.getId(),
				GsonUtil.getGson().toJson(bwBetDetailList), lastBwContinue.getTotalMatch(),
				betMoney);
	}
 

	public int update2AllowBet(Integer id, String AllowBet) {
		return bwContinueDao.update2AllowBet(id, AllowBet);
	}

}
