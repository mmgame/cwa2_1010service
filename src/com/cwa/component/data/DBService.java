package com.cwa.component.data;

import java.util.HashMap;
import java.util.Map;

import serverice.config.ServiceConfigTypeEnum;

import com.cwa.data.config.domain.DatabaseServiceConfig;

public class DBService implements IDBService {
	private String key;
	private int version;
	private int gid;

	// {key:SqlSession}
	private Map<Integer, IDBSession> sqlSessionMap = new HashMap<Integer, IDBSession>();
	// {rid:key}
	private Map<Integer, Integer> regionMap = new HashMap<Integer, Integer>();

	private DatabaseServiceConfig serviceConfig;

	public DBService(String key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public int getServiceType() {
		return ServiceConfigTypeEnum.Database.value();
	}

	@Override
	public void startup() throws Exception {
	}

	@Override
	public void shutdown() throws Exception {
		for (IDBSession session : sqlSessionMap.values()) {
			session.close();
		}
	}

	@Override
	public IDBSession getDBSession(int rid) {
		Integer key = regionMap.get(rid);
		if (key == null) {
			return null;
		}
		return sqlSessionMap.get(key);
	}

	@Override
	public IDBSession getDBSessionByKey(int key) {
		return sqlSessionMap.get(key);
	}

	@Override
	public void insertDBSession(int key, int rid, IDBSession dbSession) {
		if (!sqlSessionMap.containsKey(key)) {
			sqlSessionMap.put(dbSession.getKey(), dbSession);
		}
		regionMap.put(rid, key);
	}

	public void insertDBSession(IDBSession dbSession) {
		sqlSessionMap.put(dbSession.getKey(), dbSession);
		for (int rid : dbSession.getRids()) {
			regionMap.put(rid, dbSession.getKey());
		}
	}

	@Override
	public int getGid() {
		return gid;
	}

	@Override
	public DatabaseServiceConfig getServiceConfig() {
		return serviceConfig;
	}

	// -------------------------------------------------
	public void setVersion(int version) {
		this.version = version;
	}

	public void setServiceConfig(DatabaseServiceConfig serviceConfig) {
		this.serviceConfig = serviceConfig;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}
}
