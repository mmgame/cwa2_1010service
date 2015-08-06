package com.cwa.component.membermanager;

public enum MemberTypeEnum {
	MT_User(0), // 用户
	;

	private int value;

	MemberTypeEnum(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}
}
