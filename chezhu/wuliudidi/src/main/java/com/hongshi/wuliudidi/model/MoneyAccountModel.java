package com.hongshi.wuliudidi.model;

import java.util.Date;
import java.util.List;

/**
 * @author David
 * carrier 的资金账户信息
 *
 */
public class MoneyAccountModel {
    /**
     * 账户ID
     */
    private String            acctId;
    /**
     * 账户状态
     */
    private int               acctStatus;
    /**
     * 账户状态
     */
    private String            acctStatusText;
    /**
     * 账户类型
     */
    private int               acctType;
    /**
     * 账户类型
     */
    private String            acctTypeText;
    /**
     * 货币单位
     */
    private String            currencyEnumCode;
    /**
     * 货币代码
     */
    private String            currencyEnumText;
    /**
     * 创建时间
     */
    private Date              gmtCreate;
    /**
     * 修改时间
     */
    private Date              gmtModified;
    /**
     * 资金平台的用户ID
     */
    private String            userId;

    /**
     * 金额VO
     */
    private List<MoneyChildAccountModel>             amountVOList;

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public int getAcctStatus() {
        return acctStatus;
    }

    public void setAcctStatus(int acctStatus) {
        this.acctStatus = acctStatus;
    }

    public String getAcctStatusText() {
        return acctStatusText;
    }

    public void setAcctStatusText(String acctStatusText) {
        this.acctStatusText = acctStatusText;
    }

    public int getAcctType() {
        return acctType;
    }

    public void setAcctType(int acctType) {
        this.acctType = acctType;
    }

    public String getAcctTypeText() {
        return acctTypeText;
    }

    public void setAcctTypeText(String acctTypeText) {
        this.acctTypeText = acctTypeText;
    }

    public String getCurrencyEnumCode() {
        return currencyEnumCode;
    }

    public void setCurrencyEnumCode(String currencyEnumCode) {
        this.currencyEnumCode = currencyEnumCode;
    }

    public String getCurrencyEnumText() {
        return currencyEnumText;
    }

    public void setCurrencyEnumText(String currencyEnumText) {
        this.currencyEnumText = currencyEnumText;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<MoneyChildAccountModel> getAmountVOList() {
        return amountVOList;
    }

    public void setAmountVOList(List<MoneyChildAccountModel> amountVOList) {
        this.amountVOList = amountVOList;
    }
}
