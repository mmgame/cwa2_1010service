package com.cwa.component.data.operate;

import java.util.LinkedList;
import java.util.List;

import baseice.basedao.IEntity;

import com.cwa.component.data.IEntityDao;
import com.cwa.component.data.operate.TypeDaoOperate.DaoOperateType;

public class DaoOperateFactory {
	public IDaoOperate createReflectDaoOperate(IEntityDao<? extends IEntity> dao, String methdoName, Object[] params, Class<?>[] paramTypes,
			IDaoCallBack daoCallBack) {
		ReflectDaoOperate daoOperate = new ReflectDaoOperate();
		daoOperate.setEntityDao(dao);
		daoOperate.setParams(params);
		daoOperate.setParamTypes(paramTypes);
		daoOperate.setMethdoName(methdoName);
		daoOperate.setDaoCallBack(daoCallBack);
		return daoOperate;
	}

	public IDaoOperate createDaoOperate(IEntityDao<? extends IEntity> dao, DaoOperateType doType, List<? extends IEntity> entitys, String key, IDaoCallBack daoCallBack,
			Object attach) {
		TypeDaoOperate daoOperate = new TypeDaoOperate(key);
		daoOperate.setEntityDao(dao);
		daoOperate.setDoType(doType);
		daoOperate.setEntitys(entitys);
		daoOperate.setAttach(attach);
		daoOperate.setDaoCallBack(daoCallBack);
		return daoOperate;
	}

	public IDaoOperate createDaoOperate(IEntityDao<? extends IEntity> dao, DaoOperateType doType, IEntity entity, String key, IDaoCallBack daoCallBack,
			Object attach) {
		TypeDaoOperate daoOperate = new TypeDaoOperate(key);
		daoOperate.setEntityDao(dao);
		daoOperate.setDoType(doType);
		List<IEntity> entitys = new LinkedList<IEntity>();
		entitys.add(entity);
		daoOperate.setEntitys(entitys);
		daoOperate.setDaoCallBack(daoCallBack);
		daoOperate.setAttach(attach);
		return daoOperate;
	}
}
