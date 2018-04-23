package com.hongshi.wuliudidi.model;

public class TruckTypeModel {
	//唯一编号
	private String truckModelId;
	//名字
	private String name;
	private String length;
	//车牌
	private String truckNumber;
	//车辆id
	private String truckId;
	//用户
	private String userId;
	//最大载重量
	private double carryCapacity;
	//最大载体积量
	private double carryVolume;
	//车长ID
	private String truckLengthId;
	//车长描述
	private String truckLengthText;
	//车辆类型Id
	private String truckTypeId;
	//车辆类型描述
	private String truckTypeText;
	//行驶证照片
	private String truckLicensePhoto;
	//车头照片
	private String frontTruckPhoto;
	//车身照片
	private String middleTruckPhoto;
	//车尾照片
	private String backTruckPhoto;
	//道路运输证照片
	private String roadTransportPhoto;
	//道路运输证照片背面
	private String backRoadTransportPhoto;
	//行驶证审核状态
	private int truckLicenseAuditStatus;
	//车头审核状态
	private int frontAuditStatus;
	//车身审核状态
	private int middleAuditStatus;
	//车尾审核状态
	private int backAuditStatus;
	//道路运输证审核状态
	private int roadAuditStatus;
	//审核状态
	private int status;
	//审核不通过的原因
	private String auditNoteTypeText;
	public String getBackRoadTransportPhoto() {
		return backRoadTransportPhoto;
	}
	public void setBackRoadTransportPhoto(String backRoadTransportPhoto) {
		this.backRoadTransportPhoto = backRoadTransportPhoto;
	}
	public String getTruckNumber() {
		return truckNumber;
	}
	public void setTruckNumber(String truckNumber) {
		this.truckNumber = truckNumber;
	}
	public String getTruckId() {
		return truckId;
	}
	public void setTruckId(String truckId) {
		this.truckId = truckId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public double getCarryCapacity() {
		return carryCapacity;
	}
	public void setCarryCapacity(double carryCapacity) {
		this.carryCapacity = carryCapacity;
	}
	public double getCarryVolume() {
		return carryVolume;
	}
	public void setCarryVolume(double carryVolume) {
		this.carryVolume = carryVolume;
	}
	public String getTruckLengthText() {
		return truckLengthText;
	}
	public void setTruckLengthText(String truckLengthText) {
		this.truckLengthText = truckLengthText;
	}
	public String getTruckTypeId() {
		return truckTypeId;
	}
	public void setTruckTypeId(String truckTypeId) {
		this.truckTypeId = truckTypeId;
	}
	
	public String getTruckTypeText() {
		return truckTypeText;
	}
	public void setTruckTypeText(String truckTypeText) {
		this.truckTypeText = truckTypeText;
	}
	public String getTruckLicensePhoto() {
		return truckLicensePhoto;
	}
	public void setTruckLicensePhoto(String truckLicensePhoto) {
		this.truckLicensePhoto = truckLicensePhoto;
	}
	public String getFrontTruckPhoto() {
		return frontTruckPhoto;
	}
	public void setFrontTruckPhoto(String frontTruckPhoto) {
		this.frontTruckPhoto = frontTruckPhoto;
	}
	public String getMiddleTruckPhoto() {
		return middleTruckPhoto;
	}
	public void setMiddleTruckPhoto(String middleTruckPhoto) {
		this.middleTruckPhoto = middleTruckPhoto;
	}
	public String getBackTruckPhoto() {
		return backTruckPhoto;
	}
	public void setBackTruckPhoto(String backTruckPhoto) {
		this.backTruckPhoto = backTruckPhoto;
	}
	public String getRoadTransportPhoto() {
		return roadTransportPhoto;
	}
	public void setRoadTransportPhoto(String roadTransportPhoto) {
		this.roadTransportPhoto = roadTransportPhoto;
	}
	public int getTruckLicenseAuditStatus() {
		return truckLicenseAuditStatus;
	}
	public void setTruckLicenseAuditStatus(int truckLicenseAuditStatus) {
		this.truckLicenseAuditStatus = truckLicenseAuditStatus;
	}
	public int getFrontAuditStatus() {
		return frontAuditStatus;
	}
	public void setFrontAuditStatus(int frontAuditStatus) {
		this.frontAuditStatus = frontAuditStatus;
	}
	public int getMiddleAuditStatus() {
		return middleAuditStatus;
	}
	public void setMiddleAuditStatus(int middleAuditStatus) {
		this.middleAuditStatus = middleAuditStatus;
	}
	public int getBackAuditStatus() {
		return backAuditStatus;
	}
	public void setBackAuditStatus(int backAuditStatus) {
		this.backAuditStatus = backAuditStatus;
	}
	public int getRoadAuditStatus() {
		return roadAuditStatus;
	}
	public void setRoadAuditStatus(int roadAuditStatus) {
		this.roadAuditStatus = roadAuditStatus;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTruckLengthId() {
		return truckLengthId;
	}
	public void setTruckLengthId(String truckLengthId) {
		this.truckLengthId = truckLengthId;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getTruckModelId() {
		return truckModelId;
	}
	public void setTruckModelId(String truckModelId) {
		this.truckModelId = truckModelId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuditNoteTypeText() {
		return auditNoteTypeText;
	}
	public void setAuditNoteTypeText(String auditNoteTypeText) {
		this.auditNoteTypeText = auditNoteTypeText;
	}
	
}
