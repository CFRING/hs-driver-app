package com.hongshi.wuliudidi.cashier;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;

import java.util.List;


/**
 * Created on 2016/4/26.
 */
public class ChooseBankAdapter extends BaseAdapter{

    private Context mContext;
    private List<TiXianModel.BankCard> mBankCardList;

    public ChooseBankAdapter(Context mContext, List<TiXianModel.BankCard> mBankCardList) {
        this.mContext = mContext;
        this.mBankCardList = mBankCardList;
    }

    @Override
    public int getCount() {
        return mBankCardList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBankCardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = View.inflate(mContext, R.layout.sdk_item_choose_card, null);
            holder = new ViewHolder();
            holder.bankImg = (ImageView) convertView.findViewById(R.id.bank_image);
            holder.bankName = (TextView) convertView.findViewById(R.id.bank_name);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        TiXianModel.BankCard bankCard = mBankCardList.get(position);
        switch (bankCard.getBankType()){
            case 1:
                holder.bankImg.setVisibility(View.VISIBLE);
                holder.bankImg.setImageResource(R.drawable.zhong_guo_yin_hang);
                break;
            case 2:
                holder.bankImg.setVisibility(View.VISIBLE);
                holder.bankImg.setImageResource(R.drawable.gong_shang_yin_hang);
                break;
            case 4:
                holder.bankImg.setVisibility(View.VISIBLE);
                holder.bankImg.setImageResource(R.drawable.jian_she_yin_hang);
                break;
            case 5:
                holder.bankImg.setVisibility(View.VISIBLE);
                holder.bankImg.setImageResource(R.drawable.nong_ye_yin_hang);
                break;
            default:
                holder.bankImg.setVisibility(View.INVISIBLE);
                break;
        }
        holder.bankName.setText(bankCard.getBankName() + " " + bankCard.getBankCardType() + " (" + bankCard.getBankNumber() + ")");
        return convertView;
    }

    private class ViewHolder{
        ImageView bankImg;
        TextView bankName;
    }
}
