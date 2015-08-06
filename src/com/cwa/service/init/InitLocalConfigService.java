package com.cwa.service.init;

import com.cwa.data.config.domain.FunctionServiceConfig;
import com.cwa.data.config.domain.FunctionZKInfoConfig;
import com.cwa.service.context.FilterContext;
import com.cwa.service.context.IGloabalContext;
import com.cwa.service.filter.AbstractFilter;
import com.cwa.service.init.servicefactory.IZKFunctionServiceFactory;

/**
 * 初始化本地配置
 * 
 * @author mausmars
 * 
 */
public class InitLocalConfigService extends AbstractFilter {
	private IZKFunctionServiceFactory serviceFactory;

	private String groupFilter;
	private String functionFilter;
	private String hosts;
	private int sessionTimeout;
	private String rootPath;

	public InitLocalConfigService() {
		super("InitCreateConfig");
	}

	@Override
	public boolean doWork(Object context) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("--- InitLocalConfig ---");
		}
		FilterContext c = (FilterContext) context;
		// ------------------------------------
		IGloabalContext gloabalContext = c.getGloabalContext();

		FunctionServiceConfig serviceConfig = new FunctionServiceConfig();
		serviceConfig.getGroupIdsList().add(gloabalContext.getGid());

		serviceConfig.key = "localZKConfig";
		serviceConfig.version = Integer.MAX_VALUE;
		serviceConfig.functionFilters = functionFilter;
		serviceConfig.groupFilters = groupFilter;

		FunctionZKInfoConfig info = new FunctionZKInfoConfig();
		info.hosts = hosts;
		info.sessionTimeout = sessionTimeout;
		info.rootPath = rootPath;

		// 初始化当前服务配置
		serviceFactory.createService(serviceConfig, info, gloabalContext);
		// ------------------------------------
		return true;
	}

	// -----------------------------------------
	public void setServiceFactory(IZKFunctionServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	public void setGroupFilter(String groupFilter) {
		this.groupFilter = groupFilter;
	}

	public void setFunctionFilter(String functionFilter) {
		this.functionFilter = functionFilter;
	}

	public void setHosts(String hosts) {
		this.hosts = hosts;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
}
