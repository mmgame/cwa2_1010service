package com.cwa.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetUtil {
	protected static final Logger logger = LoggerFactory.getLogger(NetUtil.class);

	public static boolean isPortAvailable(int port) {
		boolean isAvailable = bindPort("0.0.0.0", port);
		if (!isAvailable) {
			return false;
		}
		return true;
	}

	/**
	 * 检查端口是否可用
	 * 
	 * @param ip
	 * @param port
	 * @return
	 */
	public static boolean isPortAvailable(String ip, int port) {
		boolean isAvailable = bindPort(ip, port);
		if (!isAvailable) {
			return false;
		}
		isAvailable = bindPort("0.0.0.0", port);
		if (!isAvailable) {
			return false;
		}
		return true;
	}

	private static boolean bindPort(String ip, int port) {
		Socket s = new Socket();
		try {
			s.bind(new InetSocketAddress(ip, port));
			return true;
		} catch (Exception e) {
			if (logger.isWarnEnabled()) {
				logger.warn("ip:prot isn unavailable! ip=" + ip + " port=" + port);
			}
			return false;
		} finally {
			try {
				s.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 获取外网ip，如果没有用本地ip，再没有返回null
	 * 
	 * @return
	 * @throws SocketException
	 * @throws UnknownHostException
	 */
	public static String getOutsideNetIp() throws Exception {
		return getOutsideNetIp(null);
	}

	public static String getOutsideNetIp(String matchingNetwork) throws Exception {
		Map<Integer, List<NetAddressInfo>> netAddressInfoMap = getNetAddresss();
		List<NetAddressInfo> outsideNetAddressInfos = netAddressInfoMap.get(NetAddressTypeEnum.OutsideAddress.value());
		if (outsideNetAddressInfos != null) {
			return outsideNetAddressInfos.get(0).getIp();
		}
		logger.error("OutsideNetAddresss isn't exist!");
		if (matchingNetwork == null || matchingNetwork.isEmpty()) {
			return InetAddress.getLocalHost().getHostAddress();
		} else {
			return getInsideNetIp(matchingNetwork);
		}
	}

	public static String getInsideNetIp(String matchingNetwork) throws Exception {
		Map<Integer, List<NetAddressInfo>> netAddressInfoMap = getNetAddresss();
		List<NetAddressInfo> localNetAddressInfos = netAddressInfoMap.get(NetAddressTypeEnum.LoacalAddress.value());
		for (NetAddressInfo localNetAddressInfo : localNetAddressInfos) {
			if (localNetAddressInfo.getIp().contains(matchingNetwork)) {
				return localNetAddressInfo.getIp();
			}
		}
		return null;
	}

	public static Map<Integer, List<NetAddressInfo>> getNetAddresss() throws SocketException {
		Map<Integer, List<NetAddressInfo>> netAddressMap = new HashMap<Integer, List<NetAddressInfo>>();

		Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
		while (netInterfaces.hasMoreElements()) {
			NetworkInterface ni = netInterfaces.nextElement();
			NetAddressInfo netAddressInfo = new NetAddressInfo();
			netAddressInfo.init(ni);

			if (netAddressInfo.getNetAddressType() == null) {
				continue;
			}
			int addrType = netAddressInfo.getNetAddressType().value();
			List<NetAddressInfo> netAddressInfoList = netAddressMap.get(addrType);
			if (netAddressInfoList == null) {
				netAddressInfoList = new ArrayList<NetAddressInfo>();
				netAddressMap.put(addrType, netAddressInfoList);
			}
			netAddressInfoList.add(netAddressInfo);
		}
		return netAddressMap;
	}

	public static SystemTypeEnum getSystemType() {
		String osName = System.getProperty("os.name"); // 获取系统名称
		if (osName != null && osName.startsWith("Windows")) {
			return SystemTypeEnum.Windows;
		} else if (osName != null && osName.startsWith("Linux")) {
			return SystemTypeEnum.Linux;
		}
		return SystemTypeEnum.Unknown;
	}

	public static void main(String args[]) {
		DOMConfigurator.configureAndWatch("propertiesconfig/log4j.xml");
		try {

			Map<Integer, List<NetAddressInfo>> netAddressInfoMap = getNetAddresss();
			System.out.println(netAddressInfoMap);

			SystemTypeEnum systemType = getSystemType();
			System.out.println("SystemType " + systemType);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
}
