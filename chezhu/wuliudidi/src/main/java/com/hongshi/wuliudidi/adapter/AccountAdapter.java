package com.hongshi.wuliudidi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.TradeRecordVO;
import com.hongshi.wuliudidi.utils.Util;

import java.util.List;

/**
 * Created by huiyuan on 2017/6/5.
 */

public class AccountAdapter extends BaseAdapter {
    private Context mContext;
    private List<TradeRecordVO> mAccountsList;

    public AccountAdapter(Context context, List<TradeRecordVO> mAccountsList) {
        this.mAccountsList = mAccountsList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        if(mAccountsList != null){
            return mAccountsList.size();
        }else {return 0;}
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
            convertView = View.inflate(mContext, R.layout.account_layout, null);
            viewHolder.detail_name = (TextView) convertView.findViewById(R.id.detail_name);
            viewHolder.date_and_time_txt = (TextView) convertView.findViewById(R.id.date_and_time_txt);
            viewHolder.money_txt = (TextView) convertView.findViewById(R.id.money_txt);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TradeRecordVO tradeRecordVO = mAccountsList.get(position);
        int type = tradeRecordVO.getRecordType();

        viewHolder.detail_name.setText(tradeRecordVO.getTradeName());
        viewHolder.date_and_time_txt.setText(Util.formatDateSecond(tradeRecordVO.getTradeTime()));
        if(type ==1 ){
            viewHolder.money_txt.setText( "+" + tradeRecordVO.getMoney());
        }else {viewHolder.money_txt.setText("-" + tradeRecordVO.getMoney());}

        return convertView;
    }

    static class ViewHolder {
        TextView detail_name,date_and_time_txt,money_txt;
    }
}
