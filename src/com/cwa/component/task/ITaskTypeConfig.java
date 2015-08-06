package com.cwa.component.task;

/**
 * 任务类型配置
 * 
 * @author mausmars
 * 
 */
public interface ITaskTypeConfig {
	/**
	 * 开始时间
	 * 
	 * @return
	 */
	long startTime();

	/**
	 * 结束时间
	 * 
	 * @return
	 */
	long overTime();
}
