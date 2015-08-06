package com.cwa.data.config.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

import baseice.data.config.FunctionServiceGeneralConfig;

import com.cwa.component.data.ISpreadEntity;
import com.cwa.util.prototype.JsonUtil;

public class FunctionServiceConfig extends FunctionServiceGeneralConfig implements ISpreadEntity {
	private static final long serialVersionUID = 1L;
	
	private List<Integer> groupIdsList = new ArrayList<Integer>();
	private List<Integer> groupFiltersList = new ArrayList<Integer>();
	private List<Integer> functionFiltersList = new ArrayList<Integer>();

	@Override
	public void obtainAfter() {
	    groupIdsList = JsonUtil.transferJsonTOJavaBean(this.groupIds, List.class);
	    groupFiltersList = JsonUtil.transferJsonTOJavaBean(this.groupFilters, List.class);
	    functionFiltersList = JsonUtil.transferJsonTOJavaBean(this.functionFilters, List.class);
	}

	@Override
	public void saveBefore() {
		this.groupIds = JsonUtil.gsonObj2String(groupIdsList);
		this.groupFilters = JsonUtil.gsonObj2String(groupFiltersList);
		this.functionFilters = JsonUtil.gsonObj2String(functionFiltersList);
	}

	@Override
	public void saveAfter() {
	}

	public List<Integer> getGroupIdsList(){
		return groupIdsList;
	}
	public List<Integer> getGroupFiltersList(){
		return groupFiltersList;
	}
	public List<Integer> getFunctionFiltersList(){
		return functionFiltersList;
	}

	public FunctionServiceConfig cloneEntity() {
		FunctionServiceConfig entity = new FunctionServiceConfig();
		entity.gid = this.gid;
		entity.ftype = this.ftype;
		entity.key = this.key;
		entity.version = this.version;
		entity.available = this.available;
		entity.initPriority = this.initPriority;
		entity.groupIds = this.groupIds;
		entity.groupFilters = this.groupFilters;
		entity.functionFilters = this.functionFilters;
		return entity;
	}
}
