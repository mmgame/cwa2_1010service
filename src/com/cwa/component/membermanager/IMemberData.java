package com.cwa.component.membermanager;

public interface IMemberData {
	long getKey();

	int getMemberType();

	// -------------------
	byte[] getData();

	void initData(byte[] data) throws Exception;

}
