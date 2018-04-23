package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huiyuan on 2017/3/30.
 */

public class DriverBookListVO implements Serializable {
    //备用手机号
    private java.lang.String backupCellphone;
    //手机号
    private java.lang.String cellphone;
    //司机名录记录ID
    private java.lang.String driverBookId;
    //司机ID
    private java.lang.String driverId;
    //是否是车主本人
    private boolean isOwner;
    //昵称
    private java.lang.String nickName;
    //真实姓名
    private java.lang.String realName;
    //车牌号 list
    private java.util.List<String> truckNumberList;
    //头像
    private java.lang.String userFace;

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

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
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

    public List<String> getTruckNumberList() {
        return truckNumberList;
    }

    public void setTruckNumberList(List<String> truckNumberList) {
        this.truckNumberList = truckNumberList;
    }

    public String getUserFace() {
        return userFace;
    }

    public void setUserFace(String userFace) {
        this.userFace = userFace;
    }
}
