package com.cwa.component.data.operate;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cwa.component.data.IEntityDao;

/**
 * 利用反射调用dao方法
 * 
 * @author Administrator
 * 
 */
public class ReflectDaoOperate implements IDaoOperate {
	protected static final Logger logger = LoggerFactory.getLogger(ReflectDaoOperate.class);

	private IEntityDao entityDao;// dao
	private String methdoName;// 方法名字
	private Object[] params;// 参数
	private Class<?>[] paramTypes;// 参数类型
	private IDaoCallBack daoCallBack;// 回调接口

	@Override
	public Object execute() {
		Object resulte = null;
		try {
			Class<? extends IEntityDao> classType = entityDao.getClass();

			Method method = classType.getMethod(methdoName, paramTypes);
			method.setAccessible(true);
			// 参数
			resulte = method.invoke(entityDao, params); // 自动装箱
			if (daoCallBack != null) {
				// 回调接口
				daoCallBack.executeOver(resulte);
			}
			return resulte;
		} catch (Exception e) {
			logger.error("", e);
			if (daoCallBack != null) {
				// 回调接口
				daoCallBack.excuteException(e);
			}
		}
		return resulte;
	}

	@Override
	public void run() {
		execute();
	}

	@Override
	public String getKey() {
		return null;
	}

	@Override
	public boolean canMerge() {
		return false;
	}

	@Override
	public void merge(IDaoOperate daoOperate) {
	}

	@Override
	public boolean canExecute() {
		return true;
	}

	// -------------------------------------------
	public void setEntityDao(IEntityDao entityDao) {
		this.entityDao = entityDao;
	}

	public void setMethdoName(String methdoName) {
		this.methdoName = methdoName;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public void setDaoCallBack(IDaoCallBack daoCallBack) {
		this.daoCallBack = daoCallBack;
	}

	public void setParamTypes(Class<?>[] paramTypes) {
		this.paramTypes = paramTypes;
	}
}
