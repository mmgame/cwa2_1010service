package com.cwa.service.init.servicefactory;

import java.util.List;

import baseice.basedao.IEntity;

import com.cwa.component.data.IDBSession;
import com.cwa.data.config.domain.DatabaseInfoConfig;
import com.cwa.data.config.domain.DatabaseServiceConfig;
import com.cwa.service.context.IGloabalContext;

public interface IDBServiceFactory extends IServiceFactory {
	void createService(int gid, DatabaseServiceConfig serviceConfig, List<? extends IEntity> infos, IGloabalContext gloabalContext)
			throws Exception;

	IDBSession createDBSession(DatabaseServiceConfig serviceConfig, DatabaseInfoConfig info) throws Exception;

}
