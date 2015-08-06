package com.cwa.data.config.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

import baseice.data.config.MemberServiceGeneralConfig;

import com.cwa.component.data.ISpreadEntity;
import com.cwa.util.prototype.JsonUtil;

public class MemberServiceConfig extends MemberServiceGeneralConfig implements ISpreadEntity {
	private static final long serialVersionUID = 1L;
	
	private List<Integer> groupIdsList = new ArrayList<Integer>();

	@Override
	public void obtainAfter() {
	    groupIdsList = JsonUtil.transferJsonTOJavaBean(this.groupIds, List.class);
	}

	@Override
	public void saveBefore() {
		this.groupIds = JsonUtil.gsonObj2String(groupIdsList);
	}

	@Override
	public void saveAfter() {
	}

	public List<Integer> getGroupIdsList(){
		return groupIdsList;
	}

	public MemberServiceConfig cloneEntity() {
		MemberServiceConfig entity = new MemberServiceConfig();
		entity.gid = this.gid;
		entity.ftype = this.ftype;
		entity.key = this.key;
		entity.version = this.version;
		entity.available = this.available;
		entity.initPriority = this.initPriority;
		entity.groupIds = this.groupIds;
		return entity;
	}
}
