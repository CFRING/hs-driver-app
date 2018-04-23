package com.hongshi.wuliudidi.activity;

import java.io.InputStream;
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
import com.hongshi.wuliudidi.adapter.TruckTypeGridViewAdapter;
import com.hongshi.wuliudidi.camera.ActivityCapture;
import com.hongshi.wuliudidi.camera.BitmapUtil;
import com.hongshi.wuliudidi.camera.PhotoPreviewActivity;
import com.hongshi.wuliudidi.dialog.AddTruckPop;
import com.hongshi.wuliudidi.dialog.DataFillingDialog;
import com.hongshi.wuliudidi.dialog.ListItemDeletingDialog;
import com.hongshi.wuliudidi.impl.CallBackString;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.TruckAuthModel;
import com.hongshi.wuliudidi.model.TruckTypeGridModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.photo.GetPhotoUtil;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.ImageUtil;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.AddTruckPhotoItem;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.hongshi.wuliudidi.view.NoScrollGridView;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author huiyuan
 */
public class AddTruckNewActivity extends Activity implements OnClickListener{
	private DiDiTitleView mTitle;
	private NoScrollGridView truckTypeGridView, truckCarriageGridView;
	private List<TruckTypeGridModel> truckTypeList, truckCarriageList;
	private TruckTypeGridViewAdapter typeAdapter, carriageAdapter;
	private ToggleButton isDumpTruckToggle;
	private AddTruckPhotoItem truckHeadPhoto, truckTailPhoto, truckHeadLicencePhoto, truckTailLicencePhoto,
		transiLicenceFrontPhoto, transiLicenceBackPhoto;
	private LinearLayout firstStepLayout, nextStepLayout, submitLayout, truckCarriageLayout, plateNumberHeadLayout;
	private RelativeLayout singleTruckTypeLayout, singleTruckCarrigeLayout;
	private ImageView singleTruckTypeImage, singleTruckCarrigeImage;
	private Button lastStep, submitToAudit, nextStep;
	private TextView passedText, plateNumberHeadText, hintText, singleTypeNameText, singleCarrigeNameText;
	private EditText truckNumberEditText, maxLoadEditText, maxSpaceEditText, nongyongHeadEditText;
	private View nongyongLittleLine;
	private DataFillingDialog mImageDialog;
    private ListItemDeletingDialog mDeletingDialog;
	//页面状态；默认显示第一部分
	private PageType pageType = PageType.FirstStep;
	private String truckId;
	//拍摄完，或者从相册选择完照片，接收到广播的时候，用于表示照片名称
	private CommonRes.PhotoTag photoTag;
	//用户选择的车型值和车厢类型值
	private int truckTypeValue = -1, truckCarriageValue = -1;
	private boolean hasTruckHead, hasTruckTail, hasTruckHeadLicence, hasTruckTailLicence, hasTransiLicenceFront, hasTransiLicenceBack;
	//上传基本信息
	private String basicMessageUrl = GloableParams.HOST + "uic/authentication/createOrUpdateTruck.do?";
	//上传照片（单张多张都可以）
	private String uploadPhotoUrl = GloableParams.HOST + "uic/authentication/uploadTruckDataPhoto.do";
	//获取车辆信息
	private String getTruckInfoUrl = GloableParams.HOST + "uic/authentication/getTruck.do?";
	//提交审核
	private String submitToAuditUrl = GloableParams.HOST + "uic/authentication/updateTruckDataStatus.do";
	//删除车辆接口
	private String deleteTruckUrl = GloableParams.HOST + "uic/authentication/deleteTruck.do?";
	private TruckAuthModel truckModel;
	
	private boolean editable = true;
	//选择照片完成的广播接收

	private boolean isAddNewTruck = true;

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("AddTruckNewActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("AddTruckNewActivity");
	}

	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(CommonRes.ACTIONPHOTOPATH)) {
				String imagePath = intent.getStringExtra("imagePath");
				try {
					int tag = intent.getExtras().getInt("tag", -1);
					photoTag = CommonRes.PhotoTag.values()[tag];
				} catch (Exception e) {
					photoTag = CommonRes.PhotoTag.truckHeadPhoto;
				}
				uploadImage(photoTag, imagePath);
			}
		}
	};
	
	@SuppressLint("HandlerLeak") private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CommonRes.CAMERA:
				GetPhotoUtil.callCamera(AddTruckNewActivity.this);
				break;
			case CommonRes.GALLERY:
				GetPhotoUtil.callGallery(AddTruckNewActivity.this);
				break;
			case CommonRes.DELETE_TRUCK:
				if(truckModel != null && truckModel.getTruckId() != null){
					deleteTruck(truckModel.getTruckId());
				}
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
		
		setContentView(R.layout.add_truck_activity_new);

		initViews();
		initData();
	}

	private void initViews(){
		try {
			truckId = getIntent().getStringExtra("truckId");
		} catch (Exception e) {
			truckId = null;
		}

		isAddNewTruck = getIntent().getBooleanExtra("add_new_truck",false);
		try {
			String typeStr = getIntent().getStringExtra("pageType");
			pageType = PageType.valueOf(typeStr);

		} catch (Exception e) {
			pageType = PageType.FirstStep;
		}

		mTitle = (DiDiTitleView) findViewById(R.id.add_truck_title);
		mTitle.getBackImageView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (pageType) {
					case NextStep:
//					showPage(PageType.FirstStep);
//					break;
					default:
						finish();
						break;
				}

			}
		});
		if(truckId != null){
			mTitle.getRightTextView().setText(getResources().getText(R.string.manager));
			mTitle.getRightTextView().setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mDeletingDialog != null && truckModel != null) {
						mDeletingDialog.show();
					}
				}
			});
		}

		firstStepLayout = (LinearLayout) findViewById(R.id.first_step_layout);
		nextStepLayout = (LinearLayout) findViewById(R.id.next_step_layout);

		truckTypeList = new ArrayList<TruckTypeGridModel>();
		truckTypeList.add(new TruckTypeGridModel(R.drawable.quanguache, TruckTypeGridModel.TruckType.quangua));
		truckTypeList.add(new TruckTypeGridModel(R.drawable.banguache, TruckTypeGridModel.TruckType.bangua));
		truckTypeList.add(new TruckTypeGridModel(R.drawable.nongyongche, TruckTypeGridModel.TruckType.nongyong));
		typeAdapter = new TruckTypeGridViewAdapter(AddTruckNewActivity.this, truckTypeList);
		truckTypeGridView = (NoScrollGridView) findViewById(R.id.truck_type_gridview);
		truckTypeGridView.setAdapter(typeAdapter);

		truckCarriageList = new ArrayList<TruckTypeGridModel>();
		truckCarriageList.add(new TruckTypeGridModel(R.drawable.xiangshiche, TruckTypeGridModel.TruckType.xiangshi));
		truckCarriageList.add(new TruckTypeGridModel(R.drawable.dilanche, TruckTypeGridModel.TruckType.dilan));
		truckCarriageList.add(new TruckTypeGridModel(R.drawable.gaolanche, TruckTypeGridModel.TruckType.gaolan));
		truckCarriageList.add(new TruckTypeGridModel(R.drawable.pingbanche, TruckTypeGridModel.TruckType.pingban));
		truckCarriageList.add(new TruckTypeGridModel(R.drawable.cangzhache, TruckTypeGridModel.TruckType.cangzha));
		truckCarriageList.add(new TruckTypeGridModel(R.drawable.guanche, TruckTypeGridModel.TruckType.guanche));
		carriageAdapter = new TruckTypeGridViewAdapter(AddTruckNewActivity.this, truckCarriageList);
		truckCarriageGridView = (NoScrollGridView) findViewById(R.id.truck_carriage_gridview);
		truckCarriageGridView.setAdapter(carriageAdapter);

		hintText = (TextView) findViewById(R.id.hint_text);

		singleTruckTypeLayout = (RelativeLayout) findViewById(R.id.single_truck_type_layout);
		singleTruckTypeImage = (ImageView) findViewById(R.id.single_truck_type_image);
		singleTypeNameText = (TextView) findViewById(R.id.type_name_text);

		singleTruckCarrigeLayout = (RelativeLayout) findViewById(R.id.single_truck_carrige_layout);
		singleTruckCarrigeImage = (ImageView) findViewById(R.id.single_truck_carrige_image);
		singleCarrigeNameText = (TextView) findViewById(R.id.carrige_name_text);

		isDumpTruckToggle = (ToggleButton) findViewById(R.id.is_dump_truck_toggle);
		isDumpTruckToggle.setChecked(true);


		truckHeadPhoto = (AddTruckPhotoItem) findViewById(R.id.truck_head_photo);
		truckTailPhoto = (AddTruckPhotoItem) findViewById(R.id.truck_tail_photo);
		truckHeadLicencePhoto = (AddTruckPhotoItem) findViewById(R.id.truck_head_Licence_photo);
		truckTailLicencePhoto = (AddTruckPhotoItem) findViewById(R.id.truck_tail_licence_photo);
		transiLicenceFrontPhoto = (AddTruckPhotoItem) findViewById(R.id.transi_licence_front_photo);
		transiLicenceBackPhoto = (AddTruckPhotoItem) findViewById(R.id.transi_licence_back_photo);

		truckCarriageLayout = (LinearLayout) findViewById(R.id.truck_carriage_layout);
		submitLayout = (LinearLayout) findViewById(R.id.submit_layout);
		lastStep = (Button) findViewById(R.id.last_step);
		submitToAudit = (Button) findViewById(R.id.submit_to_audit);
		nextStep = (Button) findViewById(R.id.next_step);
		passedText = (TextView) findViewById(R.id.passed_text);

		plateNumberHeadLayout = (LinearLayout) findViewById(R.id.choose_plate_number_head);
		plateNumberHeadText = (TextView) findViewById(R.id.truck_number_head_text);
		truckNumberEditText = (EditText) findViewById(R.id.plate_number_edittext);
		//农用车牌号头两位
		nongyongHeadEditText = (EditText) findViewById(R.id.nongyong_head_number_edittext);
		//农用车牌号头两位后面有根小竖线
		nongyongLittleLine = (View) findViewById(R.id.line_5);
		maxLoadEditText = (EditText) findViewById(R.id.truck_load_edittext);
		//最大载重输入限制，小数点后不能超过两位字符
		Util.getDoubleInputLimitTextWatcher().setEditText(maxLoadEditText);
		Util.getNumericLimitTextWatcher(0, 1000).setEditText(maxLoadEditText);

		maxSpaceEditText = (EditText) findViewById(R.id.truck_space_edittext);
		//载货空间输入限制，小数点后不能超过两位字符
		Util.getDoubleInputLimitTextWatcher().setEditText(maxSpaceEditText);

		//注册照片选取的广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CommonRes.ACTIONPHOTOPATH);
		registerReceiver(mRefreshBroadcastReceiver, intentFilter);

		//初始化删除车辆对话框
		mDeletingDialog = new ListItemDeletingDialog(AddTruckNewActivity.this, R.style.data_filling_dialog, mHandler);
		mDeletingDialog.setCanceledOnTouchOutside(true);
		mDeletingDialog.setText("删除车辆", "取消");
		mDeletingDialog.setMsgNum(CommonRes.DELETE_TRUCK);
		mDeletingDialog.setItemId("");
		mDeletingDialog.getExampleImg().setVisibility(View.GONE);
		UploadUtil.setAnimation(mDeletingDialog, CommonRes.TYPE_BOTTOM, false);
	}

	private void setonClick(){
		if(editable){
			nextStep.setOnClickListener(this);
			plateNumberHeadLayout.setOnClickListener(this);
			lastStep.setOnClickListener(this);
			submitToAudit.setOnClickListener(this);
			
			truckTypeGridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					gridViewSetSelect(truckTypeList, typeAdapter, position);
					
					int lastTruckTypeValue = truckTypeValue;
					truckTypeValue = position + 1;
					if(lastTruckTypeValue != truckTypeValue){
						//如果用户选的车型有变化
						if(truckTypeValue == 3){
							//如果用户选了农用车
							plateNumberHeadText.setText("浙");
							truckCarriageLayout.setVisibility(View.GONE);
							nongyongHeadEditText.setVisibility(View.VISIBLE);
							nongyongLittleLine.setVisibility(View.VISIBLE);
						}else{
							plateNumberHeadText.setText("浙  A");
							truckCarriageLayout.setVisibility(View.VISIBLE);
							nongyongHeadEditText.setVisibility(View.GONE);
							nongyongLittleLine.setVisibility(View.GONE);
						}
					}
				}
			});
			
			truckCarriageGridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					gridViewSetSelect(truckCarriageList, carriageAdapter, position);
					truckCarriageValue = position + 1;
				}
			});
	
			AddTruckPhotoItem.PhotoItemCallBack cbk = new AddTruckPhotoItem.PhotoItemCallBack() {
				@Override
				public void onPhotoGraphClick(CommonRes.PhotoTag tag) {
					photoTag = tag;
					//弹出拍照/相册对话框
					UploadUtil.setAnimation(mImageDialog, CommonRes.TYPE_BOTTOM, false);
					switch (tag) {
					case truckHeadPhoto:
						mImageDialog.setPhotoVISIBLE(R.drawable.truck_head_example);
						break;
					case truckTailPhoto:
						mImageDialog.setPhotoVISIBLE(R.drawable.truck_tail_example);
						break;
					case transiLicenceFrontPhoto:
						mImageDialog.setPhotoVISIBLE(R.drawable.transi_licence_front_example);
						break;
					case transiLicenceBackPhoto:
						mImageDialog.setPhotoVISIBLE(R.drawable.transi_licence_back_example);
						break;
					case truckHeadLicencePhoto:
						mImageDialog.setPhotoVISIBLE(R.drawable.truck_head_licence_example);
						break;
					case truckTailLicencePhoto:
						mImageDialog.setPhotoVISIBLE(R.drawable.truck_tail_licence_example);
						break;
					default:
						break;
					}
					mImageDialog.show();
				}
			};
			truckHeadPhoto.setPhotographOnClick(cbk, CommonRes.PhotoTag.truckHeadPhoto);
			truckTailPhoto.setPhotographOnClick(cbk, CommonRes.PhotoTag.truckTailPhoto);
			truckHeadLicencePhoto.setPhotographOnClick(cbk, CommonRes.PhotoTag.truckHeadLicencePhoto);
			truckTailLicencePhoto.setPhotographOnClick(cbk, CommonRes.PhotoTag.truckTailLicencePhoto);
			transiLicenceFrontPhoto.setPhotographOnClick(cbk, CommonRes.PhotoTag.transiLicenceFrontPhoto);
			transiLicenceBackPhoto.setPhotographOnClick(cbk, CommonRes.PhotoTag.transiLicenceBackPhoto);
		}else{
			nextStep.setClickable(false);
			plateNumberHeadLayout.setClickable(false);
			lastStep.setClickable(false);
			submitToAudit.setClickable(false);
			
			truckTypeGridView.setClickable(false);
			truckCarriageGridView.setClickable(false);
			
			isDumpTruckToggle.setClickable(false);
			
			truckNumberEditText.setEnabled(false);
			maxLoadEditText.setEnabled(false);
			maxSpaceEditText.setEnabled(false);
			nongyongHeadEditText.setEnabled(false);
		}
	}
	
	private void gridViewSetSelect(List<TruckTypeGridModel> list, TruckTypeGridViewAdapter adapter, int position){
		if(list == null || position < 0 || position >= list.size()){
			return;
		}
		for (TruckTypeGridModel i : list) {
			i.setSelected(false);
		}
		list.get(position).setSelected(true);
		adapter.notifyDataSetChanged();
	}
	
	private void initData(){
		truckTypeValue = -1;
		truckCarriageValue = -1;
		hasTruckHead = false;
		hasTruckTail = false;
		hasTruckHeadLicence = false;
		hasTruckTailLicence = false;
		hasTransiLicenceFront = false;
		hasTransiLicenceBack = false;
		
		truckHeadPhoto.setHint("点击上传照片");
		truckTailPhoto.setHint("点击上传照片");
		truckHeadLicencePhoto.setHint("点击上传照片");
		truckTailLicencePhoto.setHint("点击上传照片");
		transiLicenceFrontPhoto.setHint("点击上传照片");
		transiLicenceBackPhoto.setHint("点击上传照片");
		
		
		truckHeadPhoto.getMarkImage().setImageResource(R.drawable.truck_head_mark);
		truckTailPhoto.getMarkImage().setImageResource(R.drawable.truck_tail_mark);
		truckHeadLicencePhoto.getMarkImage().setImageResource(R.drawable.truck_licence_mark);
		truckTailLicencePhoto.getMarkImage().setImageResource(R.drawable.truck_licence_mark);
		transiLicenceFrontPhoto.getMarkImage().setImageResource(R.drawable.transi_licence_mark);
		transiLicenceBackPhoto.getMarkImage().setImageResource(R.drawable.transi_licence_mark);

		truckHeadPhoto.getLargePhoto().setImageResource(R.drawable.truck_image);
		truckTailPhoto.getLargePhoto().setImageResource(R.drawable.truck_image);
		truckHeadLicencePhoto.getLargePhoto().setImageResource(R.drawable.xingshizheng);
		truckTailLicencePhoto.getLargePhoto().setImageResource(R.drawable.xingshizheng);
		transiLicenceFrontPhoto.getLargePhoto().setImageResource(R.drawable.yunyingzm);
		transiLicenceBackPhoto.getLargePhoto().setImageResource(R.drawable.yunyingfm);
		
		//拍照,0 表示拍照或者图库选取
		mImageDialog = new DataFillingDialog(AddTruckNewActivity.this, R.style.data_filling_dialog, mHandler, 0);
		mImageDialog.setCanceledOnTouchOutside(true);
		mImageDialog.setText("拍照", "图库选取");
		
		
		if(truckId == null){
			showPage(PageType.FirstStep);
			mTitle.setTitle("添加车辆");
			setonClick();
		}else{
			mTitle.setTitle("车辆信息");
			loadTruckInfo();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.choose_plate_number_head:
			//选择车牌号头两位
			CallBackString cbs = new CallBackString() {
				@Override
				public void getStr(String s, String truckLengthId, String truckTypeId) {
					plateNumberHeadText.setText(s);
				}
			};
			if(truckTypeValue == 3){
				//只需选省
				AddTruckPop mAddTruckPop = new AddTruckPop(AddTruckNewActivity.this, CommonRes.TYPE_TRUCK_NONGYONG, cbs);
				mAddTruckPop.setShow(v);
			}else{
				//选省，和字母
				AddTruckPop mAddTruckPop = new AddTruckPop(AddTruckNewActivity.this, CommonRes.TYPE_TRUCK_NORMAL, cbs);
				mAddTruckPop.setShow(v);
			}
			break;
		case R.id.next_step:
			if(inputJudge()){
				submitBasicMessage();
			}
			break;
		case R.id.submit_to_audit:
//			if(inputJudge()){
				submitToAudit();
//			}
			break;
		case R.id.last_step:
			showPage(PageType.FirstStep);
			break;
		default:
			break;
		}
		
	}

	private void deleteTruck(String deletedTruckId){
		AjaxParams params = new AjaxParams();
		params.put("truckId", deletedTruckId);
		DidiApp.getHttpManager().sessionPost(AddTruckNewActivity.this, deleteTruckUrl, params, new ChildAfinalHttpCallBack() {
			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				ToastUtil.show(AddTruckNewActivity.this, "删除失败");
			}

			@Override
			public void data(String t) {
				//删除车辆可能使车主变为无车用户，所以需要刷新myfragment
				Intent broadcastIntent = new Intent();
				broadcastIntent.setAction(CommonRes.RefreshUserInfo);
				sendBroadcast(broadcastIntent);
                //删除车辆成功，刷新车辆列表页面
                broadcastIntent = new Intent();
                broadcastIntent.setAction(CommonRes.RefreshTruck);
                sendBroadcast(broadcastIntent);
				//车辆删除成功，关闭页面
				finish();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GetPhotoUtil.PHOTO_REQUEST_CAMERA && resultCode == RESULT_OK) {
			if (data != null) {
				// 得到图片的全路径
				String path = data.getStringExtra(ActivityCapture.kPhotoPath);
				PhotoPreviewActivity.open(this, path, photoTag.ordinal());
			}
		}else if (requestCode == GetPhotoUtil.PHOTO_REQUEST_GALLERY &&  resultCode == RESULT_OK) {
			if (UploadUtil.hasSdcard()) {
				Uri uri = data.getData();
				GetPhotoUtil.crop(AddTruckNewActivity.this, uri, CommonRes.tempFile);
			}
		}else if (requestCode == GetPhotoUtil.PHOTO_REQUEST_CUT && resultCode == RESULT_OK){
			String path = ImageUtil.getImageAbsolutePath(AddTruckNewActivity.this, Uri.fromFile(CommonRes.tempFile));
			uploadImage(photoTag, path);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void showPage(PageType type){
		pageType = type;
		switch (pageType) {
		case FirstStep:
			firstStepLayout.setVisibility(View.VISIBLE);
			nextStep.setVisibility(View.VISIBLE);
			
			nextStepLayout.setVisibility(View.GONE);
			passedText.setVisibility(View.GONE);
			submitLayout.setVisibility(View.GONE);
			
			break;
		case NextStep:
			firstStepLayout.setVisibility(View.GONE);
			nextStep.setVisibility(View.GONE);
			
			nextStepLayout.setVisibility(View.VISIBLE);
			passedText.setVisibility(View.GONE);
			submitLayout.setVisibility(View.VISIBLE);
			break;
		case Whole:
			firstStepLayout.setVisibility(View.VISIBLE);
			nextStep.setVisibility(View.GONE);
			
			nextStepLayout.setVisibility(View.VISIBLE);
			passedText.setVisibility(View.VISIBLE);
			submitLayout.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}
	
	//根据页面所选车辆类型的不同，显示不同的照片项目
	private void showPhotoItems(){
		switch (truckTypeValue) {
		case 1:
			//全挂车
			truckHeadPhoto.setItemName("车辆照片");
			truckHeadPhoto.setVisibility(View.VISIBLE);
			truckTailPhoto.setVisibility(View.GONE);
			truckHeadLicencePhoto.setItemName("行驶证照片");
			truckHeadLicencePhoto.setVisibility(View.VISIBLE);
			truckTailLicencePhoto.setVisibility(View.GONE);
			//原文案:营运证正面
			transiLicenceFrontPhoto.setItemName("保险单");
			transiLicenceFrontPhoto.setVisibility(View.VISIBLE);
			//原文案:营运证反面
			transiLicenceBackPhoto.setItemName("挂靠协议");
			transiLicenceBackPhoto.setVisibility(View.VISIBLE);
			break;
		case 3:
			//农用车
			truckHeadPhoto.setItemName("车辆照片");
			truckHeadPhoto.setVisibility(View.VISIBLE);
			truckTailPhoto.setVisibility(View.GONE);
			truckHeadLicencePhoto.setItemName("行驶证照片");
			truckHeadLicencePhoto.setVisibility(View.VISIBLE);
			truckTailLicencePhoto.setVisibility(View.GONE);
			transiLicenceFrontPhoto.setVisibility(View.GONE);
			transiLicenceBackPhoto.setVisibility(View.GONE);
			break;
		default://半挂车和其他情况显示全部
			truckHeadPhoto.setItemName("车头照片");
			truckHeadPhoto.setVisibility(View.VISIBLE);
			truckTailPhoto.setItemName("车尾照片");
			truckTailPhoto.setVisibility(View.VISIBLE);
			truckHeadLicencePhoto.setItemName("车头行驶证");
			truckHeadLicencePhoto.setVisibility(View.VISIBLE);
			truckTailLicencePhoto.setItemName("车尾行驶证");
			truckTailLicencePhoto.setVisibility(View.VISIBLE);
			transiLicenceFrontPhoto.setItemName("保险单");
			transiLicenceFrontPhoto.setVisibility(View.VISIBLE);
			transiLicenceBackPhoto.setItemName("挂靠协议");
			transiLicenceBackPhoto.setVisibility(View.VISIBLE);
			break;
		}
	}
	
	/**
	 * 点击“下一步”，提交车辆基本信息
	 */
	private void submitBasicMessage(){
		AjaxParams params = new AjaxParams();
		//车辆类型，后台的车型的值的区间为[2, 4],1为“不限”
		params.put("truckType", String.valueOf(truckTypeValue + 1));
		if(truckTypeValue != 3){
			//不是农用车要填车厢类型
			//车厢类型，后台的车型的值的区间为[2, 7],1为“不限”
			params.put("truckCarriage", String.valueOf(truckCarriageValue + 1));
		}else{//是农用车，车厢类型填空
			params.put("truckCarriage", "1");
		}
		//是否自卸车
		params.put("isDump", String.valueOf(isDumpTruckToggle.isChecked()));
		
		String truckPlateNumber;//车牌号拼接
		if(truckTypeValue == 3){
			truckPlateNumber = plateNumberHeadText.getText() + nongyongHeadEditText.getText().toString() +
					truckNumberEditText.getText().toString();
			//车牌
			params.put("truckNumber", Util.DeleteSpace(truckPlateNumber));
		}else{
			truckPlateNumber = plateNumberHeadText.getText() + truckNumberEditText.getText().toString();
			//车牌
			params.put("truckNumber", Util.DeleteSpace(truckPlateNumber));
		}
		//最大载重量
		params.put("carryCapacity", maxLoadEditText.getText().toString());
		
		if(!maxSpaceEditText.getText().toString().equals("")){
			//载货空间
			params.put("carryVolume", maxSpaceEditText.getText().toString());
		}
		if(truckId != null){
			params.put("truckId",truckId);
		}
		final PromptManager mPromptManager = new PromptManager();
		mPromptManager.showProgressDialog1(AddTruckNewActivity.this, "请稍等");
		DidiApp.getHttpManager().sessionPost(AddTruckNewActivity.this, basicMessageUrl, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				try {
					mPromptManager.closeProgressDialog();
					//通知"车辆信息"页面刷新
					Intent intent = new Intent();
					intent.setAction(CommonRes.RefreshTruck);
					sendBroadcast(intent);
					intent = new Intent();
					//添加车辆后用户的角色可能改变，所以要刷新用户信息
					intent.setAction(CommonRes.RefreshUserInfo);
					sendBroadcast(intent);
					
					JSONObject jsonObject = new JSONObject(t);
					String all = jsonObject.getString("body");
					truckModel = JSON.parseObject(all,TruckAuthModel.class);
					if(isAddNewTruck){
						Toast.makeText(getApplicationContext(),"资料提交成功",Toast.LENGTH_LONG).show();
						finish();
						return;
					}
					fillIn();
					showPage(PageType.NextStep);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				mPromptManager.closeProgressDialog();
				Toast.makeText(AddTruckNewActivity.this,errMsg,Toast.LENGTH_LONG).show();
			}
		});
	}
	
	/**
	 * 上传单张照片
	 * @param photoTag
	 * @param imagePath
	 */
	private void uploadImage(final CommonRes.PhotoTag photoTag, final String imagePath){
		AjaxParams params = new AjaxParams();
		params.put("truckId",truckId);
		InputStream imageIns = ImageUtil.getimage(imagePath);
		switch (photoTag) {
		case truckHeadPhoto:
			params.put("frontTruckPhoto", imageIns, "img.png");
			break;
		case truckTailPhoto:
			params.put("backTruckPhoto", imageIns, "img.png");
			break;
		case truckHeadLicencePhoto:
			params.put("frontTruckLicensePhoto", imageIns, "img.png");
			break;
		case truckTailLicencePhoto:
			params.put("backTruckLicensePhoto", imageIns, "img.png");
			break;
		case transiLicenceFrontPhoto:
			params.put("roadTransportPhoto", imageIns, "img.png");
			break;
		case transiLicenceBackPhoto:
			params.put("backRoadTransportPhoto", imageIns, "img.png");
			break;
		default:
			break;
		}
		final PromptManager mPromptManager = new PromptManager();
		mPromptManager.showProgressDialog1(AddTruckNewActivity.this, "上传中");
		DidiApp.getHttpManager().sessionPost(AddTruckNewActivity.this, uploadPhotoUrl, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				try {
					mPromptManager.closeProgressDialog();
					Bitmap loadBitmap = BitmapUtil.loadBitmap(imagePath);
					switch (photoTag) {
					case truckHeadPhoto:
						truckHeadPhoto.getLittlePhoto().setImageBitmap(loadBitmap);
						truckHeadPhoto.setHint("重新上传");
						hasTruckHead = true;
						break;
					case truckTailPhoto:
						truckTailPhoto.getLittlePhoto().setImageBitmap(loadBitmap);
						truckTailPhoto.setHint("重新上传");
						hasTruckTail = true;
						break;
					case truckHeadLicencePhoto:
						truckHeadLicencePhoto.getLittlePhoto().setImageBitmap(loadBitmap);
						truckHeadLicencePhoto.setHint("重新上传");
						hasTruckHeadLicence = true;
						break;
					case truckTailLicencePhoto:
						truckTailLicencePhoto.getLittlePhoto().setImageBitmap(loadBitmap);
						truckTailLicencePhoto.setHint("重新上传");
						hasTruckTailLicence = true;
						break;
					case transiLicenceFrontPhoto:
						transiLicenceFrontPhoto.getLittlePhoto().setImageBitmap(loadBitmap);
						transiLicenceFrontPhoto.setHint("重新上传");
						hasTransiLicenceFront = true;
						break;
					case transiLicenceBackPhoto:
						transiLicenceBackPhoto.getLittlePhoto().setImageBitmap(loadBitmap);
						transiLicenceBackPhoto.setHint("重新上传");
						hasTransiLicenceBack = true;
					default:
						break;
					}
				} catch (Exception e) {
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
	 * 根据车辆ID获取车辆信息
	 */
	private void loadTruckInfo(){
		AjaxParams params = new AjaxParams();
        params.put("truckId",truckId);
        final PromptManager mPromptManager = new PromptManager();
		mPromptManager.showProgressDialog1(AddTruckNewActivity.this, "加载中");
        DidiApp.getHttpManager().sessionPost(AddTruckNewActivity.this, getTruckInfoUrl, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				mPromptManager.closeProgressDialog();
				try {
					JSONObject jsonObject = new JSONObject(t);
					String all = jsonObject.getString("body");
					truckModel = JSON.parseObject(all,TruckAuthModel.class);
					fillIn();
					String typeStr = getIntent().getStringExtra("pageType");
					if("from_complete_material".equals(typeStr)){
						showPage(PageType.NextStep);
						lastStep.setVisibility(View.GONE);
					}else {
						lastStep.setVisibility(View.VISIBLE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				mPromptManager.closeProgressDialog();
				ToastUtil.show(AddTruckNewActivity.this, "车辆信息加载失败:" + errMsg);
				finish();
			}
		});
	}
	
	/**
	 * 根据车辆ID获取车辆信息后，对信息做页面显示
	 */
	private void fillIn(){
		if(truckModel == null){
			return;
		}
		truckId = truckModel.getTruckId();
		truckTypeValue = truckModel.getTruckType();
		truckCarriageValue = truckModel.getTruckCarriage();
		
		if(truckModel.getStatus() == 2 && truckModel.getAuditNoteTypeText() != null){
			hintText.setVisibility(View.VISIBLE);
			hintText.setText(truckModel.getAuditNoteTypeText());
		}else{
			hintText.setVisibility(View.GONE);
		}
		
		isDumpTruckToggle.setChecked(truckModel.isDump());
		
		if(truckModel.getTruckNumberProvince() != null){
			//车牌号头部
			plateNumberHeadText.setText(truckModel.getTruckNumberProvince());
		}
		if(truckTypeValue == 3){
			//如果是农用车
			truckCarriageLayout.setVisibility(View.GONE);
			nongyongHeadEditText.setVisibility(View.VISIBLE);
			nongyongLittleLine.setVisibility(View.VISIBLE);
			if(truckModel.getTruckNumberLetter() != null){
				nongyongHeadEditText.setText(truckModel.getTruckNumberMidd());
				truckNumberEditText.setText(truckModel.getTruckNumberLetter());
			}
		}else{
			truckCarriageLayout.setVisibility(View.VISIBLE);
			nongyongHeadEditText.setVisibility(View.GONE);
			nongyongLittleLine.setVisibility(View.GONE);
			if(truckModel.getTruckNumberLetter() != null){
				//车牌号号码
				truckNumberEditText.setText(truckModel.getTruckNumberLetter());
			}
		}
		maxLoadEditText.setText(String.valueOf(truckModel.getCarryCapacity()));
		maxSpaceEditText.setText(String.valueOf(truckModel.getCarryVolume()));


		if(truckModel.getStatus() == 1){
			passedText.setText("审核中");
		}
		
		//审核中，或审核已通过，则不可编辑
		if(truckModel.getStatus() == 1){
			editable = false;
		}else{
			editable = true;
		}
		FinalBitmap mitmap = FinalBitmap.create(AddTruckNewActivity.this);
		
		if(truckModel.getFrontTruckPhoto() != null){
			//车头照片
			hasTruckHead = true;
			if(!editable){
				mitmap.display(truckHeadPhoto.getLargePhoto(), truckModel.getFrontTruckPhoto());
				truckHeadPhoto.hideHint();
			}else{
				truckHeadPhoto.setHint("重新上传");
				mitmap.display(truckHeadPhoto.getLittlePhoto(), truckModel.getFrontTruckPhoto());
			}
		}
		if(truckModel.getBackTruckPhoto() != null){
			//车尾照片
			hasTruckTail = true;
			if(!editable){
				mitmap.display(truckTailPhoto.getLargePhoto(), truckModel.getBackTruckPhoto());
				truckTailPhoto.hideHint();
			}else{
				truckTailPhoto.setHint("重新上传");
				mitmap.display(truckTailPhoto.getLittlePhoto(), truckModel.getBackTruckPhoto());
			}
		}
		if(truckModel.getFrontTruckLicensePhoto() != null){
			//车头行驶证
			hasTruckHeadLicence = true;
			if(!editable){
				mitmap.display(truckHeadLicencePhoto.getLargePhoto(), truckModel.getFrontTruckLicensePhoto());
				truckHeadLicencePhoto.hideHint();
			}else{
				truckHeadLicencePhoto.setHint("重新上传");
				mitmap.display(truckHeadLicencePhoto.getLittlePhoto(), truckModel.getFrontTruckLicensePhoto());
			}
		}
		if(truckModel.getBackTruckLicensePhoto() != null){
			//车尾行驶证
			hasTruckTailLicence = true;
			if(!editable){
				mitmap.display(truckTailLicencePhoto.getLargePhoto(), truckModel.getBackTruckLicensePhoto());
				truckTailLicencePhoto.hideHint();
			}else{
				truckTailLicencePhoto.setHint("重新上传");
				mitmap.display(truckTailLicencePhoto.getLittlePhoto(), truckModel.getBackTruckLicensePhoto());
			}
		}
		if(truckModel.getRoadTransportPhoto() != null){
			//保险单，原来是营运证正面
			hasTransiLicenceFront = true;
			if(!editable){
				mitmap.display(transiLicenceFrontPhoto.getLargePhoto(), truckModel.getRoadTransportPhoto());
				transiLicenceFrontPhoto.hideHint();
			}else{
				transiLicenceFrontPhoto.setHint("重新上传");
				mitmap.display(transiLicenceFrontPhoto.getLittlePhoto(), truckModel.getRoadTransportPhoto());
			}
		}
		if(truckModel.getBackRoadTransportPhoto() != null){
			//挂靠协议，原来是营运证反面
			hasTransiLicenceBack = true;
			if(!editable){
				mitmap.display(transiLicenceBackPhoto.getLargePhoto(), truckModel.getBackRoadTransportPhoto());
				transiLicenceBackPhoto.hideHint();
			}else{
				transiLicenceBackPhoto.setHint("重新上传");
				mitmap.display(transiLicenceBackPhoto.getLittlePhoto(), truckModel.getBackRoadTransportPhoto());
			}
		}
		
		//如果已通过审核，车型、车长都只显示一种
		if(editable){
			truckTypeValue = truckModel.getTruckType();
			gridViewSetSelect(truckTypeList, typeAdapter, truckTypeValue - 1);
			

			truckCarriageValue = truckModel.getTruckCarriage();
			gridViewSetSelect(truckCarriageList, carriageAdapter, truckCarriageValue - 1);
		}else{
			truckTypeGridView.setVisibility(View.GONE);
			singleTruckTypeLayout.setVisibility(View.VISIBLE);
			switch (truckTypeValue) {
			case 1:
				singleTruckTypeImage.setImageResource(R.drawable.quanguache);
				singleTypeNameText.setText("全挂");
				break;
			case 2:
				singleTruckTypeImage.setImageResource(R.drawable.banguache);
				singleTypeNameText.setText("半挂");
				break;
			case 3:
				singleTruckTypeImage.setImageResource(R.drawable.nongyongche);
				singleTypeNameText.setText("农用车");
				break;
			default:
				break;
			}

			
			truckCarriageGridView.setVisibility(View.GONE);
			singleTruckCarrigeLayout.setVisibility(View.VISIBLE);
			switch (truckCarriageValue) {
			case 1:
				singleTruckCarrigeImage.setImageResource(R.drawable.xiangshiche);
				singleCarrigeNameText.setText("厢式");
				break;
			case 2:
				singleTruckCarrigeImage.setImageResource(R.drawable.dilanche);
				singleCarrigeNameText.setText("低栏");
				break;
			case 3:
				singleTruckCarrigeImage.setImageResource(R.drawable.gaolanche);
				singleCarrigeNameText.setText("高栏");
				break;
			case 4:
				singleTruckCarrigeImage.setImageResource(R.drawable.pingbanche);
				singleCarrigeNameText.setText("平板");
				break;
			case 5:
				singleTruckCarrigeImage.setImageResource(R.drawable.cangzhache);
				singleCarrigeNameText.setText("仓栅式");
				break;
			case 6:
				singleTruckCarrigeImage.setImageResource(R.drawable.guanche);
				singleCarrigeNameText.setText("罐车式");
				break;
			default:
				break;
			}
		}
		
		if(truckModel.getCarryVolume() <= 0 && !editable){
			RelativeLayout spaceLayout = (RelativeLayout) findViewById(R.id.truck_space_layout);
			spaceLayout.setVisibility(View.GONE);
		}
		
		showPhotoItems();//根据之前选定的车辆类型，决定显示哪几个照片条目
		setonClick();
		
		if(!editable){
			showPage(PageType.Whole);
		}else{
			String typeStr = getIntent().getStringExtra("pageType");
			if(truckModel.getStatus() == 3 && !"from_complete_material".equals(typeStr)){
				showPage(PageType.Whole);
			}else {
				showPage(PageType.FirstStep);
			}
		}
	}
	
	private void submitToAudit(){
		AjaxParams params = new AjaxParams();
        params.put("truckId",truckId);
        final PromptManager mPromptManager = new PromptManager();
		mPromptManager.showProgressDialog1(AddTruckNewActivity.this, "正在提交审核");
        DidiApp.getHttpManager().sessionPost(AddTruckNewActivity.this, submitToAuditUrl, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				mPromptManager.closeProgressDialog();
				//通知"车辆信息"页面刷新
				Intent intent = new Intent();
				intent.setAction(CommonRes.RefreshTruck);
				sendBroadcast(intent);
				
				Intent authResult_intent = new Intent(AddTruckNewActivity.this, AuthResultActivity.class);
				authResult_intent.putExtra("from", "truck");
				startActivity(authResult_intent);
				finish();
			}
			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				mPromptManager.closeProgressDialog();
			}
		});
	}
	
	/**
	 * 提交基本信息之前，先做输入限制
	 * @return
	 */
	private boolean inputJudge(){
		switch (pageType) {
		case FirstStep:
			if(truckTypeValue < 0){
				ToastUtil.show(AddTruckNewActivity.this, "请选择车辆类型");
				return false;
			}
			if(truckTypeValue != 3 && truckCarriageValue < 0){
				ToastUtil.show(AddTruckNewActivity.this, "请选择车厢类型");
				return false;
			}
			String plateNum, nongyonghead;
			if(truckTypeValue != 3){
				//不是农用车
				plateNum = truckNumberEditText.getText().toString();
				if(plateNum == null || plateNum.length() != 5){
					ToastUtil.show(AddTruckNewActivity.this, "请正确填写车牌号");
					return false;
				}
			}else{//是农用车
				nongyonghead = nongyongHeadEditText.getText().toString();
				plateNum = truckNumberEditText.getText().toString();
				if(nongyonghead == null || nongyonghead.length() != 2 || plateNum == null || plateNum.length() != 5){
					ToastUtil.show(AddTruckNewActivity.this, "请正确填写农用车车牌号");
					return false;
				}
			}
			String maxLoad = maxLoadEditText.getText().toString(), maxSpace = maxSpaceEditText.getText().toString();
			if(maxLoad.equals("")){
				ToastUtil.show(AddTruckNewActivity.this, "请填写最大运量");
				return false;
			}
			if(Util.inputToDoubleStr(maxLoad).equals("")){
				ToastUtil.show(AddTruckNewActivity.this, "最大运量格式不正确");
				return false;
			}
			if(!maxSpace.equals("") && Util.inputToDoubleStr(maxSpace).equals("")){
				ToastUtil.show(AddTruckNewActivity.this, "载货空间格式不正确");
				return false;
			}
			return true;
		case NextStep:
			//检查照片是否齐全
			switch (truckTypeValue) {
			case 1:
				//全挂车
				if(hasTruckHead && hasTruckHeadLicence && hasTransiLicenceFront && hasTransiLicenceBack){
					return true;
				}else{
					ToastUtil.show(AddTruckNewActivity.this, "照片未齐全");
					return false;
				}
			case 3:
				//农用车
				if(hasTruckHead && hasTruckHeadLicence){
					return true;
				}else{
					ToastUtil.show(AddTruckNewActivity.this, "照片未齐全");
					return false;
				}
			default://半挂车和其他情况
				if(hasTruckHead && hasTruckTail && hasTruckHeadLicence && hasTruckTailLicence &&  
						hasTransiLicenceFront && hasTransiLicenceBack){
					return true;
				}else{
					ToastUtil.show(AddTruckNewActivity.this, "照片未齐全");
					return false;
				}
			}
		default:
			return true;
		}
	}
	
	/**
	 * 监听返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            switch (pageType) {
			case FirstStep:
				finish();
				break;
			case NextStep:
//				showPage(PageType.FirstStep);
				finish();
				break;
			default:
				finish();
				break;
			}
             return true;
         }
         return super.onKeyDown(keyCode, event);
     }
	
	/**
	 * 页面显示的三种状态：第一步、第二步、完整显示
	 * @author abc
	 */
	public enum PageType{
		FirstStep, NextStep, Whole;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mRefreshBroadcastReceiver);
	}
}


