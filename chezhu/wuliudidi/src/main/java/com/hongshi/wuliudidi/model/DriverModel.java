package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.List;

public class DriverModel implements Serializable{

	private static final long serialVersionUID = 3068895192852059406L;
	private String driverBookId;
	private String userFace;
	private String realName;
	private String nickName;
	private String cellphone;
	private String backupCellPhone;
	private boolean isOwner ;
	private String driverId;
	//车牌号 list
	private List<String> truckNumberList;

	public boolean isOwner() {
		return isOwner;
	}

	public void setOwner(boolean owner) {
		isOwner = owner;
	}

	public List<String> getTruckNumberList() {
		return truckNumberList;
	}

	public void setTruckNumberList(List<String> truckNumberList) {
		this.truckNumberList = truckNumberList;
	}

	public String getDriverBookId() {
		return driverBookId;
	}
	public void setDriverBookId(String driverBookId) {
		this.driverBookId = driverBookId;
	}
	
	public String getUserFace() {
		return userFace;
	}
	public void setUserFace(String userFace) {
		this.userFace = userFace;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getBackupCellPhone() {
		return backupCellPhone;
	}
	public void setBackupCellPhone(String backupCellPhone) {
		this.backupCellPhone = backupCellPhone;
	}
	public String getDriverId() {
		return driverId;
	}
	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
