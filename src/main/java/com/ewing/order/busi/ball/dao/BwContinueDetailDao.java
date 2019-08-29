package com.ewing.order.busi.ball.dao;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

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

}
