package com.cwa.service.config;

import baseice.service.FunctionId;

/**
 * 功能id配置
 * 
 * @author mausmars
 * 
 */
public class CFunctionId implements IConfigChanger<FunctionId> {
	private int rid; // 区id
	private int ftype; // 【功能模块】类型
	private int fkey; // 【功能模块】区别同类的key

	public FunctionId change() {
		FunctionId iceObj = new FunctionId();
		iceObj.gid = rid;
		iceObj.ftype = ftype;
		iceObj.fkey = fkey;
		return iceObj;
	}

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public int getFtype() {
		return ftype;
	}

	public void setFtype(int ftype) {
		this.ftype = ftype;
	}

	public int getFkey() {
		return fkey;
	}

	public void setFkey(int fkey) {
		this.fkey = fkey;
	}
}
