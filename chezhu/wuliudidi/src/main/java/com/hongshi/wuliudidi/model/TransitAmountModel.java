package com.hongshi.wuliudidi.model;

/**
 * Created by he on 2016/7/19.
 */
public class TransitAmountModel {
    private String monthStr;
    private String transitCount;
    private String truckCount;
    private String yearStr;
    private String truckNumber;
    private String count;

    public String getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getMonthStr() {
        return monthStr;
    }

    public void setMonthStr(String monthStr) {
        this.monthStr = monthStr;
    }

    public String getTransitCount() {
        return transitCount;
    }

    public void setTransitCount(String transitCount) {
        this.transitCount = transitCount;
    }

    public String getTruckCount() {
        return truckCount;
    }

    public void setTruckCount(String truckCount) {
        this.truckCount = truckCount;
    }

    public String getYearStr() {
        return yearStr;
    }

    public void setYearStr(String yearStr) {
        this.yearStr = yearStr;
    }
}
