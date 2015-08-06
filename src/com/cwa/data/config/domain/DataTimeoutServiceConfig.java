package com.cwa.data.config.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

import baseice.data.config.DataTimeoutServiceGeneralConfig;

import com.cwa.component.data.ISpreadEntity;
import com.cwa.util.prototype.JsonUtil;

public class DataTimeoutServiceConfig extends DataTimeoutServiceGeneralConfig implements ISpreadEntity {
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


	public DataTimeoutServiceConfig cloneEntity() {
		DataTimeoutServiceConfig entity = new DataTimeoutServiceConfig();
		entity.gid = this.gid;
		entity.ftype = this.ftype;
		entity.key = this.key;
		entity.version = this.version;
		entity.available = this.available;
		entity.initPriority = this.initPriority;
		entity.timeout = this.timeout;
		entity.expression = this.expression;
		return entity;
	}
}
