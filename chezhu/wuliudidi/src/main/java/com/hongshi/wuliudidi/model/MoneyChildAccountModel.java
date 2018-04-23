package com.hongshi.wuliudidi.model;

import java.util.Date;

/**
 * @author David
 *  账户金额
 */
public class MoneyChildAccountModel {
	/**
     * 账户id，主键
     */
    private String            acctId;

    /**
     * 账户金额
     */
    private double            amount;

    /**
     * 
     */
    private String            amountId;

    /**
     * 金额类型枚举:1.可用 2.冻结
     */
    private int               amountType;

    private String            amountTypeText;

    /**
     * 创建时间
     */
    private Date              gmtCreate;

    /**
     * 货币单位
     */
    private String            moneyEnumCode;

    /**
     * 用户ID
     */
    private String            userId;

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAmountId() {
        return amountId;
    }

    public void setAmountId(String amountId) {
        this.amountId = amountId;
    }

    public int getAmountType() {
        return amountType;
    }

    public void setAmountType(int amountType) {
        this.amountType = amountType;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getMoneyEnumCode() {
        return moneyEnumCode;
    }

    public void setMoneyEnumCode(String moneyEnumCode) {
        this.moneyEnumCode = moneyEnumCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAmountTypeText() {
        return amountTypeText;
    }

    public void setAmountTypeText(String amountTypeText) {
        this.amountTypeText = amountTypeText;
    }
}
