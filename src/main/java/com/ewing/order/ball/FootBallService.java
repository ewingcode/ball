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

import com.ewing.order.ball.ft.game.FtGame;
import com.ewing.order.ball.ft.game.FtGameListResp;
import com.ewing.order.ball.ft.game.FtRollGame;
import com.ewing.order.ball.ft.game.FtRollGameListResp;
import com.ewing.order.ball.leaguelist.League;
import com.ewing.order.ball.leaguelist.LeagueListResp;
import com.google.common.collect.Lists;

/**
 *
 * @author tansonlam
 * @create 2018年7月24日
 */
public class FootBallService {
	private static Logger log = LoggerFactory.getLogger(FootBallService.class);

	private static ExecutorService receiveThreads;

	private static FootBallService footBallService = null;

	public static FootBallService getInstance() {
		if (footBallService == null) {
			synchronized (FootBallService.class) {
				if (footBallService == null)
					footBallService = new FootBallService();
			}
		}
		return footBallService;
	}

	private FootBallService() {
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

	public List<FtGame> collectCurrentFootball(final String uid) {
		List<FtGame> allList = Lists.newArrayList();
		final String gtype = "FT";// 篮球
		final String showTyp = "FT";// 未开始的比赛
		LeagueListResp leagueListResp = getLeagueList(uid, gtype, showTyp);
		if (CollectionUtils.isEmpty(leagueListResp.getGame().get(0).getLeague())) {
			log.info("暂时没有今天足球");
		} else {
			ArrayList<Future<FtGameListResp>> futureList = new ArrayList<>();
			for (final League league : leagueListResp.getGame().get(0).getLeague()) {
				//log.info("联赛：" + league.toString());
				Future<FtGameListResp> furture = receiveThreads
						.submit(new Callable<FtGameListResp>() {

							@Override
							public FtGameListResp call() throws Exception {
								return RequestTool.getFtGameList(uid, gtype, showTyp,
										league.getLeague_id());
							}
						});
				futureList.add(furture);
			}
			for (Future<FtGameListResp> f : futureList) {
				try {
					FtGameListResp gameListResp = f.get();
					//log.info("比赛:" + gameListResp);
					List<FtGame> gameList = gameListResp.getGame();
					if (CollectionUtils.isEmpty(gameList))
						continue;
					for(FtGame ftGame : gameList){
						ftGame.setSystime(gameListResp.getSystime());
					}
					allList.addAll(gameList);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			log.info("今天足球场数："+allList.size());

		}
		return allList;
	}

	public List<FtRollGame> collectRollingFootball(String uid) {
		List<FtRollGame> allList = Lists.newArrayList();
		//log.info("查询足球滚球：");
		String gtype = "FT";// 篮球
		String showTyp = "RB";// 未开始的比赛

		LeagueListResp leagueListResp = getLeagueList(uid, gtype, showTyp);
		if (CollectionUtils.isEmpty(leagueListResp.getGame().get(0).getLeague())) {
			log.info("暂时没有足球滚球");
		} else { 
			ArrayList<Future<FtRollGameListResp>> futureList = new ArrayList<>();
			for (League league : leagueListResp.getGame().get(0).getLeague()) {
			//	log.info("联赛：" + league.toString());
				Future<FtRollGameListResp> furture = receiveThreads
						.submit(new Callable<FtRollGameListResp>() {

							@Override
							public FtRollGameListResp call() throws Exception {
								return RequestTool.getFtRollGameList(uid, gtype, showTyp,
										league.getLeague_id());
							}
						});
				futureList.add(furture);
			}
			for (Future<FtRollGameListResp> f : futureList) {
				try {
					FtRollGameListResp gameListResp = f.get();
					//log.info("比赛:" + gameListResp);
					List<FtRollGame> gameList = gameListResp.getGame();
					if (CollectionUtils.isEmpty(gameList))
						continue;
					for(FtRollGame ftGame : gameList){
						ftGame.setSystime(gameListResp.getSystime());
					}
					allList.addAll(gameList);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			log.info("滚球足球场数："+allList.size());
		}
		return allList;
	}
}
