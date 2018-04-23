package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.Date;

public class TradeRecord4AppVO implements Serializable {

    private static final long serialVersionUID = -582167976183580020L;
    /**
     * ��¼ID
     */
    private String            id;
    /**
     * ������
     */
    private String            outerId;
    /**
     * ���׽��
     */
    private Double            money;
    /**
     * ���:2017
     */
    private String            year;
    /**
     * �·� :4
     */
    private String            month;
    /**
     * ʱ��: 12:28
     */
    private String            time;
    /**
     * ����: ����
     */
    private String            week;
    /**
     * ���׺�
     */
    private String            tradeId;
    /**
     * ��������
     */
    private String            tradeName;
    /**
     *
     */
    private String            status;
    /**
     * ����ʱ��
     */
    private Date              gmtCreate;
    /**
     * 1.���� 2.֧��
     */
    private Integer           recordType;

    /**
     * ������Ϣ:�ֽ�����˻� ����
     */
    private String            acctTypeText;
    /**
     * ���׼�¼���:0 �ǵ��̽��׼�¼,1 ���̽��׼�¼
     */
    private Integer           category;

    /**
     * ��Ӧ������
     */
    private String            supplierShowName;
    private String supplierShowNickName;

    /**
     * ��Ӧ��ͼƬ
     */
    private String            storePhoto;
    /**
     * ����˵��
     */
    private String            remark;
    /**
     * ����ʱ��
     */
    private String            createDate;
    /**
     * ˾������
     */
    private String            driverName;
    /**
     * ˾���ֻ���
     */
    private String            driverPhone;
    /**
     * ��Ʒ��Ϣ
     */
    private String            product;
    /**
     * �̻�ID
     */
    private String            payeeUid;

    private String moneyStr;

    //���Ͳ��������ڶ���δ���֮ǰΪtrue
    private boolean cancelable;
    //revoke=true (��������Ϊ: 2.�̳����ѣ�1.ɨ������ ʹ�ô��ֶ�
    private boolean revoke;

    private String            payType;
    private String            payTypeName;
    /**
     * ��������: 1 ����ɨ��,����֧��,2 ˾��ɨ��,�������� ,
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
