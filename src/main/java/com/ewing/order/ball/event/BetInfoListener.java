package com.ewing.order.ball.event;

import java.util.EventListener;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewing.order.ball.logger.BetStrategyErrorLogger;

public class BetInfoListener extends Thread implements EventListener {
	private static Logger log = LoggerFactory.getLogger(BetInfoListener.class);
	private ArrayBlockingQueue<BallEvent> eventQueue = new ArrayBlockingQueue<BallEvent>(100);
	private String account;
	private BetStrategyPool betStrategyPool;
	private Boolean isAuto;
	public BetInfoListener(Boolean isAuto,String account, BetStrategyPool betStrategyPool) {
		this.isAuto = isAuto;
		this.account = account;
		this.betStrategyPool = betStrategyPool;
		this.startListener();
	}

	public void startListener() {
		this.start();
	}

	public void stopListener() {
		betStrategyPool.stop();
		this.stop();
	}

	public String getAccount() {
		return account;
	}

	@Override
	public void run() {
		while (true) {
			try {
				if(isAuto){
					betStrategyPool.runAutoStratgeys();
				}else{
					betStrategyPool.runHalfAutoStratgeys();
				}
			} catch (Exception e) {
				BetStrategyErrorLogger.logger.error(e.getMessage(), e);
			}
			try {
				TimeUnit.SECONDS.sleep(1);
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
