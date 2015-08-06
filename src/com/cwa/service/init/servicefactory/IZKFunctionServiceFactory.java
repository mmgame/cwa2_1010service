package com.cwa.service.init.servicefactory;

import com.cwa.data.config.domain.FunctionServiceConfig;
import com.cwa.data.config.domain.FunctionZKInfoConfig;
import com.cwa.service.context.IGloabalContext;

public interface IZKFunctionServiceFactory extends IServiceFactory {
	void createService(FunctionServiceConfig serviceConfig, FunctionZKInfoConfig info, IGloabalContext gloabalContext) throws Exception;
}
