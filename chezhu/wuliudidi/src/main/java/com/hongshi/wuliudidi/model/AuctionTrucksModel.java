package com.hongshi.wuliudidi.model;

public class AuctionTrucksModel {

	//车辆ID
	private String truckId;
	//车牌号
	private String number;
	//车型
	private String typeText;
	//车长
	private String lengthText;
	//计量单位文本，1吨 3立方米
	private String unitText;
	//车最大载重，单位吨
	private double capacity;
	//已完成运量
	private double finishedAmount;
	//车厢
    private String carriageText;
	//载货体积
    private double carryVolume;

	public String getTruckId() {
		return truckId;
	}
	public void setTruckId(String truckId) {
		this.truckId = truckId;
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
	public String getUnitText() {  
		return unitText;
	}
	public void setUnitText(String unitText) {
		this.unitText = unitText;
	}
	public double getCapacity() {
		return capacity;
	}
	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}
	public double getFinishedAmount() {
		return finishedAmount;
	}
	public void setFinishedAmount(double finishedAmount) {
		this.finishedAmount = finishedAmount;
	}
	public String getCarriageText() {
		return carriageText;
	}
	public void setCarriageText(String carriageText) {
		this.carriageText = carriageText;
	}
	public double getCarryVolume() {
		return carryVolume;
	}
	public void setCarryVolume(double carryVolume) {
		this.carryVolume = carryVolume;
	}
	
}
