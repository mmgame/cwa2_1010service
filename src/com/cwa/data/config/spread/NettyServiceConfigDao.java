package com.cwa.data.config.spread;

import java.util.List;

import baseice.basedao.IKey;

import com.cwa.component.data.operate.pool.IOperatePool;
import com.cwa.component.data.operate.DaoOperateFactory;
import com.cwa.component.data.operate.IDaoOperate;
import com.cwa.component.data.operate.TypeDaoOperate.DaoOperateType;
import com.cwa.data.config.INettyServiceConfigDao;
import com.cwa.data.config.domain.NettyServiceConfig;

public class NettyServiceConfigDao implements INettyServiceConfigDao {
	private INettyServiceConfigDao dao;

	protected IOperatePool operatePool;// 操作池
	protected DaoOperateFactory daoOperateFactory;// 数据操作工厂
	
	@Override
	public List<NettyServiceConfig> selectEntity(IKey key, Object attach) {
		//DefaultKey defaultKey = (DefaultKey) key;
		//if (defaultKey.functionName.equals("selectEntityByChannel")) {
		//	return dao.selectEntity(defaultKey);
		//}
		return null;
	}

	@Override
	public NettyServiceConfig selectEntityByGidAndFtypeAndKey(int gid,int ftype,String key,Object attach) {
		NettyServiceConfig entity =( NettyServiceConfig)dao.selectEntityByGidAndFtypeAndKey(gid, ftype, key, attach);
		if(entity != null){
		   entity.obtainAfter();
		}
		return entity;
	}
	@Override
	public List<NettyServiceConfig> selectEntityByGidAndFtype(int gid,int ftype,Object attach) {
		List<NettyServiceConfig> entitys = dao.selectEntityByGidAndFtype(gid, ftype, attach);
		for (NettyServiceConfig entity : entitys) {
	        entity.obtainAfter();
		}
		return entitys;
	}
	@Override
	public List<NettyServiceConfig> selectEntityByGid(int gid,Object attach) {
		List<NettyServiceConfig> entitys = dao.selectEntityByGid(gid, attach);
		for (NettyServiceConfig entity : entitys) {
	        entity.obtainAfter();
		}
		return entitys;
	}
	

	@Override
	public List<NettyServiceConfig> selectAllEntity(Object attach) {
	    List<NettyServiceConfig> entitys = dao.selectAllEntity(attach);
	    for (NettyServiceConfig entity : entitys) {
	        entity.obtainAfter();
		}
		return entitys;
	}
	
	@Override
	public void insertEntity(NettyServiceConfig entity, Object attach) {
	    entity.saveBefore();
		IDaoOperate daoOperate = daoOperateFactory.createDaoOperate(dao, DaoOperateType.Insert, entity, "", null, attach);
		operatePool.execute(daoOperate);
	}

	@Override
	public void updateEntity(NettyServiceConfig entity, Object attach) {
	    entity.saveBefore();
		IDaoOperate daoOperate = daoOperateFactory.createDaoOperate(dao, DaoOperateType.Update, entity, "", null, attach);
		operatePool.execute(daoOperate);
	}

	@Override
	public void removeEntity(NettyServiceConfig entity, Object attach) {
		IDaoOperate daoOperate = daoOperateFactory.createDaoOperate(dao, DaoOperateType.Remove, entity, "", null, attach);
		operatePool.execute(daoOperate);
	}

	@Override
	public void batchInsertEntity(List<NettyServiceConfig> entitys, Object attach) {
	    for (NettyServiceConfig entity : entitys) {
	        entity.saveBefore();
		}
		IDaoOperate daoOperate = daoOperateFactory.createDaoOperate(dao, DaoOperateType.BatchInsert, entitys, "", null, attach);
		operatePool.execute(daoOperate);
	}

	@Override
	public void batchUpdateEntity(List<NettyServiceConfig> entitys, Object attach) {
	    for (NettyServiceConfig entity : entitys) {
	        entity.saveBefore();
		}
		IDaoOperate daoOperate = daoOperateFactory.createDaoOperate(dao, DaoOperateType.BatchUpdate, entitys, "", null, attach);
		operatePool.execute(daoOperate);
	}

	@Override
	public void batchRemoveEntity(List<NettyServiceConfig> entitys, Object attach) {
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

	public void setDao(INettyServiceConfigDao dao) {
		this.dao = dao;
	}
	
	public void setOperatePool(IOperatePool operatePool) {
		this.operatePool = operatePool;
	}

	public void setDaoOperateFactory(DaoOperateFactory daoOperateFactory) {
		this.daoOperateFactory = daoOperateFactory;
	}
}
