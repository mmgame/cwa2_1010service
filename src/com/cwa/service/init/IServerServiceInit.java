package com.cwa.service.init;

/**
 * 服务器启动后，注册到注册服务器前初始化接口
 * 
 * @author Administrator
 * 
 */
public interface IServerServiceInit {
	/**
	 * 初始化方法
	 * 
	 * @param iceService
	 */
	void init(IIceServer iceService);
}
