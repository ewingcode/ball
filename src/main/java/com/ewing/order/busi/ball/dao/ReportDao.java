package com.ewing.order.busi.ball.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.ewing.order.busi.ball.ddl.BetLogResult;
import com.ewing.order.busi.ball.dto.BetDetailDto;
import com.ewing.order.busi.ball.dto.BetFullDetailDto;
import com.ewing.order.busi.ball.dto.TotalBillDto;
import com.ewing.order.core.jpa.BaseDao;
import com.ewing.order.util.SqlUtil;

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
				sql += " WHERE SUBSTR(w_id,3) IN (SELECT ticket_id FROM bet_log  WHERE  bet_rule_id IS NOT NULL) ";
				if(StringUtils.isNotEmpty(account)){
					sql +=" AND  account like '"+account+"%'";
				}
				if(StringUtils.isNotEmpty(startDate))
					sql +=" AND  CONCAT(`date`,' ',`addtime`) >= '" + startDate + "'  ";
				if(StringUtils.isNotEmpty(endDate))
					sql +=" AND  CONCAT(`date`,' ',`addtime`) <= '" + endDate + "'  ";
			    sql +=" GROUP BY account";
		return baseDao.noMappedObjectQuery(sql, TotalBillDto.class);
	}
	
	
	@Transactional
	public List<TotalBillDto> findTotalWinByTicketIds(String startDate,String endDate,String account) {
		String sql = "SELECT COUNT(1) AS matchCount ,SUM(win_gold) AS totalWin FROM `bet_bill` ";
				sql += "WHERE SUBSTR(w_id,3) IN (SELECT ticket_id FROM bet_log l WHERE ";
						sql += " bet_rule_id IS NOT NULL and account='"+account+"' ";
				if(StringUtils.isNotEmpty(startDate))
					sql += " and l.create_time >= '" + startDate + "'  "; 
				if(StringUtils.isNotEmpty(endDate))
					sql += " and l.create_time <= '" + endDate + "' ";
				sql+= ")"; 		
				if(StringUtils.isNotEmpty(startDate))
					sql +=" AND  CONCAT(`date`,' ',`addtime`) >= '" + startDate + "'  ";
				if(StringUtils.isNotEmpty(endDate))
					sql +=" AND  CONCAT(`date`,' ',`addtime`) <= '" + endDate + "'  "; 
			//	 sql +=" GROUP BY account";
		return baseDao.noMappedObjectQuery(sql, TotalBillDto.class);
	}
	
	@Transactional
	public List<TotalBillDto> findOneAccountTotalWin(String account ,String startDate) {
		String sql = "SELECT  COUNT(1) AS matchCount ,SUM(win_gold) AS totalWin FROM `bet_bill` ";
				sql += " WHERE SUBSTR(w_id,3) IN (SELECT ticket_id FROM bet_log l WHERE l.bet_rule_id IS NOT NULL ";
				sql += " and code='560' and l.account ='"+account+"' and l.create_time >='"
				+ startDate + "' )";
				if(StringUtils.isNotEmpty(startDate))
					sql +=" AND  CONCAT(`date`,' ',`addtime`) >= '" + startDate + "'  "; 
			   // sql +=" GROUP BY account";
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
	
	@Transactional
	public List<BetFullDetailDto> findFullBetDetail(String account, String date) {
		String sql = "SELECT  date_format(l.create_time, '%Y%m%d%H') as createTime,match_status as matchStatus,CAST(l.ioratio*l.gold AS DECIMAL(10,1)) as wingold,l.total,r.sc_total,r.t_count AS t_count ,r.sc_FT_A AS sc_FT_A,r.sc_FT_H AS sc_FT_H,r.ior_ROUH AS ior_ROUH,r.ior_ROUC AS ior_ROUC,i.sc_FT_A AS end_sc_FT_A,i.sc_FT_H AS end_sc_FT_H,";
		sql += "l.* FROM bet_log_result l ,bet_roll_info r,bet_info  i WHERE l.roll_id=r.id AND l.gid=i.gid   ";
		sql += " and l.CODE='560' ";
		if (StringUtils.isNotEmpty(account)) {
			sql += " and l.account ='" + account + "'  ";
		}
		sql += " and l.create_time >='" + date + "'  ORDER BY id DESC ";
		return baseDao.noMappedObjectQuery(sql, BetFullDetailDto.class);
	}

	public List<BetDetailDto> findBetDetail(String account, String date) {
		String sql = "SELECT account,match_status as matchStatus, CAST(r.ioratio*r.gold AS DECIMAL(10,1)) as wingold,date_format(r.create_time, '%Y%m%d%H') as createTime,r.gold,r.total,r.type,r.spread,r.result,r.n_result,r.league,r.team_c,r.team_h,r.errormsg";
		sql += " FROM bet_log_result r WHERE 1=1 AND CODE='560' ";
		if (StringUtils.isNotEmpty(account)) {
			sql += "and account ='" + account + "'  ";
		}
		sql += " and r.create_time >='" + date + "'  ORDER BY id DESC ";
		return baseDao.noMappedObjectQuery(sql, BetDetailDto.class);
	} 

	public List<BetDetailDto> findBetDetailIncludeError(String account, String date) {
		String sql = "SELECT  match_status as matchStatus,CAST(r.ioratio*r.gold AS DECIMAL(10,1)) as wingold,date_format(r.create_time, '%Y%m%d%H') as createTime,r.gold,r.total,r.type,r.spread,r.result,r.league,r.team_c,r.team_h";
		sql += " FROM bet_log_result r WHERE account ='" + account + "'  and r.create_time >='"
				+ date + "'  GROUP BY account,gid ORDER BY id DESC ";
		return baseDao.noMappedObjectQuery(sql, BetDetailDto.class);
	}
	
	public List<BetDetailDto> findSucBetDetail(String account, String startDate,Integer num) {
		String sql = "SELECT id,account,match_status as matchStatus, CAST(r.ioratio*r.gold AS DECIMAL(10,1)) as wingold,date_format(r.create_time, '%Y%m%d%H') as createTime,r.gold,r.total,r.type,r.spread,r.result,r.n_result,r.league,r.team_c,r.team_h";
		sql += " FROM bet_log_result r WHERE 1=1 AND CODE='560' and errormsg is null";
		if (StringUtils.isNotEmpty(account)) {
			sql += " and account ='" + account + "'  ";
		}
		sql += " and r.create_time >='" + startDate + "'  ORDER BY id DESC limit  "+num;
		return baseDao.noMappedObjectQuery(sql, BetDetailDto.class);
	}
	
	public List<BetDetailDto> findAllBetDetailBeforeLastestId(String account, String startDate,Integer lastestId) {
		String sql = "SELECT id,account,match_status as matchStatus, CAST(r.ioratio*r.gold AS DECIMAL(10,1)) as wingold,date_format(r.create_time, '%Y%m%d%H') as createTime,r.gold,r.total,r.type,r.spread,r.result,r.n_result,r.league,r.team_c,r.team_h";
		sql += " FROM bet_log_result r WHERE 1=1 AND CODE='560' ";
		if(lastestId!=null){
			sql +=" and id < "+lastestId;
		}
		if (StringUtils.isNotEmpty(account)) {
			sql += " and account ='" + account + "'  ";
		}
		sql += " and r.create_time >='" + startDate + "'  ORDER BY id DESC ";
		return baseDao.noMappedObjectQuery(sql, BetDetailDto.class);
	}
	 
	public List<BetDetailDto> findTestBetDetailAfterLastestId(String account, String startDate,Integer lastestId) {
		String sql = "SELECT id,account,match_status as matchStatus, CAST(r.ioratio*r.gold AS DECIMAL(10,1)) as wingold,date_format(r.create_time, '%Y%m%d%H') as createTime,r.gold,r.total,r.type,r.spread,r.result,r.n_result,r.league,r.team_c,r.team_h";
		sql += " FROM bet_log_result r WHERE 1=1 AND CODE='560' and  errormsg ='test bet.'";
		if(lastestId!=null){
			sql +=" and id > "+lastestId;
		}
		if (StringUtils.isNotEmpty(account)) {
			sql += " and account ='" + account + "'  ";
		}
		sql += " and r.create_time >='" + startDate + "'  ORDER BY id DESC ";
		return baseDao.noMappedObjectQuery(sql, BetDetailDto.class);
	}
}
