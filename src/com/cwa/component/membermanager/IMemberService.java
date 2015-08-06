package com.cwa.component.membermanager;

import com.cwa.service.IService;

public interface IMemberService extends IService {
	/**
	 * 插入节点
	 * 
	 * @param key
	 * @param values
	 */
	void insertMemberData(IMemberData memberData);

	/**
	 * 删除节点
	 * 
	 * @param key
	 * @param values
	 */
	void removeMemberData(int mtype, long key);

	/**
	 * 查询节点
	 * 
	 * @param key
	 * @param values
	 */
	IMemberData selectMemberData(int mtype, long key);
}
