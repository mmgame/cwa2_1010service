package com.cwa.data.config;

import java.util.List;

import com.cwa.component.data.IEntityDao;
import com.cwa.data.config.domain.FunctionZKInfoConfig;

public interface IFunctionZKInfoConfigDao extends IEntityDao<FunctionZKInfoConfig> {
	
	public FunctionZKInfoConfig selectEntityByGid(int gid,Object attach);
}
