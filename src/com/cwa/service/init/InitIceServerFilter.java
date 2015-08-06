package com.cwa.service.init;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import Ice.Communicator;
import Ice.ObjectAdapter;
import Ice.ObjectFactory;
import baseice.constant.SeparatorColon;
import baseice.service.FunctionAddress;
import baseice.service.IMasterServicePrx;
import baseice.service.IMasterServicePrxHelper;

import com.cwa.service.context.FilterContext;
import com.cwa.service.context.IGloabalContext;
import com.cwa.service.filter.AbstractFilter;

/**
 * 初始化ice主服务
 * 
 * @author mausmars
 * 
 */
public class InitIceServerFilter extends AbstractFilter implements IIceServer {
	private String[] args = { "--Ice.Config=propertiesconfig/config_server_ice.properties" };
	private Ice.Communicator communicator = Ice.Util.initialize(args);

	// {适配器名:适配器}
	private Map<String, ObjectAdapter> objectAdapterMap = new HashMap<String, ObjectAdapter>();

	private String IceUrl_Template = "%s -h %s -p %d";
	// -------------------------------------------
	// 主接口名
	private String masterInterfaceName;
	// ice对象工厂
	private Map<String, ObjectFactory> objectFactoryMap;

	// -------------------------------------------
	public InitIceServerFilter() {
		super("InitIceMasterServiceFilter");
	}

	@Override
	public boolean doWork(Object context) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("--- InitIceMasterServiceFilter ---");
		}
		FilterContext c = (FilterContext) context;
		IGloabalContext gloabalContext = c.getGloabalContext();

		// 初始化ice服务
		initIceService();
		// ----------------------------------------
		gloabalContext.setIceServer(this);
		return true;
	}

	private void initIceService() {
		// ----------------------------------------
		// 初始化对象工厂
		if (objectFactoryMap != null && !objectFactoryMap.isEmpty()) {
			Set<Entry<String, ObjectFactory>> factorySet = objectFactoryMap.entrySet();
			for (Entry<String, ObjectFactory> entry : factorySet) {
				communicator.addObjectFactory(entry.getValue(), entry.getKey());
			}
		}
		// 启动ice
		Thread thread = new Thread() {
			public void run() {
				communicator.waitForShutdown();
			}
		};
		thread.setName("StartIceServiceThread");
		thread.start();
	}

	@Override
	public ObjectAdapter getObjectAdapter(String adapterName) {
		return objectAdapterMap.get(adapterName);
	}

	@Override
	public IMasterServicePrx getMasterServicePrx(FunctionAddress fa) {
		String url = masterInterfaceName + SeparatorColon.value + String.format(IceUrl_Template, fa.protocol.name(), fa.ip, fa.port);
		IMasterServicePrx masterService = IMasterServicePrxHelper.uncheckedCast(communicator.stringToProxy(url));
		return masterService;
	}

	@Override
	public void shutdown() throws Exception {
		try {
			communicator.destroy();
			if (logger.isInfoEnabled()) {
				logger.info("stop Ice service over!");
			}
		} catch (Exception e) {
			logger.error("IceService unregisterService is error!", e);
		}
	}

	@Override
	public String getUrl(FunctionAddress fa) {
		return String.format(IceUrl_Template, fa.protocol.name(), fa.ip, fa.port);
	}

	@Override
	public String getUrl(String protocol, String ip, int port) {
		return String.format(IceUrl_Template, protocol, ip, port);
	}

	@Override
	public ObjectAdapter getObjectAdapterOrCreate(String adapterName, String url) {
		if (objectAdapterMap.containsKey(adapterName)) {
			return objectAdapterMap.get(adapterName);
		} else {
			Ice.ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints(adapterName, url);
			objectAdapterMap.put(adapterName, adapter);
			return adapter;
		}
	}

	@Override
	public String getMasterInterfaceName() {
		return masterInterfaceName;
	}

	@Override
	public Communicator getCommunicator() {
		return communicator;
	}

	// ------------------------------------------
	public void setMasterInterfaceName(String masterInterfaceName) {
		this.masterInterfaceName = masterInterfaceName;
	}

	public void setObjectFactoryMap(Map<String, ObjectFactory> objectFactoryMap) {
		this.objectFactoryMap = objectFactoryMap;
	}
}
