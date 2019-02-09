package com.ewing.order.busi.ball.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ewing.order.ball.shared.BwContinueAllowBet;
import com.ewing.order.ball.shared.BwContinueStatus;
import com.ewing.order.busi.ball.ddl.BwContinue;
import com.ewing.order.core.jpa.BaseDao;

/**
 *
 * @author tansonlam
 * @create 2018年7月25日
 */
@Component
public class BwContinueDao {
	@Resource
	private BaseDao baseDao;

	public void save(BwContinue bwContinue) {
		baseDao.save(bwContinue);
	}
	
	public int updateContinueMaxMatch(Integer betRuleId,Integer continueMaxMatch){
		return baseDao.executeUpdate("update bw_continue set "
				+ "continue_max_match="+continueMaxMatch 
				+ " where bet_rule_id=" + betRuleId+" and status='"+BwContinueStatus.RUNNING+"'" );
	}
	
	
	public int updateStatus(String status,Integer allowBet,String betDetail,BwContinue oldBwContinue){
		return baseDao.executeUpdate("update bw_continue set "
				+ "status='"+status+"'"
				+ ",bet_detail='"+betDetail+"'"
				+ ",allow_bet='"+allowBet+"'"
				+ " where id=" + oldBwContinue.getId()+" and bet_detail='"+oldBwContinue.getBetDetail()+"'" );
	}
	
	public int update2AllowBet(Integer id,String AllowBet){
		return baseDao.executeUpdate("update bw_continue set "
				+ "allow_bet="+BwContinueAllowBet.ALLOW
				+ " where id=" + id+" and allow_bet="+BwContinueAllowBet.NOTALLOW );
	}

	public BwContinue findRunning(String account, Integer betRuleId) {
		return baseDao.findOne("account='" + account + "' and bet_rule_id='" + betRuleId
				+ "' and status='" + BwContinueStatus.RUNNING + "'", BwContinue.class);
	}
	
	public List<BwContinue> findAllRunning() {
		return baseDao.find(" status='" + BwContinueStatus.RUNNING + "'", BwContinue.class);
	}

	public int addNewMatch(Integer id, String betDetail, Integer lastTotalMatch,Float totalBetMoney) {
		return baseDao.executeUpdate("update bw_continue set "
				+ "bet_detail='" + betDetail+"'"
				+ ",allow_bet=" + BwContinueAllowBet.NOTALLOW 
				+ ",total_match=total_match+1  "
			    + ",total_bet_money="+totalBetMoney
				+ " where id=" + id);
	}

}
