package com.hongshi.wuliudidi.model;

public class MyGridModel {
	private int imageId;
	private String text;
	private GridType type;
	/**0、1.审核中  2.不通过, 3.通过, 4.待完善, 5.已过期  6。未提交*/
	private int flag = -1;
	
	public MyGridModel(){}
	
	public MyGridModel(int imageId, String text, GridType mType){
		this.imageId = imageId;
		this.text = text;
		this.type = mType;
	}
	
	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public GridType getType() {
		return type;
	}

	public void setType(GridType type) {
		this.type = type;
	}
	
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public static enum GridType{
		identity_authentication, enterprise_certification, driver_license, transportation_guarantee,
		my_truck, my_driver, my_accounts, bid_record, my_task, customer_service, my_setting, my_wallet,
		my_route;
	}
}
