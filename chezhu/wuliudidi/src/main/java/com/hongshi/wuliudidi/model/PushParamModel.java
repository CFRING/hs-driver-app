package com.hongshi.wuliudidi.model;

import java.io.Serializable;

/**
 * Created by huiyuan on 2016/8/27.
 */
public class PushParamModel implements Serializable{
    private String jumpTarget;
    private String params;

    public String getJumpTarget() {
        return jumpTarget;
    }

    public void setJumpTarget(String jumpTarget) {
        this.jumpTarget = jumpTarget;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
