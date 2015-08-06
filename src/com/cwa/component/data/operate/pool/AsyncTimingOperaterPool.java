package com.cwa.component.data.operate.pool;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import com.cwa.component.data.operate.IDaoOperate;
import com.cwa.component.task.ITask;
import com.cwa.component.task.ITaskContext;

/**
 * 异步定时执行操作池
 * 
 * @author Administrator
 * 
 */
public class AsyncTimingOperaterPool implements IOperatePool, ITask {

	private ExecutorService executor;
	//----------------------------------
	private String taskId;
	// 操作集合
	private volatile List<IDaoOperate> daoOperates;

	public AsyncTimingOperaterPool(String taskId) {
		daoOperates = new LinkedList<IDaoOperate>();
		this.taskId=taskId;
	}

	@Override
	public void execute(IDaoOperate daoOperate) {
		// 复制实体
		synchronized (daoOperates) {
			daoOperates.add(daoOperate);
		}
	}

	@Override
	public void reFlush() {
		List<IDaoOperate> oldDaoOperates = null;
		synchronized (daoOperates) {
			if (daoOperates.isEmpty()) {
				return;
			}
			oldDaoOperates = daoOperates;
			daoOperates = new LinkedList<IDaoOperate>();
		}
		List<IDaoOperate> cbs = new LinkedList<IDaoOperate>();
		Map<String, IDaoOperate> daoCallBackMap = new HashMap<String, IDaoOperate>();
		for (IDaoOperate daoOperate : oldDaoOperates) {
			if (!daoOperate.canMerge()) {
				// 不能合并
				cbs.add(daoOperate);
				continue;
			}
			String key = daoOperate.getKey();
			if (!daoCallBackMap.containsKey(key)) {
				daoCallBackMap.put(key, daoOperate);
				cbs.add(daoOperate);
			} else {
				IDaoOperate oldBc = daoCallBackMap.get(key);
				// 开始合并
				oldBc.merge(daoOperate);
			}
		}
		for (IDaoOperate daoOperate : cbs) {
			if (!daoOperate.canExecute()) {
				// 不可以执行
				continue;
			}
			// 执行数据库操作
			daoOperate.execute();
		}
	}

	@Override
	public String id() {
		return taskId;
	}

	@Override
	public void execute(ITaskContext context) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				reFlush();
			}
		});
	}

	// -----------------------------------------
	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}
}
