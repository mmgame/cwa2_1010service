package com.cwa.data.config;

import java.util.List;

import com.cwa.component.data.IEntityDao;
import com.cwa.data.config.domain.FunctionServiceConfig;

public interface IFunctionServiceConfigDao extends IEntityDao<FunctionServiceConfig> {
	
	public FunctionServiceConfig selectEntityByGidAndFtypeAndKey(int gid,int ftype,String key,Object attach);
	public List<FunctionServiceConfig> selectEntityByGidAndFtype(int gid,int ftype,Object attach) ;
	public List<FunctionServiceConfig> selectEntityByGid(int gid,Object attach) ;
}
