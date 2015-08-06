package com.cwa.data.config;

import java.util.List;

import com.cwa.component.data.IEntityDao;
import com.cwa.data.config.domain.NettyServiceConfig;

public interface INettyServiceConfigDao extends IEntityDao<NettyServiceConfig> {
	
	public NettyServiceConfig selectEntityByGidAndFtypeAndKey(int gid,int ftype,String key,Object attach);
	public List<NettyServiceConfig> selectEntityByGidAndFtype(int gid,int ftype,Object attach) ;
	public List<NettyServiceConfig> selectEntityByGid(int gid,Object attach) ;
}
