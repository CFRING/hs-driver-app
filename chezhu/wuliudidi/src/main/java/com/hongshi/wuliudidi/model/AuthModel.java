package com.hongshi.wuliudidi.model;

public class AuthModel {

	private String name;
	private String sex;
	private String userId;
	//身份认证ID
	private String identityId;
	//身份证正面照片
	private String frontCardPhoto;
	//身份证号码
	private String identityCode;
	//身份证正面照审核状态
	private int frontAuditStatus;
	//身份证正面照审核备注
	private String frontAuditNote;
	//身份证反面照
	private String backCardPhoto;
	//身份证反面照审核备注
	private String backAuditNote;
	//身份证反面照状态
	private int backAuditStatus;
	//驾驶证审核状态
	private int drivingAuditStatus;
	//驾驶证审核备注
	private String drivingAuditNote;
	//驾驶证照片
	private String drivingPhoto;
	//法人
	private String legalPerson;
	//法人身份证号
	private String personNumber;
	//企业ID
	private String enterpriseId;
	//营业执照
	private String businessPhoto;
	//营业执照审核状态
	private int businessAuditStatus;
	//营业执照审核备注
	private String businessAuditNote;
	//道路运输经营许可证照片
	private String transportationPhoto;
	//道路运输经营许可证照片审核状态
	private int transportationAuditStatus;
	//道路运输经营许可证照片审核备注
	private String transportationAuditNote;
	//驾照有效期
	private long validDate;
	//驾照类型
	private String drivingTypeText;
	private int drivingType;
	//审核状态
	private int status;
	private String statusText;
	//审核未通过原因
	private String auditNoteTypeText;
	public int getDrivingType() {
		return drivingType;
	}
	public void setDrivingType(int drivingType) {
		this.drivingType = drivingType;
	}
	public String getDrivingTypeText() {
		return drivingTypeText;
	}
	public void setDrivingTypeText(String drivingTypeText) {
		this.drivingTypeText = drivingTypeText;
	}
	public long getValidDate() {
		return validDate;
	}
	public void setValidDate(long validDate) {
		this.validDate = validDate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getLegalPerson() {
		return legalPerson;
	}
	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}
	public String getPersonNumber() {
		return personNumber;
	}
	public void setPersonNumber(String personNumber) {
		this.personNumber = personNumber;
	}
	public String getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public String getBusinessPhoto() {
		return businessPhoto;
	}
	public void setBusinessPhoto(String businessPhoto) {
		this.businessPhoto = businessPhoto;
	}
	public int getBusinessAuditStatus() {
		return businessAuditStatus;
	}
	public void setBusinessAuditStatus(int businessAuditStatus) {
		this.businessAuditStatus = businessAuditStatus;
	}
	public String getBusinessAuditNote() {
		return businessAuditNote;
	}
	public void setBusinessAuditNote(String businessAuditNote) {
		this.businessAuditNote = businessAuditNote;
	}
	public String getTransportationPhoto() {
		return transportationPhoto;
	}
	public void setTransportationPhoto(String transportationPhoto) {
		this.transportationPhoto = transportationPhoto;
	}
	public int getTransportationAuditStatus() {
		return transportationAuditStatus;
	}
	public void setTransportationAuditStatus(int transportationAuditStatus) {
		this.transportationAuditStatus = transportationAuditStatus;
	}
	public String getTransportationAuditNote() {
		return transportationAuditNote;
	}
	public void setTransportationAuditNote(String transportationAuditNote) {
		this.transportationAuditNote = transportationAuditNote;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getIdentityId() {
		return identityId;
	}
	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}
	public String getFrontCardPhoto() {
		return frontCardPhoto;
	}
	public void setFrontCardPhoto(String frontCardPhoto) {
		this.frontCardPhoto = frontCardPhoto;
	}
	public String getIdentityCode() {
		return identityCode;
	}
	public void setIdentityCode(String identityCode) {
		this.identityCode = identityCode;
	}
	public int getFrontAuditStatus() {
		return frontAuditStatus;
	}
	public void setFrontAuditStatus(int frontAuditStatus) {
		this.frontAuditStatus = frontAuditStatus;
	}
	public String getFrontAuditNote() {
		return frontAuditNote;
	}
	public void setFrontAuditNote(String frontAuditNote) {
		this.frontAuditNote = frontAuditNote;
	}
	public String getBackCardPhoto() {
		return backCardPhoto;
	}
	public void setBackCardPhoto(String backCardPhoto) {
		this.backCardPhoto = backCardPhoto;
	}
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
	public int getDrivingAuditStatus() {
		return drivingAuditStatus;
	}
	public void setDrivingAuditStatus(int drivingAuditStatus) {
		this.drivingAuditStatus = drivingAuditStatus;
	}
	public String getDrivingAuditNote() {
		return drivingAuditNote;
	}
	public void setDrivingAuditNote(String drivingAuditNote) {
		this.drivingAuditNote = drivingAuditNote;
	}
	public String getDrivingPhoto() {
		return drivingPhoto;
	}
	public void setDrivingPhoto(String drivingPhoto) {
		this.drivingPhoto = drivingPhoto;
	}
	public String getStatusText() {
		return statusText;
	}
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	public String getAuditNoteTypeText() {
		return auditNoteTypeText;
	}
	public void setAuditNoteTypeText(String auditNoteTypeText) {
		this.auditNoteTypeText = auditNoteTypeText;
	}
}
