package com.hongshi.wuliudidi.adapter;

import java.util.List;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.InviteModel;
import com.hongshi.wuliudidi.utils.Util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SystemMessageAdapter extends BaseAdapter {

	private Context mContext;
	private List<InviteModel> mMessageList;
	public SystemMessageAdapter(Context context, List<InviteModel> mMessageList) {
		this.mMessageList = mMessageList;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return mMessageList.size();
	}

	@Override
	public InviteModel getItem(int position) {
		return mMessageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
//		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.message_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.time = (TextView) convertView.findViewById(R.id.time);
			viewHolder.message_content = (TextView) convertView.findViewById(R.id.message_content);
			convertView.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) convertView.getTag();
//		}
		InviteModel messagemodel = mMessageList.get(position);
		viewHolder.time.setText(Util.formatDateSecond(messagemodel.getGmtCreate()));
		viewHolder.message_content.setText(messagemodel.getRealContent());
		return convertView;
	}

	static class ViewHolder {
		TextView time,message_content;

	}
}
