package com.hongshi.wuliudidi.model;

import java.io.Serializable;

/**
 * Created by huiyuna on 2017/9/16.
 */

public class SupplierTradeRecordItemModel implements Serializable {
    private String id;
    private String money;
    private String moneyStr;
    private String payDateTime;
    private String payTime;
    private int payType;
    private String payeeUid;
    private String payerUid;
    private String productName;
    private String remark;
    private int status;
    private String tradeId;
    private int tradeType;
    private String supplierShowNickName;
    private String storePhoto;
    private String createDate;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getSupplierShowNickName() {
        return supplierShowNickName;
    }

    public void setSupplierShowNickName(String supplierShowNickName) {
        this.supplierShowNickName = supplierShowNickName;
    }

    public String getStorePhoto() {
        return storePhoto;
    }

    public void setStorePhoto(String storePhoto) {
        this.storePhoto = storePhoto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMoneyStr() {
        return moneyStr;
    }

    public void setMoneyStr(String moneyStr) {
        this.moneyStr = moneyStr;
    }

    public String getPayDateTime() {
        return payDateTime;
    }

    public void setPayDateTime(String payDateTime) {
        this.payDateTime = payDateTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getPayeeUid() {
        return payeeUid;
    }

    public void setPayeeUid(String payeeUid) {
        this.payeeUid = payeeUid;
    }

    public String getPayerUid() {
        return payerUid;
    }

    public void setPayerUid(String payerUid) {
        this.payerUid = payerUid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }
}
