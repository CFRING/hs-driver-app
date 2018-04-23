package com.hongshi.wuliudidi.model;

import java.io.Serializable;

/**
 * Created by huiyuan on 2017/4/27.
 */

public class AreaQueryVo implements Serializable {
    String lng;
    String lat;

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public String getLat() {
        return lat;
    }
}
