package com.hongshi.wuliudidi.model;

import java.io.Serializable;

/**
 * @author huiyuan
 * @version 1.0
 * @created 2017/11/29 13:50
 * @title 广告数据类
 * @description 保存广告基本信息
 * @changeRecord：2017/11/29 13:50 modify by
 */
public class BannerModel implements Serializable {
    //广告优先级（目前仅轮播图使用，数字越小代表优先级越高
    private String adLevel;
    //广告图片
    private String adPic;
    //广告图位置
    private String adPosition;
    /**
    * 广告类型：
            1-浮动广告
            2-固定广告
            3-轮播广告
            4-BANNER广告
            5-图标广告
            6-弹出广告
            7-其他*/
    private String adType;
    private String adUrl;
    private String appType;
    private String title;

    public String getAdLevel() {
        return adLevel;
    }

    public void setAdLevel(String adLevel) {
        this.adLevel = adLevel;
    }

    public String getAdPic() {
        return adPic;
    }

    public void setAdPic(String adPic) {
        this.adPic = adPic;
    }

    public String getAdPosition() {
        return adPosition;
    }

    public void setAdPosition(String adPosition) {
        this.adPosition = adPosition;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
