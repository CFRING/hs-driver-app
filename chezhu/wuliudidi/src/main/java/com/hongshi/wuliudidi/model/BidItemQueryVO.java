package com.hongshi.wuliudidi.model;

import java.io.Serializable;

/**
 * Created by huiyuan on 2017/4/5.
 */

public class BidItemQueryVO implements Serializable {
    //1.待运输。2运输中。3.运输完成。0或者不传（全部-即接单历史）,4-运输记录
    private int queryStatus;

    public int getQueryStatus() {
        return queryStatus;
    }

    public void setQueryStatus(int queryStatus) {
        this.queryStatus = queryStatus;
    }
}
