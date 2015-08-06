package com.cwa.component.membermanager;

import java.util.List;

import com.cwa.component.bean.DataBean.UserInfoData;

/**
 * 用户成员信息
 * 
 * @author mausmars
 *
 */
public class UserMemberData implements IMemberData {
	private UserInfoData.Builder builder;

	public UserMemberData() {
		builder = UserInfoData.newBuilder();
	}

	@Override
	public long getKey() {
		return builder.getUserId();
	}

	@Override
	public int getMemberType() {
		return MemberTypeEnum.MT_User.value();
	}

	@Override
	public void initData(byte[] data) throws Exception {
		builder.mergeFrom(data);
	}

	@Override
	public byte[] getData() {
		return builder.build().toByteArray();
	}

	public long getUserId() {
		return builder.getUserId();
	}

	public void setUserId(long userId) {
		builder.setUserId(userId);
	}

	public long getCreateTime() {
		return builder.getCreateTime();
	}

	public void setCreateTime(long createTime) {
		builder.setCreateTime(createTime);
	}

	public int getState() {
		return builder.getState();
	}

	public void setState(int state) {
		builder.setState(state);
	}

	public int getGroupId() {
		return builder.getGroupId();
	}

	public void setGroupId(int groupId) {
		builder.setGroupId(groupId);
	}

	public List<Integer> getServerIds() {
		return builder.getServerIdsList();
	}

	public void addServerIds(int serverId) {
		builder.addServerIds(serverId);
	}

	public int getRid() {
		return builder.getRid();
	}

	public void setRid(int rid) {
		builder.setRid(rid);
	}

	public static void main(String args[]) throws Exception {
		UserMemberData test = new UserMemberData();
		test.setUserId(111);
		test.setState(11);
		test.setGroupId(11);
		test.setCreateTime(11);
		byte[] data = test.getData();

		UserMemberData test2 = new UserMemberData();
		test2.initData(data);
		System.out.println(test2.getUserId());
		test2.setUserId(222);
		System.out.println(test2.getUserId());
	}
}
