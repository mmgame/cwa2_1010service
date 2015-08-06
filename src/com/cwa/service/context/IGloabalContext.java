package com.cwa.service.context;

import java.util.List;

import com.cwa.component.data.IDBService;
import com.cwa.component.functionmanage.IFunctionService;
import com.cwa.component.membermanager.IMemberService;
import com.cwa.service.IService;
import com.cwa.service.init.IIceServer;

/**
 * 全局上下文
 * 
 * @author mausmars
 *
 */
public interface IGloabalContext {
	/**
	 * 获取外网Ip
	 * 
	 * @return
	 */
	String getOutsideNetIp();

	/**
	 * 当前服务组id
	 * 
	 * @return
	 */
	int getGid();

	/**
	 * 当前服务功能类型
	 * 
	 * @return
	 */
	int getFunctionType();

	/**
	 * 服务器启动时间
	 * 
	 * @return
	 */
	long getStartTime();

	long getConfigVersion();

	void setConfigVersion(long version);

	String getMatchingNetwork();

	// -------------------------------------------
	/**
	 * 获取功能管理
	 * 
	 * @param gid
	 * @return
	 */
	IFunctionService getFunctionService(int gid);

	List<IFunctionService> getAllFunctionService();

	IFunctionService getCurrentFunctionService();

	/**
	 * 插入功能管理
	 * 
	 * @param gid
	 * @return
	 */
	void insertFunctionService(int gid, IFunctionService service);

	/**
	 * 是否存在该组的功能管理
	 * 
	 * @param gid
	 * @return
	 */
	boolean isContainFunctionService(int gid);

	// --------------------------
	IMemberService getMemberService(int gid);

	List<IMemberService> getAllMemberService();

	IMemberService getCurrentMemberService();

	void insertMemberService(int gid, IMemberService service);

	/**
	 * 是否存在该组的功能管理
	 * 
	 * @param gid
	 * @return
	 */
	boolean isContainMemberService(int gid);

	// --------------------------
	IDBService getDBService(int gid);

	List<IDBService> getAllDBService();

	IDBService getCurrentDBService();

	void insertDBService(int gid, IDBService service);

	/**
	 * 是否存在该组的功能管理
	 * 
	 * @param gid
	 * @return
	 */
	boolean isContainDBService(int gid);

	// --------------------------
	/**
	 * 插入功能管理
	 * 
	 * @param gid
	 * @return
	 */
	void insertIService(int gid, IService service);

	IService getCurrentService(String key);

	IService getService(int gid, String key);

	List<IService> getCurrentServices(int serviceType);

	List<IService> getServices(int gid, int serviceType);

	// -------------------------------------------
	/**
	 * 获取ice服务
	 * 
	 * @return
	 */
	IIceServer getIceServer();

	void setIceServer(IIceServer iceServer);
}
