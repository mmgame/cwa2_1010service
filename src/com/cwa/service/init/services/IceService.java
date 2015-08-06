package com.cwa.service.init.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import serverice.config.ServiceConfigTypeEnum;
import baseice.service.FunctionMenu;
import baseice.service.ServiceInfo;

import com.cwa.service.IService;
import com.cwa.service.ServiceUtil;
import com.cwa.service.init.IIceServer;
import com.cwa.service.service.interf.IMasterServiceI;

/**
 * ice 服务
 * 
 * @author mausmars
 *
 */
public class IceService implements IMasterServiceI, IService {
	private static final Logger logger = LoggerFactory.getLogger(IService.class);

	private String key;
	private int version;

	private List<Integer> groupIds;
	// 本服的功能菜单
	private FunctionMenu functionMenu;

	private IIceServer iceServer;

	// 是否启动
	private boolean isStarted;

	public IceService(String key) {
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
		return ServiceConfigTypeEnum.Iec.value();
	}

	@Override
	public void startup() throws Exception {
		if (!isStarted) {
			if (logger.isInfoEnabled()) {
				logger.info("IceService start! key=" + key);
			}
			isStarted = true;
			if (logger.isInfoEnabled()) {
				logger.info("IceService start end! key" + key);
				ServiceUtil.functionMenuPrint("IceLocalService", functionMenu);
			}
		}
	}

	@Override
	public void shutdown() throws Exception {
		if (functionMenu == null) {
			return;
		}
		// 获取适配器
		Ice.ObjectAdapter adapter = iceServer.getObjectAdapter(functionMenu.fa.adapterName);
		if (adapter == null) {
			return;
		}
		for (ServiceInfo serviceInfo : functionMenu.serviceInfos.values()) {
			// 移除服务
			adapter.remove(iceServer.getCommunicator().stringToIdentity(serviceInfo.interfcName));
		}
	}

	@Override
	public FunctionMenu getFunctionMenu() {
		return functionMenu;
	}

	// ----------------------------------------------
	public void setVersion(int version) {
		this.version = version;
	}

	public void setFunctionMenu(FunctionMenu functionMenu) {
		this.functionMenu = functionMenu;
	}

	public void setIceServer(IIceServer iceServer) {
		this.iceServer = iceServer;
	}

	public List<Integer> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(List<Integer> groupIds) {
		this.groupIds = groupIds;
	}
}
