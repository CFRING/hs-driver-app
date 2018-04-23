package com.hongshi.wuliudidi.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RejectModel {
	//运输任务单据复核ID
	private String Id;
	//运输任务ID
	private String taskId;
	//驳回原因。1净重不符 2过磅单不清晰 3签收单不清晰 99其它
	private List<String> refuseType = new ArrayList<String>();
	//复核备注
	private String reviewRemark;
	//实际运量
	private double realAmount;
	//驳回时间
	private Date gmtReview;
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public List<String> getRefuseType() {
		return refuseType;
	}
	public void setRefuseType(List<String> refuseType) {
		this.refuseType = refuseType;
	}
	public String getReviewRemark() {
		return reviewRemark;
	}
	public void setReviewRemark(String reviewRemark) {
		this.reviewRemark = reviewRemark;
	}
	public double getRealAmount() {
		return realAmount;
	}
	public void setRealAmount(double realAmount) {
		this.realAmount = realAmount;
	}
	public Date getGmtReview() {
		return gmtReview;
	}
	public void setGmtReview(Date gmtReview) {
		this.gmtReview = gmtReview;
	}
	
}
