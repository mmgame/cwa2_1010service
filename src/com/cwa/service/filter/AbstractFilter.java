package com.cwa.service.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cwa.util.linklist.AbstractNode;

/**
 * 抽象Filter
 * 
 * @author mausmars
 * 
 */
public abstract class AbstractFilter extends AbstractNode implements IFilter {
	protected static final Logger logger = LoggerFactory.getLogger(AbstractFilter.class);
	protected String name;

	public AbstractFilter(String name) {
		this.name = name;
	}

	public String key() {
		return name;
	}

	/**
	 * 做事情
	 * 
	 * @param context
	 * @return true往下执行，false不执行
	 */
	public abstract boolean doWork(Object context) throws Exception;

	@Override
	public void doFilter(IFilter next, Object context) throws Exception {
		boolean isContinue = doWork(context);
		if (isContinue && next != null) {
			IFilter n = (IFilter) next.next();
			next.doFilter(n, context);
		}
	}
}
