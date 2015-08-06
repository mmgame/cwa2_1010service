package com.cwa.component.zkservice;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cwa.component.event.ILocalEventListener;
import com.cwa.component.functionmanage.conostant.FunctionConstant;

/**
 * zk 服务器管理； 路径： /root/区id/功能类型/功能key_zk自动生成的id。 例子： /sms/rid/ftype/fkey_00000xx
 * 
 * @author mausmars
 * 
 */
public class ZKService implements IZKService, Watcher {
	protected static final Logger logger = LoggerFactory.getLogger(IZKService.class);

	private volatile ZooKeeper zk;
	// --------------------------------
	private String hosts;
	private int sessionTimeOut = 1000;

	private List<ILocalEventListener> expiredListeners = new LinkedList<ILocalEventListener>();// session过期监听
	private Map<Object, ILocalEventListener> eventListenerMap = new HashMap<Object, ILocalEventListener>();

	private volatile CountDownLatch connectedLatch;

	private static final int MAX_RETRIES = 3;
	private static final long RETRY_PERIOD_SECONDS = 20;

	private IZKEventFactory eventFactory;

	public ZKService(String hosts) {
		this.hosts = hosts;
	}

	@Override
	public void startup() throws Exception {
		connectedLatch = new CountDownLatch(1);
		zk = new ZooKeeper(hosts, sessionTimeOut, this);
		waitUntilConnected();
	}

	private void waitUntilConnected() {
		if (States.CONNECTING == zk.getState()) {
			try {
				connectedLatch.await();
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
		}
	}

	@Override
	public void shutdown() throws Exception {
		zk.close();
	}

	@Override
	public void registerListener(ILocalEventListener listener) {
		if (!listener.key().equals(FunctionConstant.SessionExpiredListenerKey)) {
			eventListenerMap.put(listener.key(), listener);
		} else {
			expiredListeners.add(listener);
		}
	}

	@Override
	public void unregisterListener(Object key) {
		eventListenerMap.remove(key);
	}

	@Override
	public void process(WatchedEvent event) {
		if (logger.isInfoEnabled()) {
			logger.info("【Watch " + event + "】");
		}
		String path = event.getPath();

		if (path == null) {
			if (event.getState() == KeeperState.SyncConnected) {
				connectedLatch.countDown();
			} else if (event.getState() == KeeperState.Expired) {
				reStartup();
			}
			return;
		}
		IZKEvent e = eventFactory.create(event);
		for (ILocalEventListener listener : eventListenerMap.values()) {
			listener.answer(e);
		}
	}

	private void reStartup() {
		Thread thread = new Thread() {
			public void run() {
				for (;;) {
					try {
						// 这里重新创建连接
						if (logger.isInfoEnabled()) {
							logger.info("Zkservice reStartup!");
						}
						startup();
						break;
					} catch (Exception e1) {
						logger.error("", e1);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					}
				}
				for (ILocalEventListener expiredListener : expiredListeners) {
					expiredListener.answer(null);
				}
			}
		};
		thread.start();
	}

	// -------------------------------------------------------
	@Override
	public Stat exists(String path, boolean watch) throws KeeperException, InterruptedException {
		return zk.exists(path, watch);
	}

	@Override
	public String create(String path, byte data[], List<ACL> acl, CreateMode createMode) throws KeeperException, InterruptedException {
		int retries = 0;
		while (true) {
			try {
				return zk.create(path, data, acl, createMode);
			} catch (KeeperException.SessionExpiredException e) {
				throw e;
			} catch (KeeperException e) {
				if (retries++ == MAX_RETRIES) {
					throw e;
				}
				// sleep then retry
				Thread.sleep(RETRY_PERIOD_SECONDS);
			}
		}
	}

	@Override
	public List<String> getChildren(String path, boolean watch) throws KeeperException, InterruptedException {
		int retries = 0;
		while (true) {
			try {
				return zk.getChildren(path, watch);
			} catch (KeeperException.SessionExpiredException e) {
				throw e;
			} catch (KeeperException e) {
				if (retries++ == MAX_RETRIES) {
					throw e;
				}
				// sleep then retry
				Thread.sleep(RETRY_PERIOD_SECONDS);
			}
		}
	}

	@Override
	public byte[] getData(String path, boolean watch, Stat stat) throws KeeperException, InterruptedException {
		int retries = 0;
		while (true) {
			try {
				return zk.getData(path, watch, stat);
			} catch (KeeperException.SessionExpiredException e) {
				throw e;
			} catch (KeeperException e) {
				if (retries++ == MAX_RETRIES) {
					throw e;
				}
				// sleep then retry
				Thread.sleep(RETRY_PERIOD_SECONDS);
			}
		}
	}

	@Override
	public void delete(final String path, int version) throws InterruptedException, KeeperException {
		int retries = 0;
		while (true) {
			try {
				zk.delete(path, version);
			} catch (KeeperException.SessionExpiredException e) {
				throw e;
			} catch (KeeperException e) {
				if (retries++ == MAX_RETRIES) {
					throw e;
				}
				// sleep then retry
				Thread.sleep(RETRY_PERIOD_SECONDS);
			}
		}
	}

	@Override
	public Stat setData(String path, byte[] data, int version) throws KeeperException, InterruptedException {
		int retries = 0;
		while (true) {
			try {
				return zk.setData(path, data, version);
			} catch (KeeperException.SessionExpiredException e) {
				throw e;
			} catch (KeeperException e) {
				if (retries++ == MAX_RETRIES) {
					throw e;
				}
				// sleep then retry
				Thread.sleep(RETRY_PERIOD_SECONDS);
			}
		}
	}

	// -------------------------------------------------------
	@Override
	public String toString() {
		return "ZKService [hosts=" + hosts + ", sessionTimeOut=" + sessionTimeOut + "]";
	}

	// -----------------------------------
	public void setSessionTimeOut(int sessionTimeOut) {
		this.sessionTimeOut = sessionTimeOut;
	}

	public void setEventFactory(IZKEventFactory eventFactory) {
		this.eventFactory = eventFactory;
	}
}
