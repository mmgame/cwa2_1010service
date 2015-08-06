package com.cwa.component.netlock;

/**
 * 锁管理接口
 * 
 * @author Administrator
 * 
 */
public interface INetLockManager {
	/**
	 * 创建锁
	 * 
	 * @param moduleName
	 * @return
	 */
	INetLock createLock(String moduleName);
}
