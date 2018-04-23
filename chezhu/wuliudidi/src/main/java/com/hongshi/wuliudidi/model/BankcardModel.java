package com.hongshi.wuliudidi.model;

import android.os.Parcel;

import java.io.Serializable;

/**
 * Created by abc on 2016/4/11.
 */
public class BankcardModel implements Serializable{
     //银行卡所属银行名称
    private String bankName;
    //银行卡号
    private String bankNumber;
    //银行类型 1.中国银行 2.工商银行 3.招商银行 4.建设银行 5.农业银行 6.浦发银行
    // 7.兴业银行 8.广发银行 9.民生银行 10.中信银行 11.杭州银行 12.光大银行 13.上海银行
    // 14.宁波银行 15.平安银行 16.交通银行 17.华夏银行 18.邮储银行
    private int bankType;
    //银行卡id
    private String id;
    //持卡人id
    private String userId;

    private String bankCardType;
    //登记手机号码
    private java.lang.String cellphone;
    //开户行
    private java.lang.String openBankName;

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getOpenBankName() {
        return openBankName;
    }

    public void setOpenBankName(String openBankName) {
        this.openBankName = openBankName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBankType() {
        return bankType;
    }

    public void setBankType(int bankType) {
        this.bankType = bankType;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getBankCardType() {
        return bankCardType;
    }

    public void setBankCardType(String bankCardType) {
        this.bankCardType = bankCardType;
    }
}
