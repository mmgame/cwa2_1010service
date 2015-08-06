package com.cwa.test.lock;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cwa.component.netlock.INetLock;

public class TestTask implements Runnable {
	protected static final Logger logger = LoggerFactory.getLogger(TestTask.class);

	private Counter counter;
	private INetLock lock;
	private CountDownLatch latch;

	private List<Integer> counts = new LinkedList<Integer>();

	public TestTask(Counter counter, INetLock lock, CountDownLatch latch) {
		this.counter = counter;
		this.lock = lock;
		this.latch = latch;
	}

	@Override
	public void run() {
		for (int i = 1; i <= 15; i++) {
			try {
				lock.lock();
				int count = counter.increaseAndGet();
				// Thread.sleep(1000);
				counts.add(count);
			} catch (Exception e) {
				logger.error("", e);
			} finally {
				lock.unlock();
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("Task is over! threadName=" + Thread.currentThread().getName());
		}
		latch.countDown();
	}

	public List<Integer> getCounts() {
		return counts;
	}
}
