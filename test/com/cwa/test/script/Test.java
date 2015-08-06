package com.cwa.test.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngineManager;

import com.cwa.component.script.IScriptEngineOperater;
import com.cwa.component.script.JSScriptEngineOperater;
import com.cwa.component.script.ScriptConfig;

public class Test {
	private static List<ScriptConfig> createScriptConfigs() {
		String scriptFilePath = "testfile/testscriptfile/scriptinterface";
		File fileDir = new File(scriptFilePath);
		if (!fileDir.isDirectory()) {
			return null;
		}
		List<ScriptConfig> scList = new LinkedList<ScriptConfig>();
		File[] files = fileDir.listFiles();
		for (File file : files) {
			if (!file.getName().contains(".script")) {
				continue;
			}
			try {
				StringBuffer buffer = new StringBuffer();
				BufferedReader reader = new BufferedReader(new FileReader(file));
				for (String str = null; (str = reader.readLine()) != null;) {
					buffer.append(str);
				}
				reader.close();

				String content = buffer.toString();

				ScriptConfig sc = new ScriptConfig();
				String fileName = file.getName();
				sc.setScriptId(fileName.split("\\.")[0]);
				sc.setBuildEngine(false);
				sc.setScriptContent(content);
				// sc.addContext(fieldName, fieldValue, scope);
				scList.add(sc);
				System.out.println(content);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return scList;
	}

	public static void main(String args[]) {
		ScriptEngineManager sem = new ScriptEngineManager();

		Map<String, ITestInterface> interfaceMap = new HashMap<String, ITestInterface>();
		Map<String, IScriptEngineOperater> scriptEngineOperaterMap = new HashMap<String, IScriptEngineOperater>();

		List<ScriptConfig> scList = createScriptConfigs();
		for (ScriptConfig sc : scList) {
			JSScriptEngineOperater se = new JSScriptEngineOperater(sem);
			se.resetConfig(sc);
			ITestInterface testInterface = se.getInterface(ITestInterface.class);
			interfaceMap.put(sc.getScriptId(), testInterface);
			scriptEngineOperaterMap.put(sc.getScriptId(), se);
		}

		// ---接口调用---
		ITestInterface tif1 = interfaceMap.get("execute1");
		ITestInterface tif2 = interfaceMap.get("execute2");

		Item item1 = new Item();
		Role role1 = new Role();
		tif1.execute(role1, item1);
		System.out.println(role1);
		tif2.execute(role1, item1);
		System.out.println(role1);
		// ---方法调用---
		IScriptEngineOperater seo1 = scriptEngineOperaterMap.get("execute1");
		IScriptEngineOperater seo2 = scriptEngineOperaterMap.get("execute2");

		Item item2 = new Item();
		Role role2 = new Role();
		seo1.invokeFunction("execute", new Object[] { role2, item2 });
		System.out.println(role2);
		seo2.invokeFunction("execute", new Object[] { role2, item2 });
		System.out.println(role2);

	}

}
