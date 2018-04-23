package com.hongshi.wuliudidi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.OilCardModel;

import java.util.List;

/**
 * Created by huiyuan on 2017/8/12.
 */

public class OilCardsAdapter extends BaseAdapter {
    private Context mContext;
    private List<OilCardModel> list;

    public OilCardsAdapter(Context mContext, List<OilCardModel> list){
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        if(list != null){
            return list.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        if(list != null && i < list.size() && i >= 0){
            return list.get(i);
        }else{
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.oil_cards_list_item_layout, null);

            viewHolder.oil_icon_image = (ImageView) convertView.findViewById(R.id.oil_icon_image);
            viewHolder.oil_name_text = (TextView) convertView.findViewById(R.id.oil_name_text);
            viewHolder.card_number_text = (TextView) convertView.findViewById(R.id.card_number_text);
            viewHolder.truck_number = (TextView) convertView.findViewById(R.id.truck_number);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        OilCardModel model = list.get(position);
        String oilName = "";
        if(model != null){
            switch (model.getSupplierType()){
                case 1:
                    //中国石油
                    oilName = "中国石油";
                    viewHolder.oil_icon_image.setImageResource(R.drawable.shiyou_logo);
                    convertView.setBackgroundResource(R.drawable.shiyou_bg);
                    break;
                case 2:
                    //中国石化
                    oilName = "中国石化";
                    viewHolder.oil_icon_image.setImageResource(R.drawable.shihua_logo);
                    convertView.setBackgroundResource(R.drawable.shihua_bg);
                    break;
                case 3:
                    //中国海油
                    oilName = "中国海油";
                    viewHolder.oil_icon_image.setImageResource(R.drawable.haiyou_logo);
                    convertView.setBackgroundResource(R.drawable.haiyou_bg);
                    break;
                default:
                    oilName = "其他";
                    viewHolder.oil_icon_image.setImageResource(R.drawable.qita_logo);
                    convertView.setBackgroundResource(R.drawable.qt_bg);
                    break;
            }
            viewHolder.oil_name_text.setText(oilName);
            if(model.getCardID() != null){
                viewHolder.card_number_text.setText(model.getCardID());
            }
            if(model.getTruckNum() != null && !"".equals(model.getTruckNum())){
                viewHolder.truck_number.setVisibility(View.VISIBLE);
                viewHolder.truck_number.setText(model.getTruckNum());
            }else {
                viewHolder.truck_number.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    public static class ViewHolder{
        ImageView oil_icon_image;
        TextView oil_name_text,card_number_text,truck_number;
    }
}
