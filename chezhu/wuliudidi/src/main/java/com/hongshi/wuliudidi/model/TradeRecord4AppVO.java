package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.Date;

public class TradeRecord4AppVO implements Serializable {

    private static final long serialVersionUID = -582167976183580020L;
    /**
     * 记录ID
     */
    private String            id;
    /**
     * 订单号
     */
    private String            outerId;
    /**
     * 交易金额
     */
    private Double            money;
    /**
     * 年份:2017
     */
    private String            year;
    /**
     * 月份 :4
     */
    private String            month;
    /**
     * 时间: 12:28
     */
    private String            time;
    /**
     * 星期: 周日
     */
    private String            week;
    /**
     * 交易号
     */
    private String            tradeId;
    /**
     * 交易名称
     */
    private String            tradeName;
    /**
     *
     */
    private String            status;
    /**
     * 创建时间
     */
    private Date              gmtCreate;
    /**
     * 1.收入 2.支出
     */
    private Integer           recordType;

    /**
     * 付款信息:现金余额账户 类型
     */
    private String            acctTypeText;
    /**
     * 交易记录类别:0 非电商交易记录,1 电商交易记录
     */
    private Integer           category;

    /**
     * 供应商名称
     */
    private String            supplierShowName;
    private String supplierShowNickName;

    /**
     * 供应商图片
     */
    private String            storePhoto;
    /**
     * 付款说明
     */
    private String            remark;
    /**
     * 创建时间
     */
    private String            createDate;
    /**
     * 司机姓名
     */
    private String            driverName;
    /**
     * 司机手机号
     */
    private String            driverPhone;
    /**
     * 商品信息
     */
    private String            product;
    /**
     * 商户ID
     */
    private String            payeeUid;

    private String moneyStr;

    //提油操作，且在订单未完成之前为true
    private boolean cancelable;
    //revoke=true (交易类型为: 2.商城消费，1.扫码消费 使用此字段
    private boolean revoke;

    private String            payType;
    private String            payTypeName;
    /**
     * 操作类型: 1 车主扫码,车主支付,2 司机扫码,车主代付 ,
     */
    private Byte              operateType;

    public Byte getOperateType() {
        return operateType;
    }

    public void setOperateType(Byte operateType) {
        this.operateType = operateType;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }

    public boolean getRevoke() {
        return revoke;
    }

    public void setRevoke(boolean revoke) {
        this.revoke = revoke;
    }

    public String getSupplierShowNickName() {
        return supplierShowNickName;
    }

    public void setSupplierShowNickName(String supplierShowNickName) {
        this.supplierShowNickName = supplierShowNickName;
    }

    public boolean getCancelable() {
        return cancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public String getMoneyStr() {
        return moneyStr;
    }

    public void setMoneyStr(String moneyStr) {
        this.moneyStr = moneyStr;
    }

    public String getPayeeUid() {
        return payeeUid;
    }

    public void setPayeeUid(String payeeUid) {
        this.payeeUid = payeeUid;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOuterId() {
        return outerId;
    }

    public void setOuterId(String outerId) {
        this.outerId = outerId;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getAcctTypeText() {
        return acctTypeText;
    }

    public void setAcctTypeText(String acctTypeText) {
        this.acctTypeText = acctTypeText;
    }

    public String getSupplierShowName() {
        return supplierShowName;
    }

    public void setSupplierShowName(String supplierShowName) {
        this.supplierShowName = supplierShowName;
    }

    public String getStorePhoto() {
        return storePhoto;
    }

    public void setStorePhoto(String storePhoto) {
        this.storePhoto = storePhoto;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

}
