package com.cwa.component.data.operate;

/**
 * 数据操作接口
 * 
 * @author Administrator
 * 
 */
public interface IDaoOperate extends Runnable {
	/**
	 * 获得key
	 * 
	 * @return
	 */
	String getKey();

	/**
	 * 是否可以合并
	 */
	boolean canMerge();

	/**
	 * 合并
	 */
	void merge(IDaoOperate daoOperate);

	/**
	 * 是否可以执行
	 */
	boolean canExecute();

	/**
	 * 执行
	 */
	Object execute();
}
