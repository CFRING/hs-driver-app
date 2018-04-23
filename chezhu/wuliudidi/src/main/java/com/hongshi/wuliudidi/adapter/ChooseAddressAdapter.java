package com.hongshi.wuliudidi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.CityListModel;

import java.util.List;

/**
 * Created by bian on 2017/6/23 11:21.
 */

public class ChooseAddressAdapter extends BaseAdapter {

    private Context mContext;
    private List<CityListModel> mList;

    public ChooseAddressAdapter(Context mContext, List<CityListModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_choose_address, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.addressTV.setText(mList.get(position).getName());
        return convertView;
    }

    private static class ViewHolder {
        TextView addressTV;

        public ViewHolder(View view) {
            addressTV = (TextView) view.findViewById(R.id.address_text);
        }
    }
}
