package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huiyuan on 2017/5/20.
 */

public class BillTotalVO implements Serializable {

    private String date;
    //物料运费汇总
    private List<KVModel> goodsCategoryFeeList;
    //油费
    private double oilFee;
    //其他费用
    private double otherFee;
    //运费金额
    private double totalMoney;
    //轮胎费
    private double tyreFee;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<KVModel> getGoodsCategoryFeeList() {
        return goodsCategoryFeeList;
    }

    public void setGoodsCategoryFeeList(List<KVModel> goodsCategoryFeeList) {
        this.goodsCategoryFeeList = goodsCategoryFeeList;
    }

    public double getOilFee() {
        return oilFee;
    }

    public void setOilFee(double oilFee) {
        this.oilFee = oilFee;
    }

    public double getOtherFee() {
        return otherFee;
    }

    public void setOtherFee(double otherFee) {
        this.otherFee = otherFee;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public double getTyreFee() {
        return tyreFee;
    }

    public void setTyreFee(double tyreFee) {
        this.tyreFee = tyreFee;
    }
}
