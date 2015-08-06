package com.cwa.component.functionmanage.node;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import baseice.constant.SeparatorUnderline;
import baseice.service.FunctionAddress;
import baseice.service.FunctionMenu;
import baseice.service.IMasterServicePrx;
import baseice.service.NetProtocolEnum;
import baseice.service.ServiceInfo;

import com.cwa.component.functionmanage.node.nenum.LeafNodeTypeEnum;
import com.cwa.service.ServiceUtil;

/**
 * 其他服的功能节点
 * 
 * @author mausmars
 * 
 */
public class FunctionLeafNodeServicePrx extends FunctionLeafNode {
	private String aotuId;
	private IMasterServicePrx masterServicePrx;

	public FunctionLeafNodeServicePrx(String key) {
		super(key);
		leafNodeType = LeafNodeTypeEnum.ServicePrx;
	}

	@Override
	public void createZKNodeMap() {
		// 创建zk节点映射；设置监听。监听功能类型
		try {
			// 这里不为空，就初始化functionMenu
			initIceFunction(getCurrentpath() + SeparatorUnderline.value + aotuId);
			insertNode();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void nodeDataChanged() {
		// 数据改变证明服务器注册成功，这里调用ice获取功能服务信息
		try {
			String path = getCurrentpath() + SeparatorUnderline.value + aotuId;
			initIceFunction(path);
			insertNode();
			if (logger.isInfoEnabled()) {
				logger.info("Service nodeDataChanged! pkey=" + parent.getKey() + " key=" + key);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void nodeDeleted() {
		try {
			removeNode();

			// 设置服务器为空，不可用
			functionMenu = null;
			// 从父节点删除该孩子
			parent.removeChild(key);
			if (logger.isInfoEnabled()) {
				logger.info("Service nodeDeleted! pkey=" + parent.getKey() + " key=" + key);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void nodeCreated() {
		try {
			if (logger.isInfoEnabled()) {
				logger.info("Service nodeCreated! pkey=" + parent.getKey() + " key=" + key);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	private void initIceFunction(String path) throws Exception {
		if (masterServicePrx != null) {
			return;
		}
		byte[] bytes = zkService.getData(path, true, null);
		if (bytes == null) {
			return;
		}
		FunctionAddress fa = bytes2FAddress(bytes);
		masterServicePrx = context.getIceServer().getMasterServicePrx(fa);

		// 这里调用ice的接口，获取服务
		functionMenu = getRemoteFunctionMenu();
		ServiceUtil.functionMenuPrint("IceConnect", functionMenu);
	}

	private FunctionMenu getRemoteFunctionMenu() {
		FunctionMenu fm = masterServicePrx.getFunctionMenu();
		for (ServiceInfo si : fm.serviceInfos.values()) {
			// 转成正确的代理类
			si.server = ServiceUtil.getServicePrx(si);
		}
		return fm;
	}

	public void setAotuId(String aotuId) {
		this.aotuId = aotuId;
	}

	/**
	 * 字节数组到FunctionAddress
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	private FunctionAddress bytes2FAddress(byte[] bytes) throws IOException {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
		FunctionAddress fa = new FunctionAddress();
		fa.ip = in.readUTF();
		fa.port = in.readInt();
		fa.protocol = NetProtocolEnum.valueOf(in.readInt());
		return fa;

	}

	// 调用父节点移除节点环
	private void removeNode() {
		FunctionLeafMNode parentNode = (FunctionLeafMNode) this.parent;
		parentNode.removeNode((String) key);
	}

	// 调用父节点插入节点环
	private void insertNode() {
		FunctionLeafMNode parentNode = (FunctionLeafMNode) this.parent;
		parentNode.insertNode((String) key);
	}
}
