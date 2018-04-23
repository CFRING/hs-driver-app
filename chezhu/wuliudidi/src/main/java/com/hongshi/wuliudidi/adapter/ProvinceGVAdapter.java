package com.hongshi.wuliudidi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;

import java.util.List;

/**
 * Created by bian on 2017/3/26 9:44.
 */

public class ProvinceGVAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mList;

    public ProvinceGVAdapter(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_province, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.provinceTV.setText(mList.get(position));
        return null;
    }

    private static class ViewHolder{
        private TextView provinceTV;

        private ViewHolder(View view) {
            provinceTV = (TextView) view.findViewById(R.id.province_text1);
        }

    }
}
