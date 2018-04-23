package com.hongshi.wuliudidi.view;

import com.hongshi.wuliudidi.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class ImageItemView extends RelativeLayout {
	private View mView;
	private Context mContext;
	public ImageItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}
	private void init() {
		mView = View.inflate(mContext, R.layout.image_item_view, null); 
		addView(mView);
	}
}
