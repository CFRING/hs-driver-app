package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huiyuan on 2017/9/16.
 */

public class SupplierTradeRecordModel implements Serializable {
    private String dateTime;
    private List<SupplierTradeRecordItemModel> qrCodeOrderQueryVOs;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public List<SupplierTradeRecordItemModel> getQrCodeOrderQueryVOs() {
        return qrCodeOrderQueryVOs;
    }

    public void setQrCodeOrderQueryVOs(List<SupplierTradeRecordItemModel> qrCodeOrderQueryVOs) {
        this.qrCodeOrderQueryVOs = qrCodeOrderQueryVOs;
    }

}
