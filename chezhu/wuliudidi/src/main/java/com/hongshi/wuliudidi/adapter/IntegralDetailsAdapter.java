package com.hongshi.wuliudidi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.IntegralDetailModel;

import java.util.List;

/**
 * Created by huiyuan on 2017/11/15.
 */

public class IntegralDetailsAdapter extends BaseAdapter {
    private Context mContext;
    private List<IntegralDetailModel> mIntegralList;

    public IntegralDetailsAdapter(Context context,List<IntegralDetailModel> list) {
        this.mIntegralList = list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        if(mIntegralList != null){
            return mIntegralList.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mIntegralList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_integral_detail, null);
            viewHolder.title_text = (TextView) convertView.findViewById(R.id.title_text);
            viewHolder.date_and_time = (TextView) convertView.findViewById(R.id.date_and_time);
            viewHolder.money = (TextView) convertView.findViewById(R.id.money);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        IntegralDetailModel model = mIntegralList.get(position);
        viewHolder.title_text.setText(model.getOperationDesc());
        viewHolder.date_and_time.setText(model.getOptTime());
        if(model.isPositive()){
            viewHolder.money.setTextColor(mContext.getResources().getColor(R.color.home_text_press));
            viewHolder.money.setText("+" + model.getPoint());
        }else {
            viewHolder.money.setTextColor(mContext.getResources().getColor(R.color.black));
            viewHolder.money.setText("+" + model.getPoint());
        }

        return convertView;
    }

    static class ViewHolder {
        TextView title_text,date_and_time,money;
    }
}
