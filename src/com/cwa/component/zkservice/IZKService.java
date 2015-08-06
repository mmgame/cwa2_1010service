package com.cwa.component.zkservice;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import com.cwa.component.event.ILocalEventListener;

public interface IZKService {
	// ZooKeeper getZk();

	/**
	 * 开始服务
	 */
	void startup() throws Exception;

	/**
	 * 停止服务
	 */
	void shutdown() throws Exception;

	/**
	 * 注册监听
	 * 
	 * @param eventListener
	 */
	void registerListener(ILocalEventListener eventListener);

	/**
	 * 注销监听
	 * 
	 * @param eventListener
	 */
	void unregisterListener(Object key);

	// ------------------------------------------
	Stat exists(String path, boolean watch) throws KeeperException, InterruptedException;

	String create(String path, byte data[], List<ACL> acl, CreateMode createMode) throws KeeperException, InterruptedException;

	List<String> getChildren(String path, boolean watch) throws KeeperException, InterruptedException;

	byte[] getData(String path, boolean watch, Stat stat) throws KeeperException, InterruptedException;

	void delete(final String path, int version) throws InterruptedException, KeeperException;
	
	Stat setData(String path, byte data[], int version) throws KeeperException, InterruptedException;
}
