package com.cwa.data;

import Ice.Object;
import Ice.ObjectFactory;

import baseice.data.config.IecServiceGeneralConfig;
import com.cwa.data.config.domain.IecServiceConfig;
import baseice.data.config.FunctionServiceGeneralConfig;
import com.cwa.data.config.domain.FunctionServiceConfig;
import baseice.data.config.MemberServiceGeneralConfig;
import com.cwa.data.config.domain.MemberServiceConfig;
import baseice.data.config.DatabaseServiceGeneralConfig;
import com.cwa.data.config.domain.DatabaseServiceConfig;
import baseice.data.config.NettyServiceGeneralConfig;
import com.cwa.data.config.domain.NettyServiceConfig;
import baseice.data.config.MinaServiceGeneralConfig;
import com.cwa.data.config.domain.MinaServiceConfig;
import baseice.data.config.HttpServiceGeneralConfig;
import com.cwa.data.config.domain.HttpServiceConfig;
import baseice.data.config.ProtoServiceGeneralConfig;
import com.cwa.data.config.domain.ProtoServiceConfig;
import baseice.data.config.DataTimeoutServiceGeneralConfig;
import com.cwa.data.config.domain.DataTimeoutServiceConfig;
import baseice.data.config.DatabaseInfoGeneralConfig;
import com.cwa.data.config.domain.DatabaseInfoConfig;
import baseice.data.config.FunctionZKInfoGeneralConfig;
import com.cwa.data.config.domain.FunctionZKInfoConfig;
import baseice.data.config.MemberZKInfoGeneralConfig;
import com.cwa.data.config.domain.MemberZKInfoConfig;

/**
 * 对象工厂适配，通过staticId转化对应的类
 * 
 * @author mausmars
 * 
 */
public class ConfigObjectFactoryAdapter implements ObjectFactory {
	@Override
	public Object create(String type) {
		if (type.equals(IecServiceGeneralConfig.ice_staticId())){
			return new IecServiceConfig();
		}
		if (type.equals(FunctionServiceGeneralConfig.ice_staticId())){
			return new FunctionServiceConfig();
		}
		if (type.equals(MemberServiceGeneralConfig.ice_staticId())){
			return new MemberServiceConfig();
		}
		if (type.equals(DatabaseServiceGeneralConfig.ice_staticId())){
			return new DatabaseServiceConfig();
		}
		if (type.equals(NettyServiceGeneralConfig.ice_staticId())){
			return new NettyServiceConfig();
		}
		if (type.equals(MinaServiceGeneralConfig.ice_staticId())){
			return new MinaServiceConfig();
		}
		if (type.equals(HttpServiceGeneralConfig.ice_staticId())){
			return new HttpServiceConfig();
		}
		if (type.equals(ProtoServiceGeneralConfig.ice_staticId())){
			return new ProtoServiceConfig();
		}
		if (type.equals(DataTimeoutServiceGeneralConfig.ice_staticId())){
			return new DataTimeoutServiceConfig();
		}
		if (type.equals(DatabaseInfoGeneralConfig.ice_staticId())){
			return new DatabaseInfoConfig();
		}
		if (type.equals(FunctionZKInfoGeneralConfig.ice_staticId())){
			return new FunctionZKInfoConfig();
		}
		if (type.equals(MemberZKInfoGeneralConfig.ice_staticId())){
			return new MemberZKInfoConfig();
		}
		return null;
	}

	@Override
	public void destroy() {
	}
}
