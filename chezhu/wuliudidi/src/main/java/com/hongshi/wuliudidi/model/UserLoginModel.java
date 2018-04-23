package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.List;

public class UserLoginModel implements Serializable{
	/**
     * 当前用户id
     */
    private String            userId;

    /**
     * 邮箱
     */
    private String            email;

    /**
     * 手机号码
     */
    private String            cellphone;

    /**
     * 用户头像
     */
    private String            userFace;

    /**
     * 职位
     */
    private String            position;

	public Boolean getCurrentRole() {
		return currentRole;
	}

	public void setCurrentRole(Boolean currentRole) {
		this.currentRole = currentRole;
	}

	/**

     * 真实姓名
     */
    private String            realName;
    /**
     * 企业名称
     */
    private String            enterpriseName;
	//物流账号是否激活
	private Boolean           currentRole;
	private List<UserOrganizationVO>   userOrganizationList;

	public List<UserOrganizationVO> getUserOrganizationList() {
		return userOrganizationList;
	}

	public void setUserOrganizationList(List<UserOrganizationVO> userOrganizationList) {
		this.userOrganizationList = userOrganizationList;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getUserFace() {
		return userFace;
	}
	public void setUserFace(String userFace) {
		this.userFace = userFace;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

}
