package com.hongshi.wuliudidi.incomebook;

import java.io.Serializable;

/**
 * Created by huiyuan on 2016/8/17.
 */
public class BillSheetDetailModel implements Serializable{
    private String corporation;//公司
    private String danJu;//单据号
    private double deductAmount;//扣款，金钱
    private double dj;//单价，金钱
    private String goodsCategory;//货物类别编号
    private String goodsCategoryText;//货物类别名称(界面显示)
    private double lossWeight;//扣吨，数量
    private String mine;//提货地
    private String moneyUnitText;//金钱显示单位
    private String outBizDate;//日期
    private String outBizId;//外部业务id
    private double totalAmount;//金额，金钱
    private double weight;//数量
    private String WeightUnitText;//数量显示单位

    public String getCorporation() {
        return corporation;
    }

    public void setCorporation(String corporation) {
        this.corporation = corporation;
    }

    public String getDanJu() {
        return danJu;
    }

    public void setDanJu(String danJu) {
        this.danJu = danJu;
    }

    public double getDeductAmount() {
        return deductAmount;
    }

    public void setDeductAmount(double deductAmount) {
        this.deductAmount = deductAmount;
    }

    public double getDj() {
        return dj;
    }

    public void setDj(double dj) {
        this.dj = dj;
    }

    public String getGoodsCategory() {
        return goodsCategory;
    }

    public void setGoodsCategory(String goodsCategory) {
        this.goodsCategory = goodsCategory;
    }

    public String getGoodsCategoryText() {
        return goodsCategoryText;
    }

    public void setGoodsCategoryText(String goodsCategoryText) {
        this.goodsCategoryText = goodsCategoryText;
    }

    public double getLossWeight() {
        return lossWeight;
    }

    public void setLossWeight(double lossWeight) {
        this.lossWeight = lossWeight;
    }

    public String getMine() {
        return mine;
    }

    public void setMine(String mine) {
        this.mine = mine;
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

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
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
