package com.hongshi.wuliudidi.cashier.model;

import java.util.List;

/**
 * Created on 2016/4/26.
 */
public class TiXianModel {

    /**
     * amount : 5890
     * tradeType : 提现
     * bankCardList : [{"id":"YHK042002A000","bankName":"农业银行","bankCardType":null,"bankType":5,"bankNumber":"5570"},{"id":"YHK040002A000","bankName":"农业银行","bankCardType":null,"bankType":5,"bankNumber":"5571"}]
     * title : 余额转出
     * withdrawAllTitle : 全部提现
     * moneyUnitTxt : 元
     * totalTitle : 可提现金额
     * withdrawAcct : null
     * withdrawAcctTitle : 提现账户
     * bankTitle : 到账银行卡
     * tradeTypeTitle : 交易类型
     * moneyUnitCode : CNY_YUAN
     * currencySymbol : ￥
     * cellPhone : 15158002947
     */

    private String amount;
    private String tradeType;
    private String title;
    private String withdrawAllTitle;
    private String moneyUnitTxt;
    private String totalTitle;
    private String withdrawAcct;
    private String withdrawAcctTitle;
    private String bankTitle;
    private String tradeTypeTitle;
    private String moneyUnitCode;
    private String currencySymbol;
    private String cellPhone;


    private List<BankCard> bankCardList;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWithdrawAllTitle() {
        return withdrawAllTitle;
    }

    public void setWithdrawAllTitle(String withdrawAllTitle) {
        this.withdrawAllTitle = withdrawAllTitle;
    }

    public String getMoneyUnitTxt() {
        return moneyUnitTxt;
    }

    public void setMoneyUnitTxt(String moneyUnitTxt) {
        this.moneyUnitTxt = moneyUnitTxt;
    }

    public String getTotalTitle() {
        return totalTitle;
    }

    public void setTotalTitle(String totalTitle) {
        this.totalTitle = totalTitle;
    }

    public String getWithdrawAcct() {
        return withdrawAcct;
    }

    public void setWithdrawAcct(String withdrawAcct) {
        this.withdrawAcct = withdrawAcct;
    }

    public String getWithdrawAcctTitle() {
        return withdrawAcctTitle;
    }

    public void setWithdrawAcctTitle(String withdrawAcctTitle) {
        this.withdrawAcctTitle = withdrawAcctTitle;
    }

    public String getBankTitle() {
        return bankTitle;
    }

    public void setBankTitle(String bankTitle) {
        this.bankTitle = bankTitle;
    }

    public String getTradeTypeTitle() {
        return tradeTypeTitle;
    }

    public void setTradeTypeTitle(String tradeTypeTitle) {
        this.tradeTypeTitle = tradeTypeTitle;
    }

    public String getMoneyUnitCode() {
        return moneyUnitCode;
    }

    public void setMoneyUnitCode(String moneyUnitCode) {
        this.moneyUnitCode = moneyUnitCode;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public List<BankCard> getBankCardList() {
        return bankCardList;
    }

    public void setBankCardList(List<BankCard> bankCardList) {
        this.bankCardList = bankCardList;
    }

    public static class BankCard {

        /**
         * id : YHK042002A000
         * bankName : 农业银行
         * bankCardType : null
         * bankType : 5
         * bankNumber : 5570
         */

        private String id;
        private String bankName;
        private String bankCardType;
        private int bankType;
        private String bankNumber;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getBankCardType() {
            return bankCardType;
        }

        public void setBankCardType(String bankCardType) {
            this.bankCardType = bankCardType;
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
    }
}
