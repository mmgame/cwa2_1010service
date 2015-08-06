package com.cwa.util.hash;

/**
 * Hash算法对象，用于自定义hash算法
 */
public interface IHashFunc {
	/**
	 * 计算hash值
	 * 
	 * @param key
	 * @return
	 */
	public Integer hash(Object key);
}
