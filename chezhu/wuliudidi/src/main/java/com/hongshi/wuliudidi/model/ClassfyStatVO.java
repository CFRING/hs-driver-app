package com.hongshi.wuliudidi.model;

import java.io.Serializable;

/**
 * Created by huiyuan on 2017/9/20.
 */

public class ClassfyStatVO implements Serializable {
    //1车辆    2物料
    private int classifyType;
    //车牌号或物料
    private String classify;
    //车辆Id或物料ID
    private String classifyId;
    //消费
    private String consumptionMoney;
    //总运费
    private String money;
    //油费
    private String oilMoney;
    //劳务费
    private String salaryMoney;
    //轮胎费
    private String tyreMoney;

    public int getClassifyType() {
        return classifyType;
    }

    public void setClassifyType(int classifyType) {
        this.classifyType = classifyType;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public String getConsumptionMoney() {
        return consumptionMoney;
    }

    public void setConsumptionMoney(String consumptionMoney) {
        this.consumptionMoney = consumptionMoney;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOilMoney() {
        return oilMoney;
    }

    public void setOilMoney(String oilMoney) {
        this.oilMoney = oilMoney;
    }

    public String getSalaryMoney() {
        return salaryMoney;
    }

    public void setSalaryMoney(String salaryMoney) {
        this.salaryMoney = salaryMoney;
    }

    public String getTyreMoney() {
        return tyreMoney;
    }

    public void setTyreMoney(String tyreMoney) {
        this.tyreMoney = tyreMoney;
    }
}
