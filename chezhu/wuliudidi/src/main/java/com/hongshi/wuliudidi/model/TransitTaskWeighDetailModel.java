package com.hongshi.wuliudidi.model;
import java.io.Serializable;
import java.util.Date;

/**
 * 运输报表-详情。单个过磅单的详情
 * 
 * @author haiyang.jiang  
 * @version $Id: TransitTaskAppViewDTO.java, v 0.1 2015年8月18日 下午4:45:57 niya Exp $
 */
public class TransitTaskWeighDetailModel implements Serializable {
    private static final long serialVersionUID = 2085057661356784412L;

    //	派车单号
    private String            taskId;

    //	实际运量
    private double            realAmount;

    //	过磅单号
    private String            weighCode;

    //	收货单位 
    private String            recvName;

    //	发货单位 
    private String            sendName;

    //	车牌号码
    private String            truckNum;
    //	驾驶员
    private String            driverName;
    //	空车过磅
    private double            emptyWeight;

    //	重车过磅
    private double            fullWeight;

    //货物量单位（外）：1千克 2件 3立方米
//      private String settleUnitText;
    private String            unitText;
    //	验收人
    //	过磅人

	//	付款方式 
    private int               payType;
    private String            payTypeText;

    //	创建时间
    private Date              gmtCreate;
    private double lossWeight;//货差
    public double getLossWeight() {
		return lossWeight;
	}

	public void setLossWeight(double lossWeight) {
		this.lossWeight = lossWeight;
	}

	public String getUnitText() {
		return unitText;
	}

	public void setUnitText(String unitText) {
		this.unitText = unitText;
	}

    public String getWeighCode() {
        return weighCode;
    }

    public void setWeighCode(String weighCode) {
        this.weighCode = weighCode;
    }

    public String getRecvName() {
        return recvName;
    }

    public void setRecvName(String recvName) {
        this.recvName = recvName;
    }

    public double getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(double realAmount) {
        this.realAmount = realAmount;
    }

    public double getEmptyWeight() {
        return emptyWeight;
    }

    public void setEmptyWeight(double emptyWeight) {
        this.emptyWeight = emptyWeight;
    }

    public double getFullWeight() {
        return fullWeight;
    }

    public void setFullWeight(double fullWeight) {
        this.fullWeight = fullWeight;
    }

    public String getPayTypeText() {
        return payTypeText;
    }

    public void setPayTypeText(String payTypeText) {
        this.payTypeText = payTypeText;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getTruckNum() {
        return truckNum;
    }

    public void setTruckNum(String truckNum) {
        this.truckNum = truckNum;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

}
