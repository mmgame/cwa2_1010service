package com.cwa.component.netlock;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetLockManagerZK implements INetLockManager, Watcher {
	protected static final Logger logger = LoggerFactory.getLogger(NetLockZK.class);

	private Map<String, NetLockZK> lockMap = new HashMap<String, NetLockZK>();
	// ----------------------------
	private String hosts;
	private int sessionTimeOut = 1000;
	private ZooKeeper zk;

	public NetLockManagerZK(String hosts) {
		this.hosts = hosts;
	}

	public void init() {
		try {
			zk = new ZooKeeper(hosts, sessionTimeOut, this);
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	@Override
	public INetLock createLock(String moduleName) {
		if (lockMap.containsKey(moduleName)) {
			return lockMap.get(moduleName);
		} else {
			NetLockZK lock = new NetLockZK(moduleName);
			lock.setZk(zk);
			lock.init();
			lockMap.put(moduleName, lock);
			return lock;
		}
	}

	@Override
	public void process(WatchedEvent event) {
		if (logger.isInfoEnabled()) {
			logger.info("Watch " + event);
		}
		String path = event.getPath();
		if (path == null) {
			return;
		}
		String[] strs = path.split("/");
		Watcher watcher = lockMap.get(strs[2]);
		if (watcher != null) {
			watcher.process(event);
		}
	}

	// ---------------------------------
	public void setSessionTimeOut(int sessionTimeOut) {
		this.sessionTimeOut = sessionTimeOut;
	}
}
