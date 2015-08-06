package com.cwa.component.functionmanage.node;

import org.apache.zookeeper.WatchedEvent;

import com.cwa.component.functionmanage.node.nenum.NodeLevelTypeEnum;
import com.cwa.component.zkservice.IZKEvent;

import baseice.constant.SeparatorSlash;
import baseice.service.FunctionId;

/**
 * 节点路径
 * 
 * @author mausmars
 * 
 */
public class NodePath implements IZKEvent {
	/**
	 * [/,sms,rid,ftype,fkey]
	 */
	private String[] paths;
	private WatchedEvent watchedEvent;

	public NodePath(String path) {
		this.paths = path.split(SeparatorSlash.value);
		this.paths[NodeLevelTypeEnum.Root.value()] = SeparatorSlash.value;
	}

	public NodePath(String[] paths) {
		this.paths = paths;
		this.paths[NodeLevelTypeEnum.Root.value()] = SeparatorSlash.value;
	}

	public NodePath(WatchedEvent watchedEvent) {
		this.watchedEvent = watchedEvent;
		if (watchedEvent.getPath() == null) {
			paths = null;
		} else {
			this.paths = watchedEvent.getPath().split(SeparatorSlash.value);
			this.paths[NodeLevelTypeEnum.Root.value()] = SeparatorSlash.value;
		}
	}

	// ------------------------------
	public String getKey(int level) {
		if (level < 0 || level > paths.length - 1) {
			return null;
		}
		return paths[level];
	}

	public String getParentKey(int level) {
		return getKey(level - 1);
	}

	public String getChildKey(int level) {
		return getKey(level + 1);
	}

	public int getLevel() {
		return paths.length - 1;
	}

	public String getLastKey() {
		return paths[getLevel()];
	}

	public String[] getPaths() {
		return paths;
	}

	public WatchedEvent getWatchedEvent() {
		return watchedEvent;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String path : paths) {
			sb.append(path);
			sb.append(" ");
		}
		return "NodePath [paths=" + sb.toString() + "]";
	}

	/**
	 * 根据root，fid生成路径数组
	 * 
	 * @param root
	 * @param fid
	 * @return
	 */
	public static NodePath createNodePath(Object module, FunctionId fid) {
		String[] paths = new String[NodeLevelTypeEnum.values().length];
		paths[NodeLevelTypeEnum.Root.value()] = String.valueOf("/");
		paths[NodeLevelTypeEnum.Module.value()] = String.valueOf(module);
		paths[NodeLevelTypeEnum.Region.value()] = String.valueOf(fid.gid);
		paths[NodeLevelTypeEnum.FType.value()] = String.valueOf(fid.ftype);
		paths[NodeLevelTypeEnum.FKey.value()] = String.valueOf(fid.fkey);
		NodePath nodePath = new NodePath(paths);
		return nodePath;
	}

	/**
	 * 得到fkey一级的全路径
	 * 
	 * @param fid
	 * @return
	 */
	public static String getFKeyFullPath(String module, FunctionId fid) {
		StringBuilder sb = new StringBuilder();
		sb.append(module);
		sb.append(SeparatorSlash.value);
		sb.append(fid.gid);
		sb.append(SeparatorSlash.value);
		sb.append(fid.ftype);
		sb.append(SeparatorSlash.value);
		sb.append(fid.fkey);
		return sb.toString();
	}

	/**
	 * 得到ftype一级的全路径
	 * 
	 * @param fid
	 * @return
	 */
	public static String getFTypeFullPath(String module, FunctionId fid) {
		StringBuilder sb = new StringBuilder();
		sb.append(module);
		sb.append(SeparatorSlash.value);
		sb.append(fid.gid);
		sb.append(SeparatorSlash.value);
		sb.append(fid.ftype);
		return sb.toString();
	}

}
