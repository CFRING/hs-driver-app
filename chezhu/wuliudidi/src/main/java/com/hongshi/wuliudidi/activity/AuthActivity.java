package com.hongshi.wuliudidi.activity;

import java.io.File;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.camera.ActivityCapture;
import com.hongshi.wuliudidi.camera.BitmapUtil;
import com.hongshi.wuliudidi.camera.PhotoPreviewActivity;
import com.hongshi.wuliudidi.dialog.AddTruckPop;
import com.hongshi.wuliudidi.dialog.AuthHintDialog;
import com.hongshi.wuliudidi.dialog.DateDialog;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.CallBackString;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.SetDateCallBack;
import com.hongshi.wuliudidi.model.AuthModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.photo.GetPhotoUtil;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.ImageUtil;
import com.hongshi.wuliudidi.utils.LogUtil;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.dialog.DataFillingDialog;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.hongshi.wuliudidi.view.PhotoItemView;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author huiyuan
 */
public class AuthActivity extends Activity implements OnClickListener{
	private DiDiTitleView mAuthTitle;
	private PhotoItemView mOneItem,mTwoItem,mThreeItem,mFourItem;
	private ImageView mOneImage,mTwoImage,mThreeImage,mFourImage;
	private TextView mNameText,mSexText;
	private EditText mNameEdit,mIdEdit,mCompanyEdit;
	private RelativeLayout mSexLayout,mCompanyLayout;
	private TextView mSubmit;
	private AuthHintDialog mAuthHintDialog;
	/**头像名称 */
	private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
	// 拍照
	private static final int PHOTO_REQUEST_CAMERA = 1;
	// 从相册中选择
	private static final int PHOTO_REQUEST_GALLERY = 2;
	private String mImageName = "";
	private DataFillingDialog mImageDialog;
	private DataFillingDialog mSexDialog;
	private boolean isShowAuthHintDialog = true;
	//个人认证接口
	private String personal_url = GloableParams.HOST + "uic/authentication/uploadIdentityPhoto.do?";
	//企业认证接口
	private String enterprise_url = GloableParams.HOST + "uic/authentication/uploadEnterprisePhoto.do?";
	//获取实名认证的信息
	private String get_id_url = GloableParams.HOST + "uic/authentication/getIdentity.do?";
	//获取企业认证的信息
	private String get_enterprise_url = GloableParams.HOST + "uic/authentication/getEnterprise.do?";
	//个人信息提交
	private String submit_person_url = GloableParams.HOST + "uic/authentication/createOrUpdateIdentity.do?";
	//企业信息提交
	private String submit_enterprise_url = GloableParams.HOST + "uic/authentication/createOrUpdateEnterprise.do?";

	//驾照认证
	private String submit_license_url = GloableParams.HOST + "uic/authentication/createOrUpdateDriving.do?";
	//驾照认证信息
	private String license_url = GloableParams.HOST + "uic/authentication/getDrivingLicence.do?";
	//上传驾照图片
	private String upload_license_url = GloableParams.HOST + "uic/authentication/uploadDrivingPhoto.do?";
	//0,个人认证，1，企业认证
	private int auth_tag = -1;
	private boolean hasSex = false;
	private String name;
	//驾照类型
	private TextView truck_content;
	//驾照有效期
	private TextView license_date;
	private TextView reject_reason;
	private RelativeLayout rejectReasonLayout;
	private String dateStr = "";
	private String drivingType;
	private RelativeLayout license_layout;
	private int status = -1;

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("AuthActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("AuthActivity");
	}

//	File tempFile = new File("/sdcard/wuliudidi/"+"wuliudidi_photo"+".jpg");
	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(CommonRes.ACTIONPHOTOPATH)) {
				String imagePath = intent.getStringExtra("imagePath");
				if(imagePath == null || "".equals(imagePath)){
					return;
				}
				InputStream imageIns = ImageUtil.getimage(imagePath);
				try {
					int tag = intent.getExtras().getInt("tag", -1);
					switch (tag) {
					case CommonRes.TYPE_FAREN_SHENFENZHENG_ZHENGMIAN:
					case CommonRes.TYPE_FAREN_SHENFENZHENG_FANMIAN:
					case CommonRes.TYPE_FAREN_YINGYEZHIZHAO:
					case CommonRes.TYPE_FAREN_XUKEZHENG:
						uploadFile(enterprise_url, mImageName, imageIns);
//						Bitmap bitmap = BitmapUtil.loadBitmap(imagePath);
//						mOneImage.setImageBitmap(bitmap);
						break;
					case CommonRes.TYPE_SHIMING_ZHENGMIAN:
					case CommonRes.TYPE_SHIMING_FANMIAN:
						uploadFile(personal_url, mImageName, imageIns);
						break;
					case CommonRes.TYPE_JIASHIZHENG:
						uploadFile(upload_license_url, mImageName, imageIns);
						break;
					default:
						break;
					}
//					photoTag = CommonRes.PhotoTag.values()[tag];
				} catch (Exception e) {
//					photoTag = CommonRes.PhotoTag.truckHeadPhoto;
				}
//				uploadImage(photoTag, imagePath);
			}
		}
	};
	@SuppressLint("HandlerLeak") private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CommonRes.CAMERA:
//				camera();
				GetPhotoUtil.callCamera(AuthActivity.this);
				break;
			case CommonRes.GALLERY:
//				photo();
				GetPhotoUtil.callGallery(AuthActivity.this);
				break;
			case CommonRes.MAN:
				mSexText.setText("男");
				mSexText.setTextColor(getResources().getColor(R.color.black));
				hasSex = true;
				break;
			case CommonRes.WOMAN:
				mSexText.setText("女");
				mSexText.setTextColor(getResources().getColor(R.color.black));
				hasSex = true;
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
		setContentView(R.layout.auth_activity);

		initViews();
		init(name);
	}

	private void initViews(){
		mAuthTitle = (DiDiTitleView) findViewById(R.id.auth_title);

		reject_reason = (TextView) findViewById(R.id.reject_reason_text);
		rejectReasonLayout = (RelativeLayout) findViewById(R.id.reject_reason_layout);

		mSexLayout = (RelativeLayout) findViewById(R.id.sex_layout);
		mCompanyLayout = (RelativeLayout) findViewById(R.id.company_layout);
		mSexText = (TextView) findViewById(R.id.sex);

		mNameEdit = (EditText) findViewById(R.id.name_edit);
		mIdEdit = (EditText) findViewById(R.id.id_edit);
		mNameText = (TextView) findViewById(R.id.name_text);

		mOneItem = (PhotoItemView) findViewById(R.id.one_item);
		mTwoItem = (PhotoItemView) findViewById(R.id.two_item);
		mThreeItem = (PhotoItemView) findViewById(R.id.three_item);
		mFourItem = (PhotoItemView) findViewById(R.id.four_item);
		mAuthTitle.setBack(this);
		mSubmit = (TextView) findViewById(R.id.submit);
		mCompanyEdit = (EditText) findViewById(R.id.company_edit);

		mAuthHintDialog = new AuthHintDialog(AuthActivity.this, R.style.hint_dialog);
		mAuthHintDialog.setCanceledOnTouchOutside(true);

		//0 表示拍照或者图库选取
		mImageDialog = new DataFillingDialog(AuthActivity.this, R.style.data_filling_dialog, mHandler, 0);
		mImageDialog.setCanceledOnTouchOutside(true);
		mImageDialog.setText("拍照", "图库选取");
		//1 表示选择男女
		mSexDialog = new DataFillingDialog(AuthActivity.this, R.style.data_filling_dialog, mHandler, 1,-1);
		mSexDialog.setCanceledOnTouchOutside(true);
		mSexDialog.setText("男", "女");

		mSubmit.setOnClickListener(this);

		name = getIntent().getExtras().getString("name");
		//注册照片选取的广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CommonRes.ACTIONPHOTOPATH);
		registerReceiver(mRefreshBroadcastReceiver, intentFilter);
	}

	private void setAnimation(Dialog dialog){
		Window window = dialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.dialog);
		window.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = window.getAttributes();
		        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
		        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		        window.setAttributes(lp);
		//此处可以设置dialog显示的位置
		        window.setGravity(Gravity.BOTTOM);
	}
	public void init(String name){
		if(name.equals("name")){
			auth_tag = 0;
			mAuthTitle.setTitle("实名认证");
			mNameText.setText("姓名");
			mNameEdit.setHint("请填写您的真实姓名");
			mIdEdit.setHint("请填写您的身份证号码");
			
			mOneItem.setName("身份证正面");
			mTwoItem.setName("身份证反面");
			mThreeItem.setName("驾驶证");
			mFourItem.setName("道路运输证");
			mFourItem.setVisibility(View.GONE);
			mThreeItem.setVisibility(View.GONE);
			
			mSexLayout.setVisibility(View.VISIBLE);
			mSexLayout.setOnClickListener(this);
			mCompanyLayout.setVisibility(View.GONE);
			mOneImage = mOneItem.getImage();
			mOneImage.setImageResource(R.drawable.identity_card_front);
			mOneImage.setId(CommonRes.frontCardPhoto_id);
			mOneImage.setOnClickListener(this);
			
			mTwoImage = mTwoItem.getImage();
			mTwoImage.setImageResource(R.drawable.identity_card_back);
			mTwoImage.setId(CommonRes.backCardPhoto_id);
			mTwoImage.setOnClickListener(this);
			
			mThreeImage = mThreeItem.getImage();
			mThreeImage.setImageResource(R.drawable.driving_license);
			mThreeImage.setId(CommonRes.drivingPhoto_id);
			mThreeImage.setOnClickListener(this);
			
			mFourImage = mFourItem.getImage();
			mFourImage.setImageResource(R.drawable.transport);
			mFourImage.setVisibility(View.GONE);
			
			mAuthHintDialog.setHint(R.string.auth_individual_hint);
			loadData(get_id_url);
		}else if(name.equals("enterprise")){
			auth_tag = 1;
			mAuthTitle.setTitle("企业认证");
			mOneItem.setName("法人身份证正面");
			mOneImage = mOneItem.getImage();
			mOneImage.setImageResource(R.drawable.identity_card_front);
			mOneImage.setId(CommonRes.c_frontCardPhoto_id);
			mOneImage.setOnClickListener(this);
			
			mTwoItem.setName("法人身份证反面");
			mTwoImage = mTwoItem.getImage();
			mTwoImage.setImageResource(R.drawable.identity_card_back);
			mTwoImage.setId(CommonRes.c_backCardPhoto_id);
			mTwoImage.setOnClickListener(this);
			
			mThreeItem.setName("营业执照");
			mThreeImage = mThreeItem.getImage();
			mThreeImage.setImageResource(R.drawable.license);
			mThreeImage.setId(CommonRes.c_businessPhoto_id);
			mThreeImage.setOnClickListener(this);
			
			mFourItem.setName("道路运输经营许可证");
			mFourImage = mFourItem.getImage();
			mFourImage.setImageResource(R.drawable.business_license);
			mFourImage.setId(CommonRes.c_transportationPhoto_id);
			mFourImage.setOnClickListener(this);
			
			mAuthHintDialog.setHint(R.string.auth_company_hint);
			loadData(get_enterprise_url);
		}else if(name.equals("license")){
			ImageView past_time_image = (ImageView) findViewById(R.id.past_time_image);
			//驾照有效期
			TextView license_text = (TextView) findViewById(R.id.id_text);
			//车型
			TextView truck_text = (TextView) findViewById(R.id.sex_text);
			truck_content = (TextView) findViewById(R.id.sex);
			license_date = (TextView) findViewById(R.id.license_date);
			truck_text.setText("准驾车型");
			truck_content.setText("选择准驾车型");
			drivingType = "0";
			license_layout = (RelativeLayout) findViewById(R.id.id_layout);
			auth_tag = 2;
			mAuthTitle.setTitle("驾照认证");
			mNameText.setText("姓名");
			mNameEdit.setHint("未认证");
			mNameEdit.setEnabled(false);
			RelativeLayout nameLayout = (RelativeLayout) findViewById(R.id.name_layout);
			//驾照认证暂时隐藏姓名栏
			nameLayout.setVisibility(View.GONE);
			past_time_image.setVisibility(View.VISIBLE);
			mIdEdit.setVisibility(View.GONE);
			license_date.setVisibility(View.VISIBLE);
			license_date.setHint("选择证件有效日期");
			license_text.setText("驾驶证");
			//判断驾照是否已经认证
			if(CommonRes.hasDrivingLicenceAuth){
				mSubmit.setBackgroundResource(R.color.gray);
			}else{
				mSubmit.setBackgroundResource(R.color.theme_color);
			}
			
			mThreeItem.setName("驾驶证");
			mFourItem.setVisibility(View.GONE);
			mOneItem.setVisibility(View.GONE);
			mTwoItem.setVisibility(View.GONE);
			
			mSexLayout.setVisibility(View.VISIBLE);
			mSexLayout.setOnClickListener(this);
			mCompanyLayout.setVisibility(View.GONE);
			
			mThreeImage = mThreeItem.getImage();
			mThreeImage.setImageResource(R.drawable.driving_license);
			mThreeImage.setId(CommonRes.drivingPhoto_id);
			mThreeImage.setOnClickListener(this);
			
			mAuthHintDialog.setHint(R.string.auth_individual_hint);
			
			license_layout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// 驾照认证页面的选择过期时间
					if(status == 3){
						return;
					}else{
						DateDialog mDateDialog = new DateDialog(AuthActivity.this, R.style.data_filling_dialog,new SetDateCallBack() {
							@Override
							public void date(long date) {
								dateStr = ""+date;
								license_date.setText(Util.getFormateDateTimeShort(date));	
							}
						}, DateDialog.YearMonthDay,"选择驾驶证有效期");
						UploadUtil.setAnimation(mDateDialog, CommonRes.TYPE_BOTTOM, true);
						mDateDialog.show();
					}
				}
			});
			loadData(license_url);
		}
	}
	private void submit(String url,AjaxParams params){
		DidiApp.getHttpManager().sessionPost(AuthActivity.this, url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				Intent broadcastIntent = new Intent();
				broadcastIntent.setAction(CommonRes.RefreshUserInfo);
				sendBroadcast(broadcastIntent);
				
				Intent intent = new Intent(AuthActivity.this,AuthResultActivity.class);
				startActivity(intent);
				
				finish();
			}
		});
	}
	//获取信息
	private void loadData(String url){
		AjaxParams params = new AjaxParams();
		final PromptManager mPromptManager = new PromptManager();
		mPromptManager.showProgressDialog1(AuthActivity.this, "正在加载");
		DidiApp.getHttpManager().sessionPost(AuthActivity.this, url, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				mPromptManager.closeProgressDialog();
				try {
					JSONObject jsonObject = new JSONObject(t);
					String all = jsonObject.getString("body");
					AuthModel mAuthModel = JSON.parseObject(all,AuthModel.class);
					FinalBitmap mitmap = FinalBitmap.create(AuthActivity.this);
					//获取审核状态 3为审核通过
					status = mAuthModel.getStatus();
					if(status == 2){
						//审核不通过
						rejectReasonLayout.setVisibility(View.VISIBLE);
						if(mAuthModel.getAuditNoteTypeText() != null){
							reject_reason.setText(mAuthModel.getAuditNoteTypeText());
						}
					}else{
						rejectReasonLayout.setVisibility(View.GONE);
					}
					
					if(status == 3){
						//审核通过
						mSubmit.setBackgroundResource(R.color.gray);
						mSubmit.setClickable(false);
						if(mNameEdit != null){
							mNameEdit.setEnabled(false);
						}
						if(mIdEdit != null){
							mIdEdit.setEnabled(false);
						}
						if(mCompanyEdit != null){
							mCompanyEdit.setEnabled(false);
						}
						
						isShowAuthHintDialog = false;
					}else{
						if(isShowAuthHintDialog){
							mAuthHintDialog.show();
							isShowAuthHintDialog = false;
						}
					}
					if(name.equals("name")){
						mitmap.display(mOneImage, mAuthModel.getFrontCardPhoto());
						mitmap.display(mTwoImage, mAuthModel.getBackCardPhoto());
						mitmap.display(mThreeImage, mAuthModel.getDrivingPhoto());
						if(mAuthModel.getName()!= null&&!mAuthModel.getName().equals("")){
							mNameEdit.setText(mAuthModel.getName());
						}
						if(mAuthModel.getIdentityCode()!= null&&!mAuthModel.getIdentityCode().equals("")){
							mIdEdit.setText(mAuthModel.getIdentityCode());
						}
						if(mAuthModel.getSex()!= null && !mAuthModel.getSex().toString().equals("")){
							mSexText.setText(mAuthModel.getSex());
							mSexText.setTextColor(getResources().getColor(R.color.black));
							hasSex = true;
						}
					}else if(name.equals("enterprise")){
						mitmap.display(mOneImage, mAuthModel.getFrontCardPhoto());
						mitmap.display(mTwoImage, mAuthModel.getBackCardPhoto());
						mitmap.display(mThreeImage, mAuthModel.getBusinessPhoto());
						mitmap.display(mFourImage, mAuthModel.getTransportationPhoto());
						if(mAuthModel.getName() != null && !mAuthModel.getName().equals("")){
							//企业名字
							mCompanyEdit.setText(mAuthModel.getName());
						}
						if(mAuthModel.getLegalPerson() != null && !mAuthModel.getLegalPerson().equals("")){
							//法人名字
							mNameEdit.setText(mAuthModel.getLegalPerson());
						}
						if(mAuthModel.getPersonNumber() != null && !mAuthModel.getPersonNumber().equals("")){
							//法人身份证
							mIdEdit.setText(mAuthModel.getPersonNumber());
						}
					}else if(name.equals("license")){
						if(null != mAuthModel.getDrivingTypeText() && !mAuthModel.getDrivingTypeText().equals("")){
							truck_content.setText(mAuthModel.getDrivingTypeText());
							truck_content.setTextColor(getResources().getColor(R.color.black));
						}
						if(0 != mAuthModel.getValidDate()){
							license_date.setText(Util.getFormateDateTimeShort(mAuthModel.getValidDate()));
							//日期毫秒值
							dateStr = ""+mAuthModel.getValidDate();
						}
						if(drivingType == null || drivingType.equals("0") || drivingType.length() <= 0){
							drivingType = ""+mAuthModel.getDrivingType();
						}
						mitmap.display(mThreeImage, mAuthModel.getDrivingPhoto());
						if(null != mAuthModel.getName()&&!mAuthModel.getName().equals("")){
							mNameEdit.setText(mAuthModel.getName());
						}
					}
					mPromptManager.closeProgressDialog();
				} catch (JSONException e) {
					e.printStackTrace();
					mPromptManager.closeProgressDialog();
					if(isShowAuthHintDialog){
						mAuthHintDialog.show();
						isShowAuthHintDialog = false;
					}
				}
			}

			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				mPromptManager.closeProgressDialog();
				
			}
		});
	}
	private void uploadFile(String url,String imageName,InputStream in) {
		try {
//			PromptManager.showProgressDialog(this, "正在上传图片");
			AjaxParams params = new AjaxParams();
//            params.put("frontCardPhoto", getContentResolver().openInputStream(uri),"img.png");
            params.put(imageName, in,"img.png");
            DidiApp.getHttpManager().sessionPost(AuthActivity.this, url, params, new AfinalHttpCallBack() {
				@Override
				public void data(String t) {
					//上传照片可能使原来未填写认证信息的变成“未完善”，所以需要刷新myfragment
					Intent broadcastIntent = new Intent();
					broadcastIntent.setAction(CommonRes.RefreshUserInfo);
					sendBroadcast(broadcastIntent);
					
					Toast.makeText(AuthActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
					if(name.equals("name")){
						loadData(get_id_url);
					}else if(name.equals("enterprise")){
						loadData(get_enterprise_url);
					}else if(name.equals("license")){
						//获取到数据
						loadData(license_url);
					}
				}
			});
		} catch (Exception e) {
		}

	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.submit:
				if(name.equals("name")){
					if(status == 3){
						ToastUtil.show(AuthActivity.this, "实名已认证不可更改");
						return;
					}
					if(!inputJudge(AuthType.individual))
					{
						return;
					}
					AjaxParams params = new AjaxParams();
					params.put("name", mNameEdit.getText().toString());
					params.put("identityCode", mIdEdit.getText().toString());
					params.put("sex", mSexText.getText().toString());
					submit(submit_person_url,params);
				}else if(name.equals("enterprise")){
					if(status == 3){
						ToastUtil.show(AuthActivity.this, "企业已认证不可更改");
						return;
					}
					if(!inputJudge(AuthType.enterprise))
					{
						return;
					}
					AjaxParams params = new AjaxParams();
					params.put("legalPerson", mNameEdit.getText().toString());
					params.put("name", mCompanyEdit.getText().toString());
					params.put("personNumber", mIdEdit.getText().toString());
					submit(submit_enterprise_url,params);
				}else if(name.equals("license")){
					if(status == 3){
						ToastUtil.show(AuthActivity.this, "驾照已认证不可更改");
						return;
					}
					if(!inputJudge(AuthType.license))
					{
						return;
					}
					AjaxParams params = new AjaxParams();
					params.put("drivingType", drivingType);
					params.put("validDate", dateStr);
					submit(submit_license_url,params);
				}
				break;
			case CommonRes.frontCardPhoto_id:
				//身份证正面
				if(status == 3){
					ToastUtil.show(AuthActivity.this, "实名已认证,身份证不可更改");
					return;
				}
				mImageName = "frontCardPhoto";
				setAnimation(mImageDialog);
				mImageDialog.show();
				break;
			case CommonRes.backCardPhoto_id:
				//身份证反面
				if(status == 3){
					ToastUtil.show(AuthActivity.this, "实名已认证,身份证不可更改");
					return;
				}
				mImageName = "backCardPhoto";
				setAnimation(mImageDialog);
				mImageDialog.show();
				break;
			case CommonRes.drivingPhoto_id:
				//驾驶证
				if(status == 3){
					ToastUtil.show(AuthActivity.this, "驾照已认证不可更改");
					return;
				}
				mImageName = "drivingPhoto";
				setAnimation(mImageDialog);
				mImageDialog.show();
				break;
			case CommonRes.c_frontCardPhoto_id:
				//企业认证身份证前面
				if(status == 3){
					ToastUtil.show(AuthActivity.this, "企业已认证不可更改");
					return;
				}
				mImageName = "frontCardPhoto";
				setAnimation(mImageDialog);
				mImageDialog.show();
				break;
			case CommonRes.c_backCardPhoto_id:
				//企业认证身份证背面
				if(status == 3){
					ToastUtil.show(AuthActivity.this, "企业已认证不可更改");
					return;
				}
				mImageName = "backCardPhoto";
				setAnimation(mImageDialog);
				mImageDialog.show();
				break;
			case CommonRes.c_businessPhoto_id:
				//营业执照
				if(status == 3){
					ToastUtil.show(AuthActivity.this, "企业已认证不可更改");
					return;
				}
				mImageName = "businessPhoto";
				setAnimation(mImageDialog);
				mImageDialog.show();
				break;
			case CommonRes.c_transportationPhoto_id:
				//道路运输经营许可证
				if(status == 3){
					ToastUtil.show(AuthActivity.this, "企业已认证不可更改");
					return;
				}
				mImageName = "transportationPhoto";
				setAnimation(mImageDialog);
				mImageDialog.show();
				break;
			case R.id.sex_layout:
				//选择性别(个人认证)或选择准驾车型(驾照认证)
				if(name.equals("license")){
					if(status == 3){
						return;
					}
					AddTruckPop	mAddTruckPop = new AddTruckPop(AuthActivity.this, 5,new CallBackString() {
						@Override 
						public void getStr(String s,String lengthId,String typeId) {
							truck_content.setText(s);
							drivingType = typeId;
						}
					});
					mAddTruckPop.setShow(v);
				}else{
					if(status == 3){
						return;
					}
					mSexDialog.show();
					setAnimation(mSexDialog);	
				}
				break;
			default:
				break;
		}
	}
	/**
	 * 从相机获取
	 */
	public void camera() { 
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		// 判断存储卡是否可以用，可用进行存储
		if (hasSdcard()) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
		}
		startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
	}
	//从图库选择
	public void photo(){
		// 激活系统图库，选择一张图片
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}
	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GetPhotoUtil.PHOTO_REQUEST_GALLERY && resultCode == RESULT_OK) {
			if (data != null) {
				// 得到图片的全路径
				Uri uri = data.getData();
//				String path = ImageUtil.getImageAbsolutePath(AuthActivity.this, uri);
				GetPhotoUtil.crop(AuthActivity.this, uri,CommonRes.tempFile);
			}
		}else if (requestCode == GetPhotoUtil.PHOTO_REQUEST_CUT && resultCode == RESULT_OK){
			String path = ImageUtil.getImageAbsolutePath(AuthActivity.this,
					Uri.fromFile(CommonRes.tempFile));
			if (auth_tag == 0) {
				uploadFile(personal_url, mImageName, ImageUtil.getimage(path));
			} else if (auth_tag == 1) {
				uploadFile(enterprise_url, mImageName, ImageUtil.getimage(path));
				Bitmap bitmap = BitmapUtil.loadBitmap(path);
				mOneImage.setImageBitmap(bitmap);
			} else if (auth_tag == 2) {
				uploadFile(upload_license_url, mImageName,
						ImageUtil.getimage(path));
			}

		}else if (requestCode == GetPhotoUtil.PHOTO_REQUEST_CAMERA &&  resultCode == RESULT_OK) {
			if (hasSdcard()) {
				// 得到图片的全路径
				String path = data.getStringExtra(ActivityCapture.kPhotoPath);
				if (auth_tag == 0) {
					//个人
					if(mImageName.equals("frontCardPhoto")){
						PhotoPreviewActivity.open(this, path, CommonRes.TYPE_SHIMING_ZHENGMIAN);
					}else if(mImageName.equals("backCardPhoto")){
						PhotoPreviewActivity.open(this, path, CommonRes.TYPE_SHIMING_FANMIAN);
					}
				} else if (auth_tag == 1) {
					//企业
					if(mImageName.equals("frontCardPhoto")){
						PhotoPreviewActivity.open(this, path, CommonRes.TYPE_FAREN_SHENFENZHENG_ZHENGMIAN);
					}else if(mImageName.equals("backCardPhoto")){
						PhotoPreviewActivity.open(this, path, CommonRes.TYPE_FAREN_SHENFENZHENG_FANMIAN);
					}else if(mImageName.equals("businessPhoto")){
						PhotoPreviewActivity.open(this, path, CommonRes.TYPE_FAREN_YINGYEZHIZHAO);
					}else if(mImageName.equals("transportationPhoto")){
						//道路许可证
						PhotoPreviewActivity.open(this, path, CommonRes.TYPE_FAREN_XUKEZHENG);
					}
					
				} else if (auth_tag == 2) {
					//驾驶证
					PhotoPreviewActivity.open(this, path, CommonRes.TYPE_JIASHIZHENG);
				}
			} else {
				Toast.makeText(AuthActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private boolean inputJudge(AuthType type){
		String idNumberReg = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$";
		if(type == AuthType.individual){
			if(mNameEdit.getText().toString().equals("")){
				ToastUtil.show(AuthActivity.this, "请填写姓名");
				return false;
			}else if(!hasSex){
				ToastUtil.show(AuthActivity.this, "请选择性别");
				return false;
			}else if(mIdEdit.getText().toString().equals("")){
				ToastUtil.show(AuthActivity.this, "请完善您的身份信息");
				return false;
			}           
            if(hasIllegalCharacter(mNameEdit.getText().toString())){
                ToastUtil.show(AuthActivity.this, "姓名不允许输入特殊符号！");
                return false;
            }
            if(!mIdEdit.getText().toString().matches(idNumberReg)){
    			ToastUtil.show(AuthActivity.this, "请正确填写身份证号码");
    			return false;
    		}
		}else if(type == AuthType.enterprise){
			if(mNameEdit.getText().toString().equals("")){
				ToastUtil.show(AuthActivity.this, "请填写法人名字");
				return false;
			}else if(mCompanyEdit.getText().toString().equals("")){
				ToastUtil.show(AuthActivity.this, "请填写企业名称");
				return false;
			}else if(mIdEdit.getText().toString().equals("")){
				ToastUtil.show(AuthActivity.this, "请完善您的身份信息");
				return false;
			}
			if(hasIllegalCharacter(mNameEdit.getText().toString())){
                ToastUtil.show(AuthActivity.this, "法人姓名不允许输入特殊符号！");
                return false;
            }
			if(hasIllegalCharacter(mCompanyEdit.getText().toString())){
                ToastUtil.show(AuthActivity.this, "企业名称不允许输入特殊符号！");
                return false;
            }
			if(!mIdEdit.getText().toString().matches(idNumberReg)){
				ToastUtil.show(AuthActivity.this, "请正确填写身份证号码");
				return false;
			}
		}else if(type == AuthType.license){
			String truckContent = truck_content.getText().toString();
			String licenseDate = license_date.getText().toString();
			if(truckContent.equals("选择准驾车型") || truckContent.length() <= 0){
				ToastUtil.show(AuthActivity.this, "请选择准驾车型");
				return false;
			}
			if(licenseDate.equals("选择证件有效日期") || licenseDate.length() <= 0){
				ToastUtil.show(AuthActivity.this, "请选择证件有效日期");
				return false;
			}
		}
		
		return true;
	}
	
	private boolean hasIllegalCharacter(String str){
		Pattern p = Pattern.compile("^[A-Za-z0-9\u4e00-\u9fa5]+$");
        Matcher m = p.matcher(str);
        if(m.matches()){
            return false;
        }
        return true;
	}
	
	private enum AuthType{
		enterprise, individual, license;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mRefreshBroadcastReceiver);
	}
}
