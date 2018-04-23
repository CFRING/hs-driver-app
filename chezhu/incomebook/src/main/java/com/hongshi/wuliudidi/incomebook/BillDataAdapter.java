package com.hongshi.wuliudidi.incomebook;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huiyuan on 2016/8/15.
 */
public class BillDataAdapter extends BaseAdapter{
    private Context mContext;
    private List<BillItemModel> dataList = new ArrayList<>();
    private String bill_statusText = "";
    private int bill_status = 0;
    private String money_unitText = "";
    private String weight_unitText = "";
    private int  style = 1;
    private String  styleTxt = "";

    public BillDataAdapter(Context context,List<BillItemModel> list){
        this.mContext = context;
        this.dataList = list;
    }

    public void addGoodsList(List<BillItemModel> mGoodsList){
        this.dataList.addAll(mGoodsList);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public BillItemModel getItem(int position) {
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
            convertView = View.inflate(mContext, R.layout.electronic_bill_item, null);
            viewHolder = new ViewHolder();
            viewHolder.start_time = (TextView) convertView.findViewById(R.id.start_time);
            viewHolder.end_time = (TextView) convertView.findViewById(R.id.end_time);
            viewHolder.bill_time_text = (TextView) convertView.findViewById(R.id.bill_time_text);
            viewHolder.total_money_text = (TextView) convertView.findViewById(R.id.total_money_text);
            viewHolder.actual_money_text = (TextView) convertView.findViewById(R.id.actual_money_text);
            viewHolder.total_transit_text = (TextView) convertView.findViewById(R.id.total_transit_text);
            viewHolder.transit_count_text = (TextView) convertView.findViewById(R.id.transit_count_text);
            viewHolder.total_weight_text = (TextView) convertView.findViewById(R.id.total_weight_text);
            viewHolder.check_man_tip_text = (TextView) convertView.findViewById(R.id.check_man_tip_text);
            viewHolder.status_tip = (TextView) convertView.findViewById(R.id.status_tip);
            viewHolder.total_money = (TextView) convertView.findViewById(R.id.total_money);
            viewHolder.actual_money = (TextView) convertView.findViewById(R.id.actual_money);
            viewHolder.total_transit_tip = (TextView) convertView.findViewById(R.id.total_transit_tip);
            viewHolder.transit_count_tip = (TextView) convertView.findViewById(R.id.transit_count_tip);
            viewHolder.total_weight_tip = (TextView) convertView.findViewById(R.id.total_weight_tip);
            viewHolder.check_man_tip = (TextView) convertView.findViewById(R.id.check_man_tip);
            viewHolder.lun_tai_fei = (TextView) convertView.findViewById(R.id.lun_tai_fei);
            viewHolder.yun_fei = (TextView) convertView.findViewById(R.id.yun_fei);
            viewHolder.you_fei = (TextView) convertView.findViewById(R.id.you_fei);
            viewHolder.tong_xing_fei = (TextView) convertView.findViewById(R.id.tong_xing_fei);
            viewHolder.gong_zi = (TextView) convertView.findViewById(R.id.gong_zi);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BillItemModel model = dataList.get(position);
        style = model.getStyle();
        styleTxt = model.getStyleTxt();
        bill_status = model.getReceiptStatus();
        bill_statusText = model.getReceiptStatusText();
        money_unitText = model.getMoneyUnitText();
        weight_unitText = model.getWeightUnitText();

        viewHolder.start_time.setText(model.getBeginDateStr() + " ");
        viewHolder.end_time.setText(model.getEndDateStr());
        if(model.getPeriod() == null || model.getPeriod().equals("")){
            viewHolder.bill_time_text.setVisibility(View.GONE);
        }else {
            viewHolder.bill_time_text.setText(model.getPeriod());
        }

        if(style == 1 || styleTxt.equals("正常")){
            viewHolder.lun_tai_fei.setVisibility(View.GONE);
            viewHolder.yun_fei.setVisibility(View.GONE);
            viewHolder.you_fei.setVisibility(View.GONE);
            viewHolder.tong_xing_fei.setVisibility(View.GONE);
            viewHolder.gong_zi.setVisibility(View.GONE);
            viewHolder.total_money.setText("总金额");
            viewHolder.actual_money.setText("实际结算");
            viewHolder.total_transit_tip.setText("运输车次");
            viewHolder.transit_count_tip.setText("运输车辆");
            viewHolder.total_weight_tip.setText("数量(吨)");
            viewHolder.check_man_tip.setText("核对人");
            viewHolder.check_man_tip_text.setVisibility(View.VISIBLE);
            viewHolder.check_man_tip.setVisibility(View.VISIBLE);
            viewHolder.total_money_text.setVisibility(View.VISIBLE);
            viewHolder.actual_money_text.setVisibility(View.VISIBLE);
            viewHolder.total_transit_text.setVisibility(View.VISIBLE);
            viewHolder.transit_count_text.setVisibility(View.VISIBLE);
            viewHolder.total_weight_text.setVisibility(View.VISIBLE);
            viewHolder.total_money_text.setText(Util.formatDoubleToString(model.getTotalAmount(), money_unitText) + money_unitText);
            viewHolder.actual_money_text.setText(Util.formatDoubleToString(model.getRealAmount(), money_unitText) + money_unitText);
            viewHolder.total_transit_text.setText(model.getTransitTimes() + mContext.getString(R.string.ci));
            viewHolder.transit_count_text.setText(model.getTruckCount() + mContext.getString(R.string.liang));
            viewHolder.total_weight_text.setText(Util.formatDoubleToString(model.getWeightTotal(), weight_unitText) + weight_unitText);
            viewHolder.check_man_tip_text.setText(model.getOppIdText());

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(120,42);
            layoutParams.addRule(RelativeLayout.BELOW, R.id.total_weight_tip);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams.rightMargin = 30;
            layoutParams.topMargin = 20;
            layoutParams.bottomMargin = 10;
            viewHolder.status_tip.setLayoutParams(layoutParams);

            if(bill_statusText.equals("已同意") || bill_status == 2){
                viewHolder.status_tip.setBackgroundResource(R.drawable.agree_status);
            }else if (bill_statusText.equals("待回执") || bill_status == 1){
                viewHolder.status_tip.setBackgroundResource(R.drawable.wait_for_handle);
            }else if(bill_statusText.equals("已拒绝") || bill_status ==3){
                viewHolder.status_tip.setBackgroundResource(R.drawable.refused);
            }

        }else if(style == 2 || styleTxt.equals("补单")){
            viewHolder.lun_tai_fei.setVisibility(View.VISIBLE);
            viewHolder.yun_fei.setVisibility(View.VISIBLE);
            viewHolder.you_fei.setVisibility(View.VISIBLE);
            viewHolder.tong_xing_fei.setVisibility(View.VISIBLE);
            viewHolder.gong_zi.setVisibility(View.VISIBLE);

            viewHolder.total_money.setText("补账总金额 " + Util.formatDoubleToString(model.getRealAmount(), money_unitText) + money_unitText);
            if(model.getTruckVoList() != null && model.getTruckVoList().get(0) != null){
                viewHolder.actual_money.setText("车牌号 " + model.getTruckVoList().get(0).getTruckNumber());
            }else {
                viewHolder.actual_money.setText("车牌号 ");
            }
            if(model.getSalary() > 0){
                viewHolder.total_transit_tip.setText("工资 " + Util.formatDoubleToString(model.getSalary(), money_unitText) + money_unitText);
            }else {
                viewHolder.total_transit_tip.setText("工资 ");
            }
            if(model.getLease() > 0){
                viewHolder.transit_count_tip.setText("租赁费 " + Util.formatDoubleToString(model.getLease(), money_unitText) + money_unitText);
            }else {
                viewHolder.transit_count_tip.setText("租赁费 ");
            }
            if(model.getOil() > 0){
                viewHolder.total_weight_tip.setText("油费 " + Util.formatDoubleToString(model.getOil(), money_unitText) + money_unitText);
            }else {
                viewHolder.total_weight_tip.setText("油费 ");
            }
            if(model.getRoad() > 0){
                viewHolder.check_man_tip.setText("通行费 " + Util.formatDoubleToString(model.getRoad(), money_unitText) + money_unitText);
            }else {
                viewHolder.check_man_tip.setText("通行费 ");
            }
            if(model.getTyre() > 0){
               viewHolder.lun_tai_fei.setText("轮胎费 " + Util.formatDoubleToString(model.getTyre(), money_unitText) + money_unitText);
            }else {
                viewHolder.lun_tai_fei.setText("轮胎费 ");
            }
            if(model.getYunfei() > 0){
                viewHolder.yun_fei.setText("运费 " + Util.formatDoubleToString(model.getYunfei(), money_unitText) + money_unitText);
            }else {
                viewHolder.yun_fei.setText("运费 ");
            }
            if(model.getOppIdText() != null){
                viewHolder.you_fei.setText("补账人 " + model.getOppIdText());
            }else {
                viewHolder.you_fei.setText("补账人 ");
            }

            if(model.getGmtCreateStr() != null){
                viewHolder.tong_xing_fei.setText("补账日期 " + model.getGmtCreateStr());
            }else {
                viewHolder.tong_xing_fei.setText("补账日期 ");
            }

            if(model.getNote() != null){
                viewHolder.gong_zi.setText("备注 " + model.getNote());
            }else {
                viewHolder.gong_zi.setText("备注 " );
            }
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(120,42);
            layoutParams.addRule(RelativeLayout.BELOW, R.id.gong_zi);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams.rightMargin = 30;
            layoutParams.topMargin = 20;
            layoutParams.bottomMargin = 10;
            viewHolder.status_tip.setLayoutParams(layoutParams);
            viewHolder.status_tip.setBackgroundResource(R.drawable.agree_status);
            viewHolder.check_man_tip_text.setVisibility(View.GONE);
            viewHolder.total_money_text.setVisibility(View.GONE);
            viewHolder.actual_money_text.setVisibility(View.GONE);
            viewHolder.total_transit_text.setVisibility(View.GONE);
            viewHolder.transit_count_text.setVisibility(View.GONE);
            viewHolder.total_weight_text.setVisibility(View.GONE);
        }

        return convertView;
    }

    public static class ViewHolder {
        TextView start_time,end_time,bill_time_text,total_money_text,actual_money_text,
                total_transit_text,transit_count_text,total_weight_text,check_man_tip_text,status_tip,
                total_money,actual_money,total_transit_tip,transit_count_tip,total_weight_tip,check_man_tip,
                lun_tai_fei,yun_fei,you_fei,tong_xing_fei,gong_zi;
    }
}
