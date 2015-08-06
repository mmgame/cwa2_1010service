package com.cwa.component.data.operate;

/**
 * dao回调
 * 
 * @author mausmars
 *
 */
public interface IDaoCallBack {
	void executeOver(Object result);

	void excuteException(Exception e);
}
