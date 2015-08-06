package com.cwa.service.config;

/**
 * 配置转换接口
 * 
 * @author mausmars
 * 
 * @param <T>
 */
public interface IConfigChanger<T> {
	/**
	 * 转换
	 * 
	 * @return
	 */
	T change();
}
