package com.cwa.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cwa.component.data.IDBService;
import com.cwa.component.functionmanage.IFunctionService;
import com.cwa.component.membermanager.IMemberService;
import com.cwa.service.context.FilterContext;
import com.cwa.service.context.IGloabalContext;
import com.cwa.service.filter.IFilterChain;
import com.cwa.service.init.IIceServer;
import com.cwa.util.NetUtil;

/**
 * 服务启动类
 * 
 * @author mausmars
 * 
 */
public class ServerService implements IGloabalContext {
	protected static final Logger logger = LoggerFactory.getLogger(ServerService.class);

	// 当前服组id
	private int gid;
	// 功能类型
	private int functionType;

	private long startTime;
	private long version;
	// 初始化链
	private IFilterChain initFilterChain;

	// 模块服启动接口
	private IModuleServer moduleServer;
	// -------------------------------------
	private IIceServer iceServer;

	// {组id:IFunctionService}
	private Map<Integer, IFunctionService> functionServiceMap = new HashMap<Integer, IFunctionService>();
	// {组id:IMemberService}
	private Map<Integer, IMemberService> memberServiceMap = new HashMap<Integer, IMemberService>();
	// {组id:IDBService}
	private Map<Integer, IDBService> dbServiceMap = new HashMap<Integer, IDBService>();
	// {组id:{key:IService}}
	private Map<Integer, Map<String, IService>> serviceMap = new HashMap<Integer, Map<String, IService>>();

	private String outsideNetIp;
	private String matchingNetwork;

	public void startup() {
		try {
			startTime = System.currentTimeMillis();

			outsideNetIp = NetUtil.getOutsideNetIp(matchingNetwork);

			FilterContext filterContext = new FilterContext(this);
			initFilterChain.doFilter(null, filterContext);

			if (logger.isInfoEnabled()) {
				logger.info("### ServerService start all service! ### ");
			}
			for (Map<String, IService> services : serviceMap.values()) {
				for (IService service : services.values()) {
					// 启动服务
					service.startup();
				}
			}
			if (moduleServer != null) {
				moduleServer.startup(this);
			}
			if (logger.isInfoEnabled()) {
				logger.info("### ServerService start end all service! ### ");
			}
		} catch (Exception e) {
			logger.error("", e);
			// 初始化失败就退出程序
			System.exit(0);
		}
	}

	public void shutdown() {
		try {
			// 停止服务
			for (Map<String, IService> services : serviceMap.values()) {
				for (IService service : services.values()) {
					try {
						// 关闭服务
						service.shutdown();
					} catch (Exception e) {
						logger.error("", e);
					}
				}
			}
			if (iceServer != null) {
				try {
					// 关闭ice服务
					iceServer.shutdown();
				} catch (Exception e) {
					logger.error("", e);
				}
			}
			if (moduleServer != null) {
				moduleServer.shutdown();
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	// --------------
	@Override
	public IFunctionService getFunctionService(int gid) {
		return functionServiceMap.get(gid);
	}

	@Override
	public List<IFunctionService> getAllFunctionService() {
		List<IFunctionService> services = new LinkedList<IFunctionService>();
		services.addAll(functionServiceMap.values());
		return services;
	}

	@Override
	public void insertFunctionService(int gid, IFunctionService service) {
		functionServiceMap.put(gid, service);
		insertIService(gid, service);
	}

	@Override
	public boolean isContainFunctionService(int gid) {
		return functionServiceMap.containsKey(gid);
	}

	@Override
	public IFunctionService getCurrentFunctionService() {
		return functionServiceMap.get(this.gid);
	}

	// --------------
	@Override
	public void insertMemberService(int gid, IMemberService service) {
		memberServiceMap.put(gid, service);
		insertIService(gid, service);
	}

	@Override
	public IMemberService getMemberService(int gid) {
		return memberServiceMap.get(gid);
	}

	@Override
	public List<IMemberService> getAllMemberService() {
		List<IMemberService> services = new LinkedList<IMemberService>();
		services.addAll(memberServiceMap.values());
		return services;
	}

	@Override
	public IMemberService getCurrentMemberService() {
		return memberServiceMap.get(this.gid);
	}

	@Override
	public boolean isContainMemberService(int gid) {
		return memberServiceMap.containsKey(gid);
	}

	// --------------
	@Override
	public IDBService getDBService(int gid) {
		return dbServiceMap.get(gid);
	}

	@Override
	public List<IDBService> getAllDBService() {
		List<IDBService> services = new LinkedList<IDBService>();
		services.addAll(dbServiceMap.values());
		return services;
	}

	@Override
	public IDBService getCurrentDBService() {
		return dbServiceMap.get(this.gid);
	}

	@Override
	public void insertDBService(int gid, IDBService service) {
		dbServiceMap.put(gid, service);
		insertIService(gid, service);
	}

	@Override
	public boolean isContainDBService(int gid) {
		return dbServiceMap.containsKey(gid);
	}

	// --------------
	@Override
	public IIceServer getIceServer() {
		return iceServer;
	}

	@Override
	public int getGid() {
		return gid;
	}

	@Override
	public int getFunctionType() {
		return functionType;
	}

	@Override
	public long getStartTime() {
		return startTime;
	}

	@Override
	public long getConfigVersion() {
		return version;
	}

	@Override
	public void setConfigVersion(long version) {
		this.version = version;
	}

	@Override
	public void insertIService(int gid, IService service) {
		Map<String, IService> services = getServcies(gid);
		services.put(service.getKey(), service);
	}

	@Override
	public IService getCurrentService(String key) {
		return getService(this.gid, key);
	}

	@Override
	public IService getService(int gid, String key) {
		Map<String, IService> services = getServcies(gid);
		return services.get(key);
	}

	@Override
	public List<IService> getCurrentServices(int serviceType) {
		return getServices(this.gid, serviceType);
	}

	@Override
	public List<IService> getServices(int gid, int serviceType) {
		Map<String, IService> services = getServcies(gid);
		List<IService> serviceList = new LinkedList<IService>();
		for (IService service : services.values()) {
			if (service.getServiceType() != serviceType) {
				continue;
			}
			serviceList.add(service);
		}
		return serviceList;
	}

	@Override
	public void setIceServer(IIceServer iceServer) {
		this.iceServer = iceServer;
	}

	@Override
	public String getOutsideNetIp() {
		return outsideNetIp;
	}

	private Map<String, IService> getServcies(int gid) {
		Map<String, IService> services = serviceMap.get(gid);
		if (services == null) {
			services = new HashMap<String, IService>();
			serviceMap.put(gid, services);
		}
		return services;
	}

	public String getMatchingNetwork() {
		return matchingNetwork;
	}

	// ----------------------------------------------
	public void setInitFilterChain(IFilterChain initFilterChain) {
		this.initFilterChain = initFilterChain;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public void setFunctionType(int functionType) {
		this.functionType = functionType;
	}

	public void setModuleServer(IModuleServer moduleServer) {
		this.moduleServer = moduleServer;
	}

	public void setMatchingNetwork(String matchingNetwork) {
		this.matchingNetwork = matchingNetwork;
	}
}
