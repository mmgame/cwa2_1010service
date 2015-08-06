package com.cwa.util.hash;

import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 一致哈希
 * 
 * @author mausmars
 * 
 * @param <T>
 */
public class ConsistentHash<T> {
	protected static final Logger logger = LoggerFactory.getLogger(ConsistentHash.class);

	// 复制的节点个数（默认为3）
	private int numberOfReplicas = 3;
	// Hash计算对象，用于自定义hash算法
	private IHashFunc hashFunc = DefaultHashFunc.defaultHashFunc;
	// ---------------------------
	// 一致性Hash环
	private final SortedMap<Integer, T> circle = new TreeMap<Integer, T>();

	/**
	 * 增加节点<br>
	 * 每增加一个节点，就会在闭环上增加给定复制节点数<br>
	 * 例如复制节点数是2，则每调用此方法一次，增加两个虚拟节点，这两个节点指向同一Node
	 * 由于hash算法会调用node的toString方法，故按照toString去重
	 * 
	 * @param node
	 *            节点对象
	 */
	public void add(T node) {
		for (int index = 0; index < numberOfReplicas; index++) {
			// 哈希值作为key
			circle.put(getKey(node, index), node);
		}
	}

	public void add(List<T> nodes) {
		for (T node : nodes) {
			add(node);
		}
	}

	/**
	 * 移除节点的同时移除相应的虚拟节点
	 * 
	 * @param node
	 *            节点对象
	 */
	public void remove(T node) {
		for (int index = 0; index < numberOfReplicas; index++) {
			T t = circle.remove(getKey(node, index));
			if (logger.isInfoEnabled()) {
				logger.info("remove node=" + t);
			}
		}
	}

	private int getKey(T node, int index) {
		int hashValue = hashFunc.hash(node.toString() + index);
		if (logger.isInfoEnabled()) {
			logger.info("GetKey hashValue=" + hashValue + " ! " + (node.toString() + index));
		}
		return hashValue;
	}

	/**
	 * 获得一个最近的顺时针节点
	 * 
	 * @param key
	 *            为给定键取Hash，取得顺时针方向上最近的一个虚拟节点对应的实际节点
	 * @return 节点对象
	 */
	public T get(Object key) {
		if (circle.isEmpty()) {
			return null;
		}
		int hash = hashFunc.hash(key);
		if (!circle.containsKey(hash)) {
			// 返回此映射的部分视图，其键大于等于hash
			SortedMap<Integer, T> tailMap = circle.tailMap(hash);
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		return circle.get(hash);
	}

	public Collection<T> getNodes() {
		return circle.values();
	}

	// ------------------------
	public void setNumberOfReplicas(int numberOfReplicas) {
		this.numberOfReplicas = numberOfReplicas;
	}

	public void setHashFunc(IHashFunc hashFunc) {
		this.hashFunc = hashFunc;
	}
}