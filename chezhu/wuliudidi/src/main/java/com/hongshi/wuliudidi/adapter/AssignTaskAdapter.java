package com.hongshi.wuliudidi.adapter;

import java.util.List;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.AssignTruckActivity;
import com.hongshi.wuliudidi.activity.SeeTruckActivity;
import com.hongshi.wuliudidi.model.TransitPlanModel;
import com.hongshi.wuliudidi.utils.Util;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AssignTaskAdapter extends BaseAdapter {

	private Context mContext;
	private List<TransitPlanModel> mOrderList;

	public AssignTaskAdapter(Context context, List<TransitPlanModel> mOrderList) {
		this.mOrderList = mOrderList;
		this.mContext = context;
	}
	public void addList(List<TransitPlanModel> mOrderList){
		this.mOrderList.addAll(mOrderList);
	}
	@Override
	public int getCount() {
		return mOrderList.size();
	}

	@Override
	public TransitPlanModel getItem(int position) {
		return mOrderList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.task_assign_item, null);
			viewHolder = new ViewHolder();
			viewHolder.startCity = (TextView) convertView.findViewById(R.id.start_name);
			viewHolder.endCity = (TextView) convertView.findViewById(R.id.end_name);
			//中标单号
			viewHolder.order_type_name = (TextView) convertView.findViewById(R.id.order_type_name);
			//创建时间
			viewHolder.creat_time = (TextView) convertView.findViewById(R.id.creat_time);
			viewHolder.goods_name = (TextView) convertView.findViewById(R.id.goods_name);
			viewHolder.start_time = (TextView) convertView.findViewById(R.id.start_time);
			viewHolder.end_time = (TextView) convertView.findViewById(R.id.end_time);
			viewHolder.click_view = (TextView) convertView.findViewById(R.id.click_view);
			viewHolder.buttom_type_name = (TextView) convertView.findViewById(R.id.buttom_type_name);
			viewHolder.task_order_state = (TextView) convertView.findViewById(R.id.task_order_state);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		TransitPlanModel ordermodel = mOrderList.get(position);
		viewHolder.task_order_state.setVisibility(View.GONE);
		final String mJumpBidItemId = ordermodel.getBidItemId();
		final String mJumpPlanId = ordermodel.getPlanId();
		final String mPlanAmount = ordermodel.getPlanAmount();
		final String assignUnit = ordermodel.getAssignUnit();
		final String assignUnitText = ordermodel.getAssignUnitText();
		
		if(ordermodel.getStatus() == 1 || ordermodel.getStatus() == 4){
			//未派车
			viewHolder.click_view.setText("立即派车");
			viewHolder.click_view.setTextColor(mContext.getResources().getColor(R.color.white));
			viewHolder.click_view.setBackgroundResource(R.drawable.theme_round_corner_rectangle_style);
			viewHolder.click_view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//立即派车
					Intent intent = new Intent(mContext, AssignTruckActivity.class);
					intent.putExtra("bidItemId", mJumpBidItemId);
					intent.putExtra("planId", mJumpPlanId);
					intent.putExtra("planAmount", mPlanAmount);
					intent.putExtra("assignUnit", assignUnit);
					intent.putExtra("assignUnitText", assignUnitText);
					mContext.startActivity(intent);
				}
			});
			viewHolder.buttom_type_name.setText("");
		}else if(ordermodel.getStatus() == 2){
			//已派车 
			viewHolder.click_view.setText("已派车");
			viewHolder.buttom_type_name.setText("查看详情");
			viewHolder.click_view.setBackgroundResource(R.color.white);
			viewHolder.buttom_type_name.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//跳转到派车详情页面
					Intent intent = new Intent(mContext,SeeTruckActivity.class);
					intent.putExtra("planId", mJumpPlanId);
					mContext.startActivity(intent);
				}
			});
			
		}else if(ordermodel.getStatus() == 3){
			//派车超时
			viewHolder.click_view.setText("派车超时");
			viewHolder.click_view.setBackgroundResource(R.color.white);
			viewHolder.buttom_type_name.setText("");
		}
		
		viewHolder.order_type_name.setText(mContext.getString(R.string.consignment_bill_number) + ": " + ordermodel.getBidItemId());
		viewHolder.startCity.setText(ordermodel.getSendAddr());
		viewHolder.endCity.setText(ordermodel.getRecvAddr());
		viewHolder.creat_time.setText(Util.formatDateSecond(ordermodel.getPlanGmtCreate()));
		viewHolder.start_time.setText("发货时间" + ": "
				+ Util.formatDateSecond(ordermodel.getGmtStart()));
		viewHolder.end_time.setText(mContext.getString(R.string.send_time) + ": "
				+ Util.formatDateSecond(ordermodel.getGmtEnd()));
		viewHolder.goods_name.setText(ordermodel.getGoodsName() + ": "
				+ ordermodel.getPlanAmount()+ordermodel.getAssignUnitText());
		return convertView;
	}

	static class ViewHolder {
		TextView startCity,endCity,order_type_name,creat_time,goods_name,start_time,end_time
		   ,click_view,buttom_type_name,task_order_state;

	}
}
