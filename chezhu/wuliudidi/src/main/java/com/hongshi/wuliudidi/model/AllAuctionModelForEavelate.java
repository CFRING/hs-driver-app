package com.hongshi.wuliudidi.model;

import java.io.Serializable;

/**
 * Created by huiyuan on 2017/6/28.
 */

public class AllAuctionModelForEavelate implements Serializable {
    private static final long serialVersionUID = -213946700959100347L;

    //竞价条目ID
    private String            bidItemId;

    //状态：1未进行，2进行中
    private int               status;
    //状态：1未进行，2进行中
    private String            statusText;

    //发货方地址
    private String            sendAddr;

    //收货方地址
    private String            recvAddr;

    //货物名
    private String            goodsName;

    //货物量
    private double            goodsAmount;

    //货物量单位
    private int               unit;
    private String            unitText;
    //竞价/派车单位文本：车/吨/立方米/件
    private String            assignUnitText;
    //结算单位text：'T':'吨','M3':'立方米','PIECE':'件'
    private String            settleUnitText;

    //    中标价
    private double              bidPrice;
    //竞价单ID
    private String auctionId;
    //是否被评论过
    private boolean isJudged;
    //车牌号
    private String truckNo;
    //评价员ID
    private String optId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOptId() {
        return optId;
    }

    public void setOptId(String optId) {
        this.optId = optId;
    }

    public String getTruckNo() {
        return truckNo;
    }

    public void setTruckNo(String truckNo) {
        this.truckNo = truckNo;
    }

    public boolean getIsJudged() {
        return isJudged;
    }

    public void setIsJudged(boolean judged) {
        isJudged = judged;
    }

    public String getAssignUnitText() {
        return assignUnitText;
    }

    public void setAssignUnitText(String assignUnitText) {
        this.assignUnitText = assignUnitText;
    }

    public String getSettleUnitText() {
        return settleUnitText;
    }

    public void setSettleUnitText(String settleUnitText) {
        this.settleUnitText = settleUnitText;
    }
    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public String getBidItemId() {
        return bidItemId;
    }

    public void setBidItemId(String bidItemId) {
        this.bidItemId = bidItemId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getSendAddr() {
        return sendAddr;
    }

    public void setSendAddr(String sendAddr) {
        this.sendAddr = sendAddr;
    }

    public String getRecvAddr() {
        return recvAddr;
    }

    public void setRecvAddr(String recvAddr) {
        this.recvAddr = recvAddr;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public double getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(double goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getUnitText() {
        return unitText;
    }

    public void setUnitText(String unitText) {
        this.unitText = unitText;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }
}
