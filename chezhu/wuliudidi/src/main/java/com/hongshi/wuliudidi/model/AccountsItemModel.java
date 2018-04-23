package com.hongshi.wuliudidi.model;

public class AccountsItemModel {
	/**  结算状态*/
    private int               settleType;

    /**  结算状态文字*/
    private String            settleTypeText;

    /**  收货单位*/
    private String            recvName;

    /**  发货单位*/
    private String            sendName;

    /**  货物名称*/
    private String            goodsName;

    /**  账单的钱*/
    private double            money;

    /**  钱的单位*/
    private String            moneyUnit;

    /**  账单ID*/
    private String            billId;

    /**  派车单Id*/
    private String            taskId;

    /**
     * 订单id
     */
    private String            orderId;
    /**
     * 发货单位
     */
    private String            senderCorporation;
    /**
     * 发货方
     */
    private String            senderName;
    /**
     * 发货方-电话
     */
    private String            senderPhone;
    /**
     * 发货方-省ID
     */
    private String            senderProvince;
    /**
     * 发货方-市ID
     */
    private String            senderCity;
    /**
     * 发货方-区ID
     */
    private String            senderDistrict;
    /**
     * 发货方-详细地址(区以后的)
     */
    private String            senderAddress;
    /**
     * 收货单位
     */
    private String            recipientCorporation;
    /**
     * 收货方
     */
    private String            recipientName;
    /**
     * 收货方-电话
     */
    private String            recipientPhone;
    /**
     * 收货方-省ID
     */
    private String            recipientProvince;
    /**
     * 收货方-市ID
     */
    private String            recipientCity;
    /**
     * 收货方-区ID
     */
    private String            recipientDistrict;

    /**
     * 发布单位id，gder是goodsowner的缩写
     */
    private String            gderId;

    /**
     * 用户头像
     */
    private String            userFace;

    /**  货物重量 or 体积*/
    private double            goodsAmount;

    /**  未完成:派车单位，已完成：结算单位*/
    private String            unit;
    
    /**所属日期*/
    private String dateStr; 

    public double getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(double goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getGderId() {
        return gderId;
    }

    public void setGderId(String gderId) {
        this.gderId = gderId;
    }

    public String getUserFace() {
        return userFace;
    }

    public void setUserFace(String userFace) {
        this.userFace = userFace;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSenderCorporation() {
        return senderCorporation;
    }

    public void setSenderCorporation(String senderCorporation) {
        this.senderCorporation = senderCorporation;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getSenderProvince() {
        return senderProvince;
    }

    public void setSenderProvince(String senderProvince) {
        this.senderProvince = senderProvince;
    }

    public String getSenderCity() {
        return senderCity;
    }

    public void setSenderCity(String senderCity) {
        this.senderCity = senderCity;
    }

    public String getSenderDistrict() {
        return senderDistrict;
    }

    public void setSenderDistrict(String senderDistrict) {
        this.senderDistrict = senderDistrict;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getRecipientCorporation() {
        return recipientCorporation;
    }

    public void setRecipientCorporation(String recipientCorporation) {
        this.recipientCorporation = recipientCorporation;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getRecipientProvince() {
        return recipientProvince;
    }

    public void setRecipientProvince(String recipientProvince) {
        this.recipientProvince = recipientProvince;
    }

    public String getRecipientCity() {
        return recipientCity;
    }

    public void setRecipientCity(String recipientCity) {
        this.recipientCity = recipientCity;
    }

    public String getRecipientDistrict() {
        return recipientDistrict;
    }

    public void setRecipientDistrict(String recipientDistrict) {
        this.recipientDistrict = recipientDistrict;
    }

    /**
     * 收货方-详细地址(区以后的)
     */
    private String recipientAddress;

    public int getSettleType() {
        return settleType;
    }

    public void setSettleType(int settleType) {
        this.settleType = settleType;
    }

    public String getSettleTypeText() {
        return settleTypeText;
    }

    public void setSettleTypeText(String settleTypeText) {
        this.settleTypeText = settleTypeText;
    }

    public String getRecvName() {
        return recvName;
    }

    public void setRecvName(String recvName) {
        this.recvName = recvName;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getMoneyUnit() {
        return moneyUnit;
    }

    public void setMoneyUnit(String moneyUnit) {
        this.moneyUnit = moneyUnit;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
}
