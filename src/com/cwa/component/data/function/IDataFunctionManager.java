package com.cwa.component.data.function;

import baseice.basedao.IEntity;

import com.cwa.component.data.IDBSession;

/**
 * 数据管理
 * 
 * @author mausmars
 *
 */
public interface IDataFunctionManager {
	/**
	 * 数据session
	 * 
	 * @return
	 */
	IDBSession getDbSession();

	/**
	 * 获取数据功能
	 * 
	 * @param cla
	 * @return
	 */
	IDataFunction getDataFunction(Class<? extends IEntity> cla);

	/**
	 * 登陆初始化数据
	 */
	void initData();

	/**
	 * 初始化指定数据
	 */
	void initData(Class<? extends IEntity> cla);

	/**
	 * 设置数据超时
	 */
	void insertDataTimeout();

	/**
	 * 移除数据超时
	 */
	void removeDataTimeout();

	/**
	 * 重置数据超时
	 */
	void resetDataTimeout();

}
