package com.cwa.component.functionmanage.node.nenum;

/**
 * 节点层类型枚举
 * 
 * @author mausmars
 * 
 */
public enum NodeLevelTypeEnum {
	Root(0), // 根
	Module(1), // 模块（sms，lock）
	Region(2), // 区
	FType(3), // 功能类型
	FKey(4), // 功能key
	;
	private int value;

	NodeLevelTypeEnum(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}
}
