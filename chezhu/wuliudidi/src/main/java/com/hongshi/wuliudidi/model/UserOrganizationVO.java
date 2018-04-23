package com.hongshi.wuliudidi.model;

import java.io.Serializable;

/**
 * Created by huiyuan on 2017/3/27.
 */

public class UserOrganizationVO implements Serializable {
    private int               roleType;
    private String            roleTypeText;

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public String getRoleTypeText() {
        return roleTypeText;
    }

    public void setRoleTypeText(String roleTypeText) {
        this.roleTypeText = roleTypeText;
    }
}
