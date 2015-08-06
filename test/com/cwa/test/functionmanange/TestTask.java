package com.cwa.test.functionmanange;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cwa.component.functionmanage.FunctionService;

import baseice.service.FunctionMenu;

public class TestTask implements Runnable {
	protected static final Logger logger = LoggerFactory.getLogger(TestTask.class);

	private FunctionService sm;

	private CountDownLatch latch;

	private FunctionMenu functionMenu;

	public TestTask(FunctionMenu functionMenu, FunctionService sm, CountDownLatch latch) {
		this.functionMenu = functionMenu;
		this.sm = sm;
		this.latch = latch;
	}

	@Override
	public void run() {
		if (functionMenu.fid.fkey % 6 == 1) {
			sm.unregister(functionMenu.fid);
			logger.info("unregister sid=" + functionMenu.fid);
		}
		sleep(1000);
		if (logger.isInfoEnabled()) {
			logger.info("Task is over! threadName=" + Thread.currentThread().getName());
		}
		latch.countDown();
	}

	private void sleep(int time) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
