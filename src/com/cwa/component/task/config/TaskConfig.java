package com.cwa.component.task.config;

public class TaskConfig {
	/**
	 * 异常退出
	 */
	private boolean isExceptionCancle;
	/**
	 * 中断退出
	 */
	private boolean isInterruptCancle;
	/**
	 * 请求恢复
	 */
	private boolean isRequestRecovery;
	/**
	 * 优先级
	 */
	private int getPriority;

	public TaskConfig(boolean isExceptionCancle, boolean isInterruptCancle, boolean isRequestRecovery, int getPriority) {
		this.getPriority = getPriority;
		this.isExceptionCancle = isExceptionCancle;
		this.isInterruptCancle = isInterruptCancle;
		this.isRequestRecovery = isRequestRecovery;
	}

	public boolean isExceptionCancle() {
		return isExceptionCancle;
	}

	public boolean isInterruptCancle() {
		return isInterruptCancle;
	}

	public boolean isRequestRecovery() {
		return isRequestRecovery;
	}

	public int getPriority() {
		return getPriority;
	}
}
