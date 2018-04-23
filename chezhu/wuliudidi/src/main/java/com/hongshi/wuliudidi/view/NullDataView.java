package com.hongshi.wuliudidi.view;

import com.hongshi.wuliudidi.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NullDataView extends LinearLayout{

	private View mView;
	private Context mContext;
	private TextView mInfoHint,mInfo;
	private Button mInfoImage;
	private ImageView dataErrorIamge, dataNoneImage;
	public NullDataView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}
	private void init() {
		mView = View.inflate(mContext, R.layout.null_data_view, null);
		dataErrorIamge = (ImageView) mView.findViewById(R.id.data_error_image);
		dataNoneImage = (ImageView) mView.findViewById(R.id.data_none_image);
		mInfoHint = (TextView) mView.findViewById(R.id.info_hint);
		mInfo = (TextView) mView.findViewById(R.id.info_);
		mInfoImage = (Button) mView.findViewById(R.id.info_image);
		addView(mView);
	}
	
	/**
	 * 后台报错误信息
	 */
	public void showErrorView(){
		dataErrorIamge.setVisibility(View.VISIBLE);
		dataNoneImage.setVisibility(View.GONE);
	}
	
	/**
	 * 后台返回空数据
	 */
	public void showNoneView(){
		dataErrorIamge.setVisibility(View.GONE);
		dataNoneImage.setVisibility(View.VISIBLE);
	}
	
	public void setInfoHint(String hint){
		mInfoHint.setText(hint);
	}
	public void setInfo(String info){
		mInfo.setText(info);
	}
	public void setInfoImageResource(int id){
		mInfoImage.setBackgroundResource(id);
		mInfoImage.setVisibility(View.VISIBLE);
	}
	public Button getInfoImage(){
		return mInfoImage;
	}
}
