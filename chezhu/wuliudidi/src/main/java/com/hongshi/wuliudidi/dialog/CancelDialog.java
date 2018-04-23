package com.hongshi.wuliudidi.dialog;

import net.tsz.afinal.http.AjaxParams;

import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.DriverMainActivity;
import com.hongshi.wuliudidi.activity.LoginActivity;
import com.hongshi.wuliudidi.activity.MainActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class CancelDialog extends Dialog implements OnClickListener{
	private Context mContext;
	private TextView mHintContent, mLeft, mRight;
	private Handler mHandler;
	private String contentHint;
	private int msgCode;
	private boolean isReLogin = false;
	AjaxParams params = new AjaxParams();
	public CancelDialog(Context context) {
		super(context);
		this.mContext = context;
		init();
	}
	public CancelDialog(Context context, int theme, Handler mHandler) {
		super(context, theme);
		this.mContext = context;
		this.mHandler = mHandler;
		init();
	}
	
	public CancelDialog(Context context, int theme, boolean isReLogin){
		super(context, theme);
		this.mContext = context;
		this.isReLogin = isReLogin;
		init();
	}
	
	private void init(){
		setContentView(R.layout.cancel_dialog);
		mHintContent = (TextView) findViewById(R.id.hint_content);
		mLeft = (TextView) findViewById(R.id.left);
		mRight = (TextView) findViewById(R.id.right);
		mLeft.setOnClickListener(this);
		mRight.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.left:
				if("重新登陆".equals(mRight.getText())){
					if(DidiApp.isUserAowner){
						MainActivity.mPager.setCurrentItem(2,true);
					}else {
						DriverMainActivity.mPager.setCurrentItem(0,true);
					}
					//刷新用户信息
					Intent userInfo_intent = new Intent();
					userInfo_intent.setAction(CommonRes.RefreshUserInfo);
					mContext.sendBroadcast(userInfo_intent);
				}
				dismiss();
				break;
			case R.id.right:
				if(isReLogin){
					Intent login_intent = new Intent(mContext,LoginActivity.class);
	    			mContext.startActivity(login_intent);
				}else if(mHandler != null){
					mHandler.sendEmptyMessage(msgCode);
				}
				dismiss();
				break;
			default:
				break;
		}
	}

	public void setHint(String contentHint){
		this.contentHint = contentHint;
		mHintContent.setText(contentHint);
	}
	
	public void setLeftText(String text){
		if(mLeft != null){
			mLeft.setText(text);
		}
	}
	
	public void setRightText(String text){
		if(mRight != null){
			mRight.setText(text);
		}
	}

	public TextView getRightView(){
		return mRight;
	}
	public void setMsgCode(int code){
		this.msgCode = code;
	}
}
