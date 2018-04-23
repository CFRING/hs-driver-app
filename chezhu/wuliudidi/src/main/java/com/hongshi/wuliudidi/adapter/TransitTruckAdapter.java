package com.hongshi.wuliudidi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.SearchTruckListModel;
import com.hongshi.wuliudidi.model.TransitAmountModel;
import com.hongshi.wuliudidi.utils.Util;

import java.util.List;

public class TransitTruckAdapter extends BaseAdapter {

	private Context mContext;
	private List<TransitAmountModel> mTruckList;

	public TransitTruckAdapter(Context context, List<TransitAmountModel> mTruckList) {
		this.mTruckList = mTruckList;
		this.mContext = context;
	}
	public void addList(List<SearchTruckListModel> mOrderList){
//		this.addList(mOrderList);
	}
	@Override
	public int getCount() {
		return mTruckList.size();
	}

	@Override
	public TransitAmountModel getItem(int position) {
		return mTruckList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.transit_truck_list, null);
			viewHolder = new ViewHolder();
			viewHolder.truckNumber = (TextView) convertView.findViewById(R.id.truck_number);
			viewHolder.amount = (TextView) convertView.findViewById(R.id.amount);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		TransitAmountModel mTransitAmountModel = mTruckList.get(position);
		viewHolder.truckNumber.setText(mTransitAmountModel.getTruckNumber());
		String amount = mTransitAmountModel.getCount();
		String amountStr = "运输次数"+ amount + "次";
		Util.setText(mContext,amountStr,amount,viewHolder.amount,R.color.home_text_press);
		return convertView;
	}

	static class ViewHolder {
		TextView truckNumber,amount;
	}
}
