package com.hongshi.wuliudidi.activity;

import net.tsz.afinal.http.AjaxParams;

import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.MD5Util;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ToggleButton;

/**
 * @author huiyuan
 */
public class SetNewPasswdActivity extends Activity {

	private DiDiTitleView mTitle;
	private EditText mInputNewPasswd;
	private EditText mConfirmNewPasswd;
	private ToggleButton mPasswdVisibility, mConfirmPasswdVisibility;
	private Button mFinish;
	//提交密码url
	private String password_url =GloableParams.HOST + "uic/user/changeForgetPsw.do?";
	private String tel;

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("SetNewPasswdActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("SetNewPasswdActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.set_newpasswd_activity);
		
		initViews();
	}

	private void initViews(){
		mTitle = (DiDiTitleView) findViewById(R.id.set_newpasswd_title);
		mTitle.setTitle(getResources().getString(R.string.set_newpasswd));
		mTitle.setBack(this);

		//电话号码，唯一号
		tel = getIntent().getStringExtra("tel");
		mInputNewPasswd = (EditText) findViewById(R.id.newpasswd);
		mPasswdVisibility = (ToggleButton) findViewById(R.id.passwd_visibility);
		bindingEditTextVisibility(mInputNewPasswd, mPasswdVisibility);

		mConfirmNewPasswd = (EditText) findViewById(R.id.confirm_newpasswd);
		mConfirmPasswdVisibility = (ToggleButton) findViewById(R.id.confirm_newpasswd_visibility);
		bindingEditTextVisibility(mConfirmNewPasswd, mConfirmPasswdVisibility);

		mFinish = (Button) findViewById(R.id.finish);
		setFinishOnClick();
	}

	private void bindingEditTextVisibility(final EditText mEditText, final ToggleButton mToggle){
		mToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				}
				else{
					mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
				CharSequence mCharSq = mEditText.getText();
				if(mCharSq instanceof Spannable){
					Spannable mSpText = (Spannable) mCharSq;
					Selection.setSelection(mSpText, mCharSq.length());
				}
			}
		});
	}
	private void submit(String passwd){
		AjaxParams params = new AjaxParams();
		params.put("cellphone", tel);
		params.put("newPasswd", passwd);
		DidiApp.getHttpManager().sessionPost(this, password_url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				ToastUtil.show(SetNewPasswdActivity.this, "设置成功");
				finish();
			}
		});
	}
	private void setFinishOnClick(){
		mFinish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast mToast;
				String getText = mInputNewPasswd.getText().toString();
				String limit = "^[a-z0-9A-Z]+$";
				if(!getText.matches(limit) ){
					String mHint = getResources().getString(R.string.passwd_illegal_error);
					mToast = Toast.makeText(getApplicationContext(), mHint, Toast.LENGTH_SHORT);
					mToast.show();
					return;
				}
				else if(getText.length() > 16 || getText.length() < 6){
					String mHint = getResources().getString(R.string.passwd_length_error);
					mToast = Toast.makeText(getApplicationContext(), mHint, Toast.LENGTH_SHORT);
					mToast.show();
					return;
				}
				
				String getRepeat = mConfirmNewPasswd.getText().toString();
				if(!getText.equals(getRepeat)) {
					String mHint = getResources().getString(R.string.passwd_inconsistent_error);
					mToast = Toast.makeText(getApplicationContext(), mHint, Toast.LENGTH_SHORT);
					mToast.show();
					return;
				}
				
				//MD5加密后的字符串
				String passwd = MD5Util.getMD5String(getText);
				submit(passwd);
			}
		});
	}
}
