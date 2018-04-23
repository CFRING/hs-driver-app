package com.hongshi.wuliudidi.wheelviewlib.wheelview.library.model;

/**
 * Created on 2016/2/1.
 */
public class AreaModelLib {
    /**  */
    private static final long serialVersionUID = 1L;
    /**  */
    private String id;
    /**
     * 地区名称
     */
    private String name;

    /**
     * 城市级别
     */
    private int               level;

    /**
     * 父级id
     */
    private String parentId;

    private String sortLetters;  //显示数据拼音的首字母

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
