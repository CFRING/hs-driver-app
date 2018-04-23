package com.hongshi.wuliudidi.model;

public class QrResultModel {
	// 运输任务ID
	private String taskId;
	// 过磅单号
	private String weighCode;
	// 空车重量
	private double emptyWeight;
	// 满车重量
	private double fullWeight;
	// 损耗重量
	private double lossWeight;
	// 净重
	private double netWeight;
	//车牌号
	private String licence;
	
	public String getLicence() {
		return licence;
	}

	public void setLicence(String licence) {
		this.licence = licence;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getWeighCode() {
		return weighCode;
	}

	public void setWeighCode(String weighCode) {
		this.weighCode = weighCode;
	}

	public double getEmptyWeight() {
		return emptyWeight;
	}

	public void setEmptyWeight(double emptyWeight) {
		this.emptyWeight = emptyWeight;
	}

	public double getFullWeight() {
		return fullWeight;
	}

	public void setFullWeight(double fullWeight) {
		this.fullWeight = fullWeight;
	}

	public double getLossWeight() {
		return lossWeight;
	}

	public void setLossWeight(double lossWeight) {
		this.lossWeight = lossWeight;
	}

	public double getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(double netWeight) {
		this.netWeight = netWeight;
	}
}
