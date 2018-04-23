package com.hongshi.wuliudidi.model;


/**
 * 一个竞价条目下一辆车的运输记录
 * 
 * @author haiyang.jiang
 * @version $Id: TruckTransitTaskRecord.java, v 0.1 2015年8月18日 下午1:47:12 niya Exp $
 */
public class TruckTransitTaskSumVO {
    //车辆ID
    private String   truckId;

    //车牌号
    private String   number;

    //车型
    private String   typeText;

    //车长
    private String   lengthText;
    
   //车厢
    private String   carriageText;

    //最大载重
    private double   capacity;

    // 总运量
    private double   totalAmount;

    //货物量单位：1吨 3立方米
//    private int      unit;
    private String   unitText;
    
    //载货空间
    private double carryVolume;

    //AppMapVO<String, LinkedHashMap<String, List<TruckTransitTaskRecordVO>>>
    private AppMapVO recordList;

    public String getTruckId() {
        return truckId;
    }

    public void setTruckId(String truckId) {
        this.truckId = truckId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTypeText() {
        return typeText;
    }

    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }

    public String getLengthText() {
        return lengthText;
    }

    public void setLengthText(String lengthText) {
        this.lengthText = lengthText;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUnitText() {
        return unitText;
    }

    public void setUnitText(String unitText) {
        this.unitText = unitText;
    }

    public AppMapVO getRecordList() {
        return recordList;
    }

    public void setRecordList(AppMapVO recordList) {
        this.recordList = recordList;
    }

	public String getCarriageText() {
		return carriageText;
	}

	public void setCarriageText(String carriageText) {
		this.carriageText = carriageText;
	}

	public double getCarryVolume() {
		return carryVolume;
	}

	public void setCarryVolume(double carryVolume) {
		this.carryVolume = carryVolume;
	}
}
