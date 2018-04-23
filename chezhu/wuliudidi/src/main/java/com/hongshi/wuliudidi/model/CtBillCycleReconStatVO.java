package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by huiyuan on 2017/9/20.
 */

public class CtBillCycleReconStatVO implements Serializable {
    //账期id
    private String billCycleId;
    //消费费用
    private String consumptionMoney;
    //账单生成时间
    private Date gmtCreate;
    //总油费
    private String oilMoney;
    //运输周期
    private String period;
    //总劳务费
    private String salaryMoney;
    //总运费
    private String totalMoney;
    //总运输次数
    private int transitTimes;
    //总轮胎费
    private String tyreMoney;
    //用户id
    private String userId;

    public String getBillCycleId() {
        return billCycleId;
    }

    public void setBillCycleId(String billCycleId) {
        this.billCycleId = billCycleId;
    }

    public String getConsumptionMoney() {
        return consumptionMoney;
    }

    public void setConsumptionMoney(String consumptionMoney) {
        this.consumptionMoney = consumptionMoney;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getOilMoney() {
        return oilMoney;
    }

    public void setOilMoney(String oilMoney) {
        this.oilMoney = oilMoney;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSalaryMoney() {
        return salaryMoney;
    }

    public void setSalaryMoney(String salaryMoney) {
        this.salaryMoney = salaryMoney;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getTransitTimes() {
        return transitTimes;
    }

    public void setTransitTimes(int transitTimes) {
        this.transitTimes = transitTimes;
    }

    public String getTyreMoney() {
        return tyreMoney;
    }

    public void setTyreMoney(String tyreMoney) {
        this.tyreMoney = tyreMoney;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
