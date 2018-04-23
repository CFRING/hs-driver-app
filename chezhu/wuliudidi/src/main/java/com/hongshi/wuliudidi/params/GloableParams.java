package com.hongshi.wuliudidi.params;

/**
 * 系统常量参数设置
 */
public class GloableParams {
	// 集成环境测试
	//public static String BASE_HOSE="http://192.168.158.49";
	//优化版1.4.1环境
//	public static String BASE_HOSE="http://192.168.158.189:8080";
//	public static String BASE_HOSE="http://192.168.158.205";
	// 项目调试url
//	public static String BASE_HOSE = "http://192.168.158.204:8080";
//	public static String HOST = BASE_HOSE + "/gwcz/";
//	public static String COMET_HOST = BASE_HOSE + ":8080/gwcz/";
//	public static String WEB_HOSE = "http://cz.redlion56.com";
//	public static String WEB_URL = WEB_HOSE + "/app/";
//	public static String LOGIN="http://192.168.158.204:8080/gwlogin/";

	
	//新APP地址
	//测试环境开关
	public static final boolean IS_DEBUG_OR_TEST = false;

	public static String EVN_HOST = "redlion56.com";
	public static String BASE_HOSE = (IS_DEBUG_OR_TEST ? "http://":"https://") + "cz." + EVN_HOST;
	public static String WEB_URL = "http://cz.redlion56.com" +"/app/";
	public static String HOST= BASE_HOSE + "/gwcz/";
	public static String COMET_HOST = "http://cz.redlion56.com" + ":8080/gwcz/";
	public static String LOGIN = (IS_DEBUG_OR_TEST ? "http://":"https://") + "login." + EVN_HOST +"/gwlogin/";
	public static String DRIVER_HOST = (IS_DEBUG_OR_TEST ? "http://":"https://") + "sj." + EVN_HOST + "/gwsj/";
	public static String CHE_ZHU_HOST = BASE_HOSE + "/gwcz/";
//	public static String DRIVER_HOST = "http://192.168.158.204:8080/gwsj/";
}
