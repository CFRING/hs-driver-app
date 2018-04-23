package com.hongshi.wuliudidi.wheelviewlib.wheelview.library.model;

import java.util.List;

public class ProvinceModel {
	private String name;
	private String id;
	private List<CityModelLib> cityList;
	
	public ProvinceModel() {
		super();
	}

	public ProvinceModel(String name, String id, List<CityModelLib> cityList) {
		super();
		this.name = name;
		this.id = id;
		this.cityList = cityList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CityModelLib> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityModelLib> cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
		return "ProvinceModel [name=" + name + ", cityList=" + cityList + "]";
	}
	
}
