package com.cwa.component.functionmanage.node;

import Ice.ObjectPrx;
import baseice.service.FunctionMenu;
import baseice.service.ServiceInfo;

import com.cwa.component.functionmanage.node.nenum.LeafNodeTypeEnum;

public abstract class FunctionLeafNode extends AFunctionNode {
	protected FunctionMenu functionMenu;
	protected LeafNodeTypeEnum leafNodeType;

	public FunctionLeafNode(Object key) {
		super(key);
	}

	protected <T extends ObjectPrx> T getService(Class<T> cls) {
		return getService(functionMenu, cls);
	}

	public static <T extends ObjectPrx> T getService(FunctionMenu functionMenu, Class<T> cls) {
		if (functionMenu == null) {
			return null;
		}
		ServiceInfo serviceInfo = functionMenu.serviceInfos.get(cls.getSimpleName());
		if (serviceInfo == null) {
			return null;
		}
		return (T) serviceInfo.server;
	}

	public LeafNodeTypeEnum getLeafNodeType() {
		return leafNodeType;
	}

	public FunctionMenu getFunctionMenu() {
		return functionMenu;
	}

	public void setFunctionMenu(FunctionMenu functionMenu) {
		this.functionMenu = functionMenu;
	}

	// --------------------
	@Override
	public void none() {
	}

	@Override
	public void nodeChildrenChanged() {
	}

	@Override
	public AFunctionNode getAndCreateLocalChild(String key) {
		return null;
	}

	@Override
	public void createZKNode() throws Exception {
	}

	@Override
	public void removeZKNode() throws Exception {
	}

	@Override
	public void createZKNodeMap() {
	}

	@Override
	public void nodeDeleted() {
	}

	@Override
	public void nodeCreated() {
	}

	@Override
	public void nodeDataChanged() {
	}
}
