package com.cwa.component.data.operate.pool;

import java.util.concurrent.ExecutorService;

import com.cwa.component.data.operate.IDaoOperate;

/**
 * 异步立即执行操作池
 * 
 * @author Administrator
 * 
 */
public class AsyncImmediatelyOperatePool implements IOperatePool {
	private ExecutorService executor;

	@Override
	public void execute(IDaoOperate daoOperate) {
		executor.execute(daoOperate);
	}

	@Override
	public void reFlush() {

	}

	// -----------------------------------------
	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}

}
