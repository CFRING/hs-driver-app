package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.AccountAdapter;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.AcctInfoVO;
import com.hongshi.wuliudidi.model.TradeRecordVO;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.qr.ConfirmGoodsActivity;
import com.hongshi.wuliudidi.utils.CloseRefreshTask;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.hongshi.wuliudidi.view.TextViewWithDrawable;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huiyuan
 * Created by huiyuan on 2017/6/5.
 */

public class OilAccountActivity extends Activity implements View.OnClickListener {

    private DiDiTitleView oil_account_title;
    private PullToRefreshListView mPullToRefreshListView;
    private ListView detailListview;
    private AccountAdapter accountAdapter;
    private TextViewWithDrawable withdrawal,scan_payment,bank_card;
    private ImageView time_shift_detail,no_data_tip;
    private TextView account_money;

    private int currentPage = 1;
    private boolean isEnd = false;
    private boolean isOilCardBanded = false;

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if("com.action.oil".equals(action)){
                getSummaryData();
                sessionLoadData(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.oil_account_activity);

        initViews();
        getSummaryData();
        sessionLoadData(true);
    }

    private void initViews(){
        oil_account_title = (DiDiTitleView) findViewById(R.id.oil_account_title);
        oil_account_title.setBack(this);
        oil_account_title.setTitle("油卡余额");

        account_money = (TextView) findViewById(R.id.account_money);
        withdrawal = (TextViewWithDrawable) findViewById(R.id.withdrawal);
        scan_payment = (TextViewWithDrawable) findViewById(R.id.scan_payment);
        bank_card = (TextViewWithDrawable) findViewById(R.id.bank_card);
        time_shift_detail = (ImageView) findViewById(R.id.time_shift_detail);
        no_data_tip = (ImageView) findViewById(R.id.no_data_tip);

        withdrawal.setOnClickListener(this);
        scan_payment.setOnClickListener(this);
        bank_card.setOnClickListener(this);
        time_shift_detail.setOnClickListener(this);

        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.account_detail_list);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载更多");

        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel("松开刷新");
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel("刷新中");
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel("下拉刷新");

        detailListview = mPullToRefreshListView.getRefreshableView();
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
                            Toast.makeText(OilAccountActivity.this, "已经是最后一页", Toast.LENGTH_SHORT).show();
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
        accountAdapter = new AccountAdapter(OilAccountActivity.this,accountDetailList);
        detailListview.setAdapter(accountAdapter);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.action.oil");
        registerReceiver(mRefreshBroadcastReceiver,intentFilter);
    }

        private final String account_detail_url = GloableParams.HOST + "tradeRecord/list.do?";
//    private final String account_detail_url = "http://192.168.158.150:8080/gwcz/tradeRecord/list.do?";
    private List<TradeRecordVO> accountDetailList = new ArrayList<>();
    private void sessionLoadData(final boolean isRefresh){

        if(isRefresh){
            currentPage = 1;
        }
        AjaxParams params = new AjaxParams();
        params.put("acctType", "2");
        params.put("tradeType",type);
        params.put("beginTime", start_time);
        params.put("endTime",end_time);
        params.put("currentPage", ""+currentPage);
        params.put("pageSize","10");

        DidiApp.getHttpManager().sessionPost(OilAccountActivity.this, account_detail_url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                JSONObject jsonObject;
                try {
                    if(isRefresh){
                        if(accountDetailList.size() > 0){
                            accountDetailList.clear();
                        }
                        isEnd = false;
                        mPullToRefreshListView.onRefreshComplete();
                    }
                    jsonObject = new JSONObject(t);
                    JSONObject body = jsonObject.getJSONObject("body");

                    String all = body.getString("items");
                    //判断是否还有下一页
                    isEnd = body.getBoolean("end");
                    currentPage = body.getInt("currentPage");

                    List<TradeRecordVO> tempList = JSON.parseArray(all,TradeRecordVO.class);
                    accountDetailList.addAll(tempList);
                    if(accountDetailList.size() > 0){
                        no_data_tip.setVisibility(View.GONE);
                        mPullToRefreshListView.setVisibility(View.VISIBLE);
                    }else {
                        no_data_tip.setImageResource(R.drawable.driver_transit_record_null);
                        no_data_tip.setVisibility(View.VISIBLE);
                    }
                    accountAdapter.notifyDataSetChanged();
                    mPullToRefreshListView.onRefreshComplete();
                } catch (JSONException e) {
                    mPullToRefreshListView.onRefreshComplete();
                }
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                mPullToRefreshListView.onRefreshComplete();
            }
        });
    }


        private final String account_summary_url = GloableParams.HOST + "carrier/app/acct/acctInfo.do";
//    private final String account_summary_url = "http://192.168.158.150:8080/gwcz/carrier/app/acct/acctInfo.do";
    private void getSummaryData(){
        AjaxParams params = new AjaxParams();
        params.put("acctType","2");

        DidiApp.getHttpManager().sessionPost(OilAccountActivity.this, account_summary_url, params, new ChildAfinalHttpCallBack() {

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                Toast.makeText(OilAccountActivity.this,errMsg,Toast.LENGTH_LONG).show();
            }

            @Override
            public void data(String t) {
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    String all = jsonObject.getString("body");
                    AcctInfoVO acctInfoVO = JSON.parseObject(all,AcctInfoVO.class);

                    isOilCardBanded = acctInfoVO.getHasOilCard();
                    account_money.setText(acctInfoVO.getBalance());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.withdrawal:
                //联系客服
//                Util.call(OilAccountActivity.this, getResources().getString(R.string.contact_number));
                if(isOilCardBanded){
                    Intent intent = new Intent(this,OilCardWidtrawlActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(this,"请联系运输主管添加油卡!",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.scan_payment:
                Intent scannerIntent = new Intent(this, ConfirmGoodsActivity.class);
                scannerIntent.putExtra("from","oil_activity");
                startActivity(scannerIntent);
                break;
            case R.id.bank_card:
                Intent intent0 = new Intent(this,OilCardsActivity.class);
                startActivity(intent0);
                break;
            case R.id.time_shift_detail:
                Intent intent1 = new Intent(OilAccountActivity.this,FilterActivity.class);
                intent1.putExtra("account_value",2);
                startActivityForResult(intent1,2);
                break;
            default:
                break;
        }
    }

    private String start_time = "";
    private String end_time = "";
    private String type = "";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2 && data != null){
            start_time = data.getStringExtra("start_time");
            end_time = data.getStringExtra("end_time");
            String type_ = data.getStringExtra("type");
            if("消费".equals(type_)){
                type = "1";
            }else if("提油".equals(type_)){
                type = "2";
            }else if("结算".equals(type_)){
                type = "3";
            }else {
                type = "";
            }
            sessionLoadData(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRefreshBroadcastReceiver);
    }
}
