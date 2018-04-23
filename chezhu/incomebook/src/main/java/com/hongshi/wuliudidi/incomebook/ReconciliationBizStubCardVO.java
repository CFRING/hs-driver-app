package com.hongshi.wuliudidi.incomebook;

import java.io.Serializable;

/**
 * Created by huiyuan on 2016/8/25.
 */
public class ReconciliationBizStubCardVO implements Serializable {
    private double amount;//金额
    private String cardEndWith;//银行卡后四位

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCardEndWith() {
        return cardEndWith;
    }

    public void setCardEndWith(String cardEndWith) {
        this.cardEndWith = cardEndWith;
    }
}
