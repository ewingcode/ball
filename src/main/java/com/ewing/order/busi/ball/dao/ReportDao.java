package com.ewing.order.busi.ball.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.ewing.order.busi.ball.ddl.BetLogResult;
import com.ewing.order.busi.ball.dto.BetDetailDto;
import com.ewing.order.busi.ball.dto.TotalBillDto;
import com.ewing.order.core.jpa.BaseDao;

/**
 *
 * @author tansonlam
 * @create 2019年1月27日
 */
@Component
public class ReportDao {
	@Resource
	private BaseDao baseDao;

	@Transactional
	public List<TotalBillDto> findTotalWin(String startDate,String endDate,String account) {
		String sql = "SELECT account,COUNT(1) AS matchCount ,SUM(win_gold) AS totalWin FROM `bet_bill` ";
				sql += "WHERE SUBSTR(w_id,3) IN (SELECT ticket_id FROM bet_log WHERE bet_rule_id IS NOT NULL) ";
				if(StringUtils.isNotEmpty(account)){
					sql +=" AND account like '"+account+"%'";
				}
				if(StringUtils.isNotEmpty(startDate))
					sql +=" AND  CONCAT(`date`,' ',`addtime`) >= '" + startDate + "'  ";
				if(StringUtils.isNotEmpty(endDate))
					sql +=" AND  CONCAT(`date`,' ',`addtime`) <= '" + endDate + "'  ";
			    sql +=" GROUP BY account";
		return baseDao.noMappedObjectQuery(sql, TotalBillDto.class);
	}

	public List<TotalBillDto> findTotalWinDetail(String date) {
		String sql = "SELECT * FROM ("
				+ " SELECT account,COUNT(1) AS matchCount ,SUM(win_gold) AS totalWin"
				+ " FROM `bet_bill` WHERE SUBSTR(w_id,3) IN (SELECT ticket_id FROM bet_log WHERE bet_rule_id IS NOT NULL) AND CONCAT(`date`,' ',`addtime`) >= '"
				+ date + "' AND win_gold>0 GROUP BY account" + " UNION ALL "
				+ " SELECT account,COUNT(1) AS matchCount ,SUM(win_gold) AS totalWin"
				+ " FROM `bet_bill` WHERE SUBSTR(w_id,3) IN (SELECT ticket_id FROM bet_log WHERE bet_rule_id IS NOT NULL) AND CONCAT(`date`,' ',`addtime`) >= '"
				+ date + "' AND win_gold<0 GROUP BY account" + " ) a ORDER BY a.account ";
		return baseDao.noMappedObjectQuery(sql, TotalBillDto.class);
	}

	public List<BetDetailDto> findBetDetail(String account, String date) {
		String sql = "SELECT account,match_status as matchStatus, CAST(r.ioratio*r.gold AS DECIMAL(10,1)) as wingold,date_format(r.create_time, '%Y%m%d%H') as createTime,r.gold,r.total,r.type,r.spread,r.result,r.league,r.team_c,r.team_h";
		sql += " FROM bet_log_result r WHERE 1=1 AND CODE=560 ";
		if (StringUtils.isNotEmpty(account)) {
			sql += "and account ='" + account + "'  ";
		}
		sql += "and r.create_time >='" + date + "'  ORDER BY id DESC ";
		return baseDao.noMappedObjectQuery(sql, BetDetailDto.class);
	}

	public List<BetDetailDto> findBetDetailIncludeError(String account, String date) {
		String sql = "SELECT   match_status as matchStatus,CAST(r.ioratio*r.gold AS DECIMAL(10,1)) as wingold,date_format(r.create_time, '%Y%m%d%H') as createTime,r.gold,r.total,r.type,r.spread,r.result,r.league,r.team_c,r.team_h";
		sql += " FROM bet_log_result r WHERE account ='" + account + "'  and r.create_time >='"
				+ date + "'  GROUP BY account,gid ORDER BY id DESC ";
		return baseDao.noMappedObjectQuery(sql, BetDetailDto.class);
	}
}
