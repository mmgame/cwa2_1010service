package com.cwa.component.functionmanage.node;

import org.apache.zookeeper.data.Stat;

import com.cwa.component.functionmanage.node.nenum.LeafNodeTypeEnum;
import com.cwa.util.tree.TreeNode;

/**
 * 这个路径节点是监听该功能类型主从的节点
 * 
 * @author mausmars
 * 
 */
public class FunctionLeafNodeLeader extends FunctionLeafNode {
	public FunctionLeafNodeLeader(String key) {
		super(key);
		leafNodeType = LeafNodeTypeEnum.Leader;
	}

	@Override
	public void createZKNodeMap() {
		// 创建zk节点映射；设置监听。监听功能类型
		try {
			// 获取主服务器信息
			Stat stat = zkService.exists(getCurrentpath(), true);
			if (stat != null) {
				Stat state = new Stat();
				byte[] leader = zkService.getData(getCurrentpath(), true, state);
				if (leader != null) {
					changeMasterKey(new String(leader), state.getCtime());
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void nodeDataChanged() {
		// 数据改变证明服务器注册成功，这里调用ice获取功能服务信息
		try {
			if (logger.isInfoEnabled()) {
				logger.info("Leader nodeDataChanged! pkey=" + parent.getKey());
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void nodeDeleted() {
		try {
			// 设置为无领导
			changeMasterKey(null, 0);

			// 重新监听
			zkService.exists(getCurrentpath(), true);

			// 这里遍历重新选举
			for (TreeNode node : parent.getAllChildren()) {
				FunctionLeafNode n = (FunctionLeafNode) node;
				if (n.getLeafNodeType() == LeafNodeTypeEnum.Service) {
					FunctionLeafNodeService fln = (FunctionLeafNodeService) n;
					fln.findLeader();
				}
			}
			if (logger.isInfoEnabled()) {
				logger.info("Leader nodeDeleted! pkey=" + parent.getKey());
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void nodeCreated() {
		try {
			// 获取并重新设置监听
			Stat state = new Stat();
			byte[] leader = zkService.getData(getCurrentpath(), true, state);
			if (leader != null) {
				changeMasterKey(new String(leader), state.getCtime());
			}
			if (logger.isInfoEnabled()) {
				logger.info("Leader nodeCreated! pkey=" + parent.getKey());
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	private void changeMasterKey(String key, long ctime) {
		// 调用父节点改变
		FunctionLeafMNode parentNode = (FunctionLeafMNode) this.parent;
		parentNode.changeMasterKey(key, ctime);
		if (logger.isInfoEnabled()) {
			logger.info("Leader 节点变更 ! pkey=" + parent.getKey() + " 领导key=" + key + " ctime=" + ctime);
		}
	}
}
