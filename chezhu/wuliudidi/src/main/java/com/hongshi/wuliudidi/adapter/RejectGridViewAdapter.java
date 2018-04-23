package com.hongshi.wuliudidi.adapter;

import java.util.List;

import com.hongshi.wuliudidi.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RejectGridViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<String> mList;
	public RejectGridViewAdapter(Context context,List<String> mList) {
		this.mContext = context;
		this.mList = mList;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public String getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.reject_gridview_item,null);
			viewHolder = new ViewHolder();
			viewHolder.mText = (TextView) convertView.findViewById(R.id.reject_text);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mText.setText(mList.get(position));
		return convertView;
	}
	
	static class ViewHolder {
		TextView mText;
	}
}
