package com.ewing.busi.ball.data;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ewing.order.Door;
import com.ewing.order.ball.BallMember;
import com.ewing.order.ball.BetCollector;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Door.class)
public class BetCollectorTest {
	@Resource
	private BetCollector collectBetInfo;
	@Resource
	private BallMember ballMember;
	@Test
	public void testCollect() {

		try {
			//ballMember.login("tansonLAM38", "523123ZX");
			//ballMember.addFtCurrentListener();
			collectBetInfo.login("tansonLAM83", "523123ZX");
			collectBetInfo.startCollectFootballInfo(); 
			collectBetInfo.startCollectBasketInfo();
			//collectBetInfo.startCollectBasketInfo();
			TimeUnit.HOURS.sleep(24);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
