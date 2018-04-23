package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConsignDetailModel implements Serializable {
    private static final long serialVersionUID = 2385546301895525937L;
    //竞价单ID
    private String            auctionId;

    //发货地
    private String            sendAddr;

    //收货地
    private String            recvAddr;

    //货物名
    private String            goodsName;
    //竞价单的货物总量
    private double            goodsAmount;

    //竞价条目ID
    private String            bidItemId;

    //计划总运量
    private double            totalAmount;

    //已运量
    private double            finishedAmount;

    //剩余量
    private double            restAmount;
    //实际结算运量
    private double            settleAmount;

	/**
     * 货物-数量：个/件/袋....
     */
    private int               goodsCount;

    /**
     * 货物-数量类型，1：袋，2：件，3：箱，4：盒
     */
    private int               goodsCountType;
    private String            goodsCountTypeText;

    //货物量单位（外）：车/吨/立方米/件
    private String            assignUnitText;
    //'TRUCK':'车'；'T':'吨','M3'；'立方米','PIECE':'件'
    private String            assignUnit;

	//托运单状态1待审核, 2审核中, 3取消, 4未通过, 5运输未开始, 6运输中, 7已完成
    private int               shipmentStatus;
    private String            shipmentStatusText;

    //审核通过时间
    private Date              gmtAudit;
    //托运单完成时间
    private Date			  gmtFinish;

    //中标价
    private double              bidPrice;
    //结算单位
    private String settleUnitText;
    //
    private String           billTempalte;

	//派车单列表，包含是否已是最后一页信息
    private AppListVO         transitTaskList;
    private List<TaskOrderModel>   planList   = new ArrayList<TaskOrderModel>();
    

	public List<TaskOrderModel> getPlanList() {
		return planList;
	}

	public void setPlanList(List<TaskOrderModel> planList) {
		this.planList = planList;
	}

	public double getSettleAmount() {
		return settleAmount;
	}

	public void setSettleAmount(double settleAmount) {
		this.settleAmount = settleAmount;
	}

    public String getAssignUnit() {
		return assignUnit;
	}

	public void setAssignUnit(String assignUnit) {
		this.assignUnit = assignUnit;
	}
    public String getSettleUnitText() {
		return settleUnitText;
	}

	public void setSettleUnitText(String settleUnitText) {
		this.settleUnitText = settleUnitText;
	}
    public String getAssignUnitText() {
		return assignUnitText;
	}

	public void setAssignUnitText(String assignUnitText) {
		this.assignUnitText = assignUnitText;
	}

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
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

    public String getBidItemId() {
        return bidItemId;
    }

    public void setBidItemId(String bidItemId) {
        this.bidItemId = bidItemId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getFinishedAmount() {
        return finishedAmount;
    }

    public void setFinishedAmount(double finishedAmount) {
        this.finishedAmount = finishedAmount;
    }

    public double getRestAmount() {
        return restAmount;
    }

    public void setRestAmount(double restAmount) {
        this.restAmount = restAmount;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public int getGoodsCountType() {
        return goodsCountType;
    }

    public void setGoodsCountType(int goodsCountType) {
        this.goodsCountType = goodsCountType;
    }

    public String getGoodsCountTypeText() {
        return goodsCountTypeText;
    }

    public void setGoodsCountTypeText(String goodsCountTypeText) {
        this.goodsCountTypeText = goodsCountTypeText;
    }

    public int getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentStatus(int shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    public String getShipmentStatusText() {
        return shipmentStatusText;
    }

    public void setShipmentStatusText(String shipmentStatusText) {
        this.shipmentStatusText = shipmentStatusText;
    }

    public Date getGmtAudit() {
        return gmtAudit;
    }

    public void setGmtAudit(Date gmtAudit) {
        this.gmtAudit = gmtAudit;
    }
    
    public Date getGmtFinish() {
		return gmtFinish;
	}

	public void setGmtFinish(Date gmtFinish) {
		this.gmtFinish = gmtFinish;
	}

	public AppListVO getTransitTaskList() {
        return transitTaskList;
    }

    public void setTransitTaskList(AppListVO transitTaskList) {
        this.transitTaskList = transitTaskList;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }

	public String getBillTempalte() {
		return billTempalte;
	}

	public void setBillTempalte(String billTempalte) {
		this.billTempalte = billTempalte;
	}

}
