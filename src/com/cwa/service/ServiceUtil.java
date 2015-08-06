package com.cwa.service;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cwa.service.cloader.ServerClassLoader;

import Ice.ObjectPrx;
import Ice.ObjectPrxHelperBase;
import baseice.service.FunctionMenu;
import baseice.service.ServiceInfo;

public class ServiceUtil {
	protected static final Logger logger = LoggerFactory.getLogger(ServiceUtil.class);

	protected static final String Cast = "uncheckedCast";
	protected static final String PrxHelperPostfix = "PrxHelper";
	public static final String PrxPostfix = "Prx";

	/**
	 * 由于网络传输会将ObjectPrx直接出过来ObjectPrx，这里做对用Prx的转换，方便客户端的使用
	 * 
	 * @param serviceInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ObjectPrx getServicePrx(ServiceInfo serviceInfo) {
		String className = serviceInfo.packageName + serviceInfo.interfcName + PrxHelperPostfix;
		try {
			Class<ObjectPrxHelperBase> caster = (Class<ObjectPrxHelperBase>) ServerClassLoader.getClass(className);
			if (caster == null) {
				return null;
			}
			Method method = caster.getMethod(Cast, Ice.ObjectPrx.class);// checkedCast
			Ice.ObjectPrx prx = (Ice.ObjectPrx) method.invoke(null, serviceInfo.server);
			if (prx == null) {
				logger.error("ObjectPrx prx is null! className=" + className);
				return null;
			}
			return prx;
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
		}
	}

	public static void functionMenuPrint(String message, FunctionMenu functionMenu) {
		StringBuilder sb = new StringBuilder();
		sb.append(message);
		sb.append(" FunctionMenu [");
		sb.append("ip=" + functionMenu.fa.ip + " port=" + functionMenu.fa.port);
		sb.append("] [");
		sb.append("gid=" + functionMenu.fid.gid + " ftype=" + functionMenu.fid.ftype + " fkey=" + functionMenu.fid.fkey);
		sb.append("] services[");
		for (ServiceInfo serviceInfo : functionMenu.serviceInfos.values()) {
			sb.append("(");
			sb.append("InterfaceName=" + serviceInfo.packageName + serviceInfo.interfcName + " prx=" + serviceInfo.server);
			sb.append(") ");
		}
		sb.append("]");
		if (logger.isInfoEnabled()) {
			logger.info(sb.toString());
		}
	}
}
