package com.cwa.data.config.spread;

import java.util.List;

import baseice.basedao.IKey;

import com.cwa.component.data.operate.pool.IOperatePool;
import com.cwa.component.data.operate.DaoOperateFactory;
import com.cwa.component.data.operate.IDaoOperate;
import com.cwa.component.data.operate.TypeDaoOperate.DaoOperateType;
import com.cwa.data.config.IMemberServiceConfigDao;
import com.cwa.data.config.domain.MemberServiceConfig;

public class MemberServiceConfigDao implements IMemberServiceConfigDao {
	private IMemberServiceConfigDao dao;

	protected IOperatePool operatePool;// 操作池
	protected DaoOperateFactory daoOperateFactory;// 数据操作工厂
	
	@Override
	public List<MemberServiceConfig> selectEntity(IKey key, Object attach) {
		//DefaultKey defaultKey = (DefaultKey) key;
		//if (defaultKey.functionName.equals("selectEntityByChannel")) {
		//	return dao.selectEntity(defaultKey);
		//}
		return null;
	}

	@Override
	public MemberServiceConfig selectEntityByGidAndFtypeAndKey(int gid,int ftype,String key,Object attach) {
		MemberServiceConfig entity =( MemberServiceConfig)dao.selectEntityByGidAndFtypeAndKey(gid, ftype, key, attach);
		if(entity != null){
		   entity.obtainAfter();
		}
		return entity;
	}
	@Override
	public List<MemberServiceConfig> selectEntityByGidAndFtype(int gid,int ftype,Object attach) {
		List<MemberServiceConfig> entitys = dao.selectEntityByGidAndFtype(gid, ftype, attach);
		for (MemberServiceConfig entity : entitys) {
	        entity.obtainAfter();
		}
		return entitys;
	}
	@Override
	public List<MemberServiceConfig> selectEntityByGid(int gid,Object attach) {
		List<MemberServiceConfig> entitys = dao.selectEntityByGid(gid, attach);
		for (MemberServiceConfig entity : entitys) {
	        entity.obtainAfter();
		}
		return entitys;
	}
	

	@Override
	public List<MemberServiceConfig> selectAllEntity(Object attach) {
	    List<MemberServiceConfig> entitys = dao.selectAllEntity(attach);
	    for (MemberServiceConfig entity : entitys) {
	        entity.obtainAfter();
		}
		return entitys;
	}
	
	@Override
	public void insertEntity(MemberServiceConfig entity, Object attach) {
	    entity.saveBefore();
		IDaoOperate daoOperate = daoOperateFactory.createDaoOperate(dao, DaoOperateType.Insert, entity, "", null, attach);
		operatePool.execute(daoOperate);
	}

	@Override
	public void updateEntity(MemberServiceConfig entity, Object attach) {
	    entity.saveBefore();
		IDaoOperate daoOperate = daoOperateFactory.createDaoOperate(dao, DaoOperateType.Update, entity, "", null, attach);
		operatePool.execute(daoOperate);
	}

	@Override
	public void removeEntity(MemberServiceConfig entity, Object attach) {
		IDaoOperate daoOperate = daoOperateFactory.createDaoOperate(dao, DaoOperateType.Remove, entity, "", null, attach);
		operatePool.execute(daoOperate);
	}

	@Override
	public void batchInsertEntity(List<MemberServiceConfig> entitys, Object attach) {
	    for (MemberServiceConfig entity : entitys) {
	        entity.saveBefore();
		}
		IDaoOperate daoOperate = daoOperateFactory.createDaoOperate(dao, DaoOperateType.BatchInsert, entitys, "", null, attach);
		operatePool.execute(daoOperate);
	}

	@Override
	public void batchUpdateEntity(List<MemberServiceConfig> entitys, Object attach) {
	    for (MemberServiceConfig entity : entitys) {
	        entity.saveBefore();
		}
		IDaoOperate daoOperate = daoOperateFactory.createDaoOperate(dao, DaoOperateType.BatchUpdate, entitys, "", null, attach);
		operatePool.execute(daoOperate);
	}

	@Override
	public void batchRemoveEntity(List<MemberServiceConfig> entitys, Object attach) {
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

	public void setDao(IMemberServiceConfigDao dao) {
		this.dao = dao;
	}
	
	public void setOperatePool(IOperatePool operatePool) {
		this.operatePool = operatePool;
	}

	public void setDaoOperateFactory(DaoOperateFactory daoOperateFactory) {
		this.daoOperateFactory = daoOperateFactory;
	}
}
