package com.hongshi.wuliudidi.view;

import com.hongshi.wuliudidi.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PhotoItemView extends RelativeLayout {
	private View mView;
	private Context mContext;
	private CircleImageView mPhotoImage;
	private TextView mTitle;

	public PhotoItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}

	private void init() {
		mView = View.inflate(mContext, R.layout.photo_item_view, null);
		mTitle = (TextView) mView.findViewById(R.id.photo_name);
		mPhotoImage = (CircleImageView) mView.findViewById(R.id.photo_image);
		mPhotoImage.setRoundCorner(true, 0xFF000000);
		mPhotoImage.setCornerRadius(18);
		addView(mView);
	}

	public ImageView getImage(){
		return mPhotoImage;
	}
	public void setName(String s){
		mTitle.setText(s);
	}

}
