package com.ewing.order.core.redis;

/**
 * 分布式锁返回结果
 * 
 * @author tanson lam
 * @create 2016年10月9日
 */
public class DistributedLockResp {
	/**
	 * 是否获取锁
	 */
	private boolean hasLock;
	/**
	 * 尝试获取锁的总次数
	 */
	private int tryTotalTime;
	/**
	 * 锁名称
	 */
	private String lockKey;

	public DistributedLockResp(String lockKey, boolean hasLock, int tryTotalTime) {
		super();
		this.hasLock = hasLock;
		this.tryTotalTime = tryTotalTime;
		this.lockKey = lockKey;
	}

	public boolean isHasLock() {
		return hasLock;
	}

	public void setHasLock(boolean hasLock) {
		this.hasLock = hasLock;
	}

	public int getTryTotalTime() {
		return tryTotalTime;
	}

	public void setTryTotalTime(int tryTotalTime) {
		this.tryTotalTime = tryTotalTime;
	}

	public String getLockKey() {
		return lockKey;
	}

	public void setLockKey(String lockKey) {
		this.lockKey = lockKey;
	}

	public static Boolean isSuccessLocked(
			DistributedLockResp distributedLockResp) {
		if (distributedLockResp == null || !distributedLockResp.isHasLock())
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DistributedLockResp [hasLock=" + hasLock + ", tryTotalTime="
				+ tryTotalTime + ", lockKey=" + lockKey + "]";
	}

}
