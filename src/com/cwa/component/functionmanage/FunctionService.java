package com.cwa.component.functionmanage;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import serverice.config.ServiceConfigTypeEnum;
import Ice.ObjectPrx;
import baseice.service.FunctionId;
import baseice.service.FunctionMenu;
import baseice.service.FunctionTypeEnum;

import com.cwa.component.event.ILocalEvent;
import com.cwa.component.event.ILocalEventListener;
import com.cwa.component.functionmanage.conostant.FunctionConstant;
import com.cwa.component.functionmanage.context.IContext;
import com.cwa.component.functionmanage.node.AFunctionNode;
import com.cwa.component.functionmanage.node.FunctionLeafMNode;
import com.cwa.component.functionmanage.node.FunctionLeafNode;
import com.cwa.component.functionmanage.node.NodePath;
import com.cwa.component.functionmanage.node.filter.IFunctionNodeFilter;
import com.cwa.component.functionmanage.node.nenum.NodeLevelTypeEnum;
import com.cwa.component.zkservice.IZKService;
import com.cwa.service.IService;
import com.cwa.service.init.IIceServer;

/**
 * 功能service
 * 
 * @author mausmars
 * 
 */
public class FunctionService implements IContext, IFunctionService, ILocalEventListener {
	protected static final Logger logger = LoggerFactory.getLogger(IService.class);

	private String key;
	private int version;

	// zk服务
	private IZKService zkService;
	private String moduleName;
	private IFunctionNodeFilter nodeFilter;
	// --------------------------------------
	private IIceServer iceServer;
	private NodeManager nodeManager = new NodeManager();;

	// 已经注册的功能服务菜单
	private Map<String, FunctionMenu> fmRegisteredMap = new HashMap<String, FunctionMenu>();
	// 是否启动
	private boolean isStarted;

	public FunctionService(String key) {
		this.key = key;
	}

	@Override
	public Object key() {
		return FunctionConstant.SessionExpiredListenerKey;
	}

	@Override
	public void answer(ILocalEvent event) {
		if (logger.isInfoEnabled()) {
			logger.info("Reset ZK watch!");
		}
		for (FunctionMenu functionMenu : fmRegisteredMap.values()) {
			// 重新注册
			registerC(functionMenu);
		}
		createZKPrx();
	}

	public void startup() throws Exception {
		if (!isStarted) {
			if (logger.isInfoEnabled()) {
				logger.info("FunctionService start! key=" + key + " " + zkService.toString());
			}
			zkService.startup();
			nodeManager.init(moduleName, zkService, this);
			// 注册监听
			zkService.registerListener(nodeManager);
			zkService.registerListener(this);
			isStarted = true;
			if (logger.isInfoEnabled()) {
				logger.info("FunctionService start end! key=" + key + " " + zkService.toString());
			}
		}
	}

	public void shutdown() throws Exception {
		isStarted=false;
		zkService.shutdown();
	}

	/**
	 * 注册功能服务
	 * 
	 * @param fid
	 * @return
	 */
	@Override
	public AFunctionNode register(FunctionMenu functionMenu) {
		String key = NodePath.getFKeyFullPath(moduleName, functionMenu.fid);
		if (fmRegisteredMap.containsKey(key)) {
			return null;
		}
		fmRegisteredMap.put(key, functionMenu);
		return registerC(functionMenu);
	}

	private AFunctionNode registerC(FunctionMenu functionMenu) {
		try {
			// 根据功能创建节点
			AFunctionNode node = nodeManager.getModuleNode();
			NodePath nodePath = NodePath.createNodePath(node.getKey(), functionMenu.fid);
			int lastLevel = nodePath.getLevel();
			for (int i = node.getLevel(); i < lastLevel; i++) {
				String ckey = nodePath.getChildKey(i);
				node = (AFunctionNode) node.getAndCreateLocalChild(ckey);
				if (node.getLevel() == NodeLevelTypeEnum.FKey.value()) {
					FunctionLeafNode n = (FunctionLeafNode) node;
					n.setFunctionMenu(functionMenu);
				}
				node.createZKNode();
			}
			return node;
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

	/**
	 * 注销功能服务
	 * 
	 * @param fid
	 * @return
	 */
	@Override
	public void unregister(FunctionId fid) {
		AFunctionNode node = nodeManager.getModuleNode();

		NodePath nodePath = NodePath.createNodePath(node.getKey(), fid);
		node = nodeManager.selectNode(node, nodePath);
		if (node == null) {
			return;
		}
		try {
			node.removeZKNode();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void createZKPrx() {
		AFunctionNode node = nodeManager.getModuleNode();
		node.createZKNodeMap();
	}

	@Override
	public IIceServer getIceServer() {
		return iceServer;
	}

	@Override
	public IFunctionCluster getFunctionCluster(int gid, FunctionTypeEnum ftype) {
		AFunctionNode tnode = nodeManager.selectNode(gid, ftype.value());
		if (tnode == null) {
			return null;
		}
		return (FunctionLeafMNode) tnode;
	}

	@Override
	public <T extends ObjectPrx> T getIcePrx(int gid, FunctionTypeEnum ftype, Class<T> cls) {
		IFunctionCluster functionCluster = getFunctionCluster(gid, ftype);
		if (functionCluster == null) {
			return null;
		}
		return functionCluster.getMasterService(cls);
	}

	@Override
	public IFunctionNodeFilter getNodeFilter() {
		return nodeFilter;
	}

	@Override
	public int getServiceType() {
		return ServiceConfigTypeEnum.Function.value();
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public int getVersion() {
		return version;
	}

	// -------------------------------------------------------
	public void setZkService(IZKService zkService) {
		this.zkService = zkService;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public void setIceServer(IIceServer iceServer) {
		this.iceServer = iceServer;
	}

	public void setNodeFilter(IFunctionNodeFilter nodeFilter) {
		this.nodeFilter = nodeFilter;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
