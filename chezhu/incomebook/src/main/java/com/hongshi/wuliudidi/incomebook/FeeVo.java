package com.hongshi.wuliudidi.incomebook;

public class FeeVo {

	private String statusTxt;//状态类型
	private double sumMoney;//总金额
	private String billTypeTxt;//账单类型
	private int status;//结算类型
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStatusTxt() {
		return statusTxt;
	}
	public void setStatusTxt(String statusTxt) {
		this.statusTxt = statusTxt;
	}
	public double getSumMoney() {
		return sumMoney;
	}
	public void setSumMoney(double sumMoney) {
		this.sumMoney = sumMoney;
	}
	public String getBillTypeTxt() {
		return billTypeTxt;
	}
	public void setBillTypeTxt(String billTypeTxt) {
		this.billTypeTxt = billTypeTxt;
	}
	
	
}
