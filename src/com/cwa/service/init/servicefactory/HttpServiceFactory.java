package com.cwa.service.init.servicefactory;

import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import baseice.basedao.IEntity;

import com.cwa.component.data.IDBService;
import com.cwa.component.data.IDBSession;
import com.cwa.data.config.IHttpServiceConfigDao;
import com.cwa.data.config.domain.HttpServiceConfig;
import com.cwa.service.constant.ServiceConstant;
import com.cwa.service.context.FilterContext;
import com.cwa.service.context.IGloabalContext;
import com.cwa.service.init.services.HttpService;

/**
 * 初始化http服务
 * 
 * @author mausmars
 *
 */
public class HttpServiceFactory implements IServiceFactory {
	private static final Logger logger = LoggerFactory.getLogger(IServiceFactory.class);
	private Map<String, HttpServlet> servletMap;
	private Map<String, Class<Filter>> filterMap;
	private ServletContextHandler context;

	@Override
	public void createService(FilterContext c) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("--- InitHttpService ---");
		}
		IGloabalContext gloabalContext = c.getGloabalContext();
		IDBService dbService = (IDBService) gloabalContext.getService(ServiceConstant.General_Gid, ServiceConstant.DatabaseKey);// 配置服数据库session
		IDBSession configDBSession = dbService.getDBSession(ServiceConstant.General_Rid);

		IHttpServiceConfigDao dao = (IHttpServiceConfigDao) configDBSession.getEntityDao(HttpServiceConfig.class);
		List<? extends IEntity> serviceConfigs = (List<? extends IEntity>) dao.selectEntityByGidAndFtype(gloabalContext.getGid(),
				gloabalContext.getFunctionType(), configDBSession.getParams(ServiceConstant.General_Rid));

		if (serviceConfigs.isEmpty()) {
			// 如果是空就返回
			return;
		}
		// 这里暂时是一个后边是多个再改
		HttpServiceConfig serviceConfig = (HttpServiceConfig) serviceConfigs.get(0);
		if (serviceConfig.available != 1) {
			// 如果不可用
			return;
		}

		HttpService httpService = new HttpService(serviceConfig.key);
		httpService.setPort(serviceConfig.listening);
		httpService.setMaxThreads(serviceConfig.maxpool);
		httpService.setMinThreads(serviceConfig.minpool);
		httpService.setFilterMap(filterMap);
		httpService.setServletMap(servletMap);
		httpService.setContext(context);

		// 插入服务
		gloabalContext.insertIService(gloabalContext.getGid(), httpService);
	}

	// --------------------------------------------------------
	public void setServletMap(Map<String, HttpServlet> servletMap) {
		this.servletMap = servletMap;
	}

	public void setFilterMap(Map<String, Class<Filter>> filterMap) {
		this.filterMap = filterMap;
	}

	public void setContext(ServletContextHandler context) {
		this.context = context;
	}

}
