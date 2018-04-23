package com.hongshi.wuliudidi.activity;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.view.ChargeItemView;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author huiyuan
 */
public class NewAccountsItemActivity extends Activity implements OnClickListener{
	private DiDiTitleView mTitle;
	private ChargeItemView mYouFei,mGongZi,mLunTaiFei,mZuLinFei,mTongXingFei,mYunFei;

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("NewAccountsItemActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("NewAccountsItemActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.new_accounts_item_activity);
		mTitle = (DiDiTitleView) findViewById(R.id.title);
		mTitle.setBack(this);
		mTitle.setTitle(getResources().getString(R.string.not_paid_details));
		mYouFei = (ChargeItemView) findViewById(R.id.youfei);
		mGongZi = (ChargeItemView) findViewById(R.id.gongzi);
		mLunTaiFei = (ChargeItemView) findViewById(R.id.luntaifei);
		mZuLinFei = (ChargeItemView) findViewById(R.id.zulin_weixiufei);
		mTongXingFei = (ChargeItemView) findViewById(R.id.tongxingfei);
		mYunFei = (ChargeItemView) findViewById(R.id.yunfei);
		
		mYouFei.setContent("油费");
		mGongZi.setContent("工资");
		mLunTaiFei.setContent("轮胎费");
		mZuLinFei.setContent("租赁费(维修费)");
		mTongXingFei.setContent("通行费");
		mYunFei.setContent("自交个税运费");
		mYouFei.setIcon(R.drawable.youfei);
		mGongZi.setIcon(R.drawable.gongzi);
		mLunTaiFei.setIcon(R.drawable.luntaifei);
		mZuLinFei.setIcon(R.drawable.zulinfei);
		mTongXingFei.setIcon(R.drawable.tongxingfei);
		mYunFei.setIcon(R.drawable.yunfei);
		
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
		
	}
}
