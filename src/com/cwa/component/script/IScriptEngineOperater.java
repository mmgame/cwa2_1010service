package com.cwa.component.script;

/**
 * 引擎操作
 * 
 * @author mausmars
 * 
 */
public interface IScriptEngineOperater {
	/**
	 * 脚本引擎重置
	 * 
	 * @param scriptConfig
	 */
	void resetConfig(ScriptConfig scriptConfig);

	/**
	 * 获得脚本接口
	 * 
	 * @param clasz
	 * @return
	 */
	<T> T getInterface(Class<T> clasz);

	/**
	 * 获得脚本接口
	 * 
	 * @param mkey
	 * @param clasz
	 * @return
	 */
	<T> T getInterface(String mkey, Class<T> clasz);

	/**
	 * 脚本方法调用
	 * 
	 * @param name
	 * @param args
	 * @return
	 */
	Object invokeFunction(String name, Object... args);

	/**
	 * 脚本方法调用
	 * 
	 * @param mkey
	 * @param name
	 * @param args
	 * @return
	 */
	Object invokeMethod(String mkey, String name, Object... args);
}
