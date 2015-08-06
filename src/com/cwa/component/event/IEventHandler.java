package com.cwa.component.event;

import baseice.event.IEvent;

/**
 * 事件处理接口
 * 
 * @author Administrator
 * 
 */
public interface IEventHandler {
	/**
	 * 处理事件
	 * 
	 * @param event
	 */
	void eventHandler(IEvent event);
}
