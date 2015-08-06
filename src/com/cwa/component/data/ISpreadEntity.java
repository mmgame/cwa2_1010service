package com.cwa.component.data;

public interface ISpreadEntity {
	/**
	 * 取数据后;
	 * 1.执行数据的转换
	 * 2.如果有索引变更，要清空changeIndexs
	 */
	void obtainAfter();

	/**
	 * 存数据前
	 * 1.执行数据的转换
	 */
	void saveBefore();
	
	/**
	 * 存数据后
	 * 1.如果有索引变更，要清空changeIndexs
	 */
	void saveAfter();
}
