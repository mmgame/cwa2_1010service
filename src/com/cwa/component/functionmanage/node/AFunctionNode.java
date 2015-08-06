package com.cwa.component.functionmanage.node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import baseice.constant.SeparatorSlash;

import com.cwa.component.functionmanage.context.IContext;
import com.cwa.component.zkservice.IZKService;
import com.cwa.util.tree.TreeNode;

/**
 * 抽象功能节点
 * 
 * @author mausmars
 * 
 */
public abstract class AFunctionNode extends TreeNode {
	protected static final Logger logger = LoggerFactory.getLogger(AFunctionNode.class);
	// ----------------------
	protected IZKService zkService;
	protected IContext context;
	
	// 当前路径
	protected String currentpath;
	// 新创建
	protected boolean isNewCreate = true;

	public AFunctionNode(Object key) {
		super(key);
	}

	public String getCurrentpath() {
		if (currentpath == null) {
			currentpath = getFullPath(SeparatorSlash.value);
		}
		return currentpath;
	}

	// --------------------------------
	public abstract void none();

	public abstract void nodeCreated();

	public abstract void nodeDeleted();

	public abstract void nodeChildrenChanged();

	public abstract void nodeDataChanged();

	// --------------------------------
	/**
	 * 得到和创建一个本地子节点（有了本地节点才可以操作zk）
	 * 
	 * @param key
	 * @return
	 */
	public abstract AFunctionNode getAndCreateLocalChild(String key);

	/**
	 * 创建zk节点（只有当前服，才会调用这个方法）
	 * 
	 * @param paths
	 */
	public abstract void createZKNode() throws Exception;

	/**
	 * 移除zk节点（只有当前服，才会调用这个方法）
	 * 
	 * @param paths
	 */
	public abstract void removeZKNode() throws Exception;

	/**
	 * 根据zk的节点，映射创建节点
	 */
	public abstract void createZKNodeMap();

	// --------------------------------
	public void setContext(IContext context) {
		this.context = context;
	}

	public void setZkService(IZKService zkService) {
		this.zkService = zkService;
	}

}
