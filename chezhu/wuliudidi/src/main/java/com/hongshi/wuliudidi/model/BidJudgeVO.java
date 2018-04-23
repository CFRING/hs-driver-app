package com.hongshi.wuliudidi.model;

public class BidJudgeVO  {
    //操作员ID
    private String optID;

    //评价等级 0 : 差 1： 中 2： 好
    private int judgeLevel;
    //评价内容
    private String content;

    private String bidItemID;

    //评价发表人
    private String userID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getBidItemID() {
        return bidItemID;
    }

    public void setBidItemID(String bidItemID) {
        this.bidItemID = bidItemID;
    }

    public String getOptID() {
        return optID;
    }

    public void setOptID(String optID) {
        this.optID = optID;
    }

    public int getJudgeLevel() {
        return judgeLevel;
    }

    public void setJudgeLevel(int judgeLevel) {
        this.judgeLevel = judgeLevel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
