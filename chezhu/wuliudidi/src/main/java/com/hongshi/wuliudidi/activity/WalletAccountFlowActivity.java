package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.WalletAccountFlowAdapter;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.WalletAccountFlowModel;
import com.hongshi.wuliudidi.model.WalletAccountModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.CloseRefreshTask;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.hongshi.wuliudidi.view.NullDataView;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author huiyuan
 */
public class WalletAccountFlowActivity extends Activity {
    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListView;
    private List<WalletAccountFlowModel> list = new LinkedList<>();
    private WalletAccountFlowAdapter adapter;
    private DiDiTitleView mTitle;
    private NullDataView nullDataView;
    private String getDataUrl = GloableParams.HOST + "uic/user/myWallet/bill.do?";
    //当前处于第几页（后台页码从1开始，故还没加载数据的时候处于第0页）
    private int currentPage = 0;
    private int totalNum = 0;
    private int currentNum = 0;
    private static final int PageSize = 10;

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("WalletAccountFlowActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("WalletAccountFlowActivity");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setContentView(R.layout.wallet_account_flow_activity);
        init();
    }

    private void init(){
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.wallet_pulltorefresh);
        mListView = mPullToRefreshListView.getRefreshableView();
        nullDataView = (NullDataView) findViewById(R.id.no_data_view);
        nullDataView.setInfoHint("暂无资金流水信息");

        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载更多");
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel("松开刷新");
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel("刷新中");
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel("下拉刷新");

        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getData(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if(currentNum >= totalNum){
                    ToastUtil.show(WalletAccountFlowActivity.this, "已经是最后一页");
                    CloseRefreshTask closeRefreshTask = new CloseRefreshTask(mPullToRefreshListView);
                    closeRefreshTask.execute();
                }else{
                    getData(false);
                }
            }
        });

        mTitle = (DiDiTitleView) findViewById(R.id.record_title);
        mTitle.setBack(this);
        mTitle.setTitle(getResources().getString(R.string.account_flow));

        getData(true);
    }

    private void getData(final boolean isRefresh){
        if(isRefresh){
            list.clear();
            currentPage = 0;
            currentNum = 0;
        }
        AjaxParams params = new AjaxParams();
        params.put("currentPage", String.valueOf(currentPage + 1));
        params.put("pageSize", String.valueOf(PageSize));
        final PromptManager mPromptManager = new PromptManager();
		mPromptManager.showProgressDialog1(WalletAccountFlowActivity.this, "加载中");
        DidiApp.getHttpManager().sessionPost(WalletAccountFlowActivity.this, getDataUrl, params, new ChildAfinalHttpCallBack() {
            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                mPullToRefreshListView.onRefreshComplete();
                mPromptManager.closeProgressDialog();
                showData(false);
                //just for test
//                list = new ArrayList<WalletAccountFlowModel>();
//                WalletAccountFlowModel model = new WalletAccountFlowModel();
//                model.setDispTxt("disptxt");
//                model.setStatus("status");
//                model.setMoney(600.41);
//                model.setGmtCreate(new Date());
//                model.setRecordType(1);
//                list.add(model);
//                list.add(model);
//                list.add(model);
//                list.add(model);
//                adapter = new WalletAccountFlowAdapter(WalletAccountFlowActivity.this, list);
//                mListView.setAdapter(adapter);
            }

            @Override
            public void data(String t) {
                mPullToRefreshListView.onRefreshComplete();
                mPromptManager.closeProgressDialog();
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(t);
                    String body = jsonObject.getString("body");
                    WalletAccountModel pageModel = JSON.parseObject(body, WalletAccountModel.class);

                    currentPage = pageModel.getCurrentPage();
                    totalNum = pageModel.getItemCount();

                    Map<String, List<WalletAccountFlowModel>> map = pageModel.getMap();
                    //获取所有月份的Key
                    List<String> monthList = pageModel.getMonth();
                    for (int i = 0; i < monthList.size(); i++) {
                        //每个key下面的list
                        List<WalletAccountFlowModel> itemList = map.get(monthList.get(i));
                        if(itemList != null){
                            currentNum += itemList.size();
                            list.addAll(itemList);
                        }
                    }
                    if(isRefresh){
                        adapter = new WalletAccountFlowAdapter(WalletAccountFlowActivity.this, list);
                        mListView.setAdapter(adapter);
                    }
                    showData(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                    showData(false);
                }
            }
        });
    }

    private void showData(boolean haveData){
        if(haveData){
            mPullToRefreshListView.setVisibility(View.VISIBLE);
            nullDataView.setVisibility(View.GONE);
        }else{
            mPullToRefreshListView.setVisibility(View.GONE);
            nullDataView.setVisibility(View.VISIBLE);
        }
    }
}
