package com.ewing.order.ball;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewing.order.ball.bk.game.BkGame;
import com.ewing.order.ball.bk.game.BkGameListResp;
import com.ewing.order.ball.bk.game.BkRollGame;
import com.ewing.order.ball.bk.game.BkRollGameListResp;
import com.ewing.order.ball.leaguelist.League;
import com.ewing.order.ball.leaguelist.LeagueListResp;
import com.google.common.collect.Lists;

public class BasketBallService {
	private static Logger log = LoggerFactory.getLogger(BasketBallService.class);

	private static ExecutorService receiveThreads;

	private static BasketBallService basketBallService = null;

	public static BasketBallService getInstance() {
		if (basketBallService == null) {
			synchronized (BasketBallService.class) {
				if (basketBallService == null)
					basketBallService = new BasketBallService();
			}
		}
		return basketBallService;
	}

	private BasketBallService() {
		receiveThreads = Executors.newFixedThreadPool(3);
	}

	/**
	 * 获取指定类型球类的联赛数
	 * 
	 * @param gtype
	 * @param showType
	 * @return
	 */
	public LeagueListResp getLeagueList(String uid, String gtype, String showType) {
		return RequestTool.getLeagueList(uid, gtype, showType);
	}

	public List<BkGame> collectCurrentBasketball(final String uid) {
		List<BkGame> allList = Lists.newArrayList();
		//log.info("查询今天篮球：");
		final String gtype = "BK";// 篮球
		final String showTyp = "FT";// 未开始的比赛
		LeagueListResp leagueListResp = getLeagueList(uid, gtype, showTyp);
		if (CollectionUtils.isEmpty(leagueListResp.getGame().get(0).getLeague())) {
			log.info("暂时没有今天篮球");
		} else {
			ArrayList<Future<BkGameListResp>> futureList = new ArrayList<>();
			for (final League league : leagueListResp.getGame().get(0).getLeague()) {
				//log.info("联赛：" + league.toString());
				Future<BkGameListResp> furture = receiveThreads
						.submit(new Callable<BkGameListResp>() {

							@Override
							public BkGameListResp call() throws Exception {
								return RequestTool.getBkGameList(uid, gtype, showTyp,
										league.getLeague_id());
							}
						});
				futureList.add(furture);
			}
			for (Future<BkGameListResp> f : futureList) {
				try {
					BkGameListResp gameListResp = f.get();
					//log.info("投注项：" + gameListResp);
					List<BkGame> gameList = gameListResp.getGame();
					if (CollectionUtils.isEmpty(gameList))
						continue;
					for (BkGame ftGame : gameList) {
						ftGame.setSystime(gameListResp.getSystime());
					}
					allList.addAll(gameList);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			log.info("今天篮球场数："+allList.size());
		}
		return allList;
	}

	public List<BkRollGame> collectRollingBasketball(String uid) {
		List<BkRollGame> allList = Lists.newArrayList();
		// log.info("查询篮球滚球：");
		String gtype = "BK";// 篮球
		String showTyp = "RB";// 未开始的比赛
		LeagueListResp leagueListResp = getLeagueList(uid, gtype, showTyp);
		if (CollectionUtils.isEmpty(leagueListResp.getGame().get(0).getLeague())) {
			 log.info("暂时没有滚球篮球");
		} else {

			ArrayList<Future<BkRollGameListResp>> futureList = new ArrayList<>();
			for (League league : leagueListResp.getGame().get(0).getLeague()) {
				//log.info("联赛：" + league.toString());
				Future<BkRollGameListResp> furture = receiveThreads
						.submit(new Callable<BkRollGameListResp>() {

							@Override
							public BkRollGameListResp call() throws Exception {
								return RequestTool.getBkRollGameList(uid, gtype, showTyp,
										league.getLeague_id());
							}
						});
				futureList.add(furture);
			}
			for (Future<BkRollGameListResp> f : futureList) {
				try {
					BkRollGameListResp gameListResp = f.get();
					//log.info("投注项：" + gameListResp);
					List<BkRollGame> gameList = gameListResp.getGame();
					if (CollectionUtils.isEmpty(gameList))
						continue;
					for (BkRollGame ftGame : gameList) {
						ftGame.setSystime(gameListResp.getSystime());
					}
					allList.addAll(gameList);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			log.info("滚球篮球场数："+allList.size());
		}
		return allList;
	}
}
