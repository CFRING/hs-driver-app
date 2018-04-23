package com.hongshi.wuliudidi.adapter;

import java.util.List;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.TruckTypeGridModel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TruckTypeGridViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<TruckTypeGridModel> typeList;
	public TruckTypeGridViewAdapter(Context context, List<TruckTypeGridModel> typeList) {
		this.mContext = context;
		this.typeList = typeList;
	}
	
	@Override
	public int getCount() {
		return typeList.size();
	}

	@Override
	public Object getItem(int position) {
		if(position < 0 || position >= typeList.size()){
			return null;
		}else{
			return typeList.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.truck_type_grid_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.mImage = (ImageView) convertView.findViewById(R.id.truck_type_image);
			viewHolder.mNameText = (TextView) convertView.findViewById(R.id.type_name_text);
			viewHolder.mSelectImage = (ImageView) convertView.findViewById(R.id.selected_image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mImage.setImageResource(typeList.get(position).getDrawableId());
		viewHolder.mNameText.setText(typeList.get(position).getTypeName());
		
		if(typeList.get(position).isSelected()){
			viewHolder.mSelectImage.setVisibility(View.VISIBLE);
		}else{
			viewHolder.mSelectImage.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}
	
	static class ViewHolder {
		ImageView mImage, mSelectImage;
		TextView mNameText;
	}

}
