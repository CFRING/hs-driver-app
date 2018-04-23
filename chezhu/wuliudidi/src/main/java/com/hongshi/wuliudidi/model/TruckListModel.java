package com.hongshi.wuliudidi.model;

public class TruckListModel {
	// 车辆ID
	private String truckId;
	// 车牌号
	private String number;

	// 车型
	private String typeText;

	// 车长
	private String lengthText;

	// 最大载重
	private double capacity;
	// 竞拍条目ID
	private String bidItemId;

	// 已完成的运量
	private String finishedAmount;

	// 货物量单位（外）：1吨 3立方米
	private String unitText;
	private String unit;
	private double planAmount;
	private String planId;
	//司机号码
	private String cellphone;
	//派车传递的id
	private String driverBookId;
	//剩余量
	private double restAmount;
	//true：运输中，false：不在运输中
	private boolean inTransit;
	public double getRestAmount() {
		return restAmount;
	}

	public void setRestAmount(double restAmount) {
		this.restAmount = restAmount;
	}

	public boolean isInTransit() {
		return inTransit;
	}

	public void setInTransit(boolean inTransit) {
		this.inTransit = inTransit;
	}
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDriverBookId() {
		return driverBookId;
	}

	public void setDriverBookId(String driverBookId) {
		this.driverBookId = driverBookId;
	}

	private int id;
	
	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getTruckId() {
		return truckId;
	}

	public void setTruckId(String truckId) {
		this.truckId = truckId;
	}

	public double getPlanAmount() {
		return planAmount;
	}

	public void setPlanAmount(double planAmount) {
		this.planAmount = planAmount;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getTypeText() {
		return typeText;
	}

	public void setTypeText(String typeText) {
		this.typeText = typeText;
	}

	public String getLengthText() {
		return lengthText;
	}

	public void setLengthText(String lengthText) {
		this.lengthText = lengthText;
	}

	public double getCapacity() {
		return capacity;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public String getBidItemId() {
		return bidItemId;
	}

	public void setBidItemId(String bidItemId) {
		this.bidItemId = bidItemId;
	}

	public String getFinishedAmount() {
		return finishedAmount;
	}

	public void setFinishedAmount(String finishedAmount) {
		this.finishedAmount = finishedAmount;
	}

	public String getUnitText() {
		return unitText;
	}

	public void setUnitText(String unitText) {
		this.unitText = unitText;
	}
}
