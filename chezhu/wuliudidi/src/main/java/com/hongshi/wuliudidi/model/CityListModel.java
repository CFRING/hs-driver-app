package com.hongshi.wuliudidi.model;

/**
 * Created by bian on 2017/3/26 9:50.
 */

public class CityListModel {

    /**  */
    private String            id;
    /**
     * 地区名称
     */
    private String            name;

    /**
     * 城市级别
     */
    private int               level;

    /**
     * 父级id
     */
    private String            parentId;

    //显示数据拼音的首字母
    private String sortLetters;

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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
