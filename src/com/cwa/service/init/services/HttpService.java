package com.cwa.service.init.services;

import java.util.EnumSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import serverice.config.ServiceConfigTypeEnum;

import com.cwa.service.IService;

/**
 * 对Jetty容器的简单封装。
 */
public class HttpService implements IService {
	protected static final Logger logger = LoggerFactory.getLogger(HttpService.class);

	private String ip;
	private int port; // HTTP监听端口
	private int minThreads;
	private int maxThreads;

	private Server jettyServer;
	private Map<String, HttpServlet> servletMap;
	private Map<String, Class<Filter>> filterMap;

	private ServletContextHandler context;

	// 是否启动
	private boolean isStarted;

	private String key;
	private int version;

	public HttpService(String key) {
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
		return ServiceConfigTypeEnum.Http.value();
	}

	@Override
	public void startup() throws Exception {
		if (!isStarted) {
			if (logger.isInfoEnabled()) {
				logger.info("HttpService start! key=" + key);
			}
			start();
			isStarted = true;
			if (logger.isInfoEnabled()) {
				logger.info("HttpService start end! key=" + key);
			}
		}
	}

	@Override
	public void shutdown() throws Exception {
		stop();
	}

	private void startServer() throws Exception {
		jettyServer = new Server();

		QueuedThreadPool pool = new QueuedThreadPool();
		pool.setMinThreads(minThreads);
		pool.setMaxThreads(maxThreads);
		jettyServer.setThreadPool(pool);

		Connector connector = new SelectChannelConnector();
		connector.setPort(port);
		if (ip != null && !ip.isEmpty()) {
			connector.setHost(ip);
		}
		jettyServer.addConnector(connector);
		
		jettyServer.setHandler(context);

		if (filterMap != null) {
			Set<Entry<String, Class<Filter>>> filters = filterMap.entrySet();
			for (Entry<String, Class<Filter>> filter : filters) {
				FilterHolder filterHolder = new FilterHolder(filter.getValue());
				EnumSet<DispatcherType> enumSet = EnumSet.of(DispatcherType.FORWARD);
				context.addFilter(filterHolder, filter.getKey(), enumSet);
			}
		}

		if (servletMap != null) {
			Set<Entry<String, HttpServlet>> servlets = servletMap.entrySet();
			for (Entry<String, HttpServlet> servlet : servlets) {
				// 注册servlet
				context.addServlet(new ServletHolder(servlet.getValue()), servlet.getKey());
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("Start HTTP server: " + (ip == null ? "0.0.0.0" : ip) + ":" + port);
		}
		jettyServer.start();
		jettyServer.join();
	}

	public void start() {
		Thread thread = new Thread() {
			public void run() {
				try {
					startServer();
				} catch (Exception e) {
					logger.error("start httpserver is Error!", e);
					System.exit(0);
				}
			}
		};
		thread.start();
	}

	public void stop() throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("Stop HTTP server.");
		}
		jettyServer.stop();
		if (logger.isInfoEnabled()) {
			logger.info("Server stopped.");
		}
	}

	public String getIp() {
		return this.ip;
	}

	public int getPort() {
		return this.port;
	}

	// ---------------------------------
	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setMinThreads(int minThreads) {
		this.minThreads = minThreads;
	}

	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	public void setContext(ServletContextHandler context) {
		this.context = context;
	}

	public void setServletMap(Map<String, HttpServlet> servletMap) {
		this.servletMap = servletMap;
	}

	public void setFilterMap(Map<String, Class<Filter>> filterMap) {
		this.filterMap = filterMap;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
