package com.hongshi.wuliudidi.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.tsz.afinal.http.AjaxParams;

import com.hongshi.wuliudidi.AES;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.LogUtil;
import com.hongshi.wuliudidi.utils.MD5Util;
import com.hongshi.wuliudidi.view.AuctionItem;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.hongshi.wuliudidi.view.TimeCounterButton;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author huiyuan
 */
public class PasswdSettingActivity extends Activity implements OnClickListener{
	private DiDiTitleView mTitleView;
	private AuctionItem mCellphone;
	private AuctionItem mCaptchas;
	private AuctionItem mNewPasswd;
	private AuctionItem mConfirmNewPasswd;
	private TimeCounterButton mGetCaptchas;
	private Button mSubmit;
	String phone_number = "";
	String passwd = "";
	String verifyNumber = "";
	private SharedPreferences sp;
	//发送“修改密码”验证码
	String url = GloableParams.HOST +"uic/user/sendChangePswCode.do";
	//验证忘记密码手机验证码接口
	String verify_url = GloableParams.HOST + "uic/user/verifyChangePswCode.do";
	//设置新密码接口
	String newpasswd_url = GloableParams.HOST + "uic/user/changePasswd.do";

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("PasswdSettingActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("PasswdSettingActivity");
	}

	@SuppressLint("SetJavaScriptEnabled") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		
		setContentView(R.layout.password_setting_activity);
		initViews();
	}

	private void initViews(){
		sp = getSharedPreferences("config", MODE_PRIVATE);

		/*设置标题*/
		mTitleView = (DiDiTitleView) findViewById(R.id.passwd_modify_title);
		mTitleView.setTitle(getResources().getString(R.string.passwd_modify));
		mTitleView.setBack(this);

		/*第一行，获取手机号，手机号输入栏设为不可编辑*/
		mCellphone = (AuctionItem) findViewById(R.id.input_cellphone);
		mCellphone.setName(getResources().getString(R.string.cellphone));
		mCellphone.setNameColor(getResources().getColor(R.color.black));
		mCellphone.setHint(getResources().getString(R.string.input_cellphone));
		EditText mCell = mCellphone.getContentEdit();
		mCell.setText(sp.getString("cellphone", ""));
		mCell.setEnabled(false);

		/*第二行，输入验证码*/
		mCaptchas = (AuctionItem) findViewById(R.id.input_captchas);
		mCaptchas.setName(getResources().getString(R.string.captchas));
		mCaptchas.setNameColor(getResources().getColor(R.color.black));
		mCaptchas.setHint(getResources().getString(R.string.input_captchas));

		/*第三行，输入新密码*/
		mNewPasswd = (AuctionItem) findViewById(R.id.input_newpasswd);
		mNewPasswd.setName(getResources().getString(R.string.new_password));
		mNewPasswd.setNameColor(getResources().getColor(R.color.black));
		mNewPasswd.setHint(getResources().getString(R.string.input_new_password));
		mNewPasswd.getContentEdit().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

		/*第四行，再次输入新密码*/
		mConfirmNewPasswd = (AuctionItem) findViewById(R.id.confirm_newpasswd);
		mConfirmNewPasswd.setName(getResources().getString(R.string.confirm_password));
		mConfirmNewPasswd.setNameColor(getResources().getColor(R.color.black));
		mConfirmNewPasswd.setHint(getResources().getString(R.string.input_comfirm_password));
		mConfirmNewPasswd.getContentEdit().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

		/*第二行附有获取验证码的按钮*/
		mGetCaptchas = (TimeCounterButton) findViewById(R.id.get_captchas);
		mGetCaptchas.setOnClickListener(this);

		/*提交*/
		mSubmit = (Button) findViewById(R.id.submit_newpasswd);
		mSubmit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.get_captchas:
				if(judgePhone()){
					mGetCaptchas.setEnabled(false);
					getData();
					mGetCaptchas.TimeCounting(60);
				}
				break;
			case R.id.submit_newpasswd:
				if(judgePhone()&&judgePasswd()&&judgeCaptchas()){
					//如果校验成功，它直接就提交密码
					submit(passwd);
				}
				break;
			default:
				break;
		}
		
	}
	
	private Boolean judgePhone(){
		phone_number = mCellphone.getContentEdit().getText().toString();
		String mHint;
		if (phone_number.equals("")) {
			mHint = getResources().getString(R.string.account_empty_error);
			Toast.makeText(getApplicationContext(), mHint, Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		else if (phone_number.length() != 11) {
			mHint = getResources().getString(R.string.account_illegal_error);
			Toast.makeText(getApplicationContext(), mHint, Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}
	
	private Boolean judgePasswd(){
		passwd = mNewPasswd.getContentEdit().getText().toString();
		String limit = "^[a-z0-9A-Z]+$";
		String mHint;
		if(!passwd.matches(limit)){
			mHint = getResources().getString(R.string.passwd_illegal_error);
			Toast.makeText(getApplicationContext(), mHint, Toast.LENGTH_SHORT)
				.show();
			return false;
		}
		else if(passwd.length() > 16 || passwd.length() < 6){
			mHint = getResources().getString(R.string.passwd_length_error);
			Toast.makeText(getApplicationContext(), mHint, Toast.LENGTH_SHORT)
				.show();
			return false;
		}
		String confirm = mConfirmNewPasswd.getContentEdit().getText().toString();
		if(!confirm.equals(passwd)){
			mHint = getResources().getString(R.string.passwd_inconsistent_error);
			Toast.makeText(getApplicationContext(), mHint, Toast.LENGTH_SHORT)
				.show();
			return false;
		}	
		return true;
	}
	
	private Boolean judgeCaptchas(){
		verifyNumber = mCaptchas.getContentEdit().getText().toString();
		String mHint;
		if(verifyNumber.length() == 0){
			mHint = getResources().getString(R.string.captchas_empty_error);
			Toast.makeText(getApplicationContext(), mHint, Toast.LENGTH_SHORT)
			.show();
			return false;
		}
		return true;
	}
	
	private void getData(){
		AjaxParams params = new AjaxParams();
		AES aes = new AES();
		phone_number = mCellphone.getContentEdit().getText().toString();
		params.put("cellphone",phone_number);
		String sessionId = sp.getString("session_id", "");
		//加密之前的crc
		String crc = sessionId+"_"+ phone_number;

		try {
			//加密过后的crc
			crc = URLEncoder.encode(aes.encrypt(crc.getBytes()), "utf-8");
			params.put("crc",crc);
			DidiApp.getHttpManager().sessionPost(PasswdSettingActivity.this, url, params, new AfinalHttpCallBack() {
				@Override
				public void data(String t) {
					Toast.makeText(PasswdSettingActivity.this, "成功获取验证码", Toast.LENGTH_SHORT).show();
//					GetCaptasUtil.getCaptas(PasswdSettingActivity.this,"");//测试环境获取验证码接口
				}
			});
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}	
	}
	
	//修改密码
	private void submit(String passwd){
		LogUtil.i("passwd", "密码=="+passwd);
		//MD5加密后的字符串
		passwd = MD5Util.getMD5String(passwd);
		AjaxParams params = new AjaxParams();
		
		params.put("newPasswd", passwd);
		params.put("code", verifyNumber);

		DidiApp.getHttpManager().sessionPost(this, newpasswd_url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				Toast.makeText(PasswdSettingActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}
}
