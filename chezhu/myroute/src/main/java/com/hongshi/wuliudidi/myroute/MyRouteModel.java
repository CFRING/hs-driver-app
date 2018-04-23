package com.hongshi.wuliudidi.myroute;

/**
 * Created by huiyuan on 2016/6/20.
 */
public class MyRouteModel {
    private String id;
    private String goodsTypeName;
    private String recipientCity;
    private String recipientProvince;
    private String recipientDistrict;

    public String getRecipientDistrict() {
        return recipientDistrict;
    }

    public void setRecipientDistrict(String recipientDistrict) {
        this.recipientDistrict = recipientDistrict;
    }

    private String senderCity;
    private String senderDistrict;
    private String senderProvince;

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipientCity() {
        return recipientCity;
    }

    public void setRecipientCity(String recipientCity) {
        this.recipientCity = recipientCity;
    }

    public String getRecipientProvince() {
        return recipientProvince;
    }

    public void setRecipientProvince(String recipientProvince) {
        this.recipientProvince = recipientProvince;
    }


    public String getSenderCity() {
        return senderCity;
    }

    public void setSenderCity(String senderCity) {
        this.senderCity = senderCity;
    }

    public String getSenderDistrict() {
        return senderDistrict;
    }

    public void setSenderDistrict(String senderDistrict) {
        this.senderDistrict = senderDistrict;
    }

    public String getSenderProvince() {
        return senderProvince;
    }

    public void setSenderProvince(String senderProvince) {
        this.senderProvince = senderProvince;
    }
}
