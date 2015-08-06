package com.cwa.entity.spread;

import java.util.List;

import baseice.basedao.DefaultKey;
import baseice.basedao.IEntity;
import baseice.basedao.IKey;

import com.cwa.component.data.operate.IDaoOperate;
import com.cwa.component.data.operate.TypeDaoOperate.DaoOperateType;
import com.cwa.entity.ITestUserEntityDao;
import com.cwa.entity.abs.AbstractEntityDao;
import com.cwa.entity.domain.TestUserEntity;

public class TestUserEntityDao extends AbstractEntityDao implements ITestUserEntityDao {
	private ITestUserEntityDao dao;

	@Override
	public List<TestUserEntity> selectEntity(IKey key, Object attach) {
		DefaultKey defaultKey = (DefaultKey) key;
		// if (defaultKey.functionName.equals("selectEntityByChannel")) {
		// return dao.selectEntity(defaultKey);
		// }
		return null;
	}

	public List<TestUserEntity> selectEntityByUserId(long userId, Object attach) {
		return dao.selectEntityByUserId(userId, attach);
	}

	@Override
	public List<TestUserEntity> selectAllEntity(Object attach) {
		return dao.selectAllEntity(attach);
	}
	
	@Override
	public void insertEntity(TestUserEntity entity, Object attach) {
		IDaoOperate daoOperate = daoOperateFactory.createDaoOperate(dao, DaoOperateType.Insert, entity, "", null, attach);
		operatePool.execute(daoOperate);
	}

	@Override
	public void updateEntity(TestUserEntity entity, Object attach) {
		IDaoOperate daoOperate = daoOperateFactory.createDaoOperate(dao, DaoOperateType.Update, entity, "", null, attach);
		operatePool.execute(daoOperate);
	}

	@Override
	public void removeEntity(TestUserEntity entity, Object attach) {
		IDaoOperate daoOperate = daoOperateFactory.createDaoOperate(dao, DaoOperateType.Remove, entity, "", null, attach);
		operatePool.execute(daoOperate);
	}

	@Override
	public void batchInsertEntity(List<TestUserEntity> entitys, Object attach) {
		IDaoOperate daoOperate = daoOperateFactory.createDaoOperate(dao, DaoOperateType.BatchInsert, entitys, "", null, attach);
		operatePool.execute(daoOperate);
	}

	@Override
	public void batchUpdateEntity(List<TestUserEntity> entitys, Object attach) {
		IDaoOperate daoOperate = daoOperateFactory.createDaoOperate(dao, DaoOperateType.BatchUpdate, entitys, "", null, attach);
		operatePool.execute(daoOperate);
	}

	@Override
	public void batchRemoveEntity(List<TestUserEntity> entitys, Object attach) {
		IDaoOperate daoOperate = daoOperateFactory.createDaoOperate(dao, DaoOperateType.BatchRemove, entitys, "", null, attach);
		operatePool.execute(daoOperate);
	}

	@Override
	public void createTable(Object attach) {
		dao.createTable(attach);
	}

	@Override
	public List<String> selectAllTableName(Object attach) {
		return dao.selectAllTableName(attach);
	}

	// ------------------------------

	public void setDao(ITestUserEntityDao dao) {
		this.dao = dao;
	}
}
