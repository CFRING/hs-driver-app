package com.hongshi.wuliudidi.model;

import java.io.Serializable;

/**
 * @author huiyuan
 * @version 1.0
 * @created 2017/11/22 15:12
 * @title 积分详情item
 * @description 积分列表item数据
 * @changeRecord：2017/11/22 15:12 modify by
 */
public class IntegralDetailModel implements Serializable {
    //对应操作
    private String operationDesc;
    //操作时间，eg:2017-11-22
    private String optTime;
    //积分值
    private String point;
    //true 正数，false 负数
    private boolean positive;

    public String getOperationDesc() {
        return operationDesc;
    }

    public void setOperationDesc(String operationDesc) {
        this.operationDesc = operationDesc;
    }

    public String getOptTime() {
        return optTime;
    }

    public void setOptTime(String optTime) {
        this.optTime = optTime;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public boolean isPositive() {
        return positive;
    }

    public void setPositive(boolean positive) {
        this.positive = positive;
    }
}
