package com.cwa.service.init;

import Ice.Communicator;
import Ice.ObjectAdapter;
import baseice.service.FunctionAddress;
import baseice.service.IMasterServicePrx;

/**
 * ice服务接口
 * 
 * @author mausmars
 * 
 */
public interface IIceServer {
	void shutdown() throws Exception;

	/**
	 * 获取主服务代理类
	 * 
	 * @param fa
	 * @return
	 */
	IMasterServicePrx getMasterServicePrx(FunctionAddress fa);

	Communicator getCommunicator();

	String getUrl(FunctionAddress fa);

	String getUrl(String protocol, String ip, int port);

	ObjectAdapter getObjectAdapterOrCreate(String adapterName, String url);

	ObjectAdapter getObjectAdapter(String adapterName);

	String getMasterInterfaceName();
}
