package com.hongshi.wuliudidi.model;

import java.util.Date;

public class ReceiptReviewModel {
	private String	creator;
	//创建人
	
	private Date	gmtCreate;
	//创建时间
	
	private Date	gmtPlatform;
	//平台介入时间
	
	private Date	gmtReview;
	//审核时间（驳回时间）
	
	private String	id;
	//主键
	
	private String	platformRemark;
	//平台处理备注
	
	private String	platformUser;
	//平台小二Id
	
	private int	platfromResultType;
	//平台处理方式
	
	private String	platfromResultTypeText;
	//平台处理方式
	
	private double	realAmount;
	//实际运量
	
	private String[]	refuseType;
	//驳回原因
	
	private String	reviewer;
	//审核人
	
	private String	reviewRemark;
	//审核备注
	
	private int	reviewResult;
	//单据状态
	
	private String	reviewResultText;
	//单据状态
	
	private String	signupRemark;
	//签收备注
	
	private String	signupVoucherPic;
	//签收单图片
	
	private String	taskId;
	//运输任务ID
	
	private String	weighVouvherPic;
	//过磅单图片
	
	
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getPlatfromResultType() {
		return platfromResultType;
	}
	public void setPlatfromResultType(int platfromResultType) {
		this.platfromResultType = platfromResultType;
	}
	public double getRealAmount() {
		return realAmount;
	}
	public void setRealAmount(double realAmount) {
		this.realAmount = realAmount;
	}
	public int getReviewResult() {
		return reviewResult;
	}
	public void setReviewResult(int reviewResult) {
		this.reviewResult = reviewResult;
	}
	public String getReviewResultText() {
		return reviewResultText;
	}
	public void setReviewResultText(String reviewResultText) {
		this.reviewResultText = reviewResultText;
	}
	public String getSignupVoucherPic() {
		return signupVoucherPic;
	}
	public void setSignupVoucherPic(String signupVoucherPic) {
		this.signupVoucherPic = signupVoucherPic;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getWeighVouvherPic() {
		return weighVouvherPic;
	}
	public void setWeighVouvherPic(String weighVouvherPic) {
		this.weighVouvherPic = weighVouvherPic;
	}
	public Date getGmtPlatform() {
		return gmtPlatform;
	}
	public void setGmtPlatform(Date gmtPlatform) {
		this.gmtPlatform = gmtPlatform;
	}
	public Date getGmtReview() {
		return gmtReview;
	}
	public void setGmtReview(Date gmtReview) {
		this.gmtReview = gmtReview;
	}
	public String getPlatformRemark() {
		return platformRemark;
	}
	public void setPlatformRemark(String platformRemark) {
		this.platformRemark = platformRemark;
	}
	public String getPlatformUser() {
		return platformUser;
	}
	public void setPlatformUser(String platformUser) {
		this.platformUser = platformUser;
	}
	public String getPlatfromResultTypeText() {
		return platfromResultTypeText;
	}
	public void setPlatfromResultTypeText(String platfromResultTypeText) {
		this.platfromResultTypeText = platfromResultTypeText;
	}
	public String[] getRefuseType() {
		return refuseType;
	}
	public void setRefuseType(String[] refuseType) {
		this.refuseType = refuseType;
	}
	public String getReviewer() {
		return reviewer;
	}
	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}
	public String getReviewRemark() {
		return reviewRemark;
	}
	public void setReviewRemark(String reviewRemark) {
		this.reviewRemark = reviewRemark;
	}
	public String getSignupRemark() {
		return signupRemark;
	}
	public void setSignupRemark(String signupRemark) {
		this.signupRemark = signupRemark;
	}
}
