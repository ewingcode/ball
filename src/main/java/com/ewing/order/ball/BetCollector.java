package com.ewing.order.ball;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ewing.order.ball.bk.game.BkGame;
import com.ewing.order.ball.bk.game.BkRollGame;
import com.ewing.order.ball.dto.BetInfoDto;
import com.ewing.order.ball.event.BallEvent;
import com.ewing.order.ball.event.BallSource;
import com.ewing.order.ball.ft.game.FtGame;
import com.ewing.order.ball.ft.game.FtRollGame;
import com.ewing.order.ball.league.LeagueResp;
import com.ewing.order.ball.leaguelist.LeagueListResp;
import com.ewing.order.ball.login.LoginResp;
import com.ewing.order.ball.util.RequestTool;
import com.ewing.order.busi.ball.ddl.BetInfo;
import com.ewing.order.busi.ball.ddl.BetRollInfo;
import com.ewing.order.busi.ball.service.BetInfoService;
import com.ewing.order.busi.ball.service.BetRollInfoService;
import com.ewing.order.common.prop.BallmatchProp;
import com.ewing.order.util.BeanCopy;
import com.ewing.order.util.DataFormat;
import com.google.common.collect.Lists;

/**
 *
 * @author tansonlam
 * @create 2018年7月20日
 */
@Component
@EnableScheduling
public class BetCollector {
	private static Logger log = LoggerFactory.getLogger(BetCollector.class);
	private LoginResp loginResp;
	private String uid;
	private String user;
	private String pwd;
	@Resource
	private BetInfoService betInfoService;
	@Resource
	private BetRollInfoService betRollInfoService;

	private BasketBallService basketBalService;

	private FootBallService footBallService;

	public static AtomicBoolean istartCollect = new AtomicBoolean(false);

	private AtomicInteger reLoginTime = new AtomicInteger(0);

	private final static long todayDataCollectTime = 120 * 1000;

	private final static long rollDataCollectTime = 10 * 1000;

	private final static long heartbeatTime = 30 * 1000;
	
	private Lock uidLock = new ReentrantLock(); 

	public static class CollectDataPool {
		public static List<BetInfoDto> bkRollList = Lists.newArrayList();
		private static List<BetInfoDto> bkTodayList = Lists.newArrayList();
		private static List<BetInfoDto> ftRollList = Lists.newArrayList();
		private static List<BetInfoDto> ftTodayList = Lists.newArrayList();
		private static WeakHashMap<String, List<BetRollInfo>> bkRollDetailMap = new WeakHashMap<>();

		public static void putRollDetail(BetRollInfo betRollInfo) {
			List<BetRollInfo> list = bkRollDetailMap.get(betRollInfo.getGid());
			if (CollectionUtils.isEmpty(list)) {
				list = new CopyOnWriteArrayList<>();
				List<BetRollInfo> exitsList = bkRollDetailMap.putIfAbsent(betRollInfo.getGid(),
						list);
				if (exitsList != null) {
					list = exitsList;
				}
			}
			list.add(betRollInfo);
		}

		public static List<BetRollInfo> getRollDetail(String gid, int len) {
			List<BetRollInfo> result = Lists.newArrayList();
			List<BetRollInfo> list = bkRollDetailMap.get(gid);
			if (CollectionUtils.isEmpty(list))
				return null;
			len = list.size() < len ? list.size() : len;
			result.addAll(list.subList(list.size() - len, list.size()));
			return result;
		}

		private static String toFix2Num(String iorratio) {
			if (StringUtils.isEmpty(iorratio)) {
				return "0";
			} else {
				return String.valueOf(Math.floor(Float.valueOf(iorratio) * 100f) / 100f);
			}
		}

		private static String updateMatchTime(String systime, String matchTime) {
			return DataFormat.addHour(matchTime, DataFormat.getDiffHourWithCurrent(systime));

		}

		private static List<BetInfoDto> sort(List<BetInfoDto> dtoList) {
			for (BetInfoDto betInfoDto : dtoList) {
				betInfoDto.setIor_OUC(toFix2Num(betInfoDto.getIor_OUC()));
				betInfoDto.setIor_OUH(toFix2Num(betInfoDto.getIor_OUH()));
				betInfoDto.setIor_RC(toFix2Num(betInfoDto.getIor_RC()));
				betInfoDto.setIor_RH(toFix2Num(betInfoDto.getIor_RH()));
				betInfoDto.setN_ior_RH(toFix2Num(betInfoDto.getN_ior_RH()));
				betInfoDto.setN_ior_RC(toFix2Num(betInfoDto.getN_ior_RC()));
				betInfoDto.setN_ior_OUC(toFix2Num(betInfoDto.getN_ior_OUC()));
				betInfoDto.setN_ior_OUH(toFix2Num(betInfoDto.getN_ior_OUH()));
				betInfoDto.setMatchtime(
						updateMatchTime(betInfoDto.getSystime(), betInfoDto.getDatetime()));
			}
			Collections.sort(dtoList, new Comparator<BetInfoDto>() {
				@Override
				public int compare(BetInfoDto o1, BetInfoDto o2) {
					try {
						String radio1 = !StringUtils.isEmpty(o1.getRatio()) ? o1.getRatio() : "0";
						String radio2 = !StringUtils.isEmpty(o2.getRatio()) ? o2.getRatio() : "0";
						return Float.valueOf(radio1).compareTo(Float.valueOf(radio2));
					} catch (NumberFormatException e) {
						log.error(e.getMessage(), e);
						return 0;
					}
				}

			});
			return dtoList;
		}

		public static List<BetInfoDto> getBkRollList() {
			return  bkRollList ;
		}

		public static List<BetInfoDto> getBkTodayList() {
			return  bkTodayList ;
		}

		public static List<BetInfoDto> getFtRollList() {
			return ftRollList;
		}

		public static List<BetInfoDto> getFtTodayList() {
			return ftTodayList;
		}

	}
 

	@Scheduled(cron = "0 */2 * * * * ")
	public void changeUid() {
		if (istartCollect.get()) {
			loginResp = RequestTool.login(user, pwd);
			this.uid = loginResp.getUid();
			log.info("change uid for collector:" + this.uid);
		}
	}

	@Scheduled(cron = "*/10 * * * * * ")
	public void init() {
		if (istartCollect.get() || !BallmatchProp.allowcollect)
			return;
		istartCollect.set(true);
		try {
			if (login(BallmatchProp.getAccount(), BallmatchProp.getPwd())) {
				// startCollectFootballInfo();
				startCollectBasketInfo();
			}else{
				istartCollect.set(false);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			istartCollect.set(false);
		}
	 
	}

	public Boolean login(String user, String pwd) {
		log.info("login for user:" + user );
		this.user = user;
		this.pwd = pwd;
		loginResp = RequestTool.login(user, pwd); 
		this.uid = loginResp.getUid();
		// memberResp = RequestTool.memberSet(loginResp.getUid());
		if (StringUtils.isEmpty(uid)) {
			log.info("login fail.");
			return false;
		} else {
			log.info("login successfully.");
			basketBalService = BasketBallService.getInstance();
			footBallService = FootBallService.getInstance();
			startHeartBeat();
			return true;
		}
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
					// getLeaguesCount();
				} catch (Exception e) {
					// relogin(e);
					log.error(e.getMessage(), e);
				}
			}
		}, 1000, heartbeatTime);
	}

	public void relogin(Exception e) {
		if (e != null && !StringUtils.isEmpty(e.getMessage())
				&& e.getMessage().indexOf(RequestTool.ErrorCode.doubleLogin) > -1) {
			if (reLoginTime.incrementAndGet() < 2) {
				loginResp = RequestTool.login(user, pwd);
				this.uid = loginResp.getUid();
				if (!StringUtils.isEmpty(loginResp.getUid())) {
					reLoginTime.set(0);
				} else {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e1) {
						log.error(e1.getMessage(), e1);
					}
				}
			}
		}
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

	public void startCollectBasketInfo() {
		Timer timer1 = new Timer();
		timer1.schedule(new TimerTask() {
			public void run() {
				log.info("收集当天的篮球信息：");
				try {
					collectCurrentBasketball();
				} catch (Throwable e) {
					log.error("收集当天的篮球失败:"+e.getMessage(), e);
				}
			}
		}, 1000, todayDataCollectTime);

		Timer timer2 = new Timer();
		timer2.schedule(new TimerTask() {
			public void run() {
				 log.info("收集滚球的篮球信息：");
				try {
					collectRollingBasketball();
				} catch (Throwable e) {
					log.error("收集滚球的篮球失败:"+e.getMessage(), e);
				}
			}
		}, 1000, rollDataCollectTime);
	}

	public void startCollectFootballInfo() {
		Timer timer1 = new Timer();
		timer1.schedule(new TimerTask() {
			public void run() {
				try {
					collectCurrentFootball();
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}, 1000, todayDataCollectTime);

		Timer timer2 = new Timer();
		timer2.schedule(new TimerTask() {
			public void run() {
				try {
					collectRollingFootball();
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}, 1000, rollDataCollectTime);
	}

	public void collectCurrentBasketball() {
		// log.info("收集当天的篮球信息：");
		List<BkGame> gameList = basketBalService.collectCurrentBasketball(uid);
		BallSource ballSource = BallSource.getBKCurrent();
		for (BkGame bkGame : gameList) {
			ballSource.receiveBallEvent(new BallEvent(bkGame.getGid(), bkGame));
		}
		List<BetInfo> entityList = BeanCopy.copy(gameList, BetInfo.class);
		betInfoService.updateReadyBetInfo(entityList);
		CollectDataPool.bkTodayList = BeanCopy.copy(entityList, BetInfoDto.class); 
	}

	private void addTestGame(String gid) {
		BetInfo betInfo = betInfoService.findByGameId(gid);
		if (betInfo == null)
			return;
		BetInfoDto betInfoDto = new BetInfoDto();
		BeanCopy.copy(betInfoDto, betInfo, true);
		CollectDataPool.bkRollList.add(betInfoDto);
	}

	private BetRollInfo addBkRollTestGame(Integer id) {
		return betRollInfoService.findById(id);
	}

	public void collectRollingBasketball() {
		// log.info("收集当天篮球滚球信息：");
		List<BkRollGame> rollGameList = basketBalService.collectRollingBasketball(uid);
		List<BetRollInfo> entityList = BeanCopy.copy(rollGameList, BetRollInfo.class);
		List<BetInfo> betInfoList = betRollInfoService.updateByRoll(entityList);
		CollectDataPool.bkRollList = betRollInfoService.fillMaxMinInfo(betInfoList);
		CollectDataPool.sort(CollectDataPool.bkRollList);
		for(BetRollInfo betRollInfo : entityList){
			if(betRollInfo.getId()!=null){
				CollectDataPool.putRollDetail(betRollInfo);
			}
		}
		// addTestGame("2590702");
	}

	public void collectCurrentFootball() {
		// log.info("收集当天的足球信息：");
		List<FtGame> gameList = footBallService.collectCurrentFootball(uid);
		BallSource ballSource = BallSource.getFTCurrent();
		for (FtGame ftGame : gameList) {
			ballSource.receiveBallEvent(new BallEvent(ftGame.getGid(), ftGame));
		}
		List<BetInfo> entityList = BeanCopy.copy(gameList, BetInfo.class);
		betInfoService.updateReadyBetInfo(entityList);
		CollectDataPool.ftTodayList = BeanCopy.copy(entityList, BetInfoDto.class);
	}

	public void collectRollingFootball() {
		// log.info("收集当天足球滚球信息：");
		List<FtRollGame> rollGameList = footBallService.collectRollingFootball(uid);
		List<BetRollInfo> entityList = BeanCopy.copy(rollGameList, BetRollInfo.class);
		List<BetInfo> betInfoList = betRollInfoService.updateByRoll(entityList);
		CollectDataPool.ftTodayList = BeanCopy.copy(betInfoList, BetInfoDto.class);
	}

}
