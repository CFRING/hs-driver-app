package com.hongshi.wuliudidi.view;

import com.hongshi.wuliudidi.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChargeItemView extends RelativeLayout {
	private View mView;
	private Context mContext;
	private ImageView mItemIcon;
	private TextView mItemContent,mCharge;
	public ChargeItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}

	private void init() {
		mView = View.inflate(mContext, R.layout.charge_item_view, null);
		mItemIcon = (ImageView) mView.findViewById(R.id.item_icon);
		mItemContent = (TextView) mView.findViewById(R.id.item_content);
		mCharge = (TextView) mView.findViewById(R.id.item_price);
		addView(mView);
	}

	public void setContent(String s){
		mItemContent.setText(s);
	}
	public void setCharge(String charge){
		mCharge.setText(charge);
	}
	public void setIcon(int id){
		mItemIcon.setImageResource(id);
	}
	// 设置item的icon
	public void setItemIcon(int resource) {
		mItemIcon.setImageResource(resource);
	}
}
