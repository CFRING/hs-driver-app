package com.hongshi.wuliudidi.incomebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/4/24.
 * 运输记录->每日记录的条目适配器
 */
public class TransitRecordItemsAdapter extends BaseAdapter {
    private Context mContext;
    private List<CarrierBillDetailVO> list;

    public TransitRecordItemsAdapter(Context mContext, List<CarrierBillDetailVO> list){
        this.mContext = mContext;
        this.list = list;
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
            convertView = View.inflate(mContext, R.layout.transit_record_truck_item, null);
            viewHolder = new ViewHolder();
            viewHolder.divideView = (View) convertView.findViewById(R.id.divide_view);
            viewHolder.truckNumber = (TextView) convertView.findViewById(R.id.truck_number_text);
            viewHolder.goodsNameAndWeight = (TextView) convertView.findViewById(R.id.goods_name_and_weight);
            viewHolder.dateText = (TextView) convertView.findViewById(R.id.date_text);
            viewHolder.moneySum = (TextView) convertView.findViewById(R.id.all_fee);
            viewHolder.startCity = (TextView) convertView.findViewById(R.id.start_city);
            viewHolder.endCity = (TextView) convertView.findViewById(R.id.end_city);
            viewHolder.chargeLayout = (LinearLayout) convertView.findViewById(R.id.charge_layout);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
        CarrierBillDetailVO mCarrierBillDetailVO = list.get(position);
        viewHolder.truckNumber.setText(mCarrierBillDetailVO.getTruckNO());
        viewHolder.goodsNameAndWeight.setText(mCarrierBillDetailVO.getGoodsName()+"   "+mCarrierBillDetailVO.getGoodsAmount()+
                mCarrierBillDetailVO.getUnit());
        if(mCarrierBillDetailVO.getOutBizDate() != null) {
            viewHolder.dateText.setText(Util.formatDateMinute(mCarrierBillDetailVO.getOutBizDate()));
        }else{
            viewHolder.dateText.setText("");
        }
        viewHolder.moneySum.setText(Util.formatDoubleToString(mCarrierBillDetailVO.getMoney(), "元"));
        viewHolder.startCity.setText(mCarrierBillDetailVO.getSenderCity() + mCarrierBillDetailVO.getSenderDistrict());
        viewHolder.endCity.setText(mCarrierBillDetailVO.getRecipientCity() + mCarrierBillDetailVO.getRecipientDistrict());

        List<FeeVo> carribillTypeList = mCarrierBillDetailVO.getCarribillTypeList();
        if(carribillTypeList != null && carribillTypeList.size() > 0) {
            viewHolder.chargeLayout.removeAllViews();
            for (int i = 0; i < carribillTypeList.size(); i++) {
                FeeVo feeVo = carribillTypeList.get(i);
                RelativeLayout truck_item_lyout = (RelativeLayout) LayoutInflater.from(mContext).
                    inflate(R.layout.transit_record_charge_item, null);
                TextView billTypeTxt = (TextView) truck_item_lyout.findViewById(R.id.billTypeTxt);
                TextView state_settle = (TextView) truck_item_lyout.findViewById(R.id.state_settle);
                TextView money = (TextView) truck_item_lyout.findViewById(R.id.money);

                billTypeTxt.setText(feeVo.getBillTypeTxt());
                state_settle.setText(feeVo.getStatusTxt());
                money.setText(Util.formatDoubleToString(feeVo.getSumMoney(), "元"));
                if (feeVo.getStatus() == 0) {
                    //未结算
                    state_settle.setTextColor(mContext.getResources().getColor(R.color.black));
                } else {
                    state_settle.setTextColor(mContext.getResources().getColor(R.color.theme_color));
                }
                viewHolder.chargeLayout.addView(truck_item_lyout);
            }
            viewHolder.chargeLayout.setVisibility(View.VISIBLE);
        }else{
            viewHolder.chargeLayout.setVisibility(View.GONE);
        }

        if(position == 0){
            viewHolder.divideView.setVisibility(View.GONE);
        }else{
            viewHolder.divideView.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        View divideView;
        TextView truckNumber, goodsNameAndWeight, dateText, moneySum, startCity, endCity;
        LinearLayout chargeLayout;
    }
}
