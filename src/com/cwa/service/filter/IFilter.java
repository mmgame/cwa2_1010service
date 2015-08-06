package com.cwa.service.filter;

import com.cwa.util.linklist.INode;

/**
 * 过滤接口
 * 
 * @author mausmars
 * 
 */
public interface IFilter extends INode {
	/**
	 * 执行
	 * 
	 * @param next
	 * @param context
	 */
	void doFilter(IFilter next, Object context) throws Exception;
}
