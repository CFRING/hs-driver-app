package com.hongshi.wuliudidi.dialog;

import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.qr.ConfirmGoodsActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class DataFillingDialog extends Dialog implements
		android.view.View.OnClickListener {
	private Context mContext;
	private Button mChoice1, mChoice2, mChoice3, mCancel;
	private Handler mHandler;
	private int tag = -1;
	private String taskId = "";
	//货物送达的派车单对应的车牌号
	private String license = "";
	private RelativeLayout mPhotoExampleLayout;
	private ImageView mPhotoImage,receiptExampleImg;
	private int finalAmount = 0;

	public DataFillingDialog(Context context) {
		super(context);
		this.mContext = context;
		init();
	}

	protected DataFillingDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.mContext = context;
		init();
	}

	public DataFillingDialog(Context context, int theme, Handler mHandler, int tag) {
		super(context, theme);
		this.mContext = context;
		this.mHandler = mHandler;
		this.tag = tag;
		init();
	}

	public DataFillingDialog(Context context, int theme, Handler mHandler, int tag, double finalAmount) {
		super(context, theme);
		this.mContext = context;
		this.mHandler = mHandler;
		this.tag = tag;
		this.finalAmount = (int)finalAmount;
		init();
	}
	
	public DataFillingDialog(Context context, int theme, Handler mHandler, int tag, String taskId, String license){
		super(context, theme);
		this.mContext = context;
		this.mHandler = mHandler;
		this.tag = tag;
		this.taskId = taskId;
		this.license = license;
		init();
	}

	public void setText(String str1, String str2) {
		if(mChoice1 != null){
			mChoice1.setText(str1);
		}
		if(mChoice2 != null){
			mChoice2.setText(str2);
		}
	}
	
	public void setText(String str1, String str2, String str3) {
		if(mChoice1 != null){
			mChoice1.setText(str1);
		}
		if(mChoice2 != null){
			mChoice2.setText(str2);
		}
		if(mChoice3 != null){
			mChoice3.setText(str3);
		}
	}
	
	public void setPhotoVISIBLE(int resId) {
		if(mPhotoImage == null)
		{
			return;
		}
		mPhotoImage.setImageResource(resId);
		mPhotoExampleLayout.setBackgroundResource(resId);
		mPhotoExampleLayout.setVisibility(View.VISIBLE);
		receiptExampleImg.setVisibility(View.GONE);
	}

	@SuppressLint("NewApi") private void init() {
		setContentView(R.layout.data_filling_dialog);
		mChoice1 = (Button) findViewById(R.id.choice1);
		mChoice2 = (Button) findViewById(R.id.choice2);
		mChoice3 = (Button) findViewById(R.id.choice3);
		mCancel = (Button) findViewById(R.id.cancel);
		mPhotoExampleLayout = (RelativeLayout) findViewById(R.id.photo_example_layout);
		mPhotoImage = (ImageView) findViewById(R.id.example_image);
		receiptExampleImg = (ImageView) findViewById(R.id.example_img);

		WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		int width = windowManager.getDefaultDisplay().getWidth();
		int height = 3*width/4;
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
		receiptExampleImg.setLayoutParams(layoutParams);
		if(finalAmount != -1){
			receiptExampleImg.setImageResource(R.drawable.receipt_example_img);
		}

		mChoice1.setOnClickListener(this);
		mChoice2.setOnClickListener(this);
		mChoice3.setOnClickListener(this);
		mCancel.setOnClickListener(this);

		//choice2和choice3之间的分割线，如果只有两个选项则不需显示此分割线
		View divideLine = (View) findViewById(R.id.divide_line);
		if(tag == CommonRes.ARRIVETAG){
			mChoice3.setVisibility(View.VISIBLE);
			mChoice2.setBackgroundColor(mContext.getResources().getColor(R.color.white));
			divideLine.setVisibility(View.VISIBLE);
		}else{
			mChoice3.setVisibility(View.GONE);
			mChoice2.setBackground(mContext.getResources().getDrawable(R.drawable.dialog_button_below));
			divideLine.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
	
		switch (v.getId()) {
		case R.id.choice1:
			if(tag == CommonRes.SEXTAG){
				mHandler.obtainMessage(CommonRes.CAMERA,finalAmount,0,null).sendToTarget();
			}else if(tag == CommonRes.PICTAG){
				mHandler.sendEmptyMessage(CommonRes.MAN);
			}else if(tag == CommonRes.ARRIVETAG){
				Intent intent = new Intent(mContext,ConfirmGoodsActivity.class);
				intent.putExtra("license", license);
				intent.putExtra("taskId", taskId);
				mContext.startActivity(intent);
			}
			DataFillingDialog.this.dismiss();
			break;
		case R.id.choice2:
			if(tag == 0){
				mHandler.sendEmptyMessage(CommonRes.GALLERY);
			}else if(tag == 1){
				mHandler.sendEmptyMessage(CommonRes.WOMAN);
			}else if(tag == CommonRes.ARRIVETAG){
//				Intent intent = new Intent(mContext,UploadActivity.class);
//				intent.putExtra("taskId", taskId);
//				intent.putExtra("unit", unit);
//				mContext.startActivity(intent);
				mHandler.sendEmptyMessage(CommonRes.CAMERA);
			}
			DataFillingDialog.this.dismiss();
			break;
		case R.id.choice3:
			if(tag == CommonRes.ARRIVETAG){
				mHandler.sendEmptyMessage(CommonRes.GALLERY);
			}
			DataFillingDialog.this.dismiss();
			break;
		case R.id.cancel:
			this.dismiss();
			break;

		default:
			break;
		}

	}
}
