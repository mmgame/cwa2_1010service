package com.cwa.component.data;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import baseice.basedao.IEntity;
import baseice.basedao.IKey;

/**
 * 数据操作口
 * 
 * @author Administrator
 * 
 */
public interface IEntityDao<T extends IEntity> extends ITableDao {
	static final Logger logger = LoggerFactory.getLogger(IEntityDao.class);

	List<T> selectEntity(IKey key, Object attach);

	List<T> selectAllEntity(Object attach);

	// -------------------------
	void insertEntity(T entity, Object attach);

	void updateEntity(T entity, Object attach);

	void removeEntity(T entity, Object attach);

	void batchInsertEntity(List<T> entitys, Object attach);

	void batchUpdateEntity(List<T> entitys, Object attach);

	void batchRemoveEntity(List<T> entitys, Object attach);
}
