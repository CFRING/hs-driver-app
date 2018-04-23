package com.hongshi.wuliudidi.model;

import java.io.Serializable;

/**
 * 供应商详情VO
 */
public class SupplierUserDetailVO implements Serializable{

    /**
     * 供应商id
     */
    private String            userId;
    /**
     * 备用手机号码
     */
    private String            backNumber;
    /**
     * 邮箱
     */
    private String            email;
    /**
     * 昵称
     */
    private String            nickName;
    /**
     * 企业名称
     */
    private String            enterpriseName;
    /**
     * 企业简称
     */
    private String            abbreviation;
    /**
     * 平台展示名
     */
    private String            showName;
    /**
     * 信用证代码
     */
    private String            lcCode;
    /**
     * 供应商类型
     */
    private String            supplierType;
    /**
     * 供应商层级
     */
    private int               supplierLevel;
    /**
     * 前五级地址
     */
    private String               firstArea;
    /**
     * 是否是门店
     */
    private boolean           isStore;
    /**
     * 详细地址，一般是指区以后的
     */
    private String            detailArea;
    /**
     * 常用地-经度
     */
    private String              addressLng;
    /**
     * 常用地-纬度
     */
    private String              addressLat;
    /**
     * 法人姓名
     */
    private String            legalPerson;
    /**
     * 身份证号码
     */
    private String            identityCode;
    /**
     * 身份证正面照
     */
    private String            frontCardPhoto;

    /**
     * 身份证反面照
     */
    private String            backCardPhoto;
    /**
     * 营业执照照片
     */
    private String            businessPhoto;
    /**
     * 是否资金结算
     */
    private String            fundSettle;
    /**
     * 门店照片
     */
    private String            storePhoto;

    private String cellphone;

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getStorePhoto() {
        return storePhoto;
    }
    public void setStorePhoto(String storePhoto) {
        this.storePhoto = storePhoto;
    }
    public String getFundSettle() {
        return fundSettle;
    }
    public void setFundSettle(String fundSettle) {
        this.fundSettle = fundSettle;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getEmail() {
        return email;
    }
    
    public String getBackNumber() {
        return backNumber;
    }
    public void setBackNumber(String backNumber) {
        this.backNumber = backNumber;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getEnterpriseName() {
        return enterpriseName;
    }
    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }
    public String getAbbreviation() {
        return abbreviation;
    }
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
    public String getShowName() {
        return showName;
    }
    public void setShowName(String showName) {
        this.showName = showName;
    }
    public String getLcCode() {
        return lcCode;
    }
    public void setLcCode(String lcCode) {
        this.lcCode = lcCode;
    }
    public String getSupplierType() {
        return supplierType;
    }
    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }
    public int getSupplierLevel() {
        return supplierLevel;
    }
    public void setSupplierLevel(int supplierLevel) {
        this.supplierLevel = supplierLevel;
    }
    public String getFirstArea() {
        return firstArea;
    }
    public void setFirstArea(String firstArea) {
        this.firstArea = firstArea;
    }
    public boolean isStore() {
        return isStore;
    }
    public void setStore(boolean isStore) {
        this.isStore = isStore;
    }
    public String getDetailArea() {
        return detailArea;
    }
    public void setDetailArea(String detailArea) {
        this.detailArea = detailArea;
    }
    public String getAddressLng() {
        return addressLng;
    }
    public void setAddressLng(String addressLng) {
        this.addressLng = addressLng;
    }
    public String getAddressLat() {
        return addressLat;
    }
    public void setAddressLat(String addressLat) {
        this.addressLat = addressLat;
    }
    public String getLegalPerson() {
        return legalPerson;
    }
    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }
    public String getIdentityCode() {
        return identityCode;
    }
    public void setIdentityCode(String identityCode) {
        this.identityCode = identityCode;
    }
    public String getFrontCardPhoto() {
        return frontCardPhoto;
    }
    public void setFrontCardPhoto(String frontCardPhoto) {
        this.frontCardPhoto = frontCardPhoto;
    }
    public String getBackCardPhoto() {
        return backCardPhoto;
    }
    public void setBackCardPhoto(String backCardPhoto) {
        this.backCardPhoto = backCardPhoto;
    }
    public String getBusinessPhoto() {
        return businessPhoto;
    }
    public void setBusinessPhoto(String businessPhoto) {
        this.businessPhoto = businessPhoto;
    }
    @Override
    public String toString() {
        return "SupplierUserDetailVO [userId=" + userId + ", backNumber=" + backNumber + ", email="
               + email + ", nickName=" + nickName + ", enterpriseName=" + enterpriseName
               + ", abbreviation=" + abbreviation + ", showName=" + showName + ", lcCode=" + lcCode
               + ", supplierType=" + supplierType + ", supplierLevel=" + supplierLevel
               + ", firstArea=" + firstArea + ", isStore=" + isStore + ", detailArea=" + detailArea
               + ", addressLng=" + addressLng + ", addressLat=" + addressLat + ", legalPerson="
               + legalPerson + ", identityCode=" + identityCode + ", frontCardPhoto="
               + frontCardPhoto + ", backCardPhoto=" + backCardPhoto + ", businessPhoto="
               + businessPhoto + ", fundSettle=" + fundSettle + ", storePhoto=" + storePhoto + "]";
    }
    
    
}
