package com.cwa.component.task.quartz.config;


/**
 * 时钟任务类型
 * 
 * @author mausmars
 * 
 */
public class CronTypeConfig implements ICronTypeConfig {
	private long startTime;
	private long overTime;
	private String cornExpression;
	private int misfireHander;

	@Override
	public long startTime() {
		return startTime;
	}

	@Override
	public long overTime() {
		return overTime;
	}

	@Override
	public String cornExpression() {
		return cornExpression;
	}

	@Override
	public int misfireHander() {
		return misfireHander;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public void setOverTime(long overTime) {
		this.overTime = overTime;
	}

	public void setCornExpression(String cornExpression) {
		this.cornExpression = cornExpression;
	}

	public void setMisfireHander(int misfireHander) {
		this.misfireHander = misfireHander;
	}
}
