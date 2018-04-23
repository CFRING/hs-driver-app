package com.hongshi.wuliudidi.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.AES;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.dialog.CancelDialog;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.UserModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.qr.ConfirmGoodsActivity;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.SmsObserver;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.CircleImageView;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.hongshi.wuliudidi.view.TimeCounterButton;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author huiyuan
 */
public class RegisterActivity extends Activity implements OnClickListener{

	private DiDiTitleView mTitle;
	private EditText mInputAccount;
	private EditText mInputCaptchas;
	private Button mInputCancel;
	private TimeCounterButton mGetCaptchas;
	private Button mNextStep;
	private TextView agreementText, promoterName, promoterTitle;
	private SharedPreferences sp;
	private Editor edit;
	private String sessionID = "";
	//获取验证码接口
	String url = GloableParams.HOST +"uic/user/sendPhoneRegisterCode.do?";
	//验证手机验证码接口
	String verify_url = GloableParams.HOST + "uic/user/verifyRegisterCode.do?";
	//扫描推广专员二维码
	String scan_promoter_url = GloableParams.HOST + "uic/user/getUserByScanningID.do?";
	String phone_number = "";
	String verifyNumber = "";
	String backto = "";
	RelativeLayout promoterLayout;
	CircleImageView promoterHead;
	ImageView scanning;
	private FinalBitmap mFinalBitmap;
	String promoterId;
	private final int ScanCode = 1010;

	private SmsObserver mObserver;
	private Handler smsHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == CommonRes.SMS_PASSWORD) {
				String code = (String) msg.obj;
				mInputCaptchas.setText(code);
			}
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("RegisterActivity");
		getContentResolver().unregisterContentObserver(mObserver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("RegisterActivity");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mHandler != null){
			mHandler.removeCallbacks(null);
			mHandler = null;
		}
		if(smsHandler != null){
			smsHandler.removeCallbacks(null);
			smsHandler = null;
		}
	}

	@SuppressLint("HandlerLeak") private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CommonRes.CONTINUESCAN:
				//继续扫描
				Intent mIntent = new Intent(RegisterActivity.this, ConfirmGoodsActivity.class);
				mIntent.putExtra("type", 2);
				startActivityForResult(mIntent, ScanCode);
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	@SuppressLint("NewApi") protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);
		initViews();
	}

	private void initViews(){
		sp = getSharedPreferences("config", MODE_PRIVATE);
		sessionID = sp.getString("session_id", "");
		mTitle = (DiDiTitleView) findViewById(R.id.register_title);
		mTitle.setTitle(getResources().getString(R.string.new_user_reg));

		scanning = mTitle.getRightImage();
		scanning.setImageResource(R.drawable.scanning);

		try {
			backto = getIntent().getExtras().getString("backto", "");
		} catch (Exception e) {
			backto = "";
		}
		if(backto.length() > 0){
			try {
				mTitle.setBack(this, Class.forName(backto));
			} catch (ClassNotFoundException e) {
				mTitle.setBack(this);
			}
		}else{
			mTitle.setBack(this);
		}
		agreementText = (TextView) findViewById(R.id.agreement_text);
		agreementText.setVisibility(View.VISIBLE);
		String whole = getResources().getString(R.string.regisiter_hint) + getResources().getString(R.string.agreement);
		String changeColor = getResources().getString(R.string.agreement);
		Util.setText(RegisterActivity.this, whole, changeColor, agreementText, R.color.theme_color);
		//电话号码Edittext
		mInputAccount = (EditText) findViewById(R.id.account_input);
		mInputCancel = (Button) findViewById(R.id.input_cancel);

		Util.BindingEditTextAndButton(mInputAccount, mInputCancel);
		//验证码Edittext
		mInputCaptchas = (EditText) findViewById(R.id.captchas_input);
		mGetCaptchas = (TimeCounterButton) findViewById(R.id.get_captchas);
		mNextStep = (Button) findViewById(R.id.next_step);

		promoterLayout = (RelativeLayout) findViewById(R.id.promoter_layout);
		promoterHead = (CircleImageView) findViewById(R.id.promoter_head);
		promoterName = (TextView) findViewById(R.id.promoter_name);
		promoterTitle = (TextView) findViewById(R.id.promoter_title);
		setOnClick();

		mFinalBitmap = FinalBitmap.create(RegisterActivity.this);
		mObserver = new SmsObserver(this, smsHandler);
		Uri uri = Uri.parse("content://sms");
		// 注册ConentObserver
		getContentResolver().registerContentObserver(uri, true, mObserver);
	}

	private void setOnClick() {
		scanning.setOnClickListener(this);
		mGetCaptchas.setOnClickListener(this);
		mNextStep.setOnClickListener(this);
		agreementText.setOnClickListener(this);
		scanning.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(RegisterActivity.this, ConfirmGoodsActivity.class);
				mIntent.putExtra("type", 2);
				startActivityForResult(mIntent, ScanCode);
			}
		});
	}
	private void setParams(){
		AjaxParams params = new AjaxParams();
		AES aes = new AES();
		phone_number = mInputAccount.getText().toString();
		if(!Util.isMobile(phone_number)){
			Toast.makeText(RegisterActivity.this, "请输入合法的手机号码", Toast.LENGTH_SHORT).show();
			mGetCaptchas.setEnabled(true);
			return;
		}
		params.put("cellphone",phone_number);
		String sessionId = sp.getString("session_id", "");
		//加密之前的crc
		String crc = sessionId+"_"+ phone_number;
		try {
			//加密过后的crc
			crc = URLEncoder.encode(aes.encrypt(crc.getBytes()), "utf-8");
			params.put("crc",crc);
			getDate(url, params);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			mGetCaptchas.setEnabled(true);
		}
	}
	private void getDate(String url,AjaxParams params){
		final PromptManager mPromptManager = new PromptManager();
		mPromptManager.showProgressDialog(RegisterActivity.this, "");
		DidiApp.getHttpManager().sessionPost(RegisterActivity.this, url, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				mPromptManager.closeProgressDialog();
				mGetCaptchas.TimeCounting(60);
			}
			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				mPromptManager.closeProgressDialog();
				mGetCaptchas.setEnabled(true);
			}
		});
	}
	//第一次拿sessionId
	private void getSessionId(String url,AjaxParams params){
		DidiApp.getHttpManager().post(RegisterActivity.this, url, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				try {
					JSONObject jsonObject = new JSONObject(t);
					if (jsonObject != null) {
						String sessionId = jsonObject.getString("newSessionId");
						edit = sp.edit();
						edit.putString("session_id", sessionId);
						edit.commit();
						setParams();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				ToastUtil.show(RegisterActivity.this, getResources().getString(R.string.please_check_your_network));
			}
		});
	}
	
	//校正验证码
	private void verify(){
		AjaxParams params = new AjaxParams();
		params.put("cellphone", mInputAccount.getText().toString());
		params.put("code", mInputCaptchas.getText().toString());
		DidiApp.getHttpManager().sessionPost(RegisterActivity.this, verify_url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				Toast.makeText(RegisterActivity.this, "校验成功，请设置密码", Toast.LENGTH_SHORT).show();
				ActivityManager.getInstance().addActivity(RegisterActivity.this);
				Intent mIntent = new Intent(RegisterActivity.this,RegSetPasswdActivity.class);
				mIntent.putExtra("tel", mInputAccount.getText().toString());
				if(promoterId != null && !promoterId.equals("")){
					mIntent.putExtra("promoterId", promoterId);
				}
				startActivity(mIntent);
			}
		});	
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_captchas:
			String phone = mInputAccount.getText().toString();
			if (phone.equals("")) {
				Toast.makeText(getApplicationContext(), "请输入手机号码", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			if (phone.length() != 11) {
				String mHint = getResources().getString(
						R.string.account_illegal_error);
				Toast.makeText(getApplicationContext(), mHint, Toast.LENGTH_SHORT)
						.show();
				return;
			}
			if (sessionID.equals("")) {
				AjaxParams params = new AjaxParams();
				getSessionId(url, params);
			} else {
				mGetCaptchas.setEnabled(false);
				setParams();
			}
			break;
		case R.id.next_step:
			String getText = mInputAccount.getText().toString();
			// 账户名为11位手机号
			if (getText.length() != 11) {
				String mHint = getResources().getString(
						R.string.account_illegal_error);
				Toast.makeText(getApplicationContext(), mHint,
						Toast.LENGTH_SHORT).show();
				return;
			}

			getText = mInputCaptchas.getText().toString();
			if (getText.length() == 0) {
				String mHint = getResources().getString(
						R.string.captchas_empty_error);
				Toast.makeText(getApplicationContext(), mHint,
						Toast.LENGTH_SHORT).show();
				return;
			}
			verify();
			break;
		case R.id.agreement_text:
			Intent intent = new Intent(RegisterActivity.this, WebViewWithTitleActivity.class);
			intent.putExtra("url", GloableParams.WEB_URL + "registrationProtocol.html");
			intent.putExtra("title", "服务协议");
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent;
			try {
				intent = new Intent(RegisterActivity.this,Class.forName(backto));
				startActivity(intent);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String scanResult = "";
		if(requestCode == ScanCode){
			try {
				scanResult = data.getStringExtra("scanResult");
			} catch (Exception e) {
			}
			
			if(!scanResult.equals("")){
				AjaxParams params = new AjaxParams();
				params.put("BDId", scanResult);
				DidiApp.getHttpManager().sessionPost(RegisterActivity.this, scan_promoter_url, params, new ChildAfinalHttpCallBack() {
					@Override
					public void data(String t) {
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(t);
							String all = jsonObject.getString("body");
							UserModel mUserModel = JSON.parseObject(all, UserModel.class);
							promoterLayout.setVisibility(View.VISIBLE);
							if(mUserModel.getUserFace() != null && !mUserModel.getUserFace().equals("")){
								mFinalBitmap.display(promoterHead, mUserModel.getUserFace());
							}else{
								promoterHead.setImageResource(R.drawable.promoter_default_head);
							}
							if(mUserModel.getName() != null && !mUserModel.getName().equals("")){
								//推广员姓名
								promoterName.setText(mUserModel.getName());
							}else{
								promoterName.setText("未知");
							}
							//推广员职位
							if(mUserModel.getPosition() != null && !mUserModel.getPosition().equals("")){
								promoterTitle.setText(mUserModel.getPosition());
							}
							promoterId = mUserModel.getUserId();
						} catch (Exception e) {
							promoterId = "";
							getPromoterMsgFaild();
						}
					}

					@Override
					public void onFailure(String errCode, String errMsg, Boolean errSerious) {
						promoterId = "";
						getPromoterMsgFaild();
					}
				});
			}
		}
	}
	private void getPromoterMsgFaild(){
		CancelDialog mCancelDialog = new CancelDialog(RegisterActivity.this, R.style.data_filling_dialog, mHandler);
		mCancelDialog.setCanceledOnTouchOutside(true);
		mCancelDialog.setHint("您扫描的二维码类型系统无法识别");
		mCancelDialog.setRightText("重新扫描");
		mCancelDialog.setMsgCode(CommonRes.CONTINUESCAN);
		mCancelDialog.show();
	}
}
