package com.cwa.component.script;

import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * js引擎操作实现
 * 
 * @author mausmars
 * 
 */
public class JSScriptEngineOperater implements IScriptEngineOperater {
	/**
	 * 引擎可用控制
	 */
	private boolean isAvailable;
	/**
	 * 引擎管理者实例
	 */
	private ScriptEngineManager factory;
	/**
	 * 引擎实例
	 */
	private volatile ScriptEngine se;

	public JSScriptEngineOperater(ScriptEngineManager factory) {
		this.factory = factory;
	}

	@Override
	public void resetConfig(ScriptConfig scriptConfig) {
		try {
			if (se == null) {
				se = factory.getEngineByName("JavaScript");
				init(se, scriptConfig);
				isAvailable = true;
			} else {
				if (scriptConfig.isBuildEngine()) {
					ScriptEngine se = factory.getEngineByName("JavaScript");
					init(se, scriptConfig);
					this.se = se;
				} else {
					se.eval(scriptConfig.getScriptContent());
				}
			}
		} catch (ScriptException e) {
			throw new ScriptOperaterException(e);
		}
	}

	private void init(ScriptEngine se, ScriptConfig scriptConfig) throws ScriptException {
		ScriptContext sc = se.getContext();
		for (ScriptConfig.SContext scontxt : scriptConfig.getSContexts()) {
			// 设置上下文
			sc.setAttribute(scontxt.fieldName, scontxt.fieldValue, scontxt.scope.value());
		}
		se.eval(scriptConfig.getScriptContent());
	}

	@Override
	public Object invokeMethod(String mkey, String name, Object... args) {
		checkAvailable();

		try {
			return ((Invocable) se).invokeMethod(se.get(mkey), name, args);
		} catch (Exception e) {
			throw new ScriptOperaterException(e);
		}
	}

	@Override
	public Object invokeFunction(String name, Object... args) {
		checkAvailable();

		try {
			return ((Invocable) se).invokeFunction(name, args);
		} catch (Exception e) {
			throw new ScriptOperaterException(e);
		}
	}

	@Override
	public <T> T getInterface(Class<T> clasz) {
		checkAvailable();

		return ((Invocable) se).getInterface(clasz);
	}

	@Override
	public <T> T getInterface(String mkey, Class<T> clasz) {
		checkAvailable();

		return ((Invocable) se).getInterface(se.get(mkey), clasz);
	}

	private void checkAvailable() {
		if (!isAvailable) {
			throw new ScriptOperaterException("Engine isn't available! please init!");
		}
	}
}
