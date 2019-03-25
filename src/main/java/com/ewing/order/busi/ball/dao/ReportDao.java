package com.ewing.order.busi.ball.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

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
	public  List<TotalBillDto> findTotalWin(String date) {
		String sql="SELECT account,COUNT(1) AS matchCount ,SUM(win_gold) AS totalWin FROM `bet_bill` "
				+ "WHERE SUBSTR(w_id,3) IN (SELECT ticket_id FROM bet_log WHERE bet_rule_id IS NOT NULL) "
				+ "AND  CONCAT(`date`,' ',`addtime`) >= '"+date+"' GROUP BY account";
		return baseDao.noMappedObjectQuery(sql, TotalBillDto.class);
	}
	
	public  List<TotalBillDto> findTotalWinDetail(String date) {
		String sql="SELECT * FROM ("
+" SELECT account,COUNT(1) AS matchCount ,SUM(win_gold) AS totalWin" 
+" FROM `bet_bill` WHERE SUBSTR(w_id,3) IN (SELECT ticket_id FROM bet_log WHERE bet_rule_id IS NOT NULL) AND CONCAT(`date`,' ',`addtime`) >= '"+date+"' AND win_gold>0 GROUP BY account"
+" UNION ALL "
+" SELECT account,COUNT(1) AS matchCount ,SUM(win_gold) AS totalWin" 
+" FROM `bet_bill` WHERE SUBSTR(w_id,3) IN (SELECT ticket_id FROM bet_log WHERE bet_rule_id IS NOT NULL) AND CONCAT(`date`,' ',`addtime`) >= '"+date+"' AND win_gold<0 GROUP BY account"
+" ) a ORDER BY a.account ";
		return baseDao.noMappedObjectQuery(sql, TotalBillDto.class);
	}
}
