package com.hongshi.wuliudidi.adapter;

import java.util.List;

import com.hongshi.wuliudidi.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChooseSortRuleAdapter extends BaseAdapter {
	private Context mContext;
	private List<String> mList;
	
	public ChooseSortRuleAdapter(Context mContext, List<String> mList){
		this.mContext = mContext;
		this.mList = mList;
	}
	
	@Override
	public int getCount() {
		if(mList == null){
			return 0;
		}else{
			return mList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		if(mList == null){
			return null;
		}else{
			return mList.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.choose_sort_rule_item, null);
			
			viewHolder.choiceNameText = (TextView) convertView.findViewById(R.id.choice_name_text);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.choiceNameText.setText(mList.get(position));
		
		return convertView;
	}

	private static class ViewHolder{
		TextView choiceNameText;
	}
}
