package com.cwa.component.task;


public interface ITaskManager {
	/**
	 * 启动任务管理
	 */
	void startup();

	/**
	 * 停止任务管理
	 */
	void shutdown();

	/**
	 * 添加任务
	 * 
	 * @param task
	 * @param taskType
	 * @return
	 */
	boolean addTask(ITask task, ITaskTypeConfig taskType);

	/**
	 * 添加任务
	 * 
	 * @param task
	 * @param taskType
	 * @param config
	 * @return
	 */
	boolean addTask(ITask task, ITaskTypeConfig taskType, ITaskConfig config);

	/**
	 * 改变时间
	 * 
	 * @param taskId
	 * @param taskType
	 * @return
	 */
	boolean changeTaskTime(String taskId, ITaskTypeConfig taskType);

	/**
	 * 删除任务
	 * 
	 * @param taskId
	 */
	void deleteTask(String taskId);

}
