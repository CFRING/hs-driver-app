package com.hongshi.wuliudidi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.AuctionDetailsActivity;
import com.hongshi.wuliudidi.activity.AuctionOfferActivity;
import com.hongshi.wuliudidi.activity.LoginActivity;
import com.hongshi.wuliudidi.activity.ResultActivity;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.AuctionDoBid;
import com.hongshi.wuliudidi.model.RecgGoodsModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.Util;

import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huiyuan on 2017/4/26.
 */

public class RecgGoodsAdapter extends BaseAdapter {
    private Context mContext;
    private List<RecgGoodsModel> mOrderList;

    public RecgGoodsAdapter(Context context, List<RecgGoodsModel> mOrderList) {
        this.mOrderList = mOrderList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mOrderList.size();
    }

    @Override
    public RecgGoodsModel getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.recg_goods_item, null);
            viewHolder = new ViewHolder();
            viewHolder.start_city = (TextView) convertView.findViewById(R.id.start_city);
            viewHolder.end_city = (TextView) convertView.findViewById(R.id.end_city);
            viewHolder.goods_name = (TextView) convertView.findViewById(R.id.goods_name);
            viewHolder.goods_count = (TextView) convertView.findViewById(R.id.goods_count);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
//            viewHolder.auction_image = (ImageView) convertView.findViewById(R.id.auction_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        viewHolder.start_city.setWidth(width/2);
        viewHolder.end_city.setWidth(width - width/2);

        final RecgGoodsModel model = mOrderList.get(position);
        if(model.isSenderCorporation()){
            viewHolder.start_city.setText(model.getSendDis());
        }else {
            viewHolder.start_city.setText(model.getSendAddr() + model.getSendDis());
        }

        if(model.isRecCorporation()){
            viewHolder.end_city.setText(model.getRecvDis());
        }else {
            viewHolder.end_city.setText(model.getRecvAddr() + model.getRecvDis());
        }

        viewHolder.goods_name.setText(model.getGoodsName());
        viewHolder.goods_count.setText(Util.formatDoubleToString(Double.valueOf(model.getGoodsAmount()),model.getAssignUnitText()) + model.getAssignUnitText() );

        if(Util.isLogin(mContext)){
            viewHolder.price.setVisibility(View.VISIBLE);
            viewHolder.price.setText(model.getAuctionPrice() + "元/" + model.getSettleUnitText());
        }else {
            viewHolder.price.setVisibility(View.GONE);
        }
//        if(model.getOneKeyRec() == 1){
//            viewHolder.auction_image.setImageResource(R.drawable.bid_none);
//            viewHolder.auction_image.setVisibility(View.VISIBLE);
//            viewHolder.auction_image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(Util.isLogin(mContext)){
//                        auction(model);
//                    }else {
//                        Intent intent = new Intent(mContext, LoginActivity.class);
//                        mContext.startActivity(intent);
//                    }
//                }
//            });
//        }else if(model.getOneKeyRec() ==2){
//            viewHolder.auction_image.setImageResource(R.drawable.bid_none);
//            viewHolder.auction_image.setVisibility(View.VISIBLE);
//            viewHolder.auction_image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(Util.isLogin(mContext)){
//                        Intent intent = new Intent(mContext, AuctionDetailsActivity.class);
//                        intent.putExtra("auctionId", model.getAuctionId());
//                        mContext.startActivity(intent);
//                    }else {
//                        Intent intent = new Intent(mContext, LoginActivity.class);
//                        mContext.startActivity(intent);
//                    }
//                }
//            });
//        }else {
//            viewHolder.auction_image.setImageResource(R.drawable.can_not_bide);
//            viewHolder.auction_image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(Util.isLogin(mContext)){
//                        Intent intent = new Intent(mContext, AuctionDetailsActivity.class);
//                        intent.putExtra("auctionId", model.getAuctionId());
//                        mContext.startActivity(intent);
//                    }else {
//                        Intent intent = new Intent(mContext, LoginActivity.class);
//                        mContext.startActivity(intent);
//                    }
//                }
//            });
//        }

        return convertView;
    }

    static class ViewHolder {
        TextView start_city,end_city,goods_name,goods_count,price;
//        ImageView auction_image;
    }

//    private String auction_url = GloableParams.HOST + "carrier/bid/dobid.do?";// 竞拍出价
//    private void auction(RecgGoodsModel model) {
//
//        AuctionDoBid mAuctionDoBid;
//        List<String> truckIds = new ArrayList<>();
//        if(model.getAuctionType()== CommonRes.FIXED_PRICE){
//            mAuctionDoBid = new AuctionDoBid(model.getAuctionId(),
//                    1, model.getAuctionPrice(), truckIds);
//        }else {
//            Toast.makeText(mContext,"非一口价单子!",Toast.LENGTH_LONG).show();
//            return;
//        }
//        mAuctionDoBid.setBillTemplateId("");
//        mAuctionDoBid.setFromFrontPage(true);
//        String jsonString = JSON.toJSONString(mAuctionDoBid);
//        final PromptManager mPromptManager = new PromptManager();
//        mPromptManager.showProgressDialog(mContext, "请稍等");
//        AjaxParams params = new AjaxParams();
//        params.put("bidJson", jsonString);
//        DidiApp.getHttpManager().sessionPost(mContext,
//                auction_url, params, new ChildAfinalHttpCallBack() {
//                    @Override
//                    public void data(String t) {
//                        mPromptManager.closeProgressDialog();
//                        if(!t.equals("")){
//                            Intent check_intent = new Intent(mContext, ResultActivity.class);
//                            check_intent.putExtra("result", 1);
//                            mContext.startActivity(check_intent);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(String errCode, String errMsg, Boolean errSerious) {
//                        mPromptManager.closeProgressDialog();
////						Log.d("huiyuan","确认接单错误信息:" + errMsg);
//                    }
//                });
//    }
}
