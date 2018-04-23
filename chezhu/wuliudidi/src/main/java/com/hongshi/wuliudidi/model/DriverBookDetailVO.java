package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huiyuan on 2017/3/29.
 */

public class DriverBookDetailVO implements Serializable {
    //是否允许司机接单. 0:允许; 1:不允许.
    private int allowDriverAcceptOrderFlag;
    private String backupCellphone;
    private String cellphone;
    private String driverBookId;
    private boolean isOwner;
    private String nickName;
    private String realName;
    private static long serialVersionUID;
    //车辆列表.
    private List<TruckAuthAppVO> truckVoList;
    private String userFace;
    private String driverId;

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public int getAllowDriverAcceptOrderFlag() {
        return allowDriverAcceptOrderFlag;
    }

    public void setAllowDriverAcceptOrderFlag(int allowDriverAcceptOrderFlag) {
        this.allowDriverAcceptOrderFlag = allowDriverAcceptOrderFlag;
    }

    public String getBackupCellphone() {
        return backupCellphone;
    }

    public void setBackupCellphone(String backupCellphone) {
        this.backupCellphone = backupCellphone;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getDriverBookId() {
        return driverBookId;
    }

    public void setDriverBookId(String driverBookId) {
        this.driverBookId = driverBookId;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static void setSerialVersionUID(long serialVersionUID) {
        DriverBookDetailVO.serialVersionUID = serialVersionUID;
    }

    public List<TruckAuthAppVO> getTruckVoList() {
        return truckVoList;
    }

    public void setTruckVoList(List<TruckAuthAppVO> truckVoList) {
        this.truckVoList = truckVoList;
    }

    public String getUserFace() {
        return userFace;
    }

    public void setUserFace(String userFace) {
        this.userFace = userFace;
    }
}
