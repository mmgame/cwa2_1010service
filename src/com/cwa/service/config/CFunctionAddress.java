package com.cwa.service.config;

import baseice.service.FunctionAddress;
import baseice.service.NetProtocolEnum;

/**
 * 功能的网络配置
 * 
 * @author mausmars
 * 
 */
public class CFunctionAddress implements IConfigChanger<FunctionAddress> {
	private String ip; // ip地址
	private int port; // 端口
	private String protocol; // 网络协议
	private String adapterName;// 适配器名

	@Override
	public FunctionAddress change() {
		FunctionAddress iceObj = new FunctionAddress();
		iceObj.ip = ip;
		iceObj.port = port;
		iceObj.protocol = NetProtocolEnum.valueOf(protocol);
		iceObj.adapterName = adapterName;
		return iceObj;
	}

	public String getAdapterName() {
		return adapterName;
	}

	public void setAdapterName(String adapterName) {
		this.adapterName = adapterName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
}
