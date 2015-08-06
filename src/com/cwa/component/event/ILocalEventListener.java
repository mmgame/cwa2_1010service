package com.cwa.component.event;

/**
 * 本地事件监听接口
 * 
 * @author mausmars
 * 
 */
public interface ILocalEventListener {
	/**
	 * 监听者key
	 * 
	 * @param event
	 */
	Object key();

	/**
	 * 响应事件
	 * 
	 * @param event
	 */
	void answer(ILocalEvent event);
}
