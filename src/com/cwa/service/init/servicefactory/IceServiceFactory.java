package com.cwa.service.init.servicefactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import serverice.config.ServerInfo;
import Ice.Communicator;
import Ice.ObjectImpl;
import Ice.ObjectPrx;
import baseice.basedao.IEntity;
import baseice.constant.SeparatorColon;
import baseice.service.FunctionAddress;
import baseice.service.FunctionId;
import baseice.service.FunctionMenu;
import baseice.service.NetProtocolEnum;
import baseice.service.ServiceInfo;

import com.cwa.component.data.IDBService;
import com.cwa.component.data.IDBSession;
import com.cwa.data.config.IIecServiceConfigDao;
import com.cwa.data.config.domain.IecServiceConfig;
import com.cwa.service.ServiceUtil;
import com.cwa.service.constant.ServiceConstant;
import com.cwa.service.context.FilterContext;
import com.cwa.service.context.IGloabalContext;
import com.cwa.service.init.IIceServer;
import com.cwa.service.init.services.IceService;
import com.cwa.service.service.MasterServiceI;
import com.cwa.util.NetUtil;

/**
 * ice服务远端配置初始化
 * 
 * @author mausmars
 *
 */
public class IceServiceFactory implements IServiceFactory {
	private static final Logger logger = LoggerFactory.getLogger(IServiceFactory.class);

	private Communicator communicator;
	// -----------------------------------
	// {服务名:ice实现类}
	private Map<String, ObjectImpl> serviceImplMap;

	@Override
	public void createService(FilterContext c) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("--- InitIceService ---");
		}
		IGloabalContext gloabalContext = c.getGloabalContext();
		IIceServer iceServer = gloabalContext.getIceServer();

		communicator = iceServer.getCommunicator();
		// ---------------------------------------
		ServerInfo serverInfo = (ServerInfo) c.getAttach(ServerInfo.class);// 配置服数据库session
		IDBService dbService = (IDBService) gloabalContext.getService(ServiceConstant.General_Gid, ServiceConstant.DatabaseKey);// 配置服数据库session
		IDBSession configDBSession = dbService.getDBSession(ServiceConstant.General_Rid);

		IIecServiceConfigDao dao = (IIecServiceConfigDao) configDBSession.getEntityDao(IecServiceConfig.class);
		List<? extends IEntity> serviceConfigs = (List<? extends IEntity>) dao.selectEntityByGidAndFtype(gloabalContext.getGid(),
				gloabalContext.getFunctionType(), configDBSession.getParams(ServiceConstant.General_Rid));

		if (serviceConfigs.isEmpty()) {
			// 如果是空就返回
			return;
		}
		// 这里暂时是一个后边是多个再改
		IecServiceConfig serviceConfig = (IecServiceConfig) serviceConfigs.get(0);

		if (serviceConfig.available != 1) {
			// 如果不可用
			return;
		}
		IceService iceService = new IceService(serviceConfig.key);
		iceService.setVersion(serviceConfig.version);
		iceService.setIceServer(iceServer);
		iceService.setGroupIds(serviceConfig.getGroupIdsList());

		// 检查端口是否可用
		serviceConfig.port = checkAndModifyAddress(serverInfo.address, serviceConfig.port);
		// 设置主服务
		initMasterService(serviceConfig, serverInfo, iceService, iceServer);
		// 其他功能
		FunctionMenu functionMenu = createFunctionMenu(serviceConfig, serverInfo, gloabalContext);
		// 创建servjce 的 ice服务
		initOtherService(iceService, functionMenu, iceServer);
		iceService.setFunctionMenu(functionMenu);
		// 插入服务
		gloabalContext.insertIService(gloabalContext.getGid(), iceService);
	}

	private FunctionMenu createFunctionMenu(IecServiceConfig serviceConfig, ServerInfo serverInfo, IGloabalContext gloabalContext) {
		FunctionMenu functionMenu = new FunctionMenu();
		functionMenu.fa = new FunctionAddress();
		functionMenu.fa.protocol = NetProtocolEnum.valueOf(serviceConfig.protocol);
		functionMenu.fa.ip = serverInfo.address;
		functionMenu.fa.port = serviceConfig.port;
		functionMenu.fa.adapterName = serviceConfig.adapterName;

		functionMenu.fid = new FunctionId();
		functionMenu.fid.gid = gloabalContext.getGid();
		functionMenu.fid.ftype = gloabalContext.getFunctionType();
		functionMenu.fid.fkey = serverInfo.fid;

		functionMenu.serviceInfos = new HashMap<String, ServiceInfo>();
		Iterator<String> interfacesIt = serviceConfig.getInterfcNamesList().iterator();
		Iterator<String> packageIt = serviceConfig.getPackageNamesList().iterator();
		for (; interfacesIt.hasNext() && packageIt.hasNext();) {
			ServiceInfo serviceInfo = new ServiceInfo();
			serviceInfo.interfcName = interfacesIt.next();
			serviceInfo.packageName = packageIt.next();
			functionMenu.serviceInfos.put(serviceInfo.interfcName + ServiceUtil.PrxPostfix, serviceInfo);
		}
		return functionMenu;
	}

	private void initMasterService(IecServiceConfig serviceConfig, ServerInfo serverInfo, IceService iceService, IIceServer iceServer) {
		// 初始化ice主服务
		MasterServiceI masterService = new MasterServiceI();
		String url = iceServer.getUrl(serviceConfig.protocol, serverInfo.address, serviceConfig.port);
		Ice.ObjectAdapter adapter = iceServer.getObjectAdapterOrCreate(serviceConfig.adapterName, url);
		// 主功能注册到ice
		addFunction(url, adapter, iceServer.getMasterInterfaceName(), masterService);
		// 设置MasterService逻辑类
		masterService.setService(iceService);
	}

	private void initOtherService(IceService iceService, FunctionMenu functionMenu, IIceServer iceServer) {
		String url = iceServer.getUrl(functionMenu.fa);
		// 创建适配器
		Ice.ObjectAdapter adapter = iceServer.getObjectAdapterOrCreate(functionMenu.fa.adapterName, url);

		for (ServiceInfo serviceInfo : functionMenu.serviceInfos.values()) {
			ObjectImpl service = serviceImplMap.get(serviceInfo.interfcName);
			// 服务注册到ice
			ObjectPrx objectPrx = addFunction(url, adapter, serviceInfo.interfcName, service);
			// 服务加到功能菜单中
			serviceInfo.server = objectPrx;
			if (logger.isInfoEnabled()) {
				logger.info("[service activate AdapterName=" + functionMenu.fa.adapterName + " ServiceName=" + serviceInfo.interfcName
						+ " url=" + url + "]");
			}
		}
	}

	private int checkAndModifyAddress(String ip, int port) {
		for (;;) {
			boolean isAvailable = NetUtil.isPortAvailable(ip, port);
			if (!isAvailable) {
				port++;
			} else {
				return port;
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}

	private ObjectPrx addFunction(String url, Ice.ObjectAdapter adapter, String serviceName, ObjectImpl service) {
		// 创建适配器
		adapter.add(service, communicator.stringToIdentity(serviceName));
		// 服务注册到适配器
		adapter.activate();
		url = serviceName + SeparatorColon.value + url;
		return communicator.stringToProxy(url);
	}

	// ---------------------------------------
	public void setServiceImplMap(Map<String, ObjectImpl> serviceImplMap) {
		this.serviceImplMap = serviceImplMap;
	}
}
