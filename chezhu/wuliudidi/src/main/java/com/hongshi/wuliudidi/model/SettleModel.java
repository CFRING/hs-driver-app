package com.hongshi.wuliudidi.model;

import java.io.Serializable;

/**
 * @author huiyuan
 * @version 1.0
 * @created 2018/1/4 15:33
 * @title 类的名称
 * @description 该类主要功能描述
 * @changeRecord：2018/1/4 15:33 modify by
 */
public class SettleModel implements Serializable {
    private String id;
    private String name;
    private String fee;

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
