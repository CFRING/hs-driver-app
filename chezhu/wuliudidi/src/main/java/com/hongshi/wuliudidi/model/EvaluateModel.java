package com.hongshi.wuliudidi.model;

/**
 * Created by he on 2017/6/27.
 */

public class EvaluateModel {
    private String id;
    private String name;
    private String cellPhone;
    private String userFace;
    private float star;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getUserFace() {
        return userFace;
    }

    public void setUserFace(String userFace) {
        this.userFace = userFace;
    }
}
