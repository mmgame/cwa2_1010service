package com.cwa.component.data.operate.pool;

import com.cwa.component.data.operate.IDaoOperate;

/**
 * 立即执行
 * 
 * @author mausmars
 *
 */
public class SyncOperatePool implements IOperatePool {

	@Override
	public void execute(IDaoOperate daoOperate) {
		daoOperate.execute();
	}

	@Override
	public void reFlush() {

	}

}
