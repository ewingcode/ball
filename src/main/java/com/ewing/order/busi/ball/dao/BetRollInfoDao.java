package com.ewing.order.busi.ball.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.ewing.order.busi.ball.ddl.BetRollInfo;
import com.ewing.order.busi.ball.ddl.RollGameCompute;
import com.ewing.order.core.jpa.BaseDao;

/**
 * 系统菜单DAO
 * 
 * @author tanson lam
 * @create 2016年9月6日
 */
@Component
public class BetRollInfoDao {
	@Resource
	private BaseDao baseDao;

	public BetRollInfo findLastInfo(String gameId) {
		return baseDao.findOne(" gid='" + gameId + "' order by create_time desc  ",
				BetRollInfo.class);
	}

	public BetRollInfo findById(Integer id) {
		return baseDao.findOne(" id=" + id, BetRollInfo.class);
	}

	@Transactional
	public RollGameCompute computeMinAndMax(String gameId) {
		String sql = "SELECT MAX( ratio_re_c) AS maxRatioR, " + "MIN( ratio_re_c) AS minRatioR , "
				+ "MAX( ratio_rou_c) AS maxRatioRou," + "MIN( ratio_rou_c) AS minRatioRou   "
				+ "FROM  bet_roll_info  WHERE gid=" + gameId + " AND ratio_rou_c !=0 ";
		List<RollGameCompute> list = baseDao.noMappedObjectQuery(sql, RollGameCompute.class);
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}

	public List<BetRollInfo> find(String gameId) {
		return baseDao.find(" gid='" + gameId + "' ", BetRollInfo.class);
	}

	public void save(BetRollInfo betInfo) {
		baseDao.save(betInfo);
	}

	public void update(BetRollInfo betInfo) {
		baseDao.update(betInfo);
	}

}
