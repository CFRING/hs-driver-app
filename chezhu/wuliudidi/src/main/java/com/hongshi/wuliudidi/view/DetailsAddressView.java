package com.hongshi.wuliudidi.view;

import com.hongshi.wuliudidi.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DetailsAddressView extends LinearLayout{

	private View mView;
	private Context mContext;
	private TextView mNumber,mStartCity,mEndCity;
	private LinearLayout mNumberTitleLayout;
	public DetailsAddressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}
	private void init() {
		mView = View.inflate(mContext, R.layout.details_address_view, null);
		mNumber = (TextView) mView.findViewById(R.id.details_number);
		mStartCity = (TextView) mView.findViewById(R.id.start_city_text);
		mEndCity = (TextView) mView.findViewById(R.id.end_city_text);
		mNumberTitleLayout = (LinearLayout) mView.findViewById(R.id.number_title_layout);
		addView(mView);
	}
	public void hideNumber(){
		mNumberTitleLayout.setVisibility(View.GONE);
	}
	public void setNumber(String s){
		mNumber.setText(s);
	}
	public void setStartCity(String startCity){
		mStartCity.setText(startCity);
	}
	public void setEndCity(String endCity){
		mEndCity.setText(endCity);
	}
}
