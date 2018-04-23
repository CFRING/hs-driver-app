package com.hongshi.wuliudidi.model;

import java.io.Serializable;

public class TeamMemberModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String userId;
	//头像
	private String userFace;
	//电话
	private String cellphone;
	//备用电话
	private String backNumber;
	//队长对队员的备注昵称（可为空）
	private String nick;
	//车辆ID
	private String truckId;
	//车辆类型表的ID
	private String truckTypeId;
	//车辆类型描述
	private String truckTypeText;
	//车牌号码
	private String truckNumber;
	//最大载重量
	private double carryCapacity;
	//最大载单位
	private String carryCapacityUnitText;
	//车辆长度ID
	private String truckLengthId;
	//车辆长度描述
	private String truckLengthText;
	//最大载货体积
	private String carryVolume;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserFace() {
		return userFace;
	}
	public void setUserFace(String userFace) {
		this.userFace = userFace;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getBackNumber() {
		return backNumber;
	}
	public void setBackNumber(String backNumber) {
		this.backNumber = backNumber;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getTruckId() {
		return truckId;
	}
	public void setTruckId(String truckId) {
		this.truckId = truckId;
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
	public String getTruckNumber() {
		return truckNumber;
	}
	public void setTruckNumber(String truckNumber) {
		this.truckNumber = truckNumber;
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
	public String getTruckLengthId() {
		return truckLengthId;
	}
	public void setTruckLengthId(String truckLengthId) {
		this.truckLengthId = truckLengthId;
	}
	public String getTruckLengthText() {
		return truckLengthText;
	}
	public void setTruckLengthText(String truckLengthText) {
		this.truckLengthText = truckLengthText;
	}
	public String getCarryVolume() {
		return carryVolume;
	}
	public void setCarryVolume(String carryVolume) {
		this.carryVolume = carryVolume;
	}
}
