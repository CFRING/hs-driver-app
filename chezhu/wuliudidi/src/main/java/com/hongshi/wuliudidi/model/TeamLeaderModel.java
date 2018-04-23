package com.hongshi.wuliudidi.model;

public class TeamLeaderModel {

	private String name;
	private String cellphone;
	//企业名称
	private String enterpriseName;
	//司机队长id
	private String parentUserId;
	//车牌号码
	private String truckNumber;
	//司机队长类型,1,个人司机队长，2，企业司机队长
	private int leaderType;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	public String getParentUserId() {
		return parentUserId;
	}
	public void setParentUserId(String parentUserId) {
		this.parentUserId = parentUserId;
	}
	public String getTruckNumber() {
		return truckNumber;
	}
	public void setTruckNumber(String truckNumber) {
		this.truckNumber = truckNumber;
	}
	public int getLeaderType() {
		return leaderType;
	}
	public void setLeaderType(int leaderType) {
		this.leaderType = leaderType;
	}
}
