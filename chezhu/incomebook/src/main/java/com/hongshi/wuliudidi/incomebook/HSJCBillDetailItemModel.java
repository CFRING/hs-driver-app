package com.hongshi.wuliudidi.incomebook;

import java.io.Serializable;

/**
 * Created by huiyuan on 2016/8/17.
 */
public class HSJCBillDetailItemModel implements Serializable{
    private String custName;//经销商名称
    private double dj;//单价，金钱
    private String goodsCategoryText;//货物类别名称(界面显示)
    private String moneyUnitText;//金钱显示单位
    private String outBizDate;//交易时间
    private String outBizId;//外部业务id
    private String recipientAddress;//目的地
    private String senderAddress;//发货地
    private double totalAmount;//运费总额，金钱
    private String truckId;//车牌id
    private String truckNumber;//车牌号码
    private double weight;//运量
    private String WeightUnitText;//运量显示单位

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public double getDj() {
        return dj;
    }

    public void setDj(double dj) {
        this.dj = dj;
    }

    public String getGoodsCategoryText() {
        return goodsCategoryText;
    }

    public void setGoodsCategoryText(String goodsCategoryText) {
        this.goodsCategoryText = goodsCategoryText;
    }

    public String getMoneyUnitText() {
        return moneyUnitText;
    }

    public void setMoneyUnitText(String moneyUnitText) {
        this.moneyUnitText = moneyUnitText;
    }

    public String getOutBizDate() {
        return outBizDate;
    }

    public void setOutBizDate(String outBizDate) {
        this.outBizDate = outBizDate;
    }

    public String getOutBizId() {
        return outBizId;
    }

    public void setOutBizId(String outBizId) {
        this.outBizId = outBizId;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTruckId() {
        return truckId;
    }

    public void setTruckId(String truckId) {
        this.truckId = truckId;
    }

    public String getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getWeightUnitText() {
        return WeightUnitText;
    }

    public void setWeightUnitText(String weightUnitText) {
        WeightUnitText = weightUnitText;
    }
}
