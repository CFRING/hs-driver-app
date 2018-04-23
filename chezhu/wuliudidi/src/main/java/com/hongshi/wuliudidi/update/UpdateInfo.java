package com.hongshi.wuliudidi.update;
/**
 * 更新信息的数据实体
 * @author zn
 *
 */
public class UpdateInfo {
	//安装包地址
	private String androidPackageUrl;
	//是否强制升级
	private boolean androidUpdateMadatory;
	//升级版本号
	private int androidVcode;
	private String androidVersion;
	//升级内容
	private String androidComment;
	//是否可以重复选择司机的标记，true 允许重复，false 不可重复选择
	private boolean allowDriverDuplicate;
	private int appType;
	private int addressVersion;
	public boolean isAllowDriverDuplicate() {
		return allowDriverDuplicate;
	}
	public void setAllowDriverDuplicate(boolean allowDriverDuplicate) {
		this.allowDriverDuplicate = allowDriverDuplicate;
	}
	public String getAndroidComment() {
		return androidComment;
	}
	public void setAndroidComment(String androidComment) {
		this.androidComment = androidComment;
	}
	public String getAndroidPackageUrl() {
		return androidPackageUrl;
	}
	public void setAndroidPackageUrl(String androidPackageUrl) {
		this.androidPackageUrl = androidPackageUrl;
	}
	public boolean isAndroidUpdateMadatory() {
		return androidUpdateMadatory;
	}
	public void setAndroidUpdateMadatory(boolean androidUpdateMadatory) {
		this.androidUpdateMadatory = androidUpdateMadatory;
	}
	public int getAndroidVcode() {
		return androidVcode;
	}
	public void setAndroidVcode(int androidVcode) {
		this.androidVcode = androidVcode;
	}
	public String getAndroidVersion() {
		return androidVersion;
	}
	public void setAndroidVersion(String androidVersion) {
		this.androidVersion = androidVersion;
	}
	public int getAppType() {
		return appType;
	}
	public void setAppType(int appType) {
		this.appType = appType;
	}

	public int getAddressVersion() {
		return addressVersion;
	}

	public void setAddressVersion(int addressVersion) {
		this.addressVersion = addressVersion;
	}
}
