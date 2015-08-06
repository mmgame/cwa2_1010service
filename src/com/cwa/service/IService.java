package com.cwa.service;

/**
 * 服务接口
 * 
 * @author mausmars
 *
 */
public interface IService {
	/**
	 * 服务类型
	 * 
	 * @return
	 */
	String getKey();

	/**
	 * 服务版本
	 * 
	 * @return
	 */
	int getVersion();

	/**
	 * 服务类型
	 * 
	 * @return
	 */
	int getServiceType();

	/**
	 * 开始服务
	 */
	void startup() throws Exception;

	/**
	 * 停止服务
	 */
	void shutdown() throws Exception;
}
