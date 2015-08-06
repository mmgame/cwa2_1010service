package com.cwa.component.functionmanage.node;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

/**
 * 区节点。如： /sms/1
 * 
 * @author mausmars
 * 
 */
public class FunctionTrunkRegionNode extends AFunctionNode {
	public FunctionTrunkRegionNode(String key) {
		super(key);
	}

	// 获得并创建子节点
	@Override
	public AFunctionNode getAndCreateLocalChild(String key) {
		AFunctionNode node = (AFunctionNode) children.get(key);
		if (node != null) {
			node.isNewCreate = false;
			return node;
		}
		FunctionLeafMNode n = new FunctionLeafMNode(key);
		insertChild(n);
		n.zkService = this.zkService;
		n.context = this.context;
		
		if (logger.isInfoEnabled()) {
			logger.info("本地节点被创建！ level=" + n.getLevel() + " key=" + key + " path=" + n.getCurrentpath());
		}
		// 这里就把领导节点创建出来
		n.createLeaderNode();

		return n;
	}

	@Override
	public void createZKNodeMap() {
		// 创建zk节点映射；设置监听。监听区
		try {
			Stat stat = zkService.exists(getCurrentpath(), false);
			if (stat == null) {
				return;
			}
			// TODO 暂时为监听全部功能
			List<String> childrenNodeStrs = zkService.getChildren(getCurrentpath(), true);
			childrenNodeStrs=context.getNodeFilter().getFunctionFilterList(childrenNodeStrs);
			for (String childrenNodeStr : childrenNodeStrs) {
				AFunctionNode cnode = getAndCreateLocalChild(childrenNodeStr);
				cnode.createZKNodeMap();
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void createZKNode() throws Exception {
		String path = getCurrentpath();
		if (zkService.exists(path, true) != null) {
			if (logger.isInfoEnabled()) {
				logger.info("zk服存在当前节点，不再创建: " + path);
			}
			return;
		}
		String node = zkService.create(path, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		if (logger.isInfoEnabled()) {
			logger.info("创建zk节点 " + node);
		}
		if (true) {
			// TODO 暂时为监听全部功能
			zkService.getChildren(path, true);
		}
	}

	@Override
	public void removeZKNode() throws Exception {

	}

	@Override
	public void nodeChildrenChanged() {
		try {
			// TODO 暂时为监听全部功能
			List<String> childrenNodeStrs = zkService.getChildren(getCurrentpath(), true);
			childrenNodeStrs=context.getNodeFilter().getFunctionFilterList(childrenNodeStrs);
			for (String childNodeStr : childrenNodeStrs) {
				AFunctionNode cnode = getAndCreateLocalChild(childNodeStr);
				if (cnode.isNewCreate) {
					cnode.createZKNodeMap();
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void none() {

	}

	@Override
	public void nodeCreated() {

	}

	@Override
	public void nodeDeleted() {

	}

	@Override
	public void nodeDataChanged() {

	}
}
