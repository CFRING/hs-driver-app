package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huiyuan on 2017/3/29.
 */

public class PageInfo4App implements Serializable {
    //是否已到最后一页
    private boolean           isEnd            = true;

    private List<TruckAuthAppVO> items            = new ArrayList<>();

    //当前页
    private int               currentPage;

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public List<TruckAuthAppVO> getItems() {
        return items;
    }

    public void setItems(List<TruckAuthAppVO> items) {
        this.items = items;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
