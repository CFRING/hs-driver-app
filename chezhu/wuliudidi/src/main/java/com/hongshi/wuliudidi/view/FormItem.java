package com.hongshi.wuliudidi.view;

import com.hongshi.wuliudidi.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FormItem extends RelativeLayout {

	private View mView;
	private Context mContext;
	private RelativeLayout mItemLayout;
	private TextView mItemName, mItemValue;
	private View mLine;
	private Button mButton;
	
	public FormItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		init();
	}

	public FormItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}
	private void init(){
		mView = View.inflate(mContext, R.layout.form_item_view, null);
		mItemLayout = (RelativeLayout) mView.findViewById(R.id.item_bg);
		mItemName = (TextView) mView.findViewById(R.id.item_name);
		mItemValue = (TextView) mView.findViewById(R.id.item_value);
		mLine = (View) mView.findViewById(R.id.line);
		addView(mView);
	}
	
	public void setItemName(String name){
		mItemName.setText(name);
	}
	
	public void setItemValue(String value){
		mItemValue.setText(value);
	}
	
	public void hideLine(){
		mLine.setVisibility(INVISIBLE);
	}
	
	public void showLine(){
		mLine.setVisibility(VISIBLE);
	}
	
	public Button getButton(Context context){
		mButton = (Button) mView.findViewById(R.id.button);
		mButton.setVisibility(VISIBLE);
		return mButton;
	}
}
