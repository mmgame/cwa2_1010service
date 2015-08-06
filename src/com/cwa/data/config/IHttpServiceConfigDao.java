package com.cwa.data.config;

import java.util.List;

import com.cwa.component.data.IEntityDao;
import com.cwa.data.config.domain.HttpServiceConfig;

public interface IHttpServiceConfigDao extends IEntityDao<HttpServiceConfig> {
	
	public HttpServiceConfig selectEntityByGidAndFtypeAndKey(int gid,int ftype,String key,Object attach);
	public List<HttpServiceConfig> selectEntityByGidAndFtype(int gid,int ftype,Object attach) ;
	public List<HttpServiceConfig> selectEntityByGid(int gid,Object attach) ;
}
