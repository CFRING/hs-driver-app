package com.hongshi.wuliudidi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.TransitRecordDetailModel;
import com.hongshi.wuliudidi.utils.Util;

import java.util.List;

/**
 * Created by huiyuan on 2017/8/23.
 */

public class TransitRecordDetailAdapter extends BaseAdapter {
    private Context mContext;
    private List<TransitRecordDetailModel> mAccountsList;
    private boolean showCompany;

    public TransitRecordDetailAdapter(Context context, List<TransitRecordDetailModel> mAccountsList,
                                      boolean showCompany) {
        this.mAccountsList = mAccountsList;
        this.mContext = context;
        this.showCompany = showCompany;
    }

    @Override
    public int getCount() {
        if(mAccountsList != null){
            return mAccountsList.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mAccountsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.transit_record_detail_item_layout, null);
            viewHolder.company = (TextView) convertView.findViewById(R.id.company);
            viewHolder.start_address_text = (TextView) convertView.findViewById(R.id.start_address_text);
            viewHolder.end_address_text = (TextView) convertView.findViewById(R.id.end_address_text);
            viewHolder.truck_number = (TextView) convertView.findViewById(R.id.truck_number);
            viewHolder.materia_text = (TextView) convertView.findViewById(R.id.materia_text);
            viewHolder.time_text = (TextView) convertView.findViewById(R.id.time_text);
            viewHolder.total_money = (TextView) convertView.findViewById(R.id.total_money);
            viewHolder.company_container = (RelativeLayout) convertView.findViewById(R.id.company_container);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TransitRecordDetailModel transitRecordDetailModel = mAccountsList.get(position);

        if(showCompany){
            viewHolder.company.setText(transitRecordDetailModel.getGoodsStationName());
            viewHolder.company.setVisibility(View.VISIBLE);
        }else {
            viewHolder.company_container.setVisibility(View.GONE);
        }

        viewHolder.start_address_text.setText(transitRecordDetailModel.getSenderAddress());
        viewHolder.end_address_text.setText(transitRecordDetailModel.getReceiptAddress());
        viewHolder.truck_number.setText(transitRecordDetailModel.getTruckNo());
        viewHolder.materia_text.setText(transitRecordDetailModel.getGoodsType() + " " +
        transitRecordDetailModel.getQuantity() + " " + transitRecordDetailModel.getWeightUnit());
        viewHolder.time_text.setText(Util.formatDateMinute(transitRecordDetailModel.getOutBizDate()));
        viewHolder.total_money.setText(transitRecordDetailModel.getMoney());

        return convertView;
    }

    static class ViewHolder {
        TextView company,start_address_text,end_address_text,
                truck_number,materia_text,time_text,total_money;
        RelativeLayout company_container;
    }
}
