package com.cwa.service.config;

import java.util.HashMap;
import java.util.List;

import com.cwa.service.ServiceUtil;

import baseice.service.FunctionAddress;
import baseice.service.FunctionId;
import baseice.service.FunctionMenu;
import baseice.service.ServiceInfo;

/**
 * 功能菜单配置
 * 
 * @author mausmars
 * 
 */
public class CFunctionMenu implements IConfigChanger<FunctionMenu> {
	private int version; // 功能版本
	private IConfigChanger<FunctionId> functionId; // 功能id
	private IConfigChanger<FunctionAddress> functionAddress; // 功能网络配置
	private List<IConfigChanger<ServiceInfo>> serviceInfos; // 从接口列表

	public FunctionMenu change() {
		FunctionMenu iceObj = new FunctionMenu();
		iceObj.version = version;
		iceObj.fid = functionId.change();
		iceObj.fa = functionAddress.change();
		iceObj.serviceInfos = new HashMap<String, ServiceInfo>();
		for (IConfigChanger<ServiceInfo> serviceInfo : serviceInfos) {
			ServiceInfo si = serviceInfo.change();
			iceObj.serviceInfos.put(si.interfcName + ServiceUtil.PrxPostfix, si);
		}
		return iceObj;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public IConfigChanger<FunctionId> getFunctionId() {
		return functionId;
	}

	public void setFunctionId(IConfigChanger<FunctionId> functionId) {
		this.functionId = functionId;
	}

	public IConfigChanger<FunctionAddress> getFunctionAddress() {
		return functionAddress;
	}

	public void setFunctionAddress(IConfigChanger<FunctionAddress> functionAddress) {
		this.functionAddress = functionAddress;
	}

	public List<IConfigChanger<ServiceInfo>> getServiceInfos() {
		return serviceInfos;
	}

	public void setServiceInfos(List<IConfigChanger<ServiceInfo>> serviceInfos) {
		this.serviceInfos = serviceInfos;
	}
}
