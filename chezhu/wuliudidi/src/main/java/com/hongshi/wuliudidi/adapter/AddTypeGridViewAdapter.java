package com.hongshi.wuliudidi.adapter;

import com.hongshi.wuliudidi.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AddTypeGridViewAdapter extends BaseAdapter {
	private Context mContext;
	private String[] data = null;

	public AddTypeGridViewAdapter(Context context,String[] data) {
		mContext = context;
		this.data = data;
	}

	public void setData(String[] data){
		this.data = data;
	}
	@Override
	public int getCount() {
		return data.length;
	}

	@Override
	public Object getItem(int position) {
		return data[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.gridview_item,null);
			viewHolder = new ViewHolder();
			viewHolder.mContent = (TextView) convertView.findViewById(R.id.content);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mContent.setText(data[position]);
		return convertView;
	}
	
	static class ViewHolder {
		TextView mContent;
	}
}
