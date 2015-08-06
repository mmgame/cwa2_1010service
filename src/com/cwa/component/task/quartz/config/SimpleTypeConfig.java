package com.cwa.component.task.quartz.config;


/**
 * 简单任务类型
 * 
 * @author mausmars
 * 
 */
public class SimpleTypeConfig implements ISimpleTypeConfig {
	private long startTime;
	private long overTime;
	private int repeatCount;
	private int intervalTime;

	@Override
	public long startTime() {
		return startTime;
	}

	@Override
	public long overTime() {
		return overTime;
	}

	@Override
	public int repeatCount() {
		return repeatCount;
	}

	@Override
	public int intervalTime() {
		return intervalTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public void setOverTime(long overTime) {
		this.overTime = overTime;
	}

	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}

	public void setIntervalTime(int intervalTime) {
		this.intervalTime = intervalTime;
	}
}
