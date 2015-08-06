package com.cwa.service;

import com.cwa.service.context.IGloabalContext;

/**
 * 模块服务启动口
 * 
 * @author mausmars
 *
 */
public interface IModuleServer {
	/**
	 * 开始服务
	 */
	void startup(IGloabalContext gloabalContext) throws Exception;

	/**
	 * 停止服务
	 */
	void shutdown() throws Exception;

}
