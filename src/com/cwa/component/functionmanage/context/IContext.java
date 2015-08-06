package com.cwa.component.functionmanage.context;

import com.cwa.component.functionmanage.node.filter.IFunctionNodeFilter;
import com.cwa.service.init.IIceServer;

/**
 * 仿真上下文
 * 
 * @author mausmars
 * 
 */
public interface IContext {
	/**
	 * ice工具
	 * 
	 * @return
	 */
	IIceServer getIceServer();

	/**
	 * 节点过滤工具
	 * 
	 * @return
	 */
	IFunctionNodeFilter getNodeFilter();
}
