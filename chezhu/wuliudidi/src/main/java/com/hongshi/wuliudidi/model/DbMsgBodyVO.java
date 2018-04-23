package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by huiyuan on 2017/2/20.
 */

public class DbMsgBodyVO implements Serializable{
    private String msgBizType;
    private String msgSubBizType;
    private String msgBizTypeTxt;
    private String realContent;
    private boolean read;
    private String title;
    private String url;
    private Date gmtCreate;
    private Date gmtModified;

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getMsgBizType() {
        return msgBizType;
    }

    public void setMsgBizType(String msgBizType) {
        this.msgBizType = msgBizType;
    }

    public String getMsgSubBizType() {
        return msgSubBizType;
    }

    public void setMsgSubBizType(String msgSubBizType) {
        this.msgSubBizType = msgSubBizType;
    }

    public String getMsgBizTypeTxt() {
        return msgBizTypeTxt;
    }

    public void setMsgBizTypeTxt(String msgBizTypeTxt) {
        this.msgBizTypeTxt = msgBizTypeTxt;
    }

    public String getRealContent() {
        return realContent;
    }

    public void setRealContent(String realContent) {
        this.realContent = realContent;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
