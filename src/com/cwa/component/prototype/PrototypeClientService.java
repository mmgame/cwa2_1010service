package com.cwa.component.prototype;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import serverice.config.ServiceConfigTypeEnum;
import serverice.proto.ChangeProtoInfo;
import serverice.proto.IProtoServicePrx;
import serverice.proto.ProtoEvent;
import serverice.proto.ProtoInfo;
import baseice.service.FunctionTypeEnum;

import com.cwa.component.functionmanage.IFunctionCluster;
import com.cwa.service.IService;
import com.cwa.service.context.IGloabalContext;
import com.cwa.util.FileUtil;
import com.cwa.util.prototype.JsonUtil;
import com.cwa.util.prototype.StringUtil;

/**
 * 逻辑模块接收到原型改变事件后去原型服拿数据并更新
 * 
 * @author yangfeng
 * 
 */
public class PrototypeClientService implements IPrototypeClientService {
	protected static final Logger logger = LoggerFactory.getLogger(IService.class);

	private String key;
	private int version;
	private int ftype;
	private IGloabalContext gloabalContext;

	private List<Integer> rids;

	private int protoVersion;// 原型版本
	private Set<String> protoNameSet = new HashSet<String>();// 当前模块需要用到的原型表
	private ConcurrentHashMap<String, Map<Integer, IPrototype>> protoInfoMap;// 原型数据

	private String path = "moduleVersion//";

	private String jsonPath = "moduleprotoJson/";
	private final Lock lock = new ReentrantLock();

	// 是否启动
	private boolean isStarted;

	public PrototypeClientService(String key) {
		this.key = key;
		protoInfoMap = new ConcurrentHashMap<String, Map<Integer, IPrototype>>();
	}

	@Override
	public void startup() throws Exception {
		if (!isStarted) {
			if (logger.isInfoEnabled()) {
				logger.info("PrototypeClientService start! key=" + key);
			}

			readFromFile();
			// 远程调用检测原型
			ChangeProtoInfo info = checkProto(rids.get(0));
			if (info.version > protoVersion) {
				// 有原型改变
				getChangeProtoInfo(info);
			}
			isStarted = true;
			if (logger.isInfoEnabled()) {
				logger.info("PrototypeClientService start end! key=" + key);
			}
		}
	}

	@Override
	public void shutdown() throws Exception {

	}

	@Override
	public void protoInform(ProtoEvent event) {
		ChangeProtoInfo changeProtoInfo = event.changeInfo;
		ProtoInfo protoInfo = getChangeProtoInfo(changeProtoInfo);
		if (protoInfo == null) {
			this.protoVersion = changeProtoInfo.version;
			write2File();
			return;
		}
		changeProto(protoInfo);
	}

	@Override
	public <T extends IPrototype> List<T> getAllPrototype(Class<T> cls) {
		List<T> list = new ArrayList<T>();
		String protoName = cls.getSimpleName();
		protoName = protoName.substring(0, protoName.indexOf("Prototype"));
		list.addAll((Collection<? extends T>) protoInfoMap.get(protoName).values());
		return list;
	}

	@Override
	public <T extends IPrototype> T getPrototype(Class<T> cls, Integer keyId) {
		String protoName = cls.getSimpleName();
		protoName = protoName.substring(0, protoName.indexOf("Prototype"));
		return (T) protoInfoMap.get(protoName).get(keyId);
	}

	@Override
	public Lock getLock() {
		return this.lock;
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
		return ServiceConfigTypeEnum.Protoclient.value();
	}

	private ChangeProtoInfo checkProto(int rid) {
		ChangeProtoInfo info = new ChangeProtoInfo();
		info.version = protoVersion;
		info.protoNameList = new ArrayList<>();
		info.protoNameList.addAll(protoNameSet);

		// 远程调用检测原型
		IFunctionCluster functionCluster = gloabalContext.getCurrentFunctionService().getFunctionCluster(0, FunctionTypeEnum.Proto);
		IProtoServicePrx prx = null;
		if (functionCluster != null) {
			prx = functionCluster.getMasterService(IProtoServicePrx.class);
		}
		if (prx != null) {
			info = prx.checkPrototype(info, rid, ftype);
		}
		return info;
	}

	private void write2File() {
		FileUtil.write2File("version", String.valueOf(protoVersion), path);
	}

	private void write2File(String name, Map<Integer, IPrototype> map) {// 将原型数据写入到文件中
		List<IPrototype> list = new ArrayList<IPrototype>();
		for (Integer key : map.keySet()) {
			list.add(map.get(key));
		}
		String content = JsonUtil.transferListToJson(list);
		FileUtil.write2File(name, content, jsonPath);
	}

	private void readFromFile() {
		try {
			for (String name : protoNameSet) {
				name = StringUtil.upperFirstString(name);
				String className = "com.cwa.prototype." + name + "Prototype";
				@SuppressWarnings("unchecked")
				Class<? extends IPrototype> clazz = (Class<? extends IPrototype>) Class.forName(className);
				String fileName = jsonPath + clazz.getSimpleName() + ".json";
				File f = new File(fileName);
				if (!f.exists()) {
					continue;
				}
				Map<Integer, IPrototype> protoMap = new HashMap<Integer, IPrototype>();
				StringBuilder fileContent = new StringBuilder();
				List<String> rows = org.apache.commons.io.FileUtils.readLines(f, "utf-8");
				for (String row : rows) {
					fileContent.append(row);
				}
				List<String> jsonList = StringUtil.getJsonList(fileContent.toString());
				for (String str : jsonList) {
					IPrototype proto = JsonUtil.transferJsonTOJavaBean(str, clazz);
					proto.obtainAfter();
					protoMap.put(proto.getKeyId(), proto);
				}
				protoInfoMap.put(name, protoMap);
			}

			File f = new File(path + "version.json");
			if (!f.exists()) {
				FileUtil.write2File("version", String.valueOf(1), path);
				return;
			}
			protoVersion = Integer.parseInt(FileUtils.readFileToString(f));
		} catch (IOException | ClassNotFoundException e) {
			logger.error("", e);
		}
	}

	// 从原型服拿变化的原型信息
	private ProtoInfo getChangeProtoInfo(ChangeProtoInfo changeProtoInfo) {
		ProtoInfo protoInfo = new ProtoInfo();
		int protoVersion = changeProtoInfo.version;
		if (this.protoVersion >= protoVersion) {
			return null;
		} else {
			List<String> protoNameList = changeProtoInfo.protoNameList;
			ChangeProtoInfo info = new ChangeProtoInfo();
			info.version = protoVersion;
			List<String> protoName = new ArrayList<String>();
			for (String name : protoNameList) {
				if (protoNameSet.contains(name) || protoNameSet.contains(StringUtil.lowerFirstString(name))) {
					protoName.add(name);
				}
			}
			if (protoName.size() == 0) {
				return null;
			}
			info.protoNameList = protoName;

			// 远程调用
			IFunctionCluster functionCluster = gloabalContext.getCurrentFunctionService().getFunctionCluster(0, FunctionTypeEnum.Proto);
			IProtoServicePrx prx = null;
			if (functionCluster != null) {
				prx = functionCluster.getMasterService(IProtoServicePrx.class);
			}
			if (prx != null) {
				protoInfo = prx.getPrototype(info);
			}
		}
		changeProto(protoInfo);
		return protoInfo;
	}

	// 改变原型
	private void changeProto(ProtoInfo proto) {
		if (proto.version == protoVersion) {
			return;
		} else if (proto.version < protoVersion) {
			this.protoVersion = proto.version;
			write2File();
			return;
		} else if (proto.version > protoVersion) {// 有原型改变
			lock.lock();
			try {
				this.protoVersion = proto.version;
				write2File();
				Map<String, Map<Integer, byte[]>> map = proto.map;
				for (String key : map.keySet()) {
					String className = "com.cwa.prototype." + key + "Prototype";
					@SuppressWarnings("unchecked")
					Class<? extends IPrototype> clazz = (Class<? extends IPrototype>) Class.forName(className);
					if (protoNameSet.contains(key) || protoNameSet.contains(StringUtil.lowerFirstString(key))) {
						Map<Integer, byte[]> m = map.get(key);
						Map<Integer, IPrototype> protoMap = new HashMap<Integer, IPrototype>();
						for (Integer k : m.keySet()) {
							Method mathod = clazz.getMethod("parseFrom", byte[].class);
							byte[] bs = m.get(k);
							IPrototype prototype = (IPrototype) mathod.invoke(clazz.newInstance(), bs);
							protoMap.put(k, prototype);
						}
						protoInfoMap.put(key, protoMap);
					}
				}
			} catch (Exception e) {
				logger.error("", e);
			} finally {
				lock.unlock();
				for (String key : proto.map.keySet()) {
					write2File(key, protoInfoMap.get(key));
				}
			}
		}
	}

	// -------------------------------------------------------------
	public void setProtoNameSet(List<String> protoNameSet) {
		this.protoNameSet.addAll(protoNameSet);
	}

	public void setGloabalContext(IGloabalContext gloabalContext) {
		this.gloabalContext = gloabalContext;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setFtype(int ftype) {
		this.ftype = ftype;
	}

	public void setRids(List<Integer> rids) {
		this.rids = rids;
	}
}
