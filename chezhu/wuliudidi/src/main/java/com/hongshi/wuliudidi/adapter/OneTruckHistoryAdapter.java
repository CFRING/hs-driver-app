package com.hongshi.wuliudidi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.OneTruckHistoryModel;
import com.hongshi.wuliudidi.utils.Util;

import java.util.List;

/**
 * Created by bian on 2016/7/18 16:51.
 * 供应端记录详情界面适配器
 */
public class OneTruckHistoryAdapter extends BaseAdapter {

    private Context mContext;
    private List<OneTruckHistoryModel> mList;

    public OneTruckHistoryAdapter(Context mContext, List<OneTruckHistoryModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null){
            view = View.inflate(mContext, R.layout.item_one_truck_history, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        OneTruckHistoryModel model = mList.get(i);
        viewHolder.truckNumber.setText(model.getTruckNumber());
        viewHolder.goodsName.setText(model.getCategoryText());
        viewHolder.date.setText(Util.formatDate(model.getGmtWeigh()));
        if(model.getFromAddr() == null || model.getFromAddr().equals("")){
            viewHolder.kuang_dian.setVisibility(View.GONE);
        }else {
            viewHolder.kuang_dian.setText("/矿点 " + model.getFromAddr());
        }
        viewHolder.company.setText("公司 " + model.getPkCorpName ());
        viewHolder.billCode.setText("单据号 " + model.getBillCode());
        viewHolder.fullWeight.setText("毛重 " + model.getFullWeight() + " " + model.getWeightText());
        viewHolder.netWeight.setText("净重 " + model.getNetWeight() + " " + model.getWeightText());
        return view;
    }

    private static class ViewHolder {
        //车牌号
        TextView truckNumber;
        //货物名称
        TextView goodsName;
        //过磅时间
        TextView date;
//        //发货地
//        TextView startCity;
//        //目的地
//        TextView endCity;
        //单据号
        TextView billCode;
        //毛重
        TextView fullWeight;
        //净重
        TextView netWeight;
        //矿点
        TextView kuang_dian;
        //公司
        TextView company;

        ViewHolder(View view){
            truckNumber = (TextView) view.findViewById(R.id.truck_number_text);
            goodsName = (TextView) view.findViewById(R.id.goods_name_and_weight);
            date = (TextView) view.findViewById(R.id.date_text);
//            startCity = (TextView) view.findViewById(R.id.start_city);
//            endCity = (TextView) view.findViewById(R.id.end_city);
            billCode = (TextView) view.findViewById(R.id.bill_code_text);
            fullWeight = (TextView) view.findViewById(R.id.full_weight_text);
            netWeight = (TextView) view.findViewById(R.id.net_weight);
            kuang_dian = (TextView) view.findViewById(R.id.kuang_dian);
            company = (TextView) view.findViewById(R.id.company_text);
        }
    }
}
