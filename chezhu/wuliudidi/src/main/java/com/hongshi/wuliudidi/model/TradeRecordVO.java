package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by huiyuan on 2017/6/19.
 */

public class TradeRecordVO implements Serializable {
    //交易金额,元
    private String money;
    //收支类型:1.收入 2.支出
    private int recordType;
    private String recordTypeText;
    //状态
    private String status;
    //交易名称
    private String tradeName;
    //交易时间
    private Date tradeTime;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getRecordType() {
        return recordType;
    }

    public void setRecordType(int recordType) {
        this.recordType = recordType;
    }

    public String getRecordTypeText() {
        return recordTypeText;
    }

    public void setRecordTypeText(String recordTypeText) {
        this.recordTypeText = recordTypeText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }
}
