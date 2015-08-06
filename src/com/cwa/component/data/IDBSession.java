package com.cwa.component.data;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import baseice.basedao.IEntity;

public interface IDBSession {
	int getKey();

	String getDBName();

	Object getSqlSession();

	IEntityDao getEntityDao(Class<? extends IEntity> cls);

	Collection<IEntityDao> getAllEntityDao();

	void close();

	Set<Integer> getRids();

	Map<String, Object> getParams(int rid);
}
