package com.cwa.data.config.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

import baseice.data.config.MemberZKInfoGeneralConfig;

import com.cwa.component.data.ISpreadEntity;
import com.cwa.util.prototype.JsonUtil;

public class MemberZKInfoConfig extends MemberZKInfoGeneralConfig implements ISpreadEntity {
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


	public MemberZKInfoConfig cloneEntity() {
		MemberZKInfoConfig entity = new MemberZKInfoConfig();
		entity.gid = this.gid;
		entity.hosts = this.hosts;
		entity.sessionTimeout = this.sessionTimeout;
		entity.rootPath = this.rootPath;
		return entity;
	}
}
