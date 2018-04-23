package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.OneTruckHistoryAdapter;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.OneTruckHistoryModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.CloseRefreshTask;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;

import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huiyuan
 */
public class OneTruckHistoryActivity extends Activity {

    private final String TAG = "OneTruckHistoryActivity";
    private final String URL = GloableParams.HOST + "carrier/poundBillQuery/findAndPage4App.do";

    private DiDiTitleView mTitle;
    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListView;
    private OneTruckHistoryAdapter mAdapter;
    private List<OneTruckHistoryModel> mList;
    //当前页
    private int currentPage = 0;
    //每页条数
    private final int PAGE_SIZE = 10;
    //是否以显示所有条目
    private Boolean isEnd;
    private String monthStr = "";
    private String yearStr = "";
    private int statisticsTag = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_truck_history);
        monthStr = getIntent().getStringExtra("monthStr");
        yearStr = getIntent().getStringExtra("yearStr");
        statisticsTag = getIntent().getIntExtra("statisticsTag",-1);
        mTitle = (DiDiTitleView) findViewById(R.id.one_truck_history_title);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.one_truck_history_pull);
        init();
    }

    private void init(){
        mTitle.setBack(this);
        mTitle.setTitle(getIntent().getStringExtra("truckNumber"));
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.pull_up_load_more));
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.loading));
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.loosen_to_load_more));

        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel(getString(R.string.loosen_to_refresh));
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getString(R.string.refreshing));
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel(getString(R.string.pull_down_refresh));

        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新重置
                mList.clear();
                currentPage = 0;
                getData(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isEnd) {
                    Toast.makeText(OneTruckHistoryActivity.this, getString(R.string.end_page), Toast.LENGTH_SHORT).show();
                    CloseRefreshTask closeRefreshTask = new CloseRefreshTask(mPullToRefreshListView);
                    closeRefreshTask.execute();
                } else {
                    getData(false);
                }
            }
        });
        //初始化ListView
        mListView = mPullToRefreshListView.getRefreshableView();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OneTruckHistoryModel model = mList.get(position - 1);
                Intent intent = new Intent(OneTruckHistoryActivity.this, TruckTransitDetailActivity.class);
                intent.putExtra("truckNumber", model.getTruckNumber());
                intent.putExtra("billCode", model.getBillCode());
                intent.putExtra("fullWeight", model.getFullWeight());
                intent.putExtra("categoryText", model.getCategoryText());
                intent.putExtra("netWeight", model.getNetWeight());
                intent.putExtra("pkCorpName", model.getPkCorpName());
                intent.putExtra("supplier", model.getSupplier());
                intent.putExtra("weightText", model.getWeightText());
                intent.putExtra("sumDate", model.getSumDate());
                intent.putExtra("fromAddr", model.getFromAddr());
                intent.putExtra("id",model.getId());
                startActivity(intent);
            }
        });
        mList = new ArrayList<>();
        mAdapter = new OneTruckHistoryAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        getData(true);
    }

    private void getData(final Boolean isFirst){
        AjaxParams params = new AjaxParams();
        final PromptManager promptManager = new PromptManager();
        if (isFirst) {
            promptManager.showProgressDialog1(OneTruckHistoryActivity.this, "加载中");
        }
        Intent intent = getIntent();
        params.put("currentPage", currentPage + 1 + "");
        params.put("pageSize", PAGE_SIZE + "");
        String s[] = {intent.getStringExtra("truckNumber")};
        params.put("truckNumberJson", Util.enCode(JSON.toJSONString(s)));
        if(statisticsTag == 1){
            //月
            params.put("monthStr",monthStr);
            params.put("yearStr",yearStr);
        }else if(statisticsTag == 2){
            //年
            params.put("yearStr",yearStr);
        }
//        params.put("yearOrMonthSum", "0");
        DidiApp.getHttpManager().sessionPost(OneTruckHistoryActivity.this, URL, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                if (isFirst) {
                    promptManager.closeProgressDialog();
                } else {
                    mPullToRefreshListView.onRefreshComplete();
                }
                JSONObject jsonObject = JSON.parseObject(t);
                JSONObject body = jsonObject.getJSONObject("body");
                currentPage = body.getInteger("currentPage");
                isEnd = body.getBoolean("end");
                mList.addAll(JSON.parseArray(body.getString("items"), OneTruckHistoryModel.class));
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                if (isFirst) {
                    promptManager.closeProgressDialog();
                } else {
                    mPullToRefreshListView.onRefreshComplete();
                }
            }
        });
    }
}
