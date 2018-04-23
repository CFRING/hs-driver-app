package com.hongshi.wuliudidi.adapter;

import java.util.List;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.SearchTruckListModel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BindTruckListAdapter extends BaseAdapter {

	private Context mContext;
	private List<SearchTruckListModel> mOrderList;

	public BindTruckListAdapter(Context context, List<SearchTruckListModel> mOrderList) {
		this.mOrderList = mOrderList;
		this.mContext = context;
	}
	@Override
	public int getCount() {
		return mOrderList.size();
	}

	@Override
	public SearchTruckListModel getItem(int position) {
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
			convertView = View.inflate(mContext, R.layout.truck_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.licence = (TextView) convertView.findViewById(R.id.licence);
			viewHolder.truck_model = (TextView) convertView.findViewById(R.id.truck_model);
			viewHolder.truck_length = (TextView) convertView.findViewById(R.id.truck_length);
			viewHolder.truck_max_weight = (TextView) convertView.findViewById(R.id.truck_max_weight);
			viewHolder.audit_state = (ImageView) convertView.findViewById(R.id.audit_state);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		SearchTruckListModel searchTruckListModel = mOrderList.get(position);
		int state = searchTruckListModel.getStatus();
		viewHolder.licence.setText(searchTruckListModel.getTruckNumber());
		viewHolder.truck_model.setText(searchTruckListModel.getTruckTypeText());
		viewHolder.truck_length.setText(searchTruckListModel.getTruckLengthText());
		viewHolder.truck_max_weight.setText(""+searchTruckListModel.getCarryCapacity()+"吨");
		//1，审核中,2，审核未通过，3，审核通过，4，未完善
		if(state == 1){
			viewHolder.audit_state.setImageResource(R.drawable.audit_doing);
		}else if(state == 2){
			viewHolder.audit_state.setImageResource(R.drawable.audit_no_pass);
		}else if(state == 3){
			viewHolder.audit_state.setImageResource(R.drawable.audit_done);
		}else if(state == 4){
			viewHolder.audit_state.setImageResource(R.drawable.audit_no_pass);
		}
		viewHolder.audit_state.setVisibility(View.GONE);
		return convertView;
	}

	static class ViewHolder {

		TextView licence,truck_model,truck_length,truck_max_weight;
		ImageView audit_state;
	}
}
