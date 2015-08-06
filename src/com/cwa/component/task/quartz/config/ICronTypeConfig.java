package com.cwa.component.task.quartz.config;

import com.cwa.component.task.ITaskTypeConfig;

public interface ICronTypeConfig extends ITaskTypeConfig {
	String cornExpression(); // 表达式

	int misfireHander();
};