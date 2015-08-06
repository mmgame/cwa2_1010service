package com.cwa.entity.abs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cwa.component.data.IEntityDao;
import com.cwa.component.data.operate.DaoOperateFactory;
import com.cwa.component.data.operate.pool.IOperatePool;
import com.cwa.entity.domain.TestUserEntity;

public abstract class AbstractEntityDao implements IEntityDao<TestUserEntity> {
	protected static final Logger logger = LoggerFactory.getLogger(AbstractEntityDao.class);

	protected IOperatePool operatePool;// 操作池
	protected DaoOperateFactory daoOperateFactory;// 数据操作工厂

	//------------------------------------------------
	public void setOperatePool(IOperatePool operatePool) {
		this.operatePool = operatePool;
	}
	public void setDaoOperateFactory(DaoOperateFactory daoOperateFactory) {
		this.daoOperateFactory = daoOperateFactory;
	}
}
