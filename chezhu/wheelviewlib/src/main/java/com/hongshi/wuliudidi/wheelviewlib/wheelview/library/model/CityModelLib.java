package com.hongshi.wuliudidi.wheelviewlib.wheelview.library.model;

import java.util.List;

public class CityModelLib {
	private String name;
	private String id;
	private List<DistrictModelLib> districtList;
	
	public CityModelLib() {
		super();
	}

	public CityModelLib(String name, String id, List<DistrictModelLib> districtList) {
		super();
		this.name = name;
		this.id = id;
		this.districtList = districtList;
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

	public List<DistrictModelLib> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<DistrictModelLib> districtList) {
		this.districtList = districtList;
	}
}
