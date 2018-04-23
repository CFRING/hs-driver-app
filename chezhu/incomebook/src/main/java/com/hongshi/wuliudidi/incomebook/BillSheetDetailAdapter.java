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
public class BillSheetDetailAdapter extends BaseAdapter{
    private Context mContext;
    private List<BillSheetDetailModel> dataList = new ArrayList<>();

    public BillSheetDetailAdapter(Context context,List<BillSheetDetailModel> list){
        this.mContext = context;
        this.dataList = list;
    }

    public void addGoodsList(List<BillSheetDetailModel> mGoodsList){
        this.dataList.addAll(mGoodsList);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public BillSheetDetailModel getItem(int position) {
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
            convertView = View.inflate(mContext, R.layout.electronic_bill_detail_item, null);
            viewHolder = new ViewHolder();
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.company = (TextView) convertView.findViewById(R.id.company);
            viewHolder.ti_huo_dian = (TextView) convertView.findViewById(R.id.ti_huo_dian);
            viewHolder.huo_pin = (TextView) convertView.findViewById(R.id.huo_pin);
            viewHolder.dan_jia = (TextView) convertView.findViewById(R.id.dan_jia);
            viewHolder.dan_ju_hao = (TextView) convertView.findViewById(R.id.dan_ju_hao);
            viewHolder.kou_kuan = (TextView) convertView.findViewById(R.id.kou_kuan);
            viewHolder.shu_liang = (TextView) convertView.findViewById(R.id.shu_liang);
            viewHolder.jin_er = (TextView) convertView.findViewById(R.id.jin_er);
            viewHolder.kou_dun = (TextView) convertView.findViewById(R.id.kou_dun);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BillSheetDetailModel goodsmodel = dataList.get(position);

        viewHolder.time.setText(goodsmodel.getOutBizDate());
        viewHolder.company.setText("公司: " + goodsmodel.getCorporation());
        viewHolder.ti_huo_dian.setText("提货点: " + goodsmodel.getMine());
        viewHolder.huo_pin.setText("货品: " + goodsmodel.getGoodsCategoryText());
        viewHolder.dan_jia.setText("单价(元): " + Util.formatDoubleToString(goodsmodel.getDj(), "元"));
        viewHolder.dan_ju_hao.setText("单据号: " + goodsmodel.getDanJu());
        viewHolder.kou_kuan.setText("扣款(元): " + Util.formatDoubleToString(goodsmodel.getDeductAmount(), "元"));
        viewHolder.shu_liang.setText("数量(吨): " + Util.formatDoubleToString(goodsmodel.getWeight(),"吨"));
        viewHolder.jin_er.setText("金额(元): " + Util.formatDoubleToString(goodsmodel.getTotalAmount(),"元"));
        viewHolder.kou_dun.setText("扣吨(吨): " + Util.formatDoubleToString(goodsmodel.getLossWeight(),"吨"));

        return convertView;
    }

    public static class ViewHolder {
        TextView time,company,ti_huo_dian,huo_pin,dan_jia,dan_ju_hao,kou_kuan,shu_liang,jin_er,kou_dun;
    }
}
