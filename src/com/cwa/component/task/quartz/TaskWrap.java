package com.cwa.component.task.quartz;

import com.cwa.component.task.ITask;
import com.cwa.component.task.ITaskConfig;
import com.cwa.component.task.ITaskTypeConfig;

/**
 * 任务包装
 * 
 * @author mausmars
 * 
 */
public class TaskWrap {
	private ITask task;
	private ITaskTypeConfig taskType;
	private ITaskConfig config;

	public ITask getTask() {
		return task;
	}

	public void setTask(ITask task) {
		this.task = task;
	}

	public ITaskTypeConfig getTaskType() {
		return taskType;
	}

	public void setTaskType(ITaskTypeConfig taskType) {
		this.taskType = taskType;
	}

	public ITaskConfig getConfig() {
		return config;
	}

	public void setConfig(ITaskConfig config) {
		this.config = config;
	}
}
