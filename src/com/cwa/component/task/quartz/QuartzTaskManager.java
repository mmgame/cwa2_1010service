package com.cwa.component.task.quartz;

import java.util.Date;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cwa.component.task.ITask;
import com.cwa.component.task.ITaskConfig;
import com.cwa.component.task.ITaskManager;
import com.cwa.component.task.ITaskTypeConfig;
import com.cwa.component.task.config.TaskConfigAdapter;
import com.cwa.component.task.context.TaskContext;
import com.cwa.component.task.quartz.config.ICronTypeConfig;
import com.cwa.component.task.quartz.config.ISimpleTypeConfig;

public class QuartzTaskManager implements ITaskManager {
	enum State {
		Stop, //
		Stoping, //
		Running, //
		;
	}

	private static Logger logger = LoggerFactory.getLogger(QuartzTaskManager.class);

	private Scheduler sched;

	private String jobGroup = "Default_JobGroup";
	private String triggerGroup = "Default_TriggerGroup";

	private List<TaskWrap> taskWraps;

	private State state = State.Stop;

	public QuartzTaskManager() {
	}

	public QuartzTaskManager(String jobGroup, String triggerGroup) {
		this.jobGroup = jobGroup;
		this.triggerGroup = triggerGroup;
	}

	@Override
	public void startup() {
		SchedulerFactory sf = new StdSchedulerFactory();
		try {
			sched = sf.getScheduler();
			sched.start();
		} catch (SchedulerException e) {
			logger.error("Scheduler create is error!", e);
			System.exit(0);
		}
		state = State.Running;
		// 添加任务
		if (taskWraps != null) {
			for (TaskWrap taskWrap : taskWraps) {
				addTask(taskWrap.getTask(), taskWrap.getTaskType(), taskWrap.getConfig());
			}
		}
	}

	@Override
	public boolean addTask(ITask task, ITaskTypeConfig taskType) {
		if (state != State.Running) {
			return false;
		}
		return addTask(task, taskType, TaskConfigAdapter.DefaultTaskConfig);
	}

	@Override
	public boolean addTask(ITask task, ITaskTypeConfig taskType, ITaskConfig config) {
		if (state != State.Running) {
			return false;
		}
		try {
			if (config == null) {
				config = TaskConfigAdapter.DefaultTaskConfig;
			}

			Trigger trigger = createTrigger(task.id(), taskType);
			if (trigger == null) {
				return false;
			}
			TaskContext taskContext = new TaskContext(taskType, config);

			JobDetail job = JobBuilder.newJob(QuartzJob.class).withIdentity(task.id(), jobGroup).build();
			// 设置参数
			job.getJobDataMap().put(QuartzJob.TaskContext, taskContext);
			job.getJobDataMap().put(QuartzJob.TaskKey, task);

			Date startDate = sched.scheduleJob(job, trigger);
			// 手动启动
			// sched.triggerJob(JobKey.jobKey("job8", "group1"));

			// 打印日志
			if (trigger instanceof SimpleTrigger) {
				SimpleTrigger t = (SimpleTrigger) trigger;
//				if (logger.isInfoEnabled()) {
//					logger.info(job.getKey() + " 执行时间 at: " + startDate + " and 重复次数: " + t.getRepeatCount() + " times, 间隔时间: "
//							+ t.getRepeatInterval() / 1000 + " seconds");
//				}
			} else if (trigger instanceof CronTrigger) {
				CronTrigger t = (CronTrigger) trigger;
				if (logger.isInfoEnabled()) {
					logger.info(job.getKey() + " 执行时间 at: " + startDate + " and 表达式: " + t.getCronExpression());
				}
			}
			return true;
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
	}

	@Override
	public boolean changeTaskTime(String taskId, ITaskTypeConfig taskType) {
		if (state != State.Running) {
			return false;
		}
		try {
			Trigger trigger = createTrigger(taskId, taskType);
			// 可以重新设置时间
			Date startDate = sched.rescheduleJob(trigger.getKey(), trigger);
			if (logger.isInfoEnabled()) {
				logger.info("job7 rescheduled to run at: " + startDate);
			}

		} catch (Exception e) {
			return false;
		}
		return false;
	}

	@Override
	public void deleteTask(String taskId) {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(taskId, triggerGroup);
			sched.pauseTrigger(triggerKey);
			// 停止触发器
			sched.unscheduleJob(triggerKey);// 移除触发器
			sched.deleteJob(JobKey.jobKey(taskId, jobGroup));// 删除任务
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void shutdown() {
		try {
			state = State.Stoping;
			if (sched != null) {
				sched.shutdown(true);
			}
			state = State.Stop;
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	private Trigger createTrigger(String taskId, ITaskTypeConfig taskType) {
		TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger().withIdentity(taskId, triggerGroup);
		if (taskType.startTime() > 0) {
			// 开始时间，小于这个时间的都会开始
			triggerBuilder.startAt(new Date(taskType.startTime()));
		}
		if (taskType.overTime() > 0) {
			triggerBuilder.endAt(new Date(taskType.overTime()));
		}

		if (taskType instanceof ISimpleTypeConfig) {
			ISimpleTypeConfig simpleTypeTask = (ISimpleTypeConfig) taskType;
			SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
			if (simpleTypeTask.intervalTime() > 0) {
				// 时间间隔
				simpleScheduleBuilder.withIntervalInMilliseconds(simpleTypeTask.intervalTime());
			}
			if (simpleTypeTask.repeatCount() > 0) {
				// 重复次数
				simpleScheduleBuilder.withRepeatCount(simpleTypeTask.repeatCount());
			} else if (simpleTypeTask.repeatCount() < 0) {
				// 永远循环
				simpleScheduleBuilder.repeatForever();
			}
			triggerBuilder.withSchedule(simpleScheduleBuilder);
		} else if (taskType instanceof ICronTypeConfig) {
			ICronTypeConfig cronTypeTask = (ICronTypeConfig) taskType;
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronTypeTask.cornExpression());
			if (cronTypeTask.misfireHander() > 0) {
				if (cronTypeTask.misfireHander() == TaskMisFireTypeEnum.Cron_InstructionDoNothing.getValue()) {
					cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
				}
			}
			triggerBuilder.withSchedule(cronScheduleBuilder);
		} else {
			return null;
		}
		return triggerBuilder.build();
	}

	// ------------------------------------
	public void setTaskWraps(List<TaskWrap> taskWraps) {
		this.taskWraps = taskWraps;
	}
}
