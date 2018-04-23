package com.hongshi.wuliudidi.model;

import com.amap.api.maps2d.model.LatLng;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class GoodsModel implements Serializable{
	private static final long serialVersionUID = -2659304640608239169L;
	//竞价单ID
	private String auctionId;
	//竞拍时间类型
	private int auctionTimeType;
	//竞拍量
	private String bidAmount;
	//参与的竞拍人数
	private int bidderNum;
	//距离竞拍结束时间的毫秒差，若已结束则为0
	private long diffBidEnd;
	//距离竞拍开始时间的毫秒差，若已开始则为0
	private long diffBidStart;
	//当前时间和竞价发布时间的毫秒差
	private long diffPublish;
	//当前位置和发货地的距离，单位米
	private int distance;
	//货物总量:吨/立方米
	private double goodsAmount;
	//货物名称
	private String goodsName;
	//判断是否参与了竞拍
	private boolean hasBidded;
	//收货方地址
	private String recvAddr;
	private String recvCity;
	private String recvProvince;
	//发货方地址
	private String sendAddr;
	//竞价单状态2.竞拍即将开始，3.竞拍中
	private int status;
	private String unit;
	private String unitText;
	//计量单位，1千克2件3立方米4吨
	private String assignUnitText;
	//结算计量单位，1千克2件3立方米4吨
	private String settleUnitText;
	//竞拍起始时间毫秒数
	private long gmtStart;
	//竞拍结束时间毫秒数
	private long gmtEnd;
	//竞拍发布时间毫秒数
	private long gmtPublish;
	//竞拍类型值，1：提示价，2：一口价，3：区间
	private int auctionType;
	//竞拍类型文本，1：提示价，2：一口价，3：区间
	private String auctionTypeText;
	//竞拍参考价-人民币元
	private double auctionPrice;
	//经纬度
	private double lat;
	//经纬度
	private double lng;
	//上一页最后一条记录的ID
	private String last;
	//已阅人数
	private int readAmount;
	private List<String> trukCarriageList;
	private List<String> trukTypeList;
	//运输里程，单位为米
	private int transportDistance;
	//是否要求定金
	private boolean matchSourceTag;
	/**  发货地址--区*/
    private String            sendDis;
    /**  收货方地址--区*/
    private String            recvDis;
	/**
     * 承运期开始时间
     */
    private Date              gmtStartPeriod;
    /**
     * 承运期结束时间
     */
    private Date              gmtEndPeriod;
	/**
	 * 开始竞价时间
	 * 三种类型都有可能有
	 */
	private Date gmtBidStart;
	/**
	 * 最低报价
	 * 只有1,3有
	 */
	private double lowPrice = 3;
	/**
	 * 最高报价
	 * 只有类型1,3有
	 */
	private double highPrice = 5;
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
	 * 是否是内部货源
	 * 1, "内部货源";2, "外部货源"
	 */
	private int               innerGoods;

	private String            innerGoodsTxt;

	private boolean terminalCharges;
	//1.9.4 收货公司ID
	private String recipCopId;
	//1.9.4 发货公司ID
	private String senderCopId;
	//是否支持一键接单
	private int oneKeyRec;
	//是否是后台推送货源
	private boolean pushFlag;

	private String sendLng;
	private String sendLat;
	private String recvLng;
	private String recvLat;

	/** 竞拍提示价 结算模板拼装字符串*/
	private String                    auctionPriceTemplateStr;

	public boolean isPushFlag() {
		return pushFlag;
	}

	public void setPushFlag(boolean pushFlag) {
		this.pushFlag = pushFlag;
	}

	public String getAuctionPriceTemplateStr() {
		return auctionPriceTemplateStr;
	}

	public void setAuctionPriceTemplateStr(String auctionPriceTemplateStr) {
		this.auctionPriceTemplateStr = auctionPriceTemplateStr;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getSendLng() {
		return sendLng;
	}

	public void setSendLng(String sendLng) {
		this.sendLng = sendLng;
	}

	public String getSendLat() {
		return sendLat;
	}

	public void setSendLat(String sendLat) {
		this.sendLat = sendLat;
	}

	public String getRecvLng() {
		return recvLng;
	}

	public void setRecvLng(String recvLng) {
		this.recvLng = recvLng;
	}

	public String getRecvLat() {
		return recvLat;
	}

	public void setRecvLat(String recvLat) {
		this.recvLat = recvLat;
	}

	public int getOneKeyRec() {
		return oneKeyRec;
	}

	public void setOneKeyRec(int oneKeyRec) {
		this.oneKeyRec = oneKeyRec;
	}

	public String getRecipCopId() {
		return recipCopId;
	}

	public void setRecipCopId(String recipCopId) {
		this.recipCopId = recipCopId;
	}

	public String getSenderCopId() {
		return senderCopId;
	}

	public void setSenderCopId(String senderCopId) {
		this.senderCopId = senderCopId;
	}

	public boolean isTerminalCharges() {
		return terminalCharges;
	}

	public void setTerminalCharges(boolean terminalCharges) {
		this.terminalCharges = terminalCharges;
	}

	public Date getGmtStartPeriod() {
		return gmtStartPeriod;
	}
	public void setGmtStartPeriod(Date gmtStartPeriod) {
		this.gmtStartPeriod = gmtStartPeriod;
	}
	public Date getGmtEndPeriod() {
		return gmtEndPeriod;
	}
	public void setGmtEndPeriod(Date gmtEndPeriod) {
		this.gmtEndPeriod = gmtEndPeriod;
	}
	public String getSettleUnitText() {
		return settleUnitText;
	}
	public void setSettleUnitText(String settleUnitText) {
		this.settleUnitText = settleUnitText;
	}
	public String getLast() {
		return last;
	}
	public void setLast(String last) {
		this.last = last;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getAuctionId() {
		return auctionId;
	}
	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
	}
	public int getAuctionTimeType() {
		return auctionTimeType;
	}
	public void setAuctionTimeType(int auctionTimeType) {
		this.auctionTimeType = auctionTimeType;
	}
	public String getBidAmount() {
		return bidAmount;
	}
	public void setBidAmount(String bidAmount) {
		this.bidAmount = bidAmount;
	}
	public int getBidderNum() {
		return bidderNum;
	}
	public void setBidderNum(int bidderNum) {
		this.bidderNum = bidderNum;
	}
	public long getDiffBidEnd() {
		return diffBidEnd;
	}
	public void setDiffBidEnd(long diffBidEnd) {
		this.diffBidEnd = diffBidEnd;
	}
	public long getDiffBidStart() {
		return diffBidStart;
	}
	public void setDiffBidStart(long diffBidStart) {
		this.diffBidStart = diffBidStart;
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
	public double getGoodsAmount() {
		return goodsAmount;
	}
	public void setGoodsAmount(double goodsAmount) {
		this.goodsAmount = goodsAmount;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public boolean isHasBidded() {
		return hasBidded;
	}
	public void setHasBidded(boolean hasBidded) {
		this.hasBidded = hasBidded;
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
	public String getAssignUnitText() {
		return assignUnitText;
	}
	public void setAssignUnitText(String assignUnitText) {
		this.assignUnitText = assignUnitText;
	}
	public long getGmtStart() {
		return gmtStart;
	}
	public void setGmtStart(long gmtStart) {
		this.gmtStart = gmtStart;
	}
	public long getGmtEnd() {
		return gmtEnd;
	}
	public void setGmtEnd(long gmtEnd) {
		this.gmtEnd = gmtEnd;
	}
	public long getGmtPublish() {
		return gmtPublish;
	}
	public void setGmtPublish(long gmtPublish) {
		this.gmtPublish = gmtPublish;
	}
	public int getAuctionType() {
		return auctionType;
	}
	public void setAuctionType(int auctionType) {
		this.auctionType = auctionType;
	}
	public double getAuctionPrice() {
		return auctionPrice;
	}
	public void setAuctionPrice(double auctionPrice) {
		this.auctionPrice = auctionPrice;
	}
	public String getAuctionTypeText() {
		return auctionTypeText;
	}
	public void setAuctionTypeText(String auctionTypeText) {
		this.auctionTypeText = auctionTypeText;
	}
	public LatLng getLatLng(){
		return new LatLng(lat, lng);
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
	public String getUnitText() {
		return unitText;
	}
	public void setUnitText(String unitText) {
		this.unitText = unitText;
	}
	public String getSendDis() {
		return sendDis;
	}
	public void setSendDis(String sendDis) {
		this.sendDis = sendDis;
	}
	public String getRecvDis() {
		return recvDis;
	}
	public void setRecvDis(String recvDis) {
		this.recvDis = recvDis;
	}
	public int getReadAmount() {
		return readAmount;
	}
	public void setReadAmount(int readAmount) {
		this.readAmount = readAmount;
	}
	public List<String> getTrukCarriageList() {
		return trukCarriageList;
	}
	public void setTrukCarriageList(List<String> trukCarriageList) {
		this.trukCarriageList = trukCarriageList;
	}
	public List<String> getTrukTypeList() {
		return trukTypeList;
	}
	public void setTrukTypeList(List<String> trukTypeList) {
		this.trukTypeList = trukTypeList;
	}
	public int getTransportDistance() {
		return transportDistance;
	}
	public void setTransportDistance(int transportDistance) {
		this.transportDistance = transportDistance;
	}

	public boolean isMatchSourceTag() {
		return matchSourceTag;
	}

	public void setMatchSourceTag(boolean matchSourceTag) {
		this.matchSourceTag = matchSourceTag;
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
}
