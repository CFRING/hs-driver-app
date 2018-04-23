package com.hongshi.wuliudidi.model;

import java.io.Serializable;

/**
 * Created by huiyuan on 2017/2/20.
 */

public class MsgFindDBAndPageParamVO implements Serializable{
    private String bizType;
    private String bizSubType;
    private int currentPage;
    private int pageSize;

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getBizSubType() {
        return bizSubType;
    }

    public void setBizSubType(String bizSubType) {
        this.bizSubType = bizSubType;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
