package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by huiyuan on 2017/4/18.
 */

public class MyWalletDetailModel implements Serializable {

    private Date beginDate;
    //时间区间
    private String date;
    private Date endDate;
    //物料
    private String goodsCategory;
    //物料车次明细
    private List<KVModel> list;

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getGoodsCategory() {
        return goodsCategory;
    }

    public void setGoodsCategory(String goodsCategory) {
        this.goodsCategory = goodsCategory;
    }

    public List<KVModel> getList() {
        return list;
    }

    public void setList(List<KVModel> list) {
        this.list = list;
    }
}
