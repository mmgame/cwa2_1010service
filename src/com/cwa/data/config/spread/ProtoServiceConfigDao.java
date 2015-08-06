package com.cwa.data.config.spread;

import java.util.List;

import baseice.basedao.IKey;

import com.cwa.component.data.operate.pool.IOperatePool;
import com.cwa.component.data.operate.DaoOperateFactory;
import com.cwa.component.data.operate.IDaoOperate;
import com.cwa.component.data.operate.TypeDaoOperate.DaoOperateType;
import com.cwa.data.config.IProtoServiceConfigDao;
import com.cwa.data.config.domain.ProtoServiceConfig;

public class ProtoServiceConfigDao implements IProtoServiceConfigDao {
	private IProtoServiceConfigDao dao;

	protected IOperatePool operatePool;// 操作池
	protected DaoOperateFactory daoOperateFactory;// 数据操作工厂
	
	@Override
	public List<ProtoServiceConfig> selectEntity(IKey key, Object attach) {
		//DefaultKey defaultKey = (DefaultKey) key;
		//if (defaultKey.functionName.equals("selectEntityByChannel")) {
		//	return dao.selectEntity(defaultKey);
		//}
		return null;
	}

	@Override
	public ProtoServiceConfig selectEntityByGidAndFtypeAndKey(int gid,int ftype,String key,Object attach) {
		ProtoServiceConfig entity =( ProtoServiceConfig)dao.selectEntityByGidAndFtypeAndKey(gid, ftype, key, attach);
		if(entity != null){
		   entity.obtainAfter();
		}
		return entity;
	}
	@Override
	public List<ProtoServiceConfig> selectEntityByGidAndFtype(int gid,int ftype,Object attach) {
		List<ProtoServiceConfig> entitys = dao.selectEntityByGidAndFtype(gid, ftype, attach);
		for (ProtoServiceConfig entity : entitys) {
	        entity.obtainAfter();
		}
		return entitys;
	}
	@Override
	public List<ProtoServiceConfig> selectEntityByGid(int gid,Object attach) {
		List<ProtoServiceConfig> entitys = dao.selectEntityByGid(gid, attach);
		for (ProtoServiceConfig entity : entitys) {
	        entity.obtainAfter();
		}
		return entitys;
	}
	

	@Override
	public List<ProtoServiceConfig> selectAllEntity(Object attach) {
	    List<ProtoServiceConfig> entitys = dao.selectAllEntity(attach);
	    for (ProtoServiceConfig entity : entitys) {
	        entity.obtainAfter();
		}
		return entitys;
	}
	
	@Override
	public void insertEntity(ProtoServiceConfig entity, Object attach) {
	    entity.saveBefore();
		IDaoOperate daoOperate = daoOperateFactory.createDaoOperate(dao, DaoOperateType.Insert, entity, "", null, attach);
		operatePool.execute(daoOperate);
	}

	@Override
	public void updateEntity(ProtoServiceConfig entity, Object attach) {
	    entity.saveBefore();
		IDaoOperate daoOperate = daoOperateFactory.createDaoOperate(dao, DaoOperateType.Update, entity, "", null, attach);
		operatePool.execute(daoOperate);
	}

	@Override
	public void removeEntity(ProtoServiceConfig entity, Object attach) {
		IDaoOperate daoOperate = daoOperateFactory.createDaoOperate(dao, DaoOperateType.Remove, entity, "", null, attach);
		operatePool.execute(daoOperate);
	}

	@Override
	public void batchInsertEntity(List<ProtoServiceConfig> entitys, Object attach) {
	    for (ProtoServiceConfig entity : entitys) {
	        entity.saveBefore();
		}
		IDaoOperate daoOperate = daoOperateFactory.createDaoOperate(dao, DaoOperateType.BatchInsert, entitys, "", null, attach);
		operatePool.execute(daoOperate);
	}

	@Override
	public void batchUpdateEntity(List<ProtoServiceConfig> entitys, Object attach) {
	    for (ProtoServiceConfig entity : entitys) {
	        entity.saveBefore();
		}
		IDaoOperate daoOperate = daoOperateFactory.createDaoOperate(dao, DaoOperateType.BatchUpdate, entitys, "", null, attach);
		operatePool.execute(daoOperate);
	}

	@Override
	public void batchRemoveEntity(List<ProtoServiceConfig> entitys, Object attach) {
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

	public void setDao(IProtoServiceConfigDao dao) {
		this.dao = dao;
	}
	
	public void setOperatePool(IOperatePool operatePool) {
		this.operatePool = operatePool;
	}

	public void setDaoOperateFactory(DaoOperateFactory daoOperateFactory) {
		this.daoOperateFactory = daoOperateFactory;
	}
}
