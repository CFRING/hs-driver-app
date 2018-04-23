package com.hongshi.wuliudidi.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.http.AjaxParams;

import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.RatingActivity;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.RefreshAdapterCallBack;
import com.hongshi.wuliudidi.model.AllAuctionModel;
import com.hongshi.wuliudidi.model.AllAuctionModelForEavelate;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.Util;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TransportationTaskAdapterNew extends BaseAdapter {

    private Context mContext;
    private List<AllAuctionModelForEavelate> mOrderList;
//    private String del_url = GloableParams.HOST + "carrier/bid/delete.do?";
//    private String cancel_url = GloableParams.HOST + "carrier/bid/cancelbid.do?";
    private RefreshAdapterCallBack mRefreshAdapterCallBack;
//    private ShowType showtype = ShowType.ShowTYD;
    private boolean isDelMessage = false;
    private FinalBitmap finalBitmap;


    public TransportationTaskAdapterNew(Context context, List<AllAuctionModelForEavelate> mOrderList,RefreshAdapterCallBack mRefreshAdapterCallBack) {
        this.mOrderList = mOrderList;
        this.mContext = context;
        this.mRefreshAdapterCallBack = mRefreshAdapterCallBack;
        finalBitmap = FinalBitmap.create(context);
    }

    public void addList(List<AllAuctionModelForEavelate> mOrderList){
        this.mOrderList.addAll(mOrderList);
    }
    public void delMessage(boolean isDelMessage){
        this.isDelMessage = isDelMessage;
    }
    @Override
    public int getCount() {
        return mOrderList.size();
    }

    @Override
    public AllAuctionModelForEavelate getItem(int position) {
        return mOrderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
//		if (convertView == null) {
        convertView = View.inflate(mContext, R.layout.task_list_item_new, null);
        viewHolder = new ViewHolder();
        viewHolder.startCity = (TextView) convertView.findViewById(R.id.start_city);
        viewHolder.endCity = (TextView) convertView.findViewById(R.id.end_city);
        viewHolder.goodsName = (TextView) convertView.findViewById(R.id.task_item_goods_name);
        viewHolder.goodsWeight = (TextView) convertView.findViewById(R.id.task_item_goods_weight);
        viewHolder.price = (TextView) convertView.findViewById(R.id.buttom_type_name);
        viewHolder.order_state = (TextView) convertView.findViewById(R.id.task_order_state);
//        viewHolder.click_view = (TextView) convertView.findViewById(R.id.click_view);
        //显示消息
        viewHolder.message_view = (TextView) convertView.findViewById(R.id.message_news);
        viewHolder.message_news_layout = (RelativeLayout) convertView.findViewById(R.id.message_news_layout);
        viewHolder.state_bg = (RelativeLayout) convertView.findViewById(R.id.state_bg);
        viewHolder.truck_number = (TextView) convertView.findViewById(R.id.truck_number);
        viewHolder.name = (TextView) convertView.findViewById(R.id.name);
        viewHolder.head_image = (ImageView) convertView.findViewById(R.id.head_image);
        viewHolder.evalute = (ImageView) convertView.findViewById(R.id.evalute);

        viewHolder.name.setVisibility(View.GONE);
        viewHolder.head_image.setVisibility(View.GONE);
        //中标单号
//        viewHolder.order_type_name = (TextView) convertView.findViewById(R.id.order_type_name);
        convertView.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) convertView.getTag();
//		}
        final AllAuctionModelForEavelate ordermodel = mOrderList.get(position);

        if(ordermodel.getOptId() == null || "".equals(ordermodel.getOptId())){
            viewHolder.evalute.setVisibility(View.GONE);
        }else {
            viewHolder.evalute.setVisibility(View.VISIBLE);
            if(ordermodel.getIsJudged()){
                viewHolder.evalute.setImageResource(R.drawable.look_evalute);
            }else {
                viewHolder.evalute.setImageResource(R.drawable.wait_evalute);
            }
            viewHolder.evalute.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,RatingActivity.class);
                    intent.putExtra("model",ordermodel);
                    intent.putExtra("isJudged",ordermodel.getIsJudged());
                    mContext.startActivity(intent);
                }
            });
        }


        String bidId = ordermodel.getBidItemId();

        viewHolder.truck_number.setText(ordermodel.getTruckNo());
        viewHolder.startCity.setText(ordermodel.getSendAddr());
        viewHolder.endCity.setText(ordermodel.getRecvAddr());

        viewHolder.goodsName.setText(ordermodel.getGoodsName());
        viewHolder.goodsWeight.setText(
                Util.formatDoubleToString(ordermodel.getGoodsAmount(), ordermodel.getAssignUnitText())
                        + ordermodel.getAssignUnitText());
        String price = Util.formatDoubleToString(ordermodel.getBidPrice(), "元");
//		if(CommonRes.roleType == 3){
//			viewHolder.price.setVisibility(View.GONE);
//		}else{
        viewHolder.price.setVisibility(View.VISIBLE);
        String price_str = "价格  "+price+" 元/"+ ordermodel.getSettleUnitText();
        Util.setText(mContext, price_str, price, viewHolder.price, R.color.theme_color);
//			viewHolder.price.setText("竞价"+price+"元/"+ ordermodel.getSettleUnitText());
//		}
//		status(ordermodel.getStatus());
        //暂时隐藏小红点，以后可能还会用到 TODO
        int messageNumber = 0;
        if(isDelMessage){
//			viewHolder.message_news_layout.setVisibility(View.GONE);
//			viewHolder.state_bg.setVisibility(View.VISIBLE);
            CommonRes.taskIdList.clear();
        }else{
//			viewHolder.message_news_layout.setVisibility(View.GONE);
//			viewHolder.state_bg.setVisibility(View.VISIBLE);
//			for(int i=0;i<CommonRes.taskIdList.size();i++){
//				if(ordermodel.getBidItemId().equals(CommonRes.taskIdList.get(i))){
//					viewHolder.message_news_layout.setVisibility(View.VISIBLE);
//					viewHolder.state_bg.setVisibility(View.GONE);
//					messageNumber++;
//				}
//			}
//			viewHolder.message_view.setText(""+messageNumber);
        }
        switch (ordermodel.getStatus()) {
            //待审核
            case 1:
//                delOrCancelOrder(viewHolder.click_view,bidId,false,position);
                break;
            //审核中
            case 2:
//                viewHolder.click_view.setVisibility(View.GONE);
                break;
            //取消
            case 3:
//                delOrCancelOrder(viewHolder.click_view,bidId,true,position);
                break;
            //未通过
            case 4:
//                delOrCancelOrder(viewHolder.click_view,bidId,true,position);
                break;
            //运输未开始
            case 5:
//                viewHolder.click_view.setVisibility(View.GONE);
                viewHolder.state_bg.setBackgroundResource(R.drawable.do_bg);
                break;
            //运输中
            case 6:
//                viewHolder.click_view.setVisibility(View.GONE);
                viewHolder.state_bg.setBackgroundResource(R.drawable.doing_bg);
                break;
            //已完成
            case 7:
//					delOrCancelOrder(viewHolder.click_view,bidId,true,position);
                viewHolder.state_bg.setBackgroundResource(R.drawable.done_bg);
                break;
            default:
                break;
        }
        viewHolder.order_state.setText(ordermodel.getStatusText());

        if(CommonRes.roleType == 1){
            //如果用户为普通司机
            RelativeLayout priceLayout = (RelativeLayout) convertView.findViewById(R.id.price_layout);
            priceLayout.setVisibility(View.GONE);
        }
        return convertView;
    }
//    private void delOrCancelOrder(final TextView click_view,final String bidItemId,final boolean isDel,final int position){
//        if(isDel){
//            click_view.setVisibility(View.VISIBLE);
//            click_view.setText("删除");
//        }else{
//            click_view.setVisibility(View.VISIBLE);
//            click_view.setText("取消接单");
//        }
//        click_view.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                AjaxParams params = new AjaxParams();
//                params.put("bidItemId", bidItemId);
//                String url = "";
//                if(isDel){
//                    url = del_url;
//                }else{
//                    url = cancel_url;
//                }
//                DidiApp.getHttpManager().sessionPost(mContext, url, params, new AfinalHttpCallBack() {
//                    @Override
//                    public void data(String t) {
//                        if(isDel){
//                            Log.d("huiyuan","删除接单记录");
//                            mOrderList.remove(position);
//                            ToastUtil.show(mContext, "删除成功");
//                            mRefreshAdapterCallBack.isAccept(true);
//                        }else{
//                            ToastUtil.show(mContext, "取消成功");
//                            mRefreshAdapterCallBack.isAccept(false);
//                        }
//                    }
//                });
//            }
//        });
//
//    }
//    public void setShowType(ShowType showtype){
//        this.showtype = showtype;
//    }
    static class ViewHolder {
        TextView startCity,endCity,truck_number,goodsName,goodsWeight,price,order_state,name;
        TextView message_view;
        ImageView head_image,evalute;
        RelativeLayout message_news_layout,state_bg;
    }
    public enum ShowType{
        ShowJJD, ShowTYD;
    }
}
