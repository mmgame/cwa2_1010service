package com.cwa.service.init.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import serverice.config.ServiceConfigTypeEnum;

import com.cwa.component.datatimeout.ConnectCallBack;
import com.cwa.component.datatimeout.DataTimeoutTask;
import com.cwa.component.datatimeout.IDataTimeoutService;
import com.cwa.net.ClientSendMessage;
import com.cwa.net.netty.NettyServer;
import com.cwa.service.IService;
import com.cwa.service.constant.ServiceConstant;
import com.cwa.service.context.IGloabalContext;

/**
 * 简单封装 NettyService
 * 
 * @author mausmars
 *
 */
public class NettyService implements INetService {
	private static final Logger logger = LoggerFactory.getLogger(IService.class);

	private String key;
	private int version;
	private NettyServer server;

	private ClientSendMessage sendMessage;
	private IGloabalContext gloabalContext;

	private DataTimeoutTask dataTimeoutTask;

	// 是否启动
	private boolean isStarted;

	public NettyService(String key) {
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
		return ServiceConfigTypeEnum.Netty.value();
	}

	@Override
	public void startup() throws Exception {
		if (!isStarted) {
			if (logger.isInfoEnabled()) {
				logger.info("NettyService start! key=" + key + " " + server.toString());
			}
			IDataTimeoutService dataTimeoutService = (IDataTimeoutService) gloabalContext
					.getCurrentService(ServiceConstant.NetDataTimeoutKey);
			if (dataTimeoutService.getTimeout() > 0) {
				dataTimeoutTask = dataTimeoutService.createTask("session_timeoutcheck_" + key);
				// 超时设置
				ConnectCallBack callBack = new ConnectCallBack();
				callBack.setDataTimeoutTask(dataTimeoutTask);
				sendMessage.setCallBack(callBack);
			}
			Thread thread = new Thread() {
				public void run() {
					server.startup();
				}
			};
			thread.start();
			isStarted = true;
			if (logger.isInfoEnabled()) {
				logger.info("NettyService start end! key=" + key + " " + server.toString());
			}
		}
	}

	@Override
	public void shutdown() throws Exception {
		server.shutdown();
		if (dataTimeoutTask != null) {
			IDataTimeoutService dataTimeoutService = (IDataTimeoutService) gloabalContext
					.getCurrentService(ServiceConstant.NetDataTimeoutKey);
			dataTimeoutService.removeTask(dataTimeoutTask.id());
		}
	}

	public NettyServer getServer() {
		return server;
	}

	@Override
	public void removeTimeoutCheck(String key) {
		if (dataTimeoutTask != null) {
			dataTimeoutTask.removeTimeoutCheck(key);
		}
	}

	// ------------------------------------------------
	public void setVersion(int version) {
		this.version = version;
	}

	public void setServer(NettyServer server) {
		this.server = server;
	}

	public void setSendMessage(ClientSendMessage sendMessage) {
		this.sendMessage = sendMessage;
	}

	public void setGloabalContext(IGloabalContext gloabalContext) {
		this.gloabalContext = gloabalContext;
	}
}
