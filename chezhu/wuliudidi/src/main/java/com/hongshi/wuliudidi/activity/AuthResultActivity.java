package com.hongshi.wuliudidi.activity;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.utils.ActivityManager;
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
public class AuthResultActivity extends Activity implements OnClickListener{
	private ImageView mFinishImage;
	private DiDiTitleView mTitle;
	private TextView checkText, checkTextHint;
	private String tagFrom = "";

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("AuthResultActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("AuthResultActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.auth_result_activity);
		initViews();
	}

	private void initViews(){
		mFinishImage = (ImageView) findViewById(R.id.continue_image);
		mTitle = (DiDiTitleView) findViewById(R.id.title);
		mTitle.setTitle("等待审核");
		mTitle.hideBack();

		try {
			tagFrom = getIntent().getExtras().getString("from");
		} catch (Exception e) {
			tagFrom = "";
		}

		if(tagFrom != null && tagFrom.equals("truck")){
			TextView mTextView = mTitle.getRightTextView();
			mTextView.setText(R.string.finish);
			mTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			mFinishImage.setImageResource(R.drawable.continue_add_button_style);
		}else if(tagFrom != null && tagFrom.equals("bid")){
			checkText = (TextView) findViewById(R.id.check_text);
			checkText.setText(getResources().getString(R.string.submit_success) + "，"
					+ getResources().getString(R.string.wait_for_confirming));
			checkTextHint = (TextView) findViewById(R.id.check_text_hint);
			checkTextHint.setVisibility(View.GONE);
			mFinishImage.setImageResource(R.drawable.return_to_winbid_style);
		}

		mFinishImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.continue_image:
			if(tagFrom.equals("truck")){
				Intent intent = new Intent(AuthResultActivity.this,AddTruckNewActivity.class);
				intent.putExtra("add_new_truck",true);
				startActivity(intent);
			}else if(tagFrom.equals("bid")){
				Intent intent = new Intent(AuthResultActivity.this,WinBidActivity.class);
				startActivity(intent);
			}
			finish();
			break;
		default:
			break;
		}
		
	}
}
