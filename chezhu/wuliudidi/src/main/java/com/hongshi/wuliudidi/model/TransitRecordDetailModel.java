package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by huiyuan on 2017/8/23.
 */

public class TransitRecordDetailModel implements Serializable {
    //竞价单ID
    private String auctionId;
    //货品类型
    private String goodsType;
    //运费
    private String money;
    //签收时间
    private Date outBizDate;
    //运量
    private String quantity;
    //收货地址
    private String receiptAddress;
    //发货地址
    private String senderAddress;
    //车辆ID
    private String truckId;
    //车牌号
    private String truckNo;
    //运量单位
    private String weightUnit;
    //结算单位
    private String goodsStationName;


    public String getGoodsStationName() {
        return goodsStationName;
    }

    public void setGoodsStationName(String goodsStationName) {
        this.goodsStationName = goodsStationName;
    }

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Date getOutBizDate() {
        return outBizDate;
    }

    public void setOutBizDate(Date outBizDate) {
        this.outBizDate = outBizDate;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getReceiptAddress() {
        return receiptAddress;
    }

    public void setReceiptAddress(String receiptAddress) {
        this.receiptAddress = receiptAddress;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getTruckId() {
        return truckId;
    }

    public void setTruckId(String truckId) {
        this.truckId = truckId;
    }

    public String getTruckNo() {
        return truckNo;
    }

    public void setTruckNo(String truckNo) {
        this.truckNo = truckNo;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }
}
