package com.cwa.data.config;

import java.util.List;

import com.cwa.component.data.IEntityDao;
import com.cwa.data.config.domain.MemberServiceConfig;

public interface IMemberServiceConfigDao extends IEntityDao<MemberServiceConfig> {
	
	public MemberServiceConfig selectEntityByGidAndFtypeAndKey(int gid,int ftype,String key,Object attach);
	public List<MemberServiceConfig> selectEntityByGidAndFtype(int gid,int ftype,Object attach) ;
	public List<MemberServiceConfig> selectEntityByGid(int gid,Object attach) ;
}
