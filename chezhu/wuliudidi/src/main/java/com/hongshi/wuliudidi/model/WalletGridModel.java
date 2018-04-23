package com.hongshi.wuliudidi.model;

public class WalletGridModel {
	private int imageId, hintImageId;
	private String text;
	private boolean showHintImage;
	private GridType type;
	
	public WalletGridModel(){}
	
	public WalletGridModel(String text, int imageId, boolean showHintImage, int hintImageId, GridType type){
		this.text = text;
		this.imageId = imageId;
		this.showHintImage = showHintImage;
		this.hintImageId = hintImageId;
		this.type = type;
	}
	
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
	public int getHintImageId() {
		return hintImageId;
	}
	public void setHintImageId(int hintImageId) {
		this.hintImageId = hintImageId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isShowHintImage() {
		return showHintImage;
	}
	public void setShowHintImage(boolean showHintImage) {
		this.showHintImage = showHintImage;
	}
	public GridType getType() {
		return type;
	}
	public void setType(GridType type) {
		this.type = type;
	}

	public static enum GridType{
		recharge, transfer_accounts, withdrawl, bill_record, safety_setting;
	}
}
