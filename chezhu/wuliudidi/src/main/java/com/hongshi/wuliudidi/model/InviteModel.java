package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class InviteModel implements Serializable{
	private static final long serialVersionUID = -5070422377543289192L;
	// 消息的ID号
	private String id;
	// 用户的ID
	private String uid;
	// 消息的业务类型，主要有SYSTEM、AUCTION、FRIENDSHIP三种
	private String msgBizType;
	// 消息体的真实内容
	private String realContent;
	// 消息状态
	private boolean read;
	// 0-未处理 1同意 2不同意
	private int dealResult;
	// 数组
	private List<String> params;
	// 业务子类型,FRIENDSHIP_DRIVER_AGREE,FRIENDSHIP_DRIVER_DISAGREE,FRIENDSHIP_LEADER_INVITE
	private String msgSubBizType;
	//时间
	private Date gmtCreate;
	//消息的ID
	private String  msgId;

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public String getMsgSubBizType() {
		return msgSubBizType;
	}

	public void setMsgSubBizType(String msgSubBizType) {
		this.msgSubBizType = msgSubBizType;
	}

	public List<String> getParams() {
		return params;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getMsgBizType() {
		return msgBizType;
	}

	public void setMsgBizType(String msgBizType) {
		this.msgBizType = msgBizType;
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

	public int getDealResult() {
		return dealResult;
	}

	public void setDealResult(int dealResult) {
		this.dealResult = dealResult;
	}
}

