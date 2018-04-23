package com.hongshi.wuliudidi.incomebook;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhuoran on 2016/4/22.
 */
public class TransitRecordOverviewModel {
    private String title;
    private int currentPage;//当前的页数
    private int itemCount;//总条目数
    private Map<String, List<CarrierBillDetailVO>> map;
    private List<String> month;
    private int pageSize;
    private int pageTotal;
    private Date queryDateE;
    private Date queryDateS;
    private int queryType;//0:每日统计,1:月度统计,2:年度统计
    private double sumMoney;//年度、月度统计中的总金额
    private int sumRecord;//年度、月度统计中的总运单数目
    private int sumTruck;//年度、月度统计中参与运输的车辆总数
    private String truckId;//如果是按车查询的，返回查询时给入的truckId
    private List<CarrierBillDetailVO> billDetailVoList;//年度、月度、每日统计中的条目model列表

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public Map<String, List<CarrierBillDetailVO>> getMap() {
        return map;
    }

    public void setMap(Map<String, List<CarrierBillDetailVO>> map) {
        this.map = map;
    }

    public List<String> getMonth() {
        return month;
    }

    public void setMonth(List<String> month) {
        this.month = month;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public Date getQueryDateE() {
        return queryDateE;
    }

    public void setQueryDateE(Date queryDateE) {
        this.queryDateE = queryDateE;
    }

    public Date getQueryDateS() {
        return queryDateS;
    }

    public void setQueryDateS(Date queryDateS) {
        this.queryDateS = queryDateS;
    }

    public int getQueryType() {
        return queryType;
    }

    public void setQueryType(int queryType) {
        this.queryType = queryType;
    }

    public double getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(double sumMoney) {
        this.sumMoney = sumMoney;
    }

    public int getSumRecord() {
        return sumRecord;
    }

    public void setSumRecord(int sumRecord) {
        this.sumRecord = sumRecord;
    }

    public int getSumTruck() {
        return sumTruck;
    }

    public void setSumTruck(int sumTruck) {
        this.sumTruck = sumTruck;
    }

    public List<CarrierBillDetailVO> getBillDetailVoList() {
        return billDetailVoList;
    }

    public void setBillDetailVoList(List<CarrierBillDetailVO> billDetailVoList) {
        this.billDetailVoList = billDetailVoList;
    }

    public String getTruckId() {
        return truckId;
    }

    public void setTruckId(String truckId) {
        this.truckId = truckId;
    }
}
