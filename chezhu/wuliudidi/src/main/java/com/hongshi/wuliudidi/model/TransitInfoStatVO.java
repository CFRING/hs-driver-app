package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huiyuan on 2017/9/20.
 */

public class TransitInfoStatVO implements Serializable {
    //账期号，json格式，查看具体的运输记录需要回传
    private String billCycleJsonStr;
    //类别统计明细（按车辆或物料）
    private List<ClassfyStatVO> classfyStatVOList;
    //运输物料数
    private int goodsTypeCount;
    //总消费费用
    private String totalConsumptionMoney;
    //总运费
    private String totalMoney;
    //总油费
    private String totalOilMoney;
    //总劳务费
    private String totalSalaryMoney;
    //总轮胎费
    private String totalTyreMoney;
    //总运量
    private String totalWeight;
    //总运输次数
    private int transitTimes;
    //运输车辆数
    private int truckCount;
    //结算单位
    private String goodsStationName;
    //结算周期
    private String period;

    public String getGoodsStationName() {
        return goodsStationName;
    }

    public void setGoodsStationName(String goodsStationName) {
        this.goodsStationName = goodsStationName;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getBillCycleJsonStr() {
        return billCycleJsonStr;
    }

    public void setBillCycleJsonStr(String billCycleJsonStr) {
        this.billCycleJsonStr = billCycleJsonStr;
    }

    public List<ClassfyStatVO> getClassfyStatVOList() {
        return classfyStatVOList;
    }

    public void setClassfyStatVOList(List<ClassfyStatVO> classfyStatVOList) {
        this.classfyStatVOList = classfyStatVOList;
    }

    public int getGoodsTypeCount() {
        return goodsTypeCount;
    }

    public void setGoodsTypeCount(int goodsTypeCount) {
        this.goodsTypeCount = goodsTypeCount;
    }

    public String getTotalConsumptionMoney() {
        return totalConsumptionMoney;
    }

    public void setTotalConsumptionMoney(String totalConsumptionMoney) {
        this.totalConsumptionMoney = totalConsumptionMoney;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getTotalOilMoney() {
        return totalOilMoney;
    }

    public void setTotalOilMoney(String totalOilMoney) {
        this.totalOilMoney = totalOilMoney;
    }

    public String getTotalSalaryMoney() {
        return totalSalaryMoney;
    }

    public void setTotalSalaryMoney(String totalSalaryMoney) {
        this.totalSalaryMoney = totalSalaryMoney;
    }

    public String getTotalTyreMoney() {
        return totalTyreMoney;
    }

    public void setTotalTyreMoney(String totalTyreMoney) {
        this.totalTyreMoney = totalTyreMoney;
    }

    public String getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        this.totalWeight = totalWeight;
    }

    public int getTransitTimes() {
        return transitTimes;
    }

    public void setTransitTimes(int transitTimes) {
        this.transitTimes = transitTimes;
    }

    public int getTruckCount() {
        return truckCount;
    }

    public void setTruckCount(int truckCount) {
        this.truckCount = truckCount;
    }
}
