package com.cwa.component.functionmanage.node.filter;

/**
 * 节点筛选枚举
 * 
 * @author tzy
 * 
 */
public enum FunctionNodeFilterTypeEnum {
	All("-1"), // 全部节点
	Other("-2"),//初公共区外所有节点
	Null("-3"),//不获取任何节点
	Public("0"), // 公共区节点

	;
	private String value;

	FunctionNodeFilterTypeEnum(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}
}
