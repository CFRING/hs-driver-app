package com.hongshi.wuliudidi.adapter;

import java.util.List;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.TruckListModel;
import com.hongshi.wuliudidi.view.AuctionItem;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SeeTruckdapter extends BaseAdapter {

	private Context mContext;
	private List<TruckListModel> mList;

	public SeeTruckdapter(Context context, List<TruckListModel> mList) {
		this.mList = mList;
		this.mContext = context;
	}
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public TruckListModel getItem(int position) {
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
			convertView = View.inflate(mContext, R.layout.see_truck_item, null);
			viewHolder = new ViewHolder();
			viewHolder.truck_number = (AuctionItem) convertView.findViewById(R.id.truck_number);
			viewHolder.truck_amount = (AuctionItem) convertView.findViewById(R.id.truck_amount);
			viewHolder.phone = (AuctionItem) convertView.findViewById(R.id.phone);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		TruckListModel mTruckListModel = mList.get(position);
		if(null != mTruckListModel.getUnit() && mTruckListModel.getUnit().equals("TRUCK")){
			viewHolder.truck_amount.setVisibility(View.GONE);
		}else{
			viewHolder.truck_amount.setName("车载运量");
			viewHolder.truck_amount.setContent(mTruckListModel.getFinishedAmount()+mTruckListModel.getUnitText());
		}
		viewHolder.truck_number.setName(mTruckListModel.getNumber());
		viewHolder.truck_number.setContent(mTruckListModel.getTypeText()+"  "+mTruckListModel.getLengthText()
				+"  "+mTruckListModel.getCapacity() + "吨");
		viewHolder.phone.setName("司机号码");
		viewHolder.phone.setContent(mTruckListModel.getCellphone());
		return convertView;
	}

	static class ViewHolder {
		AuctionItem truck_number,truck_amount,phone;

	}
}
