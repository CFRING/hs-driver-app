package com.hongshi.wuliudidi.model;

import java.util.List;

public class AuctionBidMessageModel {
	private String bidItemId;
	private double bidPrice;
	private double bidAmount;
	private String assignUnit;
	private String assignUnitText;
	private String settleUnit;
	private String settleUnitText;
	private List<AuctionTrucksModel> trucks;
	public String getBidItemId() {
		return bidItemId;
	}
	public void setBidItemId(String bidItemId) {
		this.bidItemId = bidItemId;
	}
	public double getBidPrice() {
		return bidPrice;
	}
	public void setBidPrice(double bidPrice) {
		this.bidPrice = bidPrice;
	}
	public double getBidAmount() {
		return bidAmount;
	}
	public void setBidAmount(double bidAmount) {
		this.bidAmount = bidAmount;
	}
	public String getAssignUnit() {
		return assignUnit;
	}
	public void setAssignUnit(String assignUnit) {
		this.assignUnit = assignUnit;
	}
	public String getAssignUnitText() {
		return assignUnitText;
	}
	public void setAssignUnitText(String assignUnitText) {
		this.assignUnitText = assignUnitText;
	}
	public String getSettleUnit() {
		return settleUnit;
	}
	public void setSettleUnit(String settleUnit) {
		this.settleUnit = settleUnit;
	}
	public String getSettleUnitText() {
		return settleUnitText;
	}
	public void setSettleUnitText(String settleUnitText) {
		this.settleUnitText = settleUnitText;
	}
	public List<AuctionTrucksModel> getTrucks() {
		return trucks;
	}
	public void setTrucks(List<AuctionTrucksModel> trucks) {
		this.trucks = trucks;
	}
	
}
