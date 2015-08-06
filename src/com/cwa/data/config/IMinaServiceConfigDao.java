package com.cwa.data.config;

import java.util.List;

import com.cwa.component.data.IEntityDao;
import com.cwa.data.config.domain.MinaServiceConfig;

public interface IMinaServiceConfigDao extends IEntityDao<MinaServiceConfig> {
	
	public MinaServiceConfig selectEntityByGidAndFtypeAndKey(int gid,int ftype,String key,Object attach);
	public List<MinaServiceConfig> selectEntityByGidAndFtype(int gid,int ftype,Object attach) ;
	public List<MinaServiceConfig> selectEntityByGid(int gid,Object attach) ;
}
