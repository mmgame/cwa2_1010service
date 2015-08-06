package com.cwa.component.membermanager;

import java.util.HashMap;
import java.util.Map;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import serverice.config.ServiceConfigTypeEnum;
import baseice.constant.SeparatorSlash;

import com.cwa.component.event.ILocalEvent;
import com.cwa.component.event.ILocalEventListener;
import com.cwa.component.zkservice.IZKService;

/**
 * 数据服务
 * 
 * @author mausmars
 *
 */
public class MemberService implements IMemberService, ILocalEventListener {
	protected static final Logger logger = LoggerFactory.getLogger(IMemberService.class);

	private String key;
	private int version;

	// zk服务
	private IZKService zkService;
	private String moduleName;// mms

	// 是否启动
	private boolean isStarted;

	private static Map<Integer, Class<? extends IMemberData>> dataClassMap = new HashMap<Integer, Class<? extends IMemberData>>();
	static {
		// 初始化
		dataClassMap.put(MemberTypeEnum.MT_User.value(), UserMemberData.class);
	}

	// --------------------------------
	public MemberService(String key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public int getServiceType() {
		return ServiceConfigTypeEnum.Member.value();
	}

	@Override
	public void startup() throws Exception {
		if (!isStarted) {
			if (logger.isInfoEnabled()) {
				logger.info("DataService start! key=" + key + " " + zkService.toString());
			}
			zkService.startup();
			// 注册监听
			zkService.registerListener(this);

			init();
			isStarted = true;
			if (logger.isInfoEnabled()) {
				logger.info("DataService start end! key=" + key + " " + zkService.toString());
			}
		}
	}

	private void init() throws Exception {
		String path = SeparatorSlash.value + moduleName;
		createNode(path);

		for (MemberTypeEnum mtype : MemberTypeEnum.values()) {
			path = SeparatorSlash.value + moduleName + SeparatorSlash.value + mtype;
			createNode(path);
		}
	}

	private void createNode(String path) throws Exception {
		if (zkService.exists(path, false) != null) {
			if (logger.isInfoEnabled()) {
				logger.info("DataService存在当前节点，不再创建: " + path);
			}
			return;
		}
		String node = zkService.create(path, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		if (logger.isInfoEnabled()) {
			logger.info("DataService 创建节点 " + node);
		}
	}

	@Override
	public void shutdown() throws Exception {
		isStarted = false;
		zkService.shutdown();
	}

	@Override
	public Object key() {
		return MemberConstant.SessionExpiredListenerKey;
	}

	@Override
	public void answer(ILocalEvent event) {
		if (logger.isInfoEnabled()) {
			logger.info("DataService Reset ZK watch!");
		}
	}

	private String getPath(int mtype, long key) {
		return SeparatorSlash.value + moduleName + SeparatorSlash.value + mtype + SeparatorSlash.value + key;
	}

	@Override
	public void insertMemberData(IMemberData memberData) {
		try {
			String path = getPath(memberData.getMemberType(), memberData.getKey());

			if (zkService.exists(path, false) != null) {
				zkService.setData(path, memberData.getData(), -1);
			} else {
				zkService.create(path, memberData.getData(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void removeMemberData(int mtype, long key) {
		try {
			String path = getPath(mtype, key);
			zkService.delete(path, -1);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public IMemberData selectMemberData(int mtype, long key) {
		try {
			String path = getPath(mtype, key);
			Stat s = zkService.exists(path, false);
			if (s == null) {
				return null;
			}
			Stat state = new Stat();
			byte[] d = zkService.getData(path, false, state);
			if (d == null) {
				return null;
			}
			Class<? extends IMemberData> cls = dataClassMap.get(mtype);
			IMemberData data = cls.newInstance();
			data.initData(d);
			return data;
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

	// ------------------------
	public void setVersion(int version) {
		this.version = version;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public void setZkService(IZKService zkService) {
		this.zkService = zkService;
	}
}