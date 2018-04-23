package com.hongshi.wuliudidi.model;

import java.util.Date;

/**
 * Created by bian on 2016/7/18 15:01.
 * 供应端记录详情界面样式
 */
public class OneTruckHistoryModel {

    /**
     * 磅单号//单据号
     */
    private String billCode;
    /**
     * 货物分类
     */
    private String category;
    /**
     * 货物名称
     */
    private String categoryText;

    public String getCategoryText() {
        return categoryText;
    }

    public void setCategoryText(String categoryText) {
        this.categoryText = categoryText;
    }

    /**
     * 空车重量
     */
    private double emptyWeight;
    /**
     * 发货地
     */
    private String fromAddr;
    /**
     * 重车重量//毛重
     */
    private String fullWeight;
    /**
     * 创建时间
     */
    private Date gmtCreate;
    /**
     * 修改时间
     */
    private Date  gmtModified;
    /**
     * 过磅时间//要显示的时间
     */
    private Date   gmtWeigh;
    /**
     * 平台货物类型
     */
    private int goodsType;
    /**
     * 平台货物类型名称
     */
    private String goodsTypeText;

    /**
     * 主键：同NC磅单表主键
     */
    private String  id;
    /**
     * 扣杂
     */
    private String   impurityWeight;
    /**
     * 损耗
     */
    private String   lossWeight;
    /**
     * 净重
     */
    private String  netWeight;
    /**
     * 操作人
     */
    private String   operatorId;
    /**
     * 外键，N:1关联poundbill_error表
     */
    private String   pbErrorId;
    /**
     * 发货公司
     */
    private String  pkCorp;
    /**
     * 公司名称
     */
    private String   pkCorpName;
    /**
     * 状态：1待处理 2自动配对 3人工配对 4待确认异常磅单 5已确认异常磅单
     */
    private int  status;
    /**
     * 状态
     */
    private String  statusText;
    /**
     * 统计日期
     */
    private String  sumDate;
    /**
     * 供货方
     */
    private String  supplier;
    /**
     * 车牌号
     */
    private String  truckNumber;
    /**
     * 重量单位
     */
    private String  weightText;

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getEmptyWeight() {
        return emptyWeight;
    }

    public void setEmptyWeight(double emptyWeight) {
        this.emptyWeight = emptyWeight;
    }

    public String getFromAddr() {
        return fromAddr;
    }

    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
    }

    public String getFullWeight() {
        return fullWeight;
    }

    public void setFullWeight(String fullWeight) {
        this.fullWeight = fullWeight;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Date getGmtWeigh() {
        return gmtWeigh;
    }

    public void setGmtWeigh(Date gmtWeigh) {
        this.gmtWeigh = gmtWeigh;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsTypeText() {
        return goodsTypeText;
    }

    public void setGoodsTypeText(String goodsTypeText) {
        this.goodsTypeText = goodsTypeText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImpurityWeight() {
        return impurityWeight;
    }

    public void setImpurityWeight(String impurityWeight) {
        this.impurityWeight = impurityWeight;
    }

    public String getLossWeight() {
        return lossWeight;
    }

    public void setLossWeight(String lossWeight) {
        this.lossWeight = lossWeight;
    }

    public String getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(String netWeight) {
        this.netWeight = netWeight;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getPbErrorId() {
        return pbErrorId;
    }

    public void setPbErrorId(String pbErrorId) {
        this.pbErrorId = pbErrorId;
    }

    public String getPkCorp() {
        return pkCorp;
    }

    public void setPkCorp(String pkCorp) {
        this.pkCorp = pkCorp;
    }

    public String getPkCorpName() {
        return pkCorpName;
    }

    public void setPkCorpName(String pkCorpName) {
        this.pkCorpName = pkCorpName;
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

    public String getSumDate() {
        return sumDate;
    }

    public void setSumDate(String sumDate) {
        this.sumDate = sumDate;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }

    public String getWeightText() {
        return weightText;
    }

    public void setWeightText(String weightText) {
        this.weightText = weightText;
    }
}
