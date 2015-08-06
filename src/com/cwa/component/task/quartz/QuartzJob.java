package com.cwa.component.task.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.UnableToInterruptJobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cwa.component.task.ITask;
import com.cwa.component.task.ITaskConfig;
import com.cwa.component.task.ITaskTypeConfig;
import com.cwa.component.task.context.TaskContext;
import com.cwa.util.TimeUtil;

public class QuartzJob implements Job {
	private static Logger logger = LoggerFactory.getLogger(QuartzJob.class);

	public static final String TaskKey = "TaskKey";
	public static final String TaskContext = "TaskContext";

	private boolean _interrupted = false;
	private JobKey _jobKey = null;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		ITaskConfig taskConfig = null;
		try {
			_jobKey = context.getJobDetail().getKey();
			JobDataMap data = context.getJobDetail().getJobDataMap();

			ITask task = (ITask) data.get(TaskKey);

			TaskContext taskContext = (TaskContext) data.get(TaskContext);
			ITaskTypeConfig taskType = taskContext.getTaskType();

			if (taskType.overTime() > 0 && TimeUtil.currentSystemTime() >= taskType.overTime()) {
				// 结束时间大于0生效
				if (logger.isInfoEnabled()) {
					logger.info("任务时间到! _jobKey=" + _jobKey);
				}
				throw new OverTimeException();
			}

			taskConfig = taskContext.getConfig();
			if (_interrupted) {
				throw new InterruptedException();
			}
			// 执行任务
			task.execute(taskContext);
		} catch (InterruptedException e) {
			if (taskConfig.isInterruptCancle()) {
				JobExecutionException e2 = new JobExecutionException(e);
				// 取消所有的job不在运行
				e2.setUnscheduleAllTriggers(true);
				throw e2;
			}
		} catch (OverTimeException e) {
			// 取消所有的job不在运行
			JobExecutionException e2 = new JobExecutionException(e);
			e2.setUnscheduleAllTriggers(true);
			throw e2;
		} catch (Exception e) {
			logger.error("", e);
			if (taskConfig.isExceptionCancle()) {
				JobExecutionException e2 = new JobExecutionException(e);
				// 取消所有的job不在运行
				e2.setUnscheduleAllTriggers(true);
				throw e2;
			}
			// 这里会立即恢复，继续执行，如果这个不写会等到下次时间到了继续执行
			// e2.setRefireImmediately(true);
		}
	}

	public void interrupt() throws UnableToInterruptJobException {
		logger.info("中断操作 [" + _jobKey + "] ");
		_interrupted = true;
	}

	class OverTimeException extends Exception {
		private static final long serialVersionUID = 1L;
	}
}
