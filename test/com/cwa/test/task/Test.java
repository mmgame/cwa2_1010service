package com.cwa.test.task;

import org.apache.log4j.xml.DOMConfigurator;

import com.cwa.component.task.ITaskTypeConfig;
import com.cwa.component.task.quartz.QuartzTaskManager;
import com.cwa.component.task.quartz.config.SimpleTypeConfig;
import com.cwa.component.task.quartz.config.TaskTypeConfigFactory;

public class Test {
	public static void main(String[] args) throws InterruptedException {
		DOMConfigurator.configureAndWatch("propertiesconfig/log4j.xml");

		TaskTypeConfigFactory factory = new TaskTypeConfigFactory();

		QuartzTaskManager taskManager = new QuartzTaskManager();
		taskManager.startup();

		// SimpleTypeTask task1 = new SimpleTypeTask("2013-08-15 20:30:00",
		// "2013-08-15 20:59:00");
		// taskManager.addTask(new TestTask("task1"), task1);

		// SimpleTypeTask task2 = new SimpleTypeTask("2013-08-15 20:30:00",
		// "2013-08-15 20:59:00", -1, 10000);
		// taskManager.addTask(new TestTask("task2"), task2);

		ITaskTypeConfig task4 = factory.createSimpleTypeConfig(-1, 5000, 0, 0);
		taskManager.addTask(new TestTask("task4"), task4);
		taskManager.deleteTask("task4");
		SimpleTypeConfig task5 = new SimpleTypeConfig();
		taskManager.addTask(new TestTask("task4"), task5);

		// CronTypeTask task3 = new CronTypeTask("2013-08-15 20:30:00",
		// "2013-08-15 20:59:00", "0/20 * * * * ?");
		// taskManager.addTask(new TestTask("task3"), task3);

		ITaskTypeConfig task6 = factory.createCronTypeConfig(0, 0, "0/20 * * * * ?", 0);
		taskManager.addTask(new TestTask("task6"), task6);

		ITaskTypeConfig task7 = factory.createCronTypeConfig(0, 0, "0/20 * * * * ?", 0);
		taskManager.addTask(new TestTask("task7"), task7);

		Thread.sleep(10000);
		System.out.println("changeTaskTime");
		taskManager.changeTaskTime("task4", task5);
		System.out.println("deleteTask");
		taskManager.deleteTask("task6");
		System.out.println("shutdown");
		taskManager.shutdown();
	}
}
