package com.ewing.order.ball.event;

import java.sql.Timestamp;
import java.util.EventListener;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewing.order.ball.dto.BetInfoDto;

public class BetInfoListener extends Thread implements EventListener {
	private static Logger log = LoggerFactory.getLogger(BetInfoListener.class);
	private ArrayBlockingQueue<BallEvent> eventQueue = new ArrayBlockingQueue<BallEvent>(100);
	private ExecutorService workThreads;
	private String account;
	private BetStrategyPool betStrategyPool;
	private WeakHashMap<String, Timestamp> gameMap = new WeakHashMap<>();

	public BetInfoListener(String account, BetStrategyPool betStrategyPool) {
		this.account = account;
		this.betStrategyPool = betStrategyPool;
		this.startListener();
	}

	public void startListener() {
		workThreads = Executors.newFixedThreadPool(2);
		this.start();
	}

	public void stopListener() {
		workThreads.shutdown();
		this.stop();
	}

	public String getAccount() {
		return account;
	}

	@Override
	public void run() {
		/*
		 * while (true) { try { log.info("eventQueue:"+eventQueue.size());
		 * if(eventQueue ==null){ TimeUnit.SECONDS.sleep(2l); continue; } final
		 * BallEvent ballEvent = eventQueue.poll(5, TimeUnit.SECONDS);
		 * workThreads.submit(new Runnable() {
		 * 
		 * @Override public void run() {
		 * betStrategyPool.runStratgeys(ballEvent); } }); } catch (Exception e)
		 * { log.error(e.getMessage(), e); } }
		 */
		while (true) {
			try {
				List<BetInfoDto> betList = betStrategyPool.getBetStrategyContext().getBetInfoList();
				for (BetInfoDto betInfoDto : betList) {
					String gid = betInfoDto.getGid();
					if (gameMap.get(gid) == null
							|| gameMap.get(gid).before(betInfoDto.getLastUpdate())) {
						BallEvent ballEvent = new BallEvent(betInfoDto.getGid(), betInfoDto);
						workThreads.submit(new Runnable() {
							@Override
							public void run() {
								betStrategyPool.runStratgeys(ballEvent);
							}
						});
						gameMap.put(new String(gid), betInfoDto.getLastUpdate());
					}
				}

			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	public void handleEvent(BallEvent event) {
		try {
			eventQueue.put(event);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
