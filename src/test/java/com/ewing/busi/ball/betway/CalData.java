package com.ewing.busi.ball.betway;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ewing.order.Door;
import com.ewing.order.ball.dto.BetInfoDto;
import com.ewing.order.busi.ball.dao.BetRollInfoDao;
import com.ewing.order.busi.ball.ddl.BetInfo;
import com.ewing.order.busi.ball.ddl.BetRollInfo;
import com.ewing.order.busi.ball.service.BetRollInfoService;
import com.ewing.order.core.jpa.BaseDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

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
	@Resource
	private BetRollInfoService betRollInfoService;
	@Resource
	private BetRollInfoDao betRollInfoDao;
	Integer rollSmall = 0;
	Integer rollBig = 0;
	Integer todaySmall = 0;
	Integer todayBig = 0;
	DecimalFormat fnum = new DecimalFormat("##0.00");
	DecimalFormat fnum2 = new DecimalFormat("##0.0000");
	int avg = 0;
	float scoreEachSec = 0;
	float ratio_rou = 0;
	boolean showRollDetail = true;
	boolean logDetail = false;
	private Map<String, List<BetRollInfo>> rollMap = Maps.newConcurrentMap();

	class BuyWay {
		private float highScoreSec;
		private int minHighScoreTime;
		private int rollSmall_suc;
		private int rollSmall_fail;
		float smallWinMoney;
		float smallBuyMoneyEach = 100f;
		boolean showWinDetail = true;
		List<StringBuffer> smallBuyList = Lists.newArrayList();

		public BuyWay(float highScoreSec, int minHighScoreTime) {
			this.highScoreSec = highScoreSec;
			this.minHighScoreTime = minHighScoreTime;
		}

		public void compute(List<BetInfoDto> betInfoDtoList) {
			for (BetInfoDto betInfo : betInfoDtoList) {
				if (betInfo.getMinRatioRou() == null) {
					continue;
				}
				checkBuy(betInfo);
			}
			printResult();
		}

		public void checkBuy(BetInfoDto betInfo) {
			avg = Math.round(Float.valueOf(betInfo.getRatio_o().substring(1)) / 4);
			scoreEachSec = avg / (600 * 1f);
			BetRollInfo buySmall = null;
			List<BetRollInfo> list = rollMap.get(betInfo.getGid());
			int highScoreTIme = 0;

			for (BetRollInfo betRollInfo : list) {
				if (buySmall == null) {
					float scoreEveryQuartz = computeScoreSec(betRollInfo);

					if (scoreEveryQuartz >= highScoreSec) {
						highScoreTIme++;
					} else {
						highScoreTIme = 0;
					}
					if (highScoreTIme == minHighScoreTime) {
						buySmall = betRollInfo;
						break;
					}
				}
			}
			if (buySmall != null && betInfo.getSc_total() != null
					&& buySmall.getRatio_rou_c() != null) {
				StringBuffer sb = new StringBuffer();
				sb.append(" " + betInfo.getLeague());
				sb.append(" " + betInfo.getTeam_h()).append(" vs ").append(betInfo.getTeam_c())
						.append(" " + betInfo.getDatetime());
				sb.append("买入小").append(",总分:").append(betInfo.getSc_total()).append(",分数:")
						.append(buySmall.getRatio_rou_c()).append(",最大滚球分:")
						.append(betInfo.getMaxRatioRou()).append(",最小滚球分:")
						.append(betInfo.getMinRatioRou()).append(",初盘平均得分率:").append(scoreEachSec)
						.append(",买入时滚球得分率:").append(fnum2.format(computeScoreSec(buySmall)));
				float winMoney = Float.valueOf(buySmall.getIor_ROUH()) * smallBuyMoneyEach;
				if (Float.valueOf(betInfo.getSc_total()) < buySmall.getRatio_rou_c()) {
					sb.append(",结果：赢");
					rollSmall_suc++;
					smallWinMoney += winMoney;
				} else {
					sb.append(",结果：输");
					rollSmall_fail++;
					smallWinMoney -= winMoney;
				}
				smallBuyList.add(sb);
			}
		}

		public void printResult() {
			log.info("滚球买入小，得分率" + highScoreSec + "，场数：" + (rollSmall_suc + rollSmall_fail)
					+ ",出现次数:" + minHighScoreTime);
			log.info("滚球买入小，得分率" + highScoreSec + "，比率："
					+ fnum.format((rollSmall_suc / ((rollSmall_suc + rollSmall_fail) * 1f)) * 100)
					+ "%," + ",赢场次:" + rollSmall_suc + ",输场次" + rollSmall_fail + ",下注:"
					+ (smallBuyMoneyEach * (rollSmall_suc + rollSmall_fail)) + ",金额:"
					+ smallWinMoney);
			if (showWinDetail) {
				for (StringBuffer smallBuy : smallBuyList) {
					log.info(smallBuy.toString());
				}
			}
		}
	}

	@Test
	public void testAllGame() {
		List<BetInfo> entityList = baseDao.find(
				"select * from bet_info where status=1 and gtype='BK' and create_time>='2018-08-19' and create_time<='2018-09-01' "
						+ " and (league not like '%3X3%' and league not like '%美式足球%' and league not like '%篮网球%' and league not like '%测试%')"
						+ " and ratio is not null and ratio_o is not null order by datetime desc ",
				BetInfo.class);
		List<BetInfo> betInfoList = Lists.newArrayList();
		List<BetInfoDto> betInfoDtoList = Lists.newArrayList();
		for (BetInfo betInfo : entityList) {
			if (StringUtils.isEmpty(betInfo.getRatio()) || StringUtils.isEmpty(betInfo.getRatio_o())
					|| StringUtils.isEmpty(betInfo.getSc_total())
					|| betInfo.getSc_total().equals("0")) {
				continue;
			}

			betInfoList.add(betInfo);
		}
		betInfoDtoList = betRollInfoService.fillMaxMinInfo(betInfoList);
		for (BetInfoDto betInfo : betInfoDtoList) {
			if (betInfo.getMinRatioRou() == null) {
				continue;
			}
			printGame(betInfo);
			if (showRollDetail)
				printRollDetail(betInfo);
		}
		for (int j = 10; j <= 10; j++) {
			for (int i = 3; i <= 5; i++) {

				BuyWay buyWay = new BuyWay(j / (100 * 1f), i);
				buyWay.compute(betInfoDtoList);
			}
		}

		Integer total = betInfoList.size();
		log.info("滚球小比率：" + fnum.format((rollSmall / (total * 1f)) * 100) + "%");
		log.info("滚球大比率：" + fnum.format((rollBig / (total * 1f)) * 100) + "%");
		log.info("今日小比率：" + fnum.format((todaySmall / (total * 1f)) * 100) + "%");
		log.info("今日大比率：" + fnum.format((todayBig / (total * 1f)) * 100) + "%");

		log.info("球赛总数" + betInfoList.size());
	}

	public void printRollDetail(BetInfoDto betInfo) {
		List<BetRollInfo> list = betRollInfoDao.find(betInfo.getGid());
		rollMap.put(betInfo.getGid(), list);
		for (BetRollInfo betRollInfo : list) {

			StringBuffer sb = new StringBuffer();
			sb.append(betRollInfo.getRatio_rou_c()).append(" 总分：")
					.append(StringUtils.leftPad(betRollInfo.getSc_total(), 3, "  ")).append(",")
					.append(betRollInfo.getSe_now())
					.append(" 节剩余（秒）:" + StringUtils.leftPad(betRollInfo.getT_count(), 3, " "))
					.append(" 每节进球（秒）：" + fnum2.format(computeScoreSec(betRollInfo)))
					.append(" 全场进球（秒）：" + fnum2.format(computeScoreSec2(betRollInfo)))
					.append(" 期望值:" + fnum.format(expectRatio(betRollInfo)))
					.append(" [Q1]：" + betRollInfo.getSc_Q1_total())
					.append(" [Q2]：" + betRollInfo.getSc_Q2_total())
					.append(" [Q3]：" + betRollInfo.getSc_Q3_total())
					.append(" [Q4]：" + betRollInfo.getSc_Q4_total());
			if (betRollInfo.getRatio_rou_c() != null) {
				if (betRollInfo.getRatio_rou_c().equals(betInfo.getMaxRatioRou())) {
					sb.append("--[Max]");
				} else if (betRollInfo.getRatio_rou_c().equals(betInfo.getMinRatioRou())) {
					sb.append("--[Min]");
				}
			}
			if (logDetail)
				log.info(sb.toString());
		}
	}

	private void printGame(BetInfoDto betInfo) {
		if (betInfo.getRatio_rou_c() == null) {
			betInfo.setRatio_rou_c(Float.valueOf(betInfo.getRatio_o().substring(1)));
		}

		StringBuffer sb = new StringBuffer();
		sb.append(betInfo.getGid());
		sb.append(" " + betInfo.getLeague());
		sb.append(" " + betInfo.getTeam_h()).append(" vs ").append(betInfo.getTeam_c())
				.append(" " + betInfo.getDatetime());
		avg = Math.round(Float.valueOf(betInfo.getRatio_o().substring(1)) / 4);
		scoreEachSec = avg / (600 * 1f);
		ratio_rou = Float.valueOf(betInfo.getRatio_o().substring(1));
		Float Q1float = (Integer.valueOf(betInfo.getSc_Q1_total()) - avg) / (avg * 1f) * 100f;
		Float Q2float = (Integer.valueOf(betInfo.getSc_Q1_total())
				+ Integer.valueOf(betInfo.getSc_Q2_total()) - 2 * avg) / (2 * avg * 1f) * 100f;
		Float Q3float = (Integer.valueOf(betInfo.getSc_Q1_total())
				+ Integer.valueOf(betInfo.getSc_Q2_total())
				+ Integer.valueOf(betInfo.getSc_Q3_total()) - 3 * avg) / (3 * avg * 1f) * 100f;

		todaySmall += betInfo.getRatio_rou_c() > Float.valueOf(betInfo.getSc_total()) ? 1 : 0;
		todayBig += betInfo.getRatio_rou_c() < Float.valueOf(betInfo.getSc_total()) ? 1 : 0;
		rollBig += Float.valueOf(betInfo.getMinRatioRou()) < Float.valueOf(betInfo.getSc_total())
				? 1 : 0;
		rollSmall += Float.valueOf(betInfo.getMaxRatioRou()) > Float.valueOf(betInfo.getSc_total())
				? 1 : 0;
		Float minAndBeginPercent = Math
				.abs((betInfo.getRatio_rou_c() - Float.valueOf(betInfo.getMinRatioRou()))
						/ betInfo.getRatio_rou_c());
		Float maxAndBeginPercent = Math
				.abs((betInfo.getRatio_rou_c() - Float.valueOf(betInfo.getMaxRatioRou()))
						/ betInfo.getRatio_rou_c());

		String ratio_re = betInfo.getStrong().equals("H") ? betInfo.getRatio()
				: "-" + betInfo.getRatio();

		StringBuffer ratio_re_sb = new StringBuffer();
		ratio_re_sb.append(StringUtils.rightPad("让分：" + ratio_re, 11, " "));
		ratio_re_sb
				.append(StringUtils.rightPad(
						"分差： " + (numberPlus(betInfo.getSc_FT_H(), betInfo.getSc_OT_H())
								- numberPlus(betInfo.getSc_FT_A(), betInfo.getSc_OT_A())),
						11, " "));
		ratio_re_sb.append(StringUtils.rightPad("最小：" + betInfo.getMinRatioR(), 16, " "));
		ratio_re_sb.append(StringUtils.rightPad("最大：" + betInfo.getMaxRatioR(), 16, " "));
		ratio_re_sb.append(StringUtils.rightPad("主    ：" + betInfo.getIor_RH(), 14, " "));
		ratio_re_sb.append(StringUtils.rightPad("客    ：" + betInfo.getIor_RC(), 14, " "));

		StringBuffer ratio_rou_sb = new StringBuffer();
		ratio_rou_sb.append(
				StringUtils.rightPad(("大小：" + betInfo.getRatio_o().substring(1)).trim(), 11, " "));
		ratio_rou_sb.append(StringUtils.rightPad(" 总分：" + betInfo.getSc_total(), 11, " "));
		ratio_rou_sb.append(StringUtils.rightPad("最小：" + betInfo.getMinRatioRou() + "("
				+ fnum.format(minAndBeginPercent * 100) + "%)", 16, " "));
		ratio_rou_sb.append(StringUtils.rightPad("最大：" + betInfo.getMaxRatioRou() + "("
				+ fnum.format(maxAndBeginPercent * 100) + "%)", 16, " "));
		ratio_rou_sb.append(StringUtils.rightPad("大    ：" + betInfo.getIor_OUC(), 14, " "));
		ratio_rou_sb.append(StringUtils.rightPad("小    ：" + betInfo.getIor_OUH(), 14, " "));

		StringBuffer common_sb = new StringBuffer();
		common_sb.append(StringUtils
				.rightPad("主场：" + numberPlus(betInfo.getSc_FT_H(), betInfo.getSc_OT_H()), 11, " "));
		common_sb.append(StringUtils
				.rightPad("客场：" + numberPlus(betInfo.getSc_FT_A(), betInfo.getSc_OT_A()), 11, " "));
		StringBuffer quartz_sb = new StringBuffer();
		BetRollInfo Q1betRollInfo = betRollInfoDao.queryLastRollInfoByQuartz(betInfo.getGid(),
				"Q1");
		quartz_sb.append(" 【Q1】" + betInfo.getSc_Q1_total())
				.append(" " + fnum.format(Q1float) + "%").append("(")
				.append(Q1betRollInfo != null && Q1betRollInfo.getRatio_rou_c() != null
						? Q1betRollInfo.getRatio_rou_c() : "")
				.append(")");
		BetRollInfo Q2betRollInfo = betRollInfoDao.queryLastRollInfoByQuartz(betInfo.getGid(),
				"Q2");
		quartz_sb.append(" 【Q2】" + betInfo.getSc_Q2_total())
				.append(" " + fnum.format(Q2float) + "%").append("(")
				.append(Q2betRollInfo != null && Q2betRollInfo.getRatio_rou_c() != null
						? Q2betRollInfo.getRatio_rou_c() : "")
				.append(")");
		BetRollInfo Q3betRollInfo = betRollInfoDao.queryLastRollInfoByQuartz(betInfo.getGid(),
				"Q3");
		quartz_sb.append(" 【Q3】" + betInfo.getSc_Q3_total())
				.append(" " + fnum.format(Q3float) + "%").append("(")
				.append(Q3betRollInfo != null && Q3betRollInfo.getRatio_rou_c() != null
						? Q3betRollInfo.getRatio_rou_c() : "")
				.append(")");
		BetRollInfo Q4betRollInfo = betRollInfoDao.queryLastRollInfoByQuartz(betInfo.getGid(),
				"Q4");
		quartz_sb.append(" 【Q4】" + betInfo.getSc_Q4_total()).append(" ").append("(")
				.append(Q4betRollInfo != null && Q4betRollInfo.getRatio_rou_c() != null
						? Q4betRollInfo.getRatio_rou_c() : "")
				.append(")");
		log.info(sb.toString());
		log.info(ratio_re_sb.toString());
		log.info(ratio_rou_sb.toString());
		log.info(common_sb.toString());
		log.info("每节比赛分数,  每节结束后总分-(平均分*N节)/平均分*N节,  每节最后时间滚球大小球");
		log.info(quartz_sb.toString());
		log.info("平均每节需要：" + avg);
		log.info("平均每秒进球数：" + fnum2.format(scoreEachSec));
		printMinRatioRou("大小球滚球时间节点 最小", betInfo.getGid(), betInfo.getMinRatioRou());
		printMinRatioRou("大小球滚球时间节点 最大", betInfo.getGid(), betInfo.getMaxRatioRou());
		log.info("\n");
	}

	private void printMinRatioRou(String title, String gid, Float ratioRou) {
		List<BetRollInfo> list = betRollInfoDao.find(gid, ratioRou);

		for (BetRollInfo betRollInfo : list) {
			StringBuffer sb = new StringBuffer();
			sb.append(title + " " + betRollInfo.getRatio_rou_c()).append(" 总分：")
					.append(StringUtils.leftPad(betRollInfo.getSc_total(), 3, "  ")).append(",")
					.append(betRollInfo.getSe_now())
					.append(" 进球（秒）：" + fnum2.format(computeScoreSec(betRollInfo)))
					.append(" 节剩余（秒）:" + StringUtils.leftPad(betRollInfo.getT_count(), 3, " "))
					.append(" 期望值:" + fnum.format(expectRatio(betRollInfo)))
					.append(" [Q1]：" + betRollInfo.getSc_Q1_total())
					.append(" [Q2]：" + betRollInfo.getSc_Q2_total())
					.append(" [Q3]：" + betRollInfo.getSc_Q3_total())
					.append(" [Q4]：" + betRollInfo.getSc_Q4_total());
			log.info(sb.toString());
		}
	}

	private Float computeScoreSec(BetRollInfo betRollInfo) {
		Integer costTime = 600 - Integer.valueOf(betRollInfo.getT_count());
		if (betRollInfo.getSe_now().equals("Q1")) {
			return Float.valueOf(betRollInfo.getSc_Q1_total()) / costTime;
		} else if (betRollInfo.getSe_now().equals("Q2")) {
			return Float.valueOf(betRollInfo.getSc_Q2_total()) / costTime;
		} else if (betRollInfo.getSe_now().equals("Q3")) {
			return Float.valueOf(betRollInfo.getSc_Q3_total()) / costTime;
		} else if (betRollInfo.getSe_now().equals("Q4")) {
			return Float.valueOf(betRollInfo.getSc_Q4_total()) / costTime;
		}
		return 0f;
	}

	private Float expectRatio(BetRollInfo betRollInfo) {
		Integer costTime = 0;
		if (betRollInfo.getSe_now().equals("Q1")) {
			costTime = +600;
		} else if (betRollInfo.getSe_now().equals("Q2")) {
			costTime += 1200;
		} else if (betRollInfo.getSe_now().equals("Q3")) {
			costTime += 1800;
		} else if (betRollInfo.getSe_now().equals("Q4")) {
			costTime += 2400;
		}

		costTime = costTime - Integer.valueOf(betRollInfo.getT_count());
		float chabie = Integer.valueOf(betRollInfo.getSc_total()) - scoreEachSec * costTime;
		return ratio_rou + chabie;
	}

	private Float computeScoreSec2(BetRollInfo betRollInfo) {
		Integer costTime = 0;
		if (betRollInfo.getSe_now().equals("Q1")) {
			costTime = +600;
		} else if (betRollInfo.getSe_now().equals("Q2")) {
			costTime += 1200;
		} else if (betRollInfo.getSe_now().equals("Q3")) {
			costTime += 1800;
		} else if (betRollInfo.getSe_now().equals("Q4")) {
			costTime += 2400;
		}
		costTime = costTime - Integer.valueOf(betRollInfo.getT_count());
		return Integer.valueOf(betRollInfo.getSc_total()) / (costTime * 1f);
	}

	private Integer numberPlus(String number1, String number2) {
		return (StringUtils.isEmpty(number1) ? 0 : Integer.valueOf(number1))
				+ (StringUtils.isEmpty(number2) ? 0 : Integer.valueOf(number2));
	}

	private Float subtract(String numbers1, String numbers2) {
		Float num1 = StringUtils.isEmpty(numbers1) ? 0 : Float.valueOf(numbers1);
		Float num2 = StringUtils.isEmpty(numbers2) ? 0 : Float.valueOf(numbers2);
		return Math.abs(num2 - num1);
	}
}
