package com.cwa.service.init.servicefactory;

import java.util.List;

import org.apache.zookeeper.WatchedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import baseice.basedao.IEntity;

import com.cwa.component.data.IDBService;
import com.cwa.component.data.IDBSession;
import com.cwa.component.membermanager.MemberDataEvent;
import com.cwa.component.membermanager.MemberService;
import com.cwa.component.zkservice.IZKEvent;
import com.cwa.component.zkservice.IZKEventFactory;
import com.cwa.component.zkservice.ZKService;
import com.cwa.data.config.IMemberServiceConfigDao;
import com.cwa.data.config.IMemberZKInfoConfigDao;
import com.cwa.data.config.domain.MemberServiceConfig;
import com.cwa.data.config.domain.MemberZKInfoConfig;
import com.cwa.service.constant.ServiceConstant;
import com.cwa.service.context.FilterContext;
import com.cwa.service.context.IGloabalContext;
import com.cwa.service.init.IIceServer;

public class ZKMemberServiceFactory implements IServiceFactory {
	private static final Logger logger = LoggerFactory.getLogger(IServiceFactory.class);

	public void createService(FilterContext c) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("--- InitMemberService ---");
		}
		IGloabalContext gloabalContext = c.getGloabalContext();
		IIceServer iceServer = gloabalContext.getIceServer();
		IDBService dbService = (IDBService) gloabalContext.getService(ServiceConstant.General_Gid, ServiceConstant.DatabaseKey);// 配置服数据库session
		IDBSession configDBSession = dbService.getDBSession(ServiceConstant.General_Rid);

		IMemberServiceConfigDao dao = (IMemberServiceConfigDao) configDBSession.getEntityDao(MemberServiceConfig.class);
		List<? extends IEntity> serviceConfigs = (List<? extends IEntity>) dao.selectEntityByGidAndFtype(gloabalContext.getGid(),
				gloabalContext.getFunctionType(), configDBSession.getParams(ServiceConstant.General_Rid));

		if (serviceConfigs.isEmpty()) {
			// 如果是空就返回
			return;
		}
		// 这里暂时是一个后边是多个再改
		MemberServiceConfig serviceConfig = (MemberServiceConfig) serviceConfigs.get(0);

		if (serviceConfig.available != 1) {
			// 如果不可用
			return;
		}
		IMemberZKInfoConfigDao infoDao = (IMemberZKInfoConfigDao) configDBSession.getEntityDao(MemberZKInfoConfig.class);

		List<Integer> gids = serviceConfig.getGroupIdsList();
		if (gids.contains(-1)) {
			List<? extends IEntity> infos = infoDao.selectAllEntity(configDBSession.getParams(ServiceConstant.General_Rid));
			for (IEntity e : infos) {
				MemberZKInfoConfig info = (MemberZKInfoConfig) e;
				createFS(serviceConfig, info, iceServer, gloabalContext);
			}
		} else {
			for (int gid : gids) {
				MemberZKInfoConfig info = (MemberZKInfoConfig) infoDao.selectEntityByGid(gid,
						configDBSession.getParams(ServiceConstant.General_Rid));
				createFS(serviceConfig, info, iceServer, gloabalContext);
			}
		}
	}

	// 创建zk功能服管理
	private void createFS(MemberServiceConfig serviceConfig, MemberZKInfoConfig info, IIceServer iceServer, IGloabalContext gloabalContext) {
		MemberService service = createMemberService(serviceConfig, info, iceServer);
		// service.startup();
		// 插入服务
		gloabalContext.insertMemberService(gloabalContext.getGid(), service);
	}

	private MemberService createMemberService(MemberServiceConfig serviceConfig, MemberZKInfoConfig info, IIceServer iceServer) {
		// 初始化zk的service
		ZKService zkService = new ZKService(info.hosts);
		zkService.setSessionTimeOut(info.sessionTimeout);
		zkService.setEventFactory(new IZKEventFactory() {
			@Override
			public IZKEvent create(WatchedEvent event) {
				return new MemberDataEvent(event);
			}
		});

		// 初始化zk的service
		MemberService service = new MemberService(serviceConfig.key);
		service.setZkService(zkService);
		service.setModuleName(info.rootPath);
		service.setVersion(serviceConfig.version);
		return service;
	}
}
