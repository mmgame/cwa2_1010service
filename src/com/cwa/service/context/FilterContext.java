package com.cwa.service.context;

import java.util.HashMap;
import java.util.Map;

/**
 * 初始化流上下文
 * 
 * @author mausmars
 *
 */
public class FilterContext {
	private IGloabalContext gloabalContext;
	private Map<Object, Object> storage = new HashMap<Object, Object>();

	public FilterContext(IGloabalContext gloabalContext) {
		this.gloabalContext = gloabalContext;
	}

	public Object getAttach(Object key) {
		return storage.get(key);
	}

	public void putAttach(Object key, Object attach) {
		storage.put(key, attach);
	}

	public void reset() {
		storage.clear();
	}

	public IGloabalContext getGloabalContext() {
		return gloabalContext;
	}
}
