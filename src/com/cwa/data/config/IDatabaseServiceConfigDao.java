package com.cwa.data.config;

import java.util.List;

import com.cwa.component.data.IEntityDao;
import com.cwa.data.config.domain.DatabaseServiceConfig;

public interface IDatabaseServiceConfigDao extends IEntityDao<DatabaseServiceConfig> {
	
	public DatabaseServiceConfig selectEntityByGidAndFtypeAndKey(int gid,int ftype,String key,Object attach);
	public List<DatabaseServiceConfig> selectEntityByGidAndFtype(int gid,int ftype,Object attach) ;
	public List<DatabaseServiceConfig> selectEntityByGid(int gid,Object attach) ;
}
