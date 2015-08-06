package com.cwa.test.data;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import baseice.basedao.IEntity;

import com.cwa.entity.ITestUserEntityDao;
import com.cwa.entity.domain.TestUserEntity;

public class TestStart {
	private static Random r = new Random();
	private static Map<Long, TestUserEntity> entityMap = new HashMap<Long, TestUserEntity>();

	public static void main(String[] args) {
		DOMConfigurator.configureAndWatch(args[0]);
		// PropertyConfigurator.configure(args[0]);
		Resource[] props = new FileSystemResource[args.length - 1];
		for (int i = 1; i < args.length; i++) {
			props[i - 1] = new FileSystemResource(args[i]);
		}
		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
		ppc.setFileEncoding("UTF-8");
		ppc.setLocations(props);

		GenericApplicationContext ctx = new GenericApplicationContext();
		XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
		xmlReader.loadBeanDefinitions(new FileSystemResource("config/root.xml"));
		ppc.postProcessBeanFactory((ConfigurableListableBeanFactory) ctx.getDefaultListableBeanFactory());
		ctx.refresh();

		try {
			testDao(ctx);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void testDao(GenericApplicationContext ctx) throws UnsupportedEncodingException {
		ITestUserEntityDao testUserEntityDao = (ITestUserEntityDao) ctx.getBean("testUserEntityDao");
		SqlSession sqlSession = (SqlSession) ctx.getBean("sqlSession");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sql_session", sqlSession);
		params.put("table_number", 1);
		//TODO 自己改库名
		params.put("db_number", "fight1_new1");
		try {
			testUserEntityDao.createTable(params);
		} catch (Exception e) {
			// e.printStackTrace();
		}

		List<? extends IEntity> entiys = testUserEntityDao.selectEntityByUserId(59251, params);
		for (IEntity entiy : entiys) {
			TestUserEntity e = (TestUserEntity) entiy;
			for (byte b : e.name.getBytes()) {
				System.out.println(b);
			}
		}

//		insertTestUser(testUserEntityDao, params);
		TestUserEntity w=selectTestUser(testUserEntityDao, 73964, params);
		System.out.println(w.userId);
		// batchInsertTestUser(testUserEntityDao, params);

		// updateTestUser(testUserEntityDao, params);
		// batchUpdateTestUser(testUserEntityDao, params);

		// removeTestUser(testUserEntityDao, params);
		// batchRemoveTestUser(testUserEntityDao, params);
		// updateTestUser(testUserEntityDao, params);
	}

	private static void insertTestUser(ITestUserEntityDao testUserEntityDao, Map<String, Object> params) throws UnsupportedEncodingException {
		System.out.println("---insert start---");
		TestUserEntity e = new TestUserEntity();
		Random r = new Random();
		e.userId = r.nextInt(100000);
		e.level = 1;

		byte[] bytes = new byte[] { -16, -97, -112, -76 };
		e.name = new String(bytes, "utf-8");
		e.exp = 0;
		testUserEntityDao.insertEntity(e, params);
		entityMap.put(e.userId, e);
		printEntity();
		System.out.println("insert over");
	}

	
	private static TestUserEntity selectTestUser(ITestUserEntityDao testUserEntityDao,long userId, Map<String, Object> params) throws UnsupportedEncodingException {
		System.out.println("---select  start---");
		TestUserEntity e=(TestUserEntity) testUserEntityDao.selectEntityByUserId(userId, params).get(0);
		System.out.println(" select over");
		return e;
	}
	private static void batchInsertTestUser(ITestUserEntityDao testUserEntityDao, Map<String, Object> params) {
		System.out.println("---batchInsert start---");
		TestUserEntity e1 = new TestUserEntity();
		e1.userId = r.nextInt(1000000);
		e1.level = 1;
		e1.name = "游客a_" + e1.userId;
		e1.exp = 0;
		TestUserEntity e2 = new TestUserEntity();
		e2.userId = r.nextInt(1000000);
		e2.level = 1;
		e2.name = "游客a_" + e2.userId;
		e2.exp = 0;
		TestUserEntity e3 = new TestUserEntity();
		e3.userId = r.nextInt(1000000);
		e3.level = 1;
		e3.name = "游客a_" + e3.userId;
		e3.exp = 0;

		List<TestUserEntity> entitys = new ArrayList<TestUserEntity>();
		entitys.add(e1);
		entitys.add(e2);
		entitys.add(e3);
		testUserEntityDao.batchInsertEntity(entitys, params);
		for (IEntity entity : entitys) {
			TestUserEntity tue = (TestUserEntity) entity;
			entityMap.put(tue.userId, tue);
		}
		printEntity();
		System.out.println("batchInsert over");
	}

	private static void removeTestUser(ITestUserEntityDao testUserEntityDao, Map<String, Object> params) {
		System.out.println("---remove start---");
		TestUserEntity tue = null;
		for (IEntity e : entityMap.values()) {
			tue = (TestUserEntity) e;
			break;
		}
		testUserEntityDao.removeEntity(tue, params);
		entityMap.remove(tue.userId);
		printEntity();
		System.out.println("remove over");
	}

	private static void batchRemoveTestUser(ITestUserEntityDao testUserEntityDao, Map<String, Object> params) {
		System.out.println("---batchRemove start---");
		List<TestUserEntity> entitys = new ArrayList<TestUserEntity>();
		for (TestUserEntity entity : entityMap.values()) {
			entitys.add(entity);
		}
		testUserEntityDao.batchRemoveEntity(entitys, params);
		entityMap.clear();
		printEntity();
		System.out.println("batchRemove over");
	}

	private static void updateTestUser(ITestUserEntityDao testUserEntityDao, Map<String, Object> params) {
		System.out.println("---update start---");
		TestUserEntity tue = null;
		for (TestUserEntity e : entityMap.values()) {
			e.level = 99;
			e.exp = 99;
			break;
		}
		testUserEntityDao.updateEntity(tue, params);
		printEntity();
		System.out.println("update over");
	}

	private static void batchUpdateTestUser(ITestUserEntityDao testUserEntityDao, Map<String, Object> params) {
		System.out.println("---batchUpdate start---");
		List<TestUserEntity> entitys = new ArrayList<TestUserEntity>();
		for (TestUserEntity entity : entityMap.values()) {
			entity.level = 88;
			entity.exp = 88;
			entitys.add(entity);
		}
		testUserEntityDao.batchUpdateEntity(entitys, params);
		printEntity();
		System.out.println("batchUpdate over");
	}

	private static void printEntity() {
		for (IEntity e : entityMap.values()) {
			TestUserEntity tue = (TestUserEntity) e;
			System.out.println("TestUserEntity uid=" + tue.userId + " level=" + tue.level);
		}
	}
}
