package com.cwa.component.data.operate.pool;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import com.cwa.component.data.operate.IDaoOperate;

/**
 * 异步次数执行操作池
 * 
 * @author Administrator
 * 
 */
public class AsyncCountOperatePool implements IOperatePool {
	private int expectCount = 5;// 期望次数
	private volatile List<IDaoOperate> daoOperates;// 操作集合

	private ExecutorService executor;

	public AsyncCountOperatePool() {
		daoOperates = new LinkedList<IDaoOperate>();
	}

	@Override
	public void execute(IDaoOperate daoOperate) {
		// 复制实体
		synchronized (daoOperates) {
			daoOperates.add(daoOperate);
			if (daoOperates.size() >= expectCount) {
				final List<IDaoOperate> oldDaoOperates = daoOperates;
				daoOperates = new LinkedList<IDaoOperate>();
				executor.execute(new Runnable() {
					@Override
					public void run() {
						reFlush(oldDaoOperates);
					}
				});
			}
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
		reFlush(oldDaoOperates);
	}

	private void reFlush(List<IDaoOperate> oldDaoOperates) {
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
	
	// -----------------------------------------
	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}

	public void setExpectCount(int expectCount) {
		this.expectCount = expectCount;
	}
}



