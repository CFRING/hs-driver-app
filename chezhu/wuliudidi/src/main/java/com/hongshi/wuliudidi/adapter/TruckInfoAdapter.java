package com.hongshi.wuliudidi.adapter;

import java.util.List;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.AddTruckNewActivity;
import com.hongshi.wuliudidi.model.SearchTruckListModel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TruckInfoAdapter extends BaseAdapter {

	private Context mContext;
	private List<SearchTruckListModel> mOrderList;

	public TruckInfoAdapter(Context context, List<SearchTruckListModel> mOrderList) {
		this.mOrderList = mOrderList;
		this.mContext = context;
	}
	public void addList(List<SearchTruckListModel> mOrderList){
		this.mOrderList.addAll(mOrderList);
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
			viewHolder.detailLayout = (LinearLayout) convertView.findViewById(R.id.detail_text_layout);
			viewHolder.truck_model = (TextView) convertView.findViewById(R.id.truck_model);
			viewHolder.truck_length = (TextView) convertView.findViewById(R.id.truck_length);
			viewHolder.truck_max_weight = (TextView) convertView.findViewById(R.id.truck_max_weight);
			viewHolder.audit_state = (ImageView) convertView.findViewById(R.id.audit_state);
			viewHolder.complete_material = (ImageView) convertView.findViewById(R.id.complete_material);
			viewHolder.forbid_icon_bg = (RelativeLayout) convertView.findViewById(R.id.forbid_icon_bg);
			viewHolder.forbid_icon = (ImageView) convertView.findViewById(R.id.forbid_icon);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final SearchTruckListModel searchTruckListModel = mOrderList.get(position);

		if(searchTruckListModel.isAcceptOrder()){
			viewHolder.forbid_icon_bg.setVisibility(View.GONE);
			viewHolder.forbid_icon.setVisibility(View.GONE);
		}else {
			viewHolder.forbid_icon_bg.setVisibility(View.VISIBLE);
			viewHolder.forbid_icon.setVisibility(View.VISIBLE);
		}

		int state = searchTruckListModel.getStatus();
		viewHolder.truck_model.setText(searchTruckListModel.getTruckTypeText());
		viewHolder.truck_length.setText(String.valueOf(searchTruckListModel.getTruckLength()));
		viewHolder.truck_max_weight.setText(""+searchTruckListModel.getCarryCapacity()+"吨");
		//1，审核中,2，审核未通过，3，审核通过，4，未完善
		if(state == 1){
			viewHolder.licence.setText("审核中");
			viewHolder.audit_state.setImageResource(R.drawable.audit_doing);
			viewHolder.detailLayout.setVisibility(View.GONE);
		}else if(state == 2){
			viewHolder.licence.setText("审核未通过");
			viewHolder.audit_state.setImageResource(R.drawable.audit_no_pass);
			viewHolder.detailLayout.setVisibility(View.GONE);
		}else if(state == 3){
			viewHolder.licence.setText(searchTruckListModel.getTruckNumber());
			viewHolder.audit_state.setImageResource(R.drawable.audit_done);
			viewHolder.detailLayout.setVisibility(View.VISIBLE);
		}else{
			viewHolder.licence.setText("未完善");
			viewHolder.audit_state.setImageResource(R.drawable.unfinished);
			viewHolder.detailLayout.setVisibility(View.GONE);
		}

		viewHolder.complete_material.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, AddTruckNewActivity.class);
				intent.putExtra("pageType","from_complete_material");
				intent.putExtra("truckId","" + searchTruckListModel.getTruckId());
				mContext.startActivity(intent);
			}
		});

		if(searchTruckListModel.isDataComplete()){
			viewHolder.complete_material.setVisibility(View.GONE);
		}else {
			viewHolder.complete_material.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	static class ViewHolder {
		TextView licence,truck_model,truck_length,truck_max_weight;
		ImageView audit_state,complete_material,forbid_icon;
		LinearLayout detailLayout;
		RelativeLayout forbid_icon_bg;
	}
}
