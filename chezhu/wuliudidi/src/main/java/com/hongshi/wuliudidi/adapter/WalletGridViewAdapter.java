package com.hongshi.wuliudidi.adapter;

import java.util.List;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.WalletGridModel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WalletGridViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<WalletGridModel> gridList;
	public WalletGridViewAdapter(Context context,List<WalletGridModel> gridList) {
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
			convertView = View.inflate(mContext, R.layout.wallet_gridview_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mImage = (ImageView) convertView.findViewById(R.id.image);
			viewHolder.mTextView = (TextView) convertView.findViewById(R.id.text);
			viewHolder.mHintImage = (ImageView) convertView.findViewById(R.id.hint_image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mImage.setImageResource(gridList.get(position).getImageId());
		viewHolder.mTextView.setText(gridList.get(position).getText());
		
		if(gridList.get(position).isShowHintImage()){
			viewHolder.mHintImage.setVisibility(View.VISIBLE);
			viewHolder.mHintImage.setImageResource(gridList.get(position).getHintImageId());
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