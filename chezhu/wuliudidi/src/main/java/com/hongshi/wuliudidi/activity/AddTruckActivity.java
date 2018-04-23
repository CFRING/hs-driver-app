package com.hongshi.wuliudidi.activity;

import java.io.File;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.dialog.AddTruckPop;
import com.hongshi.wuliudidi.dialog.DataFillingDialog;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.CallBackString;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.TruckTypeModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.ImageUtil;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.AuctionItem;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddTruckActivity extends Activity implements OnClickListener{
	private DiDiTitleView mTitle;
	private AuctionItem mTruckLengthItem,mTruckLoadItem,mTruckSpaceItem;
	private String mTruckLoad, mTruckSpace;
	private  AddTruckPop mAddTruckPop;
	private TextView mLicenceText;
	private RelativeLayout mSelectLayout;
	//添加车辆接口
	private String submit_url = GloableParams.HOST + "uic/authentication/createOrUpdateTruck.do?";

	//获取车辆信息
	private String get_truckinfo_url = GloableParams.HOST + "uic/authentication/getTruck.do?";

	//上传图片
	private String upload_photo_url = GloableParams.HOST + "uic/authentication/uploadTruckPhoto.do?";
	
	private String truckId = "";
	private DataFillingDialog mImageDialog;
	private ImageView mTruckHeadImage,mTruckSideImage,mTruckEndImage,mDriverLicenseImage,mTransiteLicenceImage
			,mTransiteLicenseBackImage;
	private String upLoadParams = "";
	private EditText mTruckEdit;
	private String truckTypeId,truckLengthId;
	private Button mSubmit;
	private boolean isChangeEnabled = true;
	private RelativeLayout rejectReasonLayout;
	private TextView rejectReasonText;
	@SuppressLint("HandlerLeak") private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CommonRes.CAMERA:
				UploadUtil.camera(AddTruckActivity.this,mHandler);
				break;
			case CommonRes.GALLERY:
				UploadUtil.photo(AddTruckActivity.this,mHandler);
				break;
			default:
				break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.add_truck_activity);
		initViews();
	}

	private void initViews(){
		mTitle = (DiDiTitleView) findViewById(R.id.add_truck_title);
		mTitle.setBack(this);
		mTitle.setTitle("添加车辆");
		try {
			truckId = getIntent().getExtras().getString("truckId");
		} catch (Exception e) {
			truckId = "";
		}

		rejectReasonLayout = (RelativeLayout) findViewById(R.id.reject_reason_layout);
		rejectReasonText = (TextView) findViewById(R.id.reject_reason_text);

		//选择车牌的layout
		mSelectLayout = (RelativeLayout) findViewById(R.id.select_layout);
		mLicenceText = (TextView) findViewById(R.id.licence_text);

		mTruckEdit = (EditText) findViewById(R.id.truck_number);
		//添加车型车长item
		mTruckLengthItem = (AuctionItem) findViewById(R.id.truck_length);
		mTruckLengthItem.setName("车型车长");
		mTruckLengthItem.setContent("请选择您的车辆类型和车长");
		mTruckLengthItem.setContentColor(getResources().getColor(R.color.line_color));
		mTruckLengthItem.getRightImage().setVisibility(View.VISIBLE);
		//载重
		mTruckLoadItem = (AuctionItem) findViewById(R.id.truck_load);
		mTruckLoadItem.setName("载重");
		mTruckLoadItem.setHint("请输入车辆最大载重量(吨)");
		mTruckLoadItem.setHinttColor(getResources().getColor(R.color.line_color));
		mTruckLoadItem.setInputLimit(getResources().getString(R.string.double_limit));
		//载货空间
		mTruckSpaceItem = (AuctionItem) findViewById(R.id.truck_space);
		mTruckSpaceItem.setHint("请输入载货空间");
		mTruckSpaceItem.setName("载货空间");
		mTruckSpaceItem.setHinttColor(getResources().getColor(R.color.line_color));
		mTruckSpaceItem.setInputLimit(getResources().getString(R.string.double_limit));

		//要上传图片的控件
		mTruckHeadImage = (ImageView) findViewById(R.id.truck_head);
		mTruckSideImage = (ImageView) findViewById(R.id.truck_side);
		mTruckEndImage = (ImageView) findViewById(R.id.truck_end);
		mDriverLicenseImage = (ImageView) findViewById(R.id.driver_license);
		mTransiteLicenceImage = (ImageView) findViewById(R.id.transite_license);
		mTransiteLicenseBackImage = (ImageView) findViewById(R.id.transite_license_back);

		mSubmit = (Button) findViewById(R.id.submit);

		//0 表示拍照或者图库选取
		mImageDialog = new DataFillingDialog(AddTruckActivity.this, R.style.data_filling_dialog, mHandler, 0);
		mImageDialog.setCanceledOnTouchOutside(true);
		mImageDialog.setText("拍照", "图库选取");

		if(!truckId.equals("")){
			loadImage(truckId);
		}
		mSelectLayout.setOnClickListener(this);
		mTruckLengthItem.setOnClickListener(this);
//		mTruckSpaceItem.setOnClickListener(this);

		mTruckHeadImage.setOnClickListener(this);
		mTruckSideImage.setOnClickListener(this);
		mTruckEndImage.setOnClickListener(this);
		mDriverLicenseImage.setOnClickListener(this);
		mTransiteLicenceImage.setOnClickListener(this);
		mTransiteLicenseBackImage.setOnClickListener(this);
		mSubmit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.truck_length:
			mAddTruckPop = new AddTruckPop(AddTruckActivity.this, 3,new CallBackString() {
				@Override
				public void getStr(String s,String lengthId,String typeId) {
					mTruckLengthItem.setContent(s);
					truckTypeId = typeId;
					truckLengthId = lengthId;
				}
			});
			mAddTruckPop.setShow(v);
			break;
		case R.id.select_layout:
			mAddTruckPop = new AddTruckPop(AddTruckActivity.this, 1,new CallBackString() {
				@Override
				public void getStr(String s,String truckLengthId,String truckTypeId) {
					mLicenceText.setText(s);
				}
			});
			mAddTruckPop.setShow(v);
			break;
		case R.id.truck_space:
			Intent truck_space_intent = new Intent(AddTruckActivity.this,TruckSpaceActivity.class);
			
			startActivity(truck_space_intent);
			break;
		case R.id.truck_head:
			if(!isChangeEnabled){
				ToastUtil.show(AddTruckActivity.this, getResources().getString(R.string.truck_modify_dynied));
				return;
			}
			upLoadParams = "frontTruckPhoto";
			UploadUtil.setAnimation(mImageDialog,CommonRes.TYPE_BOTTOM, false);
			mImageDialog.show();
			break;
		case R.id.truck_side:
			if(!isChangeEnabled){
				ToastUtil.show(AddTruckActivity.this, getResources().getString(R.string.truck_modify_dynied));
				return;
			}
			upLoadParams = "middleTruckPhoto";
			UploadUtil.setAnimation(mImageDialog,CommonRes.TYPE_BOTTOM, false);
			mImageDialog.show();
			break;
		case R.id.truck_end:
			if(!isChangeEnabled){
				ToastUtil.show(AddTruckActivity.this, getResources().getString(R.string.truck_modify_dynied));
				return;
			}
			upLoadParams = "backTruckPhoto";
			UploadUtil.setAnimation(mImageDialog,CommonRes.TYPE_BOTTOM, false);
			mImageDialog.show();
			break;
		case R.id.driver_license:
			if(!isChangeEnabled){
				ToastUtil.show(AddTruckActivity.this, getResources().getString(R.string.truck_modify_dynied));
				return;
			}
			upLoadParams = "truckLicensePhoto";
			UploadUtil.setAnimation(mImageDialog,CommonRes.TYPE_BOTTOM, false);
			mImageDialog.show();
			break;
		case R.id.transite_license:
			if(!isChangeEnabled){
				ToastUtil.show(AddTruckActivity.this, getResources().getString(R.string.truck_modify_dynied));
				return;
			}
			upLoadParams = "roadTransportPhoto";
			UploadUtil.setAnimation(mImageDialog,CommonRes.TYPE_BOTTOM, false);
			mImageDialog.show();
			break;
		case R.id.transite_license_back:
			if(!isChangeEnabled){
				ToastUtil.show(AddTruckActivity.this, getResources().getString(R.string.truck_modify_dynied));
				return;
			}
			upLoadParams = "backRoadTransportPhoto";
			UploadUtil.setAnimation(mImageDialog,CommonRes.TYPE_BOTTOM, false);
			mImageDialog.show();
			break;
		case R.id.submit:
			if(!isChangeEnabled){
				ToastUtil.show(AddTruckActivity.this, getResources().getString(R.string.truck_modify_dynied));
				return;
			}
			submit();
			break;
		default:
			break;
		}
		
	}
	//上传
	private void uploadFile(String url,String imageName,InputStream in) {
		try {
			final PromptManager mPromptManager = new PromptManager();
			mPromptManager.showProgressDialog(this, "正在上传图片");
			AjaxParams params = new AjaxParams();
            params.put(imageName, in,"img.png");
            params.put("truckId",truckId);
            DidiApp.getHttpManager().sessionPost(AddTruckActivity.this, url, params, new ChildAfinalHttpCallBack() {
				@Override
				public void data(String t) {
					mPromptManager.closeProgressDialog();
					Toast.makeText(AddTruckActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
					try {
						JSONObject jsonObject = new JSONObject(t);
						String all = jsonObject.getString("body");
						TruckTypeModel mTruckTypeModel = JSON.parseObject(all,TruckTypeModel.class);
						FinalBitmap mitmap = FinalBitmap.create(AddTruckActivity.this);
						mitmap.display(mTruckHeadImage, mTruckTypeModel.getFrontTruckPhoto());
						mitmap.display(mTruckSideImage, mTruckTypeModel.getMiddleTruckPhoto());
						mitmap.display(mTruckEndImage, mTruckTypeModel.getBackTruckPhoto());
						mitmap.display(mDriverLicenseImage, mTruckTypeModel.getTruckLicensePhoto());
						mitmap.display(mTransiteLicenceImage, mTruckTypeModel.getRoadTransportPhoto());
						mitmap.display(mTransiteLicenseBackImage, mTruckTypeModel.getBackRoadTransportPhoto());
						truckId = mTruckTypeModel.getTruckId();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(String errCode, String errMsg, Boolean errSerious) {
					mPromptManager.closeProgressDialog();
				}

			});
		} catch (Exception e) {
		}
	}
	//加载信息
	private void loadImage(String truckId) {
		AjaxParams params = new AjaxParams();
		params.put("truckId", truckId);
		DidiApp.getHttpManager().sessionPost(AddTruckActivity.this, get_truckinfo_url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				if(t.equals("")){
					return;
				}
				try {
					JSONObject jsonObject = new JSONObject(t);
					String all = jsonObject.getString("body");
					
					TruckTypeModel mTruckTypeModel = JSON.parseObject(all,TruckTypeModel.class);
					if(mTruckTypeModel.getStatus() == 2){
						rejectReasonLayout.setVisibility(View.VISIBLE);
						rejectReasonText.setText(mTruckTypeModel.getAuditNoteTypeText());
					}else{
						rejectReasonLayout.setVisibility(View.GONE);
					}
					
					if(mTruckTypeModel.getStatus() == 3){
						//审核通过不可修改标记
						isChangeEnabled = false;
						mSubmit.setBackgroundResource(R.color.gray);
					}
					FinalBitmap mitmap = FinalBitmap.create(AddTruckActivity.this);
					mitmap.display(mTruckHeadImage, mTruckTypeModel.getFrontTruckPhoto());
					mitmap.display(mTruckSideImage, mTruckTypeModel.getMiddleTruckPhoto());
					mitmap.display(mTruckEndImage, mTruckTypeModel.getBackTruckPhoto());
					mitmap.display(mDriverLicenseImage, mTruckTypeModel.getTruckLicensePhoto());
					mitmap.display(mTransiteLicenceImage, mTruckTypeModel.getRoadTransportPhoto());
					mitmap.display(mTransiteLicenseBackImage, mTruckTypeModel.getBackRoadTransportPhoto());
					
					if(mTruckTypeModel.getTruckNumber().length() == 7){
						String truckprovince,truckletter,trucknumber;
						trucknumber = mTruckTypeModel.getTruckNumber();
						//省份是车牌号的第一个位
						truckprovince = String.copyValueOf(trucknumber.toCharArray(), 0, 1);
						//城市字符是车牌号的第二位
						truckletter = String.copyValueOf(trucknumber.toCharArray(), 1, 1);
						//去掉前两位
						trucknumber = String.copyValueOf(trucknumber.toCharArray(), 2, trucknumber.length() - 2);
						mLicenceText.setText(truckprovince + " " + truckletter);
						mTruckEdit.setText(trucknumber);
					}
					if(!mTruckTypeModel.getTruckLengthText().equals("")){
						mTruckLengthItem.setContent(mTruckTypeModel.getTruckTypeText() +"  "+ mTruckTypeModel.getTruckLengthText());
					}
					if(mTruckTypeModel.getCarryCapacity()>0){
						mTruckLoadItem.setEditContent(""+mTruckTypeModel.getCarryCapacity());
					}
					if(mTruckTypeModel.getCarryVolume()>0){
						mTruckSpaceItem.setEditContent(""+mTruckTypeModel.getCarryVolume());
					}
					truckTypeId = mTruckTypeModel.getTruckTypeId();
					truckLengthId = mTruckTypeModel.getTruckLengthId();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	private void submit(){
		if(!inputJudge()){
			return;
		}
		final PromptManager mPromptManager = new PromptManager();
		mPromptManager.showProgressDialog(AddTruckActivity.this, "上传中");
		AjaxParams params = new AjaxParams();
		params.put("truckTypeId", truckTypeId);
		params.put("truckLengthId", truckLengthId);
		params.put("truckNumber", mLicenceText.getText().toString().replace(" ", "") +mTruckEdit.getText().toString());//车牌
		params.put("carryCapacity", mTruckLoad);//最大载重量
		params.put("carryVolume", mTruckSpace);//载货空间
		params.put("truckId",truckId);
		DidiApp.getHttpManager().sessionPost(AddTruckActivity.this, submit_url, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				try {
					Thread.sleep(2000);
					mPromptManager.closeProgressDialog();
//					ToastUtil.show(AddTruckActivity.this, "上传成功");
					//通知"车辆信息"页面刷新
					Intent intent = new Intent();
					intent.setAction(CommonRes.RefreshTruck);
					sendBroadcast(intent);
					intent = new Intent();
					intent.setAction(CommonRes.RefreshUserInfo);
					sendBroadcast(intent);
					
					Intent authResult_intent = new Intent(AddTruckActivity.this, AuthResultActivity.class);
					authResult_intent.putExtra("from", "truck");
					startActivity(authResult_intent);
					finish();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				mPromptManager.closeProgressDialog();
			}
		});
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode ==UploadUtil.PHOTO_REQUEST_GALLERY && resultCode == RESULT_OK) {
			if (data != null) {
				// 得到图片的全路径
				Uri uri = data.getData();
				String path = ImageUtil.getImageAbsolutePath(AddTruckActivity.this, uri);
				uploadFile(upload_photo_url,upLoadParams ,ImageUtil.getimage(path));
			}
		}else if (requestCode == UploadUtil.PHOTO_REQUEST_CAMERA &&  resultCode == RESULT_OK) {
			if (UploadUtil.hasSdcard()) {
				if(UploadUtil.tempFile == null){
					UploadUtil.tempFile = new File(Environment.getExternalStorageDirectory(),UploadUtil.PHOTO_FILE_NAME);	
				}
					uploadFile(upload_photo_url,upLoadParams,ImageUtil.getimage(ImageUtil.
							getImageAbsolutePath(AddTruckActivity.this, Uri.fromFile(UploadUtil.tempFile))));	
					UploadUtil.tempFile = null;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private boolean inputJudge(){
		//车牌号不能为空，且必须为5位，已在布局文件中限制为仅含字母（含大小写）和数字
		if(mTruckEdit.getText().toString().length() <= 0){
			ToastUtil.show(AddTruckActivity.this, "请输入车牌号");
			return false;
		}else if(mTruckEdit.getText().toString().length() != 5){
			ToastUtil.show(AddTruckActivity.this, "请输入正确车牌号");
			return false;
		}

		//车型车长不能为空
		if(truckTypeId == null || truckLengthId == null){
			ToastUtil.show(AddTruckActivity.this, "请选择车型及车长");
			return false;
		}
		
		//载重量不能为空
		if(mTruckLoadItem.getEditContent().length() <= 0){
			ToastUtil.show(AddTruckActivity.this, "请输入载重量");
			return false;
		}
		//载货空间不能为空
		if(mTruckSpaceItem.getEditContent().length() <= 0){
			ToastUtil.show(AddTruckActivity.this, "请输入载货空间");
			return false;
		}
		
		//载重量必须能转化为double
		mTruckLoad = Util.inputToDoubleStr(mTruckLoadItem.getEditContent());
		if(mTruckLoad == ""){
			ToastUtil.doubleParseError(AddTruckActivity.this, "载重量");
			return false;
		}else if(mTruckLoad == "0"){
			ToastUtil.doubleNonpositiveError(AddTruckActivity.this, "载重量");
			return false;
		}
		
		//载货空间必须能转化为double
		mTruckSpace = Util.inputToDoubleStr(mTruckSpaceItem.getEditContent());
		if(mTruckSpace == ""){
			ToastUtil.doubleParseError(AddTruckActivity.this, "载货空间");
			return false;
		}else if(mTruckSpace == "0"){
			ToastUtil.doubleNonpositiveError(AddTruckActivity.this, "载货空间");
			return false;
		}
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("AddTruckActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("AddTruckActivity");
	}
}
