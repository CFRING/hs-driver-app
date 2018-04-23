package com.hongshi.wuliudidi.view;

import com.hongshi.wuliudidi.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PriceItemView extends LinearLayout{
	private View mView;
	private Context mContext;
	private TextView mColor,mSecondColor,mPrice,mSecondPrice,mBidNumber,mSecondNumber;
	private RelativeLayout mSecondLayout;
	public PriceItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}
	private void init() {
		mView = View.inflate(mContext, R.layout.price_item_view, null);
		mColor = (TextView) mView.findViewById(R.id.color_show);
		mSecondColor = (TextView) mView.findViewById(R.id.second_color_show);
		mPrice = (TextView) mView.findViewById(R.id.price);
		mSecondPrice = (TextView) mView.findViewById(R.id.second_price);
		mBidNumber = (TextView) mView.findViewById(R.id.bidnumber);
		mSecondNumber = (TextView) mView.findViewById(R.id.second_bidnumber);
		mSecondLayout = (RelativeLayout) mView.findViewById(R.id.second_layout);
		addView(mView);
	}
	public void hideSecondLayout(){
		mSecondLayout.setVisibility(View.INVISIBLE);
	}
	public void showSecondLayout(){
		mSecondLayout.setVisibility(View.VISIBLE);
	}
	public void setColor(int color){
		mColor.setBackgroundColor(color);
	}
	public void setSecondColor(int color){
		mSecondColor.setBackgroundColor(color);
	}
	public void setPrice(String price,String type,boolean isHidePrice){
		if(isHidePrice){
			mPrice.setText("");
		}else{
			mPrice.setText(price+"元/"+type);
		}
	}
	public void setSecondPrice(String price,String type,boolean isHidePrice){
		if(isHidePrice){
			mSecondPrice.setText("");
		}else{
			mSecondPrice.setText(price+"元/"+type);
		}
	}
	public void setSecondPrice(String text){
		mSecondPrice.setText(text);
	}
	public void setBidNumber(int number){
		mBidNumber.setText(""+number+"人");
	}
	public void setSecondBidNumber(int number){
		mSecondNumber.setText(""+number+"人");
	}
}
