package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class WalletModel implements Serializable{
	private String	backNumber;
	//备用号码
	private String	cellphone;
	//手机号码
	private String	email;
	//邮箱
	private String	name;
	//用户名
	private String	position;
	//职位;
	private String	userFace;
	//用户头像
	private String	userId;
	//id
	//key 1基本账户，2保证金，3一卡通，4 运费，5油费，6收益，7信用度， 8水泥预付运费
	private Map<String, MoneyAccountModel> 	liabilityAcctMap;

	private String cifUserId;

	private List<BankcardModel> bankCardVOList;
	//银行卡列表

	private BillTotalVO billTotalVO;
	//油卡余额
	private String oilAmount;
	//轮胎余额
	private String tyreAmount;

	public String getTyreAmount() {
		return tyreAmount;
	}

	public void setTyreAmount(String tyreAmount) {
		this.tyreAmount = tyreAmount;
	}

	public String getOilAmount() {
		return oilAmount;
	}

	public void setOilAmount(String oilAmount) {
		this.oilAmount = oilAmount;
	}

	public BillTotalVO getBillTotalVO() {
		return billTotalVO;
	}

	public void setBillTotalVO(BillTotalVO billTotalVO) {
		this.billTotalVO = billTotalVO;
	}

	public String getBackNumber() {
		return backNumber;
	}
	public void setBackNumber(String backNumber) {
		this.backNumber = backNumber;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getUserFace() {
		return userFace;
	}
	public void setUserFace(String userFace) {
		this.userFace = userFace;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCifUserId() {
		return cifUserId;
	}
	public void setCifUserId(String cifUserId) {
		this.cifUserId = cifUserId;
	}
	public Map<String, MoneyAccountModel> getLiabilityAcctMap() {
		return liabilityAcctMap;
	}
	public void setLiabilityAcctMap(
			Map<String, MoneyAccountModel> liabilityAcctMap) {
		this.liabilityAcctMap = liabilityAcctMap;
	}

	public List<BankcardModel> getBankCardVOList() {
		return bankCardVOList;
	}

	public void setBankCardVOList(List<BankcardModel> bankCardVOList) {
		this.bankCardVOList = bankCardVOList;
	}

	public double getSum(String keyStr){
		try {
			double result = 0;
			MoneyAccountModel moneyAccount = liabilityAcctMap.get(keyStr);
			List<MoneyChildAccountModel> amountVOList = moneyAccount.getAmountVOList();
			for(int i = 0; i < amountVOList.size(); i++){
				result += amountVOList.get(i).getAmount();
			}
			return result;
		} catch (Exception e) {
			return 0;
		}
	}
}
