package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.Map;

/**
 * app端专用的Map DTO，使用end属性标记该Map存放的数据是否是最后一屏
 * 
 * @author haiyang.jiang  
 * @version $Id: AppListDTO.java, v 0.1 2015年8月18日 下午3:09:15 niya Exp $
 */
public class AppMapVO<K, V> implements Serializable {
    private static final long serialVersionUID = -4266303631786017574L;

    //是否已到最后一页
    private boolean           end              = true;

    private Map            items;

    public AppMapVO() {
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public Map getItems() {
        return items;
    }

    public void setItems(Map items) {
        this.items = items;
    }
}
