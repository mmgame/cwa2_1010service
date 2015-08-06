package com.cwa.service.filter;

import java.util.List;

import com.cwa.util.linklist.AbstractNodeChain;

/**
 * 过滤链
 * 
 * @author mausmars
 * 
 */
public class DefaultFilterChain extends AbstractNodeChain<IFilter> implements IFilterChain {
	public DefaultFilterChain(String name, List<IFilter> nodes) {
		super(name, nodes);
	}

	@Override
	public void doFilter(IFilter next, Object context) throws Exception {
		IFilter filter = selectFirst();
		if (filter != null) {
			filter.doFilter((IFilter) filter.next(), context);
		}
	}
}
