package com.cwa.service.init.servicefactory;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.firewall.ConnectionThrottleFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import baseice.basedao.IEntity;

import com.cwa.component.data.IDBService;
import com.cwa.component.data.IDBSession;
import com.cwa.data.config.IMinaServiceConfigDao;
import com.cwa.data.config.domain.MinaServiceConfig;
import com.cwa.handler.IClosedSessionHandler;
import com.cwa.handler.ICreateSessionHandler;
import com.cwa.handler.IMessageHandler;
import com.cwa.message.IConfigMessage;
import com.cwa.net.mina.MinaServer;
import com.cwa.net.mina.filter.MessageCodecFactory;
import com.cwa.net.mina.filter.MessageReceiveDecoder;
import com.cwa.net.mina.filter.MessageSendEncoder;
import com.cwa.net.mina.handler.MinaHandler;
import com.cwa.net.mina.heartbeat.HeartBeatMessageFactory;
import com.cwa.net.mina.heartbeat.HeartBeatRequestTimeoutHandler;
import com.cwa.net.mina.message.MinaMessageType;
import com.cwa.net.mina.message.MinaSendMessage;
import com.cwa.service.constant.ServiceConstant;
import com.cwa.service.context.FilterContext;
import com.cwa.service.context.IGloabalContext;
import com.cwa.service.init.services.MinaService;
import com.cwa.util.NetUtil;

/**
 * 初始化mina服务
 * 
 * @author mausmars
 *
 */
public class MinaServiceFactory implements IServiceFactory {
	private static final Logger logger = LoggerFactory.getLogger(IServiceFactory.class);

	private IConfigMessage configMessage;
	private IMessageHandler<Object> messageHandler;

	private List<IClosedSessionHandler> closeSessionHandlerList;
	private List<ICreateSessionHandler> createSessionHandlerList;

	@Override
	public void createService(FilterContext c) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("--- InitMinaService ---");
		}
		IGloabalContext gloabalContext = c.getGloabalContext();
		IDBService dbService = (IDBService) gloabalContext.getService(ServiceConstant.General_Gid, ServiceConstant.DatabaseKey);// 配置服数据库session
		IDBSession configDBSession = dbService.getDBSession(ServiceConstant.General_Rid);

		IMinaServiceConfigDao dao = (IMinaServiceConfigDao) configDBSession.getEntityDao(MinaServiceConfig.class);
		List<? extends IEntity> serviceConfigs = (List<? extends IEntity>) dao.selectEntityByGidAndFtype(gloabalContext.getGid(),
				gloabalContext.getFunctionType(), configDBSession.getParams(ServiceConstant.General_Rid));

		if (serviceConfigs.isEmpty()) {
			// 如果是空就返回
			return;
		}
		// 这里暂时是一个后边是多个再改
		MinaServiceConfig serviceConfig = (MinaServiceConfig) serviceConfigs.get(0);

		if (serviceConfig.available != 1) {
			// 如果不可用
			return;
		}

		// ---- sendMessage配置 -----
		MinaSendMessage sendMessage = new MinaSendMessage();
		sendMessage.setConfigMessage(configMessage);

		// ---- handler配置 -----
		MinaHandler handler = new MinaHandler();
		handler.setCloseSessionHandlerList(closeSessionHandlerList);
		handler.setCreateSessionHandlerList(createSessionHandlerList);
		handler.setMessageHandler(messageHandler);
		handler.setSessionManager(sendMessage);

		// ---- filter配置 -----
		// filter线程池
		ExecutorFilter executorFilter = new ExecutorFilter(serviceConfig.minpool, serviceConfig.maxpool);
		// filter日志
		LoggingFilter loggingFilter = new LoggingFilter();

		// filter编码解码
		MessageSendEncoder encoder = new MessageSendEncoder();
		MessageReceiveDecoder decoder = new MessageReceiveDecoder();
		decoder.setMaxPackSize(serviceConfig.messagemaxpackage);
		decoder.setMessageType(new MinaMessageType());
		MessageCodecFactory messageCodecFactory = new MessageCodecFactory(encoder, decoder);
		ProtocolCodecFilter protocolCodecFilter = new ProtocolCodecFilter(messageCodecFactory);

		// filter 心跳配置
		HeartBeatMessageFactory heartBeatMessageFactory = new HeartBeatMessageFactory();
		heartBeatMessageFactory.setHeartbeatId(serviceConfig.heartbeatId);

		HeartBeatRequestTimeoutHandler heartBeatRequestTimeoutHandler = new HeartBeatRequestTimeoutHandler();

		KeepAliveFilter keepAliveFilter = new KeepAliveFilter(heartBeatMessageFactory, IdleStatus.BOTH_IDLE, heartBeatRequestTimeoutHandler);
		keepAliveFilter.setForwardEvent(serviceConfig.forwardEvent == 1);// 是否回复
		keepAliveFilter.setRequestInterval(serviceConfig.heartbeatrate);// 回复频率
		keepAliveFilter.setRequestTimeout(serviceConfig.ideltimeout);// 心跳超时

		// filter 控制客户端请求服务器频率的过滤器,简易防止网络攻击
		ConnectionThrottleFilter connectionThrottleFilter = new ConnectionThrottleFilter();
		connectionThrottleFilter.setAllowedInterval(serviceConfig.allowedInterval);

		// filter链
		LinkedHashMap<String, IoFilter> filters = new LinkedHashMap<String, IoFilter>();
		filters.put("protocolCodecFilter", protocolCodecFilter);
		filters.put("logger", loggingFilter);
		filters.put("heartbeat", keepAliveFilter);
		filters.put("exceutor", executorFilter);

		// ---- 服务配置 -----
		checkAndModifyAddress(gloabalContext.getOutsideNetIp(), serviceConfig);

		MinaServer minaServer = new MinaServer();
		minaServer.setPort(serviceConfig.listening);
		minaServer.setFilters(filters);
		minaServer.setGameHandler(handler);
		minaServer.setSessionManager(sendMessage);

		// ---- 简单封装 MinaService -----
		MinaService service = new MinaService(serviceConfig.key);
		service.setVersion(serviceConfig.version);
		service.setServer(minaServer);

		service.setGloabalContext(gloabalContext);
		service.setSendMessage(sendMessage);
		// 插入服务
		gloabalContext.insertIService(gloabalContext.getGid(), service);
	}

	private void checkAndModifyAddress(String ip, MinaServiceConfig serviceConfig) {
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

	// --------------------------------------------------
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
