package com.cwa.component.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;

import baseice.basedao.IEntity;

/**
 * 每个库的连接
 * 
 * @author mausmars
 * 
 */
public class DBSession implements IDBSession {
	private int key;
	private SqlSession sqlSession;
	// {DaoName:IEntityDao}
	private Map<String, IEntityDao> entityDaoMap;

	private String dbName;
	// 区id集合
	private Set<Integer> rids = new HashSet<Integer>();

	public DBSession(int key) {
		this.key = key;
	}

	@Override
	public int getKey() {
		return key;
	}

	@Override
	public Object getSqlSession() {
		return sqlSession;
	}

	@Override
	public IEntityDao getEntityDao(Class<? extends IEntity> cls) {
		return entityDaoMap.get(cls.getSimpleName());
	}

	@Override
	public Collection<IEntityDao> getAllEntityDao() {
		return entityDaoMap.values();
	}

	@Override
	public void close() {
		sqlSession.close();
	}

	@Override
	public Set<Integer> getRids() {
		return rids;
	}

	@Override
	public String getDBName() {
		return dbName;
	}

	public void addRid(int rid) {
		this.rids.add(rid);
	}

	public void addRids(List<Integer> rids) {
		this.rids.addAll(rids);
	}

	@Override
	public Map<String, Object> getParams(int rid) {
		// if (!rids.contains(rid)) {
		// return null;
		// }
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sql_session", sqlSession);
		params.put("table_number", rid);
		params.put("db_number", dbName);
		return params;
	}

	// ------------------
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public void setEntityDaoMap(Map<String, IEntityDao> entityDaoMap) {
		this.entityDaoMap = entityDaoMap;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
}
