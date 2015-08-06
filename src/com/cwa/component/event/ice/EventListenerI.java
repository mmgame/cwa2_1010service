package com.cwa.component.event.ice;

import java.util.Map;

import Ice.Current;
import baseice.event.AMD_IEventListener_answer;
import baseice.event.IEvent;
import baseice.event._IEventListenerDisp;

import com.cwa.component.event.IEventHandler;

/**
 * 事件监听
 * 
 * @author yangfeng
 *
 */
public class EventListenerI extends _IEventListenerDisp {
	private static final long serialVersionUID = 1L;

	private Map<Class<IEvent>, IEventHandler> eventHandlerMap;

	@Override
	public void answer_async(AMD_IEventListener_answer __cb, IEvent event, Current __current) {
		try {
			// 处理事件
			if (eventHandlerMap.containsKey(event.getClass())) {
				eventHandlerMap.get(event.getClass()).eventHandler(event);
			}
			__cb.ice_response();
		} catch (Exception ex) {
			__cb.ice_exception(ex);
		}
	}

	// ------------------------------------
	public void setEventHandlerMap(Map<Class<IEvent>, IEventHandler> eventHandlerMap) {
		this.eventHandlerMap = eventHandlerMap;
	}

}
