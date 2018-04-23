package com.hongshi.wuliudidi.view;

import com.hongshi.wuliudidi.R;

import android.content.Context;
import android.text.Editable;
import android.text.method.KeyListener;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AuctionItem extends RelativeLayout{

	private View mView;
	private Context mContext;
	private TextView mTitle,mContent;
	private RelativeLayout mItemLayout;
	private ImageView mRightImage;
	private EditText mContentEdit;
	private Button mRightButton;
	public AuctionItem(Context context){
		super(context);
		this.mContext = context;
		init();
	}
	public AuctionItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}
	private void init() {
		mView = View.inflate(mContext, R.layout.auction_item_view, null);
		mItemLayout = (RelativeLayout) mView.findViewById(R.id.item_bg);
		mTitle = (TextView) mView.findViewById(R.id.name);
		mContent = (TextView) mView.findViewById(R.id.content);
		mRightImage = (ImageView) mView.findViewById(R.id.my_right_icon);
		mContentEdit = (EditText) mView.findViewById(R.id.content_edit);
		mRightButton = (Button) mView.findViewById(R.id.right_btn);
		addView(mView);
	}
	public void setHint(String content){
		mContent.setVisibility(View.GONE);
		mContentEdit.setVisibility(View.VISIBLE);
		mContentEdit.setHint(content);
	}
	public void setBackground(int id){
		mItemLayout.setBackgroundResource(id);
	}
	public void setName(String name){
		mTitle.setText(name);
	}
	public void setNameColor(int color){
		mTitle.setTextColor(color);
	}
	//设置textview的内容
	public void setContent(String content){
		mContent.setVisibility(View.VISIBLE);
		mContentEdit.setVisibility(View.GONE);
		mContent.setText(content);
	}
	//设置Edit的内容
	public void setEditContent(String content){
		mContentEdit.setVisibility(View.VISIBLE);
		mContentEdit.setText(content);
	}
	//设置textview的颜色
	public void setContentColor(int color){
		mContent.setTextColor(color);
	}
	//获取Edit的内容
	public String getEditContent(){
		return mContentEdit.getText().toString();
	}
	public void setHinttColor(int color){
		mContentEdit.setHintTextColor(color);
	}
	public void setContentHide(){
		mContent.setVisibility(View.GONE);
	}
	public ImageView getRightImage(){
		mRightImage.setVisibility(View.VISIBLE);
		return mRightImage;
	}
	
	public EditText getContentEdit(){
		mContentEdit.setVisibility(View.VISIBLE);
		return mContentEdit;
	}
	
	public Button getRightBtn(){
		mRightButton.setVisibility(View.VISIBLE);
		return mRightButton;
	}
	
	public void setInputLimit(final String limitStr){
		
		mContentEdit.setKeyListener(new NumberKeyListener() {
			
			@Override
			public int getInputType() {
				return android.text.InputType.TYPE_CLASS_NUMBER;
			}
			
			@Override
			protected char[] getAcceptedChars() {
				char[] limitArray = limitStr.toCharArray();
				return limitArray;
			}
		});
	}
}
