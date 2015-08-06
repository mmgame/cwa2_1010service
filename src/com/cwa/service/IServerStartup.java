package com.cwa.service;

import com.cwa.service.context.IGloabalContext;

/**
 * 服务启动口
 * 
 * @author mausmars
 *
 */
public interface IServerStartup {
	void execute(IGloabalContext gloabalContext);
}
