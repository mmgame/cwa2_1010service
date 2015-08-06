package com.cwa.service.init.servicefactory;

import com.cwa.service.context.FilterContext;

/**
 * 服务处理器
 * 
 * @author mausmars
 *
 */
public interface IServiceFactory {
	/**
	 * 创建service
	 * 
	 * @param sc
	 * @param serviceBaseConfigMap
	 * @param c
	 */
	void createService(FilterContext c) throws Exception;
}
