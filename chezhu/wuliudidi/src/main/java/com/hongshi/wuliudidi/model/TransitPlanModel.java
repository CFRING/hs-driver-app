package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 运输计划列表中的一条记录
 * 
 * @author haiyang.jiang  
 * @version $Id: TransitPlanRecordVO.java, v 0.1 2015年8月20日 下午4:18:26 niya Exp $
 */
public class TransitPlanModel implements Serializable {
    private static final long serialVersionUID = -3251916745565755683L;

    //	运输计划ID
    private String            planId;

    //	运输计划创建时间
    private Date              planGmtCreate;

    //	竞价条目ID
    private String            bidItemId;

    //	发货地
    private String            sendAddr;

    //	收货地
    private String            recvAddr;

    //	货物名
    private String            goodsName;

    //	运输计划 货物量
    private String            planAmount;

    //	运输计划 货物量单位
    private String            assignUnit;

	private String            assignUnitText;

	//	运输计划发货时间
    private Date              gmtStart;

    //	运输计划到货时间
    private Date              gmtEnd;

    //状态， 1.未派车 2.已派车 3.派车超时
    private int               status;
    private String            statusText;
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
    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public Date getPlanGmtCreate() {
        return planGmtCreate;
    }

    public void setPlanGmtCreate(Date planGmtCreate) {
        this.planGmtCreate = planGmtCreate;
    }

    public String getBidItemId() {
        return bidItemId;
    }

    public void setBidItemId(String bidItemId) {
        this.bidItemId = bidItemId;
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

    public String getPlanAmount() {
        return planAmount;
    }

    public void setPlanAmount(String planAmount) {
        this.planAmount = planAmount;
    }

    public Date getGmtStart() {
        return gmtStart;
    }

    public void setGmtStart(Date gmtStart) {
        this.gmtStart = gmtStart;
    }

    public Date getGmtEnd() {
        return gmtEnd;
    }

    public void setGmtEnd(Date gmtEnd) {
        this.gmtEnd = gmtEnd;
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
}
