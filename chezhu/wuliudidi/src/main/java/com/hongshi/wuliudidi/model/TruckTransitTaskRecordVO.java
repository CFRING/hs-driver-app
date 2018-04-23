package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 一个竞价条目下一辆车的运输记录
 * 
 * @author haiyang.jiang
 * @version $Id: TruckTransitTaskRecord.java, v 0.1 2015年8月18日 下午1:47:12 niya Exp $
 */
public class TruckTransitTaskRecordVO implements Serializable {
    private static final long serialVersionUID = -4941512355247636514L;

    //运输任务ID
    private String            taskId;

    //车辆ID
    private String            truckId;

    //车牌号
    private String            truckNum;

    //司机ID
    private String            driverId;

    //司机姓名
    private String            driverName;

    private int               dayOfMonth;
    // 到货签收时间
    private Date              gmtSignup;
    // 实际运量
    private double            realAmount;

    //货物量单位：1吨 3立方米
//    private int               unit;
    private String            unitText;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTruckId() {
        return truckId;
    }

    public void setTruckId(String truckId) {
        this.truckId = truckId;
    }

    public String getTruckNum() {
        return truckNum;
    }

    public void setTruckNum(String truckNum) {
        this.truckNum = truckNum;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public Date getGmtSignup() {
        return gmtSignup;
    }

    public void setGmtSignup(Date gmtSignup) {
        this.gmtSignup = gmtSignup;
    }

    public double getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(double realAmount) {
        this.realAmount = realAmount;
    }

    public String getUnitText() {
        return unitText;
    }

    public void setUnitText(String unitText) {
        this.unitText = unitText;
    }
}
