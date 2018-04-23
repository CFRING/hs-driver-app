package com.hongshi.wuliudidi.view;


import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class AddTruckPhotoItem extends LinearLayout {
	private View mView;
	private Context mContext;
	private TextView itemNameText, hintText;
	private LinearLayout photographLayout;
	private ImageView littlePhoto, largePhoto, itemMarkImage;
	private PhotoItemCallBack callBack;
	private CommonRes.PhotoTag tag;
	
	public AddTruckPhotoItem(Context context){
		super(context);
		this.mContext = context;
		init();
	}
	
	public AddTruckPhotoItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}
	
	private void init(){
		mView = View.inflate(mContext, R.layout.add_truck_photo_item, null);
		inflate(mContext, R.layout.add_truck_photo_item, null);
		itemMarkImage = (ImageView) mView.findViewById(R.id.item_mark_image);
		itemNameText = (TextView) mView.findViewById(R.id.item_name_text);
		hintText = (TextView) mView.findViewById(R.id.photograph_hint_text);
		photographLayout = (LinearLayout) mView.findViewById(R.id.photograph_layout);
		littlePhoto = (ImageView) mView.findViewById(R.id.little_photo_image);
		largePhoto = (ImageView) mView.findViewById(R.id.large_photo_image);
		addView(mView);
		
		photographLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callBack.onPhotoGraphClick(tag);
			}
		});

		largePhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(callBack != null){
					callBack.onPhotoGraphClick(tag);
				}
			}
		});
	}
	
	public ImageView getMarkImage(){
		return itemMarkImage;
	}
	 
	public void setItemName(String itemName){
		itemNameText.setText(itemName);
	}
	
	public void setHint(String hint){
		hintText.setText(hint);
	}

	public void hideHint(){
		photographLayout.setVisibility(View.GONE);
	}
	
	public ImageView getLittlePhoto() {
		littlePhoto.setVisibility(View.VISIBLE);
		largePhoto.setVisibility(View.GONE);
		return littlePhoto;
	}

	public ImageView getLargePhoto() {
		littlePhoto.setVisibility(View.GONE);
		largePhoto.setVisibility(View.VISIBLE);
		return largePhoto;
	}
	
	public void setPhotographOnClick(PhotoItemCallBack callBack, CommonRes.PhotoTag tag){
		this.callBack = callBack;
		this.tag = tag;
	}
	
	public void hidePhoto(){
		photographLayout.setVisibility(View.GONE);
	}
	
	public interface PhotoItemCallBack{
		public void onPhotoGraphClick(CommonRes.PhotoTag tag);
	}
}
