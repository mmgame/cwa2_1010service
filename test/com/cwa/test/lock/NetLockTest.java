package com.cwa.test.lock;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cwa.component.netlock.INetLock;
import com.cwa.component.netlock.NetLockManagerZK;

public class NetLockTest {
	protected static final Logger logger = LoggerFactory.getLogger(NetLockTest.class);
	private String hosts = "172.16.0.145:2181,172.16.0.141:2182";

	public void test() throws IOException {
		Counter counter = new Counter(0);
		int size = 5;
		CountDownLatch latch = new CountDownLatch(size);

		NetLockManagerZK lockManager = new NetLockManagerZK(hosts);
		lockManager.init();
		INetLock local = lockManager.createLock("test_1");

		List<TestTask> testTasks = new LinkedList<TestTask>();
		List<Thread> tasks = new LinkedList<Thread>();
		for (int i = 1; i <= size; i++) {
			TestTask task = new TestTask(counter, local, latch);
			Thread thread = new Thread(task);
			thread.setName("test_" + i);
			tasks.add(thread);
			testTasks.add(task);
		}
		for (Thread task : tasks) {
			task.start();
		}
		try {
			// 等待
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (logger.isInfoEnabled()) {
			logger.info("repeat num check start!");
		}
		Set<Integer> counts = new HashSet<Integer>();
		for (TestTask testTask : testTasks) {
			for (Integer count : testTask.getCounts()) {
				if (counts.contains(count)) {
					logger.info("repeat num=" + count);
				} else {
					counts.add(count);
				}
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("repeat num check over!");
		}
	}

	public static void main(String[] args) throws IOException {
		DOMConfigurator.configureAndWatch("propertiesconfig/log4j.xml");

		NetLockTest componentTest = new NetLockTest();
		componentTest.test();
	}
}
