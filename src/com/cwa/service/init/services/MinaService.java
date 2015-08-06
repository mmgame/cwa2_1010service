package com.cwa.service.init.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import serverice.config.ServiceConfigTypeEnum;

import com.cwa.component.datatimeout.ConnectCallBack;
import com.cwa.component.datatimeout.DataTimeoutService;
import com.cwa.component.datatimeout.DataTimeoutTask;
import com.cwa.component.datatimeout.IDataTimeoutService;
import com.cwa.net.ClientSendMessage;
import com.cwa.net.mina.MinaServer;
import com.cwa.service.IService;
import com.cwa.service.constant.ServiceConstant;
import com.cwa.service.context.IGloabalContext;

/**
 * 简单封装 MinaService
 * 
 * @author mausmars
 *
 */
public class MinaService implements INetService {
	private static final Logger logger = LoggerFactory.getLogger(IService.class);

	private String key;
	private int version;
	private MinaServer server;

	private ClientSendMessage sendMessage;
	private IGloabalContext gloabalContext;

	private DataTimeoutTask dataTimeoutTask;

	// 是否启动
	private boolean isStarted;

	public MinaService(String key) {
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
		return ServiceConfigTypeEnum.Mina.value();
	}

	@Override
	public void startup() throws Exception {
		if (!isStarted) {
			if (logger.isInfoEnabled()) {
				logger.info("MinaService start! key=" + key + " " + server.toString());
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
			server.startup();
			isStarted = true;
			if (logger.isInfoEnabled()) {
				logger.info("MinaService start end! key=" + key + " " + server.toString());
			}
		}
	}

	@Override
	public void shutdown() throws Exception {
		server.shutdown();
		if (dataTimeoutTask != null) {
			DataTimeoutService dataTimeoutService = (DataTimeoutService) gloabalContext
					.getCurrentService(ServiceConstant.NetDataTimeoutKey);
			dataTimeoutService.removeTask(dataTimeoutTask.id());
		}
	}

	@Override
	public void removeTimeoutCheck(String key) {
		if (dataTimeoutTask != null) {
			dataTimeoutTask.removeTimeoutCheck(key);
		}
	}

	public MinaServer getServer() {
		return server;
	}

	// ---------------------------------------------
	public void setVersion(int version) {
		this.version = version;
	}

	public void setServer(MinaServer server) {
		this.server = server;
	}

	public void setSendMessage(ClientSendMessage sendMessage) {
		this.sendMessage = sendMessage;
	}

	public void setGloabalContext(IGloabalContext gloabalContext) {
		this.gloabalContext = gloabalContext;
	}
}
