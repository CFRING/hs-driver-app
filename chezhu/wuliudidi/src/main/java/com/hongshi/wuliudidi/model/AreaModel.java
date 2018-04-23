package com.hongshi.wuliudidi.model;

public class AreaModel {
	private String areaCode;
	private String areaText;
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getAreaText() {
		return areaText;
	}
	public void setAreaText(String areaText) {
		this.areaText = areaText;
	}
	public AreaModel Copy(){
		AreaModel m = new AreaModel();
		m.setAreaCode(this.areaCode);
		m.setAreaText(this.areaText);
		return m;
	}
}
