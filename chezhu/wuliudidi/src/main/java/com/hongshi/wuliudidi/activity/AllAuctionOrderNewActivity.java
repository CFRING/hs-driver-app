package com.hongshi.wuliudidi.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.TransportationTaskAdapter;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.RefreshAdapterCallBack;
import com.hongshi.wuliudidi.model.AllAuctionModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.CloseRefreshTask;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * @author huiyuan
 */
public class AllAuctionOrderNewActivity extends Activity{
    private DiDiTitleView mTitle;
    private PullToRefreshListView mPullToRefreshListView;
    private ListView mOrderList;
    private String all_url = GloableParams.HOST + "carrier/bid/allconsign.do";
    private List<AllAuctionModel> mAuctionList = new ArrayList<AllAuctionModel>();
    private List<AllAuctionModel> mMoreAuctionList = new ArrayList<AllAuctionModel>();
    private List<AllAuctionModel> mList = new ArrayList<AllAuctionModel>();
    private TransportationTaskAdapter mTransportationTaskAdapter;
    private boolean isEnd = false;
    /**
     * 加载更多，是否最后一页，在请求里去掉加载的状态
     */
    private boolean tag = false;

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("AllAuctionOrderActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("AllAuctionOrderActivity");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setContentView(R.layout.all_auction_order_new_activity);
        initViews();
        loadData();
    }

    private void initViews(){
        mTitle = (DiDiTitleView) findViewById(R.id.all_title);
        mTitle.setTitle("接单记录");
        mTitle.setBack(this);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.all_auction_listview);

        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载更多");

        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel("松开刷新");
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel("刷新中");
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel("下拉刷新");

        mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mPullToRefreshListView.isRefreshing()) {
                    if (mPullToRefreshListView.isHeaderShown()) {
                        //刷新
                        loadData();
                    } else if (mPullToRefreshListView.isFooterShown()) {
                        // 加载更多
                        if(isEnd){
                            tag = true;
                            ToastUtil.show(AllAuctionOrderNewActivity.this, "已经是最后一页");
                            CloseRefreshTask closeRefreshTask = new CloseRefreshTask(mPullToRefreshListView);
                            closeRefreshTask.execute();
                            return;
                        }
                        loadMoreData();

                    }
                }
            }


        });
        mOrderList = mPullToRefreshListView.getRefreshableView();
        mOrderList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AllAuctionModel mModel = mList.get(position - 1);
                //1待审核, 2审核中, 3已取消, 4审核未通过, 5运输未开始, 6运输中, 7已完成
                ArrayList<Integer> jumpToJJD = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
                ArrayList<Integer> jumpToTYD = new ArrayList<Integer>(Arrays.asList(5, 6, 7));

                //1待审核, 2审核中, 3已取消, 4审核未通过  四种状态的竞价项目点击跳转到竞价单详情页面
                if(jumpToJJD.contains(mModel.getStatus())){
                    Intent intent = new Intent(AllAuctionOrderNewActivity.this,AuctionDetailsActivity.class);
                    intent.putExtra("auctionId", mModel.getAuctionId());
                    startActivity(intent);
                }else if(jumpToTYD.contains(mModel.getStatus())){
                    //5运输未开始, 6运输中, 7已完成 三种状态的竞价项目点击跳转到托运详情页面
                    Intent intent = new Intent(AllAuctionOrderNewActivity.this,WinBidActivity.class);
                    intent.putExtra("AuctionId", mModel.getBidItemId());
                    startActivity(intent);
                }
            }

        });
    }

    private void loadMoreData() {
        AjaxParams params = new AjaxParams();
        if(mAuctionList.size()>0){
            params.put("last", mAuctionList.get(mAuctionList.size()-1).getBidItemId());
        }
        DidiApp.getHttpManager().sessionPost(AllAuctionOrderNewActivity.this, all_url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                JSONObject jsonObject;
                if(tag){
                    mPullToRefreshListView.onRefreshComplete();
                }else{
                    try {
                        mPullToRefreshListView.onRefreshComplete();
                        jsonObject = new JSONObject(t);
                        JSONObject body = jsonObject.getJSONObject("body");
                        String all = body.getString("items");
                        mMoreAuctionList = JSON.parseArray(all,AllAuctionModel.class);
                        mList.addAll(mMoreAuctionList);
                        mTransportationTaskAdapter.addList(mMoreAuctionList);
                        mTransportationTaskAdapter.notifyDataSetChanged(); // 数据集变化后,通知adapter
                        isEnd = body.getBoolean("end");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                mPullToRefreshListView.onRefreshComplete();
            }
        });
    }
    private void loadData() {
        AjaxParams params = new AjaxParams();
        DidiApp.getHttpManager().sessionPost(AllAuctionOrderNewActivity.this, all_url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                isEnd = false;
                tag = false;
                mPullToRefreshListView.onRefreshComplete();
                JSONObject jsonObject;
                if (mAuctionList.size() > 0){
                    mAuctionList.clear();
                }
                if (mList.size() > 0){
                    mList.clear();
                }
                try {
                    jsonObject = new JSONObject(t);
                    JSONObject body = jsonObject.getJSONObject("body");
                    String all = body.getString("items");
                    mAuctionList = JSON.parseArray(all,AllAuctionModel.class);
                    mList.addAll(mAuctionList);
                    mTransportationTaskAdapter = new TransportationTaskAdapter(AllAuctionOrderNewActivity.this, mAuctionList,new RefreshAdapterCallBack() {
                        @Override
                        public void isAccept(boolean args) {
                            loadData();//操作成功重新加载数据
                        }
                    });
                    mTransportationTaskAdapter.setShowType(TransportationTaskAdapter.ShowType.ShowJJD);
                    mOrderList.setAdapter(mTransportationTaskAdapter);
                    mTransportationTaskAdapter.notifyDataSetChanged();
                    isEnd = body.getBoolean("end");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                mPullToRefreshListView.onRefreshComplete();
            }
        });
    }
}
