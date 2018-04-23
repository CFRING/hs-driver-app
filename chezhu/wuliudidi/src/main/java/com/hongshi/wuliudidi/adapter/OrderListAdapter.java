package com.hongshi.wuliudidi.adapter;

import java.util.List;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.TaskOrderModel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderListAdapter extends BaseAdapter {

	private Context mContext;
	private List<TaskOrderModel> mOrderList;

	public OrderListAdapter(Context context, List<TaskOrderModel> mOrderList) {
		this.mOrderList = mOrderList;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return mOrderList.size();
	}

	@Override
	public TaskOrderModel getItem(int position) {
		return mOrderList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.task_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.startCity = (TextView) convertView.findViewById(R.id.start_city);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		TaskOrderModel ordermodel = mOrderList.get(position);
		return convertView;
	}

	static class ViewHolder {
		TextView startCity;

	}
}
