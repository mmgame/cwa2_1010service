package com.cwa.entity.service;

import com.cwa.entity.ITestUserEntityDao;

public class EntityDaoService {


	private ITestUserEntityDao testUserEntityDao;

	public ITestUserEntityDao getTestUserEntityDao() {
	     return testUserEntityDao;
	}
	
	
	//-----------------------------------------------------
	public void setTestUserEntityDao(ITestUserEntityDao testUserEntityDao) {
		 this.testUserEntityDao = testUserEntityDao;
	}
	
}