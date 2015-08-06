package com.cwa.component.netlock;

import java.util.Collections;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * zookeeper实现网络锁
 * 
 * @author Administrator
 * 
 */
public class NetLockZK implements INetLock, Watcher {
	protected static final Logger logger = LoggerFactory.getLogger(NetLockZK.class);

	private String rootPath;
	private final String root = "/locks";
	private ThreadLocal<String> store = new ThreadLocal<String>();
	// ----------------
	private ZooKeeper zk;

	public NetLockZK(String moduleName) {
		this.rootPath = root + "/" + moduleName;
	}

	public void init() {
		try {
			if (zk.exists(root, false) == null) {
				zk.create(root, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
			if (zk.exists(rootPath, false) == null) {
				zk.create(rootPath, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public boolean tryLock() {
		try {
			// 创建一个有序的临时节点
			String node = zk.create(rootPath + "/", null, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			store.set(node);
			for (;;) {
				List<String> nodes = zk.getChildren(rootPath, false);
				Collections.sort(nodes);
				printNode(node, nodes);
				if (node.equals(rootPath + "/" + nodes.get(0))) {
					// 成功获得锁
					return true;
				} else {
					Stat stat = zk.exists(rootPath + "/" + nodes.get(0), true);
					if (stat != null) {
						if (logger.isInfoEnabled()) {
							logger.info("wait! threadName=" + Thread.currentThread().getName());
						}
						// 获取锁失败
						return false;
					} else {
						continue;
					}
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return false;
	}

	@Override
	public void lock() {
		try {
			// 创建一个有序的临时节点
			String node = zk.create(rootPath + "/", null, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			store.set(node);

			for (;;) {
				List<String> nodes = zk.getChildren(rootPath, false);
				Collections.sort(nodes);
				printNode(node, nodes);
				if (node.equals(rootPath + "/" + nodes.get(0))) {
					break;
				} else {
					Stat stat = zk.exists(rootPath + "/" + nodes.get(0), true);
					if (stat != null) {
						if (logger.isInfoEnabled()) {
							logger.info("wait! threadName=" + Thread.currentThread().getName());
						}
						synchronized (this) {
							this.wait();
						}
						continue;
					} else {
						continue;
					}
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void unlock() {
		try {
			String node = store.get();
			if (logger.isInfoEnabled()) {
				logger.info("del node=" + node);
			}
			zk.delete(node, -1);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void process(WatchedEvent event) {
		if (logger.isInfoEnabled()) {
			logger.info("Watch " + event);
		}
		if (event.getType() == Watcher.Event.EventType.NodeDeleted) {
			if (logger.isInfoEnabled()) {
				logger.info("NodeDeleted");
			}
			synchronized (this) {
				this.notifyAll();
			}
		} else if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
			if (logger.isInfoEnabled()) {
				logger.info("NodeChildrenChanged");
			}
		} else if (event.getType() == Watcher.Event.EventType.NodeDataChanged) {
			if (logger.isInfoEnabled()) {
				logger.info("NodeDataChanged");
			}
		}
	}

	private void printNode(String n, List<String> nodes) {
		StringBuilder sb = new StringBuilder();
		for (String node : nodes) {
			sb.append(node);
			sb.append(" ");
		}
		if (logger.isInfoEnabled()) {
			logger.info("node=" + n + " nodes=" + sb.toString());
		}
	}

	// ------------------------------
	public void setZk(ZooKeeper zk) {
		this.zk = zk;
	}
}
