package com.cwa.component.datatimeout;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.cwa.component.task.ITaskContext;
import com.cwa.util.priorityqueue.PriorityQueueMap;
import com.cwa.util.priorityqueue.QueueCell;

/**
 * 数据超时任务，管理超时的数据
 * 
 * @author mausmars
 *
 */
public class DataTimeoutTask implements IDataTimeoutManager {
	private String id;

	private int timeout;
	// 队列
	private PriorityQueueMap priorityQueue = new PriorityQueueMap();

	public DataTimeoutTask(String id) {
		this.id = id;
	}

	@Override
	public String id() {
		return id;
	}

	public void insertTimeoutCheck(String key, Object obj, IDataTimeoutCallBlack callBlack) {
		long time = System.currentTimeMillis() + timeout;
		insertTimeoutCheck(key, time, obj, callBlack);
	}

	@Override
	public void insertTimeoutCheck(String key, long time, Object obj, IDataTimeoutCallBlack callBlack) {
		QueueCell cell = new QueueCell();
		cell.setKey(key);
		cell.setTime(time);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Timeout_Object", obj);
		params.put("Timeout_CallBlack", callBlack);
		cell.setObj(params);

		priorityQueue.putCell(cell);
	}

	public void removeTimeoutCheck(String key) {
		priorityQueue.removeCell(key);
	}

	public void resetTime(String key) {
		long time = System.currentTimeMillis() + timeout;
		resetTime(key, time);
	}

	@Override
	public void resetTime(String key, long time) {
		QueueCell cell = priorityQueue.removeCell(key);
		cell.setTime(time);
		priorityQueue.putCell(cell);
	}

	@Override
	public void execute(ITaskContext context) {
		long executeTime = System.currentTimeMillis();
		for (;;) {
			Set<QueueCell> cells = priorityQueue.pollCell(executeTime);
			if (cells == null) {
				break;
			}
			for (QueueCell cell : cells) {
				Map<String, Object> params = (Map<String, Object>) cell.getObj();
				Object obj = params.get("Timeout_Object");
				IDataTimeoutCallBlack callBlack = (IDataTimeoutCallBlack) params.get("Timeout_CallBlack");
				if (callBlack != null) {
					callBlack.callblack(obj);
				}
			}
		}
	}

	// -----------------------------------------
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
