package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.PaymentOrdersAdapter;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.MemberModel;
import com.hongshi.wuliudidi.model.TaskOrderModel;
import com.hongshi.wuliudidi.model.TradeRecord4AppVO;
import com.hongshi.wuliudidi.model.TradeRecordVO;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.CloseRefreshTask;
import com.hongshi.wuliudidi.view.DiDiTitleView;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *@author  Created by huiyuan on 2017/6/6.
 */

public class PaymentOrdersActivity extends Activity {

    private DiDiTitleView title;
    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListview;
    private PaymentOrdersAdapter paymentOrdersAdapter;
    private int currentPage = 1;
    private boolean isEnd = false;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sessionLoadData(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_orders_activity);

        initViews();
        sessionLoadData(true);
    }

    private void initViews(){
        title = (DiDiTitleView) findViewById(R.id.title);
        title.setBack(this);
        title.setTitle("订单列表");

        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.orders_list);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载更多");

        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel("松开刷新");
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel("刷新中");
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel("下拉刷新");

        mListview = mPullToRefreshListView.getRefreshableView();
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mPullToRefreshListView.isRefreshing()) {
                    if (mPullToRefreshListView.isHeaderShown()){
                        currentPage = 1;
                        sessionLoadData(true);
                    } else if (mPullToRefreshListView.isFooterShown()) {
                        // 加载更多
                        if(isEnd){
                            Toast.makeText(PaymentOrdersActivity.this, "已经是最后一页", Toast.LENGTH_SHORT).show();
                            CloseRefreshTask closeRefreshTask = new CloseRefreshTask(mPullToRefreshListView);
                            closeRefreshTask.execute();
                            return;
                        }
                        currentPage = currentPage + 1;
                        sessionLoadData(false);
                    }
                }
            }
        });

        paymentOrdersAdapter = new PaymentOrdersAdapter(PaymentOrdersActivity.this,dataList);
        mListview.setAdapter(paymentOrdersAdapter);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TradeRecord4AppVO tradeRecord4AppVO = dataList.get(position -1);
                Intent intent;
                if(tradeRecord4AppVO.getOperateType() == 2 && "1".equals(tradeRecord4AppVO.getStatus())){
                    intent = new Intent(PaymentOrdersActivity.this,TruckOwnerPayDetailActivity.class);
                    intent.putExtra("detail",tradeRecord4AppVO);
                }else {
                    if(tradeRecord4AppVO.getCancelable()){
                        intent = new Intent(PaymentOrdersActivity.this,RevokeOilWidthdrawlActivity.class);
                        intent.putExtra("detail",tradeRecord4AppVO);
//                }else if(!"11".equals(tradeRecord4AppVO.getPayType()) &&
//                        "1".equals(tradeRecord4AppVO.getStatus())){
//                    intent = new Intent(PaymentOrdersActivity.this,TruckOwnerPayDetailActivity.class);
//                    intent.putExtra("detail",tradeRecord4AppVO);
                    }else {
                        intent = new Intent(PaymentOrdersActivity.this,OrderDetailActivity.class);
                        intent.putExtra("orderId",tradeRecord4AppVO.getOuterId());
                    }
                }
                startActivity(intent);
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.action.cash");
        intentFilter.addAction("com.action.tyre");
        intentFilter.addAction("com.action.oil");
        intentFilter.addAction("com.action.revoke");
        intentFilter.addAction("com.action.wait_to_pay");
        registerReceiver(broadcastReceiver,intentFilter);
    }

    private String url = GloableParams.HOST + "carrier/qrcodepay/orderList.do";
//    private String url = "http://192.168.158.33:8080/gwcz/carrier/qrcodepay/orderList.do";

    private List<TradeRecord4AppVO> dataList = new ArrayList<>();
    private void sessionLoadData(final boolean isRefresh) {

        AjaxParams params = new AjaxParams();

        params.put("currentPage", ""+currentPage);
        params.put("pageSize", "100");

        DidiApp.getHttpManager().sessionPost(PaymentOrdersActivity.this, url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                if(isRefresh){
                    if(dataList != null && dataList.size() > 0){
                        dataList.clear();
                    }
                    isEnd = false;
                    mPullToRefreshListView.onRefreshComplete();
                }
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    JSONObject body = jsonObject.getJSONObject("body");
                    String all = body.getString("items");
                    //判断是否还有下一页
                    isEnd = body.getBoolean("end");
//                    Log.i("http","订单列表 end = " + isEnd);
                    currentPage = body.getInt("currentPage");
                    List<TradeRecord4AppVO> tempList = JSON.parseArray(all,TradeRecord4AppVO.class);
                    dataList.addAll(tempList);
                    paymentOrdersAdapter.notifyDataSetChanged();
                }catch (Exception e){

                }

            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                mPullToRefreshListView.onRefreshComplete();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
