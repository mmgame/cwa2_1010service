package com.cwa.data.config.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

import baseice.data.config.HttpServiceGeneralConfig;

import com.cwa.component.data.ISpreadEntity;
import com.cwa.util.prototype.JsonUtil;

public class HttpServiceConfig extends HttpServiceGeneralConfig implements ISpreadEntity {
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


	public HttpServiceConfig cloneEntity() {
		HttpServiceConfig entity = new HttpServiceConfig();
		entity.gid = this.gid;
		entity.ftype = this.ftype;
		entity.key = this.key;
		entity.version = this.version;
		entity.available = this.available;
		entity.initPriority = this.initPriority;
		entity.listening = this.listening;
		entity.minpool = this.minpool;
		entity.maxpool = this.maxpool;
		return entity;
	}
}
