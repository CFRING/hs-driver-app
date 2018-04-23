package com.hongshi.wuliudidi.model;

import java.io.Serializable;

/**
 * Created by huiyuan on 2017/4/4.
 */

public class BillTmpltVo implements Serializable {
    //费用分摊
    private String templateStr;
    //显示的价格价格
    private double totalFee;
    //价格模板ID
    private String billTmpltId;

    public String getTemplateStr() {
        return templateStr;
    }

    public void setTemplateStr(String templateStr) {
        this.templateStr = templateStr;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public String getBillTmpltId() {
        return billTmpltId;
    }

    public void setBillTmpltId(String billTmpltId) {
        this.billTmpltId = billTmpltId;
    }
}
