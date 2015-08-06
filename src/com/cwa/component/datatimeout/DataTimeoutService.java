package com.cwa.component.datatimeout;

import serverice.config.ServiceConfigTypeEnum;

import com.cwa.component.task.ITaskTypeConfig;
import com.cwa.component.task.quartz.QuartzTaskManager;
import com.cwa.component.task.quartz.config.TaskTypeConfigFactory;

public class DataTimeoutService implements IDataTimeoutService {
	private String key;
	private int version;

	private String expression;
	private int timeout;

	private TaskTypeConfigFactory factory = new TaskTypeConfigFactory();
	private QuartzTaskManager taskManager = new QuartzTaskManager();

	public DataTimeoutService(String key) {
		this.key = key;
	}

	public DataTimeoutTask createTask(String id) {
		return createTask(id, expression);
	}

	@Override
	public DataTimeoutTask createTask(String id, String expression) {
		DataTimeoutTask task = new DataTimeoutTask(id);
		task.setTimeout(timeout);
		ITaskTypeConfig taskConfig = factory.createCronTypeConfig(0, 0, expression, 0);
		taskManager.addTask(task, taskConfig);
		return task;
	}

	@Override
	public void removeTask(String taskId) {
		taskManager.deleteTask(taskId);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public int getServiceType() {
		return ServiceConfigTypeEnum.DataTimeout.value();
	}

	@Override
	public void startup() throws Exception {
		taskManager.startup();
	}

	@Override
	public void shutdown() throws Exception {
		taskManager.shutdown();
	}

	@Override
	public String getExpression() {
		return expression;
	}

	@Override
	public int getTimeout() {
		return timeout;
	}

	// ------------------------------------------------------
	public void setExpression(String expression) {
		this.expression = expression;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
