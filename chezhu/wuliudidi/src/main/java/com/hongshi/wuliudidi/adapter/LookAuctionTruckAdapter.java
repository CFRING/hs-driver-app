package com.hongshi.wuliudidi.adapter;

import java.util.List;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.AuctionTrucksModel;
import com.hongshi.wuliudidi.utils.Util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LookAuctionTruckAdapter extends BaseAdapter {

	private Context mContext;
	private List<AuctionTrucksModel> mTruckList;

	public LookAuctionTruckAdapter(Context context, List<AuctionTrucksModel> mTruckList) {
		this.mTruckList = mTruckList;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return mTruckList.size();
	}

	@Override
	public AuctionTrucksModel getItem(int position) {
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
			convertView = View.inflate(mContext, R.layout.look_auction_truck_item, null);
			viewHolder = new ViewHolder();
			viewHolder.truckNumber = (TextView) convertView.findViewById(R.id.truck_number);
			viewHolder.truckInfo = (TextView) convertView.findViewById(R.id.truck_info);
			viewHolder.finishNumber = (TextView) convertView.findViewById(R.id.finish_number);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		AuctionTrucksModel mAuctionTrucksModel = mTruckList.get(position);
		viewHolder.truckNumber.setText(mAuctionTrucksModel.getNumber());
		String truckInfoStr = "";
		if(mAuctionTrucksModel.getTypeText() != null && !mAuctionTrucksModel.getTypeText().equals("")){
			truckInfoStr += mAuctionTrucksModel.getTypeText() + "  ";
		}
		if(mAuctionTrucksModel.getCarriageText() != null &&
				!mAuctionTrucksModel.getCarriageText().equals(mContext.getResources().getString(R.string.unlimited))
				&& !mAuctionTrucksModel.getCarriageText().equals("")){
			truckInfoStr += mAuctionTrucksModel.getCarriageText() + "  ";
		}
		if(mAuctionTrucksModel.getLengthText() != null && !mAuctionTrucksModel.getLengthText().equals("")
				&& !mAuctionTrucksModel.getLengthText().equals(mContext.getResources().getString(R.string.unlimited))){
			truckInfoStr += mAuctionTrucksModel.getLengthText() + "  ";
		}
		if(mAuctionTrucksModel.getCapacity() > 0){
			truckInfoStr += Util.formatDoubleToString(mAuctionTrucksModel.getCapacity(), "吨") + "吨" + "  ";
		}
		if(mAuctionTrucksModel.getCarryVolume() > 0){
			truckInfoStr += " " + Util.formatDoubleToString(mAuctionTrucksModel.getCarryVolume(), "立方米") + "立方米";
		}
		viewHolder.truckInfo.setText(truckInfoStr);
		viewHolder.finishNumber.setText(Util.formatDoubleToString(mAuctionTrucksModel.getFinishedAmount(), mAuctionTrucksModel.getUnitText()));
		viewHolder.truckNumber.setText(mAuctionTrucksModel.getNumber());
		return convertView;
	}

	static class ViewHolder {

		TextView truckNumber,truckInfo,finishNumber;
	}
}
