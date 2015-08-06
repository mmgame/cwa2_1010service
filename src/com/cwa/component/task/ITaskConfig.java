package com.cwa.component.task;

/**
 * 任务配置
 * 
 * @author mausmars
 * 
 */
public interface ITaskConfig {
	public static final int NoPriority = -1;

	enum TaskConfigType {
		SimpleConfig, //
		CronConfig, //
		;
	}

	boolean isExceptionCancle(); // 是否异常终止

	boolean isInterruptCancle(); // 是否异常终止

	boolean isRequestRecovery(); // 执行中应用发生故障，需要重新执行

	int getPriority(); // 得到优先级
}
