package com.cwa.service.config;

import baseice.service.ServiceInfo;

/**
 * 功能服务配置
 * 
 * @author mausmars
 * 
 */
public class CServiceInfo implements IConfigChanger<ServiceInfo> {
	private String packageName; // 包名
	private String interfcName; // 接口类名

	public ServiceInfo change() {
		ServiceInfo iceObj = new ServiceInfo();
		iceObj.packageName = packageName;
		iceObj.interfcName = interfcName;
		iceObj.server = null;
		return iceObj;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getInterfcName() {
		return interfcName;
	}

	public void setInterfcName(String interfcName) {
		this.interfcName = interfcName;
	}
}
