package com.hongshi.wuliudidi.model;

import java.io.Serializable;

/**
 * Created by huiyuan on 2017/3/28.
 */

public class TruckAuthAppVO extends TruckAuthModel implements Serializable {
    //未通过原因
    private java.lang.String auditNoteTypeText;
    //1.9.4 是否被分配到当前司机.
    private boolean bundWithCurrDriver;
    //是否自卸车
    private boolean isDump;
    private java.lang.String isDumpText;
    private static long serialVersionUID;
    //总体的审核状态
    private int status;
    private java.lang.String statusText;
    //车牌号码后面的数字字母
    private java.lang.String truckNumberLetter;
    //农用车的中间字段
    private java.lang.String truckNumberMidd;
    //车牌号码前面字段 特别注意农用车的车牌字段分为三段，其他车辆两个字段
    private java.lang.String truckNumberProvince;

    public String getAuditNoteTypeText() {
        return auditNoteTypeText;
    }

    public void setAuditNoteTypeText(String auditNoteTypeText) {
        this.auditNoteTypeText = auditNoteTypeText;
    }

    public boolean isBundWithCurrDriver() {
        return bundWithCurrDriver;
    }

    public void setBundWithCurrDriver(boolean bundWithCurrDriver) {
        this.bundWithCurrDriver = bundWithCurrDriver;
    }

    public boolean isDump() {
        return isDump;
    }

    public void setDump(boolean dump) {
        isDump = dump;
    }

    public String getIsDumpText() {
        return isDumpText;
    }

    public void setIsDumpText(String isDumpText) {
        this.isDumpText = isDumpText;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static void setSerialVersionUID(long serialVersionUID) {
        TruckAuthAppVO.serialVersionUID = serialVersionUID;
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

    public String getTruckNumberLetter() {
        return truckNumberLetter;
    }

    public void setTruckNumberLetter(String truckNumberLetter) {
        this.truckNumberLetter = truckNumberLetter;
    }

    public String getTruckNumberMidd() {
        return truckNumberMidd;
    }

    public void setTruckNumberMidd(String truckNumberMidd) {
        this.truckNumberMidd = truckNumberMidd;
    }

    public String getTruckNumberProvince() {
        return truckNumberProvince;
    }

    public void setTruckNumberProvince(String truckNumberProvince) {
        this.truckNumberProvince = truckNumberProvince;
    }
}
