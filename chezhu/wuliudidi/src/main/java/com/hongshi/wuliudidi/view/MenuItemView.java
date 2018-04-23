package com.hongshi.wuliudidi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;

public class MenuItemView extends RelativeLayout {
	private View mView;
	private Context mContext;
	private ImageView mIcon;
	private TextView mItemName;

	public MenuItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}

	private void init() {
		mView = View.inflate(mContext, R.layout.menu_item_view, null);
		mIcon = (ImageView) mView.findViewById(R.id.item_image);
		mItemName = (TextView) mView.findViewById(R.id.item_name);
		addView(mView);
	}

	//设置item的名字
	public void setItemName(String title){
		mItemName.setText(title);
		}

	//设置item的icon
	public void setItemIcon(int resource){
		mIcon.setImageResource(resource);
	}

}
