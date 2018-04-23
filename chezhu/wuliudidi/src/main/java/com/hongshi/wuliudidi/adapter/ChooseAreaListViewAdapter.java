package com.hongshi.wuliudidi.adapter;

import java.util.List;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.AreaModel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChooseAreaListViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<AreaModel> mList;
	private int layoutId;
	private int currentItem = -1;
	private AreaType type = AreaType.province;
	
	public ChooseAreaListViewAdapter(Context mContext, List<AreaModel> mList, int layoutId, AreaType t){
		this.mContext = mContext;
		this.mList = mList;
		this.layoutId = layoutId;
		this.type = t;
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
			convertView = View.inflate(mContext, layoutId, null);
			
			viewHolder.areaNameText = (TextView) convertView.findViewById(R.id.area_name);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.areaNameText.setText(mList.get(position).getAreaText());
		if(position == currentItem){
			switch (type) {
			case province:
				convertView.setBackgroundColor(mContext.getResources().getColor(R.color.area_item_light_grey));
				viewHolder.areaNameText.setTextColor(mContext.getResources().getColor(R.color.theme_color));
				break;
			case city:
				convertView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
				viewHolder.areaNameText.setTextColor(mContext.getResources().getColor(R.color.theme_color));
				break;
			case county:
				viewHolder.areaNameText.setTextColor(mContext.getResources().getColor(R.color.theme_color));
				break;
			default:
				break;
			}
		}else{
			switch (type) {
			case province:
				convertView.setBackgroundColor(mContext.getResources().getColor(R.color.area_item_grey));
				viewHolder.areaNameText.setTextColor(mContext.getResources().getColor(R.color.light_grey));
				break;
			case city:
				convertView.setBackgroundColor(mContext.getResources().getColor(R.color.area_item_light_grey));
				viewHolder.areaNameText.setTextColor(mContext.getResources().getColor(R.color.light_grey));
				break;
			case county:
				convertView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
				viewHolder.areaNameText.setTextColor(mContext.getResources().getColor(R.color.light_grey));
				break;
				default:
					break;
			}
		}
		return convertView;
	}

	public void setCurrentItem(int current){
		this.currentItem = current;
	}
	
	private static class ViewHolder{
		TextView areaNameText;
	}
	
	public enum AreaType{
		province, city, county;
	}
}
