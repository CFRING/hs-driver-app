package com.hongshi.wuliudidi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.ClassfyStatVO;
import com.hongshi.wuliudidi.model.CtBillCycleReconStatVO;

import java.util.List;

/**
 * Created by huiyuan on 2017/8/22.
 */

public class IncomeBookAdapter extends BaseAdapter {
    private Context mContext;
    private List<ClassfyStatVO> mAccountsList;

    public IncomeBookAdapter(Context context, List<ClassfyStatVO> mAccountsList) {
        this.mAccountsList = mAccountsList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        if(mAccountsList != null){
            return mAccountsList.size();
        }else {
            return 0;
        }
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
            convertView = View.inflate(mContext, R.layout.income_book_list_item_layout, null);
            viewHolder.truck_number_text = (TextView) convertView.findViewById(R.id.truck_number_text);
            viewHolder.total_money = (TextView) convertView.findViewById(R.id.total_money);
            viewHolder.cash_text = (TextView) convertView.findViewById(R.id.cash_text);
            viewHolder.consume_text = (TextView) convertView.findViewById(R.id.consume_text);
            viewHolder.oil_text = (TextView) convertView.findViewById(R.id.oil_text);
            viewHolder.tyre_text = (TextView) convertView.findViewById(R.id.tyre_text);
            viewHolder.truck_icon = (ImageView) convertView.findViewById(R.id.truck_icon);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ClassfyStatVO inComeBookModel = mAccountsList.get(position);

        if(inComeBookModel.getClassifyType() == 1){
            viewHolder.truck_icon.setBackgroundResource(R.drawable.truck_head_left);
        }else {
            viewHolder.truck_icon.setBackgroundResource(R.drawable.goods_1);
        }
        viewHolder.truck_number_text.setText(inComeBookModel.getClassify());
        viewHolder.total_money.setText(inComeBookModel.getMoney());
        viewHolder.cash_text.setText(inComeBookModel.getSalaryMoney());
        viewHolder.consume_text.setText(inComeBookModel.getConsumptionMoney());
        viewHolder.oil_text.setText(inComeBookModel.getOilMoney());
        viewHolder.tyre_text.setText(inComeBookModel.getTyreMoney());

        return convertView;
    }

    static class ViewHolder {
        TextView truck_number_text,total_money,cash_text,consume_text,oil_text,tyre_text;
        ImageView truck_icon;
    }
}
