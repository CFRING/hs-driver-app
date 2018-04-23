package com.hongshi.wuliudidi.activity;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author huiyuan
 */
public class ResultActivity extends Activity implements OnClickListener{
	private DiDiTitleView mCheckTitle;
	//1,代表审核界面；2，代码邀请队员界面
	private int result = 0;
	private TextView mResultText;
	private ImageView mContinueImage,mSeeImage;

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("ResultActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("ResultActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aucction_check_activity);
		mCheckTitle = (DiDiTitleView) findViewById(R.id.auction_check_title);
		mResultText = (TextView) findViewById(R.id.check_text);
		mContinueImage = (ImageView) findViewById(R.id.continue_image);
		mSeeImage = (ImageView) findViewById(R.id.see_image);
		mCheckTitle.setBack(this);
		result = getIntent().getIntExtra("result", 0);
		mContinueImage.setOnClickListener(this);
		mSeeImage.setOnClickListener(this);
		showView(result);
	}

	public void showView(int result){
		switch (result) {
		case 1:
			mCheckTitle.setTitle("接单审核");
			mSeeImage.setVisibility(View.GONE);
			mContinueImage.setImageResource(R.drawable.back_home_style);
			break; 
		case 2:
			mCheckTitle.setTitle("邀请队员");
			mResultText.setText("邀请发送成功，请耐心等待对方通过");
			mContinueImage.setImageResource(R.drawable.continue_to_invite);
			mSeeImage.setImageResource(R.drawable.see_the_players);
			break;
		default:
			break;
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.continue_image:
			if(result == 1){
				finish();
			}else if(result == 2){
				Intent intent = new Intent(ResultActivity.this,InvatePlayerActivity.class);
				startActivity(intent);
				finish();
			}
			break;
		case R.id.see_image:
			if(result == 1){
				Intent intent = new Intent(ResultActivity.this,AllAuctionOrderActivity.class);
				startActivity(intent);
				finish();
			}else if(result == 2){
				Intent intent = new Intent(ResultActivity.this,TruckTeamActivity.class);
				startActivity(intent);
				finish();
			}
			finish();
			break;
		default:
			break;
		}
	}
}
