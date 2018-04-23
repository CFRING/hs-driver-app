package com.hongshi.wuliudidi.myroute;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by huiyuan on 2016/6/20.
 */
public class MyRouteListAdapter extends BaseAdapter{
    private Context mContext;
    private List<MyRouteModel> myRouteList;

    public MyRouteListAdapter(Context context, List<MyRouteModel> myRouteModels){
        mContext = context;
        this.myRouteList = myRouteModels;
    }
    @Override
    public int getCount() {
        return myRouteList.size();
    }

    @Override
    public Object getItem(int position) {
        return myRouteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.my_route_list_item, null);

            viewHolder.start_city = (TextView) convertView.findViewById(R.id.start_city);
            viewHolder.start_city_area = (TextView) convertView.findViewById(R.id.start_city_area);
            viewHolder.end_city = (TextView) convertView.findViewById(R.id.end_city);
            viewHolder.end_city_area = (TextView) convertView.findViewById(R.id.end_city_area);
            viewHolder.goods_name = (TextView) convertView.findViewById(R.id.goods_name);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.start_city.setText(myRouteList.get(position).getSenderCity());
        viewHolder.start_city_area.setText(myRouteList.get(position).getSenderDistrict());
        viewHolder.end_city.setText(myRouteList.get(position).getRecipientCity());
        viewHolder.end_city_area.setText(myRouteList.get(position).getRecipientDistrict());
        viewHolder.goods_name.setText(myRouteList.get(position).getGoodsTypeName());
        return convertView;
    }

    public static class ViewHolder {
        TextView start_city,start_city_area,goods_name,end_city,end_city_area;
    }

}
