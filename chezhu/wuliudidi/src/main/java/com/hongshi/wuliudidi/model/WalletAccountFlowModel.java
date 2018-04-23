package com.hongshi.wuliudidi.model;

import java.util.Date;

/**
 * Created by Administrator on 2016/4/15.
 */
public class WalletAccountFlowModel {
    private String dispTxt;
//    显示的文本

    private Date gmtCreate;
//    创建时间

    private Date gmtModified;
//    更改时间

    private String id;
//    ID

    private double money;
//    金钱

    private String moneyUnitText;

    private String pricipalUid;
//    主体的用户ID

//    private BaseProductVO productInfo; 此字段1.4.6版本暂时用不到 TODO
////    产品交易信息

    private String status;
//    状态

    private String tradeId;
//    交易号

    private String tradeProductCode;
//    产品类型

    private int recordType;
//    1.收入。2支出


    public String getDispTxt() {
        return dispTxt;
    }

    public void setDispTxt(String dispTxt) {
        this.dispTxt = dispTxt;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getMoneyUnitText() {
        return moneyUnitText;
    }

    public void setMoneyUnitText(String moneyUnitText) {
        this.moneyUnitText = moneyUnitText;
    }

    public String getPricipalUid() {
        return pricipalUid;
    }

    public void setPricipalUid(String pricipalUid) {
        this.pricipalUid = pricipalUid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getTradeProductCode() {
        return tradeProductCode;
    }

    public void setTradeProductCode(String tradeProductCode) {
        this.tradeProductCode = tradeProductCode;
    }

    public int getRecordType() {
        return recordType;
    }

    public void setRecordType(int recordType) {
        this.recordType = recordType;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
