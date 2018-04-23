package com.hongshi.wuliudidi.model;

/**
 * Created by he on 2016/6/27.
 */
public class DeviceInfoModel {
    private String userName;
    private String userId;
    private String sessionId;
    //应用市场
    private String publisher;
    //平台 android
    private String platform;
    //手机版本
    private String version;
    //app版本
    private String appVer;
    //手机型号
    private String ua ;
    //屏幕分辨率
    private String screen;
    //当前手机网络类型
    private String net;
    private String localIp;
    private String remoteIp;
    private String mac;
    private String  imei;
    //运营商
    private String provider;
    //参数不能为空 调用接口类型：1，打开app就调用，2，登陆后调用
    private String type;
    //客户端对象标识符,app启动“init”，方法内部调用的用方法名“function”，点击按钮或者其他对象触发的用对象名字“tableView1”
    private String appSpm;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppVer() {
        return appVer;
    }

    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAppSpm() {
        return appSpm;
    }

    public void setAppSpm(String appSpm) {
        this.appSpm = appSpm;
    }
}
