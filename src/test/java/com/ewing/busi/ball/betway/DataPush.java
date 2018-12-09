package com.ewing.busi.ball.betway;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ewing.order.Door;
import com.ewing.order.ball.BallAutoBet;
import com.ewing.order.ball.BetCollector;
import com.ewing.order.ball.BetCollector.CollectDataPool;
import com.ewing.order.ball.dto.BetInfoDto;
import com.ewing.order.ball.util.CalUtil;
import com.ewing.order.busi.ball.ddl.BetInfo;
import com.ewing.order.busi.ball.ddl.BetRollInfo;
import com.ewing.order.core.jpa.BaseDao;
import com.google.common.collect.Maps;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Door.class)
public class DataPush {
	private static Logger log = LoggerFactory.getLogger(CalData.class);
	@Resource
	private BaseDao baseDao;
	@Resource
	private BallAutoBet ballAutoBet;
	DecimalFormat fnum = new DecimalFormat("##0.00");
	DecimalFormat fnum2 = new DecimalFormat("##0.0000");
	DecimalFormat fnum3 = new DecimalFormat("##0.000");

	@Test
	public void pushData() {
		ballAutoBet.start("tansonLAM38", "523123ZX");
		String[] gIds = { "2732844" };
		Map<String, List<BetRollInfo>> rollMaps = Maps.newConcurrentMap();
		Map<String, BetInfoDto> betInfoDtoMaps = Maps.newConcurrentMap();
		for (String gId : gIds) {
			List<BetRollInfo> rollList = baseDao.find("select * from bet_roll_info where gid in ("
					+ gId + ")   and se_now='Q4' order by gId,id ", BetRollInfo.class);

			BetInfo betInfo = baseDao.findOne("select * from bet_info where gid = '" + gId + "' ",
					BetInfo.class);
			BetInfoDto betInfoDto = new BetInfoDto();
			BeanUtils.copyProperties(betInfo, betInfoDto);
			BetCollector.CollectDataPool.bkRollList.add(betInfoDto);
			rollMaps.put(gId, rollList); 
		}
		Integer index = 0;
		while (true) {
			for (String gId : rollMaps.keySet()) {
				List<BetRollInfo> rollInfoList = rollMaps.get(gId);
				if (rollInfoList.size() > index) {
					for(BetInfoDto betInfoDto :BetCollector.CollectDataPool.bkRollList){
						if(betInfoDto.getGid().equals(gId)){
							betInfoDto.setLastUpdate(new Timestamp(System.currentTimeMillis()));
						}
					}
					BetRollInfo betRollInfo = rollInfoList.get(index);
					if (betRollInfo.getId() != null) {

						StringBuffer sb = new StringBuffer();
						sb.append("ID：").append(betRollInfo.getId());
						sb.append(",分数:" + betRollInfo.getRatio_rou_c()).append(",总分：")
								.append(StringUtils.leftPad(betRollInfo.getSc_total(), 3, "  "))
								.append(",").append(betRollInfo.getSe_now())
								.append(" 节剩余（秒）:"
										+ StringUtils.leftPad(betRollInfo.getT_count(), 3, " "))
								.append(",Q4-全场得分:")
								.append(fnum2.format(CalUtil.computeScoreSec4Quartz(betRollInfo)
										- CalUtil.computeScoreSec4Alltime(betRollInfo)))
								.append(" 每节进球（秒）："
										+ fnum2.format(CalUtil.computeScoreSec4Quartz(betRollInfo)))
								.append(" 全场进球（秒）：" + fnum2
										.format(CalUtil.computeScoreSec4Alltime(betRollInfo)))
								.append(" [Q1]：" + betRollInfo.getSc_Q1_total())
								.append(" [Q2]：" + betRollInfo.getSc_Q2_total())
								.append(" [Q3]：" + betRollInfo.getSc_Q3_total())
								.append(" [Q4]：" + betRollInfo.getSc_Q4_total());
						log.info(sb.toString());
						BetCollector.CollectDataPool.bkRollList.get(0)
								.setLastUpdate(new Timestamp(System.currentTimeMillis()));
						CollectDataPool.putRollDetail(betRollInfo);
					}
				}
			}
			index++;
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
