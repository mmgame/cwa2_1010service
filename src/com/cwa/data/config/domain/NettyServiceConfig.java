package com.cwa.data.config.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

import baseice.data.config.NettyServiceGeneralConfig;

import com.cwa.component.data.ISpreadEntity;
import com.cwa.util.prototype.JsonUtil;

public class NettyServiceConfig extends NettyServiceGeneralConfig implements ISpreadEntity {
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


	public NettyServiceConfig cloneEntity() {
		NettyServiceConfig entity = new NettyServiceConfig();
		entity.gid = this.gid;
		entity.ftype = this.ftype;
		entity.key = this.key;
		entity.version = this.version;
		entity.available = this.available;
		entity.initPriority = this.initPriority;
		entity.listening = this.listening;
		entity.readerIdleTime = this.readerIdleTime;
		entity.writerIdleTime = this.writerIdleTime;
		entity.allIdleTime = this.allIdleTime;
		entity.subReactorThreadNum = this.subReactorThreadNum;
		entity.handlerThreadNum = this.handlerThreadNum;
		return entity;
	}
}
