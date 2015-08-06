package com.cwa.component.membermanager;

import serverice.account.AccountStateEnum;

import com.cwa.service.constant.ServiceConstant;

public class MemberUtil {
	public static boolean isOverTime(UserMemberData userMemberData) {
		if (userMemberData.getState() == AccountStateEnum.Online.value()) {
			return false;
		} else if (userMemberData.getState() == AccountStateEnum.Offline.value()) {
			long t = System.currentTimeMillis() - userMemberData.getCreateTime();
			return t >= ServiceConstant.OffLineOverTime;
		}
		return true;
	}
}
