package com.hongshi.wuliudidi.incomebook;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zhuoran on 2016/4/24.
 * 运输记录->年度/月度记录的条目适配器
 */
public class TransitRecordStatisticAdapter extends BaseAdapter {
    private Context mContext;
    private List<CarrierBillDetailVO> list;
    private String statisticType;//1.月度统计;2.年度统计;
    private final String RefreshTransitMonthly = "action.refresh.transit_monthly";//刷新运输记录月度
    private final String RefreshTransitDialy = "action.refresh.transit_daily";//刷新运输记录每日

    public TransitRecordStatisticAdapter(Context mContext, List<CarrierBillDetailVO> list, String statisticType){
        this.mContext = mContext;
        this.list = list;
        this.statisticType = statisticType;
    }

    @Override
    public int getCount() {
        if(list != null){
            return list.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        if(list != null && i >= 0 && i < list.size()){
            return list.get(i);
        }else {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.transit_record_statistic, null);
            viewHolder = new ViewHolder();
            viewHolder.truckNumberText = (TextView) convertView.findViewById(R.id.truck_number_text);
            viewHolder.transitCountText = (TextView) convertView.findViewById(R.id.transit_count_text);
            viewHolder.totalIncomeText = (TextView) convertView.findViewById(R.id.total_income_text);
            viewHolder.feeItemsLayout = (LinearLayout) convertView.findViewById(R.id.fee_items_layout);
            viewHolder.arrowClickView = convertView.findViewById(R.id.arrow_click_view);
            viewHolder.dateText = (TextView) convertView.findViewById(R.id.date_text);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final CarrierBillDetailVO mCarrierBillDetailVO = list.get(position);

        if(mCarrierBillDetailVO.getTruckNO() != null) {
            viewHolder.truckNumberText.setText(mCarrierBillDetailVO.getTruckNO());
        }

        viewHolder.dateText.setText(mCarrierBillDetailVO.getOutBizDateDayTxt());

        String transitNum = String.valueOf(mCarrierBillDetailVO.getTaskCount());
        String transitCount = "运输次数" + transitNum + "次";
        Util.setText(mContext, transitCount, transitNum, viewHolder.transitCountText, R.color.theme_color);

        String income = Util.formatDoubleToString(mCarrierBillDetailVO.getMoney(), "元");
        String totalIncome = "收入总计" + income + "元";
        Util.setText(mContext, totalIncome, income, viewHolder.totalIncomeText, R.color.theme_color);

        List<FeeVo> carribillTypeList = mCarrierBillDetailVO.getCarribillTypeList();
        if(carribillTypeList != null && carribillTypeList.size() > 0) {
            viewHolder.feeItemsLayout.removeAllViews();
            for (int i = 0; i < carribillTypeList.size(); i+=2) {//每一行展示两条，所以i每次加2
                LinearLayout fee_items_lyout = (LinearLayout) LayoutInflater.from(mContext).
                        inflate(R.layout.transit_record_statistic_fee_item, null);

                TextView item1Name = (TextView) fee_items_lyout.findViewById(R.id.item1_name);
                TextView item1Num = (TextView) fee_items_lyout.findViewById(R.id.item1_num);
                TextView item2Name = (TextView) fee_items_lyout.findViewById(R.id.item2_name);
                TextView item2Num = (TextView) fee_items_lyout.findViewById(R.id.item2_num);

                FeeVo feeVo1 = carribillTypeList.get(i), feeVo2;
                item1Name.setText(feeVo1.getBillTypeTxt());
                item1Num.setText(Util.formatDoubleToString(feeVo1.getSumMoney(), "元"));
                if(i + 1 < carribillTypeList.size()){
                    feeVo2 = carribillTypeList.get(i +1);
                    item2Name.setText(feeVo2.getBillTypeTxt());
                    item2Num.setText(Util.formatDoubleToString(feeVo2.getSumMoney(), "元"));
                }
                viewHolder.feeItemsLayout.addView(fee_items_lyout);
            }
            viewHolder.feeItemsLayout.setVisibility(View.VISIBLE);
        }else{
            viewHolder.feeItemsLayout.setVisibility(View.GONE);
        }

        viewHolder.arrowClickView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(statisticType == null || statisticType.length() <= 0){
                    return;
                }
                String dateStr = mCarrierBillDetailVO.getOutBizDateDayTxt();
                if(dateStr == null || dateStr.length() <= 0){
                    return;
                }
                Intent intent = new Intent();
                Date queryDateS, queryDateE;
                SimpleDateFormat sdf;
                if(statisticType.equals("2")) {//年度统计的被点击了，要通知月度统计的fragment刷新
                    intent.setAction(RefreshTransitMonthly);
                    try{
                        sdf = new SimpleDateFormat("yyyy");
                        queryDateS = sdf.parse(dateStr);

                        intent.putExtra("queryDateStart", String.valueOf(queryDateS.getTime()));
                        intent.putExtra("queryTruckId", mCarrierBillDetailVO.getTruckId());
                        mContext.sendBroadcast(intent);
                    }catch (Exception e){

                    }
                }else if(statisticType.equals("1")){//月度统计的被点击了，要通知每日统计的fragment刷新
                    intent.setAction(RefreshTransitDialy);
                    try{
                        sdf = new SimpleDateFormat("yyyy-MM");
                        queryDateS = sdf.parse(dateStr);

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(queryDateS);
                        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                        queryDateE = cal.getTime();

                        intent.putExtra("queryDateStart", String.valueOf(queryDateS.getTime()));
                        intent.putExtra("queryDateEnd", String.valueOf(queryDateE.getTime()));
                        intent.putExtra("queryTruckId", mCarrierBillDetailVO.getTruckId());
                        mContext.sendBroadcast(intent);
                    }catch (Exception e){

                    }
                }

            }
        });

        return convertView;
    }

    static class ViewHolder {
        View arrowClickView;
        LinearLayout feeItemsLayout;
        TextView truckNumberText, transitCountText, totalIncomeText, dateText;
    }
}
