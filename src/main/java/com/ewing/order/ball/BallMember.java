package com.ewing.order.ball;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ewing.order.ball.bill.DailyBillResp;
import com.ewing.order.ball.bk.bet.BkPreOrderViewResp;
import com.ewing.order.ball.bk.bet.BetResp;
import com.ewing.order.ball.bk.game.BkGame;
import com.ewing.order.ball.bk.game.BkGameListResp;
import com.ewing.order.ball.bk.game.BkRollGame;
import com.ewing.order.ball.bk.game.BkRollGameListResp;
import com.ewing.order.ball.event.BallSource;
import com.ewing.order.ball.event.BetInfoListener;
import com.ewing.order.ball.event.BetStrategyPool;
import com.ewing.order.ball.ft.game.FtGame;
import com.ewing.order.ball.ft.game.FtRollGame;
import com.ewing.order.ball.ft.game.FtRollGameListResp;
import com.ewing.order.ball.league.LeagueResp;
import com.ewing.order.ball.leaguelist.League;
import com.ewing.order.ball.leaguelist.LeagueListResp;
import com.ewing.order.ball.login.LoginResp;
import com.ewing.order.ball.login.MemberResp;
import com.ewing.order.busi.ball.ddl.BetBill;
import com.ewing.order.busi.ball.service.BetBillService;
import com.ewing.order.busi.ball.service.BetLogService;
import com.ewing.order.busi.ball.service.BetRuleService;
import com.ewing.order.util.BeanCopy;
import com.ewing.order.util.DataFormat;
import com.ewing.order.util.HttpUtils;

/**
 *
 * @author tansonlam
 * @create 2018年7月20日
 */
@Component
public class BallMember {
	private static Logger log = LoggerFactory.getLogger(HttpUtils.class);
	private LoginResp loginResp;
	private String uid;
	private String account;
	private MemberResp memberResp;
	private String eachBetMoney = "50";
	private int dateBillInterval = 2;
	@Resource
	private BetBillService betBillService;
	@Resource
	private BetLogService betLogService;
	@Resource
	private BetRuleService betRuleService;

	private FootBallService footBallService;

	public void login(String account, String pwd) {
		loginResp = RequestTool.login(account, pwd);
		this.uid = loginResp.getUid();
		this.account = account;
		this.footBallService = FootBallService.getInstance();
		// memberResp = RequestTool.memberSet(loginResp.getUid());
		log.info("login successfully.");
		startHeartBeat();
	}

	/**
	 * 添加今日足球的监听器
	 */
	public void addFtCurrentListener() {
		BetStrategyPool betStrategyPool = new BetStrategyPool();

		// betStrategyPool.addBetStrategy(fTBasicBetStrategy1());
		// betStrategyPool.addBetStrategy(fTBasicBetStrategy2());
		betStrategyPool.getBetStrategyContext().setBetLogService(betLogService)
				.setBetRuleService(betRuleService).setAccount(account).setUid(uid);
		betStrategyPool.loadBetStrategyConf();
		BetInfoListener betInfoListener = new BetInfoListener(account, betStrategyPool);
		BallSource.getFTCurrent().addBallListener(betInfoListener);
	}

	/**
	 * 添加今日篮球的监听器
	 */
	public void addBkCurrentListener() {
		BetStrategyPool betStrategyPool = new BetStrategyPool();
		// betStrategyPool.addBetStrategy(fTBasicBetStrategy1());
		betStrategyPool.getBetStrategyContext().setBetLogService(betLogService).setAccount(account);
		BetInfoListener betInfoListener = new BetInfoListener(account, betStrategyPool);
		BallSource.getBKCurrent().addBallListener(betInfoListener);
	}

	/**
	 * 登陆后的心跳，保持在线连接
	 */
	private void startHeartBeat() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					RequestTool.heartBeat(uid);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}, 1000, 5000);
	}

	/**
	 * 获取联赛总数
	 * 
	 * @return
	 */
	public LeagueResp getLeaguesCount() {
		return RequestTool.getLeaguesCount(uid);
	}

	/**
	 * 获取指定类型球类的联赛数
	 * 
	 * @param gtype
	 * @param showType
	 * @return
	 */
	public LeagueListResp getLeagueList(String gtype, String showType) {
		return RequestTool.getLeagueList(uid, gtype, showType);
	}

	/**
	 * 投注足球滚球
	 */
	public void playRollFootball() {
		log.info("查询足球滚球：");
		String gtype = "FT";// 篮球
		String showTyp = "RB";// 未开始的比赛
		String wtype = "ROU";// 让球
		LeagueListResp leagueListResp = getLeagueList(gtype, showTyp);
		if (CollectionUtils.isEmpty(leagueListResp.getGame().get(0).getLeague())) {
			log.info("暂时没有足球滚球");
		} else {
			for (League league : leagueListResp.getGame().get(0).getLeague()) {
				log.info(league.toString());
				FtRollGameListResp gameListResp = RequestTool.getFtRollGameList(uid, gtype, showTyp,
						league.getLeague_id());
				log.info("投注项：" + gameListResp);
				List<FtRollGame> gameList = gameListResp.getGame();
				if (CollectionUtils.isEmpty(gameList))
					continue;

				for (FtRollGame game : gameList) {
					if (allowBuy(game.getRatio_rouu())) {
						String side = game.getStrong();
						BkPreOrderViewResp bkPreOrderViewResp = RequestTool.getFtPreOrderView(uid,
								game.getGid(), gtype, wtype, game.getStrong());
						log.info("投注前信息：" + bkPreOrderViewResp);

						BetResp ftBetResp = RequestTool.ftbet(uid, game.getGid(), gtype,
								eachBetMoney, wtype, side, bkPreOrderViewResp);
						log.info("=====下注结果：" + ftBetResp);
					} else {
						log.info("=====不符合下注");
					}

				}
			}
		}
	}

	/**
	 * 投注今日足球
	 */
	public void playCurrentFootball() {
		log.info("查询足球滚球：");
		String gtype = "FT";// 篮球
		String wtype = "OU";// 大小
		List<FtGame> gameList = footBallService.collectCurrentFootball(uid);

		for (FtGame game : gameList) {
			String side = game.getStrong();

			if (allowBuy(game.getRatio_u())) {

				BkPreOrderViewResp bkPreOrderViewResp = RequestTool.getFtPreOrderView(uid,
						game.getGid(), gtype, wtype, game.getStrong());
				log.info("投注前信息：" + bkPreOrderViewResp);
				BetResp ftBetResp = RequestTool.ftbet(uid, game.getGid(), gtype, eachBetMoney,
						wtype, side, bkPreOrderViewResp);

				log.info("=====下注结果：" + ftBetResp);

			} else {
				log.info("=====不符合下注");
			}

		}
	}

	/**
	 * 较低的赔率
	 * 
	 * @param ior_OUH
	 * @param ior_OUC
	 * @return
	 */
	private Boolean lowerIor(Float ior_OUH, Float ior_OUC) {
		return ior_OUH.compareTo(ior_OUC) == -1;
	}

	private Boolean allowBuy(String radio) {
		return radio.matches("小 2") || radio.matches("小 3.5") || radio.matches("小 4")
				|| radio.matches("小 4.5") || radio.matches("小 5") || radio.matches("小 5.5")
				|| radio.matches("小 6") || radio.matches("小 6.5");
	}

	/**
	 * 投注篮球滚球
	 */
	public void playRollBasketball() {
		log.info("查询篮球滚球：");
		String gtype = "BK";// 篮球
		String showTyp = "RB";// 未开始的比赛
		String wtype = "R";// 让球
		LeagueListResp leagueListResp = getLeagueList(gtype, showTyp);
		if (CollectionUtils.isEmpty(leagueListResp.getGame().get(0).getLeague())) {
			log.info("暂时没有篮球滚球");
		} else {
			for (League league : leagueListResp.getGame().get(0).getLeague()) {
				log.info(league.toString());
				BkRollGameListResp gameListResp = RequestTool.getBkRollGameList(uid, gtype, showTyp,
						league.getLeague_id());
				log.info("投注项：" + gameListResp);
				List<BkRollGame> gameList = gameListResp.getGame();
				if (CollectionUtils.isEmpty(gameList))
					continue;
				for (BkRollGame game : gameList) {
					String side = game.getStrong();
					BkPreOrderViewResp bkPreOrderViewResp = RequestTool.getbkPreOrderView(uid,
							game.getGid(), gtype, wtype, game.getStrong());
					log.info("投注前信息：" + bkPreOrderViewResp);
					RequestTool.bkbet(uid, game.getGid(), gtype, eachBetMoney, wtype, side,
							bkPreOrderViewResp);
				}
			}
		}
	}

	public void playCurrentBasketball() {
		log.info("查询今天篮球：");
		String gtype = "BK";// 篮球
		String showTyp = "FT";// 未开始的比赛
		String wtype = "R";// 让球
		LeagueListResp leagueListResp = getLeagueList(gtype, showTyp);
		if (CollectionUtils.isEmpty(leagueListResp.getGame().get(0).getLeague())) {
			log.info("今天暂时没有篮球");
		} else {
			for (League league : leagueListResp.getGame().get(0).getLeague()) {
				log.info("联赛：" + league.toString());
				BkGameListResp gameListResp = RequestTool.getBkGameList(uid, gtype, showTyp,
						league.getLeague_id());
				log.info("投注项：" + gameListResp);
				List<BkGame> gameList = gameListResp.getGame();
				if (CollectionUtils.isEmpty(gameList))
					continue;
				for (BkGame game : gameList) {
					String side = game.getStrong();
					BkPreOrderViewResp bkPreOrderViewResp = RequestTool.getbkPreOrderView(uid,
							game.getGid(), gtype, wtype, game.getStrong());
					log.info("投注前信息：" + bkPreOrderViewResp);
					RequestTool.bkbet(uid, game.getGid(), gtype, eachBetMoney, wtype, side,
							bkPreOrderViewResp);
				}
			}
		}
	}

	public DailyBillResp getHistoryView(String date) {
		return RequestTool.getDailyBill(uid, date);
	}

	public void collectBetBill() {
		Calendar cal = Calendar.getInstance();

		for (int i = 0; i < dateBillInterval; i++) {
			cal.add(Calendar.DATE, -1);
			String date = DataFormat.DateToString(cal.getTime(), DataFormat.DATE_FORMAT);
			DailyBillResp dailyBillResp = getHistoryView(
					DataFormat.DateToString(cal.getTime(), DataFormat.DATE_FORMAT));
			if (dailyBillResp != null && CollectionUtils.isNotEmpty(dailyBillResp.getWagers())) {
				List<BetBill> billList = BeanCopy.copy(dailyBillResp.getWagers(), BetBill.class);
				betBillService.saveBill(this.account, date, billList);
			}
		}
	}

	public static void main(String[] args) {
		// System.out.println(System.currentTimeMillis());
		BallMember ballMember = new BallMember();
		ballMember.login("tansonLAM83", "523123ZX");
		LeagueResp leagueResp = ballMember.getLeaguesCount();
		log.info("联赛赛程：" + leagueResp.toString());
		// ballMember.playRollBasketball();
		// ballMember.playCurrentBasketball();
		// ballMember.playRollFootball();
		ballMember.playCurrentFootball();
		// DailyBillResp dailyBillResp =
		// ballMember.getHistoryView("2018-07-24");
		// log.info("账单：" + dailyBillResp.toString());
	}
}
