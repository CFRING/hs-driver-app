package com.hongshi.wuliudidi.utils;

public class GoodsBubbleMsg {
	String provinceAndCity = "";
	String addr = "";

	public String getProvinceAndCity() {
		return provinceAndCity;
	}

	public String getAddr() {
		return addr;
	}

	public void setMessage(String province, String city, String addr) {
		if (city == null || addr == null)
		{
			return;
		}

		if (province == null) {
			// 如果省份为空，则无须显示省份，详细地址 = 地址 - 城市
			this.provinceAndCity = city;
		} else {// 如果省份不为空，需要显示省份，详细地址 = 地址 - 省份 -城市
			this.provinceAndCity = province + " " + city;
		}

		this.addr = addr;
	}

}
