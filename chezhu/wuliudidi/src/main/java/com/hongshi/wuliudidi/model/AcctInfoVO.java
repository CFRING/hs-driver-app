package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huiyuan on 2017/6/19.
 */

public class AcctInfoVO extends BankcardModel implements Serializable {
    private String balance;
    private List<BankcardModel> bankCardVOList;
    private boolean hasOilCard;

    public boolean getHasOilCard() {
        return hasOilCard;
    }

    public void setHasOilCard(boolean hasOilCard) {
        this.hasOilCard = hasOilCard;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public List<BankcardModel> getBankCardVOList() {
        return bankCardVOList;
    }

    public void setBankCardVOList(List<BankcardModel> bankCardVOList) {
        this.bankCardVOList = bankCardVOList;
    }
}
