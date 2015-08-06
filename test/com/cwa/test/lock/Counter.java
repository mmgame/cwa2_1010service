package com.cwa.test.lock;

/**
 * 计数器
 * 
 * @author Administrator
 * 
 */
public class Counter {
	private int i;

	public Counter(int count) {
		this.i = count;
	}

	public int increaseAndGet() {
		i++;
		return i;
	}
}
