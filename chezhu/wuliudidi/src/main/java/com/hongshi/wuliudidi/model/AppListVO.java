package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AppListVO implements Serializable {
    private static final long serialVersionUID = -4266303631786017574L;

    private boolean           end              = true;

    private List<TaskOrderModel>           items            = new ArrayList<TaskOrderModel>();

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public List<TaskOrderModel> getItems() {
        return items;
    }

    public void setItems(List<TaskOrderModel> items) {
        this.items = items;
    }
}
