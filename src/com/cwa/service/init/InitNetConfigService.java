package com.cwa.service.init;

import java.util.LinkedList;
import java.util.List;

import serverice.config.IConfigServicePrx;
import serverice.config.ServerInfo;
import baseice.basedao.IEntity;
import baseice.constant.CommonalityGroupId;
import baseice.service.FunctionTypeEnum;

import com.cwa.component.functionmanage.IFunctionCluster;
import com.cwa.component.functionmanage.IFunctionService;
import com.cwa.data.config.domain.DatabaseInfoConfig;
import com.cwa.data.config.domain.DatabaseServiceConfig;
import com.cwa.service.constant.ServiceConstant;
import com.cwa.service.context.FilterContext;
import com.cwa.service.context.IGloabalContext;
import com.cwa.service.filter.AbstractFilter;
import com.cwa.service.init.servicefactory.IDBServiceFactory;
import com.cwa.service.init.servicefactory.IServiceFactory;

/**
 * 初始化网络配置
 * 
 * @author mausmars
 *
 */
public class InitNetConfigService extends AbstractFilter {
	public InitNetConfigService() {
		super("InitIceCommunicator");
	}

	protected IDBServiceFactory serviceFactory;
	protected List<IServiceFactory> serviceFactorys;

	@Override
	public boolean doWork(Object context) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("--- InitGetNetConfig ---");
		}
		FilterContext c = (FilterContext) context;
		// -----------------------------------------------------
		if (c.getGloabalContext().getFunctionType() != FunctionTypeEnum.Config.value()) {
			initNetConfig1(c);
		} else {
			initNetConfig2(c);
		}

		for (IServiceFactory serviceFactory : serviceFactorys) {
			serviceFactory.createService(c);
		}
		return true;
	}

	protected void initNetConfig2(FilterContext c) throws Exception {
	}

	private void initNetConfig1(FilterContext c) throws Exception {
		IGloabalContext gloabalContext = c.getGloabalContext();

		IFunctionService manager = gloabalContext.getFunctionService(gloabalContext.getGid());
		if (manager == null) {
			throw new RuntimeException("Start error! IFunctionManager is null! ");
		}
		IFunctionCluster functionCluster = manager.getFunctionCluster(CommonalityGroupId.value, FunctionTypeEnum.Config);
		if (functionCluster == null) {
			throw new RuntimeException("Start error! Config server isn't exist! ");
		}
		IConfigServicePrx prx = functionCluster.getMasterService(IConfigServicePrx.class);
		if (prx == null) {
			throw new RuntimeException("Start error! IConfigServicePrx is null! ");
		}
		// 配置
		ServerInfo serverInfo = prx.register(gloabalContext.getGid(), gloabalContext.getFunctionType());
		if (serverInfo == null) {
			throw new RuntimeException("Start error! ServerConfigs is null! [gid=" + gloabalContext.getGid() + " functionType="
					+ gloabalContext.getFunctionType() + "]");
		}
		gloabalContext.setConfigVersion(serverInfo.version);

		DatabaseInfoConfig dbic = (DatabaseInfoConfig) serverInfo.dbInfo;
		dbic.obtainAfter();

		// 初始化网络配置
		c.putAttach(ServerInfo.class, serverInfo);
		createDBService(ServiceConstant.General_Gid, serverInfo, gloabalContext);
	}

	protected void createDBService(int gid, ServerInfo serverInfo, IGloabalContext gloabalContext) throws Exception {
		// 链接配置数据库默认配置
		DatabaseServiceConfig serviceConfig = new DatabaseServiceConfig();
		serviceConfig.key = ServiceConstant.DatabaseKey;
		serviceConfig.acquireIncrement = 3;
		serviceConfig.driverClass = "com.mysql.jdbc.Driver";
		serviceConfig.maxIdleTime = 45;
		serviceConfig.minPool = 1;
		serviceConfig.maxPool = 1;
		serviceConfig.initialPool = 1;
		serviceConfig.maxStatements = 0;
		serviceConfig.maxStatementsPerConnection = 2;
		serviceConfig.checkoutTimeout = 300;

		List<IEntity> infos = new LinkedList<IEntity>();
		infos.add(serverInfo.dbInfo);

		serviceFactory.createService(gid, serviceConfig, infos, gloabalContext);
	}

	// -----------------------------------------------------
	public void setServiceFactory(IDBServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	public void setServiceFactorys(List<IServiceFactory> serviceFactorys) {
		this.serviceFactorys = serviceFactorys;
	}
}
