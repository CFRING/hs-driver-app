package com.hongshi.wuliudidi.model;

/**
 * Created by bian on 2016/7/14 17:03.
 */
public class AuctionQueryModel {

    /**
     * 经度
     */
    private String lng;
    /**
     * 纬度
     */
    private String lat;
    /**
     * 发货省_市_区ID
     */
    private String senderAreaId;
    /**
     * 接受地省_市_区ID
     */
    private String recipientAreaId;
    /**
     * 当前页
     */
    private int currentPage;
    /**
     * 页面大小
     */
    private int pageSize;
    /**
     * 货源来源，全部的默认为 0.我要竞价为1，红狮叫车为2
     */
    private int fromType;

//    private int goodsType;//货源类型 默认值-1 为全部货源

    //1.9.4 收货公司ID
    private String recipCopId;

    //1.9.4 发货公司ID
    private String senderCopId;

    /**  货源类型 默认值-1 为全部货源    1_2_3 拼接*/
    private String            goodsType;

    /**  1.9.4 搜索货品名 关键字*/
    private String            goodsName;

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getRecipCopId() {
        return recipCopId;
    }

    public void setRecipCopId(String recipCopId) {
        this.recipCopId = recipCopId;
    }

    public String getSenderCopId() {
        return senderCopId;
    }

    public void setSenderCopId(String senderCopId) {
        this.senderCopId = senderCopId;
    }


    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getSenderAreaId() {
        return senderAreaId;
    }

    public void setSenderAreaId(String senderAreaId) {
        this.senderAreaId = senderAreaId;
    }

    public String getRecipientAreaId() {
        return recipientAreaId;
    }

    public void setRecipientAreaId(String recipientAreaId) {
        this.recipientAreaId = recipientAreaId;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getFromType() {
        return fromType;
    }

    public void setFromType(int fromType) {
        this.fromType = fromType;
    }
}
