package com.hongshi.wuliudidi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *  派车单详情
 * 
 * @author jianghaiyang
 * @version $Id: TransitTaskDetailVO.java, v 0.1 2015年9月6日 上午9:26:57 niya Exp $
 */
public class TransitTaskDetailModel implements Serializable {
    private static final long serialVersionUID = -5151447097838153648L;

    /**
     * 状态：1未开始，2运输中，3已签收 4，等待审核 5.复核通过 6.货主驳回 7.调解通过 8.复核超时 9.nc对接通过 99.强制作废
     */
    private int               status;
    //状态：1未开始，2运输中，3已完成
    private String            statusText;

    //发货方地址
    private String            sendAddr;

    //收货方地址
    private String            recvAddr;

    //运输里程，单位m
    private int               distance;

    //发货地经度
    private double            sendLng;

    //发货地纬度
    private double            sendLat;

    //收货地经度
    private double            recvLng;

    //收货地纬度
    private double            recvLat;

    //  发货时间
    private Date              gmtStart;

    //  到货时间
    private Date              gmtEnd;

    //    车牌号
    private String            truckNumber;

    //货物名
    private String            goodsName;
    //    货物重量 or 体积
    private double            goodsAmount;

    //计量量单位
    private String               unit;

	private String            unitText;
	
	//计价单位
	private String			settleUnit;
	
	private String			settleUnitText;

    //型号
    private String            modelNumber;

    //    备注
    private String            remark;

    //    收货人名称
    private String            recvName;

    //    收货人电话
    private String            recvTel;

    //    发货人名称
    private String            sendName;

    //    发货人电话
    private String            sendTel;

    //    竞价条目ID
    private String            bidItemId;

    //    运输任务ID
    private String            transitTaskId;

    //    运输任务创建时间
    private Date              gmtCreate;

    // 开始运输时间
    private Date              gmtStartTransit;

    // 货物送达时间
    private Date              gmtSignup;
    
    //发货地省份
    private String			sendProvince;
    
    //发货地城市
    private String			sendCity;
    
    //收货地省份
    private String			recvProvince;
    
    //收货地城市
    private String			recvCity;
    
    private ReceiptReviewModel taskVoucherReviewVO;

    private List<TaskStatusTrackVO> taskStatusTrackVO;

    /**  提货凭证*/
    private String                  deliveryProof;

    /**  上传提货凭证人Id*/
    private String                  deliveryProofUid;
    /**  上传提货凭证人名称*/
    private String                  deliveryProofName;

    /**  上传提货凭证时间*/
    private Date                    gmtDelivery;

    public String getDeliveryProof() {
        return deliveryProof;
    }

    public void setDeliveryProof(String deliveryProof) {
        this.deliveryProof = deliveryProof;
    }

    public String getDeliveryProofUid() {
        return deliveryProofUid;
    }

    public void setDeliveryProofUid(String deliveryProofUid) {
        this.deliveryProofUid = deliveryProofUid;
    }

    public String getDeliveryProofName() {
        return deliveryProofName;
    }

    public void setDeliveryProofName(String deliveryProofName) {
        this.deliveryProofName = deliveryProofName;
    }

    public Date getGmtDelivery() {
        return gmtDelivery;
    }

    public void setGmtDelivery(Date gmtDelivery) {
        this.gmtDelivery = gmtDelivery;
    }

    public void setTSTVOList(List<TaskStatusTrackVO> tSTVOList){
        this.taskStatusTrackVO = tSTVOList;
    }

    public List<TaskStatusTrackVO> getTSTVOList(){
        return this.taskStatusTrackVO;
    }

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
    public String getSendProvince() {
		return sendProvince;
	}

	public void setSendProvince(String sendProvince) {
		this.sendProvince = sendProvince;
	}

	public String getSendCity() {
		return sendCity;
	}

	public void setSendCity(String sendCity) {
		this.sendCity = sendCity;
	}

	public String getRecvProvince() {
		return recvProvince;
	}

	public void setRecvProvince(String recvProvince) {
		this.recvProvince = recvProvince;
	}

	public String getRecvCity() {
		return recvCity;
	}

	public void setRecvCity(String recvCity) {
		this.recvCity = recvCity;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
     * 状态：1未开始，2运输中，3已签收 4，等待审核 5.复核通过 6.货主驳回 7.调解通过 8.复核超时 9.nc对接通过 99.强制作废
     */
	public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getSendAddr() {
        return sendAddr;
    }

    public void setSendAddr(String sendAddr) {
        this.sendAddr = sendAddr;
    }

    public String getRecvAddr() {
        return recvAddr;
    }

    public void setRecvAddr(String recvAddr) {
        this.recvAddr = recvAddr;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public double getSendLng() {
        return sendLng;
    }

    public void setSendLng(double sendLng) {
        this.sendLng = sendLng;
    }

    public double getSendLat() {
        return sendLat;
    }

    public void setSendLat(double sendLat) {
        this.sendLat = sendLat;
    }

    public double getRecvLng() {
        return recvLng;
    }

    public void setRecvLng(double recvLng) {
        this.recvLng = recvLng;
    }

    public double getRecvLat() {
        return recvLat;
    }

    public void setRecvLat(double recvLat) {
        this.recvLat = recvLat;
    }

    public Date getGmtStart() {
        return gmtStart;
    }

    public void setGmtStart(Date gmtStart) {
        this.gmtStart = gmtStart;
    }

    public Date getGmtEnd() {
        return gmtEnd;
    }

    public void setGmtEnd(Date gmtEnd) {
        this.gmtEnd = gmtEnd;
    }

    public String getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public double getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(double goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public String getUnitText() {
        return unitText;
    }

    public void setUnitText(String unitText) {
        this.unitText = unitText;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRecvName() {
        return recvName;
    }

    public void setRecvName(String recvName) {
        this.recvName = recvName;
    }

    public String getRecvTel() {
        return recvTel;
    }

    public void setRecvTel(String recvTel) {
        this.recvTel = recvTel;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getSendTel() {
        return sendTel;
    }

    public void setSendTel(String sendTel) {
        this.sendTel = sendTel;
    }

    public String getBidItemId() {
        return bidItemId;
    }

    public void setBidItemId(String bidItemId) {
        this.bidItemId = bidItemId;
    }

    public String getTransitTaskId() {
        return transitTaskId;
    }

    public void setTransitTaskId(String transitTaskId) {
        this.transitTaskId = transitTaskId;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtStartTransit() {
        return gmtStartTransit;
    }

    public void setGmtStartTransit(Date gmtStartTransit) {
        this.gmtStartTransit = gmtStartTransit;
    }

    public Date getGmtSignup() {
        return gmtSignup;
    }

    public void setGmtSignup(Date gmtSignup) {
        this.gmtSignup = gmtSignup;
    }
    
    public String getSendAddrWhole(){
    	return sendProvince + sendCity + sendAddr;
	}
    
	public String getRecvAddrWhole(){
		return recvProvince + recvCity + recvAddr;
	}

	public ReceiptReviewModel getTaskVoucherReviewVO() {
		return taskVoucherReviewVO;
	}

	public void setTaskVoucherReviewVO(ReceiptReviewModel taskVoucherReviewVO) {
		this.taskVoucherReviewVO = taskVoucherReviewVO;
	}

	public String getSettleUnit() {
		return settleUnit;
	}

	public void setSettleUnit(String settleUnit) {
		this.settleUnit = settleUnit;
	}

	public String getSettleUnitText() {
		return settleUnitText;
	}

	public void setSettleUnitText(String settleUnitText) {
		this.settleUnitText = settleUnitText;
	}
}
