package com.hongshi.wuliudidi.adapter;

import java.util.List;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.MyGridModel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyGridViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<MyGridModel> gridList;
	public MyGridViewAdapter(Context context,List<MyGridModel> gridList) {
		this.mContext = context;
		this.gridList = gridList;
	}
	
	@Override
	public int getCount() {
		return gridList.size();
	}

	@Override
	public Object getItem(int position) {
		if(position < 0 || position >= gridList.size()){
			return null;
		}else{
			return gridList.get(position);
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
			convertView = View.inflate(mContext, R.layout.my_gridview_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mImage = (ImageView) convertView.findViewById(R.id.image);
			viewHolder.mTextView = (TextView) convertView.findViewById(R.id.text);
			viewHolder.mHintImage = (ImageView) convertView.findViewById(R.id.auth_flage_image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mImage.setImageResource(gridList.get(position).getImageId());
		viewHolder.mTextView.setText(gridList.get(position).getText());
		
		if(gridList.get(position).getFlag() >= 0){
			viewHolder.mHintImage.setVisibility(View.VISIBLE);
			switch (gridList.get(position).getFlag()) {
			case 0:
			case 1:
				//审核中
				viewHolder.mHintImage.setImageResource(R.drawable.auth_verifying);
				break;
			case 2:
				//不通过
			case 4:
				//待完善
			case 5:
				//已过期
				viewHolder.mHintImage.setImageResource(R.drawable.auth_wait_complete);
				break;
			case 3:
				//已通过
				viewHolder.mHintImage.setImageResource(R.drawable.auth_complete);
				break;
			case 100:
				//未提交
				viewHolder.mHintImage.setImageResource(R.drawable.auth_none);
				break;
			default:
				break;
			}
		}else{
			viewHolder.mHintImage.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}
	
	static class ViewHolder {
		ImageView mImage,mHintImage;
		TextView mTextView;
	}

}
