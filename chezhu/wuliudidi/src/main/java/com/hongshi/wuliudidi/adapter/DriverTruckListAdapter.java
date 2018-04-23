package com.hongshi.wuliudidi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.TruckIdCallBack;
import com.hongshi.wuliudidi.model.AuctionOfferModel;
import com.hongshi.wuliudidi.model.TruckAuthAppVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huiyuan on 2017/3/29.
 */

public class DriverTruckListAdapter extends BaseAdapter {
    private Context mContext;
    private List<TruckAuthAppVO> mTruckList;
    private TruckIdCallBack mTruckIdCallBack;
    boolean isSelect;
    boolean isNoneSelect;
    //多个报备车辆的ID数组
    private List<String> mTruckIdsList = new ArrayList<String>();
    public DriverTruckListAdapter(Context context, List<String> mTruckIdsList,
                                  List<TruckAuthAppVO> mTruckList, TruckIdCallBack mTruckIdCallBack) {
        this.mTruckList = mTruckList;
        this.mContext = context;
        this.mTruckIdCallBack = mTruckIdCallBack;
        this.mTruckIdsList = mTruckIdsList;
    }

    public void setSelect(boolean isSelect){
        this.isSelect = isSelect;
    }
    public void setNoneSelect(boolean isNoneSelect){
        this.isNoneSelect = isNoneSelect;
    }
    @Override
    public int getCount() {
        return mTruckList.size();
    }

    @Override
    public TruckAuthAppVO getItem(int position) {
        return mTruckList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final DriverTruckListAdapter.ViewHolder viewHolder;
        convertView = View.inflate(mContext, R.layout.choose_truck_item, null);
        viewHolder = new DriverTruckListAdapter.ViewHolder();
        viewHolder.truckNumberText = (TextView) convertView.findViewById(R.id.truck_number_text);
        viewHolder.truckTypeCarriageText = (TextView) convertView.findViewById(R.id.truck_type_carriage_text);
        viewHolder.truckLoadSpaceText = (TextView) convertView.findViewById(R.id.truck_load_space_text);
        viewHolder.chooseTruckCheckbox = (CheckBox) convertView.findViewById(R.id.choose_truck_checkbox);
        convertView.setTag(viewHolder);

        TruckAuthAppVO mTruckMode = mTruckList.get(position);
        viewHolder.truckNumberText.setText(mTruckMode.getTruckNumber());

        String truckTypeCarriageStr = "", truckLoadSpaceStr = "";
        if(mTruckMode.getTruckTypeText()!= null){
            //车辆类型
            truckTypeCarriageStr += mTruckMode.getTruckTypeText();
        }
        if(mTruckMode.getTruckCarriageText() != null){
            //车厢类型
            truckTypeCarriageStr += " " + mTruckMode.getTruckCarriageText();
        }
        if(mTruckMode.getTruckLengthText() != null){
            //车长
            truckTypeCarriageStr += " " + mTruckMode.getTruckLengthText();
        }
        if(truckTypeCarriageStr.length() > 0){
            viewHolder.truckTypeCarriageText.setText(truckTypeCarriageStr);
        }else{
            viewHolder.truckTypeCarriageText.setVisibility(View.GONE);
        }


        if(mTruckMode.getCarryCapacity() > 0){
            truckLoadSpaceStr += String.valueOf(mTruckMode.getCarryCapacity()) + "吨";
        }
        if(mTruckMode.getCarryVolume() > 0){
            truckLoadSpaceStr += " " + String.valueOf(mTruckMode.getCarryVolume()) + "立方米";
        }
        if(truckLoadSpaceStr.length() > 0){
            viewHolder.truckLoadSpaceText.setText(truckLoadSpaceStr);
        }else{
            viewHolder.truckLoadSpaceText.setVisibility(View.GONE);
        }

        viewHolder.chooseTruckCheckbox.setVisibility(View.VISIBLE);
        for(int i = 0; i < mTruckIdsList.size(); i++){
            if(mTruckMode.getTruckId().equals(mTruckIdsList.get(i))){
                viewHolder.chooseTruckCheckbox.setChecked(true);
            }
        }
        viewHolder.chooseTruckCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mTruckIdCallBack.addId(""+mTruckList.get(position).getTruckId(), position,
                            mTruckList.get(position).getTruckNumber());
                }else{
                    mTruckIdCallBack.minusId(""+mTruckList.get(position).getTruckId(), position,
                            mTruckList.get(position).getTruckNumber());
                }
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewHolder.chooseTruckCheckbox.isChecked()){
                    viewHolder.chooseTruckCheckbox.setChecked(false);
                }else{
                    viewHolder.chooseTruckCheckbox.setChecked(true);
                }
            }
        });
        if(isSelect){
            viewHolder.chooseTruckCheckbox.setChecked(true);
        }
        if(isNoneSelect){
            viewHolder.chooseTruckCheckbox.setChecked(false);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView truckNumberText, truckTypeCarriageText, truckLoadSpaceText;
        CheckBox chooseTruckCheckbox;

    }
}
