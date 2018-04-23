package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.Intent;
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
import com.hongshi.wuliudidi.adapter.TransitRecordDetailAdapter;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.ClassfyStatVO;
import com.hongshi.wuliudidi.model.TransitRecordDetailModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.CloseRefreshTask;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by huiyuan on 2017/8/23.
 */

public class TransitRecordDetailActivity extends Activity {
    private ImageView back;
    private TextView date_text,total_money_text,cash_text,oil_text,tyre_text,consume_text;
    private PullToRefreshListView mPullToRefreshListView;
    private ListView listView;

    private List<TransitRecordDetailModel> transitRecordDetailModelList = new ArrayList<>();
    private TransitRecordDetailAdapter  transitRecordDetailAdapter;

    private int currentPage = 1;
    private boolean isEnd = false;

    private final String transit_record_detail_url = GloableParams.HOST + "carrier/app/bill/classifyStatRecordList.do";
    private String billCycleJsonStr;
    private String periodText;
    private int classifyStatType = 1;
    private ClassfyStatVO classfyStatVO;
    private boolean showCompany = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        periodText = intent.getStringExtra("periodText");
        billCycleJsonStr = intent.getStringExtra("billCycleJsonStr");
        classifyStatType = intent.getIntExtra("classifyStatType",1);
        classfyStatVO =(ClassfyStatVO) intent.getSerializableExtra("classfyStatVO");
        showCompany = intent.getBooleanExtra("showCompany",false);
        setContentView(R.layout.transit_record_detail_activity);

        initView();
    }

    private void initView(){
        back = (ImageView) findViewById(R.id.back);
        date_text = (TextView) findViewById(R.id.date_text);
        total_money_text = (TextView) findViewById(R.id.total_money_text);
        cash_text = (TextView) findViewById(R.id.cash_text);
        oil_text = (TextView) findViewById(R.id.oil_text);
        tyre_text = (TextView) findViewById(R.id.tyre_text);
        consume_text = (TextView) findViewById(R.id.consume_text);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.detail_list);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载更多");

        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel("松开刷新");
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel("刷新中");
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel("下拉刷新");

        listView = mPullToRefreshListView.getRefreshableView();
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mPullToRefreshListView.isRefreshing()) {
                    if (mPullToRefreshListView.isHeaderShown()){
                        sessionLoadData(true);
                    } else if (mPullToRefreshListView.isFooterShown()) {
                        // 加载更多
                        if(isEnd){
                            Toast.makeText(TransitRecordDetailActivity.this, "已经是最后一页", Toast.LENGTH_SHORT).show();
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

        date_text.setText(periodText);
        if(classfyStatVO != null){
            total_money_text.setText(classfyStatVO.getMoney());
            cash_text.setText(classfyStatVO.getSalaryMoney());
            oil_text.setText(classfyStatVO.getOilMoney());
            tyre_text.setText(classfyStatVO.getTyreMoney());
            consume_text.setText(classfyStatVO.getConsumptionMoney());
        }
        transitRecordDetailAdapter =
                new TransitRecordDetailAdapter(TransitRecordDetailActivity.this,
                        transitRecordDetailModelList,showCompany);
        listView.setAdapter(transitRecordDetailAdapter);
        sessionLoadData(true);
    }


    private void sessionLoadData(final boolean isRefresh){
        if(isRefresh){
            currentPage = 1;
        }
        AjaxParams params = new AjaxParams();
        params.put("billCycleJsonStr", billCycleJsonStr);
        params.put("classifyStatType","" + classifyStatType);
        if(classfyStatVO != null){
        params.put("classifyId",classfyStatVO.getClassifyId());
        }
        params.put("currentPage",""+ currentPage);
        params.put("pageSize","10");
        DidiApp.getHttpManager().sessionPost(TransitRecordDetailActivity.this, transit_record_detail_url, params, new ChildAfinalHttpCallBack() {

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                mPullToRefreshListView.setVisibility(View.GONE);
            }

            @Override
            public void data(String t) {
                if(isRefresh){
                    isEnd = false;
                    if(transitRecordDetailModelList.size() > 0){
                        transitRecordDetailModelList.clear();
                    }
                    mPullToRefreshListView.onRefreshComplete();
                }
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    String all = jsonObject.getString("body");
                    if(all != null && !"".equals(all)){
                        JSONObject jsonObject1 = new JSONObject(all);
                        currentPage = jsonObject1.optInt("currentPage");
                        isEnd = jsonObject1.optBoolean("end");
                        String itemsJson = jsonObject1.optString("items");
                        List<TransitRecordDetailModel> tmpList = JSON.parseArray(itemsJson,TransitRecordDetailModel.class);
                        if(isRefresh){
                            transitRecordDetailModelList = tmpList;
                            transitRecordDetailAdapter =
                                    new TransitRecordDetailAdapter(TransitRecordDetailActivity.this,
                                            transitRecordDetailModelList,showCompany);
                            listView.setAdapter(transitRecordDetailAdapter);
                        }else {
                            transitRecordDetailModelList.addAll(tmpList);
                            transitRecordDetailAdapter.notifyDataSetChanged();
                        }

                        if(transitRecordDetailModelList.size() <= 0){
                            mPullToRefreshListView.setVisibility(View.GONE);
                        }else {
                            mPullToRefreshListView.setVisibility(View.VISIBLE);
                        }
                    }
                    mPullToRefreshListView.onRefreshComplete();
                } catch (JSONException e) {
                    e.printStackTrace();
                    mPullToRefreshListView.onRefreshComplete();
                }
            }
        });
    }
}
