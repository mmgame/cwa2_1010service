package com.cwa.component.datatimeout;

import com.cwa.service.IService;

/**
 * 数据超时服务
 * 
 * @author mausmars
 *
 */
public interface IDataTimeoutService extends IService {
	/**
	 * 数据超时时间
	 * 
	 * @return
	 */
	int getTimeout();

	/**
	 * 检查表达式
	 * 
	 * @return
	 */
	String getExpression();

	/**
	 * 创建默认配置任务
	 * 
	 * @param id
	 * @param cornExpression
	 * @return
	 */
	DataTimeoutTask createTask(String id);

	/**
	 * 创建自定义任务
	 * 
	 * @param id
	 * @param cornExpression
	 * @return
	 */
	DataTimeoutTask createTask(String id, String expression);

	/**
	 * 移除任务
	 * 
	 * @param id
	 * @param cornExpression
	 * @return
	 */
	void removeTask(String taskId);
}
