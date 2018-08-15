package com.ewing.order.ball.event;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {
	static ReentrantLock lock = new ReentrantLock();

	public static void main(String[] args) {
		Thread thread1 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					doPrint("thread 1 try get lock.");
					do123();
					doPrint("thread 1 end.");

				} catch (InterruptedException e) {
					doPrint("thread 1 is interrupted.");
				}
			}
		});

		Thread thread2 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					doPrint("thread 2 try get lock.");
					do123();
					doPrint("thread 2 end.");
				} catch (InterruptedException e) {
					doPrint("thread 2 is interrupted.");
				}
			}
		});

		thread1.setName("thread1");
		thread2.setName("thread2");
		thread1.start();
		try {
			Thread.sleep(100);// 等待一会使得thread1会在thread2前面执行
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		thread2.start();
	}

	private static void do123() throws InterruptedException {

		if (lock.tryLock(10, TimeUnit.SECONDS)) {
			doPrint(Thread.currentThread().getName() + " is locked.");
			try {
				doPrint(Thread.currentThread().getName() + " doSoming1....");
				Thread.sleep(30000);// 等待几秒方便查看线程的先后顺序
				doPrint(Thread.currentThread().getName() + " doSoming2....");

				doPrint(Thread.currentThread().getName() + " is finished.");
			} finally {
				lock.unlock();
			}
		}
	}

	private static void doPrint(String text) {
		System.out.println((new Date()).toLocaleString() + " : " + text);
	}
}