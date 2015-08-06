package com.cwa.component.membermanager;

import org.apache.zookeeper.WatchedEvent;

import com.cwa.component.functionmanage.node.NodePath;
import com.cwa.component.zkservice.IZKEvent;

/**
 * zk事件
 * 
 * @author mausmars
 * 
 */
public class MemberDataEvent implements IZKEvent {
	private NodePath nodePath;
	private WatchedEvent watchedEvent;

	public MemberDataEvent(WatchedEvent watchedEvent) {
		this.watchedEvent = watchedEvent;
		if (watchedEvent.getPath() == null) {
			nodePath = null;
		} else {
			nodePath = new NodePath(watchedEvent.getPath());
		}
	}

	public NodePath getNodePath() {
		return nodePath;
	}

	public WatchedEvent getWatchedEvent() {
		return watchedEvent;
	}

	public void setWatchedEvent(WatchedEvent watchedEvent) {
		this.watchedEvent = watchedEvent;
	}
}
