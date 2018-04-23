package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.UserLoginModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.update.AppUtils;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.JpushUtil;
import com.hongshi.wuliudidi.utils.MD5Util;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author huiyuan
 */
public class LoginActivity extends Activity {

	private DiDiTitleView mTitle;
	private EditText mInputAccount;
	private EditText mInputPasswd;
	private Button mInputCancel, mPasswdCancel;
	private TextView mForgetPasswd;
	private TextView mRegisterNow;
	private Toast mToast;
	private Button mLogin;
//	private String login_url = GloableParams.HOST + "uic/user/appLogin.do?";
//登陆接口
	private String login_url = GloableParams.LOGIN + "user/login.do?";
	private SharedPreferences sp;
	String sessionID;
	String passwd;

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("LoginActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("LoginActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);

		initViews();
//		ActivityManager.getInstance().exitAllActivity();
	}

	private void initViews(){
		sp = getSharedPreferences("config", MODE_PRIVATE);
		sessionID = sp.getString("session_id", "");
		String userName = sp.getString("cellphone", "");

		mTitle = (DiDiTitleView) findViewById(R.id.login_title);
		mTitle.setTitle(getResources().getString(R.string.login_text));
		mTitle.setBack(this);

		mInputAccount = (EditText) findViewById(R.id.account_input);
		mInputAccount.setText(userName);
		mInputCancel = (Button) findViewById(R.id.input_cancel);
		Util.BindingEditTextAndButton(mInputAccount, mInputCancel);

		mInputPasswd = (EditText) findViewById(R.id.passwd_input);
		mPasswdCancel = (Button) findViewById(R.id.passwd_cancel);
		Util.BindingEditTextAndButton(mInputPasswd, mPasswdCancel);

		mLogin = (Button) findViewById(R.id.login);
		setLoginOnClick();

		mForgetPasswd = (TextView) findViewById(R.id.forget_passwd);
		setForgetPasswdOnClick();

		mRegisterNow = (TextView) findViewById(R.id.register_now);
		setRegisterNowOnClick();
	}

	private void setForgetPasswdOnClick(){
		mForgetPasswd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(LoginActivity.this,PasswordRetrieveActivity.class);
				startActivity(mIntent);
			}
		});
	}
	
	private void setRegisterNowOnClick(){
		mRegisterNow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(LoginActivity.this,RegisterActivity.class);
				ActivityManager.getInstance().addActivity(LoginActivity.this);
				startActivity(mIntent);
			}
		});
	}
	
	private void setLoginOnClick(){
		mLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String getText = mInputAccount.getText().toString();
				//账户名为11位手机号
				if(getText.length() != 11)
				{
					String mHint = getResources().getString(R.string.account_illegal_error);
					mToast = Toast.makeText(getApplicationContext(), mHint, Toast.LENGTH_SHORT);
					mToast.show();
					return;
				}

				getText = mInputPasswd.getText().toString();
				if(getText.length() == 0){
					String mHint = getResources().getString(R.string.passed_empty_error);
					mToast = Toast.makeText(getApplicationContext(), mHint, Toast.LENGTH_SHORT);
					mToast.show();
					return;
				}
				if(getText.length() < 6 || getText.length() > 16){
					String mHint = getResources().getString(R.string.account_passwd_wrong);
					mToast = Toast.makeText(getApplicationContext(), mHint, Toast.LENGTH_SHORT);
					mToast.show();
					return;
				}
				doLogin();
			}
		});
	}

	private void doLogin(){
		AppUtils util = new AppUtils();
		util.doLogin(LoginActivity.this,mInputPasswd.getText().toString(),mInputAccount.getText().toString());
	}
}
