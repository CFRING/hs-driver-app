package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huiyuan on 2017/8/12.
 */

public class OilModel implements Serializable {
    private String amount = "";
    private List<OilCardModel> cardInfoList;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public List<OilCardModel> getCardInfoList() {
        return cardInfoList;
    }

    public void setCardInfoList(List<OilCardModel> cardInfoList) {
        this.cardInfoList = cardInfoList;
    }
}
