package com.cwa.component.task.config;

import com.cwa.component.task.ITaskConfig;

public class TaskConfigAdapter implements ITaskConfig {
	public static TaskConfigAdapter DefaultTaskConfig = new TaskConfigAdapter();

	@Override
	public boolean isExceptionCancle() {
		return false;
	}

	@Override
	public boolean isInterruptCancle() {
		return false;
	}

	@Override
	public boolean isRequestRecovery() {
		return false;
	}

	@Override
	public int getPriority() {
		return ITaskConfig.NoPriority;
	}
}
