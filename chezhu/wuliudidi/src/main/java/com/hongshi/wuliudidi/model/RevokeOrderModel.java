package com.hongshi.wuliudidi.model;

import java.io.Serializable;

/**
 * Created by huiyuan on 2017/9/15.
 */

public class RevokeOrderModel implements Serializable {
    private String amount;
    private String gasolineStation;
    private String cardID;
    private String supplier;
    private String truckNum;
    private int supplierType = 1;
    //申请时间
    private String gmtCreate;

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getGasolineStation() {
        return gasolineStation;
    }

    public void setGasolineStation(String gasolineStation) {
        this.gasolineStation = gasolineStation;
    }

    public int getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(int supplierType) {
        this.supplierType = supplierType;
    }

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getTruckNum() {
        return truckNum;
    }

    public void setTruckNum(String truckNum) {
        this.truckNum = truckNum;
    }
}
