package com.hongshi.wuliudidi.model;

public class TruckAuthModel {
	private String backAuditNote;
	//车尾照片审核备注 
	private int backAuditStatus;
	//车尾照片审核状态 1---审核中 2---审核不通过 3---审核通过
	private String backAuditStatusText;
	
	private String backRoadTransportPhoto;
	//道路运输证背面照片
	
	private String backTruckLicensePhoto;
	//行驶证背面照片
	 
	private String backTruckPhoto;
	//车尾照片
	 
	private double carryCapacity;
	//载重
	 
	private String carryCapacityUnitText;
	//载重单位
	
	private double carryVolume;
	//最大载货体积量
	 
	private String carryVolumeUnitText;
	private String frontAuditNote;
	//车头照片审核备注
	 
	private int frontAuditStatus;
	//车头照片审核状态 1---审核中 2---审核不通过 3---审核通过
	 
	private String frontAuditStatusText;
	private String frontTruckPhoto;
	//车头照片
	 
	private long gmtCreate;
	private long gmtModified;
	private boolean hasGuarantyMoneyAuth;
	//是否有保证金认证
	 
	private boolean hasRoadTransportAuth;
	//是否有道路运输证认证
	 
	private String middleAuditNote;
	//车身照片审核备注
	 
	private int middleAuditStatus;
	//车身照片审核状态 1---审核中 2---审核不通过 3---审核通过
	 
	private String middleAuditStatusText;
	private String middleTruckPhoto;
	//车身照片
	 
	private String note;
	//一次性审核的备注
	 
	private String roadAuditNote;
	//审核备注
	 
	private int roadAuditStatus;
	//照片审核状态
	 
	private String roadAuditStatusText;
	private String roadTransportPhoto;
	//道路运输许可证照片
	 
	private int truckCarriage;
	//车厢类型
	 
	private String truckCarriageText;
	private String truckId;
	//车辆id
	 
	private int truckLength;
	//车长
	 
	private String truckLengthText;
	private String truckLicenseAuditNote;
	//行驶证照片审核备注
	 
	private int truckLicenseAuditStatus;
	//行驶证照片审核状态 1---审核中 2---审核不通过 3---审核通过
	 
	private String truckLicenseAuditStatusText;
	private String frontTruckLicensePhoto;
	//行驶证照片
	 
	private String truckNumber;
	//车牌号码
	 
	private int truckType;
	//车辆类型表的id
	 
	private String truckTypeText;
	private String userId;
	//用户id 
	
	private String auditNoteTypeText;
	//未通过原因
	 
	private boolean isDump;
	//是否自卸车
	 
	private String isDumpText;
	//1：审核中  3:审核通过  4:待完善
	private int status;
	//总体的审核状态
	 
	private String statusText;
	private String truckNumberLetter;
	//车牌号码后面的数字字母
	 
	private String truckNumberProvince;
	//车牌号码前面两个字 

	private String truckNumberMidd;
	//农用车车牌号的中间两位
	
	
	
	public String getBackAuditNote() {
		return backAuditNote;
	}
	public void setBackAuditNote(String backAuditNote) {
		this.backAuditNote = backAuditNote;
	}
	public int getBackAuditStatus() {
		return backAuditStatus;
	}
	public void setBackAuditStatus(int backAuditStatus) {
		this.backAuditStatus = backAuditStatus;
	}
	public String getBackAuditStatusText() {
		return backAuditStatusText;
	}
	public void setBackAuditStatusText(String backAuditStatusText) {
		this.backAuditStatusText = backAuditStatusText;
	}
	public String getBackRoadTransportPhoto() {
		return backRoadTransportPhoto;
	}
	public void setBackRoadTransportPhoto(String backRoadTransportPhoto) {
		this.backRoadTransportPhoto = backRoadTransportPhoto;
	}
	public String getBackTruckLicensePhoto() {
		return backTruckLicensePhoto;
	}
	public void setBackTruckLicensePhoto(String backTruckLicensePhoto) {
		this.backTruckLicensePhoto = backTruckLicensePhoto;
	}
	public String getBackTruckPhoto() {
		return backTruckPhoto;
	}
	public void setBackTruckPhoto(String backTruckPhoto) {
		this.backTruckPhoto = backTruckPhoto;
	}
	public double getCarryCapacity() {
		return carryCapacity;
	}
	public void setCarryCapacity(double carryCapacity) {
		this.carryCapacity = carryCapacity;
	}
	public String getCarryCapacityUnitText() {
		return carryCapacityUnitText;
	}
	public void setCarryCapacityUnitText(String carryCapacityUnitText) {
		this.carryCapacityUnitText = carryCapacityUnitText;
	}
	public double getCarryVolume() {
		return carryVolume;
	}
	public void setCarryVolume(double carryVolume) {
		this.carryVolume = carryVolume;
	}
	public String getCarryVolumeUnitText() {
		return carryVolumeUnitText;
	}
	public void setCarryVolumeUnitText(String carryVolumeUnitText) {
		this.carryVolumeUnitText = carryVolumeUnitText;
	}
	public String getFrontAuditNote() {
		return frontAuditNote;
	}
	public void setFrontAuditNote(String frontAuditNote) {
		this.frontAuditNote = frontAuditNote;
	}
	public int getFrontAuditStatus() {
		return frontAuditStatus;
	}
	public void setFrontAuditStatus(int frontAuditStatus) {
		this.frontAuditStatus = frontAuditStatus;
	}
	public String getFrontAuditStatusText() {
		return frontAuditStatusText;
	}
	public void setFrontAuditStatusText(String frontAuditStatusText) {
		this.frontAuditStatusText = frontAuditStatusText;
	}
	public String getFrontTruckPhoto() {
		return frontTruckPhoto;
	}
	public void setFrontTruckPhoto(String frontTruckPhoto) {
		this.frontTruckPhoto = frontTruckPhoto;
	}
	public long getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(long gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public long getGmtModified() {
		return gmtModified;
	}
	public void setGmtModified(long gmtModified) {
		this.gmtModified = gmtModified;
	}
	public boolean isHasGuarantyMoneyAuth() {
		return hasGuarantyMoneyAuth;
	}
	public void setHasGuarantyMoneyAuth(boolean hasGuarantyMoneyAuth) {
		this.hasGuarantyMoneyAuth = hasGuarantyMoneyAuth;
	}
	public boolean isHasRoadTransportAuth() {
		return hasRoadTransportAuth;
	}
	public void setHasRoadTransportAuth(boolean hasRoadTransportAuth) {
		this.hasRoadTransportAuth = hasRoadTransportAuth;
	}
	public java.lang.String getMiddleAuditNote() {
		return middleAuditNote;
	}
	public void setMiddleAuditNote(java.lang.String middleAuditNote) {
		this.middleAuditNote = middleAuditNote;
	}
	public int getMiddleAuditStatus() {
		return middleAuditStatus;
	}
	public void setMiddleAuditStatus(int middleAuditStatus) {
		this.middleAuditStatus = middleAuditStatus;
	}
	public java.lang.String getMiddleAuditStatusText() {
		return middleAuditStatusText;
	}
	public void setMiddleAuditStatusText(java.lang.String middleAuditStatusText) {
		this.middleAuditStatusText = middleAuditStatusText;
	}
	public java.lang.String getMiddleTruckPhoto() {
		return middleTruckPhoto;
	}
	public void setMiddleTruckPhoto(java.lang.String middleTruckPhoto) {
		this.middleTruckPhoto = middleTruckPhoto;
	}
	public java.lang.String getNote() {
		return note;
	}
	public void setNote(java.lang.String note) {
		this.note = note;
	}
	public java.lang.String getRoadAuditNote() {
		return roadAuditNote;
	}
	public void setRoadAuditNote(java.lang.String roadAuditNote) {
		this.roadAuditNote = roadAuditNote;
	}
	public int getRoadAuditStatus() {
		return roadAuditStatus;
	}
	public void setRoadAuditStatus(int roadAuditStatus) {
		this.roadAuditStatus = roadAuditStatus;
	}
	public java.lang.String getRoadAuditStatusText() {
		return roadAuditStatusText;
	}
	public void setRoadAuditStatusText(java.lang.String roadAuditStatusText) {
		this.roadAuditStatusText = roadAuditStatusText;
	}
	public java.lang.String getRoadTransportPhoto() {
		return roadTransportPhoto;
	}
	public void setRoadTransportPhoto(java.lang.String roadTransportPhoto) {
		this.roadTransportPhoto = roadTransportPhoto;
	}
	public int getTruckCarriage() {
		return truckCarriage;
	}
	public void setTruckCarriage(int truckCarriage) {
		this.truckCarriage = truckCarriage;
		this.truckCarriage -= 1;//后台的车型的值的区间为[2, 4],1为“不限”
	}
	public String getTruckCarriageText() {
		return truckCarriageText;
	}
	public void setTruckCarriageText(String truckCarriageText) {
		this.truckCarriageText = truckCarriageText;
	}
	public String getTruckId() {
		return truckId;
	}
	public void setTruckId(String truckId) {
		this.truckId = truckId;
	}
	public int getTruckLength() {
		return truckLength;
	}
	public void setTruckLength(int truckLength) {
		this.truckLength = truckLength;
	}
	public String getTruckLengthText() {
		return truckLengthText;
	}
	public void setTruckLengthText(String truckLengthText) {
		this.truckLengthText = truckLengthText;
	}
	public String getTruckLicenseAuditNote() {
		return truckLicenseAuditNote;
	}
	public void setTruckLicenseAuditNote(String truckLicenseAuditNote) {
		this.truckLicenseAuditNote = truckLicenseAuditNote;
	}
	public int getTruckLicenseAuditStatus() {
		return truckLicenseAuditStatus;
	}
	public void setTruckLicenseAuditStatus(int truckLicenseAuditStatus) {
		this.truckLicenseAuditStatus = truckLicenseAuditStatus;
	}
	public String getTruckLicenseAuditStatusText() {
		return truckLicenseAuditStatusText;
	}
	public void setTruckLicenseAuditStatusText(String truckLicenseAuditStatusText) {
		this.truckLicenseAuditStatusText = truckLicenseAuditStatusText;
	}
	public String getFrontTruckLicensePhoto() {
		return frontTruckLicensePhoto;
	}
	public void setFrontTruckLicensePhoto(String frontTruckLicensePhoto) {
		this.frontTruckLicensePhoto = frontTruckLicensePhoto;
	}
	public String getTruckNumber() {
		return truckNumber;
	}
	public void setTruckNumber(String truckNumber) {
		this.truckNumber = truckNumber;
	}
	public int getTruckType() {
		return truckType;
	}
	public void setTruckType(int truckType) {
		this.truckType = truckType;
		this.truckType -= 1;
	}
	public String getTruckTypeText() {
		return truckTypeText;
	}
	public void setTruckTypeText(String truckTypeText) {
		this.truckTypeText = truckTypeText;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAuditNoteTypeText() {
		return auditNoteTypeText;
	}
	public void setAuditNoteTypeText(String auditNoteTypeText) {
		this.auditNoteTypeText = auditNoteTypeText;
	}
	public boolean isDump() {
		return isDump;
	}
	public void setDump(boolean isDump) {
		this.isDump = isDump;
	}
	public String getIsDumpText() {
		return isDumpText;
	}
	public void setIsDumpText(String isDumpText) {
		this.isDumpText = isDumpText;
	}
	
	/**
	 * 1.审核中 2.审核不通过 3.审核通过 4.待完善 5.已过期 100.其他
	 * @return
	 */
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
	public String getTruckNumberLetter() {
		return truckNumberLetter;
	}
	public void setTruckNumberLetter(String truckNumberLetter) {
		this.truckNumberLetter = truckNumberLetter;
	}
	public String getTruckNumberProvince() {
		return truckNumberProvince;
	}
	public void setTruckNumberProvince(String truckNumberProvince) {
		this.truckNumberProvince = truckNumberProvince;
	}
	public String getTruckNumberMidd() {
		return truckNumberMidd;
	}
	public void setTruckNumberMidd(String truckNumberMidd) {
		this.truckNumberMidd = truckNumberMidd;
	}
	
}
