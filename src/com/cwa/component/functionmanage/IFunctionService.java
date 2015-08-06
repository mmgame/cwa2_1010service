package com.cwa.component.functionmanage;

import Ice.ObjectPrx;
import baseice.service.FunctionId;
import baseice.service.FunctionMenu;
import baseice.service.FunctionTypeEnum;

import com.cwa.component.functionmanage.node.AFunctionNode;
import com.cwa.service.IService;

/**
 * 功能管理接口
 * 
 * @author mausmars
 * 
 */
public interface IFunctionService extends IService {
	void unregister(FunctionId fid);

	AFunctionNode register(FunctionMenu functionMenu);

	void createZKPrx();

	/**
	 * 获得主功能服务器服务接口
	 * 
	 * @param rid
	 * @param ftype
	 * @return
	 */
	IFunctionCluster getFunctionCluster(int gid, FunctionTypeEnum ftype);

	/**
	 * 
	 * @param gid
	 * @param functionType
	 * @param cls
	 * @return
	 */
	<T extends ObjectPrx> T getIcePrx(int gid, FunctionTypeEnum functionType, Class<T> cls);
}
