package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.List;

public class RefreshMessage implements Serializable{
	private static final long serialVersionUID = 7604954773252405398L;
	private List<InviteModel> REFRESH_MSG_AUCTION;
	private List<InviteModel> REFRESH_TASK_PLAN;
	private List<InviteModel> REFRESH_TASK_TRANSIT;
	private List<InviteModel> REFRESH_MSG_SYSTEM;
	private List<InviteModel> REFRESH_MSG_FRIENDSHIP;
	
	public List<InviteModel> getREFRESH_MSG_FRIENDSHIP() {
		return REFRESH_MSG_FRIENDSHIP;
	}
	public void setREFRESH_MSG_FRIENDSHIP(List<InviteModel> rEFRESH_MSG_FRIENDSHIP) {
		REFRESH_MSG_FRIENDSHIP = rEFRESH_MSG_FRIENDSHIP;
	}
	public List<InviteModel> getREFRESH_MSG_AUCTION() {
		return REFRESH_MSG_AUCTION;
	}
	public void setREFRESH_MSG_AUCTION(List<InviteModel> rEFRESH_MSG_AUCTION) {
		REFRESH_MSG_AUCTION = rEFRESH_MSG_AUCTION;
	}
	public List<InviteModel> getREFRESH_TASK_PLAN() {
		return REFRESH_TASK_PLAN;
	}
	public void setREFRESH_TASK_PLAN(List<InviteModel> rEFRESH_TASK_PLAN) {
		REFRESH_TASK_PLAN = rEFRESH_TASK_PLAN;
	}
	public List<InviteModel> getREFRESH_TASK_TRANSIT() {
		return REFRESH_TASK_TRANSIT;
	}
	public void setREFRESH_TASK_TRANSIT(List<InviteModel> rEFRESH_TASK_TRANSIT) {
		REFRESH_TASK_TRANSIT = rEFRESH_TASK_TRANSIT;
	}
	public List<InviteModel> getREFRESH_MSG_SYSTEM() {
		return REFRESH_MSG_SYSTEM;
	}
	public void setREFRESH_MSG_SYSTEM(List<InviteModel> rEFRESH_MSG_SYSTEM) {
		REFRESH_MSG_SYSTEM = rEFRESH_MSG_SYSTEM;
	}
}
