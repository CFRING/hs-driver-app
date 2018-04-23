package com.hongshi.wuliudidi.cashier.model;

/**
 * Created on 2016/4/27.
 */
public class TradeResultModel {
    /**
     * gmtModified : 1461747193000
     * id : 2016042702100248002A000
     * desc : 提现申请已提交，请等待银行处理
     * statusFinish : false
     * gmtCreate : 1461747193000
     * userId : CYTI0000017002A000
     * money : 3780
     * moneyUnitText : 元
     * tradeName : 提现
     * tradeProductCode : WITHDRAW_XIAOER_AR
     */

    /**
     * 更新时间
     */
    private long gmtModified;
    private String id;
    /**
     * 描述
     */
    private String desc;
    /**
     * 状态是否结束
     */
    private boolean statusFinish;
    /**
     * 创建时间
     */
    private long gmtCreate;
    /**
     * 资金用户ID号
     */
    private String userId;
    /**
     * 金钱
     */
    private String money;
    private String moneyUnitText;
    /**
     * 交易名字
     */
    private String tradeName;
    /**
     * 交易码
     */
    private String tradeProductCode;

    public long getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(long gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isStatusFinish() {
        return statusFinish;
    }

    public void setStatusFinish(boolean statusFinish) {
        this.statusFinish = statusFinish;
    }

    public long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMoneyUnitText() {
        return moneyUnitText;
    }

    public void setMoneyUnitText(String moneyUnitText) {
        this.moneyUnitText = moneyUnitText;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getTradeProductCode() {
        return tradeProductCode;
    }

    public void setTradeProductCode(String tradeProductCode) {
        this.tradeProductCode = tradeProductCode;
    }
}
