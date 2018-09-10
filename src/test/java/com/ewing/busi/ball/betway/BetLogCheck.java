package com.ewing.busi.ball.betway;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ewing.order.Door;
import com.ewing.order.busi.ball.dao.BetInfoDao;
import com.ewing.order.busi.ball.ddl.BetInfo;
import com.ewing.order.busi.ball.ddl.BetLog;
import com.ewing.order.core.jpa.BaseDao;

/**
 *
 * @author tansonlam
 * @create 2018年9月10日
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Door.class)
public class BetLogCheck {
	private static Logger log = LoggerFactory.getLogger(BetLogCheck.class);
	@Resource
	private BaseDao baseDao;
	@Resource
	private BetInfoDao betInfoDao;

	@Test
	public void checkResult() {

		List<BetLog> entityList = baseDao.find(
				"select * from bet_log where create_time>='2018-09-010' and create_time<='2018-09-11'  ",
				BetLog.class);
		for (BetLog betLog : entityList) {
			BetInfo betInfo = betInfoDao.findByGameId(betLog.getGid());
			 
		}
	}
}
