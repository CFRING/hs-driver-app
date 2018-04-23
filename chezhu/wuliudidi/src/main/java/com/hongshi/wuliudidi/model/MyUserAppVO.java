package com.hongshi.wuliudidi.model;

import java.io.Serializable;

/**
 * Created by huiyuan on 2017/4/19.
 */

public class MyUserAppVO extends UserModel implements Serializable {
    //驾驶证认证状态
    private int drivingLicenceStatus;
    //法人身份认证状态
    private int enterpriseAuthstatus;
    //是否已经保证金
    private boolean hasAcct;
    //营业执照认证
    private boolean hasBusinessAuth;
    //是否有常用路线
    private boolean hasCommonlines;
    //驾驶证认证
    private boolean hasDrivingLicenceAuth;
    //法人身份认证
    private boolean hasEnterpriseAuth;
    //身份基本信息认证
    private boolean hasIdentityAuth;
    //道路运输经营许可证认证
    private boolean hasTransportationPermissionAuth;
    //身份基本信息认证状态
    private int identityAuthStatus;
    //司机组织关系枚举
    private int organizationType;
    private java.lang.String organizationTypeText;
    //司机身份枚举
    private int roleType;
    private java.lang.String roleTypeText;
    private static long serialVersionUID;
    //车主电话
    private java.lang.String truckOwnerCellphone;
    //我的车主名字 (真实姓名)
    private java.lang.String truckOwnerName;
    //司机真实姓名
    private String realName;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Override
    public int getDrivingLicenceStatus() {
        return drivingLicenceStatus;
    }

    @Override
    public void setDrivingLicenceStatus(int drivingLicenceStatus) {
        this.drivingLicenceStatus = drivingLicenceStatus;
    }

    @Override
    public int getEnterpriseAuthstatus() {
        return enterpriseAuthstatus;
    }

    @Override
    public void setEnterpriseAuthstatus(int enterpriseAuthstatus) {
        this.enterpriseAuthstatus = enterpriseAuthstatus;
    }

    @Override
    public boolean isHasAcct() {
        return hasAcct;
    }

    @Override
    public void setHasAcct(boolean hasAcct) {
        this.hasAcct = hasAcct;
    }

    @Override
    public boolean isHasBusinessAuth() {
        return hasBusinessAuth;
    }

    @Override
    public void setHasBusinessAuth(boolean hasBusinessAuth) {
        this.hasBusinessAuth = hasBusinessAuth;
    }

    @Override
    public boolean isHasCommonlines() {
        return hasCommonlines;
    }

    @Override
    public void setHasCommonlines(boolean hasCommonlines) {
        this.hasCommonlines = hasCommonlines;
    }

    @Override
    public boolean isHasDrivingLicenceAuth() {
        return hasDrivingLicenceAuth;
    }

    @Override
    public void setHasDrivingLicenceAuth(boolean hasDrivingLicenceAuth) {
        this.hasDrivingLicenceAuth = hasDrivingLicenceAuth;
    }

    @Override
    public boolean isHasEnterpriseAuth() {
        return hasEnterpriseAuth;
    }

    @Override
    public void setHasEnterpriseAuth(boolean hasEnterpriseAuth) {
        this.hasEnterpriseAuth = hasEnterpriseAuth;
    }

    @Override
    public boolean isHasIdentityAuth() {
        return hasIdentityAuth;
    }

    @Override
    public void setHasIdentityAuth(boolean hasIdentityAuth) {
        this.hasIdentityAuth = hasIdentityAuth;
    }

    @Override
    public boolean isHasTransportationPermissionAuth() {
        return hasTransportationPermissionAuth;
    }

    @Override
    public void setHasTransportationPermissionAuth(boolean hasTransportationPermissionAuth) {
        this.hasTransportationPermissionAuth = hasTransportationPermissionAuth;
    }

    @Override
    public int getIdentityAuthStatus() {
        return identityAuthStatus;
    }

    @Override
    public void setIdentityAuthStatus(int identityAuthStatus) {
        this.identityAuthStatus = identityAuthStatus;
    }

    @Override
    public int getOrganizationType() {
        return organizationType;
    }

    @Override
    public void setOrganizationType(int organizationType) {
        this.organizationType = organizationType;
    }

    public String getOrganizationTypeText() {
        return organizationTypeText;
    }

    public void setOrganizationTypeText(String organizationTypeText) {
        this.organizationTypeText = organizationTypeText;
    }

    @Override
    public int getRoleType() {
        return roleType;
    }

    @Override
    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public String getRoleTypeText() {
        return roleTypeText;
    }

    public void setRoleTypeText(String roleTypeText) {
        this.roleTypeText = roleTypeText;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static void setSerialVersionUID(long serialVersionUID) {
        MyUserAppVO.serialVersionUID = serialVersionUID;
    }

    public String getTruckOwnerCellphone() {
        return truckOwnerCellphone;
    }

    public void setTruckOwnerCellphone(String truckOwnerCellphone) {
        this.truckOwnerCellphone = truckOwnerCellphone;
    }

    public String getTruckOwnerName() {
        return truckOwnerName;
    }

    public void setTruckOwnerName(String truckOwnerName) {
        this.truckOwnerName = truckOwnerName;
    }
}
