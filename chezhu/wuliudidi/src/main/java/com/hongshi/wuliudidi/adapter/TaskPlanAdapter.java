package com.hongshi.wuliudidi.adapter;

import java.util.ArrayList;
import java.util.List;

import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.AssignTruckActivity;
import com.hongshi.wuliudidi.activity.RejectActivity;
import com.hongshi.wuliudidi.dialog.DataFillingDialog;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.TaskPlanModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.utils.Util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

public class TaskPlanAdapter extends BaseAdapter {
	private Context mContext;
	private DataFillingDialog mImageDialog;
	private List<TaskPlanModel> mTaskPlanList = new ArrayList<TaskPlanModel>();
	private Handler mHandler;
	public TaskPlanAdapter(Context context, List<TaskPlanModel> mList,Handler mHandler){
		mContext = context;
		mTaskPlanList = mList;
		this.mHandler = mHandler;
	}
	
	@Override
	public int getCount() {
		return mTaskPlanList.size();
	}

	@Override
	public Object getItem(int position) {
		if(position < mTaskPlanList.size() && position >= 0){
			return mTaskPlanList.get(position);
		}else{
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void addList(List<TaskPlanModel> mAdd){
		mTaskPlanList.addAll(mAdd);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.task_plan_item, null);
			viewHolder.transitAmount = (TextView) convertView.findViewById(R.id.transit_amount_text);
			viewHolder.startCityText = (TextView) convertView.findViewById(R.id.start_city_text);
			viewHolder.endCityText = (TextView) convertView.findViewById(R.id.end_city_text);
			viewHolder.startCountyText = (TextView) convertView.findViewById(R.id.start_county_text);
			viewHolder.endCountyText = (TextView) convertView.findViewById(R.id.end_county_text);
			viewHolder.goodsNameText = (TextView) convertView.findViewById(R.id.goods_name_text);
			viewHolder.goodsWeightText = (TextView) convertView.findViewById(R.id.goods_weight_text);
			viewHolder.transitTimeText = (TextView) convertView.findViewById(R.id.transit_time);
			viewHolder.rec_call_text = (TextView) convertView.findViewById(R.id.rec_call_text);
			viewHolder.flagImage = (ImageView) convertView.findViewById(R.id.flag_image);
			viewHolder.plan_status_text = (TextView) convertView.findViewById(R.id.plan_status_text);
			viewHolder.contactSenderLayout = (RelativeLayout) convertView.findViewById(R.id.contact_sender_layout);
			viewHolder.contactSenderImage = (ImageView) convertView.findViewById(R.id.contact_sender_image);
			viewHolder.contactSenderText = (TextView) convertView.findViewById(R.id.contact_sender_text);
			viewHolder.contactReceiverLayout = (RelativeLayout) convertView.findViewById(R.id.contact_receiver_layout);
			viewHolder.plan_icon_layout = (LinearLayout) convertView.findViewById(R.id.plan_icon_layout);
			//派车单上传回单layout
			viewHolder.pcd_icon_layout = (RelativeLayout) convertView.findViewById(R.id.pcd_icon_layout);
			viewHolder.upload_receipt_image = (ImageView) convertView.findViewById(R.id.upload_receipt_image);
			viewHolder.re_upload_receipt_image = (ImageView) convertView.findViewById(R.id.re_upload_receipt_image);
			viewHolder.submit_goods_ca = (ImageView) convertView.findViewById(R.id.submit_goods_ca);
			viewHolder.pcd_layout = (RelativeLayout) convertView.findViewById(R.id.pcd_layout);
			viewHolder.plan_layout = (RelativeLayout) convertView.findViewById(R.id.plan_layout);
			viewHolder.creat_time = (TextView) convertView.findViewById(R.id.creat_time);
			viewHolder.truck_number = (TextView) convertView.findViewById(R.id.truck_number);
			viewHolder.pcd_state = (TextView) convertView.findViewById(R.id.pcd_state);
			viewHolder.assingTruckButton = (Button) convertView.findViewById(R.id.assign_truck_button);
			viewHolder.contact_receiver_content_layout = (LinearLayout) convertView.findViewById(R.id.contact_receiver_content_layout);
			viewHolder.show_comment_order_num = (TextView)convertView.findViewById(R.id.show_comment_order_num);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final TaskPlanModel model = mTaskPlanList.get(position);

		if(model.getOutBizType() == 1){
			//任务的类型 1=派车计划
			viewHolder.pcd_layout.setVisibility(View.GONE);
			viewHolder.plan_layout.setVisibility(View.VISIBLE);
		}else if(model.getOutBizType() == 2){
			//任务的类型2=派车单
			viewHolder.pcd_layout.setVisibility(View.VISIBLE);
			viewHolder.plan_layout.setVisibility(View.GONE);
		}
		if(model.getSalerBizID() == null){
			//非水泥订单
			//我要出发按钮展示
			viewHolder.submit_goods_ca.setVisibility(View.VISIBLE);
			if((model.getOutBizType() == 2 && model.getStatus() ==2)
					|| (model.getStatusTxt() != null && model.getStatusTxt().equals("等待复核"))){
				//是派车单，且在运输中或者等待复核
				viewHolder.pcd_icon_layout.setVisibility(View.VISIBLE);
				viewHolder.plan_icon_layout.setVisibility(View.GONE);
				if(model.isGoodsSourceTag()){
					viewHolder.upload_receipt_image.setImageResource(R.drawable.transi_arrive_style);
					viewHolder.upload_receipt_image.setClickable(false);
				}else{
					viewHolder.upload_receipt_image.setImageResource(R.drawable.upload_receipt);
				}
			}else{
				viewHolder.pcd_icon_layout.setVisibility(View.GONE);
				viewHolder.plan_icon_layout.setVisibility(View.GONE);
			}
		}else {
			//水泥订单
			//我要出发按钮隐藏
			viewHolder.submit_goods_ca.setVisibility(View.GONE);
			if(model.isOutLib()){
				viewHolder.pcd_icon_layout.setVisibility(View.VISIBLE);
//				viewHolder.upload_receipt_image.setImageResource(R.drawable.upload_receipt);
//				viewHolder.upload_receipt_image.setClickable(true);
			}else {//未出库的单子上传回单功能无效
				viewHolder.pcd_icon_layout.setVisibility(View.GONE);
//				viewHolder.upload_receipt_image.setImageResource(R.drawable.upload_receipt);
//				viewHolder.upload_receipt_image.setClickable(false);
			}
		}

//		if(model.getStatusTxt() != null && model.getStatusTxt().equals("等待复核")){//added by huiyuan for image upload again
//			viewHolder.re_upload_receipt_image.setVisibility(View.VISIBLE);
//			viewHolder.re_upload_receipt_image.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					uproadImage(model);
//				}
//			});
//		}else {
//			viewHolder.re_upload_receipt_image.setVisibility(View.GONE);
//		}
		
		double restAmount = model.getPlanAmount() - model.getRealAmount();
		String unit = model.getAssignUnitText();
		String restAmountStr = Util.formatDoubleToString(restAmount, unit);
		String realAmountStr = Util.formatDoubleToString(model.getRealAmount(), unit);

		if(model.getSalerBizID() != null && !"".equals(model.getSalerBizID())){
			viewHolder.show_comment_order_num.setText("关联订单号 " + model.getSalerBizID());
			viewHolder.show_comment_order_num.setVisibility(View.VISIBLE);
		}else {
			viewHolder.show_comment_order_num.setVisibility(View.GONE);
		}
		viewHolder.transitAmount.setText(restAmountStr + unit + "剩余，" + realAmountStr + unit + "已运");
		viewHolder.startCityText.setText(model.getSenderCity());
		viewHolder.endCityText.setText(model.getRecipientCity());
		viewHolder.startCountyText.setText(model.getSenderDistrict());
		viewHolder.endCountyText.setText(model.getRecipientDistrict());
		if(model.getGmtEnd() != null){
			//派车单创建时间
			viewHolder.creat_time.setText(Util.formatDate(model.getGmtStart()));
		}
		if(model.getTruckNO() != null){
			viewHolder.truck_number.setText(model.getTruckNO());
		}
		//派车单状态text
		viewHolder.pcd_state.setText(model.getStatusTxt());
		if(model.getOutBizType() == 2 && model.getStatus() == 6){
			//派车单，货主驳回时改变状态text的颜色
			viewHolder.pcd_state.setTextColor(mContext.getResources().getColor(R.color.theme_color));
		}else{
			viewHolder.pcd_state.setTextColor(mContext.getResources().getColor(R.color.blue_task));
		}
		viewHolder.goodsNameText.setText(model.getGoodsName());
		viewHolder.goodsWeightText.setText(Util.formatDoubleToString(model.getPlanAmount(), unit) + unit);
		viewHolder.transitTimeText.setText("运输时间  " + Util.formatDatePoint(model.getGmtStart()) + " — " +
				Util.formatDatePoint(model.getGmtEnd()));
		if(model.getOutBizType() == 1){
			//是派车计划
			if(model.isGoodsSourceTag()){
				//外部货源有 未派车、派车完成、超时、部分派车
				viewHolder.plan_status_text.setVisibility(View.VISIBLE);
				viewHolder.flagImage.setVisibility(View.GONE);
				if(model.getStatusTxt() != null){
					viewHolder.plan_status_text.setText(model.getStatusTxt());
				}
			}else{
				//内部货源只有运输中与运输完成
				viewHolder.plan_status_text.setVisibility(View.GONE);
				viewHolder.flagImage.setVisibility(View.VISIBLE);
				if(model.getStatus() == 1 || model.getStatus() == 4){
					//未派车或为部分派车
					viewHolder.flagImage.setImageResource(R.drawable.transit_unfinish);
				}else{
					viewHolder.flagImage.setImageResource(R.drawable.transit_finish);
				}
			}
		}
		
		//plan_icon_layout的左边只在派车单被驳回时显示“驳回原因”，其余时候显示“联系发货人”
		if(model.getOutBizType() == 2 && model.getStatus() == 6){
			//是派车单且已被驳回
			viewHolder.contactSenderImage.setImageResource(R.drawable.reject_reason);
			viewHolder.contactSenderText.setText("驳回原因");
		}else{
			viewHolder.contactSenderImage.setImageResource(R.drawable.contact_sender);
			viewHolder.contactSenderText.setText(mContext.getResources().getString(R.string.contact_sender));
		}
		
		//plan_icon_layout的右边，在派车计划还需派车的时候显示“立即派车”，不需派车的时候显示“联系收货人”； 在派车单异常的时候显示“客服投诉”，
		//在派车单运输中时，plan_icon_layout不显示，在派车单其它状态下显示“联系收货人”
		if(model.getOutBizType() == 1){
			//是派车计划
			//派车计划可以派车，且状态为未派车或部分派车
			if(model.isGoodsSourceTag()){
				if(model.getStatus() == 1 || model.getStatus() == 4){
					viewHolder.contact_receiver_content_layout.setVisibility(View.GONE);
					viewHolder.assingTruckButton.setVisibility(View.VISIBLE);
					viewHolder.assingTruckButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext, AssignTruckActivity.class);
							intent.putExtra("planId", model.getOutBizId());
							intent.putExtra("planAmount", String.valueOf(model.getPlanAmount()));
							intent.putExtra("assignUnit", model.getAssignUnit());
							intent.putExtra("assignUnitText", model.getAssignUnitText());
							mContext.startActivity(intent);
						}
					});
				}else{
					//是平台货源的派车计划但不处在“未派车”和“部分派车”状态
					viewHolder.contact_receiver_content_layout.setVisibility(View.VISIBLE);
					viewHolder.assingTruckButton.setVisibility(View.GONE);
				}
			}else{
				//派车计划不是平台货源不能派车
				viewHolder.contact_receiver_content_layout.setVisibility(View.VISIBLE);
				viewHolder.assingTruckButton.setVisibility(View.GONE);
			}
		}else if(model.getOutBizType() == 2){
			viewHolder.contact_receiver_content_layout.setVisibility(View.VISIBLE);
			viewHolder.assingTruckButton.setVisibility(View.GONE);
			//等待复核、货主驳回、复核超时
			if(model.getStatus() == 4 || model.getStatus() == 6 || model.getStatus() == 8){
				viewHolder.rec_call_text.setText("客服投诉");
			}else{
				viewHolder.rec_call_text.setText(mContext.getResources().getString(R.string.contact_receiver));
			}
		}

//		if(model.getStatus() == 2){
//			viewHolder.pcd_icon_layout.findViewById(R.id.submit_goods_ca).setVisibility(View.VISIBLE);
		if(!model.isHasDelivery()){
			viewHolder.submit_goods_ca.setImageResource(R.drawable.submit_goods_ca);
			viewHolder.submit_goods_ca.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					mContext.getSharedPreferences("config",Context.MODE_PRIVATE)
//							.edit().putString("submit_img_type","submit_goods_ca").commit();
//					uproadImage(model);
					uploadStartedStatus(model.getOutBizId());
				}
			});
		}else {
			viewHolder.submit_goods_ca.setImageResource(R.drawable.already_started);
			viewHolder.submit_goods_ca.setClickable(false);
		}

		viewHolder.pcd_icon_layout.findViewById(R.id.upload_receipt_image).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mContext.getSharedPreferences("config",Context.MODE_PRIVATE)
						.edit().putString("submit_img_type","upload_receipt_image").commit();
				uproadImage(model);
			}
		});
		viewHolder.contactSenderLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//派车单、被驳回状态下，执行“查看驳回原因”，其余时候执行联系发货人操作
				if(model.getOutBizType() == 2 && model.getStatus() == 6){
					Intent intent = new Intent(mContext, RejectActivity.class);
					intent.putExtra("taskId", model.getOutBizId());
					mContext.startActivity(intent);
				}else{
					if(model.getSenderPhone() != null){
						Util.call(mContext, model.getSenderPhone());
					}
				}
			}
		});
		viewHolder.contactReceiverLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//派车计划，有未派完的车，不需执行联系收货人操作，而是显示“立即派车”
				if(model.getOutBizType() == 1 && (model.getStatus() == 1 || model.getStatus() == 4)){
					return;
				}
				//派车单，在4.等待复核  6.货主驳回  8.复核超时状态下，可以联系客服
				if(model.getOutBizType() == 2 && (model.getStatus() == 4 || model.getStatus() == 6 || model.getStatus() == 8)){
					Util.call(mContext, mContext.getResources().getString(R.string.contact_number));
					return;
				}
				//其余情况下联系收货人
				if(model.getRecipientPhone() != null){
					Util.call(mContext, model.getRecipientPhone());
				}
			}
		});
		return convertView;
	}
	
	private static class ViewHolder {
		TextView transitAmount, startCityText, endCityText, startCountyText,
			endCountyText, goodsNameText, goodsWeightText, transitTimeText,creat_time,truck_number,pcd_state,
			rec_call_text, plan_status_text, contactSenderText,show_comment_order_num;
		ImageView flagImage, contactSenderImage, upload_receipt_image,re_upload_receipt_image,submit_goods_ca;
		LinearLayout plan_icon_layout, contact_receiver_content_layout;
		RelativeLayout contactSenderLayout, contactReceiverLayout,pcd_layout,plan_layout,pcd_icon_layout;
		Button assingTruckButton;
	}

	private void uproadImage(TaskPlanModel model){
		mContext.getSharedPreferences("config",Context.MODE_PRIVATE).edit().putString("planAmount","" + model.getPlanAmount()).commit();
		if(!model.isGoodsSourceTag()){
			// 0 表示拍照或者图库选取
			mImageDialog = new DataFillingDialog(mContext, R.style.data_filling_dialog, mHandler, 0,model.getPlanAmount());
			mImageDialog.setCanceledOnTouchOutside(true);
			UploadUtil.setAnimation(mImageDialog, CommonRes.TYPE_BOTTOM, false);
			mImageDialog.setText("拍照", "图库选取");
			mImageDialog.show();
		}else{
			mImageDialog = new DataFillingDialog(mContext, R.style.data_filling_dialog, mHandler,
					CommonRes.ARRIVETAG, model.getOutBizId(), model.getTruckNO());
			mImageDialog.setCanceledOnTouchOutside(true);
			UploadUtil.setAnimation(mImageDialog, CommonRes.TYPE_BOTTOM, false);
			mImageDialog.setText("扫码上传", "拍照上传", "图库选取");
			mImageDialog.show();
		}
		Message msg = new Message();
		Bundle b = new Bundle();
		if(model.getOutBizId() != null){
			b.putString("taskId", model.getOutBizId());
		}
		if(model.getSettleUnitText() != null){
			b.putString("unitText", model.getSettleUnitText());
		}
		msg.setData(b);
		msg.what = CommonRes.TYPE_CALLBACK_PCD_ID;
		mHandler.sendMessage(msg);
	}

	private final String upload_take_goods_ca_url = GloableParams.HOST + "carrier/transit/task/deliveryProofByTaskId.do";
	private void uploadStartedStatus(String taskId){

				AjaxParams params = new AjaxParams();
				if(taskId != null && !"".equals(taskId)){
					params.put("taskId",taskId);
				}else{
					Toast.makeText(mContext,"taskId参数异常",Toast.LENGTH_LONG).show();
					return;
				}
				DidiApp.getHttpManager().sessionPost(mContext, upload_take_goods_ca_url, params,new ChildAfinalHttpCallBack(){
					@Override
					public void data(String t) {
						try {
							JSONObject jsonObject = new JSONObject(t);
							String integral = jsonObject.optString("body");
							if (integral != null && !"".equals(integral) && Integer.parseInt(integral) > 0){
								Toast.makeText(mContext,"获得" + integral + "个积分",Toast.LENGTH_LONG).show();
								Intent intent = new Intent();
								intent.setAction("earned_integral");
								mContext.sendBroadcast(intent);
							}else {
								Toast.makeText(mContext.getApplicationContext(), "已出发!", Toast.LENGTH_LONG).show();
							}
						}catch (Exception e){

						}

						Intent intent = new Intent();
						intent.setAction(CommonRes.MessageFromTransit);
						mContext.sendBroadcast(intent);
					}

					@Override
					public void onFailure(String errCode, String errMsg, Boolean errSerious) {
					}
				});
			}


}
