package com.hongshi.wuliudidi.view;

import com.hongshi.wuliudidi.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TaskOrderTitleView extends RelativeLayout {
	private View mView;
	private Context mContext;
    private TextView mTitleText;
    private View mLine;
	public TaskOrderTitleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		init();
	}
	public TaskOrderTitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}
	public TaskOrderTitleView(Context context) {
		super(context);
		this.mContext = context;
		init();
	}

	private void init(){
		mView = View.inflate(mContext, R.layout.task_order_title_view, null);
		mTitleText = (TextView) mView.findViewById(R.id.task_order_text);
		mLine = mView.findViewById(R.id.line_view);
		addView(mView);
	}
	public void setTitleText(String name){
		mTitleText.setText(name);
	}
	public void setTextColor(int color,boolean isShowLine){
		mTitleText.setTextColor(color);
		if(isShowLine){
			mLine.setVisibility(View.VISIBLE);
		}else{
			mLine.setVisibility(View.INVISIBLE);
		}
	}
}
