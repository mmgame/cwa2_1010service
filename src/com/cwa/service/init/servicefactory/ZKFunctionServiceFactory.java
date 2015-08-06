package com.cwa.service.init.servicefactory;

import java.util.List;

import org.apache.zookeeper.WatchedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import baseice.basedao.IEntity;

import com.cwa.component.data.IDBService;
import com.cwa.component.data.IDBSession;
import com.cwa.component.functionmanage.FunctionService;
import com.cwa.component.functionmanage.node.NodePath;
import com.cwa.component.functionmanage.node.filter.FunctionNodeFilter;
import com.cwa.component.zkservice.IZKEvent;
import com.cwa.component.zkservice.IZKEventFactory;
import com.cwa.component.zkservice.ZKService;
import com.cwa.data.config.IFunctionServiceConfigDao;
import com.cwa.data.config.IFunctionZKInfoConfigDao;
import com.cwa.data.config.domain.FunctionServiceConfig;
import com.cwa.data.config.domain.FunctionZKInfoConfig;
import com.cwa.service.constant.ServiceConstant;
import com.cwa.service.context.FilterContext;
import com.cwa.service.context.IGloabalContext;
import com.cwa.service.init.IIceServer;

public class ZKFunctionServiceFactory implements IZKFunctionServiceFactory {
	private static final Logger logger = LoggerFactory.getLogger(IServiceFactory.class);

	public void createService(FilterContext c) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("--- InitFunctionService ---");
		}
		IGloabalContext gloabalContext = c.getGloabalContext();
		IDBService dbService = (IDBService) gloabalContext.getService(ServiceConstant.General_Gid, ServiceConstant.DatabaseKey);// 配置服数据库session
		IDBSession configDBSession = dbService.getDBSession(ServiceConstant.General_Rid);

		IFunctionServiceConfigDao dao = (IFunctionServiceConfigDao) configDBSession.getEntityDao(FunctionServiceConfig.class);
		List<? extends IEntity> serviceConfigs = (List<? extends IEntity>) dao.selectEntityByGidAndFtype(gloabalContext.getGid(),
				gloabalContext.getFunctionType(), configDBSession.getParams(ServiceConstant.General_Rid));

		if (serviceConfigs.isEmpty()) {
			// 如果是空就返回
			return;
		}
		// 这里暂时是一个后边是多个再改
		FunctionServiceConfig serviceConfig = (FunctionServiceConfig) serviceConfigs.get(0);

		if (serviceConfig.available != 1) {
			// 如果不可用
			return;
		}
		IFunctionZKInfoConfigDao infoDao = (IFunctionZKInfoConfigDao) configDBSession.getEntityDao(FunctionZKInfoConfig.class);

		List<Integer> gids = serviceConfig.getGroupIdsList();
		if (gids.contains(-1)) {
			List<? extends IEntity> infos = infoDao.selectAllEntity(configDBSession.getParams(ServiceConstant.General_Rid));
			for (IEntity e : infos) {
				FunctionZKInfoConfig info = (FunctionZKInfoConfig) e;
				if (info.gid == gloabalContext.getGid()) {
					continue;
				}
				createService(serviceConfig, info, gloabalContext);
			}
		} else {
			for (int gid : gids) {
				if (gid == gloabalContext.getGid()) {
					continue;
				}
				FunctionZKInfoConfig info = (FunctionZKInfoConfig) infoDao.selectEntityByGid(gid,
						configDBSession.getParams(ServiceConstant.General_Rid));
				createService(serviceConfig, info, gloabalContext);
			}
		}
	}

	// 创建zk功能服管理
	@Override
	public void createService(FunctionServiceConfig serviceConfig, FunctionZKInfoConfig info, IGloabalContext gloabalContext)
			throws Exception {
		FunctionService service = createFunctionService(serviceConfig, info, gloabalContext.getIceServer());
		service.startup();
		// 插入服务
		gloabalContext.insertFunctionService(gloabalContext.getGid(), service);
		// 创建代理
		service.createZKPrx();
	}

	private FunctionService createFunctionService(FunctionServiceConfig serviceConfig, FunctionZKInfoConfig info, IIceServer iceServer) {
		FunctionNodeFilter nodeFilter = new FunctionNodeFilter();
		nodeFilter.setFunctionFilterStr(serviceConfig.functionFilters);
		nodeFilter.setGroupFilterStr(serviceConfig.groupFilters);

		// 初始化zk的service
		ZKService zkService = new ZKService(info.hosts);
		zkService.setSessionTimeOut(info.sessionTimeout);
		zkService.setEventFactory(new IZKEventFactory() {
			@Override
			public IZKEvent create(WatchedEvent event) {
				return new NodePath(event);
			}
		});

		// 初始化zk的service
		FunctionService functionService = new FunctionService(serviceConfig.key);
		functionService.setZkService(zkService);
		functionService.setNodeFilter(nodeFilter);
		functionService.setIceServer(iceServer);
		functionService.setModuleName(info.rootPath);
		functionService.setVersion(serviceConfig.version);
		return functionService;
	}
}
