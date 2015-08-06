package com.cwa.component.task.quartz.config;

import com.cwa.component.task.ITaskTypeConfig;

public class TaskTypeConfigFactory implements ITaskTypeConfigFactory {

	@Override
	public ITaskTypeConfig createCronTypeConfig(long startTime, long overTime, String cornExpression, int misfireHander) {
		CronTypeConfig config=new CronTypeConfig();
		config.setStartTime(startTime);
		config.setOverTime(overTime);
		config.setCornExpression(cornExpression);
		config.setMisfireHander(misfireHander);
		return config;
	}

	@Override
	public ITaskTypeConfig createSimpleTypeConfig(long startTime, long overTime, int repeatCount, int intervalTime) {
		SimpleTypeConfig config=new SimpleTypeConfig();
		config.setStartTime(startTime);
		config.setOverTime(overTime);
		config.setRepeatCount(repeatCount);
		config.setIntervalTime(intervalTime);
		return config;
	}

}
