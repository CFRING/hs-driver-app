package com.hongshi.wuliudidi.view;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.LoginActivity;
import com.hongshi.wuliudidi.activity.RegisterActivity;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class NoLoginView extends LinearLayout {
	private View mView;
	private Context mContext;
	private Button mLoginBtn,mRegisterBtn;
	public NoLoginView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}

	private void init() {
		mView = View.inflate(mContext, R.layout.no_login_layout, null); 
		mLoginBtn = (Button) mView.findViewById(R.id.sign_btn);
		mRegisterBtn = (Button) mView.findViewById(R.id.register_btn);
		setLogin();
		setRegist();
		addView(mView);
	}
	public void setLogin(){
		mLoginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,LoginActivity.class);
				mContext.startActivity(intent);
			}
		});
	}
	public void setRegist(){
		mRegisterBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,RegisterActivity.class);
				mContext.startActivity(intent);
			}
		});
	}
}
