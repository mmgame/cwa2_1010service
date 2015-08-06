package com.cwa.component.event;

/**
 * 本地主题接口
 * 
 * @author mausmars
 * 
 */
public interface ILocalSubject {
	/**
	 * 注册监听
	 * 
	 * @param listener
	 */
	void register(ILocalEventListener listener);

	/**
	 * 注销监听
	 * 
	 * @param listener
	 */
	void unregister(Object key);
}
