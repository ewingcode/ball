package com.ewing.order.busi.ball.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.utils.StringUtils;
import com.ewing.order.ball.BallAccountCenter;
import com.ewing.order.ball.BallMember;
import com.ewing.order.ball.shared.BetRuleStatus;
import com.ewing.order.ball.shared.GtypeStatus;
import com.ewing.order.ball.shared.PtypeStatus;
import com.ewing.order.busi.ball.dao.BwContinueDao;
import com.ewing.order.busi.ball.dao.ReportDao;
import com.ewing.order.busi.ball.ddl.BetAutoBuy;
import com.ewing.order.busi.ball.ddl.BetLog;
import com.ewing.order.busi.ball.ddl.BetRule;
import com.ewing.order.busi.ball.ddl.BwContinue;
import com.ewing.order.busi.ball.dto.BetAutoBuyDto;
import com.ewing.order.busi.ball.dto.BetDetailDto;
import com.ewing.order.busi.ball.dto.TotalBillDto;
import com.ewing.order.busi.ball.service.BetAutoBuyService;
import com.ewing.order.busi.ball.service.BetLogService;
import com.ewing.order.busi.ball.service.BetRuleService;
import com.ewing.order.common.contant.IsEff;
import com.ewing.order.core.web.base.BaseRest;
import com.ewing.order.core.web.common.RestResult;
import com.ewing.order.util.BeanCopy;
import com.google.common.collect.Lists;

/**
 *
 * @author tansonlam
 * @create 2018年8月10日
 */
@RestController
public class ReportRest extends BaseRest {
	private static Logger log = LoggerFactory.getLogger(ReportRest.class);
	@Resource
	private BetAutoBuyService betAutoBuyService;
	@Resource
	private ReportDao reportDao;
	@Resource
	private BallMember ballMember;
	@Resource
	private BetRuleService betRuleService;
	@Resource
	private BwContinueDao bwContinueDao;
	@Resource
	private BetLogService betLogService;

	public static void main(String[] args) {
		String likeType = "ts[^,]*";
		String sourceStr = "tsLAM83";
		//likeType = likeType.replaceAll("%", "\\\\d").replaceAll("\\*", "\\\\d\\*");
		System.out.println(likeType);
		System.out.println(sourceStr.matches(likeType));
 
	}
	
	@RequestMapping(value = "/report/win.op", method = RequestMethod.GET)
	@ResponseBody
	public RestResult<List<BetAutoBuyDto>> win() throws Exception {
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String requestAccount = request.getParameter("account");
		checkRequired(startDate, "date");
		 
		List<BetAutoBuyDto> betAutoBuyDtoList=Lists.newArrayList();
		try {
			List<BetAutoBuy> betAutoBuyList = betAutoBuyService.findAll();
			List<BwContinue> bwContinueList = bwContinueDao.findAllRunning();
			betAutoBuyDtoList = Lists.newArrayList();
			for (BetAutoBuy betAutoBuy : betAutoBuyList) {
				String account = betAutoBuy.getAccount();
				 if(StringUtils.isNotEmpty(requestAccount) && !account.matches(requestAccount+"[^,]*")){
					 continue;
				 }
				List<BetRule> ruleList = betRuleService.findRule(account, BetRuleStatus.NOTSUCCESS,
						GtypeStatus.BK, PtypeStatus.ROLL);
				List<BetLog> betLogList = betLogService.findEachDay(account, GtypeStatus.BK);
				if (betAutoBuy != null) { 
					BetAutoBuyDto dto = new BetAutoBuyDto();
					betAutoBuyDtoList.add(dto);
					BeanCopy.copy(dto, betAutoBuy, true);
					if (CollectionUtils.isNotEmpty(ruleList)) {
						dto.setMoney(ruleList.get(0).getMoney());
						dto.setContinueMaxMatch(ruleList.get(0).getContinueMaxMatch() == null ? "0"
								: ruleList.get(0).getContinueMaxMatch().toString());
						dto.setContinueStartLostnum(ruleList.get(0).getContinueStartLostnum() == null
								? "1" : ruleList.get(0).getContinueStartLostnum().toString());
						dto.setContinuePlanMoney(ruleList.get(0).getContinuePlanMoney());
						dto.setStopWingold(ruleList.get(0).getStopWingold() == null ? "0"
								: String.valueOf(ruleList.get(0).getStopWingold().intValue()));
						dto.setStopLosegold(ruleList.get(0).getStopLosegold() == null ? "0"
								: String.valueOf(ruleList.get(0).getStopLosegold().intValue()));
						dto.setIsTest(ruleList.get(0).getIsTest() == null ? "0"
								: ruleList.get(0).getIsTest().toString());
						dto.setRuleName(ruleList.get(0).getName()); 
						dto.setMaxEachDay(ruleList.get(0).getMaxEachday().toString());
					}
					
					dto.setTodayTotalMatch(CollectionUtils.isEmpty(betLogList)?0:betLogList.size());
					// 如果不是活跃中的用户则设置为失效用户，让前台可以更新用户状态来激活自动下注
					dto.setIseff(
							ballMember.isActiveAccount(account) ? IsEff.EFFECTIVE : IsEff.INEFFECTIVE);
					List<TotalBillDto> totalWinList = reportDao.findTotalWinByTicketIds(startDate, endDate, account);
						if(CollectionUtils.isNotEmpty(totalWinList)){
							dto.setTotalBillDto(totalWinList.get(0));
						} 
					for(BwContinue bwContinue : bwContinueList){
						if(bwContinue.getAccount().equals(account)){
							dto.setBwContinue(bwContinue);
							if(StringUtils.isNotEmpty(bwContinue.getContinuePlanMoney()))
								dto.setContinuePlanMoney(bwContinue.getContinuePlanMoney());
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	
		return RestResult.successResult(betAutoBuyDtoList);
	}

	@RequestMapping(value = "/report/windetail.op", method = RequestMethod.GET)
	@ResponseBody
	public RestResult<List<TotalBillDto>> windetail() throws Exception {
		String date = request.getParameter("date");
		checkRequired(date, "date");
		List<TotalBillDto> totalList = reportDao.findTotalWinDetail(date);
		return RestResult.successResult(totalList);
	}

	@RequestMapping(value = "/report/betdetail.op", method = RequestMethod.GET)
	@ResponseBody
	public RestResult<List<BetDetailDto>> betdetail() throws Exception {
		String date = request.getParameter("date");
		String account = request.getParameter("account");
		// checkRequired(account, "account");
		checkRequired(date, "date");
		List<BetDetailDto> totalList = reportDao.findBetDetail(account, date);
		return RestResult.successResult(totalList);
	}
}
