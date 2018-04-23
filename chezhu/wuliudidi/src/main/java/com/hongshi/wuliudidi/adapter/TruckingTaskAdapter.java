package com.hongshi.wuliudidi.adapter;

import java.util.List;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.TruckTaskAssignCallBack;
import com.hongshi.wuliudidi.model.TruckListModel;
import com.hongshi.wuliudidi.utils.LogUtil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class TruckingTaskAdapter extends BaseAdapter{

	private Context mContext;
	private List<TruckListModel> mTruckList;
	private TruckTaskAssignCallBack mTruckTaskAssignCallBack;
	public TruckingTaskAdapter(Context context, List<TruckListModel> mTruckList,TruckTaskAssignCallBack mTruckTaskAssignCallBack) {
		this.mTruckList = mTruckList;
		this.mContext = context;
		this.mTruckTaskAssignCallBack = mTruckTaskAssignCallBack;
	}

	@Override
	public int getCount() {
		return mTruckList.size();
	}

	@Override
	public TruckListModel getItem(int position) {
		return mTruckList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.assign_truck_item, null);
			viewHolder = new ViewHolder();
			viewHolder.choose_truck_checkbox = (CheckBox) convertView.findViewById(R.id.check_box);
			viewHolder.edit = (EditText) convertView.findViewById(R.id.weight_edit);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		TruckListModel mTruckMode = mTruckList.get(position);
		viewHolder.choose_truck_checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){  
                      LogUtil.i("lihe", "重量====="+viewHolder.edit.getText().toString());
                }else{  

                } 
			}
		});
		return convertView;
	}

	static class ViewHolder {
		EditText edit;
		CheckBox choose_truck_checkbox;

	}
}
