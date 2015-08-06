package com.cwa.data.config.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

import baseice.data.config.DatabaseServiceGeneralConfig;

import com.cwa.component.data.ISpreadEntity;
import com.cwa.util.prototype.JsonUtil;

public class DatabaseServiceConfig extends DatabaseServiceGeneralConfig implements ISpreadEntity {
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

	public DatabaseServiceConfig cloneEntity() {
		DatabaseServiceConfig entity = new DatabaseServiceConfig();
		entity.gid = this.gid;
		entity.ftype = this.ftype;
		entity.key = this.key;
		entity.version = this.version;
		entity.available = this.available;
		entity.initPriority = this.initPriority;
		entity.groupIds = this.groupIds;
		entity.driverClass = this.driverClass;
		entity.minPool = this.minPool;
		entity.initialPool = this.initialPool;
		entity.maxPool = this.maxPool;
		entity.acquireIncrement = this.acquireIncrement;
		entity.maxStatements = this.maxStatements;
		entity.maxStatementsPerConnection = this.maxStatementsPerConnection;
		entity.maxIdleTime = this.maxIdleTime;
		entity.checkoutTimeout = this.checkoutTimeout;
		return entity;
	}
}
