package com.cwa.component.functionmanage.node.filter;

import java.util.List;

/**
 * 节点过滤接口
 * 
 * @author tzy
 * 
 * 
 */
public interface IFunctionNodeFilter {

	/**
	 * 获取区滤接后节点数组
	 * @param childrenNodeStrs
	 * @return
	 */
	List<String> getRegionFilterList(List<String> childrenNodeStrs);
	
	/**
	 * 获取功能滤接后节点数组
	 * @param childrenNodeStrs
	 * @return
	 */
	List<String> getFunctionFilterList(List<String> childrenNodeStrs);

}
