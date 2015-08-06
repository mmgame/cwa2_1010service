package com.cwa.component.prototype;

import java.util.List;
import java.util.concurrent.locks.Lock;

import com.cwa.service.IService;

import serverice.proto.ProtoEvent;

/**
 * 原型管理接口
 * 
 * @author yangfeng
 * 
 */
public interface IPrototypeClientService extends IService {
	/**
	 * 原型管理锁
	 * 
	 * @return
	 */
	Lock getLock();

	/**
	 * 原型事件
	 * 
	 * @param event
	 */
	void protoInform(ProtoEvent event);

	/**
	 * 获得全部原型
	 * 
	 * @param cls
	 * @return
	 */
	<T extends IPrototype> List<T> getAllPrototype(Class<T> cls);

	/**
	 * 获得指定keyId原型
	 * 
	 * @param cls
	 * @return
	 */
	<T extends IPrototype> T getPrototype(Class<T> cls, Integer keyId);
}
