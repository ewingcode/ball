package com.ewing.busi.ball.betway;

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
import com.ewing.order.ball.util.CalUtil;
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
	DecimalFormat fnum3 = new DecimalFormat("##0.000");
	int avg = 0;
	float scoreEachSec = 0;
	float ratio_rou = 0;
	boolean showRollDetail = false;
	boolean logDetail = true;
	private Map<String, List<BetRollInfo>> rollMap = Maps.newConcurrentMap();

	@Test
	public void testAllGame() {
		List<BetInfo> entityList = baseDao
				.find("select * from bet_info where status=1 and gtype='BK' and create_time>='2018-09-01' and create_time<='2018-09-13' "
						+ " and (league not like '%3X3%' and league not like '%美式足球%' and league not like '%篮网球%' and league not like '%测试%')"
						+ " and ratio is not null and ratio_o is not null "
						// + " and gid in ('2600579')"
						+ " order by datetime desc ", BetInfo.class);
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
			if (showRollDetail) {
				printGame(betInfo);
				printRollDetail(betInfo);
			} else {
				log.info("查询滚球信息"+betInfo.getGid());
				List<BetRollInfo> list = betRollInfoDao.find(betInfo.getGid());
				rollMap.put(betInfo.getGid(), list);
			}

		}
		Float maxWinMoney = 0f;
		BuyWay2 bestBuyWay2 = null;
		for (int j = 20; j <= 30; j += 5) {
			for (int i = 5; i <= 10; i++) {
				for (int z = 30; z <= 90; z += 20) {
					BuyWay2 buyWay = new BuyWay2(j / (1000 * 1f), i, z, "AUTO", "Q4", true, 0.8f);
					buyWay.compute(betInfoDtoList);
					// if (buyWay.sucRate() > 0.7f) {
					buyWay.printResult();
					// }

					if (bestBuyWay2 == null || buyWay.smallWinMoney > bestBuyWay2.smallWinMoney)
						bestBuyWay2 = buyWay;

				}

			}
		}
		log.info("最赚钱的方案=====");
		bestBuyWay2.printResult();
		/*
		 * for (int j = 20; j <= 30; j += 5) { for (int i = 20; i <= 140; i +=
		 * 30) { BuyWay2 buyWay = new BuyWay2(j / (1000 * 1f), null, i, "AUTO",
		 * "Q4", false, 0.8f); buyWay.compute(betInfoDtoList);
		 * 
		 * } }
		 */

		Integer total = betInfoList.size();
		log.info("滚球小比率：" + fnum.format((rollSmall / (total * 1f)) * 100) + "%");
		log.info("滚球大比率：" + fnum.format((rollBig / (total * 1f)) * 100) + "%");
		log.info("今日小比率：" + fnum.format((todaySmall / (total * 1f)) * 100) + "%");
		log.info("今日大比率：" + fnum.format((todayBig / (total * 1f)) * 100) + "%");

		log.info("球赛总数" + betInfoList.size());
	}

	class BuyWay {
		private Float highScoreSec;
		private Integer highScoreCostTime;
		private Integer minHighScoreTime;
		private int rollSmall_suc;
		private int rollSmall_fail;
		float smallWinMoney;
		float smallBuyMoneyEach = 100f;
		boolean showWinDetail = true;

		List<StringBuffer> smallBuyList = Lists.newArrayList();
		private String buySide;

		public BuyWay(Float highScoreSec, Integer highScoreCostTime, Integer minHighScoreTime,
				String buySide) {
			this.highScoreSec = highScoreSec;
			this.highScoreCostTime = highScoreCostTime;
			this.minHighScoreTime = minHighScoreTime;
			this.buySide = buySide;
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
			int highScoreTime = 0;
			int highScoreCost = 0;
			BetRollInfo previousBetRollInfo = null;
			for (BetRollInfo betRollInfo : list) {
				if (buySmall == null) {
					if (previousBetRollInfo != null
							&& previousBetRollInfo.isSameRatioOU(betRollInfo)) {
						continue;
					}

					float scoreEveryQuartz = CalUtil.computeScoreSec4Quartz(betRollInfo);
					if (scoreEveryQuartz <= 0)
						continue;
					if (highScoreSec != null) {

						if (scoreEveryQuartz >= highScoreSec) {
							highScoreTime++;
							highScoreCost += 600 - Integer.valueOf(betRollInfo.getT_count());
						} else {
							highScoreTime = 0;
							highScoreCost = 0;
						}

						if (highScoreTime == minHighScoreTime
								&& highScoreCost >= highScoreCostTime) {
							buySmall = betRollInfo;
							break;
						}

					}

					previousBetRollInfo = betRollInfo;
				}

			}
			if (buySmall != null && betInfo.getSc_total() != null
					&& buySmall.getRatio_rou_c() != null) {
				StringBuffer sb = new StringBuffer();

				sb.append(" " + betInfo.getLeague());
				sb.append(" " + betInfo.getTeam_h()).append(" vs ").append(betInfo.getTeam_c())
						.append(" " + betInfo.getDatetime());
				sb.append("买入").append(buySide).append(",比赛ID：").append(buySmall.getGid())
						.append(",滚球ID：").append(buySmall.getId()).append(",分数:")
						.append(buySmall.getRatio_rou_c()).append(",场节:")
						.append(buySmall.getSe_now()).append(",时间:").append(buySmall.getT_count())
						.append(",总分:").append(betInfo.getSc_total())
						.append(",初盘：" + betInfo.getRatio_o().substring(1)).append(",最大滚球分:")
						.append(betInfo.getMaxRatioRou()).append(",最小滚球分:")
						.append(betInfo.getMinRatioRou()).append(",初盘平均得分率:").append(scoreEachSec)
						.append(",买入时滚球得分率:")
						.append(fnum2.format(CalUtil.computeScoreSec4Quartz(buySmall)));
				float winMoney = Float.valueOf(buySmall.getIor_ROUH()) * smallBuyMoneyEach;
				boolean isWin = false;
				if (buySide.equals("H")) {
					isWin = Float.valueOf(betInfo.getSc_total()) < buySmall.getRatio_rou_c();
				} else if (buySide.equals("C")) {
					isWin = Float.valueOf(betInfo.getSc_total()) > buySmall.getRatio_rou_c();
				}
				if (isWin) {
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
			log.info("========================================================");
			log.info("滚球买入" + buySide + "，得分率" + highScoreSec + ",高比分时间：" + highScoreCostTime
					+ ",场数：" + (rollSmall_suc + rollSmall_fail) + ",出现次数:" + minHighScoreTime);
			log.info("滚球买入" + buySide + "，得分率" + highScoreSec + ",高比分时间：" + highScoreCostTime
					+ ",比率："
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

	class BuyWay2 {

		private Float interval;
		private Integer minHighScoreTime;
		private int rollSmall_suc;
		private int rollSmall_fail;
		private String seNow;
		float smallWinMoney;
		float smallBuyMoneyEach = 100f;
		boolean showWinDetail = true;
		private Integer highScoreCostTime;
		List<StringBuffer> smallBuyList = Lists.newArrayList();
		private String buySide;
		private String side;
		private Boolean desc;
		private Float maxintervalPercent;

		public BuyWay2(Float interval, Integer minHighScoreTime, Integer highScoreCostTime,
				String buySide, String seNow, Boolean desc, Float maxintervalPercent) {
			this.interval = interval;
			this.minHighScoreTime = minHighScoreTime;
			this.highScoreCostTime = highScoreCostTime;
			this.buySide = buySide;
			this.side = buySide;
			this.seNow = seNow;
			this.desc = desc;
			this.maxintervalPercent = maxintervalPercent;
		}

		public void compute(List<BetInfoDto> betInfoDtoList) {
			for (BetInfoDto betInfo : betInfoDtoList) {
				if (betInfo.getMinRatioRou() == null) {
					continue;
				}
				checkBuy(betInfo);
			}

		}

		public Float smallWinMoney() {
			return smallWinMoney;
		}

		public Float sucRate() {
			return (rollSmall_suc / ((rollSmall_suc + rollSmall_fail) * 1f));
		}

		private Boolean isAutoBuy() {
			return buySide.equals("AUTO");
		}

		public void checkBuy(BetInfoDto betInfo) {
			Float beginRatioOu = null;
			if (!StringUtils.isEmpty(betInfo.getRatio_o()))
				beginRatioOu = Float.valueOf(betInfo.getRatio_o().substring(1));

			BetRollInfo buySmall = null;
			BetRollInfo beginBuySmall = null;
			List<BetRollInfo> list = rollMap.get(betInfo.getGid());
			int highScoreTime = 0;
			int tmpHighScoreCostTime = 0;
			BetRollInfo previousBetRollInfo = null;
			float inter = 0f;
			side = "";
			beginBuySmall = null;
			String tmpSide = "";
			for (BetRollInfo betRollInfo : list) {
				if (buySmall == null) {

					if (previousBetRollInfo != null
							&& previousBetRollInfo.isSameRatioOU(betRollInfo)) {
						continue;
					}

					if (seNow != null && !seNow.equals(betRollInfo.getSe_now())) {
						continue;
					}
					float scoreEveryQuartz = CalUtil.computeScoreSec4Quartz(betRollInfo);
					float scoreAllQuartz = CalUtil.computeScoreSec4Alltime(betRollInfo);
					if (scoreEveryQuartz == 0f || scoreAllQuartz == 0f)
						continue;
					inter = Math.abs(scoreEveryQuartz - scoreAllQuartz);
					if (isAutoBuy()) {
						if (scoreEveryQuartz > scoreAllQuartz && inter >= interval) {
							tmpSide = "H";
						} else if (scoreEveryQuartz < scoreAllQuartz && inter >= interval) {
							tmpSide = "C";
						} else {
							tmpSide = "";
						}
					}
					if (!StringUtils.isEmpty(tmpSide) && !StringUtils.isEmpty(side)
							&& !tmpSide.equals(side)) {
						highScoreTime = 0;
						tmpHighScoreCostTime = 0;
					}
					if (!StringUtils.isEmpty(tmpSide)) {
						if (!tmpSide.equals(side)) {
							highScoreTime = 0;
						}
						side = tmpSide;
					}

					if (side.equalsIgnoreCase("H")) {
						if (scoreEveryQuartz - scoreAllQuartz >= interval) {
							highScoreTime++;
							if (beginBuySmall == null)
								beginBuySmall = betRollInfo;
							else
								tmpHighScoreCostTime = Integer.valueOf(beginBuySmall.getT_count())
										- Integer.valueOf(betRollInfo.getT_count())
										- tmpHighScoreCostTime;
						} else {
							highScoreTime = 0;
							tmpHighScoreCostTime = 0;
							beginBuySmall = null;
						}
					} else if (side.equalsIgnoreCase("C")) {
						if (scoreAllQuartz - scoreEveryQuartz >= interval) {
							highScoreTime++;
							if (beginBuySmall == null)
								beginBuySmall = betRollInfo;
							else
								tmpHighScoreCostTime = Integer.valueOf(beginBuySmall.getT_count())
										- Integer.valueOf(betRollInfo.getT_count())
										- tmpHighScoreCostTime;
						} else {
							highScoreTime = 0;
							beginBuySmall = null;
							tmpHighScoreCostTime = 0;
						}
					}
					if (minHighScoreTime != null && highScoreCostTime != null) {
						if (highScoreTime >= minHighScoreTime
								&& tmpHighScoreCostTime >= highScoreCostTime) {
							buySmall = betRollInfo;
							break;
						}
					} else {

						if (minHighScoreTime != null && highScoreTime == minHighScoreTime) {
							buySmall = betRollInfo;
							break;
						}

						if (highScoreCostTime != null
								&& tmpHighScoreCostTime >= highScoreCostTime) {
							buySmall = betRollInfo;
							break;
						}

					}
					previousBetRollInfo = betRollInfo;
				}

			}

			String operateName = "";
			operateName = desc ? "反向操作" : "正向操作";
			if (!desc || (maxintervalPercent != null
					&& inter >= (interval + maxintervalPercent * interval))) {
				operateName += (maxintervalPercent != null
						&& inter >= (interval + maxintervalPercent * interval))
								? ",再反转大于阀值"
										+ fnum2.format(interval + maxintervalPercent * interval)
								: "";
				if (side.equals("H")) {
					side = "C";
				} else if (side.equals("C")) {
					side = "H";
				}
			}
			if (buySmall!=null && buySmall.getId()==83557) {
				System.out.println(1);
			}
			Float fullScoreRate = CalUtil.computeScoreSec4Alltime(buySmall);
			avg = Math.round(Float.valueOf(betInfo.getRatio_o().substring(1)) / 4);
			scoreEachSec = avg / (600 * 1f);
			float rateInterval =  Float.valueOf(fnum3.format(fullScoreRate)) - Float.valueOf(fnum3.format(scoreEachSec));
			if (false && Math.abs(rateInterval) >= 0.009f) {
				operateName += "初盘与全场率差大于0.009";
				if (rateInterval > 0) {
					side = "C";
				} else {
					side = "H";
				}
			}
			if (buySmall != null && betInfo.getSc_total() != null
					&& buySmall.getRatio_rou_c() != null) {
				StringBuffer sb = new StringBuffer();

				// side =
				// Float.valueOf(buySmall.getIor_ROUH())>Float.valueOf(buySmall.getIor_ROUC())?"C":"H";
				String ratio_ou = side.equals("H") ? buySmall.getIor_ROUH()
						: buySmall.getIor_ROUC();
				float winMoney = Float.valueOf(ratio_ou) * smallBuyMoneyEach;
				boolean isWin = false;
				if (side.equals("H")) {
					isWin = Float.valueOf(betInfo.getSc_total()) < buySmall.getRatio_rou_c();
				} else if (side.equals("C")) {
					isWin = Float.valueOf(betInfo.getSc_total()) > buySmall.getRatio_rou_c();
				}
				if (isWin) {
					sb.append("结果：【+】赢" + fnum2.format(winMoney));
					rollSmall_suc++;
					smallWinMoney += winMoney;
				} else {
					sb.append("结果：【-】输" + fnum2.format(smallBuyMoneyEach));
					rollSmall_fail++;
					smallWinMoney -= smallBuyMoneyEach;
				}
				sb.append(",买入").append(side.equals("H") ? "【小】" : "【大】").append(",按方向:")
						.append(operateName).append(",买入分数:").append(buySmall.getRatio_rou_c())
						.append(",总分结果:").append(betInfo.getSc_total()).append(",Q4-全场得分:")
						.append(fnum2.format(CalUtil.computeScoreSec4Quartz(buySmall)
								- CalUtil.computeScoreSec4Alltime(buySmall)))
						.append(",持续时间").append(tmpHighScoreCostTime).append(",初率:")
						.append(fnum2.format(scoreEachSec)).append(",买入率:")
						.append(fnum2.format(CalUtil.computeScoreSec4Quartz(buySmall)))
						.append(",全场率:")
						.append(fnum2.format(CalUtil.computeScoreSec4Alltime(buySmall)))
						.append(",大回报:").append(buySmall.getIor_ROUC()).append(",小回报:")
						.append(buySmall.getIor_ROUH()).append(" " + betInfo.getLeague())
						.append(" " + betInfo.getTeam_h()).append(" vs ")
						.append(betInfo.getTeam_c()).append(" " + betInfo.getDatetime())
						.append(",比赛ID：").append(buySmall.getGid());
				if (beginBuySmall != null)
					sb.append(",开始计算滚球ID:" + beginBuySmall.getId());
				sb.append(",滚球ID：").append(buySmall.getId())

						.append(",场节:").append(buySmall.getSe_now()).append(",时间:")
						.append(buySmall.getT_count())
						.append(",初盘：" + betInfo.getRatio_o().substring(1)).append(",最大滚球分:")
						.append(betInfo.getMaxRatioRou()).append(",最小滚球分:")
						.append(betInfo.getMinRatioRou());
				smallBuyList.add(sb);
			}
		}

		public void printResult() {
			log.info("========================================================");
			log.info("滚球买入" + buySide + "，得分率差" + interval + ",场数："
					+ (rollSmall_suc + rollSmall_fail) + ",出现次数:" + minHighScoreTime + ",出现时间:"
					+ highScoreCostTime + ",是否反向买入:" + desc);
			log.info("滚球买入" + buySide + "，得分率差" + interval + ",比率："
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

	public void printRollDetail(BetInfoDto betInfo) {
		List<BetRollInfo> list = betRollInfoDao.find(betInfo.getGid());
		rollMap.put(betInfo.getGid(), list);
		for (BetRollInfo betRollInfo : list) {

			try {
				StringBuffer sb = new StringBuffer();
				sb.append("ID：").append(betRollInfo.getId());
				sb.append(",分数:" + betRollInfo.getRatio_rou_c()).append(",总分：")
						.append(StringUtils.leftPad(betRollInfo.getSc_total(), 3, "  ")).append(",")
						.append(betRollInfo.getSe_now())
						.append(" 节剩余（秒）:" + StringUtils.leftPad(betRollInfo.getT_count(), 3, " "))
						.append(",Q4-全场得分:")
						.append(fnum2.format(CalUtil.computeScoreSec4Quartz(betRollInfo)
								- CalUtil.computeScoreSec4Alltime(betRollInfo)))
						.append(" 每节进球（秒）："
								+ fnum2.format(CalUtil.computeScoreSec4Quartz(betRollInfo)))
						.append(" 全场进球（秒）："
								+ fnum2.format(CalUtil.computeScoreSec4Alltime(betRollInfo)))
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
			} catch (Exception e) {
				log.error("rollinfo id:" + betRollInfo.getId(), e);
			}
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
		StringBuffer quartz1_sb = new StringBuffer();
		StringBuffer quartz2_sb = new StringBuffer();
		StringBuffer quartz3_sb = new StringBuffer();
		StringBuffer quartz4_sb = new StringBuffer();
		if (true) {
			BetRollInfo Q1betRollInfo = betRollInfoDao.queryLastRollInfoByQuartz(betInfo.getGid(),
					"Q1");
			quartz1_sb.append(" 【Q1】" + betInfo.getSc_Q1_total())
					.append(" " + fnum.format(Q1float) + "%").append("(")
					.append(Q1betRollInfo != null && Q1betRollInfo.getRatio_rou_c() != null
							? Q1betRollInfo.getRatio_rou_c() : "")
					.append(")")
					.append(" 进球（秒）：" + fnum2.format(CalUtil.computeScoreSec4Quartz(Q1betRollInfo)))
					.append(" 全场进球（秒）："
							+ fnum2.format(CalUtil.computeScoreSec4Alltime(Q1betRollInfo)));
			BetRollInfo Q2betRollInfo = betRollInfoDao.queryLastRollInfoByQuartz(betInfo.getGid(),
					"Q2");
			quartz2_sb.append(" 【Q2】" + betInfo.getSc_Q2_total())
					.append(" " + fnum.format(Q2float) + "%").append("(")
					.append(Q2betRollInfo != null && Q2betRollInfo.getRatio_rou_c() != null
							? Q2betRollInfo.getRatio_rou_c() : "")
					.append(")")
					.append(" 进球（秒）：" + fnum2.format(CalUtil.computeScoreSec4Quartz(Q2betRollInfo)))
					.append(" 全场进球（秒）："
							+ fnum2.format(CalUtil.computeScoreSec4Alltime(Q2betRollInfo)));
			BetRollInfo Q3betRollInfo = betRollInfoDao.queryLastRollInfoByQuartz(betInfo.getGid(),
					"Q3");
			quartz3_sb.append(" 【Q3】" + betInfo.getSc_Q3_total())
					.append(" " + fnum.format(Q3float) + "%").append("(")
					.append(Q3betRollInfo != null && Q3betRollInfo.getRatio_rou_c() != null
							? Q3betRollInfo.getRatio_rou_c() : "")
					.append(")")
					.append(" 进球（秒）：" + fnum2.format(CalUtil.computeScoreSec4Quartz(Q3betRollInfo)))
					.append(" 全场进球（秒）："
							+ fnum2.format(CalUtil.computeScoreSec4Alltime(Q3betRollInfo)));
			BetRollInfo Q4betRollInfo = betRollInfoDao.queryLastRollInfoByQuartz(betInfo.getGid(),
					"Q4");
			quartz4_sb.append(" 【Q4】" + betInfo.getSc_Q4_total()).append(" ").append("(")
					.append(Q4betRollInfo != null && Q4betRollInfo.getRatio_rou_c() != null
							? Q4betRollInfo.getRatio_rou_c() : "")
					.append(")")
					.append(" 进球（秒）：" + fnum2.format(CalUtil.computeScoreSec4Quartz(Q4betRollInfo)))
					.append(" 全场进球（秒）："
							+ fnum2.format(CalUtil.computeScoreSec4Alltime(Q4betRollInfo)));
		}
		log.info(sb.toString());
		log.info(ratio_re_sb.toString());
		log.info(ratio_rou_sb.toString());
		log.info(common_sb.toString());
		log.info("每节比赛分数,  每节结束后总分-(平均分*N节)/平均分*N节,  每节最后时间滚球大小球");
		log.info(quartz1_sb.toString());
		log.info(quartz2_sb.toString());
		log.info(quartz3_sb.toString());
		log.info(quartz4_sb.toString());
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
					.append(",Q4得分-全场:")
					.append(fnum2.format(CalUtil.computeScoreSec4Quartz(betRollInfo)
							- CalUtil.computeScoreSec4Alltime(betRollInfo)))
					.append(",大回报:").append(betRollInfo.getIor_ROUC()).append(",小回报:")
					.append(betRollInfo.getIor_ROUH()).append(betRollInfo.getSe_now())
					.append(" 进球（秒）：" + fnum2.format(CalUtil.computeScoreSec4Quartz(betRollInfo)))
					.append(" 全场进球（秒）："
							+ fnum2.format(CalUtil.computeScoreSec4Alltime(betRollInfo)))
					.append(" 节剩余（秒）:" + StringUtils.leftPad(betRollInfo.getT_count(), 3, " "))
					.append(" 期望值:" + fnum.format(expectRatio(betRollInfo)))
					.append(" [Q1]：" + betRollInfo.getSc_Q1_total())
					.append(" [Q2]：" + betRollInfo.getSc_Q2_total())
					.append(" [Q3]：" + betRollInfo.getSc_Q3_total())
					.append(" [Q4]：" + betRollInfo.getSc_Q4_total());
			log.info(sb.toString());
		}
	}

	private Float expectRatio(BetRollInfo betRollInfo) {
		if (betRollInfo == null || StringUtils.isEmpty(betRollInfo.getT_count()))
			return 0f;
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

	private Float expectRatio2(BetRollInfo betRollInfo) {
		if (betRollInfo == null)
			return 0f;
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
