package com.hongshi.wuliudidi.dialog;

import java.io.InputStream;

import net.tsz.afinal.http.AjaxParams;

import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.RatingActivity;
import com.hongshi.wuliudidi.camera.BitmapUtil;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ImageUtil;
import com.hongshi.wuliudidi.utils.PromptManager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class UploadReceiptDialog extends Dialog implements OnClickListener{
	private Context mContext;
	private Handler mHandler;
//	private EditText transiAmountEditText;
	private TextView renewPhotoText,upload_receipt_textview;
	private ImageView receiptImage;
	private Button cancelButton, uploadButton;
//	private LinearLayout transi_amount_edit_container;
	//上传单据接口
	private final String upload_receipt_url = GloableParams.HOST + "carrier/transit/task/voucherreview/upload.do?";
	//上传提货凭证接口
	private final String upload_take_goods_ca_photo_url = GloableParams.CHE_ZHU_HOST + "commonservice/photo/upload.do";
	private final String upload_take_goods_ca_url = GloableParams.HOST + "carrier/transit/task/deliveryProofByTaskId.do";
	private String taskId, receiptImagePath;
	private double amount;
	private String finalAmount;
	private String unit;
	String submit_img_type = "";
	
	public UploadReceiptDialog(Context context, int theme, Handler mHandler, String taskId, String receiptImagePath,
			String unit) {
		super(context, theme);
		this.mContext = context;
		this.mHandler = mHandler;
		this.taskId = taskId;
		this.receiptImagePath = receiptImagePath;
		this.unit = unit;
		init();
	}

	private void init(){
		setContentView(R.layout.upload_receipt_dailog);
//		countUnit = (TextView) findViewById(R.id.count_unit);
//		transiAmountEditText = (EditText) findViewById(R.id.transi_amount_edittext);
//		transi_amount_edit_container = (LinearLayout) findViewById(R.id.transi_amount_edit_container);
		renewPhotoText = (TextView) findViewById(R.id.renew_photo_textview);
		upload_receipt_textview = (TextView) findViewById(R.id.upload_receipt_textview);
		receiptImage = (ImageView) findViewById(R.id.receipt_image);
		cancelButton = (Button) findViewById(R.id.cancel_button);
		uploadButton = (Button) findViewById(R.id.upload_button);
		
		renewPhotoText.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		uploadButton.setOnClickListener(this);

		submit_img_type = mContext.getSharedPreferences("config",Context.MODE_PRIVATE)
				.getString("submit_img_type","upload_receipt_image");

//		String  inputHint = "本次运量（";
//		if(unit != null){
//			inputHint += unit;
//		}
//		inputHint += "）：";
//		countUnit.setText(inputHint);
//		finalAmount = mContext.getSharedPreferences("config",Context.MODE_PRIVATE).getString("planAmount","0");
//		transiAmountEditText.setText(finalAmount);
		Bitmap loadBitmap = BitmapUtil.loadBitmap(receiptImagePath);
		receiptImage.setImageBitmap(loadBitmap);
		if("upload_receipt_image".equals(submit_img_type)){
			upload_receipt_textview.setText("上传回单");
			uploadButton.setText("上传回单");
//			transi_amount_edit_container.setVisibility(View.VISIBLE);
		}else if("submit_goods_ca".equals(submit_img_type)){
			upload_receipt_textview.setText("上传提货凭证");
			uploadButton.setText("上传提货凭证");
//			transi_amount_edit_container.setVisibility(View.GONE);
		}
	}
	
//	private boolean inputJudge(){
//		if(transiAmountEditText == null){
//			return false;
//		}
//		String getEdit = transiAmountEditText.getEditableText().toString();
//		if(getEdit == null){
//			return false;
//		}
//		amount = Util.inputToDoubleValue(getEdit);
//		if(amount <= 0){
//			return false;
//		}
//		return true;
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.renew_photo_textview:
			mHandler.sendEmptyMessage(CommonRes.RENEW_UPLOAD_PHOTO);
			dismiss();
			break;
		case R.id.upload_button:
//			if(!inputJudge()){
//				ToastUtil.show(mContext, "请填写本次运量");
//				return;
//			}
			if("upload_receipt_image".equals(submit_img_type)){
				uploadReceipt(receiptImagePath);
			}else if("submit_goods_ca".equals(submit_img_type)){
				uploadReceiptForDriverCa(receiptImagePath);
			}
			break;
		case R.id.cancel_button:
			dismiss();
			break;
		default:
			break;
		}
		
	}

	private void uploadReceiptForDriverCa(final String receiptImagePath){
		AjaxParams params = new AjaxParams();
		InputStream imageIns = ImageUtil.getimage(receiptImagePath);
		params.put("photo", imageIns, "img.png");
		final PromptManager mPromptManager = new PromptManager();
		mPromptManager.showProgressDialog1(mContext, "上传中");
		DidiApp.getHttpManager().sessionPost(mContext,upload_take_goods_ca_photo_url, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				Log.d("huiyuan","上传提货凭证照片返回信息 = " + t);
				String url = "";
				try {
					JSONObject jsonObject = new JSONObject(t);
					 url = jsonObject.optString("body");
				}catch (Exception e){
					return;
				}

				AjaxParams params = new AjaxParams();
				params.put("taskId", taskId);
				params.put("deliveryProof", url);
				DidiApp.getHttpManager().sessionPost(mContext, upload_take_goods_ca_url, params,new ChildAfinalHttpCallBack(){
					@Override
					public void data(String t) {
						mPromptManager.closeProgressDialog();
						Toast.makeText(mContext.getApplicationContext(), "上传完成!", Toast.LENGTH_LONG).show();

//						Intent intent = new Intent();
//						intent.setAction(CommonRes.UploadTakeOrdersCaFinished);
//						mContext.sendBroadcast(intent);
						dismiss();
					}

					@Override
					public void onFailure(String errCode, String errMsg, Boolean errSerious) {
						mPromptManager.closeProgressDialog();
						Toast.makeText(mContext,"上传提货凭证失败:" + errMsg, Toast.LENGTH_LONG).show();
					}
				});
			}
			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				mPromptManager.closeProgressDialog();
			}
		});
	}

	private void uploadReceipt(final String receiptImagePath){
		AjaxParams params = new AjaxParams();
		params.put("taskId", taskId);
		params.put("realAmount", String.valueOf(amount));
		InputStream imageIns = ImageUtil.getimage(receiptImagePath);
		params.put("signupVoucherPic", imageIns, "img.png");
		final PromptManager mPromptManager = new PromptManager();
		mPromptManager.showProgressDialog1(mContext, "上传中");
		DidiApp.getHttpManager().sessionPost(mContext,  upload_receipt_url, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				mPromptManager.closeProgressDialog();
				try {
					JSONObject jsonObject = new JSONObject(t);
					String integral = jsonObject.optString("body");
					if (integral != null && !"".equals(integral) && Integer.parseInt(integral) > 0){
						Toast.makeText(mContext,"获得" + integral + "个积分",Toast.LENGTH_LONG).show();
						Intent intent = new Intent();
						intent.setAction("earned_integral");
						mContext.sendBroadcast(intent);
					}else {
						Toast.makeText(mContext.getApplicationContext(), "上传完成!", Toast.LENGTH_LONG).show();
					}
				}catch (Exception e){

				}

				Intent intent = new Intent();
				intent.setAction(CommonRes.UploadReceiptFinished);
				mContext.sendBroadcast(intent);
				dismiss();
			}
			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				mPromptManager.closeProgressDialog();
			}
		});	
	}
}
