package com.cwa.service.constant;

public class ServiceConstant {
	public static int General_Gid = 0;// 公共组id
	public static int General_Rid = 0;// 公共区id，整个组公用的
	
	public static String FunctionMenuKey = "FunctionMenu";
	public static String AdapterMapKey = "AdapterMap";

	public static String NettyServerClientKey = "NettyServerClient";
	public static String MinaServerClientKey = "MinaServerClient";
	public static String ProtoclientKey = "Protoclient";
	public static String IceServerKey = "IceServer";
	public static String HttpServerKey = "HttpServer";
	public static String DatabaseKey = "Database";
	public static String NetDataTimeoutKey = "NetDataTimeout";
	public static String DataTimeoutKey = "DataTimeout";
	
	public static int OffLineOverTime = 5 * 60 * 1000;// 下线失效时间
}
