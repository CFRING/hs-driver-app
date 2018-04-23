package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huiyuan on 2017/3/29.
 */

public class DriverBookInputFormVO implements Serializable {
    //是否允许司机接单. 0:允许; 1:不允许.
    private int allowDriverAcceptOrderFlag;
    //司机的备用手机号码
    private java.lang.String backupCellphone;
    //司机的首选手机号码
    private java.lang.String cellphone;
    //司机ID
    private java.lang.String driverId;
    //主键ID (driver_book 主键)
    private java.lang.String id;
    //司机的昵称
    private java.lang.String nickName;
    //车主ID
    private java.lang.String ownerId;
    private static long serialVersionUID;
    //车辆列表
    private java.util.List<TruckAuthAppVO> truckVoList;

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

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static void setSerialVersionUID(long serialVersionUID) {
        DriverBookInputFormVO.serialVersionUID = serialVersionUID;
    }

    public List<TruckAuthAppVO> getTruckVoList() {
        return truckVoList;
    }

    public void setTruckVoList(List<TruckAuthAppVO> truckVoList) {
        this.truckVoList = truckVoList;
    }
}
