package com.hongshi.wuliudidi.model;

public class TaskOrderModel {
	//竞价条目/托运单ID
	private String bidItemId;
	private String driverName;
	//派车单状态值， 1.待运输 2.运输中 3.已签收
	private int status;
	private String statusText;
	private double transitAmount;
	private String transitTaskId;
	private String truckNum;
	private String unit;
	private String unitText;
	//发货时间
	private long gmtStart;
	private long gmtCreate;
	//到货时间
	private long gmtEnd;
	private String sendAddr;
	private String recvAddr;
	//派车id
	private String planId;
	//货物名
	private String goodsName;
	//计划运输量
	private double planAmount;
	//计划运输量
	private String assignUnitText;
	//计划运输量
	private String assignUnit;
	public String getAssignUnit() {
		return assignUnit;
	}
	public void setAssignUnit(String assignUnit) {
		this.assignUnit = assignUnit;
	}
	private ConsignDetailModel consignDetailModel;
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public double getPlanAmount() {
		return planAmount;
	}
	public void setPlanAmount(double planAmount) {
		this.planAmount = planAmount;
	}
	public String getAssignUnitText() {
		return assignUnitText;
	}
	public void setAssignUnitText(String assignUnitText) {
		this.assignUnitText = assignUnitText;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public long getGmtEnd() {
		return gmtEnd;
	}
	public void setGmtEnd(long gmtEnd) {
		this.gmtEnd = gmtEnd;
	}
	public ConsignDetailModel getConsignDetailModel() {
		return consignDetailModel;
	}
	public void setConsignDetailModel(ConsignDetailModel consignDetailModel) {
		this.consignDetailModel = consignDetailModel;
	}
	public long getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(long gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public String getSendAddr() {
		return sendAddr;
	}
	public void setSendAddr(String sendAddr) {
		this.sendAddr = sendAddr;
	}
	public String getRecvAddr() {
		return recvAddr;
	}
	public void setRecvAddr(String recvAddr) {
		this.recvAddr = recvAddr;
	}

	public long getGmtStart() {
		return gmtStart;
	}
	public void setGmtStart(long gmtStart) {
		this.gmtStart = gmtStart;
	}
	public String getBidItemId() {
		return bidItemId;
	}
	public void setBidItemId(String bidItemId) {
		this.bidItemId = bidItemId;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStatusText() {
		return statusText;
	}
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	public double getTransitAmount() {
		return transitAmount;
	}
	public void setTransitAmount(double transitAmount) {
		this.transitAmount = transitAmount;
	}
	public String getTransitTaskId() {
		return transitTaskId;
	}
	public void setTransitTaskId(String transitTaskId) {
		this.transitTaskId = transitTaskId;
	}
	public String getTruckNum() {
		return truckNum;
	}
	public void setTruckNum(String truckNum) {
		this.truckNum = truckNum;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getUnitText() {
		return unitText;
	}
	public void setUnitText(String unitText) {
		this.unitText = unitText;
	}
}
