package com.hongshi.wuliudidi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.OilCardModel;

import java.util.List;


/**
 * Created by huiyuan on 2017/8/16.
 */
public class ChooseOilCardAdapter extends BaseAdapter{

    private Context mContext;
    private List<OilCardModel> mCardList;

    public ChooseOilCardAdapter(Context mContext, List<OilCardModel> mBankCardList) {
        this.mContext = mContext;
        this.mCardList = mBankCardList;
    }

    @Override
    public int getCount() {
       if(mCardList != null){
           return mCardList.size();
       }else {
           return 0;
       }
    }

    @Override
    public Object getItem(int position) {
        return mCardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = View.inflate(mContext, R.layout.sdk_item_choose_card1, null);
            holder = new ViewHolder();
            holder.oil_image = (ImageView) convertView.findViewById(R.id.oil_image);
            holder.oil_name = (TextView) convertView.findViewById(R.id.oil_name);
            holder.card_number_text = (TextView) convertView.findViewById(R.id.card_number_text);
            holder.company_name = (TextView) convertView.findViewById(R.id.company_name);
            holder.money_text = (TextView) convertView.findViewById(R.id.money_text);
            holder.settle_company_container = (RelativeLayout) convertView.findViewById(R.id.settle_company_container);
            holder.oil_info_container = (RelativeLayout) convertView.findViewById(R.id.oil_info_container);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        OilCardModel model = mCardList.get(position);
        if(model.getGasolineStation() != null && !"".equals(model.getGasolineStation())){
            holder.settle_company_container.setVisibility(View.GONE);
            holder.oil_info_container.setVisibility(View.VISIBLE);
            String oilName = "";
            switch (model.getSupplierType()){
                case 1:
                    //中国石油
                    oilName = "中国石油";
                    holder.oil_image.setImageResource(R.drawable.shiyou_logo);
                    break;
                case 2:
                    //中国石化
                    oilName = "中国石化";
                    holder.oil_image.setImageResource(R.drawable.shihua_logo);
                    break;
                case 3:
                    //中国海油
                    oilName = "中国海油";
                    holder.oil_image.setImageResource(R.drawable.haiyou_logo);
                    break;
                default:
                    //其他
                    oilName = "其他";
                    holder.oil_image.setImageResource(R.drawable.qita_logo);
                    break;
            }
            if(model.getTruckNum() != null){
                holder.oil_name.setText(oilName + " (" + model.getTruckNum() + ")");
            }else {
                holder.oil_name.setText(oilName);
            }
            if(model.getCardID() != null){
                holder.card_number_text.setText(model.getCardID());
            }
        }else {
            holder.oil_info_container.setVisibility(View.GONE);
            holder.settle_company_container.setVisibility(View.VISIBLE);
            holder.company_name.setText(model.getSupplier());
            holder.money_text.setText(model.getAmount());
        }
        return convertView;
    }

    private static class ViewHolder{
        ImageView oil_image;
        TextView oil_name,card_number_text,company_name,money_text;
        RelativeLayout settle_company_container,oil_info_container;
    }
}
