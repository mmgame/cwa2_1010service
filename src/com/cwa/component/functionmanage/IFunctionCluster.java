package com.cwa.component.functionmanage;

import java.util.List;

import Ice.ObjectPrx;
import baseice.service.FunctionMenu;

/**
 * 功能集群接口（某一类型的功能服务管理）
 * 
 * @author mausmars
 * 
 */
public interface IFunctionCluster {
	/**
	 * 该功能集群是否可用
	 * 
	 * @return
	 */
	boolean isAvailable();

	/**
	 * 获得指定功能服的服务
	 * 
	 * @param ftype
	 * @param cls
	 * @return
	 */
	<T extends ObjectPrx> T getMasterService(Class<T> cls);

	/**
	 * 获得指定功能服的服务
	 * 
	 * @param ftype
	 * @param cls
	 * @return
	 */
	<T extends ObjectPrx> T getSlaveService(int fkey, Class<T> cls);

	<T extends ObjectPrx> List<T> getAllService(Class<T> cls);

	FunctionMenu getRandomFunctionMenu(Object key);

	FunctionMenu getFunctionMenu(int fkey);

	/**
	 * 获取全部功能菜单
	 * 
	 * @return
	 */
	List<FunctionMenu> getAllFunctionMenu();

	FunctionMenu getCurrentFunctionMenu();
}
