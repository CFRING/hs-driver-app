package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AuctionOfferModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8806581456407572544L;
	private String auctionId;
	// 竞拍提示价,单位元
	private double auctionPrice;
	// 竞拍类型值，1：提示价，2：一口价, 3:报价区间
	private int auctionType;
	//竞拍类型文本，1.提示价，2.一口价，3：报价区间
	private String auctionTypeText;
	//已参与竞价的人数
	private int bidderNum;
	// 当前时间和竞价发布时间的毫秒差
	private long diffPublish;
	// 当前位置和发货地的距离，单位米
	private int distance;
	// 货物名称
	private String goodsName;
	// 货物总量:吨/立方米
	private double goodsAmount;
	//竞价、派车时的计量单位
	private String assignUnit;
	//竞价、派车时的计量单位文本
	private String assignUnitText;
	//结算单位
	private String settleUnit;
	//结算单位文本
	private String settleUnitText;
	// 收货方地址
	private String recvAddr;
	// 发货方地址
	private String sendAddr;
	// 竞价单状态：3.竞拍中，4已取消，99竞价已结束
	private int status;
	//竞价单状态文本：3.竞拍中，4已取消，99竞价已结束
	private String statusText;
	private List<TruckMode> trucks;
	private List<Statistic> statistic;
	//1.9.4 结算比例list 具体类型 看BillTempDTO
	private List<BillTmpltVo> billTmpltVoList;
	//结算模板2.7.0新增
	private List<SettleModel> billPaymentWayList;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public List<SettleModel> getBillPaymentWayList() {
		return billPaymentWayList;
	}

	public void setBillPaymentWayList(List<SettleModel> billPaymentWayList) {
		this.billPaymentWayList = billPaymentWayList;
	}

	public List<BillTmpltVo> getBillTmpltVoList() {
		return billTmpltVoList;
	}

	public void setBillTmpltVoList(List<BillTmpltVo> billTmpltVoList) {
		this.billTmpltVoList = billTmpltVoList;
	}

	/**
     * 货源来源标签 ture为平台货源 false为外部货源
     */
    private boolean goodsSourceTag;
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
	private String bidIncrement;
	/**
	 * 是否开始接单
	 * 三种类型都有可能有
	 */
	private boolean isBidStart;
	/**
	 * 货物剩余量
	 * 只有类型2有
	 */
	private String goodsAmountSp;
	/**
	 * 是否是内部货源
	 * 1, "内部货源";2, "外部货源"
	 */
	private int               innerGoods;

	private String            innerGoodsTxt;

	/** 竞拍提示价 结算模板拼装字符串*/
	private String                    auctionPriceTemplateStr;

	public String getAuctionPriceTemplateStr() {
		return auctionPriceTemplateStr;
	}

	public void setAuctionPriceTemplateStr(String auctionPriceTemplateStr) {
		this.auctionPriceTemplateStr = auctionPriceTemplateStr;
	}

	public boolean isGoodsSourceTag() {
		return goodsSourceTag;
	}

	public void setGoodsSourceTag(boolean goodsSourceTag) {
		this.goodsSourceTag = goodsSourceTag;
	}

	public String getAuctionTypeText() {
		return auctionTypeText;
	}

	public void setAuctionTypeText(String auctionTypeText) {
		this.auctionTypeText = auctionTypeText;
	}

	public double getGoodsAmount() {
		return goodsAmount;
	}

	public void setGoodsAmount(double goodsAmount) {
		this.goodsAmount = goodsAmount;
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

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setAuctionPrice(double auctionPrice) {
		this.auctionPrice = auctionPrice;
	}

	public List<TruckMode> getTrucks() {
		return trucks;
	}

	public void setTrucks(List<TruckMode> trucks) {
		this.trucks = trucks;
	}

	public String getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
	}

	public double getAuctionPrice() {
		return auctionPrice;
	}

	public void setAuctionPrice(Double auctionPrice) {
		this.auctionPrice = auctionPrice;
	}

	public int getAuctionType() {
		return auctionType;
	}

	public void setAuctionType(int auctionType) {
		this.auctionType = auctionType;
	}

	public int getBidderNum() {
		return bidderNum;
	}

	public void setBidderNum(int bidderNum) {
		this.bidderNum = bidderNum;
	}

	public long getDiffPublish() {
		return diffPublish;
	}

	public void setDiffPublish(long diffPublish) {
		this.diffPublish = diffPublish;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public void setGoodsAmount(long goodsAmount) {
		this.goodsAmount = goodsAmount;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getRecvAddr() {
		return recvAddr;
	}

	public void setRecvAddr(String recvAddr) {
		this.recvAddr = recvAddr;
	}

	public String getSendAddr() {
		return sendAddr;
	}

	public void setSendAddr(String sendAddr) {
		this.sendAddr = sendAddr;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<Statistic> getStatistic() {
		return statistic;
	}

	public void setStatistic(List<Statistic> statistic) {
		this.statistic = statistic;
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

	public String getBidIncrement() {
		return bidIncrement;
	}

	public void setBidIncrement(String bidIncrement) {
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

	public static class TruckMode implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1853355967741736395L;
		// trucks;车辆信息
		private String truckId;
		//载货空间
		private double carryVolume;
		// 最大运载量，单位吨
		private String carryAmount;
		// 车牌号
		private String truckNumber;
		// 车型
		private String truckModelText;
		//车厢类型
		private String truckCarriage;
		// 车长
		private String truckLengthText;
		// 司机ID
		private String driverId;
		// 司机name
		private String driverName;
		//是否允许接单
		public boolean isValid;
		

		public String getTruckId() {
			return truckId;
		}

		public void setTruckId(String truckId) {
			this.truckId = truckId;
		}

		public String getCarryAmount() {
			return carryAmount;
		}

		public void setCarryAmount(String carryAmount) {
			this.carryAmount = carryAmount;
		}

		public String getTruckNumber() {
			return truckNumber;
		}

		public void setTruckNumber(String truckNumber) {
			this.truckNumber = truckNumber;
		}

		public String getTruckModelText() {
			return truckModelText;
		}

		public void setTruckModelText(String truckModelText) {
			this.truckModelText = truckModelText;
		}

		public String getTruckLengthText() {
			return truckLengthText;
		}

		public void setTruckLengthText(String truckLengthText) {
			this.truckLengthText = truckLengthText;
		}

		public String getDriverId() {
			return driverId;
		}

		public void setDriverId(String driverId) {
			this.driverId = driverId;
		}

		public String getDriverName() {
			return driverName;
		}

		public void setDriverName(String driverName) {
			this.driverName = driverName;
		}

		public String getTruckCarriage() {
			return truckCarriage;
		}

		public void setTruckCarriage(String truckCarriage) {
			this.truckCarriage = truckCarriage;
		}

		public double getCarryVolume() {
			return carryVolume;
		}

		public void setCarryVolume(double carryVolume) {
			this.carryVolume = carryVolume;
		}
		
	}

	public class Statistic implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		// 竞价人报价
		String bidPrice;
		// 当前报价人数
		int bidderNum;

		public String getBidPrice() {
			return bidPrice;
		}

		public void setBidPrice(String bidPrice) {
			this.bidPrice = bidPrice;
		}

		public int getBidderNum() {
			return bidderNum;
		}

		public void setBidderNum(int bidderNum) {
			this.bidderNum = bidderNum;
		}

	}
}
