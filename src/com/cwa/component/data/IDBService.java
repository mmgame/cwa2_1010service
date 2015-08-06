package com.cwa.component.data;

import com.cwa.data.config.domain.DatabaseServiceConfig;
import com.cwa.service.IService;

/**
 * 数据服务
 * 
 * @author mausmars
 *
 */
public interface IDBService extends IService {
	int getGid();

	/**
	 * 通过区id查询
	 * 
	 * @param rid
	 * @return
	 */
	IDBSession getDBSession(int rid);

	/**
	 * 通过区key查询
	 * 
	 * @param key
	 * @return
	 */
	IDBSession getDBSessionByKey(int key);

	/**
	 * 插入
	 * 
	 * @param dbSession
	 */
	void insertDBSession(int key, int rid, IDBSession dbSession);

	DatabaseServiceConfig getServiceConfig();
}
