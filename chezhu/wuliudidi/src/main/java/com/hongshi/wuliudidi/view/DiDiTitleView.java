package com.hongshi.wuliudidi.view;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.LoginActivity;
import com.hongshi.wuliudidi.activity.RegisterActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DiDiTitleView extends RelativeLayout {
	private View mView;
	private ImageView mBackImage,mRightImage;
	private Context mContext;
	private TextView mTitle,mRightText;
	public DiDiTitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}
	private void init() {
		mView = View.inflate(mContext, R.layout.didi_title_view, null); 
		mBackImage = (ImageView) mView.findViewById(R.id.back);
		mTitle = (TextView) mView.findViewById(R.id.title);
		mRightImage = (ImageView) mView.findViewById(R.id.right_image);
		mRightText = (TextView) mView.findViewById(R.id.right_text);
		addView(mView);
	}
	public void setBack(final Activity mActivity){
		mBackImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mActivity.finish();
			}
		});
	}
	public void setBack(final Activity mActivity, final Class<?> activityCls){
		mBackImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(mActivity,activityCls);
				mActivity.startActivity(mIntent);
				mActivity.finish();
			}
		});
	}
	public void hideBack(){
		mBackImage.setVisibility(View.GONE);
	}
	public void setTitle(String name){
		mTitle.setText(name);
	}
	//得到右边的图片按钮并显示
	public ImageView  getRightImage(){
		mRightImage.setVisibility(View.VISIBLE);
		return mRightImage;
	}
	//得到右边textview并显示
	public TextView getRightTextView(){
		mRightText.setVisibility(View.VISIBLE);
		return mRightText;
	}
	
	public ImageView getBackImageView(){
		return mBackImage;
	}
}
