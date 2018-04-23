package com.hongshi.wuliudidi.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.http.AjaxParams;

import com.hongshi.wuliudidi.AES;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.SmsObserver;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.hongshi.wuliudidi.view.TimeCounterButton;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telecom.Call;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author huiyuan
 */
public class PasswordRetrieveActivity extends Activity implements OnClickListener{

	private DiDiTitleView mTitle;
	private EditText mInputAccount;
	private EditText mInputCaptchas;
	private Button mInputCancel;
	private TimeCounterButton mGetCaptchas;
	private Button mNextStep;
	private String sessionID = "";
	private SharedPreferences sp;
	private Editor edit;
	String phone_number = "";
	String verifyNumber = "";
	//获取忘记密码验证码接口
	String url = GloableParams.HOST +"uic/user/sendForgetPswCode.do?";
	//验证忘记密码手机验证码接口
	String verify_url = GloableParams.HOST + "uic/user/verifyForgetPswCode.do?";

	private SmsObserver mObserver;
	private Handler mHandler = new Handler(){
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
		MobclickAgent.onPageEnd("PasswordRetrieveActivity");
		getContentResolver().unregisterContentObserver(mObserver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("PasswordRetrieveActivity");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mHandler != null){
			mHandler.removeCallbacks(null);
			mHandler = null;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.register_activity);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		sessionID = sp.getString("session_id", "");
		mTitle = (DiDiTitleView) findViewById(R.id.register_title);
		mTitle.setTitle(getResources().getString(R.string.passwd_recovery));
		mTitle.setBack(this);

		mInputAccount = (EditText) findViewById(R.id.account_input);
		mInputCancel = (Button) findViewById(R.id.input_cancel);

		Util.BindingEditTextAndButton(mInputAccount, mInputCancel);

		mInputCaptchas = (EditText) findViewById(R.id.captchas_input);

		mGetCaptchas = (TimeCounterButton) findViewById(R.id.get_captchas);
		mGetCaptchas.setOnClickListener(this);

		mNextStep = (Button) findViewById(R.id.next_step);
		mNextStep.setOnClickListener(this);

		mObserver = new SmsObserver(this, mHandler);
		Uri uri = Uri.parse("content://sms");
		// 注册ConentObserver
		getContentResolver().registerContentObserver(uri, true, mObserver);

	}
	//第一次拿sessionId
		private void getSessionId(String url,AjaxParams params){
			DidiApp.getHttpManager().post(PasswordRetrieveActivity.this, url, params, new ChildAfinalHttpCallBack() {
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
					ToastUtil.show(PasswordRetrieveActivity.this, getResources().getString(R.string.please_check_your_network));
				}
			});
		}
		private void setParams(){
			AjaxParams params = new AjaxParams();
			AES aes = new AES();
			phone_number = mInputAccount.getText().toString();
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
			DidiApp.getHttpManager().sessionPost(PasswordRetrieveActivity.this, url, params, new AfinalHttpCallBack() {
				@Override
				public void data(String t) {
					ToastUtil.show(PasswordRetrieveActivity.this, "成功获取验证码");
					mGetCaptchas.TimeCounting(60);
				}

			});
		}
		//校正验证码
		private void verify(){
			AjaxParams params = new AjaxParams();
			params.put("cellphone", mInputAccount.getText().toString());
			params.put("code", mInputCaptchas.getText().toString());
			DidiApp.getHttpManager().sessionPost(PasswordRetrieveActivity.this, verify_url, params, new AfinalHttpCallBack() {
				@Override
				public void data(String t) {
					ToastUtil.show(PasswordRetrieveActivity.this, "校验成功，请设置密码");
					Intent mIntent = new Intent(PasswordRetrieveActivity.this,SetNewPasswdActivity.class);
					mIntent.putExtra("tel", mInputAccount.getText().toString());
					startActivity(mIntent);
					finish();
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

			// 判断
			verify();
			break;
		default:
			break;
		}
	}
}
