package com.hongshi.wuliudidi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.TradeRecord4AppVO;

import java.util.List;

/**
 * Created by huiyuan on 2017/6/7.
 */

public class PaymentOrdersAdapter extends BaseAdapter {

    private Context mContext;
    private List<TradeRecord4AppVO> mOrderList;

    public PaymentOrdersAdapter(Context context, List<TradeRecord4AppVO> mOrderList) {
        this.mOrderList = mOrderList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mOrderList.size();
    }

    @Override
    public TradeRecord4AppVO getItem(int position) {
        return mOrderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.payment_order_item, null);

            viewHolder.month = (TextView) convertView.findViewById(R.id.month);
            viewHolder.day = (TextView) convertView.findViewById(R.id.day);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.consume_money = (TextView) convertView.findViewById(R.id.consume_money);
            viewHolder.consume_type = (TextView) convertView.findViewById(R.id.consume_type);
            viewHolder.type_icon = (ImageView) convertView.findViewById(R.id.type_icon);
            viewHolder.wait_to_pay = (ImageView) convertView.findViewById(R.id.wait_to_pay);
//            viewHolder.revoke = (ImageView) convertView.findViewById(R.id.revoke);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TradeRecord4AppVO ordermodel = mOrderList.get(position);

        if(ordermodel.getMonth() != null && !"".equals(ordermodel.getMonth())){
            viewHolder.month.setVisibility(View.VISIBLE);
            viewHolder.month.setText(ordermodel.getMonth() + "月");
        }else {
            viewHolder.month.setVisibility(View.GONE);
        }

        viewHolder.day.setText(ordermodel.getWeek());
        viewHolder.time.setText(ordermodel.getTime());
        viewHolder.consume_money.setText(ordermodel.getMoneyStr());

        viewHolder.consume_type.setText(ordermodel.getPayTypeName());
        if("轮胎余额支付".equals(ordermodel.getPayTypeName())){
            viewHolder.type_icon.setImageResource(R.drawable.tyre_icon_0);
        }else if("油卡支付".equals(ordermodel.getProduct())){
            viewHolder.type_icon.setImageResource(R.drawable.oil_icon_0);
        }else if("维修消费".equals(ordermodel.getProduct())){
            viewHolder.type_icon.setImageResource(R.drawable.maintain_icon);
        }else {
            viewHolder.type_icon.setImageResource(R.drawable.xf);
        }

//        if("扫码消费".equals(ordermodel.getTradeName()) ||
//                "商城消费".equals(ordermodel.getTradeName()) ){
//            if(ordermodel.getRevoke()){
//                if("1".equals(ordermodel.getStatus())){
//                    viewHolder.wait_to_pay.setBackgroundResource(R.drawable.wait_to_pay_tip);
//                    viewHolder.wait_to_pay.setVisibility(View.VISIBLE);
//                }else if("2".equals(ordermodel.getStatus())){
//                    viewHolder.wait_to_pay.setBackgroundResource(R.drawable.wait_to_confirm);
//                    viewHolder.wait_to_pay.setVisibility(View.VISIBLE);
//                }else viewHolder.wait_to_pay.setVisibility(View.GONE);
//
//                if("11".equals(ordermodel.getPayType())){//消费账户余额支付单子
//                    viewHolder.wait_to_pay.setBackgroundResource(R.drawable.wait_to_confirm);
//                    viewHolder.wait_to_pay.setVisibility(View.VISIBLE);
//                }
//            }else {
//                viewHolder.wait_to_pay.setVisibility(View.GONE);
//            }
//        }else if("提油消费".equals(ordermodel.getTradeName())){
//            if(ordermodel.getCancelable()){
//                viewHolder.wait_to_pay.setBackgroundResource(R.drawable.revoke);
//                viewHolder.wait_to_pay.setVisibility(View.VISIBLE);
//            }else {
//                if("1".equals(ordermodel.getStatus())){
//                    viewHolder.wait_to_pay.setBackgroundResource(R.drawable.wait_to_pay_tip);
//                    viewHolder.wait_to_pay.setVisibility(View.VISIBLE);
//                }else if("2".equals(ordermodel.getStatus())){
//                    viewHolder.wait_to_pay.setBackgroundResource(R.drawable.wait_to_confirm);
//                    viewHolder.wait_to_pay.setVisibility(View.VISIBLE);
//                }else viewHolder.wait_to_pay.setVisibility(View.GONE);
//            }
//        }

        if(ordermodel.getOperateType() == 2){
            //司机扫码,车主代付
            if("1".equals(ordermodel.getStatus())){
                viewHolder.wait_to_pay.setBackgroundResource(R.drawable.wait_to_pay_tip);
                viewHolder.wait_to_pay.setVisibility(View.VISIBLE);
            }else {
                viewHolder.wait_to_pay.setVisibility(View.GONE);
            }
        }else {
            //车主扫码,车主支付
            // 消费账户
            if("11".equals(ordermodel.getPayType()) ){
                //状态-1未支付-2等待回执
                if("1".equals(ordermodel.getStatus()) || "2".equals(ordermodel.getStatus())) {
                    if(ordermodel.getRevoke()){
                        // 展示"待确认"按钮
                        viewHolder.wait_to_pay.setBackgroundResource(R.drawable.wait_to_confirm);
                        viewHolder.wait_to_pay.setVisibility(View.VISIBLE);
                    }else {
                        viewHolder.wait_to_pay.setVisibility(View.GONE);
                    }
                }else {
                    viewHolder.wait_to_pay.setVisibility(View.GONE);
                }
            }else if(ordermodel.getCancelable()) {
                // 展示"撤销"按钮
                viewHolder.wait_to_pay.setBackgroundResource(R.drawable.revoke);
                viewHolder.wait_to_pay.setVisibility(View.VISIBLE);
            }else {
                viewHolder.wait_to_pay.setVisibility(View.GONE);
            }
        }


        return convertView;
    }

    static class ViewHolder {
        TextView month,day,time,consume_money,consume_type;
        ImageView type_icon,wait_to_pay;
    }
}
