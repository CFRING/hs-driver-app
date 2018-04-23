package com.hongshi.wuliudidi.model;

import java.util.List;

public class AuctionDoBid {
	private String auctionId;
	// 竞拍量
	private double bidAmount;
	//竞拍报价，单位元
	private double bidPrice;
	//多个报备车辆的ID数组
	private List<String> truckIds;
	//运价模板ID
	private String billTemplateId;
	//是否来自首页一键接单
	private boolean fromFrontPage;
	//结算方式ID
	private String PayTypeId;

	public String getPayTypeId() {
		return PayTypeId;
	}

	public void setPayTypeId(String payTypeId) {
		PayTypeId = payTypeId;
	}

	public boolean isFromFrontPage() {
		return fromFrontPage;
	}

	public void setFromFrontPage(boolean fromFrontPage) {
		this.fromFrontPage = fromFrontPage;
	}

	public String getBillTemplateId() {
		return billTemplateId;
	}

	public void setBillTemplateId(String billTemplateId) {
		this.billTemplateId = billTemplateId;
	}

	public String getAuctionId() {
		return auctionId;
	}
	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
	}
	public double getBidAmount() {
		return bidAmount;
	}
	public void setBidAmount(double bidAmount) {
		this.bidAmount = bidAmount;
	}
	public double getBidPrice() {
		return bidPrice;
	}
	public void setBidPrice(double bidPrice) {
		this.bidPrice = bidPrice;
	}
	public List<String> getTruckIds() {
		return truckIds;
	}
	public void setTruckIds(List<String> truckIds) {
		this.truckIds = truckIds;
	}
	public AuctionDoBid(String auctionId, double bidAmount, double bidPrice, List<String> truckIds) {
		super();
		this.auctionId = auctionId;
		this.bidAmount = bidAmount;
		this.bidPrice = bidPrice;
		this.truckIds = truckIds;
	}
	
}
