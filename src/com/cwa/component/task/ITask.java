package com.cwa.component.task;

public interface ITask {
	String id(); // 任务id

	void execute(ITaskContext context); // 任务执行
}
