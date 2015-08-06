package com.cwa.component.functionmanage.node.filter;

import java.util.ArrayList;
import java.util.List;

import baseice.constant.SeparatorComma;

/**
 * 节点过滤类
 * 
 * @author tzy
 * 
 * 
 */
public class FunctionNodeFilter implements IFunctionNodeFilter {

	// 所需要的组节点列表
	private String groupFilterStr;
	// 所需要的功能节点列表
	private String functionFilterStr;

	@Override
	public List<String> getRegionFilterList(List<String> childrenNodeStrs) {
		List<String> returnStr = new ArrayList<String>();
		String[] filterStrs = groupFilterStr.split(SeparatorComma.value);
		for (String string : filterStrs) {
			if (string.equals(FunctionNodeFilterTypeEnum.Null.value())) {
				return returnStr;
			} else if (string.equals(FunctionNodeFilterTypeEnum.All.value())) {
				return childrenNodeStrs;
			} else if (string.equals(FunctionNodeFilterTypeEnum.Other.value())) {
				returnStr.addAll(childrenNodeStrs);
				if (childrenNodeStrs.contains(FunctionNodeFilterTypeEnum.Public.value())) {
					returnStr.remove(FunctionNodeFilterTypeEnum.Public.value());
				}
				return returnStr;
			}
			if (childrenNodeStrs.contains(string)) {
				returnStr.add(string);
			}
		}
		return returnStr;
	}

	@Override
	public List<String> getFunctionFilterList(List<String> childrenNodeStrs) {
		List<String> returnStr = new ArrayList<String>();
		String[] filterStrs = functionFilterStr.split(SeparatorComma.value);
		for (String string : filterStrs) {
			if (string.equals(FunctionNodeFilterTypeEnum.Null.value())) {
				return returnStr;
			} else if (string.equals(FunctionNodeFilterTypeEnum.All.value())) {
				return childrenNodeStrs;
			} else if (string.equals(FunctionNodeFilterTypeEnum.Other.value())) {
				returnStr.addAll(childrenNodeStrs);
				if (childrenNodeStrs.contains(FunctionNodeFilterTypeEnum.Public.value())) {
					returnStr.remove(FunctionNodeFilterTypeEnum.Public.value());
				}
				return returnStr;
			}
			if (childrenNodeStrs.contains(string)) {
				returnStr.add(string);
			}
		}
		return returnStr;
	}

	// ------------------------------------

	public void setGroupFilterStr(String groupFilterStr) {
		this.groupFilterStr = groupFilterStr;
	}

	public void setFunctionFilterStr(String functionFilterStr) {
		this.functionFilterStr = functionFilterStr;
	}

}
