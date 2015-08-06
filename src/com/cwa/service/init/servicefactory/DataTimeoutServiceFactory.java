package com.cwa.service.init.servicefactory;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import baseice.basedao.IEntity;

import com.cwa.component.data.IDBService;
import com.cwa.component.data.IDBSession;
import com.cwa.component.datatimeout.DataTimeoutService;
import com.cwa.data.config.IDataTimeoutServiceConfigDao;
import com.cwa.data.config.domain.DataTimeoutServiceConfig;
import com.cwa.service.constant.ServiceConstant;
import com.cwa.service.context.FilterContext;
import com.cwa.service.context.IGloabalContext;

public class DataTimeoutServiceFactory implements IServiceFactory {
	private static final Logger logger = LoggerFactory.getLogger(IServiceFactory.class);

	@Override
	public void createService(FilterContext c) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("--- InitDataTimeoutService ---");
		}
		IGloabalContext gloabalContext = c.getGloabalContext();
		IDBService dbService = (IDBService) gloabalContext.getService(ServiceConstant.General_Gid, ServiceConstant.DatabaseKey);// 配置服数据库session
		IDBSession configDBSession = dbService.getDBSession(ServiceConstant.General_Rid);

		IDataTimeoutServiceConfigDao dao = (IDataTimeoutServiceConfigDao) configDBSession.getEntityDao(DataTimeoutServiceConfig.class);

		List<? extends IEntity> serviceConfigs = (List<? extends IEntity>) dao.selectEntityByGidAndFtype(gloabalContext.getGid(),
				gloabalContext.getFunctionType(), configDBSession.getParams(ServiceConstant.General_Rid));

		if (serviceConfigs.isEmpty()) {
			// 如果是空就返回
			return;
		}
		for (IEntity sc : serviceConfigs) {
			// 这里暂时是一个后边是多个再改
			DataTimeoutServiceConfig serviceConfig = (DataTimeoutServiceConfig) sc;
			if (serviceConfig.available != 1) {
				// 如果不可用
				return;
			}
			DataTimeoutService service = new DataTimeoutService(serviceConfig.key);
			service.setExpression(serviceConfig.expression);
			service.setTimeout(serviceConfig.timeout);

			// 插入服务
			gloabalContext.insertIService(gloabalContext.getGid(), service);
		}
	}
}
