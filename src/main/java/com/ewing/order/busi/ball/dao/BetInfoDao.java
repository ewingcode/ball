package com.ewing.order.busi.ball.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ewing.order.busi.ball.ddl.BetInfo;
import com.ewing.order.common.contant.IsEff;
import com.ewing.order.core.jpa.BaseDao;

/**
 * 系统菜单DAO
 * 
 * @author tanson lam
 * @create 2016年9月6日
 */
@Component
public class BetInfoDao {
	@Resource
	private BaseDao baseDao;

	/**
	 * 获取所有的菜单
	 * 
	 * @return
	 */
	public List<BetInfo> getAll() {
		return baseDao.find(" iseff='" + IsEff.EFFECTIVE + "'  ", BetInfo.class);
	}

	public BetInfo findByGameId(String gameId) {
		return baseDao.findOne(" gid='" + gameId + "'  ", BetInfo.class);
	}

	public void save(BetInfo betInfo) {
		baseDao.save(betInfo);
	}
	
	public void update(BetInfo betInfo){
		baseDao.update(betInfo);
	}

}
