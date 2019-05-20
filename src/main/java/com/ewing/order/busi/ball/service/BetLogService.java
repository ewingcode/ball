package com.ewing.order.busi.ball.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.ewing.order.busi.ball.dao.BetLogDao;
import com.ewing.order.busi.ball.dao.ReportDao;
import com.ewing.order.busi.ball.ddl.BetLog;
import com.ewing.order.busi.ball.dto.BetDetailDto;
import com.ewing.order.common.contant.IsEff;
import com.ewing.order.core.jpa.BaseDao;
import com.ewing.order.util.DataFormat;

/**
 *
 * @author tansonlam
 * @create 2018年7月24日
 */
@Component
public class BetLogService {
	@Resource
	private BetLogDao betLogDao;
	@Resource
	private BaseDao baseDao;
	@Resource
	private ReportDao reportDao;
	

	@Transactional(rollbackOn = { Exception.class })
	public void save(String account, BetLog betLog) {
		betLog.setAccount(account);
		betLog.setIs_notify(IsEff.INEFFECTIVE);
		betLogDao.save(betLog);
	}

	public List<BetLog> findSucBet(String account, String gId) {
		return betLogDao.findSucBet(account, gId);
	}

	@Transactional(rollbackOn = { Exception.class })
	public void update2Notify(Integer id) {
		BetLog betLog = baseDao.findOne(id, BetLog.class);
		betLog.setIs_notify(IsEff.EFFECTIVE);
		baseDao.save(betLog);
	}

	public List<BetLog> findNotNofity(String account) {
		return betLogDao.findNotNofity(account);
	}

	public List<BetLog> findEachDay(String account, String gtype) {
		String startTime = DataFormat.DateToString(new Date(), DataFormat.DATE_ZEROTIME_FORMAT);
		String endTime = DataFormat.DateToString(new Date(), DataFormat.DATE_FULLTIME_FORMAT);
		return betLogDao.findEachDay(account, gtype, startTime, endTime);
	}
	@Transactional(rollbackOn = { Exception.class })
	public List<BetDetailDto> findSucBetDetail(String account, String startDate,Integer num) {
		return reportDao.findSucBetDetail(account, startDate, num);
	}
	
}
