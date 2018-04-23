package com.hongshi.wuliudidi.activity;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.view.AuctionItem;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * @author huiyuan
 */
public class TruckSpaceActivity extends Activity {
	private DiDiTitleView mTitle;
	private AuctionItem mTruckLengthItem,mTruckWidthItem,mTruckHeightItem,mTruckAreaItem;
	private Button mSureBtn;

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("TruckSpaceActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("TruckSpaceActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.truck_space_activity);
		mTitle = (DiDiTitleView) findViewById(R.id.truck_space_title);
		mTitle.setTitle("载货空间");
		mTitle.setBack(this);
		mTruckLengthItem = (AuctionItem) findViewById(R.id.truck_length);
		mTruckWidthItem = (AuctionItem) findViewById(R.id.truck_width);
		mTruckHeightItem = (AuctionItem) findViewById(R.id.truck_height);
		mTruckAreaItem = (AuctionItem) findViewById(R.id.truck_area);
		mSureBtn = (Button) findViewById(R.id.ok_btn);
		mTruckLengthItem.setName("内长(m)");
		mTruckWidthItem.setName("内宽(m)");
		mTruckHeightItem.setName("内高(m)");
		mTruckAreaItem.setName("体积(m³)");
		mTruckLengthItem.setHint("请输入内长");
		mTruckWidthItem.setHint("请输入内宽");
		mTruckHeightItem.setHint("请输入内高");
		mSureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if(!mTruckLengthItem.getEditContent().equals("")){
					
				}
				if(!mTruckWidthItem.getEditContent().equals("")){
					
				}
				if(!mTruckHeightItem.getEditContent().equals("")){
					
				}
				
			}
		});
	}
}
