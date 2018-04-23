package com.hongshi.wuliudidi.activity;

import java.io.File;
import java.io.InputStream;
import net.tsz.afinal.http.AjaxParams;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.dialog.CancelDialog;
import com.hongshi.wuliudidi.dialog.DataFillingDialog;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.ImageUtil;
import com.hongshi.wuliudidi.utils.InputLimitTextWatcher;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.hongshi.wuliudidi.view.EditView;
import com.hongshi.wuliudidi.view.PhotoItemView;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author huiyuan
 */
public class UploadActivity extends Activity implements OnClickListener{
	private EditText mContentEdit;
	private TextView mLast;
	// 拍照
	private static final int PHOTO_REQUEST_CAMERA = 1;
	// 从相册中选择
	private static final int PHOTO_REQUEST_GALLERY = 2;
	public static File tempFile1;
	public static File tempFile2;
	public static final String PHOTO_FILE_NAME1 = "temp_photo1.jpg";
	public static final String PHOTO_FILE_NAME2 = "temp_photo2.jpg";
	private DataFillingDialog mImageDialog;
	private PhotoItemView mPhotoItemView1,mPhotoItemView2;
	private boolean first;
	private EditView mEditView;
	private TextView mSubmit;
	private String frontPath = "",backPath = "";
	private String taskId = "";
	private String upload_url = GloableParams.HOST +"carrier/transit/task/voucherreview/upload.do?";
	private DiDiTitleView title;

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("UploadActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("UploadActivity");
	}

	@SuppressLint("HandlerLeak") private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CommonRes.CAMERA:
				if(first){
					camera(PHOTO_FILE_NAME1);
				}else{
					camera(PHOTO_FILE_NAME2);
				}
				break;
			case CommonRes.GALLERY:
				photo();
				break;
			case CommonRes.PONDERATIONUPLOAD:
				uploadFile(upload_url,ImageUtil.getimage(frontPath),ImageUtil.getimage(backPath));
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
		
		setContentView(R.layout.upload_activity);
		taskId = getIntent().getStringExtra("taskId");
		mContentEdit = (EditText) findViewById(R.id.content_edit);
		mLast = (TextView) findViewById(R.id.last_text);
		title = (DiDiTitleView) findViewById(R.id.upload_title);
		mPhotoItemView1 = (PhotoItemView) findViewById(R.id.one_item);
		mPhotoItemView2 = (PhotoItemView) findViewById(R.id.two_item);
		mSubmit = (TextView) findViewById(R.id.submit);
		mEditView = (EditView) findViewById(R.id.editView);
		mPhotoItemView1.setOnClickListener(this);
		mPhotoItemView2.setOnClickListener(this);
		mPhotoItemView1.setName("过磅单");
		mPhotoItemView1.getImage().setImageResource(R.drawable.pound_bill);
		mPhotoItemView2.getImage().setImageResource(R.drawable.receipt);
		mPhotoItemView2.setName("签收单");
		mSubmit.setOnClickListener(this);
		mEditView.setEditName("实际运量");
		mEditView.setEditHint("请输入您的实际运量");
		title.setBack(this);
		title.setTitle("上传确认");
		String unit ;
		try {
			unit = getIntent().getExtras().getString("unit");
		} catch (Exception e) {
			unit = "";
		}
		//请求数据单位
//		AjaxParams params = new  AjaxParams();
//		params.put("transitTaskId", taskId);
//		DidiApp.getHttpManager().sessionPost(UploadActivity.this, unit_url, params, new ChildAfinalHttpCallBack() {
//			@Override
//			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
//			}
//			@Override
//			public void data(String t) {
//				
//			}
//		});
		EditText editTextWidget = mEditView.getEditTextWidget();
		editTextWidget.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
       if(unit.equals("车") || unit.equals("件")){
    	   editTextWidget.setInputType(InputType.TYPE_CLASS_NUMBER);
		}else{
		   InputLimitTextWatcher doubleInputLimitTextWatcher = Util.getDoubleInputLimitTextWatcher();
		   doubleInputLimitTextWatcher.setEditText(editTextWidget);
		}
		mEditView.setUnit(unit);
		// 0 表示拍照或者图库选取
		mImageDialog = new DataFillingDialog(UploadActivity.this,
				R.style.data_filling_dialog, mHandler, 0);
		mImageDialog.setCanceledOnTouchOutside(true);
		mImageDialog.setText("拍照", "图库选取");
        mContentEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				int num = 100;
				int n = num - s.length();
				mLast.setText("还剩"+n+"字输入");
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
				
			}
		});
	}
	/*
	 * 从相机获取
	 */
	public void camera(String PHOTO_FILE_NAME) {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		// 判断存储卡是否可以用，可用进行存储
		if (UploadUtil.hasSdcard()) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
		}
		startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
	}

	// 从图库选择
	public void photo() {
		// 激活系统图库，选择一张图片
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}

	private void uploadFile(String url, InputStream oneIn,InputStream twoIn) {
		try {
			AjaxParams params = new AjaxParams();
			params.put("taskId", taskId);
			params.put("realAmount", mEditView.getEditText());
			params.put("weighVoucherPic", oneIn, "img.png");
			params.put("signupVoucherPic", twoIn, "img.png");
			params.put("signupRemark", mContentEdit.getText().toString());

			final PromptManager mPromptManager = new PromptManager();
			mPromptManager.showProgressDialog(UploadActivity.this, "正在上传图片");
			DidiApp.getHttpManager().sessionPost(UploadActivity.this, url,
					params, new ChildAfinalHttpCallBack() {
						@Override
						public void data(String t) {
							mPromptManager.closeProgressDialog();
//							Toast.makeText(UploadActivity.this, "上传成功", 0).show();
							Intent intent = new Intent();
							intent.setAction(CommonRes.RefreshWinBid);
							sendBroadcast(intent);
							Intent intent1 = new Intent();
							intent1.setAction(CommonRes.UploadReceiptFinished);
							sendBroadcast(intent);
							Intent auth_intent = new Intent(UploadActivity.this,AuthResultActivity.class);
							auth_intent.putExtra("from", "bid");
							startActivity(auth_intent);
							if(tempFile2 != null){
							tempFile2.delete();
							}
							if(tempFile1 != null){
							tempFile1.delete();
							}
							finish();
						}

						@Override
						public void onFailure(String errCode, String errMsg, Boolean errSerious) {
							mPromptManager.closeProgressDialog();
						}

					});
		} catch (Exception e) {
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode ==UploadUtil.PHOTO_REQUEST_GALLERY && resultCode == RESULT_OK) {
			if (data != null) {
				// 得到图片的全路径
				Uri uri = data.getData();
				String path = ImageUtil.getImageAbsolutePath(UploadActivity.this, uri);
				Bitmap image = ImageUtil.getImage(path);
				if(first){
					mPhotoItemView1.getImage().setImageBitmap(image);
					frontPath = path;
				}else{
					mPhotoItemView2.getImage().setImageBitmap(image);
					backPath = path;
				}
			}
		}else if (requestCode == UploadUtil.PHOTO_REQUEST_CAMERA &&  resultCode == RESULT_OK) {
			if (UploadUtil.hasSdcard()) {
				if(first){
					if(tempFile1 == null){
						tempFile1 = new File(Environment.getExternalStorageDirectory(),PHOTO_FILE_NAME1);
					}	
					Uri fromFile1 = Uri.fromFile(tempFile1);
					Bitmap bitmap1 = ImageUtil.getImage(ImageUtil.getImageAbsolutePath(UploadActivity.this, fromFile1));
					//得到正面图片地址
//					frontPath = ImageUtil.getImageAbsolutePath(UploadActivity.this, fromFile1);
					String imageAbsolutePath = ImageUtil.getImageAbsolutePath(UploadActivity.this, Uri.fromFile(tempFile1));
					frontPath = imageAbsolutePath;
//					InputStream getimage = ImageUtil.getimage(imageAbsolutePath);
					if(bitmap1 != null && !bitmap1.equals("null")){
						mPhotoItemView1.getImage().setImageBitmap(bitmap1);	
					}
//					tempFile1.delete();
				}else{
					if(tempFile2 == null){
						tempFile2 = new File(Environment.getExternalStorageDirectory(),PHOTO_FILE_NAME2);
					}
						Uri fromFile2 = Uri.fromFile(tempFile2);
						Bitmap bitmap2 = ImageUtil.getImage(ImageUtil.getImageAbsolutePath(UploadActivity.this, fromFile2));
						//得到背面图片地址
//						ImageUtil.SavePicInLocal("backName", bitmap2);
						String imageAbsolutePath = ImageUtil.getImageAbsolutePath(UploadActivity.this, Uri.fromFile(tempFile2));
						backPath = imageAbsolutePath;
						if(bitmap2 != null && !bitmap2.equals("null")){
							mPhotoItemView2.getImage().setImageBitmap(bitmap2);	
						}
					
//					tempFile2.delete();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.one_item:
			// 拍照
			first = true;
			UploadUtil.setAnimation(mImageDialog,CommonRes.TYPE_BOTTOM, false);
			mImageDialog.show();
			break;
		case R.id.two_item:
			// 拍照
			first = false;
			UploadUtil.setAnimation(mImageDialog,CommonRes.TYPE_BOTTOM, false);
			mImageDialog.show();
			break;
		case R.id.submit: 
			//开始上传
			if(mEditView.getEditText().equals("")){
				ToastUtil.show(UploadActivity.this, "请填写实际运量");
				return;	
			}
			if(frontPath.equals("")){
				ToastUtil.show(UploadActivity.this, "请选择过磅单");
				return;
			}
			if(backPath.equals("")){
				ToastUtil.show(UploadActivity.this, "请选择签收单");
				return;
			}
			CancelDialog mCancelDialog = new CancelDialog(UploadActivity.this, R.style.data_filling_dialog, mHandler);
			mCancelDialog.setCanceledOnTouchOutside(true);
			mCancelDialog.setHint("您确定要提交上传单据确认？");
			mCancelDialog.setMsgCode(CommonRes.PONDERATIONUPLOAD);
			mCancelDialog.show();
			break;
		default:
			break;
		}
	}
}
