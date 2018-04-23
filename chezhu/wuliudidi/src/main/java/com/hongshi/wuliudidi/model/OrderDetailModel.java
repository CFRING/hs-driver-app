package com.hongshi.wuliudidi.model;

import java.io.Serializable;

/**
 * Created by huiyuan on 2017/9/15.
 */

public class OrderDetailModel implements Serializable {
    private String id;
    private String supplierShowName;
    private String supplierShowNickName;
    private String storePhoto;
    private String moneyStr;
    private byte payType;
    private String payTypeName;
    private byte status;
    //状态:1 未支付,2 等待回执,3 支付完成,4 已取消,5 支付失败,6 已退款
    private String statusName;
    private String product;
    //创建时间
    private String createDate;
    //付款说明
    private String remark;
    //联系商户（手机）
    private String cellPhone;
    //是否评价:0未评价,1已评价
    private byte evaluationType;
    //是否评价名称
    private String evaluationName;
    //付款人用户ID(车主)
    private String payerUid;
    //收款人用户ID（商家）
    private String payeeUid;
    //星级: 数字，如:1,2,3,4,5
    private byte star;
    //交易单号
    private String tradeId;
    //评价内容
    private String content;
    /**
     * 司机姓名
     */
    private String            driverName;
    /**
     * 司机电话
     */
    private String            driverPhone;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getSupplierShowName() {
        return supplierShowName;
    }

    public void setSupplierShowName(String supplierShowName) {
        this.supplierShowName = supplierShowName;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSupplierShowNickName() {
        return supplierShowNickName;
    }

    public void setSupplierShowNickName(String supplierShowNickName) {
        this.supplierShowNickName = supplierShowNickName;
    }

    public String getStorePhoto() {
        return storePhoto;
    }

    public void setStorePhoto(String storePhoto) {
        this.storePhoto = storePhoto;
    }

    public String getMoneyStr() {
        return moneyStr;
    }

    public void setMoneyStr(String moneyStr) {
        this.moneyStr = moneyStr;
    }

    public byte getPayType() {
        return payType;
    }

    public void setPayType(byte payType) {
        this.payType = payType;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public byte getEvaluationType() {
        return evaluationType;
    }

    public void setEvaluationType(byte evaluationType) {
        this.evaluationType = evaluationType;
    }

    public String getEvaluationName() {
        return evaluationName;
    }

    public void setEvaluationName(String evaluationName) {
        this.evaluationName = evaluationName;
    }

    public String getPayerUid() {
        return payerUid;
    }

    public void setPayerUid(String payerUid) {
        this.payerUid = payerUid;
    }

    public String getPayeeUid() {
        return payeeUid;
    }

    public void setPayeeUid(String payeeUid) {
        this.payeeUid = payeeUid;
    }

    public byte getStar() {
        return star;
    }

    public void setStar(byte star) {
        this.star = star;
    }
}
