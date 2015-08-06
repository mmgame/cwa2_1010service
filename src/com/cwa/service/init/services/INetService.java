package com.cwa.service.init.services;

import com.cwa.service.IService;

/**
 * 网络服务
 * 
 * @author mausmars
 *
 */
public interface INetService extends IService {
	/**
	 * 移除超时检查
	 * 
	 * @param key
	 */
	void removeTimeoutCheck(String key);
}
