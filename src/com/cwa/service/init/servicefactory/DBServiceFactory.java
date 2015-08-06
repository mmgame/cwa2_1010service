package com.cwa.service.init.servicefactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import baseice.basedao.IEntity;

import com.cwa.component.data.DBService;
import com.cwa.component.data.DBSession;
import com.cwa.component.data.IDBService;
import com.cwa.component.data.IDBSession;
import com.cwa.component.data.IEntityDao;
import com.cwa.data.config.IDatabaseInfoConfigDao;
import com.cwa.data.config.IDatabaseServiceConfigDao;
import com.cwa.data.config.domain.DatabaseInfoConfig;
import com.cwa.data.config.domain.DatabaseServiceConfig;
import com.cwa.service.constant.ServiceConstant;
import com.cwa.service.context.FilterContext;
import com.cwa.service.context.IGloabalContext;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBServiceFactory implements IDBServiceFactory {
	private static final Logger logger = LoggerFactory.getLogger(IServiceFactory.class);

	// 这里暂时一套dao，如果有需求 这里可以按db的key多几套dao
	private Map<String, IEntityDao> entityDaoMap;

	private final String UrlFormat = "jdbc:mysql://%s:%d/%s?useUnicode=true&amp;characterEncoding=utf-8";

	@Override
	public void createService(FilterContext c) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("--- InitDBService ---");
		}
		IGloabalContext gloabalContext = c.getGloabalContext();
		IDBService dbService = (IDBService) gloabalContext.getService(ServiceConstant.General_Gid, ServiceConstant.DatabaseKey);// 配置服数据库session
		IDBSession configDBSession = dbService.getDBSession(ServiceConstant.General_Rid);

		IDatabaseServiceConfigDao dao = (IDatabaseServiceConfigDao) configDBSession.getEntityDao(DatabaseServiceConfig.class);
		List<? extends IEntity> serviceConfigs = (List<? extends IEntity>) dao.selectEntityByGidAndFtype(gloabalContext.getGid(),
				gloabalContext.getFunctionType(), configDBSession.getParams(ServiceConstant.General_Rid));

		if (serviceConfigs.isEmpty()) {
			// 如果是空就返回
			return;
		}
		// 这里暂时是一个后边是多个再改
		DatabaseServiceConfig serviceConfig = (DatabaseServiceConfig) serviceConfigs.get(0);
		if (serviceConfig.available != 1) {
			// 如果不可用
			return;
		}
		IDatabaseInfoConfigDao infoDao = (IDatabaseInfoConfigDao) configDBSession.getEntityDao(DatabaseInfoConfig.class);

		List<Integer> gids = serviceConfig.getGroupIdsList();
		if (gids.contains(-1)) {
			List<? extends IEntity> infos = infoDao.selectAllEntity(configDBSession.getParams(ServiceConstant.General_Rid));
			for (Entry<Integer, List<IEntity>> es : changMap(infos).entrySet()) {
				if (gloabalContext.isContainDBService(es.getKey())) {
					// 如果已经存在不处理
					continue;
				}
				createService(es.getKey(), serviceConfig, es.getValue(), gloabalContext);
			}
		} else {
			// 指定的服
			for (int gid : gids) {
				if (gloabalContext.isContainDBService(gid)) {
					// 如果已经存在不处理
					continue;
				}
				List<? extends IEntity> infos = infoDao.selectEntityByGid(gid, configDBSession.getParams(ServiceConstant.General_Rid));
				createService(gid, serviceConfig, infos, gloabalContext);
			}
		}
	}

	private Map<Integer, List<IEntity>> changMap(List<? extends IEntity> infos) {
		Map<Integer, List<IEntity>> map = new HashMap<Integer, List<IEntity>>();
		for (IEntity info : infos) {
			DatabaseInfoConfig i = (DatabaseInfoConfig) info;
			List<IEntity> list = map.get(i.gid);
			if (list == null) {
				list = new LinkedList<IEntity>();
				map.put(i.gid, list);
			}
			list.add(i);
		}
		return map;
	}

	public void createService(int gid, DatabaseServiceConfig serviceConfig, List<? extends IEntity> infos, IGloabalContext gloabalContext)
			throws Exception {
		DBService service = new DBService(serviceConfig.key);
		service.setGid(gid);
		service.setServiceConfig(serviceConfig);

		for (IEntity c : infos) {
			DatabaseInfoConfig dbic = (DatabaseInfoConfig) c;
			IDBSession dbSession = createDBSession(serviceConfig, dbic);
			service.insertDBSession(dbSession);
		}
		// 插入服务
		gloabalContext.insertDBService(gid, service);
	}

	public IDBSession createDBSession(DatabaseServiceConfig serviceConfig, DatabaseInfoConfig info) throws Exception {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setJdbcUrl(String.format(UrlFormat, info.ip, info.port, info.dbName));
		dataSource.setUser(info.user);
		dataSource.setPassword(info.password);

		dataSource.setDriverClass(serviceConfig.driverClass);
		dataSource.setMinPoolSize(serviceConfig.minPool);
		dataSource.setInitialPoolSize(serviceConfig.initialPool);
		dataSource.setMaxPoolSize(serviceConfig.maxPool);
		dataSource.setAcquireIncrement(serviceConfig.acquireIncrement);
		dataSource.setMaxStatementsPerConnection(serviceConfig.maxStatementsPerConnection);
		dataSource.setMaxStatements(serviceConfig.maxStatements);

		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);

		ClassPathResource resource = new ClassPathResource(info.mybatis);
		sqlSessionFactoryBean.setConfigLocation(resource);
		SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();

		SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);

		// 创建dbSession
		DBSession dbSession = new DBSession(info.dbid);
		dbSession.setEntityDaoMap(entityDaoMap);
		dbSession.setSqlSession(sqlSessionTemplate);
		dbSession.addRids(info.getRegionMapList());
		dbSession.setDbName(info.dbName);

		return dbSession;
	}

	// --------------------------------
	public void setEntityDaoMap(Map<String, IEntityDao> entityDaoMap) {
		this.entityDaoMap = entityDaoMap;
	}
}
