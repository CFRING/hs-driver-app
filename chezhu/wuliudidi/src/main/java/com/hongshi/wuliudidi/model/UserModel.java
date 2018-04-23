package com.hongshi.wuliudidi.model;


/**
 * 我的页面的接口的返回值
 * 
 * @author binyao.ruan
 * @version $Id: MyUserAppVO.java, v 0.1 2015年8月20日 上午11:16:14 binyao.ruan Exp $
 */
public class UserModel{

    /**  */
    private static final long serialVersionUID = 8815862127036472079L;

    /**
     * 身份基本信息认证
     */
    private boolean           hasIdentityAuth;
    
    /**  身份基本信息认证状态 */
	/**0、1.审核中  2.不通过, 3.通过, 4.待完善, 5.已过期  6。未提交*/
    private int               identityAuthStatus;

    /**
     * 驾驶证认证
     */
    private boolean           hasDrivingLicenceAuth;
    
    /**  驾驶证认证状态*/
    private int               drivingLicenceStatus;

    /**
     * 法人身份认证
     */
    private boolean           hasEnterpriseAuth;
    
    /**  法人身份认证状态*/
    private int               enterpriseAuthstatus;

    /**
     * 营业执照认证
     */
    private boolean           hasBusinessAuth;

    /**
     * 道路运输经营许可证认证
     */
    private boolean           hasTransportationPermissionAuth;
    /**
     * id
     */
    private String            userId;

    /**
     * 用户名
     */
    private String            name;

    /**
     * 邮箱
     */
    private String            email;

    /**
     * 手机号码
     */
    private String            cellphone;

    /**
     * 备用号码
     */
    private String            backNumber;
	//3-普通司机，4-司机队长，5，司机队员
    private int roleType;
	//2-企业,3,个人
    private int organizationType;

	/**
     * 用户头像
     */
    private String            userFace;
    /**用户是否已缴保证金*/
    private boolean hasAcct;
    
    /**职位（如果是内部人员，会有相应职位）*/
    private String position;

	/**
	 * 是否有常用路线
	 */
	private boolean           hasCommonlines;

	public boolean isHasCommonlines() {
		return hasCommonlines;
	}

	public void setHasCommonlines(boolean hasCommonlines) {
		this.hasCommonlines = hasCommonlines;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(int organizationType) {
		this.organizationType = organizationType;
	}

	public int getRoleType() {
		return roleType;
	}

	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}
    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getBackNumber() {
		return backNumber;
	}

	public void setBackNumber(String backNumber) {
		this.backNumber = backNumber;
	}

	public String getUserFace() {
		return userFace;
	}

	public void setUserFace(String userFace) {
		this.userFace = userFace;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

    public boolean isHasIdentityAuth() {
        return hasIdentityAuth;
    }

    public void setHasIdentityAuth(boolean hasIdentityAuth) {
        this.hasIdentityAuth = hasIdentityAuth;
    }

    public boolean isHasDrivingLicenceAuth() {
        return hasDrivingLicenceAuth;
    }

    public void setHasDrivingLicenceAuth(boolean hasDrivingLicenceAuth) {
        this.hasDrivingLicenceAuth = hasDrivingLicenceAuth;
    }

    public boolean isHasEnterpriseAuth() {
        return hasEnterpriseAuth;
    }

    public void setHasEnterpriseAuth(boolean hasEnterpriseAuth) {
        this.hasEnterpriseAuth = hasEnterpriseAuth;
    }

    public boolean isHasBusinessAuth() {
        return hasBusinessAuth;
    }

    public void setHasBusinessAuth(boolean hasBusinessAuth) {
        this.hasBusinessAuth = hasBusinessAuth;
    }

    public boolean isHasTransportationPermissionAuth() {
        return hasTransportationPermissionAuth;
    }

    public void setHasTransportationPermissionAuth(boolean hasTransportationPermissionAuth) {
        this.hasTransportationPermissionAuth = hasTransportationPermissionAuth;
    }

	public int getIdentityAuthStatus() {
		return identityAuthStatus;
	}

	public void setIdentityAuthStatus(int identityAuthStatus) {
		this.identityAuthStatus = identityAuthStatus;
	}

	public int getDrivingLicenceStatus() {
		return drivingLicenceStatus;
	}

	public void setDrivingLicenceStatus(int drivingLicenceStatus) {
		this.drivingLicenceStatus = drivingLicenceStatus;
	}

	public int getEnterpriseAuthstatus() {
		return enterpriseAuthstatus;
	}

	public void setEnterpriseAuthstatus(int enterpriseAuthstatus) {
		this.enterpriseAuthstatus = enterpriseAuthstatus;
	}

	public boolean isHasAcct() {
		return hasAcct;
	}

	public void setHasAcct(boolean hasAcct) {
		this.hasAcct = hasAcct;
	}
	
}
