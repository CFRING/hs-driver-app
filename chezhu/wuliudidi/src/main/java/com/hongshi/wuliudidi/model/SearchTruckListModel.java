package com.hongshi.wuliudidi.model;

public class SearchTruckListModel {
    private String truckNumber;
    private String truckId;
    //最大载重量
    private double carryCapacity;
    private String carryCapacityUnitText;
    //最大体积
    private double carryVolume;
    private String carryVolumeUnitText;
    private String truckTypeId;
    //车辆类型描述
    private String truckTypeText;
    //车长id
    private String truckLengthId;
    //车长描述（小二填写的）
    private String truckLengthText;
    //车长（枚举的车长）
    private double truckLength;
    //审核状态1，审核中,2，审核未通过，3，审核通过，4，未完善
    private int status;
    //最后传递的参数
    private long gmtModified;
    //是否有保证金
    private boolean hasGuarantyMoneyAuth;
    //车厢
    private String truckCarriage;
    //车厢
    private String truckCarriageText;
    //关联司机人数
    private String bundDriverCount;
    //资料是否齐全
    private boolean dataComplete;
    //车辆接单是否禁止,true表示可以接单
    private boolean acceptOrder;

    public boolean isAcceptOrder() {
        return acceptOrder;
    }

    public void setAcceptOrder(boolean acceptOrder) {
        this.acceptOrder = acceptOrder;
    }

    public boolean isDataComplete() {
        return dataComplete;
    }

    public void setDataComplete(boolean dataComplete) {
        this.dataComplete = dataComplete;
    }

    public String getBundDriverCount() {
        return bundDriverCount;
    }

    public void setBundDriverCount(String bundDriverCount) {
        this.bundDriverCount = bundDriverCount;
    }

    public String getTruckCarriageText() {
        return truckCarriageText;
    }

    public void setTruckCarriageText(String truckCarriageText) {
        this.truckCarriageText = truckCarriageText;
    }

    public String getTruckCarriage() {

        return truckCarriage;
    }

    public void setTruckCarriage(String truckCarriage) {
        this.truckCarriage = truckCarriage;
    }

    public boolean isHasGuarantyMoneyAuth() {
        return hasGuarantyMoneyAuth;
    }

    public void setHasGuarantyMoneyAuth(boolean hasGuarantyMoneyAuth) {
        this.hasGuarantyMoneyAuth = hasGuarantyMoneyAuth;
    }

    public long getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(long gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }

    public String getTruckId() {
        return truckId;
    }

    public void setTruckId(String truckId) {
        this.truckId = truckId;
    }

    public double getCarryCapacity() {
        return carryCapacity;
    }

    public void setCarryCapacity(double carryCapacity) {
        this.carryCapacity = carryCapacity;
    }

    public double getCarryVolume() {
        return carryVolume;
    }

    public void setCarryVolume(double carryVolume) {
        this.carryVolume = carryVolume;
    }

    public String getTruckTypeText() {
        return truckTypeText;
    }

    public void setTruckTypeText(String truckTypeText) {
        this.truckTypeText = truckTypeText;
    }

    public String getTruckLengthText() {
        return truckLengthText;
    }

    public void setTruckLengthText(String truckLengthText) {
        this.truckLengthText = truckLengthText;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCarryCapacityUnitText() {
        return carryCapacityUnitText;
    }

    public void setCarryCapacityUnitText(String carryCapacityUnitText) {
        this.carryCapacityUnitText = carryCapacityUnitText;
    }

    public String getCarryVolumeUnitText() {
        return carryVolumeUnitText;
    }

    public void setCarryVolumeUnitText(String carryVolumeUnitText) {
        this.carryVolumeUnitText = carryVolumeUnitText;
    }

    public String getTruckTypeId() {
        return truckTypeId;
    }

    public void setTruckTypeId(String truckTypeId) {
        this.truckTypeId = truckTypeId;
    }

    public String getTruckLengthId() {
        return truckLengthId;
    }

    public void setTruckLengthId(String truckLengthId) {
        this.truckLengthId = truckLengthId;
    }

    public double getTruckLength() {
        return truckLength;
    }

    public void setTruckLength(double truckLength) {
        this.truckLength = truckLength;
    }

}
