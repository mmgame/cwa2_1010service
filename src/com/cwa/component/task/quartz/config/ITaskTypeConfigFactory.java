package com.cwa.component.task.quartz.config;

import com.cwa.component.task.ITaskTypeConfig;

public interface ITaskTypeConfigFactory {
	ITaskTypeConfig createCronTypeConfig(long startTime, long overTime, String cornExpression, int misfireHander);

	ITaskTypeConfig createSimpleTypeConfig(long startTime, long overTime, int repeatCount, int intervalTime);
}
