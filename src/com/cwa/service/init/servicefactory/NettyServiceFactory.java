package com.cwa.service.init.servicefactory;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import baseice.basedao.IEntity;

import com.cwa.component.data.IDBService;
import com.cwa.component.data.IDBSession;
import com.cwa.data.config.INettyServiceConfigDao;
import com.cwa.data.config.domain.NettyServiceConfig;
import com.cwa.handler.IClosedSessionHandler;
import com.cwa.handler.ICreateSessionHandler;
import com.cwa.handler.IMessageHandler;
import com.cwa.message.IConfigMessage;
import com.cwa.net.netty.NettyServer;
import com.cwa.net.netty.handler.NettyHandler;
import com.cwa.net.netty.message.NettyMessageType;
import com.cwa.net.netty.message.NettySendMessage;
import com.cwa.service.constant.ServiceConstant;
import com.cwa.service.context.FilterContext;
import com.cwa.service.context.IGloabalContext;
import com.cwa.service.init.services.NettyService;
import com.cwa.util.NetUtil;

/**
 * 初始化netty服务
 * 
 * @author mausmars
 *
 */
public class NettyServiceFactory implements IServiceFactory {
	private static final Logger logger = LoggerFactory.getLogger(IServiceFactory.class);

	private IConfigMessage configMessage;
	private IMessageHandler<Object> messageHandler;

	private List<IClosedSessionHandler> closeSessionHandlerList;
	private List<ICreateSessionHandler> createSessionHandlerList;

	@Override
	public void createService(FilterContext c) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("--- InitNettyService ---");
		}
		IGloabalContext gloabalContext = c.getGloabalContext();
		IDBService dbService = (IDBService) gloabalContext.getService(ServiceConstant.General_Gid, ServiceConstant.DatabaseKey);// 配置服数据库session
		IDBSession configDBSession = dbService.getDBSession(ServiceConstant.General_Rid);

		INettyServiceConfigDao dao = (INettyServiceConfigDao) configDBSession.getEntityDao(NettyServiceConfig.class);
		List<? extends IEntity> serviceConfigs = (List<? extends IEntity>) dao.selectEntityByGidAndFtype(gloabalContext.getGid(),
				gloabalContext.getFunctionType(), configDBSession.getParams(ServiceConstant.General_Rid));

		if (serviceConfigs.isEmpty()) {
			// 如果是空就返回
			return;
		}
		// 这里暂时是一个后边是多个再改
		NettyServiceConfig serviceConfig = (NettyServiceConfig) serviceConfigs.get(0);

		if (serviceConfig.available != 1) {
			// 如果不可用
			return;
		}

		// ---- sendMessage配置 -----
		NettySendMessage sendMessage = new NettySendMessage();
		sendMessage.setConfigMessage(configMessage);

		// ---- handler配置 -----
		NettyHandler handler = new NettyHandler();
		handler.setSessionManager(sendMessage);
		handler.setMessageHandler(messageHandler);
		handler.setCloseSessionHandlerList(closeSessionHandlerList);
		handler.setCreateSessionHandlerList(createSessionHandlerList);

		// ---- 服务配置 -----
		checkAndModifyAddress(gloabalContext.getOutsideNetIp(), serviceConfig);

		NettyServer server = new NettyServer();
		server.setPort(serviceConfig.listening);
		server.setReaderIdleTimeSeconds(serviceConfig.readerIdleTime);
		server.setWriterIdleTimeSeconds(serviceConfig.writerIdleTime);
		server.setAllIdleTimeSeconds(serviceConfig.allIdleTime);
		server.setHandlerThreadNum(serviceConfig.handlerThreadNum);
		server.setSubReactorThreadNum(serviceConfig.subReactorThreadNum);
		server.setMessageType(new NettyMessageType());
		server.setHandler(handler);
		server.setSessionManager(sendMessage);

		// ---- 简单封装 NettyService -----
		NettyService service = new NettyService(serviceConfig.key);
		service.setVersion(serviceConfig.version);
		service.setServer(server);

		service.setGloabalContext(gloabalContext);
		service.setSendMessage(sendMessage);
		// 插入服务
		gloabalContext.insertIService(gloabalContext.getGid(), service);
	}

	private void checkAndModifyAddress(String ip, NettyServiceConfig serviceConfig) {
		for (;;) {
			boolean isAvailable = NetUtil.isPortAvailable(ip, serviceConfig.listening);
			if (!isAvailable) {
				serviceConfig.listening++;
			} else {
				break;
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}

	// ------------------------------
	public void setConfigMessage(IConfigMessage configMessage) {
		this.configMessage = configMessage;
	}

	public void setMessageHandler(IMessageHandler<Object> messageHandler) {
		this.messageHandler = messageHandler;
	}

	public void setCloseSessionHandlerList(List<IClosedSessionHandler> closeSessionHandlerList) {
		this.closeSessionHandlerList = closeSessionHandlerList;
	}

	public void setCreateSessionHandlerList(List<ICreateSessionHandler> createSessionHandlerList) {
		this.createSessionHandlerList = createSessionHandlerList;
	}
}
