package com.cwa.data.config.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

import baseice.data.config.DatabaseInfoGeneralConfig;

import com.cwa.component.data.ISpreadEntity;
import com.cwa.util.prototype.JsonUtil;

public class DatabaseInfoConfig extends DatabaseInfoGeneralConfig implements ISpreadEntity {
	private static final long serialVersionUID = 1L;
	
	private List<Integer> regionMapList = new ArrayList<Integer>();

	@Override
	public void obtainAfter() {
	    regionMapList = JsonUtil.transferJsonTOJavaBean(this.regionMap, List.class);
	}

	@Override
	public void saveBefore() {
		this.regionMap = JsonUtil.gsonObj2String(regionMapList);
	}

	@Override
	public void saveAfter() {
	}

	public List<Integer> getRegionMapList(){
		return regionMapList;
	}

	public DatabaseInfoConfig cloneEntity() {
		DatabaseInfoConfig entity = new DatabaseInfoConfig();
		entity.gid = this.gid;
		entity.dbid = this.dbid;
		entity.ip = this.ip;
		entity.port = this.port;
		entity.dbName = this.dbName;
		entity.user = this.user;
		entity.password = this.password;
		entity.regionMap = this.regionMap;
		entity.mybatis = this.mybatis;
		return entity;
	}
}
