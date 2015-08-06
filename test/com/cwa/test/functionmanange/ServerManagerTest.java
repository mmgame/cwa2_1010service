package com.cwa.test.functionmanange;

import java.util.HashMap;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cwa.component.functionmanage.FunctionService;
import com.cwa.component.zkservice.ZKService;

import baseice.service.FunctionAddress;
import baseice.service.FunctionId;
import baseice.service.FunctionMenu;
import baseice.service.NetProtocolEnum;
import baseice.service.ServiceInfo;

public class ServerManagerTest {
	protected static final Logger logger = LoggerFactory.getLogger(ServerManagerTest.class);
	private String hosts = "172.16.0.145:2181,172.16.0.141:2182";

	public void test() throws Exception {
		int size = 3;
		CountDownLatch latch = new CountDownLatch(size);

		int rid = 1;
		int serverType = 1;

		ZKService zk = new ZKService(hosts);
		List<Thread> tasks = new LinkedList<Thread>();
		for (int i = 1; i <= size; i++) {
			FunctionService sm = new FunctionService(i + "");
			sm.setModuleName("test_" + i);
			sm.setZkService(zk);

			sm.startup();

			FunctionMenu functionMenu = new FunctionMenu();
			functionMenu.fid = new FunctionId(rid, serverType, i);
			functionMenu.fa = new FunctionAddress("111.1111.1111.1111", 1, NetProtocolEnum.tcp, "");
			functionMenu.version = 1;
			functionMenu.serviceInfos = new HashMap<java.lang.String, ServiceInfo>();

			sm.register(functionMenu);

			TestTask task = new TestTask(functionMenu, sm, latch);
			Thread thread = new Thread(task);
			thread.setName("test_" + i);
			tasks.add(thread);
		}
		if (logger.isInfoEnabled()) {
			logger.info("#### start thread! ####");
		}
		for (Thread task : tasks) {
			task.run();
		}
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		DOMConfigurator.configureAndWatch("propertiesconfig/log4j.xml");

		ServerManagerTest test = new ServerManagerTest();
		test.test();
	}
}
