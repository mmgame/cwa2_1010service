package com.cwa.data.config;

import java.util.List;

import com.cwa.component.data.IEntityDao;
import com.cwa.data.config.domain.DataTimeoutServiceConfig;

public interface IDataTimeoutServiceConfigDao extends IEntityDao<DataTimeoutServiceConfig> {
	
	public DataTimeoutServiceConfig selectEntityByGidAndFtypeAndKey(int gid,int ftype,String key,Object attach);
	public List<DataTimeoutServiceConfig> selectEntityByGidAndFtype(int gid,int ftype,Object attach) ;
	public List<DataTimeoutServiceConfig> selectEntityByGid(int gid,Object attach) ;
}
