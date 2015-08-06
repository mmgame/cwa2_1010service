package com.cwa.entity;

import java.util.List;

import com.cwa.component.data.IEntityDao;
import com.cwa.entity.domain.TestUserEntity;

public interface ITestUserEntityDao extends IEntityDao<TestUserEntity> {
	public List<TestUserEntity> selectEntityByUserId(long userId,Object attach);
}
