package com.hongshi.wuliudidi.incomebook;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huiyuan on 2016/8/15.
 */
public class HSJCBillSheetAdapter extends BaseAdapter{
    private Context mContext;
    private List<HSJCBillDetailItemModel> dataList = new ArrayList<>();

    public HSJCBillSheetAdapter(Context context,List<HSJCBillDetailItemModel> list){
        this.mContext = context;
        this.dataList = list;
    }

    public void addGoodsList(List<HSJCBillDetailItemModel> mGoodsList){
        this.dataList.addAll(mGoodsList);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public HSJCBillDetailItemModel getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.hsjc_bill_detail_item, null);
            viewHolder = new ViewHolder();
            viewHolder.truck_number_text = (TextView) convertView.findViewById(R.id.truck_number_text);
            viewHolder.trade_time = (TextView) convertView.findViewById(R.id.trade_time);
            viewHolder.jing_xiao_shang_name = (TextView) convertView.findViewById(R.id.jing_xiao_shang_name);
            viewHolder.goods_name = (TextView) convertView.findViewById(R.id.goods_name);
            viewHolder.fa_huo_di_name = (TextView) convertView.findViewById(R.id.fa_huo_di_name);
            viewHolder.mu_di_di_name = (TextView) convertView.findViewById(R.id.mu_di_di_name);
            viewHolder.yun_liang_name = (TextView) convertView.findViewById(R.id.yun_liang_name);
            viewHolder.dan_jia_name = (TextView) convertView.findViewById(R.id.dan_jia_name);
            viewHolder.yun_fei_name = (TextView) convertView.findViewById(R.id.yun_fei_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        HSJCBillDetailItemModel model = dataList.get(position);
        viewHolder.truck_number_text.setText("车号 " + model.getTruckNumber());
        viewHolder.trade_time.setText(model.getOutBizDate());
        viewHolder.jing_xiao_shang_name.setText(model.getCustName());
        viewHolder.goods_name.setText(model.getGoodsCategoryText());
        viewHolder.fa_huo_di_name.setText(model.getSenderAddress());
        viewHolder.mu_di_di_name.setText(model.getRecipientAddress());
        viewHolder.yun_liang_name.setText(Util.formatDoubleToString(model.getWeight(), "吨"));
        viewHolder.dan_jia_name.setText(Util.formatDoubleToString(model.getDj(),"元"));
        viewHolder.yun_fei_name.setText(Util.formatDoubleToString(model.getTotalAmount(),"元"));

        return convertView;
    }

    public static class ViewHolder {
        TextView truck_number_text,trade_time,jing_xiao_shang_name,goods_name,fa_huo_di_name,
                mu_di_di_name,yun_liang_name,dan_jia_name,yun_fei_name;
    }
}
