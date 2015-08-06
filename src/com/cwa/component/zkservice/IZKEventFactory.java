package com.cwa.component.zkservice;

import org.apache.zookeeper.WatchedEvent;

/**
 * 事件工厂
 * 
 * @author mausmars
 *
 */
public interface IZKEventFactory {
	IZKEvent create(WatchedEvent event);
}
