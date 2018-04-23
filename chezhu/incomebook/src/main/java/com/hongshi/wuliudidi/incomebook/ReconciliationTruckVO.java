package com.hongshi.wuliudidi.incomebook;

import java.io.Serializable;

/**
 * Created by huiyuan on 2016/8/23.
 */
public class ReconciliationTruckVO implements Serializable{

    private java.lang.String truckId;//车牌id

    private java.lang.String truckNumber;//车牌号码

    public String getTruckId() {
        return truckId;
    }

    public void setTruckId(String truckId) {
        this.truckId = truckId;
    }

    public String getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }
}
