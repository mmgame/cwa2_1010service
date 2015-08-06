package com.cwa.data.config;

import java.util.List;

import com.cwa.component.data.IEntityDao;
import com.cwa.data.config.domain.DatabaseInfoConfig;

public interface IDatabaseInfoConfigDao extends IEntityDao<DatabaseInfoConfig> {
	
	public DatabaseInfoConfig selectEntityByGidAndDbid(int gid,int dbid,Object attach);
	public List<DatabaseInfoConfig> selectEntityByGid(int gid,Object attach) ;
}
