package com.hongshi.wuliudidi.model;

import java.io.Serializable;

/**
 * Created by huiyuan on 2016/3/30.
 */
public class TaskStatusTrackVO implements Serializable {
    private String id;
    private Long serialVersionUID;
    private int sortOrder;
    private int status;
    private String statusDesc;
    private String taskId;
    private String tstName;

    public void setTaskId(String taskId){
        this.taskId = taskId;
    }
    public String getTaskId(){
        return this.taskId;
    }
    public void setTstName(String name){
        this.tstName = name;
    }
    public String getTstName(){
        return this.tstName;
    }

    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }

    public void setStatusDesc(String desc){
        this.statusDesc = desc;
    }

    public String getStatusDesc(){
        return this.statusDesc;
    }

    public void setSortOrder(int order){
        this.sortOrder = order;
    }

    public int getSortOrder(){
        return this.sortOrder;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return this.id;
    }

    public void setSerialVersionUID(Long uid){
        this.serialVersionUID = uid;
    }

    public Long getSerialVersionUID(){
        return this.serialVersionUID;
    }
}
