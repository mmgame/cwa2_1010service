package com.cwa.component.data.operate.pool;

import com.cwa.component.data.operate.IDaoOperate;

public interface IOperatePool {
	/**
	 * 执行一个操作
	 * 
	 * @param daoOperate
	 */
	void execute(IDaoOperate daoOperate);

	/**
	 * 刷新立即执行
	 * 
	 * @param daoOperate
	 */
	void reFlush();
}
