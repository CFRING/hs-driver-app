package com.hongshi.wuliudidi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.BankcardModel;
import com.hongshi.wuliudidi.view.CircleImageView;

import java.util.List;

/**
 * Created by abc on 2016/4/13.
 */
public class MyBankcardListAdapter extends BaseAdapter{
    private Context mContext;
    private List<BankcardModel> list;

    public MyBankcardListAdapter(Context mContext, List<BankcardModel> list){
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

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.my_bankcard_item_layout, null);

            viewHolder.bankIcon = (ImageView) convertView.findViewById(R.id.bank_icon_image);
            viewHolder.bankNameText = (TextView) convertView.findViewById(R.id.bank_name_text);
            viewHolder.cardNameText = (TextView) convertView.findViewById(R.id.card_name_text);
            viewHolder.cardNumbertText = (TextView) convertView.findViewById(R.id.card_number_text);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BankcardModel model = list.get(position);
        switch (model.getBankType()){
            case 1:
                //中国银行
                viewHolder.bankIcon.setImageResource(R.drawable.boc_bank_icon_big);
                convertView.setBackgroundResource(R.drawable.boc_background_round_corner_style);
                break;
            case 2:
                //工商银行
                viewHolder.bankIcon.setImageResource(R.drawable.icbc_bank_icon_big);
                convertView.setBackgroundResource(R.drawable.icbc_background_round_corner_style);
                break;
            case 4:
                //建设银行
                viewHolder.bankIcon.setImageResource(R.drawable.ccb_bank_icon_big);
                convertView.setBackgroundResource(R.drawable.ccb_background_round_corner_style);
                break;
            case 5:
                //农业银行
                viewHolder.bankIcon.setImageResource(R.drawable.abc_bank_icon_big);
                convertView.setBackgroundResource(R.drawable.abc_background_round_corner_style);
                break;
            default:
                viewHolder.bankIcon.setImageResource(R.drawable.other_bank_icon_big);
                convertView.setBackgroundResource(R.drawable.other_background_round_corner_style);
                break;
        }
        viewHolder.bankNameText.setText(model.getBankName());
        viewHolder.cardNameText.setText(model.getBankCardType());
        String cardNumber = model.getBankNumber(), hidedNumber = "";
        if(cardNumber != null) {
            for (int i = 0; i < cardNumber.length(); i++){
                if(i < cardNumber.length() - 4) {
                    hidedNumber += '*';
                }else{
                    hidedNumber += cardNumber.charAt(i);
                }
            }
        }
        viewHolder.cardNumbertText.setText(hidedNumber);


        return convertView;
    }

    public static class ViewHolder{
        ImageView bankIcon;
        TextView bankNameText, cardNameText, cardNumbertText;
    }
}
