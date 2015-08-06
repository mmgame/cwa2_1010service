package com.cwa.component.netlock;

/**
 * 锁接口
 * 
 * @author Administrator
 * 
 */
public interface INetLock {
	/**
	 * 尝试获取锁
	 * 
	 * @return
	 */
	boolean tryLock();

	/**
	 * 锁
	 */
	void lock();

	/**
	 * 解锁
	 */
	void unlock();
}
