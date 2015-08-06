package com.cwa.component.datatimeout;

import com.cwa.component.task.ITask;

/**
 * 时间超时管理
 * 
 * @author mausmars
 *
 */
public interface IDataTimeoutManager extends ITask {
	public static final String DataTimeoutManagerKey = "DataTimeoutManager";

	/**
	 * 插入默认配置超时检查
	 * 
	 * @param key
	 * @param time
	 * @param obj
	 * @param callBlack
	 */
	void insertTimeoutCheck(String key, Object obj, IDataTimeoutCallBlack callBlack);

	/**
	 * 插入指定超时检查
	 * 
	 * @param key
	 * @param time
	 * @param obj
	 * @param callBlack
	 */
	void insertTimeoutCheck(String key, long time, Object obj, IDataTimeoutCallBlack callBlack);

	/**
	 * 移除超时检查
	 * 
	 * @param key
	 */
	void removeTimeoutCheck(String key);

	/**
	 * 重置默认配置时间
	 * 
	 * @param key
	 * @param time
	 */
	void resetTime(String key);

	/**
	 * 重置指定时间
	 * 
	 * @param key
	 * @param time
	 */
	void resetTime(String key, long time);
}
