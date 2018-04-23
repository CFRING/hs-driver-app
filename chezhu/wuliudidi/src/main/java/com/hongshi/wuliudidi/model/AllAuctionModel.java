package com.hongshi.wuliudidi.model;

import java.io.Serializable;

/**
 * 托运单基本信息（竞价条目审核通过后形成托运单），非竞价人的承运司机能看到的信息
 * 
 * @author haiyang.jiang  
 * @version $Id: BidItemTaskBaseDTO.java, v 0.1 2015年8月10日 下午3:33:06 niya Exp $
 */
public class AllAuctionModel implements Serializable {
    private static final long serialVersionUID = -213946700959100347L;

    //竞价条目ID
    private String            bidItemId;

    //状态：1未进行，2进行中
    private int               status;
    //状态：1未进行，2进行中
    private String            statusText;

    //发货方地址
    private String            sendAddr;

    //收货方地址
    private String            recvAddr;

    //货物名
    private String            goodsName;

    //货物量
    private double            goodsAmount;

    //货物量单位
    private int               unit;
    private String            unitText;
    //竞价/派车单位文本：车/吨/立方米/件
    private String            assignUnitText;
    //结算单位text：'T':'吨','M3':'立方米','PIECE':'件'
	private String            settleUnitText;

    //    中标价
    private double              bidPrice;
    //竞价单ID
    private String auctionId;

    public String getAssignUnitText() {
		return assignUnitText;
	}

	public void setAssignUnitText(String assignUnitText) {
		this.assignUnitText = assignUnitText;
	}

	public String getSettleUnitText() {
		return settleUnitText;
	}

	public void setSettleUnitText(String settleUnitText) {
		this.settleUnitText = settleUnitText;
	}
    public String getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
	}

	public String getBidItemId() {
        return bidItemId;
    }

    public void setBidItemId(String bidItemId) {
        this.bidItemId = bidItemId;
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public double getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(double goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getUnitText() {
        return unitText;
    }

    public void setUnitText(String unitText) {
        this.unitText = unitText;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }
}
