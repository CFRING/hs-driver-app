package com.hongshi.wuliudidi.model;

import java.util.Map;

public class AreaBackstageModel {
	private  Map<String, Map<String, Map<String, String>>> provinceIDCityMap;

	public Map<String, Map<String, Map<String, String>>> getProvinceIDCityMap() {
		return provinceIDCityMap;
	}

	public void setProvinceIDCityMap(
			Map<String, Map<String, Map<String, String>>> provinceIDCityMap) {
		this.provinceIDCityMap = provinceIDCityMap;
	}
	
}
