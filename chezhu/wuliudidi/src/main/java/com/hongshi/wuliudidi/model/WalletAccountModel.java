package com.hongshi.wuliudidi.model;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/18.
 */
public class WalletAccountModel {
    private int currentPage;
    private int itemCount;
    private List<String> month;
    private int pageSize;
    private int pageTotal;
    private Map<String, List<WalletAccountFlowModel>> map;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public List<String> getMonth() {
        return month;
    }

    public void setMonth(List<String> month) {
        this.month = month;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public Map<String, List<WalletAccountFlowModel>> getMap() {
        return map;
    }

    public void setMap(Map<String, List<WalletAccountFlowModel>> map) {
        this.map = map;
    }
}
