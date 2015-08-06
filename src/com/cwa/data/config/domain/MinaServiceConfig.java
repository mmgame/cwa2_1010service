package com.cwa.data.config.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

import baseice.data.config.MinaServiceGeneralConfig;

import com.cwa.component.data.ISpreadEntity;
import com.cwa.util.prototype.JsonUtil;

public class MinaServiceConfig extends MinaServiceGeneralConfig implements ISpreadEntity {
	private static final long serialVersionUID = 1L;
	

	@Override
	public void obtainAfter() {
	}

	@Override
	public void saveBefore() {
	}

	@Override
	public void saveAfter() {
	}


	public MinaServiceConfig cloneEntity() {
		MinaServiceConfig entity = new MinaServiceConfig();
		entity.gid = this.gid;
		entity.ftype = this.ftype;
		entity.key = this.key;
		entity.version = this.version;
		entity.available = this.available;
		entity.initPriority = this.initPriority;
		entity.listening = this.listening;
		entity.heartbeatId = this.heartbeatId;
		entity.forwardEvent = this.forwardEvent;
		entity.ideltimeout = this.ideltimeout;
		entity.heartbeatrate = this.heartbeatrate;
		entity.messagemaxpackage = this.messagemaxpackage;
		entity.connectTimeout = this.connectTimeout;
		entity.minpool = this.minpool;
		entity.maxpool = this.maxpool;
		entity.allowedInterval = this.allowedInterval;
		return entity;
	}
}
