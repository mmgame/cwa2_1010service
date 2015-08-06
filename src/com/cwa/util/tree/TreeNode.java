package com.cwa.util.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树节点
 * 
 * @author mausmars
 * 
 */
public class TreeNode {
	protected Object key; // key
	protected int level; // 层

	protected TreeNode parent;// 父节点
	protected Map<Object, TreeNode> children = new HashMap<Object, TreeNode>();// 子节点

	public TreeNode(Object key) {
		this.key = key;
	}

	/**
	 * 插入子节点
	 */
	public boolean insertChild(TreeNode node) {
		if (children.containsKey(node.key)) {
			return false;
		}
		node.level = this.level + 1;
		node.parent = this;
		this.children.put(node.key, node);
		return true;
	}

	/**
	 * 得到根节点
	 * 
	 * @return
	 */
	public TreeNode getRoot() {
		if (this.parent != null) {
			return this.parent.getRoot();
		} else {
			return this;
		}
	}

	/**
	 * 得到节点全路径
	 * 
	 * @return
	 */
	public String getFullPath(String separator) {
		if (parent == null) {
			return separator + key;
		}
		String str = parent.getFullPath(separator);
		return str + separator + key;
	}

	/**
	 * 得到节点全路径
	 * 
	 * @return
	 */
	public List<String> getFullPath(int level) {
		List<String> paths = new ArrayList<String>(level);
		setPath(paths);
		return paths;
	}

	private void setPath(List<String> paths) {
		if (parent != null) {
			parent.setPath(paths);
		} else {
			paths.add("/");
		}
		paths.add(String.valueOf(key));
	}

	/**
	 * 移除节点本身
	 */
	public void remove() {
		if (parent == null) {
			return;
		}
		parent.children.remove(this);
	}

	/**
	 * 移除子节点
	 * 
	 * @param key
	 * @return
	 */
	public TreeNode removeChild(Object key) {
		return children.remove(key);
	}

	/**
	 * 查询子节点
	 * 
	 * @param key
	 * @return
	 */
	public TreeNode selectChild(Object key) {
		return children.get(key);
	}

	public boolean containsChild(Object key) {
		return children.containsKey(key);
	}

	/**
	 * 得到全部子节点
	 * 
	 * @return
	 */
	public Collection<TreeNode> getAllChildren() {
		return children.values();
	}

	/**
	 * 得到父节点
	 * 
	 * @return
	 */
	public TreeNode getParent() {
		return parent;
	}

	/**
	 * 得到节点key
	 * 
	 * @return
	 */
	public Object getKey() {
		return key;
	}

	/**
	 * 得到节点level
	 * 
	 * @return
	 */
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
}
