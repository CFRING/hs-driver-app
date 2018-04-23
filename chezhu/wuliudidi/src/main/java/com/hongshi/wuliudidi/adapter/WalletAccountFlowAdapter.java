package com.hongshi.wuliudidi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.WalletAccountFlowModel;
import com.hongshi.wuliudidi.utils.Util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by abc on 2016/4/12.
 */
public class WalletAccountFlowAdapter extends BaseAdapter{
    private Context mContext;
    private List<WalletAccountFlowModel> list;

    public WalletAccountFlowAdapter(Context mContext, List<WalletAccountFlowModel> list){
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
            convertView = View.inflate(mContext, R.layout.wallet_account_flow_item, null);

            viewHolder.divideView = (View) convertView.findViewById(R.id.divide_view);
            viewHolder.timeText = (TextView) convertView.findViewById(R.id.time_text);
            viewHolder.itemNameText = (TextView) convertView.findViewById(R.id.item_name_text);
            viewHolder.itemDetailText = (TextView) convertView.findViewById(R.id.item_detail_text);
            viewHolder.dateText = (TextView) convertView.findViewById(R.id.date_text);
            viewHolder.amountText = (TextView) convertView.findViewById(R.id.amount_text);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(list != null && position < list.size() && position >= 0) {
            WalletAccountFlowModel model = list.get(position);
            //此一条与上一条不属于同年同月
            if(position == 0 || !compareYearAndMonth(model.getGmtCreate(), list.get(position - 1).getGmtCreate())){
                if(position > 0){
                    viewHolder.divideView.setVisibility(View.VISIBLE);
                }else{
                    viewHolder.divideView.setVisibility(View.GONE);
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(model.getGmtCreate());
                int year = cal.get(Calendar.YEAR), month = cal.get(Calendar.MONTH) + 1;
                String timeStr = String.valueOf(year) + '\n' + String.valueOf(month) + "月";
                viewHolder.timeText.setText(timeStr);
                viewHolder.timeText.setVisibility(View.VISIBLE);
            }else{
                viewHolder.divideView.setVisibility(View.GONE);
                viewHolder.timeText.setVisibility(View.INVISIBLE);
            }
            viewHolder.itemNameText.setText(model.getDispTxt());
            viewHolder.itemDetailText.setText(model.getStatus());
            viewHolder.dateText.setText(Util.formatDate(model.getGmtCreate()));

            String amountStr;
            if(model.getRecordType() == 1){
                amountStr = "+";
            }else{
                amountStr = "-";
            }
            amountStr = amountStr + Util.formatDoubleToString(model.getMoney(), "元");
            viewHolder.amountText.setText(amountStr);
        }

        return convertView;
    }

    private boolean compareYearAndMonth(Date date1, Date date2){
        int year1, month1, year2, month2;
        Calendar cal1 = Calendar.getInstance(), cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        year1 = cal1.get(Calendar.YEAR);
        month1= cal1.get(Calendar.MONTH);
        year2 = cal2.get(Calendar.YEAR);
        month2 = cal2.get(Calendar.MONTH);
        if(year1 == year2 && month1 == month2){
            return true;
        }else{
            return false;
        }
    }

    public static class ViewHolder{
        View divideView;
        TextView timeText, itemNameText, itemDetailText, dateText, amountText;
    }
}
