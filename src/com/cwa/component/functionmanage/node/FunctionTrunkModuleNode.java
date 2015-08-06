package com.cwa.component.functionmanage.node;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

/**
 * 模块节点。如：/sms。这个节点保存每个区节点
 * 
 * @author mausmars
 * 
 */
public class FunctionTrunkModuleNode extends AFunctionNode {
	public FunctionTrunkModuleNode(String key) {
		super(key);
	}

	public AFunctionNode getAndCreateLocalChild(String key) {
		AFunctionNode node = (AFunctionNode) children.get(key);
		if (node != null) {
			node.isNewCreate = false;
			return node;
		}
		node = new FunctionTrunkRegionNode(key);
		insertChild(node);
		node.zkService = this.zkService;
		node.context = this.context;
		if (logger.isInfoEnabled()) {
			logger.info("本地节点被创建！ level=" + node.getLevel() + " key=" + key + " path=" + node.getCurrentpath());
		}
		return node;
	}

	@Override
	public void createZKNodeMap() {
		try {
			Stat stat = zkService.exists(getCurrentpath(), false);
			if (stat == null) {
				return;
			}
			// TODO 暂时为监听全部区
			List<String> childrenNodeStrs = zkService.getChildren(getCurrentpath(), true);
			childrenNodeStrs=context.getNodeFilter().getRegionFilterList(childrenNodeStrs);
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
			// TODO 暂时为监听全部区
			zkService.getChildren(path, true);
		}
	}

	@Override
	public void removeZKNode() throws Exception {

	}

	@Override
	public void nodeChildrenChanged() {
		try {
			// TODO 暂时为监听全部区
			List<String> childrenNodeStrs = zkService.getChildren(getCurrentpath(), true);
			childrenNodeStrs=context.getNodeFilter().getRegionFilterList(childrenNodeStrs);
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
