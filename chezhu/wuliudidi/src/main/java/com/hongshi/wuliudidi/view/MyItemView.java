package com.hongshi.wuliudidi.view;

import com.hongshi.wuliudidi.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyItemView extends RelativeLayout {
	private View mView;
	private Context mContext;
	private LinearLayout mAuthLayout,mMessageLayout;
	private ImageView oneImage,twoImage,threeImage,rightIcon,mItemIcon;
	private TextView mTitle,mCenterContent,mLeftContent;

	public MyItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}

	private void init() {
		mView = View.inflate(mContext, R.layout.my_item_view, null);
		mTitle = (TextView) mView.findViewById(R.id.my_item_title);
		mCenterContent = (TextView) mView.findViewById(R.id.content);
		mLeftContent = (TextView) mView.findViewById(R.id.my_item_content);
		mAuthLayout = (LinearLayout) mView.findViewById(R.id.authentication_info_layout);
		mMessageLayout = (LinearLayout) mView.findViewById(R.id.my_message_layout);
		oneImage = (ImageView) mView.findViewById(R.id.one_image);
		twoImage = (ImageView) mView.findViewById(R.id.two_image);
		threeImage = (ImageView) mView.findViewById(R.id.three_image);
		rightIcon = (ImageView) mView.findViewById(R.id.my_right_icon);
		mItemIcon = (ImageView) mView.findViewById(R.id.my_item_icon);
		addView(mView);
	}

	//设置item的名字
	public void setItemName(String title){
		mTitle.setText(title);
		}
	//设置居中内容的名字
	public void setContent(String content){
		mCenterContent.setText(content);
		mCenterContent.setVisibility(View.VISIBLE);
	}
	//设置靠左内容的名字
	public void setLeftContent(String content){
		mLeftContent.setText(content);
		mLeftContent.setVisibility(View.VISIBLE);
	}
	//认证信息显示
	public void setAuthVisible() {
		mAuthLayout.setVisibility(View.VISIBLE);
	}
	//item通知消息显示
	public void setMessageVisible() {
		mMessageLayout.setVisibility(View.VISIBLE);
	}

	//设置item的icon
	public void setItemIcon(int resource){
		mItemIcon.setImageResource(resource);
	}
	
	public ImageView getItemIconImage(){
		return mItemIcon;
	}
	
	public ImageView getAuthImage(int pos) {
		switch (pos) {
		case 1:
			return oneImage;
		case 2:
			return twoImage;
		case 3:
			return threeImage;
		case 4:
			return rightIcon;
		default:
			break;
		}
		return null;
	}
}
