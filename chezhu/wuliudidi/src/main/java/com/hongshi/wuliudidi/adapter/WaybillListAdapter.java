package com.hongshi.wuliudidi.adapter;

import java.util.List;


import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.AuctionDetailsActivity;
import com.hongshi.wuliudidi.activity.AuctionTruckListActivity;
import com.hongshi.wuliudidi.activity.RejectActivity;
import com.hongshi.wuliudidi.activity.TruckingOrderDetailsActivity;
import com.hongshi.wuliudidi.model.ConsignDetailModel;
import com.hongshi.wuliudidi.model.TaskOrderModel;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.GoodsDetailsView;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WaybillListAdapter extends BaseAdapter {

	private Context mContext;
	private List<TaskOrderModel> mOrderList;
	private ConsignDetailModel mConsignDetailModel;
	//托运单是否已完成
	private boolean isEnd = false;
	private String mGoodsName = "";
	
	private ViewHolder viewHolder ;
	public WaybillListAdapter(Context context, List<TaskOrderModel> mOrderList,ConsignDetailModel mConsignDetailModel) {
		this.mOrderList = mOrderList;
		this.mContext = context;
		this.mConsignDetailModel = mConsignDetailModel;
	}

	public void addList(List<TaskOrderModel> mOrderList){
		this.mOrderList.addAll(mOrderList);
	}
	@Override
	public int getCount() {
		return mOrderList.size();
	}

	@Override
	public TaskOrderModel getItem(int position) {
		return mOrderList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = View.inflate(mContext, R.layout.win_bid_item, null);
		viewHolder = new ViewHolder();
		if(position == 0){
			//整个竞价单的货运起终点
			LinearLayout mHeadView = (LinearLayout) convertView.findViewById(R.id.consignment_msg_head_view);
			mHeadView.setVisibility(View.VISIBLE);
			
			//查看竞拍车辆
			viewHolder.mLookTruckLayout = (RelativeLayout) convertView.findViewById(R.id.look_truck_layout);
			viewHolder.pcd_layout = (LinearLayout) convertView.findViewById(R.id.pcd_layout);
			
			viewHolder.mStatus = (TextView) convertView.findViewById(R.id.status);
			viewHolder.mAllCountText = (TextView) convertView.findViewById(R.id.all_count_text);
			viewHolder.mAllCount = (TextView) convertView.findViewById(R.id.all_count);
			viewHolder.mFinishedText = (TextView) convertView.findViewById(R.id.finished_text);
			viewHolder.mFinished = (TextView) convertView.findViewById(R.id.finished);
			viewHolder.mRestAmountText = (TextView) convertView.findViewById(R.id.rest_amount_text);
			viewHolder.mRestAmount = (TextView) convertView.findViewById(R.id.rest_amount);
			viewHolder.mAuctionPrice = (TextView) convertView.findViewById(R.id.price_);
			viewHolder.mGoodsDetailsView = (GoodsDetailsView)convertView.findViewById(R.id.auction_number_view);
			LinearLayout bidPriceAndTruckLayout = (LinearLayout) convertView.findViewById(R.id.bidprice_trucks_layout);
			if(CommonRes.roleType == 1){
				//如果用为普通司机
				bidPriceAndTruckLayout.setVisibility(View.GONE);
			}
			
			viewHolder.mGoodsDetailsView.setAuctionNumber(mConsignDetailModel.getAuctionId());
			TextView auctionNumberTextView = viewHolder.mGoodsDetailsView.getAuctionNumberTextView();
			//运输状态
			viewHolder.mStatus.setText(mConsignDetailModel.getShipmentStatusText());
			String assignUnitText = mConsignDetailModel.getAssignUnitText();
			viewHolder.mAllCountText.setText("运输总量("+ assignUnitText +")");
			viewHolder.mFinishedText.setText("已运量("+ assignUnitText +")");
			viewHolder.mRestAmountText.setText("剩余量("+ assignUnitText +")");
			viewHolder.mAuctionPrice.setText(Util.formatDoubleToString(mConsignDetailModel.getBidPrice(), "元")
					+ "元/" + mConsignDetailModel.getSettleUnitText());//中标价格
			viewHolder.mAllCount.setText(Util.formatDoubleToString(mConsignDetailModel.getTotalAmount(), assignUnitText));
			viewHolder.mFinished.setText(Util.formatDoubleToString(mConsignDetailModel.getFinishedAmount(), assignUnitText));
			viewHolder.mRestAmount.setText(Util.formatDoubleToString(mConsignDetailModel.getRestAmount(), assignUnitText));
			mGoodsName = mConsignDetailModel.getGoodsName();
			viewHolder.mGoodsDetailsView.setStartCity(mConsignDetailModel.getSendAddr());
			viewHolder.mGoodsDetailsView.setEndCity(mConsignDetailModel.getRecvAddr());
			viewHolder.mGoodsDetailsView.setGoodsName(mGoodsName);
			viewHolder.mGoodsDetailsView.setGoodsWeight(
					Util.formatDoubleToString(mConsignDetailModel.getGoodsAmount(), mConsignDetailModel.getAssignUnit())
					+ mConsignDetailModel.getAssignUnitText());
			// 显示竞拍单号
			viewHolder.mGoodsDetailsView.showAuctionOrder();
			// 隐藏竞拍人数
			viewHolder.mGoodsDetailsView.hideJoinView();
			viewHolder.mGoodsDetailsView.setHideTime();
			viewHolder.mGoodsDetailsView.showArrow();
			//第一条运输跳转到派车单详情
			if(null == mOrderList.get(position).getPlanId()){
				viewHolder.pcd_layout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext,TruckingOrderDetailsActivity.class);
						intent.putExtra("transitId", mOrderList.get(0).getTransitTaskId());
						mContext.startActivity(intent);
					}
				});
			}
			auctionNumberTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, AuctionDetailsActivity.class);
					intent.putExtra("auctionId", mConsignDetailModel.getAuctionId());
					mContext.startActivity(intent);
				}
			});
			viewHolder.mLookTruckLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext,AuctionTruckListActivity.class);
					intent.putExtra("bidId", mConsignDetailModel.getBidItemId());
					mContext.startActivity(intent);	
				}
			});
		}
		//item中的按钮
		viewHolder.reject_view = (TextView) convertView.findViewById(R.id.reject_view);
		//发货时间控件
		viewHolder.orderTypeName = (TextView) convertView.findViewById(R.id.order_type_name);
		//货物名称以及运量
		viewHolder.goods_name = (TextView) convertView.findViewById(R.id.name_goods);
		//底部运输车辆
		viewHolder.buttom_type_name = (TextView) convertView.findViewById(R.id.buttom_type_name);
		viewHolder.task_order_state = (TextView) convertView.findViewById(R.id.task_order_state);
		viewHolder.fact_unit_text = (TextView) convertView.findViewById(R.id.fact_unit_text);
		viewHolder.fact_amount_text = (TextView) convertView.findViewById(R.id.fact_amount_text);
		viewHolder.send_time = (TextView) convertView.findViewById(R.id.send_time);
		viewHolder.receive_time = (TextView) convertView.findViewById(R.id.receive_time);
		viewHolder.fact_layout = (RelativeLayout) convertView.findViewById(R.id.fact_layout);
		viewHolder.mRightIcon = (ImageView) convertView.findViewById(R.id.right_icon);
			
		final TaskOrderModel taskOrderModel = mOrderList.get(position);
		if(mConsignDetailModel.getAssignUnit().equals("TRUCK")){
			viewHolder.fact_layout.setVisibility(View.VISIBLE);
			viewHolder.fact_unit_text.setText("实际运量(" + mConsignDetailModel.getSettleUnitText() + ")");
			viewHolder.fact_amount_text.setText(Util.formatDoubleToString(mConsignDetailModel.getSettleAmount(),
					mConsignDetailModel.getSettleUnitText()));
		}else{
			viewHolder.fact_layout.setVisibility(View.GONE);
		}
		final String transitTaskId = taskOrderModel.getTransitTaskId();
		final String license = taskOrderModel.getTruckNum();
		
		if(position == mOrderList.size() - 1 && isEnd){
			//托运信息的完成时间
			LinearLayout mBottonView = (LinearLayout) convertView.findViewById(R.id.consign_finish_time_layout);
			mBottonView.setVisibility(View.VISIBLE);
			TextView tydNumber = (TextView) convertView.findViewById(R.id.tyd_number);
			TextView creatTime = (TextView) convertView.findViewById(R.id.creat_time);
			TextView finishTime = (TextView) convertView.findViewById(R.id.finish_time);
			TextView payWayText = (TextView) convertView.findViewById(R.id.pay_way_text);
			
			tydNumber.setText("托运单号： " + this.TYDNumber);
			creatTime.setText("创建时间： " + this.creatTime);
			if(this.finishTime.length() > 0){
				finishTime.setText("完成时间：" + this.finishTime);
			}else{
				finishTime.setVisibility(View.GONE);
			}
			
			if(this.payWay.length() > 0){
				payWayText.setText(this.payWay);
				payWayText.setVisibility(View.VISIBLE);
			}else{
				payWayText.setVisibility(View.GONE);
			}
		}
		
		if(taskOrderModel.getStatus() == 6){
			//运单异常，货主驳回
			viewHolder.reject_view.setVisibility(View.VISIBLE);
			viewHolder.reject_view.setBackgroundResource(R.drawable.reject_style);
			viewHolder.reject_view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//查看驳回原因
					Intent intent = new Intent(mContext,RejectActivity.class);
					intent.putExtra("taskId", transitTaskId);
					intent.putExtra("license", license);
					mContext.startActivity(intent);
				}
			});
		}
       if(null != taskOrderModel.getPlanId()){
		   String unit = taskOrderModel.getAssignUnitText();
		   String planAmount = Util.formatDoubleToString(taskOrderModel.getPlanAmount(), unit);
    	   String name = taskOrderModel.getGoodsName() + "	  " + planAmount + unit;
    	   Util.setText(mContext, name, planAmount + unit, viewHolder.orderTypeName,
    			  R.color.theme_color);
//        	viewHolder.orderTypeName.setText(taskOrderModel.getGoodsName()+"	"+taskOrderModel.getPlanAmount() +taskOrderModel.getAssignUnitText());
//        	viewHolder.buttom_type_name.setVisibility(View.GONE);
        	viewHolder.task_order_state.setText("");
			viewHolder.goods_name.setVisibility(View.GONE);
			viewHolder.mRightIcon.setVisibility(View.GONE);
			viewHolder.buttom_type_name.setText("请按提示运量运输");
			viewHolder.buttom_type_name.setTextColor(mContext.getResources().getColor(R.color.theme_color));
       }else{
			viewHolder.buttom_type_name.setText("运输车辆	"+taskOrderModel.getTruckNum());
			viewHolder.orderTypeName.setText("运单	" + taskOrderModel.getTransitTaskId());
			viewHolder.task_order_state.setText(taskOrderModel.getStatusText());
			viewHolder.task_order_state.setTextColor(mContext.getResources().getColor(R.color.theme_color));
			viewHolder.goods_name.setVisibility(View.VISIBLE);
		    String transitAmount = Util.formatDoubleToString(taskOrderModel.getTransitAmount(), taskOrderModel.getUnitText());
			viewHolder.goods_name.setText(mGoodsName + "  " + transitAmount + taskOrderModel.getUnitText());
			viewHolder.buttom_type_name.setVisibility(View.VISIBLE);
			viewHolder.mRightIcon.setVisibility(View.VISIBLE);
		}
		viewHolder.send_time.setText("发货时间  "+Util.getFormatedDateTime(taskOrderModel.getGmtStart()));
		viewHolder.receive_time.setText("到货时间  "+Util.getFormatedDateTime(taskOrderModel.getGmtEnd()));
		return convertView;
	}
	
	public void setIsEnd(boolean value){
		isEnd = value;
	}
	
	
	//已完成的托运单底部显示托运单号、托运单创建时间、托运单结束时间
	private String TYDNumber = "", creatTime = "", finishTime = "", payWay = "";
	public void setConsignMessage(String TYDNumber, String creatTime, String finishTime, String payWay){
		if(TYDNumber != null){
			this.TYDNumber = TYDNumber;
		}
		if(creatTime != null){
			this.creatTime = creatTime;
		}
		if(finishTime != null){
			this.finishTime = finishTime;
		}
		if(payWay != null){
			this.payWay = payWay;
		}
	}
	static class ViewHolder {
		TextView orderTypeName,buttom_type_name,task_order_state,reject_view;
		TextView mAllCountText,mAllCount,mFinishedText,mFinished,mRestAmountText,mRestAmount,mAuctionPrice,mStatus,
		         fact_unit_text,fact_amount_text,send_time,receive_time,goods_name;
		RelativeLayout mLookTruckLayout;
		GoodsDetailsView mGoodsDetailsView;
		RelativeLayout fact_layout;
		LinearLayout pcd_layout;
		ImageView mRightIcon;
	}
}
