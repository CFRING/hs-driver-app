package com.hongshi.wuliudidi.model;

import java.util.Date;
import java.util.List;

public class AuctionDetailsModel{
 
	private String auctionId;
	//竞拍类型值，1：提示价，2：一口价, 3:报价区间
	private int auctionType ;
	//竞拍类型文本，1.提示价，2.一口价，3：报价区间
	private String auctionTypeText;
	//竞拍提示价,单位元
	private double auctionPrice;
	//距离竞拍结束时间的毫秒差，若已结束则为0
	private long diffBidEnd;
	//当前位置和发货地的距离，单位米
	private int distance;
	//货物名称
	private String goodsName;
	//货物数量
	private int goodsCount;
	//货物数量code
	private String goodsCountUnit;
	//货物数量text
	private String goodsCountUnitText;
	//货物体积
	private double goodsVolume;
	//货物体积code
	private String goodsVolumeUnit;
	//货物体积text
	private String goodsVolumeUnitText;
	//货物重量
	private double goodsWeight;
	//货物重量code
	private String goodsWeightUnit;
	//货物重量text
	private String goodsWeightUnitText;
	//货物装车数量
	private int goodsTrucks;
	//发票类型值：1:不用发票，2：3%，3：11%
	private int invoiceType;
	//发票类型文本：1:不用发票，2：3%，3：11%
	private String invoiceTypeText;
	//货物型号
	private String modelNumber;
	//包装类型文本，1：袋装，2：散装
	private String packageType;
	//包装类型文本，1：袋装，2：散装
	private String packageTypeText;
	//付款类型值，1.先汇付，2。货到付款，3。延期付款
	private int payType;
	//付款类型文本，1：先汇付，2：货到付款，3：延期付款
	private String payTypeText;
	//承运期开始时间毫秒
	private long gmtStartPeriod;
	//承运期结束时间毫秒
	private long gmtEndPeriod;
	//收货方省份
	private String recvProvince;
	//收货方城市
	private String recvCity;
	//收货地址
	private String recvAddr;
	//收货地维度
	private double recvLat;
	//收货地经度
	private double recvLng;
	//发货方省份
	private String sendProvince;
	//发货方地市
	private String sendCity;
	//发货地址
	private String sendAddr;
	private double sendLat;
	private double sendLng;
	//发货人电话
	private String senderPhone;
	//车型要求
	private List<String> truckModel;
	//车长要求
	private List<String> truckLength;
	//车厢类型要求
	private List<String> truckCarriage;
	//备注
	private String remark;
	//结算单位code
	private String settleUnit;
	//结算单位文本
	private String settleUnitText;
	//计量单位，1千克2件3立方米4吨
	private String assignUnit;
	//竞价人竞价时间毫秒
	private long gmtBid;
	//竞价单状态3.竞拍中，4已取消，99竞价已结束
	private int status;
	//竞价单状态value：3.竞价中，4.已取消，99竞价已截止
	private String statusText;
	//竞价条目ID（仅当已竞价时才有效）
	private String bidItemId;
	//参与竞价的人数
	private int bidderAmount;
	//结算比例
	private String auctionBillTemplate;
	//结算方式 2.7.0新增
	private List<SettleModel> billPaymentWayList;
	/**
	 * 开始竞价时间
	 * 三种类型都有可能有
	 */
	private Date gmtBidStart;
	/**
	 * 最低报价
	 * 只有1,3有
	 */
	private double lowPrice;
	/**
	 * 最高报价
	 * 只有类型1,3有
	 */
	private double highPrice;
	/**
	 * 加价幅度
	 * 只有类型1,3有
	 */
	private double bidIncrement;
	/**
	 * 是否开始接单
	 * 三种类型都有可能有
	 */
	private boolean isBidStart = true;
	/**
	 * 货物剩余量
	 * 只有类型2有
	 */
	private String goodsAmountSp;
	/**
	 * 距离竞拍开始时间差（秒）
	 */
	private long diffStartBid;
	/**
	 * 是否是内部货源
	 * 1, "内部货源";2, "外部货源"
	 */
	private int               innerGoods;

	private String            innerGoodsTxt;

	/**  分享字段*/
	private String            dynamicShareStr;

	private String senderCorporation;//结算公司名称

	public List<SettleModel> getBillPaymentWayList() {
		return billPaymentWayList;
	}

	public void setBillPaymentWayList(List<SettleModel> billPaymentWayList) {
		this.billPaymentWayList = billPaymentWayList;
	}

	public String getSenderCorporation() {
		return senderCorporation;
	}

	public void setSenderCorporation(String senderCorporation) {
		this.senderCorporation = senderCorporation;
	}

	public String getDynamicShareStr() {
		return dynamicShareStr;
	}

	public void setDynamicShareStr(String dynamicShareStr) {
		this.dynamicShareStr = dynamicShareStr;
	}

	public String getRecvProvince() {
		return recvProvince;
	}
	public void setRecvProvince(String recvProvince) {
		this.recvProvince = recvProvince;
	}
	public String getRecvCity() {
		return recvCity;
	}
	public void setRecvCity(String recvCity) {
		this.recvCity = recvCity;
	}
	public String getSendProvince() {
		return sendProvince;
	}
	public void setSendProvince(String sendProvince) {
		this.sendProvince = sendProvince;
	}
	public String getSendCity() {
		return sendCity;
	}
	public void setSendCity(String sendCity) {
		this.sendCity = sendCity;
	}
	
	public String getPayTypeText() {
		return payTypeText;
	}
	public void setPayTypeText(String payTypeText) {
		this.payTypeText = payTypeText;
	}
	public String getAuctionId() {
		return auctionId;
	}
	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
	}
	public int getAuctionType() {
		return auctionType;
	}
	public void setAuctionType(int auctionType) {
		this.auctionType = auctionType;
	}
	public void setAuctionPrice(long auctionPrice) {
		this.auctionPrice = auctionPrice;
	}
	public long getDiffBidEnd() {
		return diffBidEnd;
	}
	public void setDiffBidEnd(long diffBidEnd) {
		this.diffBidEnd = diffBidEnd;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public int getGoodsCount() {
		return goodsCount;
	}
	public void setGoodsCount(int goodsCount) {
		this.goodsCount = goodsCount;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public double getGoodsVolume() {
		return goodsVolume;
	}
	public void setGoodsVolume(double goodsVolume) {
		this.goodsVolume = goodsVolume;
	}
	public double getGoodsWeight() {
		return goodsWeight;
	}
	public void setGoodsWeight(double goodsWeight) {
		this.goodsWeight = goodsWeight;
	}
	public String getInvoiceTypeText() {
		return invoiceTypeText;
	}
	public void setInvoiceTypeText(String invoiceTypeText) {
		this.invoiceTypeText = invoiceTypeText;
	}
	public String getModelNumber() {
		return modelNumber;
	}
	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}
	public String getPackageType() {
		return packageType;
	}
	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}
	public String getPackageTypeText() {
		return packageTypeText;
	}
	public void setPackageTypeText(String packageTypeText) {
		this.packageTypeText = packageTypeText;
	}
	public String getRecvAddr() {
		return recvAddr;
	}
	public void setRecvAddr(String recvAddr) {
		this.recvAddr = recvAddr;
	}
	public double getRecvLat() {
		return recvLat;
	}
	public void setRecvLat(double recvLat) {
		this.recvLat = recvLat;
	}
	public double getRecvLng() {
		return recvLng;
	}
	public void setRecvLng(double recvLng) {
		this.recvLng = recvLng;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSendAddr() {
		return sendAddr;
	}
	public void setSendAddr(String sendAddr) {
		this.sendAddr = sendAddr;
	}
	public double getSendLat() {
		return sendLat;
	}
	public void setSendLat(double sendLat) {
		this.sendLat = sendLat;
	}
	public double getSendLng() {
		return sendLng;
	}
	public void setSendLng(double sendLng) {
		this.sendLng = sendLng;
	}
	public String getSenderPhone() {
		return senderPhone;
	}
	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}
	public List<String> getTruckModel() {
		return truckModel;
	}
	public void setTruckModel(List<String> truckModel) {
		this.truckModel = truckModel;
	}
	public List<String> getTruckLength() {
		return truckLength;
	}
	public void setTruckLength(List<String> truckLength) {
		this.truckLength = truckLength;
	}
	public long getGmtBid() {
		return gmtBid;
	}
	public void setGmtBid(long gmtBid) {
		this.gmtBid = gmtBid;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getAuctionTypeText() {
		return auctionTypeText;
	}
	public void setAuctionTypeText(String auctionTypeText) {
		this.auctionTypeText = auctionTypeText;
	}
	public double getAuctionPrice() {
		return auctionPrice;
	}
	public void setAuctionPrice(double auctionPrice) {
		this.auctionPrice = auctionPrice;
	}
	public String getGoodsCountUnit() {
		return goodsCountUnit;
	}
	public void setGoodsCountUnit(String goodsCountUnit) {
		this.goodsCountUnit = goodsCountUnit;
	}
	public String getGoodsCountUnitText() {
		return goodsCountUnitText;
	}
	public void setGoodsCountUnitText(String goodsCountUnitText) {
		this.goodsCountUnitText = goodsCountUnitText;
	}
	public String getGoodsVolumeUnit() {
		return goodsVolumeUnit;
	}
	public void setGoodsVolumeUnit(String goodsVolumeUnit) {
		this.goodsVolumeUnit = goodsVolumeUnit;
	}
	public String getGoodsWeightUnit() {
		return goodsWeightUnit;
	}
	public void setGoodsWeightUnit(String goodsWeightUnit) {
		this.goodsWeightUnit = goodsWeightUnit;
	}
	public String getGoodsWeightUnitText() {
		return goodsWeightUnitText;
	}
	public void setGoodsWeightUnitText(String goodsWeightUnitText) {
		this.goodsWeightUnitText = goodsWeightUnitText;
	}
	public int getGoodsTrucks() {
		return goodsTrucks;
	}
	public void setGoodsTrucks(int goodsTrucks) {
		this.goodsTrucks = goodsTrucks;
	}
	public int getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(int invoiceType) {
		this.invoiceType = invoiceType;
	}
	public int getPayType() {
		return payType;
	}
	public void setPayType(int payType) {
		this.payType = payType;
	}
	public long getGmtStartPeriod() {
		return gmtStartPeriod;
	}
	public void setGmtStartPeriod(long gmtStartPeriod) {
		this.gmtStartPeriod = gmtStartPeriod;
	}
	public long getGmtEndPeriod() {
		return gmtEndPeriod;
	}
	public void setGmtEndPeriod(long gmtEndPeriod) {
		this.gmtEndPeriod = gmtEndPeriod;
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
	public String getStatusText() {
		return statusText;
	}
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	public String getBidItemId() {
		return bidItemId;
	}
	public void setBidItemId(String bidItemId) {
		this.bidItemId = bidItemId;
	}
	public String getGoodsVolumeUnitText() {
		return goodsVolumeUnitText;
	}
	public void setGoodsVolumeUnitText(String goodsVolumeUnitText) {
		this.goodsVolumeUnitText = goodsVolumeUnitText;
	}
	public String getSendAddrWhole(){
		return sendProvince + sendCity + sendAddr;
	}
	public String getRecvAddrWhole(){
		return recvProvince + recvCity + recvAddr;
	}
	public int getBidderAmount() {
		return bidderAmount;
	}
	public void setBidderAmount(int bidderAmount) {
		this.bidderAmount = bidderAmount;
	}
	public List<String> getTruckCarriage() {
		return truckCarriage;
	}
	public void setTruckCarriage(List<String> truckCarriage) {
		this.truckCarriage = truckCarriage;
	}
	public String getAuctionBillTemplate() {
		return auctionBillTemplate;
	}
	public void setAuctionBillTemplate(String auctionBillTemplate) {
		this.auctionBillTemplate = auctionBillTemplate;
	}

	public Date getGmtBidStart() {
		return gmtBidStart;
	}

	public void setGmtBidStart(Date gmtBidStart) {
		this.gmtBidStart = gmtBidStart;
	}

	public double getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(double lowPrice) {
		this.lowPrice = lowPrice;
	}

	public double getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(double highPrice) {
		this.highPrice = highPrice;
	}

	public double getBidIncrement() {
		return bidIncrement;
	}

	public void setBidIncrement(double bidIncrement) {
		this.bidIncrement = bidIncrement;
	}

	public boolean isBidStart() {
		return isBidStart;
	}

	public void setBidStart(boolean bidStart) {
		isBidStart = bidStart;
	}

	public String getGoodsAmountSp() {
		return goodsAmountSp;
	}

	public void setGoodsAmountSp(String goodsAmountSp) {
		this.goodsAmountSp = goodsAmountSp;
	}

	public long getDiffStartBid() {
		return diffStartBid;
	}

	public void setDiffStartBid(long diffStartBid) {
		this.diffStartBid = diffStartBid;
	}

	public int getInnerGoods() {
		return innerGoods;
	}

	public void setInnerGoods(int innerGoods) {
		this.innerGoods = innerGoods;
	}

	public String getInnerGoodsTxt() {
		return innerGoodsTxt;
	}

	public void setInnerGoodsTxt(String innerGoodsTxt) {
		this.innerGoodsTxt = innerGoodsTxt;
	}

	public String getAssignUnit() {
		return assignUnit;
	}

	public void setAssignUnit(String assignUnit) {
		this.assignUnit = assignUnit;
	}

}
