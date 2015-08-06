package com.cwa.component.script;

import java.util.LinkedList;
import java.util.List;

/**
 * 脚本配置
 * 
 * @author mausmars
 * 
 */
public class ScriptConfig {
	/**
	 * 是否重建引擎
	 */
	private boolean isBuildEngine;
	/**
	 * 脚本id
	 */
	private String scriptId;
	/**
	 * 脚本内容
	 */
	private String scriptContent;
	/**
	 * 脚本上下文
	 */
	private List<SContext> scriptContexts;

	public ScriptConfig() {
		scriptContexts = new LinkedList<SContext>();
	}

	public static class SContext {
		String fieldName;
		Object fieldValue;
		ContextScopeEnum scope;
	}

	public void addContext(String fieldName, Object fieldValue, ContextScopeEnum scope) {
		SContext sc = new SContext();
		sc.fieldName = fieldName;
		sc.fieldValue = fieldValue;
		sc.scope = scope;
		scriptContexts.add(sc);
	}

	public String getScriptId() {
		return scriptId;
	}

	public void setScriptId(String scriptId) {
		this.scriptId = scriptId;
	}

	public String getScriptContent() {
		return scriptContent;
	}

	public void setScriptContent(String scriptContent) {
		this.scriptContent = scriptContent;
	}

	public List<SContext> getSContexts() {
		return scriptContexts;
	}

	public boolean isBuildEngine() {
		return isBuildEngine;
	}

	public void setBuildEngine(boolean isBuildEngine) {
		this.isBuildEngine = isBuildEngine;
	}

	public List<SContext> getScriptContexts() {
		return scriptContexts;
	}

	public void setScriptContexts(List<SContext> scriptContexts) {
		this.scriptContexts = scriptContexts;
	}
}
