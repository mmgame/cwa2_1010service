package com.cwa.test.task;


import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cwa.component.task.ITask;
import com.cwa.component.task.ITaskContext;

public class TestTask implements ITask {
	private static Logger logger = LoggerFactory.getLogger(TestTask.class);

	private String taskId;

	public TestTask(String taskId) {
		this.taskId = taskId;
	}

	@Override
	public String id() {
		return taskId;
	}

	@Override
	public void execute(ITaskContext context) {
		if (logger.isInfoEnabled()) {
			logger.info("任务执行 TestTask [taskId=" + taskId + "] TaskContext " + context + " Date" + new Date());
		}
	}
}
