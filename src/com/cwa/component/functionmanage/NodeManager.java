package com.cwa.component.functionmanage;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cwa.component.event.ILocalEvent;
import com.cwa.component.event.ILocalEventListener;
import com.cwa.component.functionmanage.context.IContext;
import com.cwa.component.functionmanage.node.AFunctionNode;
import com.cwa.component.functionmanage.node.FunctionTrunkModuleNode;
import com.cwa.component.functionmanage.node.NodePath;
import com.cwa.component.functionmanage.node.nenum.NodeLevelTypeEnum;
import com.cwa.component.zkservice.IZKService;
import com.cwa.util.tree.TreeNode;

/**
 * 节点管理
 * 
 * @author mausmars
 * 
 */
public class NodeManager implements ILocalEventListener {
	protected static final Logger logger = LoggerFactory.getLogger(NodeManager.class);

	// 模块节点
	private AFunctionNode moduleNode;

	public void init(String moduleName, IZKService zkService, IContext context) throws Exception {
		moduleNode = new FunctionTrunkModuleNode(moduleName);
		moduleNode.setLevel(NodeLevelTypeEnum.Module.value());
		moduleNode.setZkService(zkService);
		moduleNode.setContext(context);
		moduleNode.createZKNode();
	}

	public AFunctionNode selectNode(int rid, int ftype) {
		AFunctionNode rnode = (AFunctionNode) moduleNode.selectChild(String.valueOf(rid));
		if (rnode == null) {
			return null;
		}
		return (AFunctionNode) rnode.selectChild(String.valueOf(ftype));
	}

	public AFunctionNode selectNode(NodePath path) {
		return selectNode(moduleNode, path);
	}

	/**
	 * 查找Node
	 * 
	 * @param node
	 * @param path
	 * @return
	 */
	public AFunctionNode selectNode(TreeNode node, NodePath path) {
		if (node.getRoot().getKey().equals(path.getLastKey())) {
			// 如果是根路径
			return (AFunctionNode) node.getRoot();
		}
		String ckey = path.getChildKey(node.getLevel());
		AFunctionNode cnode = (AFunctionNode) node.selectChild(ckey);
		if (cnode == null) {
			return null;
		}
		if (cnode.getLevel() == path.getLevel()) {
			return cnode;
		} else if (cnode.getLevel() < path.getLevel()) {
			return selectNode(cnode, path);
		} else {
			return null;
		}
	}

	@Override
	public void answer(ILocalEvent localEvent) {
		try {
			if (!(localEvent instanceof NodePath)) {
				if (logger.isWarnEnabled()) {
					logger.warn("event type isn't ZKEvent!");
				}
				return;
			}
			NodePath e = (NodePath) localEvent;
			AFunctionNode node = selectNode(moduleNode, e);
			if (node == null) {
				if (logger.isInfoEnabled()) {
					logger.info("没有通知路径=" + e.toString());
				}
				return;
			}
			WatchedEvent event = e.getWatchedEvent();
			if (event.getType() == Watcher.Event.EventType.None) {
				node.none();
			} else if (event.getType() == Watcher.Event.EventType.NodeCreated) {
				node.nodeCreated();
			} else if (event.getType() == Watcher.Event.EventType.NodeDeleted) {
				node.nodeDeleted();
			} else if (event.getType() == Watcher.Event.EventType.NodeDataChanged) {
				node.nodeDataChanged();
			} else if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
				node.nodeChildrenChanged();
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public AFunctionNode getModuleNode() {
		return moduleNode;
	}

	@Override
	public Object key() {
		return moduleNode.getKey();
	}

}
