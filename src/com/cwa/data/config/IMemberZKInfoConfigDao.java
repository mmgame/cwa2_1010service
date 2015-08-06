package com.cwa.data.config;

import java.util.List;

import com.cwa.component.data.IEntityDao;
import com.cwa.data.config.domain.MemberZKInfoConfig;

public interface IMemberZKInfoConfigDao extends IEntityDao<MemberZKInfoConfig> {
	
	public MemberZKInfoConfig selectEntityByGid(int gid,Object attach);
}
