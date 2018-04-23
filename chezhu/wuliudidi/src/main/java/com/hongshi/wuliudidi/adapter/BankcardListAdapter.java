package com.hongshi.wuliudidi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.BankcardModel;

import java.util.List;

/**
 * Created by abc on 2016/4/12.
 */
public class BankcardListAdapter extends BaseAdapter{
    private Context mContext;
    private List<BankcardModel> list;

    public BankcardListAdapter(Context mContext, List<BankcardModel> list){
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
            convertView = View.inflate(mContext, R.layout.bankcard_item_layout, null);

            viewHolder.bankIcon = (ImageView) convertView.findViewById(R.id.bank_card_image);
            viewHolder.cardText = (TextView) convertView.findViewById(R.id.bank_card_text);
                convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(list != null && position < list.size() && position >=0) {
            BankcardModel model = list.get(position);
            switch (model.getBankType()){
                case 1:
                    //中国银行
                    viewHolder.bankIcon.setImageResource(R.drawable.boc_bank_icon);
                    break;
                case 2:
                    //工商银行
                    viewHolder.bankIcon.setImageResource(R.drawable.icbc_bank_icon);
                    break;
                case 4:
                    //建设银行
                    viewHolder.bankIcon.setImageResource(R.drawable.ccb_bank_icon);
                    break;
                case 5:
                    //农业银行
                    viewHolder.bankIcon.setImageResource(R.drawable.abc_bank_icon);
                    break;
                default:
                    viewHolder.bankIcon.setImageResource(R.drawable.other_bank_icon);
                    break;
            }

            String cardText = "";
            if(model.getBankName() != null) {
                cardText += model.getBankName() + " ";
            }
            if(model.getBankCardType() != null) {
                cardText += model.getBankCardType();
            }
            String cardNumber = model.getBankNumber();
            if(cardNumber != null && cardNumber.length() >= 4){
                cardText += " (" + cardNumber.substring(cardNumber.length() - 4, cardNumber.length()) + ")";
            }
            viewHolder.cardText.setText(cardText);
        }

        return convertView;
    }

    public static class ViewHolder{
        ImageView bankIcon;
        TextView cardText;
    }
}
