package com.hongshi.wuliudidi.model;

import java.io.Serializable;

/**
 * Created by huiyuan on 2017/10/23.
 */

public class EmptyTruckModel implements Serializable {
    //最近交易时间
    private String lastestGmtFinished;
    //拥有车辆数
    private String truckNum;
    //完成单数
    private String finishedDealNum;
    //上一次空车上报时间
    private String lastReportDate;

    public String getLastestGmtFinished() {
        return lastestGmtFinished;
    }

    public void setLastestGmtFinished(String lastestGmtFinished) {
        this.lastestGmtFinished = lastestGmtFinished;
    }

    public String getTruckNum() {
        return truckNum;
    }

    public void setTruckNum(String truckNum) {
        this.truckNum = truckNum;
    }

    public String getFinishedDealNum() {
        return finishedDealNum;
    }

    public void setFinishedDealNum(String finishedDealNum) {
        this.finishedDealNum = finishedDealNum;
    }

    public String getLastReportDate() {
        return lastReportDate;
    }

    public void setLastReportDate(String lastReportDate) {
        this.lastReportDate = lastReportDate;
    }
}
