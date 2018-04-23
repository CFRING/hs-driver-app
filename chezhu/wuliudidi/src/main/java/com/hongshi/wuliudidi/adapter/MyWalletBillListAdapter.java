package com.hongshi.wuliudidi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.ClassfyStatVO;
import com.hongshi.wuliudidi.model.CtBillCycleReconStatVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huiyuan on 2017/4/17.
 */

public class MyWalletBillListAdapter extends BaseAdapter {

    private Context mContext;
    private List<CtBillCycleReconStatVO> walletModelList = new ArrayList<>();

    public MyWalletBillListAdapter(Context context, List<CtBillCycleReconStatVO> list){
        this.mContext = context;
        walletModelList = list;
    }

    @Override
    public int getCount() {
        return walletModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyWalletHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.my_wallet_bill_item,null);
            holder = new MyWalletHolder();
            holder.account_rest_money_text = (TextView) convertView.findViewById(R.id.account_rest_money_text);
            holder.consume_account_rest_money_text = (TextView) convertView.findViewById(R.id.consume_account_rest_money_text);
            holder.tyre_rest_money_text = (TextView) convertView.findViewById(R.id.tyre_rest_money_text);
            holder.oil_rest_money_text = (TextView) convertView.findViewById(R.id.oil_rest_money_text);
            holder.transit_period_text = (TextView) convertView.findViewById(R.id.transit_period_text);
            holder.he_ji_tip = (TextView) convertView.findViewById(R.id.he_ji_tip);

            convertView.setTag(holder);
        }else {
            holder = (MyWalletHolder)convertView.getTag();
        }

        CtBillCycleReconStatVO model = walletModelList.get(position);
        holder.account_rest_money_text.setText(model.getSalaryMoney());
        holder.consume_account_rest_money_text.setText(model.getConsumptionMoney());
        holder.tyre_rest_money_text.setText(model.getTyreMoney());
        holder.oil_rest_money_text.setText(model.getOilMoney());
        holder.transit_period_text.setText(model.getPeriod());
        holder.he_ji_tip.setText("合计￥" + model.getTotalMoney());

        return convertView;
    }

    static class MyWalletHolder{
        TextView account_rest_money_text,consume_account_rest_money_text,
                tyre_rest_money_text,oil_rest_money_text,transit_period_text,he_ji_tip;
    }
}
