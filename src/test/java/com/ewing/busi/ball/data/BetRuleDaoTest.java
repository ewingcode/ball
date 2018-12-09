package com.ewing.busi.ball.data;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import com.ewing.order.Door;
import com.ewing.order.busi.ball.ddl.BetRule;
import com.ewing.order.busi.ball.service.BetRuleService;
import com.ewing.order.util.GsonUtil;

/**
 *
 * @author tansonlam
 * @create 2018年7月24日
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Door.class)
public class BetRuleDaoTest {
	@Resource
	private BetRuleService betRuleService;


	public void testupdate2Ineff() {

		try {
			betRuleService.update2Ineff(3);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test 
	public void testQuery(){
		List<BetRule> ruleList = betRuleService.findRule("tansonLAM38", "1", "BK", "ROLL");
		BetRule betRule = ruleList.get(0);
		HashMap<String,String> paramMap = GsonUtil.getGson().fromJson(betRule.getParam(), HashMap.class);
		String execludeLeague = paramMap.get("EXCLUDE_LEAGUE");
		String league = "欧洲女子篮球杯赛";
		boolean exclude = isNotExcludeLeague(league,execludeLeague);
		System.out.println(exclude);
		
	}
	
	private boolean isNotExcludeLeague(String league,String EXCLUDE_LEAGUE) {
		if (StringUtils.isEmpty(EXCLUDE_LEAGUE))
			return true;
		String[] str = StringUtils.split(EXCLUDE_LEAGUE, ",");
		for (String s : str) {
			if (league.indexOf(s) > -1) {
				return false;
			}
		}
		return true;
	}
}
