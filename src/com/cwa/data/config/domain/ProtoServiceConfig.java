package com.cwa.data.config.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

import baseice.data.config.ProtoServiceGeneralConfig;

import com.cwa.component.data.ISpreadEntity;
import com.cwa.util.prototype.JsonUtil;

public class ProtoServiceConfig extends ProtoServiceGeneralConfig implements ISpreadEntity {
	private static final long serialVersionUID = 1L;
	
	private List<Integer> groupIdsList = new ArrayList<Integer>();
	private List<String> protoNamesList = new ArrayList<String>();
	private List<Integer> ridsList = new ArrayList<Integer>();

	@Override
	public void obtainAfter() {
	    groupIdsList = JsonUtil.transferJsonTOJavaBean(this.groupIds, List.class);
	    protoNamesList = JsonUtil.transferJsonTOJavaBean(this.protoNames, List.class);
	    ridsList = JsonUtil.transferJsonTOJavaBean(this.rids, List.class);
	}

	@Override
	public void saveBefore() {
		this.groupIds = JsonUtil.gsonObj2String(groupIdsList);
		this.protoNames = JsonUtil.gsonObj2String(protoNamesList);
		this.rids = JsonUtil.gsonObj2String(ridsList);
	}

	@Override
	public void saveAfter() {
	}

	public List<Integer> getGroupIdsList(){
		return groupIdsList;
	}
	public List<String> getProtoNamesList(){
		return protoNamesList;
	}
	public List<Integer> getRidsList(){
		return ridsList;
	}

	public ProtoServiceConfig cloneEntity() {
		ProtoServiceConfig entity = new ProtoServiceConfig();
		entity.gid = this.gid;
		entity.ftype = this.ftype;
		entity.key = this.key;
		entity.version = this.version;
		entity.available = this.available;
		entity.initPriority = this.initPriority;
		entity.groupIds = this.groupIds;
		entity.protoNames = this.protoNames;
		entity.rids = this.rids;
		return entity;
	}
}
