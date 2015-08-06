package com.cwa.component.task.context;

import com.cwa.component.task.ITaskConfig;
import com.cwa.component.task.ITaskContext;
import com.cwa.component.task.ITaskTypeConfig;

/**
 * 任务上下文
 * 
 * @author mausmars
 * 
 */
public class TaskContext implements ITaskContext{
	private ITaskConfig config;
	private ITaskTypeConfig taskType;

	public TaskContext(ITaskTypeConfig taskType, ITaskConfig config) {
		this.config = config;
		this.taskType = taskType;
	}

	public ITaskConfig getConfig() {
		return config;
	}

	public void setConfig(ITaskConfig config) {
		this.config = config;
	}

	public ITaskTypeConfig getTaskType() {
		return taskType;
	}

	public void setTaskType(ITaskTypeConfig taskType) {
		this.taskType = taskType;
	}
}
