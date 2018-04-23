package com.hongshi.wuliudidi.activity;

import net.tsz.afinal.http.AjaxParams;

import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.JpushUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.hongshi.wuliudidi.view.MyItemView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

/**
 * @author huiyuan
 */
public class SettingsActivity extends Activity implements OnClickListener{
	private DiDiTitleView mSettingsTitle;
	private MyItemView mChangePassWordItem, mChangePhoneItem,mTransitAgreementItem, mUpdateItem, mOpinionItem, mAboutItem;
	private Button mExitBtn;
	private ToggleButton voice_switch;
	private SharedPreferences sp;
	//车主退出登录接口
	private String logout_url = "https://cz.redlion56.com/gwcz/" +"uic/user/logout.do?";

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("SettingsActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("SettingsActivity");
	}

	//刷新页面的广播接收
		private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (action.equals(CommonRes.RefreshUserInfo)) {
					if(!Util.isLogin(SettingsActivity.this)){
				  		mExitBtn.setText("登录");
				  		mChangePassWordItem.setVisibility(View.GONE);
				  	}else{
				  		mExitBtn.setText("退出登录");
				  		mChangePassWordItem.setVisibility(View.VISIBLE);
				  	}
				}
			}
		};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		initView();
		onClick();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CommonRes.RefreshUserInfo);
		registerReceiver(mRefreshBroadcastReceiver, intentFilter);
	}

	private void initView() {
		mSettingsTitle = (DiDiTitleView) findViewById(R.id.settings_title);
		mSettingsTitle.setTitle("设置");
		mSettingsTitle.setBack(this);
		mExitBtn = (Button) findViewById(R.id.exit_btn);
		voice_switch = (ToggleButton) findViewById(R.id.voice_switch);
		voice_switch.setChecked(sp.getBoolean("voice_switch",true));
		voice_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				voice_switch.setChecked(isChecked);
				sp.edit().putBoolean("voice_switch",isChecked).commit();
			}
		});

		//修改密码
	    mChangePassWordItem = (MyItemView) findViewById(R.id.change_password_item);
	    mChangePassWordItem.setItemName("修改密码");
	    mChangePassWordItem.getItemIconImage().setVisibility(View.GONE);
		//更换绑定手机
		mChangePhoneItem = (MyItemView)findViewById(R.id.change_phone_item);
		mChangePhoneItem.setItemName("更换绑定手机");
		mChangePhoneItem.getItemIconImage().setVisibility(View.GONE);
	    //运输协议
	    mTransitAgreementItem = (MyItemView) findViewById(R.id.transit_agreement);
	    mTransitAgreementItem.setItemName("运输协议");
	    mTransitAgreementItem.getItemIconImage().setVisibility(View.GONE);
	    //关于我们
	  	mAboutItem = (MyItemView) findViewById(R.id.about_item);
	  	mAboutItem.setItemName("关于我们");
	  	mAboutItem.getItemIconImage().setVisibility(View.GONE);
	    //版本更新
	  	mUpdateItem = (MyItemView) findViewById(R.id.update_version_item);
	  	mUpdateItem.setItemName("版本信息");
	  	mUpdateItem.setContent("当前版本是:"+Util.getVersionNmae(SettingsActivity.this));
	  	mUpdateItem.getItemIconImage().setVisibility(View.GONE);
		if(!DidiApp.isUserAowner){
			mUpdateItem.setVisibility(View.GONE);
		}
	    //意见反馈
	  	mOpinionItem = (MyItemView) findViewById(R.id.opinion_item);
	  	mOpinionItem.setItemName("意见反馈");
	  	mOpinionItem.getItemIconImage().setVisibility(View.GONE);
	  	if(!Util.isLogin(SettingsActivity.this)){
	  		mExitBtn.setText("登录");
	  		mChangePassWordItem.setVisibility(View.GONE);
			mChangePhoneItem.setVisibility(View.GONE);
	  	}
	}

	private void onClick(){
		mChangePassWordItem.setOnClickListener(this);
		mChangePhoneItem.setOnClickListener(this);
		mTransitAgreementItem.setOnClickListener(this);
		mAboutItem.setOnClickListener(this);
		mUpdateItem.setOnClickListener(this);
		mOpinionItem.setOnClickListener(this);
		mExitBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about_item:
			Intent mIntent = new Intent(SettingsActivity.this,WebViewWithTitleActivity.class);
			Bundle mBundle = new Bundle();
			mBundle.putString("title", getResources().getString(R.string.about_us));
			mBundle.putString("url", GloableParams.WEB_URL+"about.html");
			mIntent.putExtras(mBundle);
			startActivity(mIntent);
			break;
		case R.id.change_password_item:
			if(Util.isLogin(SettingsActivity.this)){
				Intent mPasswdIntent = new Intent(SettingsActivity.this,PasswdSettingActivity.class);
				startActivity(mPasswdIntent);
			}else{
				Intent mLoginIntent = new Intent(SettingsActivity.this, LoginActivity.class);
				startActivity(mLoginIntent);
			}
			
			break;
			case R.id.change_phone_item:
				if(Util.isLogin(SettingsActivity.this)){
					Intent mPasswdIntent = new Intent(SettingsActivity.this,PhoneChangeActivity.class);
					startActivity(mPasswdIntent);
				}else{
					Intent mLoginIntent = new Intent(SettingsActivity.this, LoginActivity.class);
					startActivity(mLoginIntent);
				}
				break;
		case R.id.transit_agreement:
			Intent mTransitAgreementIntent = new Intent(SettingsActivity.this,WebViewWithTitleActivity.class);
			Bundle mTransitAgreementBundle = new Bundle();
			mTransitAgreementBundle.putString("title", "运输协议");
			mTransitAgreementBundle.putString("url", GloableParams.WEB_URL + "transportationProtocol.html");
			mTransitAgreementIntent.putExtras(mTransitAgreementBundle);
			startActivity(mTransitAgreementIntent);
			break;
		case R.id.update_version_item:
			break;
		case R.id.opinion_item:
			Intent mSubmitIntent = new Intent(SettingsActivity.this,SubmitSuggestionActivity.class);
			startActivity(mSubmitIntent);
			break;
		case R.id.exit_btn:
			if(Util.isLogin(SettingsActivity.this)){
				exitDailog();
			}else{
				Intent intent = new Intent(SettingsActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
			break;
		default:
			break;
		}
	}
	private void logout(){
		AjaxParams params = new AjaxParams();
//		params.put("userId", sp.getString("userId", ""));
		DidiApp.getHttpManager().sessionPost(SettingsActivity.this, logout_url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				//退出时清除用户数据
				Editor edit = sp.edit();
//				edit.putString("cellphone", "");
				edit.putString("userId", "");
				edit.putString("session_id", "");
				edit.putString("user_role","车主");
				edit.commit();
				//退出时清除极光标记
				JpushUtil mJpushUtil = new JpushUtil();
				mJpushUtil.initJpush(getApplicationContext(), "", CommonRes.UserId);
//				Intent intent = new Intent(SettingsActivity.this,LoginActivity.class);
//				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
				//停止消息线程广播
				Intent stop_intent = new Intent();
				stop_intent.setAction(CommonRes.CometStop);
				sendBroadcast(stop_intent);
				//做刷新使用的广播
				Intent ref_intent = new Intent();
				ref_intent.setAction(CommonRes.RefreshData);
				sendBroadcast(ref_intent);
				//我的界面更新
				Intent userInfo_intent = new Intent();
				userInfo_intent.setAction(CommonRes.RefreshUserInfo);
				sendBroadcast(userInfo_intent);
				sp.edit().putString("user_role","").commit();
				Intent intent_new = new Intent(SettingsActivity.this,MainActivity.class);
				intent_new.putExtra("from","from_driver");
				startActivity(intent_new);
				DidiApp.setUserOwnerRole(true);
				finish();
			}
		});
	}
	private void exitDailog() {
		AlertDialog.Builder dialog = new Builder(SettingsActivity.this);
		dialog.setTitle("退出");
		dialog.setMessage("是否退出当前账号?");
		dialog.setPositiveButton("退出", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//向服务器发送通知退出
				logout();
			}
		});
		dialog.setNegativeButton("取消", null);
		dialog.show();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mRefreshBroadcastReceiver);
	}
}
