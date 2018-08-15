package com.ewing.busi.ball.data;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ewing.order.ball.BallMember;
import com.ewing.order.ball.BetCollector; 
import com.ewing.order.Door;

/**
 *
 * @author tansonlam
 * @create 2018年7月25日
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Door.class)
public class BallMemberTest {
	@Resource
	private BallMember ballMember;
	@Resource
	private BetCollector collectBetInfo;

	@Test
	public void testCollect() {

		try {

			ballMember.login("tansonLAM83", "523123ZX");
			//ballMember.addFtCurrentListener();
			collectBetInfo.login("tansonLAM38", "523123ZX");
			//collectBetInfo.startCollectFootballInfo();
			collectBetInfo.startCollectBasketInfo(); 
			TimeUnit.HOURS.sleep(24);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
