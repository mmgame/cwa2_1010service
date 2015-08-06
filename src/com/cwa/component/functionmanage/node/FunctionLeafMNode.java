package com.cwa.component.functionmanage.node;

import java.util.LinkedList;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import Ice.ObjectPrx;
import baseice.constant.SeparatorUnderline;
import baseice.service.FunctionMenu;

import com.cwa.component.functionmanage.IFunctionCluster;
import com.cwa.component.functionmanage.conostant.FunctionConstant;
import com.cwa.component.functionmanage.node.nenum.LeafNodeTypeEnum;
import com.cwa.util.hash.ConsistentHash;
import com.cwa.util.tree.TreeNode;

/**
 * 叶子管理节点。如：/sms/1/1
 * 
 * @author mausmars
 * 
 */
public class FunctionLeafMNode extends AFunctionNode implements IFunctionCluster {
	// 主服务器key
	private String masterKey;
	// 当前服务器key（如果为null就没有当前服）
	private String currentKey;

	// 一致哈希算法
	private ConsistentHash<String> consistentHash;

	public FunctionLeafMNode(String key) {
		super(key);
		consistentHash = new ConsistentHash<String>();
	}

	public AFunctionNode getAndCreateLocalChild(String key) {
		AFunctionNode node = (AFunctionNode) children.get(key);
		if (node != null) {
			node.isNewCreate = false;
			return node;
		}
		node = new FunctionLeafNodeService(key);
		insertChild(node);
		node.zkService = this.zkService;
		node.context = this.context;
		if (logger.isInfoEnabled()) {
			logger.info("本地节点被创建！ level=" + node.getLevel() + " key=" + key + " path=" + node.getCurrentpath());
		}
		currentKey = key;
		return node;
	}

	private AFunctionNode getAndCreateLocalChild2(String key) {
		AFunctionNode node = (AFunctionNode) children.get(key);
		if (node != null) {
			return node;
		}
		if (key.equals(FunctionConstant.ZK_SM_Leader_Path)) {
			// 首领路径
			node = new FunctionLeafNodeLeader(key);
		} else {
			// 功能服务路径
			node = new FunctionLeafNodeServicePrx(key);
		}
		insertChild(node);
		node.zkService = this.zkService;
		node.context = this.context;
		if (logger.isInfoEnabled()) {
			logger.info("本地节点被创建！ level=" + node.getLevel() + " key=" + key + " path=" + node.getCurrentpath());
		}
		currentKey = key;
		return node;
	}

	@Override
	public void createZKNodeMap() {
		// 创建zk节点映射；设置监听。监听功能类型
		try {
			// 这里不用设计过滤，如果不需要这个类型的服务上一层就过滤掉了，这层不会被创建
			Stat stat = zkService.exists(getCurrentpath(), false);
			if (stat == null) {
				return;
			}
			createLeaderNode();
			createChilderNode();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	void createLeaderNode() {
		// 创建领导节点的映射节点
		AFunctionNode leaderNode = getAndCreateLocalChild2(FunctionConstant.ZK_SM_Leader_Path);
		leaderNode.createZKNodeMap();
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
		// 设置子节点监听
		zkService.getChildren(getCurrentpath(), true);
	}

	@Override
	public void nodeDataChanged() {
		try {
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void nodeDeleted() {
		try {
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void nodeCreated() {
		try {
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void nodeChildrenChanged() {
		// 之前设置过监听，后边要继续监听
		try {
			createChilderNode();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	// 初始化或监听zk子节点变化，创建node
	private void createChilderNode() throws Exception {
		List<String> childrenNodeStrs = zkService.getChildren(getCurrentpath(), true);
		for (String childrenNodeStr : childrenNodeStrs) {
			String[] nodeStrs = childrenNodeStr.split(SeparatorUnderline.value);
			FunctionLeafNode childNode = (FunctionLeafNode) getAndCreateLocalChild2(nodeStrs[0]);
			if (childNode.getLeafNodeType() == LeafNodeTypeEnum.ServicePrx) {
				FunctionLeafNodeServicePrx cn = (FunctionLeafNodeServicePrx) childNode;
				cn.setAotuId(nodeStrs[1]);
				cn.createZKNodeMap();
			}
		}
	}

	@Override
	public void none() {

	}

	@Override
	public void removeZKNode() throws Exception {

	}

	void changeMasterKey(String key, long ctime) {
		masterKey = key;
	}

	void insertNode(String key) {
		consistentHash.add(key);
	}

	void removeNode(String key) {
		consistentHash.remove(key);
	}

	// ----------------------------------------
	@Override
	public boolean isAvailable() {
		return masterKey != null;
	}

	public boolean isCurrentMaster() {
		// 当前服是主服
		if (masterKey == null || currentKey == null) {
			return false;
		}
		return currentKey.equals(masterKey);
	}

	@Override
	public FunctionMenu getCurrentFunctionMenu() {
		AFunctionNode node = (AFunctionNode) this.selectChild(currentKey);
		if (node == null) {
			return null;
		}
		return ((FunctionLeafNode) node).functionMenu;
	}

	@Override
	public List<FunctionMenu> getAllFunctionMenu() {
		List<FunctionMenu> functionMenuList = new LinkedList<FunctionMenu>();
		for (TreeNode node : this.children.values()) {
			FunctionLeafNode n = (FunctionLeafNode) node;
			if (n.getLeafNodeType() == LeafNodeTypeEnum.Leader) {
				continue;
			}
			functionMenuList.add(n.functionMenu);
		}
		return functionMenuList;
	}

	@Override
	public <T extends ObjectPrx> T getMasterService(Class<T> cls) {
		return getService(masterKey, cls);
	}

	@Override
	public <T extends ObjectPrx> T getSlaveService(int fkey, Class<T> cls) {
		return getService(String.valueOf(fkey), cls);
	}

	@Override
	public <T extends ObjectPrx> List<T> getAllService(Class<T> cls) {
		List<T> prxList = new LinkedList<T>();
		for (TreeNode node : this.children.values()) {
			FunctionLeafNode n = (FunctionLeafNode) node;
			if (n.getLeafNodeType() == LeafNodeTypeEnum.Leader) {
				continue;
			}
			T prx = FunctionLeafNode.getService(n.functionMenu, cls);
			if (prx == null) {
				continue;
			}
			prxList.add(prx);
		}
		return prxList;
	}

	@Override
	public FunctionMenu getRandomFunctionMenu(Object key) {
		String k = consistentHash.get(key);
		FunctionLeafNode leafNode = (FunctionLeafNode) selectChild(k);
		if (leafNode == null) {
			return null;
		}
		return leafNode.functionMenu;
	}

	@Override
	public FunctionMenu getFunctionMenu(int fkey) {
		FunctionLeafNode leafNode = (FunctionLeafNode) selectChild(String.valueOf(fkey));
		if (leafNode == null) {
			return null;
		}
		return leafNode.functionMenu;
	}

	private <T extends ObjectPrx> T getService(String key, Class<T> cls) {
		FunctionLeafNode leafNode = (FunctionLeafNode) selectChild(key);
		if (leafNode == null) {
			return null;
		}
		return leafNode.getService(cls);
	}
}
