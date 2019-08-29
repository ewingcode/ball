package com.ewing.order.busi.ball.dao;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ewing.order.ball.shared.BwContinueAllowBet;
import com.ewing.order.busi.ball.ddl.BwContinueDetail;
import com.ewing.order.core.jpa.BaseDao;

/**
 *
 * @author tansonlam
 * @create 2018年7月25日
 */
@Component
public class BwContinueDetailDao {
	@Resource
	private BaseDao baseDao;

	public void save(BwContinueDetail bwContinueDetail) {
		baseDao.save(bwContinueDetail);
	}
	
	public void update(BwContinueDetail bwContinueDetail) {
		baseDao.update(bwContinueDetail);
	}

	public int update(Integer bwContinueId, Integer betLogId, Float poolMoney,Float winGold,String status) {
		return baseDao.executeUpdate("update bw_continue_detail set "
				+ "pool_money=" + poolMoney
				+ ",win_gold=" + winGold
				+ ",status='"+status+"'" 
				+ " where bw_continue_id=" + bwContinueId+" and bet_log_id="+betLogId);
	} 
}
