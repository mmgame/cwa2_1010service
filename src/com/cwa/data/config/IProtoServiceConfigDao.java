package com.cwa.data.config;

import java.util.List;

import com.cwa.component.data.IEntityDao;
import com.cwa.data.config.domain.ProtoServiceConfig;

public interface IProtoServiceConfigDao extends IEntityDao<ProtoServiceConfig> {
	
	public ProtoServiceConfig selectEntityByGidAndFtypeAndKey(int gid,int ftype,String key,Object attach);
	public List<ProtoServiceConfig> selectEntityByGidAndFtype(int gid,int ftype,Object attach) ;
	public List<ProtoServiceConfig> selectEntityByGid(int gid,Object attach) ;
}
