package com.cwa.data.config.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

import baseice.data.config.IecServiceGeneralConfig;

import com.cwa.component.data.ISpreadEntity;
import com.cwa.util.prototype.JsonUtil;

public class IecServiceConfig extends IecServiceGeneralConfig implements ISpreadEntity {
	private static final long serialVersionUID = 1L;
	
	private List<Integer> groupIdsList = new ArrayList<Integer>();
	private List<String> packageNamesList = new ArrayList<String>();
	private List<String> interfcNamesList = new ArrayList<String>();

	@Override
	public void obtainAfter() {
	    groupIdsList = JsonUtil.transferJsonTOJavaBean(this.groupIds, List.class);
	    packageNamesList = JsonUtil.transferJsonTOJavaBean(this.packageNames, List.class);
	    interfcNamesList = JsonUtil.transferJsonTOJavaBean(this.interfcNames, List.class);
	}

	@Override
	public void saveBefore() {
		this.groupIds = JsonUtil.gsonObj2String(groupIdsList);
		this.packageNames = JsonUtil.gsonObj2String(packageNamesList);
		this.interfcNames = JsonUtil.gsonObj2String(interfcNamesList);
	}

	@Override
	public void saveAfter() {
	}

	public List<Integer> getGroupIdsList(){
		return groupIdsList;
	}
	public List<String> getPackageNamesList(){
		return packageNamesList;
	}
	public List<String> getInterfcNamesList(){
		return interfcNamesList;
	}

	public IecServiceConfig cloneEntity() {
		IecServiceConfig entity = new IecServiceConfig();
		entity.gid = this.gid;
		entity.ftype = this.ftype;
		entity.key = this.key;
		entity.version = this.version;
		entity.available = this.available;
		entity.initPriority = this.initPriority;
		entity.groupIds = this.groupIds;
		entity.port = this.port;
		entity.protocol = this.protocol;
		entity.adapterName = this.adapterName;
		entity.packageNames = this.packageNames;
		entity.interfcNames = this.interfcNames;
		return entity;
	}
}
