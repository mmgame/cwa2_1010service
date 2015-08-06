package com.cwa.component.task.quartz.config;

import com.cwa.component.task.ITaskTypeConfig;

public interface ISimpleTypeConfig extends ITaskTypeConfig {
	int repeatCount(); // 重复次数

	int intervalTime(); // 间隔时间(ms)
};
