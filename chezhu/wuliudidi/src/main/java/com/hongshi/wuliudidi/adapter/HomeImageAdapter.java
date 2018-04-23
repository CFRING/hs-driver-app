package com.hongshi.wuliudidi.adapter;


import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.WebViewWithTitleActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import net.tsz.afinal.FinalBitmap;

import java.util.ArrayList;
import java.util.List;

public class HomeImageAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private int[] ids;
	private int size;

	public HomeImageAdapter(Context context, int[] ids) {
		mContext = context;
		this.ids = ids;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		size = ids.length;
	}
	@Override
	public int getCount() {
		// 返回最大值来实现循环
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.image_item, null);
		}

		ImageView image = (ImageView) convertView.findViewById(R.id.imgView);
		image.setImageResource(ids[position % ids.length]);

		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(position % size == 0){
					Intent intent = new Intent(mContext,WebViewWithTitleActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("title", "轮胎供应商");
					bundle.putString("url", "http://www.redlion56.com/my.html");
					intent.putExtras(bundle);
					mContext.startActivity(intent);
				}else if(position % size == 1){
					Intent intent = new Intent(mContext,WebViewWithTitleActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("title", "油品供应商");
					bundle.putString("url", "http://www.redlion56.com/gas-Station.html");
					intent.putExtras(bundle);
					mContext.startActivity(intent);
				}else if(position % size == 3){
					Intent intent = new Intent(mContext,WebViewWithTitleActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("title", "活动说明");
					bundle.putString("url", "http://cz.redlion56.com/app/activities.html");
					intent.putExtras(bundle);
					mContext.startActivity(intent);
				}

			}
		});
		return convertView;
	}

}
