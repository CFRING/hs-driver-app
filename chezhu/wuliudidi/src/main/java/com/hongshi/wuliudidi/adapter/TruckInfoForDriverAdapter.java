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

import java.util.List;

public class TruckInfoForDriverAdapter extends BaseAdapter {

    private Context mContext;
    private List<SearchTruckListModel> mOrderList;

    public TruckInfoForDriverAdapter(Context context, List<SearchTruckListModel> mOrderList) {
        this.mOrderList = mOrderList;
        this.mContext = context;
    }

    public void addList(List<SearchTruckListModel> mOrderList) {
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
            convertView = View.inflate(mContext, R.layout.truck_list_item_fordriver, null);
            viewHolder = new ViewHolder();
            viewHolder.licence = (TextView) convertView.findViewById(R.id.licence);
            viewHolder.detailLayout = (LinearLayout) convertView.findViewById(R.id.detail_text_layout);
            viewHolder.truck_model = (TextView) convertView.findViewById(R.id.truck_model);
            viewHolder.truckBoxTV = (TextView) convertView.findViewById(R.id.truck_box);
            viewHolder.truck_length = (TextView) convertView.findViewById(R.id.truck_length);
            viewHolder.truck_max_weight = (TextView) convertView.findViewById(R.id.truck_max_weight);
            viewHolder.truckMaxVTV = (TextView) convertView.findViewById(R.id.truck_max_v);
//			viewHolder.audit_state = (ImageView) convertView.findViewById(R.id.audit_state);
            viewHolder.driverNumTV = (TextView) convertView.findViewById(R.id.driver_num_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SearchTruckListModel searchTruckListModel = mOrderList.get(position);
        int state = searchTruckListModel.getStatus();
        viewHolder.licence.setText(searchTruckListModel.getTruckNumber());
        viewHolder.truck_model.setText(searchTruckListModel.getTruckTypeText());
        viewHolder.truck_length.setText(searchTruckListModel.getTruckLengthText());
        viewHolder.truckBoxTV.setText(searchTruckListModel.getTruckCarriageText());
        viewHolder.truck_max_weight.setText(searchTruckListModel.getCarryCapacity() + searchTruckListModel.getCarryCapacityUnitText());
        if (searchTruckListModel.getCarryVolume() > 0) {
            viewHolder.truckMaxVTV.setText(searchTruckListModel.getCarryVolume() + searchTruckListModel.getCarryVolumeUnitText());
        } else {
            viewHolder.truckMaxVTV.setText("");
        }
        viewHolder.driverNumTV.setText("关联司机 " + searchTruckListModel.getBundDriverCount() + " 人");
        //1，审核中,2，审核未通过，3，审核通过，4，未完善
//		if(state == 1){
//			viewHolder.licence.setText("审核中");
//			viewHolder.audit_state.setImageResource(R.drawable.audit_doing);
//			viewHolder.detailLayout.setVisibility(View.GONE);
//		}else if(state == 2){
//			viewHolder.licence.setText("审核未通过");
//			viewHolder.audit_state.setImageResource(R.drawable.audit_no_pass);
//			viewHolder.detailLayout.setVisibility(View.GONE);
//		}else if(state == 3){
//			viewHolder.licence.setText(searchTruckListModel.getTruckNumber());
//			viewHolder.audit_state.setImageResource(R.drawable.audit_done);
//			viewHolder.detailLayout.setVisibility(View.VISIBLE);
//		}else{
//			viewHolder.licence.setText("未完善");
//			viewHolder.audit_state.setImageResource(R.drawable.unfinished);
//			viewHolder.detailLayout.setVisibility(View.GONE);
//		}
        return convertView;
    }

    static class ViewHolder {
        TextView licence, truck_model, truckBoxTV, truck_length, truck_max_weight, truckMaxVTV, driverNumTV;
        LinearLayout detailLayout;
    }
}
