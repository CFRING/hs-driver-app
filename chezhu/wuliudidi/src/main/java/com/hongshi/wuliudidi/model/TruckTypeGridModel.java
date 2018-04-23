package com.hongshi.wuliudidi.model;

public class TruckTypeGridModel {
	private int drawableId;
	private TruckType type;
	private String typeName;
	private boolean isSelected;

	public TruckTypeGridModel(int drawableId, TruckType type){
		this.drawableId = drawableId;
		this.type = type;
		switch(this.type){
		case quangua:
			typeName = "全挂";
			break;
		case bangua:
			typeName = "半挂";
			break;
		case nongyong:
			typeName = "农用车";
			break;
		case xiangshi:
			typeName = "厢式";
			break;
		case dilan:
			typeName = "低栏";
			break;
		case gaolan:
			typeName = "高栏";
			break;
		case pingban:
			typeName = "平板";
			break;
		case cangzha:
			typeName = "仓栅式";
			break;
		case guanche:
			typeName = "罐车式";
			break;
		}
	}
	
	public int getDrawableId() {
		return drawableId;
	}
	public void setDrawableId(int drawableId) {
		this.drawableId = drawableId;
	}
	public TruckType getType() {
		return type;
	}
	public void setType(TruckType type) {
		this.type = type;
	}
	public String getTypeName(){
		return typeName;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public enum TruckType{
		quangua, bangua, nongyong,
		xiangshi, dilan, gaolan, pingban, cangzha, guanche;
	}
}
