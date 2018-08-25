package com.ewing.busi.ball.betway;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ewing.order.Door;
import com.ewing.order.busi.ball.ddl.BetInfo;
import com.ewing.order.core.jpa.BaseDao;
import com.ewing.order.util.HttpUtils;
import com.google.common.collect.Lists;

/**
 *
 * @author tansonlam
 * @create 2018年8月24日
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Door.class)
public class CalData {
	private static Logger log = LoggerFactory.getLogger(CalData.class);
	@Resource
	private BaseDao baseDao;

	@Test
	public void test() {
		List<BetInfo> entityList = baseDao
				.find("select * from bet_info where gtype='BK' and create_time>='2018-08-19' and create_time<='2018-08-24' "
						+ " and (league not like '%3X3%' and league not like '%美式足球%' and league not like '%篮网球%' and league not like '%测试%')"
						+ " and ratio is not null and ratio_o is not null", BetInfo.class);
		List<BetInfo> betInfoList = Lists.newArrayList();
		for (BetInfo betInfo : entityList) {
			if (StringUtils.isEmpty(betInfo.getRatio())
					|| StringUtils.isEmpty(betInfo.getRatio_o())) {
				continue;
			}
			betInfoList.add(betInfo);
			printGame(betInfo);
		}
		log.info("球赛总数" + betInfoList.size());
	}

	private void printGame(BetInfo betInfo) {
		StringBuffer sb = new StringBuffer();
		sb.append(betInfo.getGid());
		sb.append(" "+betInfo.getLeague());
		sb.append(" "+betInfo.getLeague());
		sb.append(" "+betInfo.getTeam_h()).append(" vs ").append(betInfo.getTeam_c());
		String ratio_re = betInfo.getStrong().equals("H") ? betInfo.getRatio()
				: "-" + betInfo.getRatio();
		sb.append(" 让分： " + ratio_re);
		sb.append(" 大小球： " + betInfo.getRatio_o().substring(1));
		sb.append(" 主场： " + numberPlus(betInfo.getSc_FT_H(),betInfo.getSc_OT_H())); 
		sb.append(" 客场： " + numberPlus(betInfo.getSc_FT_A(),betInfo.getSc_OT_A())); 
		sb.append(" 总分： " + betInfo.getSc_total());
		log.info(sb.toString());
	}

	private Integer numberPlus(String number1, String number2) {
		return (StringUtils.isEmpty(number1) ? 0 : Integer.valueOf(number1))
				+ (StringUtils.isEmpty(number2) ? 0 : Integer.valueOf(number2));

	}
}
