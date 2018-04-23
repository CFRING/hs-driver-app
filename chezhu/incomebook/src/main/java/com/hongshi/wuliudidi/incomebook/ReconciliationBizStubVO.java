package com.hongshi.wuliudidi.incomebook;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by huiyuan on 2016/8/23.
 */
public class ReconciliationBizStubVO implements Serializable{

    private String billCycleId;//账期实例编号
    private ArrayList<ReconciliationBizStubCardVO> cardVoList;//银行卡-工资分配
    private double deductAmount;//途耗扣款
    private double leaseAndRepairAmount;//租赁费/维修费
    private String moneyUnitText;//金钱显示单位
    private double oilAmount;//油费
    private double roadAmount;//通行费
    private double salaryAmount;//工资
    private double totalAmount;//总金额
    private double transitAmount;//运费
    private String truckId;//车牌id，所要查询的车牌id
    private double tyreAmount;//轮胎费
    private double laowufei;//劳务费

    public double getLaowufei() {
        return laowufei;
    }

    public void setLaowufei(double laowufei) {
        this.laowufei = laowufei;
    }

    public ArrayList<ReconciliationBizStubCardVO> getCardVoList() {
        return cardVoList;
    }

    public void setCardVoList(ArrayList<ReconciliationBizStubCardVO> cardVoList) {
        this.cardVoList = cardVoList;
    }

    public String getBillCycleId() {
        return billCycleId;
    }

    public void setBillCycleId(String billCycleId) {
        this.billCycleId = billCycleId;
    }

    public double getDeductAmount() {
        return deductAmount;
    }

    public void setDeductAmount(double deductAmount) {
        this.deductAmount = deductAmount;
    }

    public double getLeaseAndRepairAmount() {
        return leaseAndRepairAmount;
    }

    public void setLeaseAndRepairAmount(double leaseAndRepairAmount) {
        this.leaseAndRepairAmount = leaseAndRepairAmount;
    }

    public String getMoneyUnitText() {
        return moneyUnitText;
    }

    public void setMoneyUnitText(String moneyUnitText) {
        this.moneyUnitText = moneyUnitText;
    }

    public double getOilAmount() {
        return oilAmount;
    }

    public void setOilAmount(double oilAmount) {
        this.oilAmount = oilAmount;
    }

    public double getRoadAmount() {
        return roadAmount;
    }

    public void setRoadAmount(double roadAmount) {
        this.roadAmount = roadAmount;
    }

    public double getSalaryAmount() {
        return salaryAmount;
    }

    public void setSalaryAmount(double salaryAmount) {
        this.salaryAmount = salaryAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTransitAmount() {
        return transitAmount;
    }

    public void setTransitAmount(double transitAmount) {
        this.transitAmount = transitAmount;
    }

    public String getTruckId() {
        return truckId;
    }

    public void setTruckId(String truckId) {
        this.truckId = truckId;
    }

    public double getTyreAmount() {
        return tyreAmount;
    }

    public void setTyreAmount(double tyreAmount) {
        this.tyreAmount = tyreAmount;
    }
}
