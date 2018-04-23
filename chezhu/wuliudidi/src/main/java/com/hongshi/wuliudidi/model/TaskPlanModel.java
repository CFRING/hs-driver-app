package com.hongshi.wuliudidi.model;

import java.util.Date;

public class TaskPlanModel {
	private String	assignUnit;
	//运输计划 货物量单位
	private String	assignUnitText;
	
	private double	finalAmount;
	//bidItem 的最终竞拍量
	
	private Date	gmtCreate;
	private Date	gmtEnd;
	//运输计划到货时间
	
	private Date	gmtStart;
	//运输计划发货时间
	
	private String	goodsName;
	//货物名称
	
	private boolean	goodsSourceTag;
	// 平台业务True,内部业务 False
	
	private String	outBizId;
	//派车计划或者派车任务 的 ID
	
	private int	outBizType;
	//任务的类型 1=派车计划，2=派车任务
	private String	outBizTypeTxt;
	//派车计划=派车计划，派车任务=派车任务
	
	private double	planAmount;
	//运输计划 货物量
	
	private double	realAmount;
	//运输计划 已经运输量
	
	private String	recipientCity;
	//收货方-市
	private String	recipientDistrict;
	//收货方-区
	private String	recipientPhone;
	//收货人电话
	private String	senderCity;
	//发货方-市
	private String	senderDistrict;
	//发货方-区
	private String	senderPhone;
	//发货人电话
	private String	settleUnit;
	//结算单位
	private String	settleUnitText;
	//结算单位
	private int	status;
	//任务的状态（ 参照派车计划和派车任务的相关状态） 派车计划：1.未派车 2.派车完成 3.超时 4.部分派车
	//派车单：1.待运输 2.运输中 3.已签收 4.等待复核 5.复核通过 6.货主驳回 7.调解通过 8.复核超时
	
	private String	statusTxt;
	//任务状态的文字
	private String	truckNO;
	//车辆的车牌号码

	private String salerBizID;

	/**  2.2 是否已经点击我要出发 true 已经点击，false未点击*/
	private boolean           hasDelivery;

	private boolean outLib;

	public boolean isOutLib() {
		return outLib;
	}

	public void setOutLib(boolean outLib) {
		this.outLib = outLib;
	}

	public boolean isHasDelivery() {
		return hasDelivery;
	}

	public void setHasDelivery(boolean hasDelivery) {
		this.hasDelivery = hasDelivery;
	}

	public String getSalerBizID() {
		return salerBizID;
	}

	public void setSalerBizID(String salerBizID) {
		this.salerBizID = salerBizID;
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
	public double getFinalAmount() {
		return finalAmount;
	}
	public void setFinalAmount(double finalAmount) {
		this.finalAmount = finalAmount;
	}
	public Date getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public Date getGmtEnd() {
		return gmtEnd;
	}
	public void setGmtEnd(Date gmtEnd) {
		this.gmtEnd = gmtEnd;
	}
	public Date getGmtStart() {
		return gmtStart;
	}
	public void setGmtStart(Date gmtStart) {
		this.gmtStart = gmtStart;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public boolean isGoodsSourceTag() {
		return goodsSourceTag;
	}
	public void setGoodsSourceTag(boolean goodsSourceTag) {
		this.goodsSourceTag = goodsSourceTag;
	}
	public String getOutBizId() {
		return outBizId;
	}
	public void setOutBizId(String outBizId) {
		this.outBizId = outBizId;
	}
	public int getOutBizType() {
		return outBizType;
	}
	public void setOutBizType(int outBizType) {
		this.outBizType = outBizType;
	}
	public String getOutBizTypeTxt() {
		return outBizTypeTxt;
	}
	public void setOutBizTypeTxt(String outBizTypeTxt) {
		this.outBizTypeTxt = outBizTypeTxt;
	}
	public double getPlanAmount() {
		return planAmount;
	}
	public void setPlanAmount(double planAmount) {
		this.planAmount = planAmount;
	}
	public double getRealAmount() {
		return realAmount;
	}
	public void setRealAmount(double realAmount) {
		this.realAmount = realAmount;
	}
	public String getRecipientCity() {
		return recipientCity;
	}
	public void setRecipientCity(String recipientCity) {
		this.recipientCity = recipientCity;
	}
	public String getRecipientDistrict() {
		return recipientDistrict;
	}
	public void setRecipientDistrict(String recipientDistrict) {
		this.recipientDistrict = recipientDistrict;
	}
	public String getRecipientPhone() {
		return recipientPhone;
	}
	public void setRecipientPhone(String recipientPhone) {
		this.recipientPhone = recipientPhone;
	}
	public String getSenderCity() {
		return senderCity;
	}
	public void setSenderCity(String senderCity) {
		this.senderCity = senderCity;
	}
	public String getSenderDistrict() {
		return senderDistrict;
	}
	public void setSenderDistrict(String senderDistrict) {
		this.senderDistrict = senderDistrict;
	}
	public String getSenderPhone() {
		return senderPhone;
	}
	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStatusTxt() {
		return statusTxt;
	}
	public void setStatusTxt(String statusTxt) {
		this.statusTxt = statusTxt;
	}
	public String getTruckNO() {
		return truckNO;
	}
	public void setTruckNO(String truckNO) {
		this.truckNO = truckNO;
	}
	
}
