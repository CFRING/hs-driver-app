package com.hongshi.wuliudidi.incomebook;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by huiyuan on 2016/8/15.
 */
public class BillItemModel implements Serializable{

    private String beginDateStr;//账期的开始时间 yyyyy-MM-dd格式

    private String billCycleId;//账期实例编号

    private String endDateStr;//账期的结束时间 yyyyy-MM-dd格式

    private String formType;//1表示供应段账单，2代表红狮叫车账单

    private String goodsStationUid;//货运站编号

    private String moneyUnitText;//金钱显示单位

    private String oppId;//核对人ID

    private String oppIdText;//核对人姓名

    private String period;//期号

    private double realAmount;//实际结算，金钱

    private int receiptStatus;//回执状态值，1-待回执、2-已同意、3-已拒绝

    private String receiptStatusText;//回执状态文本，0-待回执、1-已同意、2-已拒绝

    private double totalAmount;//总金额，金钱

    private int transitTimes;//运输车次

    private int truckCount;//运输车辆

    private ArrayList<ReconciliationTruckVO> truckVoList;//车牌VO数组

    private double weightTotal;//数量

    private String weightUnitText;//数量显示单位

    /**  对账单种类：1-正常，2-补单*/
    private int                         style;

    private String                      styleTxt;

    /**  备注*/
    private String                      note;

    /**
     * 补账的生成时间 yyyyy-MM-dd格式
     */
    private String                      gmtCreateStr;

    private long   oil; //油费
    private long   salary;//工资
    private long   tyre;//轮胎费
    private long   lease;//租凭费
    private long   road;//通行费
    private long   yunfei;//运费

    public long getOil() {
        return oil;
    }

    public void setOil(long oil) {
        this.oil = oil;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public long getTyre() {
        return tyre;
    }

    public void setTyre(long tyre) {
        this.tyre = tyre;
    }

    public long getLease() {
        return lease;
    }

    public void setLease(long lease) {
        this.lease = lease;
    }

    public long getRoad() {
        return road;
    }

    public void setRoad(long road) {
        this.road = road;
    }

    public long getYunfei() {
        return yunfei;
    }

    public void setYunfei(long yunfei) {
        this.yunfei = yunfei;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public String getStyleTxt() {
        return styleTxt;
    }

    public void setStyleTxt(String styleTxt) {
        this.styleTxt = styleTxt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getGmtCreateStr() {
        return gmtCreateStr;
    }

    public void setGmtCreateStr(String gmtCreateStr) {
        this.gmtCreateStr = gmtCreateStr;
    }

    public String getBeginDateStr() {
        return beginDateStr;
    }

    public void setBeginDateStr(String beginDateStr) {
        this.beginDateStr = beginDateStr;
    }

    public String getBillCycleId() {
        return billCycleId;
    }

    public void setBillCycleId(String billCycleId) {
        this.billCycleId = billCycleId;
    }

    public String getEndDateStr() {
        return endDateStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getGoodsStationUid() {
        return goodsStationUid;
    }

    public void setGoodsStationUid(String goodsStationUid) {
        this.goodsStationUid = goodsStationUid;
    }

    public String getMoneyUnitText() {
        return moneyUnitText;
    }

    public void setMoneyUnitText(String moneyUnitText) {
        this.moneyUnitText = moneyUnitText;
    }

    public String getOppId() {
        return oppId;
    }

    public void setOppId(String oppId) {
        this.oppId = oppId;
    }

    public String getOppIdText() {
        return oppIdText;
    }

    public void setOppIdText(String oppIdText) {
        this.oppIdText = oppIdText;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public double getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(double realAmount) {
        this.realAmount = realAmount;
    }

    public int getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(int receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    public String getReceiptStatusText() {
        return receiptStatusText;
    }

    public void setReceiptStatusText(String receiptStatusText) {
        this.receiptStatusText = receiptStatusText;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
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

    public ArrayList<ReconciliationTruckVO> getTruckVoList() {
        return truckVoList;
    }

    public void setTruckVoList(ArrayList<ReconciliationTruckVO> truckVoList) {
        this.truckVoList = truckVoList;
    }

    public double getWeightTotal() {
        return weightTotal;
    }

    public void setWeightTotal(double weightTotal) {
        this.weightTotal = weightTotal;
    }

    public String getWeightUnitText() {
        return weightUnitText;
    }

    public void setWeightUnitText(String weightUnitText) {
        this.weightUnitText = weightUnitText;
    }
}
