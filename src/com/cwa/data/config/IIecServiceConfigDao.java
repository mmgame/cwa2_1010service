package com.cwa.data.config;

import java.util.List;

import com.cwa.component.data.IEntityDao;
import com.cwa.data.config.domain.IecServiceConfig;

public interface IIecServiceConfigDao extends IEntityDao<IecServiceConfig> {
	
	public IecServiceConfig selectEntityByGidAndFtypeAndKey(int gid,int ftype,String key,Object attach);
	public List<IecServiceConfig> selectEntityByGidAndFtype(int gid,int ftype,Object attach) ;
	public List<IecServiceConfig> selectEntityByGid(int gid,Object attach) ;
}
