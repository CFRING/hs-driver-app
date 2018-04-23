package com.hongshi.wuliudidi.plugin.bean;

/**
 * Created by huiyuan on 2016/4/22.
 */
public class PluginParams {

    //插件配置信息地址
    public static final String CHECK_PLUGIN_UPDATE_URL = "http://cz.redlion56.com/gwcz/appUpdate?appName=cz&appType=1";//线上
//    public static final String CHECK_PLUGIN_UPDATE_URL = "http://192.168.158.204:8080/gwthirdpt/appUpdate?";//204环境
    //apk类型，安卓车主为“1”
    public static final String APPTYPE = "1";

    //插件名称
    public static final String CHENGYUNTONG = "cz";
    public static final String MY_CASH = "cash";
    public static final String INCOME_BOOK = "incomebook";
    public static final String MY_ROUTE = "myroute";

    //跳转Action
    public static final String MY_CASH_ACTION = "com.hongshi.wuliudidi.cashier.activity.TiXianActivity";//收银台
    public static final String INCOME_BOOK_ACTION = "com.hongshi.wuliudidi.incomebook.activity.IncomeActivity";//收入账本action
    public static final String MY_ROUTE_ACTION = "com.hongshi.wuliudidi.MyRouteActivity";

    //插件包名
    public static final String INCOME_BOOK_PKG_NAME = "com.hongshi.wuliudidi.incomebook";//收入账本
    public static final String MY_CASH_PKG_NAME = "com.hongshi.wuliudidi.cashier";//收银台
    public static final String MY_ROUTE_PKG_NAME = "com.hongshi.wuliudidi.myroute";//我的路线

}
