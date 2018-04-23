package com.hongshi.wuliudidi.model;

import java.util.Map;

public class AccountModel {
	/**六个月的开始月份*/
	private int firstMouth;
	/**净资产*/
	private double sellterMoney;
	/**六个月的交易额，倒序*/
	Map<Integer, Double> sixMouthMoney;
	/**交易数*/
	private int taskCount;
	/**待发放金额*/
	private double unSellterMoney;
	
	public int getFirstMouth() {
		return firstMouth;
	}
	public void setFirstMouth(int firstMouth) {
		this.firstMouth = firstMouth;
	}
	public double getSellterMoney() {
		return sellterMoney;
	}
	public void setSellterMoney(double sellterMoney) {
		this.sellterMoney = sellterMoney;
	}
	public Map<Integer, Double> getSixMouthMoney() {
		return sixMouthMoney;
	}
	public void setSixMouthMoney(Map<Integer, Double> sixMouthMoney) {
		this.sixMouthMoney = sixMouthMoney;
	}
	public int getTaskCount() {
		return taskCount;
	}
	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}
	public double getUnSellterMoney() {
		return unSellterMoney;
	}
	public void setUnSellterMoney(double unSellterMoney) {
		this.unSellterMoney = unSellterMoney;
	}
}
