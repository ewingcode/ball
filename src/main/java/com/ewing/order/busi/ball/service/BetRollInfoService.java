package com.ewing.order.busi.ball.service;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ewing.order.ball.dto.BetInfoDto;
import com.ewing.order.ball.shared.GameStatus;
import com.ewing.order.busi.ball.dao.BetInfoDao;
import com.ewing.order.busi.ball.dao.BetRollInfoDao;
import com.ewing.order.busi.ball.ddl.BetInfo;
import com.ewing.order.busi.ball.ddl.BetRollInfo;
import com.ewing.order.busi.ball.ddl.RollGameCompute;
import com.ewing.order.util.BeanCopy;
import com.google.common.collect.Lists;

@Component
public class BetRollInfoService {
	@Resource
	private BetRollInfoDao betRollInfoDao;
	@Resource
	private BetInfoDao betInfoDao;

	public BetRollInfo findById(Integer id) {
		return betRollInfoDao.findById(id);
	}

	public List<BetInfoDto> fillMaxMinInfo(List<BetInfo> list) {
		List<BetInfoDto> dtoList = BeanCopy.copy(list, BetInfoDto.class);
		for (BetInfoDto betInfo : dtoList) {
			RollGameCompute rollGameCompute = betRollInfoDao.computeMinAndMax(betInfo.getGid());
			if (rollGameCompute != null) {
				betInfo.setMaxRatioR(rollGameCompute.getMaxRatioR());
				betInfo.setMinRatioR(rollGameCompute.getMinRatioR());
				betInfo.setMinRatioRou(rollGameCompute.getMinRatioRou());
				betInfo.setMaxRatioRou(rollGameCompute.getMaxRatioRou());
			}
		}
		return dtoList;
	}

	@Transactional(rollbackOn = { Exception.class })
	public List<BetInfo> updateByRoll(List<BetRollInfo> list) {
		List<BetInfo> lastBetInfos = Lists.newArrayList();
		for (BetRollInfo betRollInfo : list) {

			if (StringUtils.isEmpty(betRollInfo.getGid()))
				continue;
			// 计算滚球变化数据
			if (betRollInfo.getGtype().equals("BK")) {
				calBasketball(betRollInfo);
			}
			// 记录滚球变化
			recordRollBetInfo(betRollInfo);
			// 更新对应投注项
			lastBetInfos.add(updateBetInfo(betRollInfo));
		}
		return lastBetInfos;
	}

	/**
	 * 更新记录滚球的变化
	 * 
	 * @param betRollInfo
	 */
	private void recordRollBetInfo(BetRollInfo betRollInfo) {
		BetRollInfo yetRollBetInfo = betRollInfoDao.findLastInfo(betRollInfo.getGid());
		if ((yetRollBetInfo == null)
				|| (yetRollBetInfo != null && !yetRollBetInfo.isSame(betRollInfo))) {
			betRollInfoDao.save(betRollInfo);
		}
	}

	/**
	 * 更新投注信息
	 * 
	 * @param betRollInfo
	 */
	private BetInfo updateBetInfo(BetRollInfo betRollInfo) {
		BetInfo yetBetInfo = betInfoDao.findByGameId(betRollInfo.getGid());

		boolean isExist = true;
		if (yetBetInfo == null) {
			isExist = false;
			yetBetInfo = new BetInfo();
			BeanCopy.copy(yetBetInfo, betRollInfo, true);
			yetBetInfo.setId(null);
			yetBetInfo.setCreateTime(null);
			yetBetInfo.setLastUpdate(null);
			yetBetInfo.setStrong(betRollInfo.getStrong());
			yetBetInfo.setRatio(betRollInfo.getRatio_re());
			yetBetInfo.setIor_RC(betRollInfo.getIor_REC());
			yetBetInfo.setIor_RH(betRollInfo.getIor_REH());
			yetBetInfo.setRatio_o(betRollInfo.getRatio_rouo());
			yetBetInfo.setRatio_u(betRollInfo.getRatio_rouu());
			yetBetInfo.setIor_OUC(betRollInfo.getIor_HROUC());
			yetBetInfo.setIor_OUH(betRollInfo.getIor_HROUH());
			yetBetInfo.setSw_OU(betRollInfo.getSw_ROU());
			yetBetInfo.setSw_R(betRollInfo.getSw_RE());
		}
		if (!StringUtils.isEmpty(betRollInfo.getRatio_re_c()))
			yetBetInfo.setN_ratio_re_c(betRollInfo.getRatio_re_c());
		if (!StringUtils.isEmpty(betRollInfo.getRatio_rou_c()))
			yetBetInfo.setN_ratio_rou_c(betRollInfo.getRatio_rou_c());
		if (!StringUtils.isEmpty(betRollInfo.getRe_time()))
			yetBetInfo.setRe_time(betRollInfo.getRe_time());
		if (!StringUtils.isEmpty(betRollInfo.getSystime()))
			yetBetInfo.setSystime(betRollInfo.getSystime());
		if (!StringUtils.isEmpty(betRollInfo.getSc_total()))
			yetBetInfo.setSc_total(betRollInfo.getSc_total());
		if (!StringUtils.isEmpty(betRollInfo.getRou_dis()))
			yetBetInfo.setRou_dis(betRollInfo.getRou_dis());
		if (!StringUtils.isEmpty(betRollInfo.getRe_dis()))
			yetBetInfo.setRe_dis(betRollInfo.getRe_dis());
		if (!StringUtils.isEmpty(betRollInfo.getSc_Q1_A()))
			yetBetInfo.setSc_Q1_A(betRollInfo.getSc_Q1_A());
		if (!StringUtils.isEmpty(betRollInfo.getSc_Q1_H()))
			yetBetInfo.setSc_Q1_H(betRollInfo.getSc_Q1_H());
		if (!StringUtils.isEmpty(betRollInfo.getSc_Q1_total()))
			yetBetInfo.setSc_Q1_total(betRollInfo.getSc_Q1_total());
		if (!StringUtils.isEmpty(betRollInfo.getSc_Q2_A()))
			yetBetInfo.setSc_Q2_A(betRollInfo.getSc_Q2_A());
		if (!StringUtils.isEmpty(betRollInfo.getSc_Q2_H()))
			yetBetInfo.setSc_Q2_H(betRollInfo.getSc_Q2_H());
		if (!StringUtils.isEmpty(betRollInfo.getSc_Q2_total()))
			yetBetInfo.setSc_Q2_total(betRollInfo.getSc_Q2_total());
		if (!StringUtils.isEmpty(betRollInfo.getSc_Q3_A()))
			yetBetInfo.setSc_Q3_A(betRollInfo.getSc_Q3_A());
		if (!StringUtils.isEmpty(betRollInfo.getSc_Q3_H()))
			yetBetInfo.setSc_Q3_H(betRollInfo.getSc_Q3_H());
		if (!StringUtils.isEmpty(betRollInfo.getSc_Q3_total()))
			yetBetInfo.setSc_Q3_total(betRollInfo.getSc_Q3_total());
		if (!StringUtils.isEmpty(betRollInfo.getSc_Q4_A()))
			yetBetInfo.setSc_Q4_A(betRollInfo.getSc_Q4_A());
		if (!StringUtils.isEmpty(betRollInfo.getSc_Q4_H()))
			yetBetInfo.setSc_Q4_H(betRollInfo.getSc_Q4_H());
		if (!StringUtils.isEmpty(betRollInfo.getSc_Q4_total()))
			yetBetInfo.setSc_Q4_total(betRollInfo.getSc_Q4_total());
		if (!StringUtils.isEmpty(betRollInfo.getStrong()))
			yetBetInfo.setN_strong(betRollInfo.getStrong());
		if (!StringUtils.isEmpty(betRollInfo.getRatio_re()))
			yetBetInfo.setN_ratio(betRollInfo.getRatio_re());
		if (!StringUtils.isEmpty(betRollInfo.getIor_REC()))
			yetBetInfo.setN_ior_RC(betRollInfo.getIor_REC());
		if (!StringUtils.isEmpty(betRollInfo.getIor_REH()))
			yetBetInfo.setN_ior_RH(betRollInfo.getIor_REH());
		if (!StringUtils.isEmpty(betRollInfo.getRatio_rouo()))
			yetBetInfo.setN_ratio_o(betRollInfo.getRatio_rouo());
		if (!StringUtils.isEmpty(betRollInfo.getRatio_rouu()))
			yetBetInfo.setN_ratio_u(betRollInfo.getRatio_rouu());
		if (!StringUtils.isEmpty(betRollInfo.getIor_ROUC()))
			yetBetInfo.setN_ior_OUC(betRollInfo.getIor_ROUC());
		if (!StringUtils.isEmpty(betRollInfo.getIor_ROUH()))
			yetBetInfo.setN_ior_OUH(betRollInfo.getIor_ROUH());
		if (!StringUtils.isEmpty(betRollInfo.getSw_ROU()))
			yetBetInfo.setN_sw_OU(betRollInfo.getSw_ROU());
		if (!StringUtils.isEmpty(betRollInfo.getSw_RE()))
			yetBetInfo.setN_sw_R(betRollInfo.getSw_RE());
		if (!StringUtils.isEmpty(betRollInfo.getSc_new()))
			yetBetInfo.setSc_new(betRollInfo.getSc_new());
		if (!StringUtils.isEmpty(betRollInfo.getSe_now()))
			yetBetInfo.setSe_now(betRollInfo.getSe_now());
		if (!StringUtils.isEmpty(betRollInfo.getT_count()))
			yetBetInfo.setT_count(betRollInfo.getT_count());
		if (!StringUtils.isEmpty(betRollInfo.getT_status()))
			yetBetInfo.setT_status(betRollInfo.getT_status());
		if (!StringUtils.isEmpty(betRollInfo.getScore_c()))
			yetBetInfo.setScore_c(betRollInfo.getScore_c());
		if (!StringUtils.isEmpty(betRollInfo.getScore_h()))
			yetBetInfo.setScore_h(betRollInfo.getScore_h());
		if (!StringUtils.isEmpty(betRollInfo.getPtype()))
			yetBetInfo.setPtype(betRollInfo.getPtype());
		if (!StringUtils.isEmpty(betRollInfo.getSc_FT_A()))
			yetBetInfo.setSc_FT_A(betRollInfo.getSc_FT_A());
		if (!StringUtils.isEmpty(betRollInfo.getSc_FT_H()))
			yetBetInfo.setSc_FT_H(betRollInfo.getSc_FT_H());
		if (!StringUtils.isEmpty(betRollInfo.getSc_OT_A()))
			yetBetInfo.setSc_OT_A(betRollInfo.getSc_OT_A());
		if (!StringUtils.isEmpty(betRollInfo.getSc_OT_H()))
			yetBetInfo.setSc_OT_H(betRollInfo.getSc_OT_H());
		if (!StringUtils.isEmpty(betRollInfo.getSe_type()))
			yetBetInfo.setSe_type(betRollInfo.getSe_type());
		if (!StringUtils.isEmpty(betRollInfo.getSe_now()))
			yetBetInfo.setSe_now(betRollInfo.getSe_now());
		if (!StringUtils.isEmpty(betRollInfo.getT_count()))
			yetBetInfo.setT_count(betRollInfo.getT_count());
		if (isExist) {
			yetBetInfo.setLastUpdate(new java.sql.Timestamp((new java.util.Date()).getTime()));
			yetBetInfo.setStatus(GameStatus.RUNNING);
			betInfoDao.update(yetBetInfo);
		} else {
			betInfoDao.save(yetBetInfo);
		}
		return yetBetInfo;
	}

	private void calBasketball(BetRollInfo betRollInfo) {
		if (!StringUtils.isEmpty(betRollInfo.getRatio_re())) {
			Float ratio_re_c = Float.valueOf(
					(betRollInfo.getStrong().equals("H") ? "" : "-") + betRollInfo.getRatio_re());
			betRollInfo.setRatio_re_c(ratio_re_c);
		}
		if (!StringUtils.isEmpty(betRollInfo.getRatio_rouo())) {
			Float ratio_rou_c = Float.valueOf(betRollInfo.getRatio_rouo().replace("大", "").trim());
			betRollInfo.setRatio_rou_c(ratio_rou_c);
		}
		if (!StringUtils.isEmpty(betRollInfo.getSc_FT_A())
				&& !StringUtils.isEmpty(betRollInfo.getSc_FT_H()))
			betRollInfo.setSc_total(
					add(betRollInfo.getSc_FT_A(), betRollInfo.getSc_FT_H()).toString());
		if (!StringUtils.isEmpty(betRollInfo.getRatio_rouu())
				&& !StringUtils.isEmpty(betRollInfo.getSc_total()))
			betRollInfo.setRou_dis(
					subtract(betRollInfo.getRatio_rouu().replace(" ", "").replace("小", ""),
							betRollInfo.getSc_total()).toString());
		if (!StringUtils.isEmpty(betRollInfo.getSc_FT_A())
				&& !StringUtils.isEmpty(betRollInfo.getSc_FT_H()))
			betRollInfo.setRe_dis(
					subtract(betRollInfo.getSc_FT_A(), betRollInfo.getSc_FT_H()).toString());
		if (!StringUtils.isEmpty(betRollInfo.getSc_Q4_H())
				&& !StringUtils.isEmpty(betRollInfo.getSc_Q4_A()))
			betRollInfo.setSc_Q4_total(
					add(betRollInfo.getSc_Q4_H(), betRollInfo.getSc_Q4_A()).toString());
		if (!StringUtils.isEmpty(betRollInfo.getSc_Q3_H())
				&& !StringUtils.isEmpty(betRollInfo.getSc_Q3_A()))
			betRollInfo.setSc_Q3_total(
					add(betRollInfo.getSc_Q3_H(), betRollInfo.getSc_Q3_A()).toString());
		if (!StringUtils.isEmpty(betRollInfo.getSc_Q2_H())
				&& !StringUtils.isEmpty(betRollInfo.getSc_Q2_A()))
			betRollInfo.setSc_Q2_total(
					add(betRollInfo.getSc_Q2_H(), betRollInfo.getSc_Q2_A()).toString());
		if (!StringUtils.isEmpty(betRollInfo.getSc_Q1_H())
				&& !StringUtils.isEmpty(betRollInfo.getSc_Q1_A()))
			betRollInfo.setSc_Q1_total(
					add(betRollInfo.getSc_Q1_H(), betRollInfo.getSc_Q1_A()).toString());

	}

	private Integer add(String... numbers) {
		Integer total = 0;
		for (String num : numbers) {
			if (!StringUtils.isEmpty(num)) {
				total += Integer.valueOf(num);
			}
		}
		return total;
	}

	private Float subtract(String numbers1, String numbers2) {
		Float num1 = StringUtils.isEmpty(numbers1) ? 0 : Float.valueOf(numbers1);
		Float num2 = StringUtils.isEmpty(numbers2) ? 0 : Float.valueOf(numbers2);
		return Math.abs(num2 - num1);
	}

}
