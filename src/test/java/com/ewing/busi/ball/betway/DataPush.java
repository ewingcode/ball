package com.ewing.busi.ball.betway;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ewing.order.Door;
import com.ewing.order.ball.BetCollector.CollectDataPool;
import com.ewing.order.busi.ball.ddl.BetInfo;
import com.ewing.order.busi.ball.ddl.BetRollInfo;
import com.ewing.order.core.jpa.BaseDao;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Door.class)
public class DataPush {
	private static Logger log = LoggerFactory.getLogger(CalData.class);
	@Resource
	private BaseDao baseDao;

	@Test
	public void pushData() {
		String gId = "2602133";
		List<BetRollInfo> entityList = baseDao.find(
				"select * from bet_roll_info where gid in (" + gId + ") order by gId,id ",
				BetRollInfo.class);
		 
		for(BetRollInfo betRollInfo : entityList){
			
			if(betRollInfo.getId()!=null){
				log.info("putRollDetail:"+betRollInfo.getId());
				CollectDataPool.putRollDetail(betRollInfo);
			}
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) { 
				e.printStackTrace();
			}
		}
		try {
			TimeUnit.SECONDS.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
