package com.cwa.component.functionmanage.node;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import com.cwa.component.functionmanage.conostant.FunctionConstant;
import com.cwa.component.functionmanage.node.nenum.LeafNodeTypeEnum;

import baseice.constant.SeparatorSlash;
import baseice.constant.SeparatorUnderline;
import baseice.service.FunctionAddress;

/**
 * 当前服的功能节点
 * 
 * @author mausmars
 * 
 */
public class FunctionLeafNodeService extends FunctionLeafNode {
	// node=/sms/rid/ftype/fkey_n
	private String node;

	public FunctionLeafNodeService(String key) {
		super(key);
		leafNodeType = LeafNodeTypeEnum.Service;
	}

	@Override
	public void createZKNode() throws Exception {
		String path = getCurrentpath();
		byte[] data = fAddress2Bytes(functionMenu.fa);
		
		node = zkService.create(path + SeparatorUnderline.value, data, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		if (logger.isInfoEnabled()) {
			logger.info("【register node（创建zk临时节点）=" + node + "】");
		}
		findLeader();
	}

	@Override
	public void removeZKNode() throws Exception {
		// 删除节点
		zkService.delete(node, -1);
		if (logger.isInfoEnabled()) {
			logger.info("【unregister node=" + node);
		}
	}

	/**
	 * 选举主服务器，如果是当前服的才参与
	 * 
	 * @throws InterruptedException
	 */
	void findLeader() throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("进行选举!");
		}
		String leaderPath = parent.getFullPath(SeparatorSlash.value) + SeparatorSlash.value + FunctionConstant.ZK_SM_Leader_Path;
		try {
			Stat stat = zkService.exists(leaderPath, true);
			if (stat != null) {
				byte[] leader = zkService.getData(leaderPath, true, null);
				if (leader == null) {
					// 这里成功就表示是主服务器，后注册的抛异常
					leader = String.valueOf(key).getBytes();
					zkService.create(leaderPath, leader, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
				}
				if (logger.isInfoEnabled()) {
					logger.info("选举 结果! 首领 key=" + new String(leader));
				}
			} else {
				byte[] leader = String.valueOf(key).getBytes();
				zkService.create(leaderPath, leader, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
				if (logger.isInfoEnabled()) {
					logger.info("选举 结果! 首领 key=" + new String(leader));
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	/**
	 * FunctionAddress到字节数组
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	private byte[] fAddress2Bytes(FunctionAddress fa) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		out.writeUTF(fa.ip);
		out.writeInt(fa.port);
		out.writeInt(fa.protocol.value());
		return baos.toByteArray();
	}
}
