package com.hongshi.wuliudidi.activity;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.camera.ActivityCapture;
import com.hongshi.wuliudidi.camera.PhotoPreviewActivity;
import com.hongshi.wuliudidi.dialog.DataFillingDialog;
import com.hongshi.wuliudidi.dialog.UploadReceiptDialog;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.TaskStatusTrackVO;
import com.hongshi.wuliudidi.model.TransitTaskDetailModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.photo.GetPhotoUtil;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.GoodsBubbleMsg;
import com.hongshi.wuliudidi.utils.ImageUtil;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.AuctionItem;
import com.hongshi.wuliudidi.view.DetailsAddressView;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.hongshi.wuliudidi.view.MyItemView;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author huiyuan
 */
public class TruckingOrderDetailsActivity extends Activity implements OnClickListener{

	//获取派车单详情
	private String detail_url = GloableParams.HOST + "carrier/transit/task/detail.do?";
//	private String start_transit_url = GloableParams.HOST + "carrier/transit/task/startTransit.do?";//开始运输接口
	private DiDiTitleView mTitle;
	private AuctionItem mTransportStateItem,mGoodsNameItem,mGoodsWeightItem,mGoodSendTimeItem,mGoodArriveTimeItem,
	mTruckInfoItem,mTransportMilItem,mShipperPhoneItem,mConsigneePhoneItem,mGoodsModelType;
	private Button mGoodsTransitBtn, mTransitStatusBtn;
	private String mTransitId = "";
	//详情页model
	private TransitTaskDetailModel mTransitTaskDetailModel;
	private TextView mRemarkContent, transitTaskId, mFinishTime, mSendTime,
			mCreatTime, uploadTransitAmount,huidan_info_text,upload_sheet_info_text;
	private DetailsAddressView mDetailsAddressView;
	private LinearLayout mFinishLayout,send_layout;
	private String taskId;
//	private String license;
	private ImageView contact_imag,reject_imag, uploadReceiptImage,upload_transit_imageview1;
	private MyItemView mSignItem;
	private DataFillingDialog mImageDialog;
	private RelativeLayout receiptPhotoLayout, goodsRemarkLayout,receipt_photo_layout1;
	private final int TRANSIT_STATUS = 101;
	private boolean isShowBtn = true;

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("TruckingOrderDetailsActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("TruckingOrderDetailsActivity");
	}

	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(CommonRes.RefreshWinBid)) {
				//刷新
				getData(mTransitId);
			}else if (action.equals(CommonRes.UploadReceiptFinished)) {
				//刷新
				getData(mTransitId);
			}else if (action.equals(CommonRes.ACTIONPHOTOPATH)) {
				String imagePath = intent.getStringExtra("imagePath");
				int tag;
				try {
					tag = intent.getExtras().getInt("tag", -1);
				} catch (Exception e) {
					tag = -1;
				}
				if(tag == CommonRes.TYPE_HUIDAN){
					//调起是否上传回单对话框
					UploadReceiptDialog uploadDialog = new UploadReceiptDialog(TruckingOrderDetailsActivity.this,
							R.style.data_filling_dialog, mHandler, taskId, imagePath,
							mTransitTaskDetailModel.getSettleUnitText());
					UploadUtil.setAnimation(uploadDialog, CommonRes.TYPE_BOTTOM, true);
					uploadDialog.setCanceledOnTouchOutside(false);
					uploadDialog.show();
				}
			}
		}
	};
	
	@SuppressLint("HandlerLeak") private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CommonRes.CAMERA:
				//拍照
				GetPhotoUtil.callCamera(TruckingOrderDetailsActivity.this);
				break;
			case CommonRes.GALLERY:
				//图册选取
				GetPhotoUtil.callGallery(TruckingOrderDetailsActivity.this);
				break;
			case CommonRes.RENEW_UPLOAD_PHOTO:
				//重新选图
				mImageDialog.show();
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trucking_order_details);
		ActivityManager.getInstance().addActivity(this);

		isShowBtn = getIntent().getBooleanExtra("isShowBtn",true);
		mTransitId = getIntent().getStringExtra("transitId");
		init();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CommonRes.RefreshWinBid);
		intentFilter.addAction(CommonRes.UploadReceiptFinished);
		intentFilter.addAction(CommonRes.ACTIONPHOTOPATH);
		registerReceiver(mRefreshBroadcastReceiver, intentFilter);
		
		mGoodsTransitBtn.setOnClickListener(this);
		getData(mTransitId);
	}

	private void getData(String mTransitId) {
		AjaxParams params = new AjaxParams();
		params.put("taskId", mTransitId);
		final PromptManager mPromptManager = new PromptManager();
		mPromptManager.showProgressDialog1(TruckingOrderDetailsActivity.this, "加载中");
		DidiApp.getHttpManager().sessionPost(TruckingOrderDetailsActivity.this, detail_url, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				mPromptManager.closeProgressDialog();
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(t);
					String all = jsonObject.getString("body");	
					mTransitTaskDetailModel = JSON.parseObject(all,TransitTaskDetailModel.class);
					taskId = mTransitTaskDetailModel.getTransitTaskId();
//					license = mTransitTaskDetailModel.getTruckNumber();
					setViewData(mTransitTaskDetailModel);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				mPromptManager.closeProgressDialog();
			}

		});
	}
	/**
	 * 获取到数据以后为控件设置
	 * @param mTransitTaskDetailModel
	 */
	private void setViewData(final TransitTaskDetailModel mTransitTaskDetailModel) {
		//运输状态
		mTransportStateItem.setContent(mTransitTaskDetailModel.getStatusText());
		//货物名称
		mGoodsNameItem.setContent(mTransitTaskDetailModel.getGoodsName());
		//货物重量
		if(mTransitTaskDetailModel.getUnit().equals("T")){
			mGoodsWeightItem.setName("货物重量");
		}else if(mTransitTaskDetailModel.getUnit().equals("M3")){
			mGoodsWeightItem.setName("货物体积");
		}else if(mTransitTaskDetailModel.getUnit().equals("PIECE") || mTransitTaskDetailModel.getUnit().equals("TRUCK")){
			mGoodsWeightItem.setName("货物数量");
		}
		String goodsAmount  = Util.formatDoubleToString(mTransitTaskDetailModel.getGoodsAmount(), mTransitTaskDetailModel.getUnitText());
		mGoodsWeightItem.setContent(goodsAmount +mTransitTaskDetailModel.getUnitText());
		//发货时间
		mGoodSendTimeItem.setContent(Util.formatDateSecond(mTransitTaskDetailModel.getGmtStart()));
		//送达时间
		mGoodArriveTimeItem.setContent(Util.formatDateSecond(mTransitTaskDetailModel.getGmtEnd()));
		//运输车辆
		mTruckInfoItem.setContent(mTransitTaskDetailModel.getTruckNumber());
		//型号
		String modelType = mTransitTaskDetailModel.getModelNumber();
		if(modelType == null || modelType.length() <= 0){
			mGoodsModelType.setVisibility(View.GONE);
		}else {
			mGoodsModelType.setVisibility(View.VISIBLE);
			mGoodsModelType.setContent(modelType);
		}
		//运输里程
		mTransportMilItem.setContent(""+Math.round(mTransitTaskDetailModel.getDistance()/1000)+"公里");
		//发货人信息
		mShipperPhoneItem.setContent(mTransitTaskDetailModel.getSendName() + "  "+mTransitTaskDetailModel.getSendTel());
		mShipperPhoneItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Util.call(TruckingOrderDetailsActivity.this, mTransitTaskDetailModel.getSendTel());
			}
		});
		//收货人信息
		mConsigneePhoneItem.setContent(mTransitTaskDetailModel.getRecvName() + "  "+mTransitTaskDetailModel.getRecvTel());
		mConsigneePhoneItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Util.call(TruckingOrderDetailsActivity.this, mTransitTaskDetailModel.getRecvTel());
			}
		});
		//备注
		String remark = mTransitTaskDetailModel.getRemark();
		if(remark == null || remark.length() <= 0){
			goodsRemarkLayout.setVisibility(View.GONE);
		}else{
			mRemarkContent.setText(remark);
			goodsRemarkLayout.setVisibility(View.VISIBLE);
		}

		mDetailsAddressView.setNumber(mTransitTaskDetailModel.getBidItemId());
		mDetailsAddressView.setStartCity(mTransitTaskDetailModel.getSendAddrWhole());
		mDetailsAddressView.setEndCity(mTransitTaskDetailModel.getRecvAddrWhole());
		mDetailsAddressView.setOnClickListener(this);
		//派车单号
		transitTaskId.setText(mTransitTaskDetailModel.getTransitTaskId());

		if(isShowBtn){
			//判断是否完成签单：1未开始，2运输中，3已签收 4，等待审核 5.复核通过 6.货主驳回 7.调解通过 8.复核超时 9.nc对接通过 99.强制作废
			if(mTransitTaskDetailModel.getStatus() == 1){
				//未开始
//			mGoodsTransitBtn.setText(R.string.start_transit);
//			mGoodsTransitBtn.setVisibility(View.VISIBLE);
				receiptPhotoLayout.setVisibility(View.GONE);
			}else if(mTransitTaskDetailModel.getStatus() == 2){
				//运输中
				mGoodsTransitBtn.setText(R.string.upload_receipt);
				mGoodsTransitBtn.setVisibility(View.VISIBLE);
				receiptPhotoLayout.setVisibility(View.GONE);
				receipt_photo_layout1.setVisibility(View.VISIBLE);
				//提货凭证
				String imagePath1 = mTransitTaskDetailModel.getDeliveryProof();
				FinalBitmap bitmap1 = FinalBitmap.create(TruckingOrderDetailsActivity.this);
				bitmap1.display(upload_transit_imageview1, imagePath1);
				upload_sheet_info_text.setText("提货凭证 " + mTransitTaskDetailModel.getDeliveryProofName()
						+ " " + (Util.formatDateSecond(mTransitTaskDetailModel.getGmtDelivery())));
			}else if(mTransitTaskDetailModel.getStatus()==3 || mTransitTaskDetailModel.getStatus() == 7){
				//3已签收, 7.调解通过
				mFinishLayout.setVisibility(View.VISIBLE);
				mFinishTime.setText(""+mTransitTaskDetailModel.getGmtSignup());
				mSignItem.setVisibility(View.VISIBLE);
				mSignItem.setOnClickListener(this);
				mGoodsTransitBtn.setText(mTransitTaskDetailModel.getStatusText());
				mGoodsTransitBtn.setBackgroundColor(getResources().getColor(R.color.light_grey));
				mGoodsTransitBtn.setVisibility(View.GONE);
				receiptPhotoLayout.setVisibility(View.GONE);
			}else if(mTransitTaskDetailModel.getStatus() == 4 || mTransitTaskDetailModel.getStatus() == 5){
				// 4，等待审核 5.复核通过
				//显示上次上传的到货回单
				receiptPhotoLayout.setVisibility(View.VISIBLE);
				receipt_photo_layout1.setVisibility(View.VISIBLE);
				String settleUnit = mTransitTaskDetailModel.getSettleUnitText();
				String realAmount = Util.formatDoubleToString(mTransitTaskDetailModel.getTaskVoucherReviewVO().getRealAmount(), settleUnit);
				uploadTransitAmount.setText(realAmount + settleUnit);
				String imagePath = mTransitTaskDetailModel.getTaskVoucherReviewVO().getSignupVoucherPic();
				FinalBitmap bitmap = FinalBitmap.create(TruckingOrderDetailsActivity.this);
				bitmap.display(uploadReceiptImage, imagePath);

				//提货凭证
				String imagePath1 = mTransitTaskDetailModel.getDeliveryProof();
				FinalBitmap bitmap1 = FinalBitmap.create(TruckingOrderDetailsActivity.this);
				bitmap1.display(upload_transit_imageview1, imagePath1);
				upload_sheet_info_text.setText("提货凭证 " + mTransitTaskDetailModel.getDeliveryProofName()
						+ " " + (Util.formatDateSecond(mTransitTaskDetailModel.getGmtDelivery())));

				if(mTransitTaskDetailModel.getStatus() == 4){
					mGoodsTransitBtn.setText("客服投诉");
					mGoodsTransitBtn.setVisibility(View.VISIBLE);
				}
			}else if(mTransitTaskDetailModel.getStatus()==6){
				//6.货主驳回
				mGoodsTransitBtn.setVisibility(View.GONE);
				contact_imag.setVisibility(View.VISIBLE);
				reject_imag.setVisibility(View.VISIBLE);
				contact_imag.setOnClickListener(this);
				reject_imag.setOnClickListener(this);
				receiptPhotoLayout.setVisibility(View.GONE);
			}else{
				contact_imag.setVisibility(View.GONE);
				reject_imag.setVisibility(View.GONE);
				mGoodsTransitBtn.setVisibility(View.GONE);
				receiptPhotoLayout.setVisibility(View.GONE);
			}
		}else {
			contact_imag.setVisibility(View.GONE);
			reject_imag.setVisibility(View.GONE);
			mGoodsTransitBtn.setVisibility(View.GONE);
			receiptPhotoLayout.setVisibility(View.GONE);
		}

		mCreatTime.setText(Util.formatDateSecond(mTransitTaskDetailModel.getGmtCreate()));
		if(mTransitTaskDetailModel.getGmtStartTransit() != null){
			mSendTime.setText(Util.formatDateSecond(mTransitTaskDetailModel.getGmtStartTransit()));
		}else{
			mSendTime.setText("您还没开始运输");
			send_layout.setVisibility(View.GONE);
		}
		if(mTransitTaskDetailModel.getGmtSignup() != null){
			mFinishLayout.setVisibility(View.GONE);
			mFinishTime.setText(Util.formatDateSecond(mTransitTaskDetailModel.getGmtSignup()));
		}else{
//			mFinishTime.setText("货物还没签收");
			mFinishLayout.setVisibility(View.GONE);
		}
	}


	private void init() {
		mTitle = (DiDiTitleView) findViewById(R.id.trucking_order_title);
		mTitle.setBack(this);
		mTitle.setTitle("派车单详情");
		//签收过后的item
		mSignItem = (MyItemView) findViewById(R.id.sign_item);
		mSignItem.setItemIcon(R.drawable.qianshou);
		mSignItem.setItemName("已签收");
		//地址详情
		mDetailsAddressView = (DetailsAddressView) findViewById(R.id.address_view);
		mDetailsAddressView.hideNumber();
		//备注
		mRemarkContent = (TextView) findViewById(R.id.remark_content);
		//驳回
		contact_imag = (ImageView) findViewById(R.id.contact_imag);
		reject_imag = (ImageView) findViewById(R.id.reject_imag);
		//运输状态
		mTransportStateItem = (AuctionItem) findViewById(R.id.transport_state_item);
		mTransportStateItem.setName("运输状态");
//		mTransportStateItem.setContent("未开始");
		mTransportStateItem.setContentColor(getResources().getColor(R.color.theme_color));
		//货物名称
		mGoodsNameItem = (AuctionItem) findViewById(R.id.goods_name_item);
		mGoodsNameItem.setName("货物名称");
//		mGoodsNameItem.setContent("红狮水泥");
		//货物重量
		mGoodsWeightItem = (AuctionItem) findViewById(R.id.goods_weight_item);
		mGoodsWeightItem.setName("货物重量");
//		mGoodsWeightItem.setContent("40kg");
		//发货时间
		mGoodSendTimeItem = (AuctionItem) findViewById(R.id.goods_send_time_item);
		mGoodSendTimeItem.setName("发货时间");
//		mGoodSendTimeItem.setContent("2015-2-24");
		//到货时间
		mGoodArriveTimeItem = (AuctionItem) findViewById(R.id.goods_arrive_time_item);
		mGoodArriveTimeItem.setName("到货时间");
//		mGoodArriveTimeItem.setContent("2015-2-24");
		//运输车辆
		mTruckInfoItem = (AuctionItem) findViewById(R.id.truck_info_item);
		mTruckInfoItem.setName("运输车辆");
//		mTruckInfoItem.setContent("浙A219");
		//货物型号
		mGoodsModelType = (AuctionItem) findViewById(R.id.goods_model_item);
		mGoodsModelType.setName("型号");
		//运输里程
		mTransportMilItem = (AuctionItem) findViewById(R.id.transport_mil_item);
		mTransportMilItem.setName("运输里程");
//		mTransportMilItem.setContent("1000公里");
		//发货人信息
		mShipperPhoneItem = (AuctionItem) findViewById(R.id.shipper_phone_item);
		mShipperPhoneItem.setName("发货人");
//		mShipperPhoneItem.setContent("姓名  15158172356");
		mShipperPhoneItem.getRightImage().setImageResource(R.drawable.call);
		//收货人信息
		mConsigneePhoneItem = (AuctionItem) findViewById(R.id.consignee_phone_item);
		mConsigneePhoneItem.setName("收货人");
//		mConsigneePhoneItem.setContent("姓名  15158172356");
		mConsigneePhoneItem.getRightImage().setImageResource(R.drawable.call);

		mGoodsTransitBtn = (Button) findViewById(R.id.goods_transit_btn);
		//派车单号
		transitTaskId = (TextView) findViewById(R.id.number);
		mFinishLayout = (LinearLayout) findViewById(R.id.finish_layout);
		send_layout = (LinearLayout) findViewById(R.id.send_layout);
		mFinishTime = (TextView) findViewById(R.id.finish_time);
		mCreatTime = (TextView) findViewById(R.id.creat_time);
		mSendTime = (TextView) findViewById(R.id.send_time);
		huidan_info_text = (TextView) findViewById(R.id.huidan_info_text);
		upload_sheet_info_text = (TextView) findViewById(R.id.upload_sheet_info_text);
		
		receiptPhotoLayout = (RelativeLayout) findViewById(R.id.receipt_photo_layout);
		receipt_photo_layout1 = (RelativeLayout) findViewById(R.id.receipt_photo_layout1);
		goodsRemarkLayout = (RelativeLayout) findViewById(R.id.goods_remark_layout);
		uploadTransitAmount = (TextView) findViewById(R.id.upload_transit_amount_text);
		uploadReceiptImage = (ImageView) findViewById(R.id.upload_transit_imageview);
		upload_transit_imageview1 = (ImageView) findViewById(R.id.upload_transit_imageview1);
		mTransitStatusBtn = (Button) findViewById(R.id.transit_status_btn);
		mTransitStatusBtn.setOnClickListener(this);
		if(DidiApp.isUserAowner){
			mTransitStatusBtn.setVisibility(View.VISIBLE);
		}else {
			mTransitStatusBtn.setVisibility(View.GONE);
		}
		
		//0 表示拍照或者图库选取
		mImageDialog = new DataFillingDialog(TruckingOrderDetailsActivity.this, R.style.data_filling_dialog, mHandler, 0);
		mImageDialog.setCanceledOnTouchOutside(true);
		mImageDialog.setText("拍照", "图库选取");
		UploadUtil.setAnimation(mImageDialog, CommonRes.TYPE_BOTTOM, false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sign_item:
			Intent intent_Detail = new Intent(TruckingOrderDetailsActivity.this,TransitDetailActivity.class);
			intent_Detail.putExtra("taskId", taskId);
			intent_Detail.putExtra("isTruckingOrderDetailsActivity", true);
			startActivity(intent_Detail);
			break;
		case R.id.goods_transit_btn:
			if(mTransitTaskDetailModel.getStatus() == 2){
				//运输中
				getSharedPreferences("config",Context.MODE_PRIVATE)
						.edit().putString("submit_img_type","upload_receipt_image").commit();
				mImageDialog.show();
			}else if(mTransitTaskDetailModel.getStatus() == 4){
				//审核中
				Util.call(TruckingOrderDetailsActivity.this, getString(R.string.contact_number));
			}
			break;
		case R.id.address_view:
			Intent intent = new Intent(TruckingOrderDetailsActivity.this,
					MapActivity.class);
			Bundle b = new Bundle();
			b.putString("mapType", "StartEnd");
			GoodsBubbleMsg startMsg = new GoodsBubbleMsg(),
			endMsg = new GoodsBubbleMsg();
			startMsg.setMessage(mTransitTaskDetailModel.getSendProvince(),
					mTransitTaskDetailModel.getSendCity(),
					mTransitTaskDetailModel.getSendAddr());

			endMsg.setMessage(mTransitTaskDetailModel.getRecvProvince(),
					mTransitTaskDetailModel.getRecvCity(),
					mTransitTaskDetailModel.getRecvAddr());

			b.putString("startProvinceAndCity", startMsg.getProvinceAndCity());
			b.putString("startLat", "" + mTransitTaskDetailModel.getSendLat());
			b.putString("startLng", "" + mTransitTaskDetailModel.getSendLng());
			b.putString("startAddr", startMsg.getAddr());

			b.putString("endProvinceAndCity", endMsg.getProvinceAndCity());
			b.putString("endLat", "" + mTransitTaskDetailModel.getRecvLat());
			b.putString("endLng", "" + mTransitTaskDetailModel.getRecvLng());
			b.putString("endAddr", endMsg.getAddr());
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.contact_imag:
			Util.call(TruckingOrderDetailsActivity.this, getString(R.string.contact_number));
			break;
		case R.id.reject_imag:
			Intent intent_reject = new Intent(TruckingOrderDetailsActivity.this,RejectActivity.class);
			intent_reject.putExtra("taskId", taskId);
			startActivity(intent_reject);
			break;
		case R.id.transit_status_btn:
			Intent mIntent = new Intent();
			mIntent.setClass(TruckingOrderDetailsActivity.this, TransportStatusActivity.class);
			Bundle bundle = new Bundle();
			List<TaskStatusTrackVO> list = null;
			if(mTransitTaskDetailModel != null){
			list = mTransitTaskDetailModel.getTSTVOList();
			}
			ArrayList<TaskStatusTrackVO> arrayList = new ArrayList<TaskStatusTrackVO>();
			if(list != null){
				for(TaskStatusTrackVO status:list){
					arrayList.add(status);
				}
				bundle.putSerializable("transport", arrayList);
				mIntent.putExtras(bundle);
				startActivityForResult(mIntent, this.TRANSIT_STATUS);
			}else{
				ToastUtil.show(TruckingOrderDetailsActivity.this, "数据出错");
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GetPhotoUtil.PHOTO_REQUEST_CAMERA && resultCode == RESULT_OK) {
			if (UploadUtil.hasSdcard()) {
				if (data != null) {
					// 得到图片的全路径
					String path = data.getStringExtra(ActivityCapture.kPhotoPath);
					PhotoPreviewActivity.open(this, path, CommonRes.TYPE_HUIDAN);
				}
			}
		}else if (requestCode == GetPhotoUtil.PHOTO_REQUEST_GALLERY &&  resultCode == RESULT_OK) {
			if (UploadUtil.hasSdcard()) {
				Uri uri = data.getData();
				GetPhotoUtil.crop(TruckingOrderDetailsActivity.this, uri, CommonRes.tempFile);
			}
		}else if (requestCode == GetPhotoUtil.PHOTO_REQUEST_CUT && resultCode == RESULT_OK){
			String path = ImageUtil.getImageAbsolutePath(TruckingOrderDetailsActivity.this, Uri.fromFile(CommonRes.tempFile));
			//调起是否上传回单对话框
			UploadReceiptDialog uploadDialog = new UploadReceiptDialog(TruckingOrderDetailsActivity.this,
					R.style.data_filling_dialog, mHandler, taskId, path,
					mTransitTaskDetailModel.getSettleUnitText());
			UploadUtil.setAnimation(uploadDialog, CommonRes.TYPE_BOTTOM, true);
			uploadDialog.setCanceledOnTouchOutside(false);
			uploadDialog.show();
		}else if(requestCode == this.TRANSIT_STATUS && resultCode == 0){
			getData(mTransitId);
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mRefreshBroadcastReceiver);
	}
}
