package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.GoodsAdapter;
import com.hongshi.wuliudidi.dialog.ChooseAreaPopupWindow;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.ChooseAreaCallBack;
import com.hongshi.wuliudidi.model.AreaModel;
import com.hongshi.wuliudidi.model.AuctionQueryModel;
import com.hongshi.wuliudidi.model.GoodsModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.CloseRefreshTask;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.hongshi.wuliudidi.view.NullDataView;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bian
 * Created by bian on 2016/7/11 15:49.
 */
public class CementGoodsActivity extends Activity implements View.OnClickListener {

    private static String TAG = "CementGoodsActivity";

    private ListView mGoddsListview;
    private List<GoodsModel> mGoodsList = new ArrayList<GoodsModel>();
    private GoodsAdapter mHomeAdapter;
    private ImageView mMap;
    private ImageButton mBackTop;
    private PullToRefreshListView mPullToRefreshListView;
    private String goods_url = GloableParams.HOST + "carrier/auction/getAuctionByType.do?";
    public List<GoodsModel> AllAccount = new ArrayList<GoodsModel>();
    //	private long gmtPublish;
    private String lastId;
    private boolean isEnd = false;
    private SharedPreferences sharedPreferences;
    private String latitude;
    private String longitude;
    private int tagNumbber = 0;//记录竞价条目tag
    private DiDiTitleView mTitle;
    private NullDataView nullDataView;
    private RelativeLayout filterStartLayout, filterEndLayout;
    private TextView startAreaText, endAreaText;
    private ChooseAreaPopupWindow chooseStartAreaPop, chooseEndAreaPop;
    private int currentPage = 0;
    private final int pageSize = 10;
    private AreaModel currentStartArea = new AreaModel(), currentEndArea = new AreaModel();
    //刷新页面的广播接收
    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(CommonRes.RefreshGoodsList)) {
                //刷新
                init();
            }
//            }else if(action.equals(CommonRes.RefreshData)){
//                init();
//            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_cement);
        filterStartLayout = (RelativeLayout) findViewById(R.id.filter_start_layout);
        filterStartLayout.setOnClickListener(this);
        filterEndLayout = (RelativeLayout) findViewById(R.id.filter_end_layout);
        filterEndLayout.setOnClickListener(this);
        startAreaText = (TextView) findViewById(R.id.start_area_text);
        endAreaText = (TextView) findViewById(R.id.end_area_text);
        sharedPreferences = getSharedPreferences("config",Context.MODE_PRIVATE);
        latitude = sharedPreferences.getString("latitude", "");
        longitude = sharedPreferences.getString("longitude", "");
        nullDataView = (NullDataView) findViewById(R.id.no_data_view);
        nullDataView.setInfoHint("很抱歉，暂无货源信息");
        nullDataView.setInfo("请后续关注");
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.goods_list);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载更多");

        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel("松开刷新");
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel("刷新中");
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel("下拉刷新");

        mGoddsListview = mPullToRefreshListView.getRefreshableView();
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mPullToRefreshListView.isRefreshing()) {
                    if (mPullToRefreshListView.isHeaderShown()){
                        currentPage = 0;
                        if(Util.isLogin(CementGoodsActivity.this)){
                            sessionLoadData(true);
                        }else{
                            loadData(true);
                        }
                    } else if (mPullToRefreshListView.isFooterShown()) {
                        // 加载更多
                        if(isEnd){
                            Toast.makeText(CementGoodsActivity.this, "已经是最后一页", Toast.LENGTH_SHORT).show();
                            CloseRefreshTask closeRefreshTask = new CloseRefreshTask(mPullToRefreshListView);
                            closeRefreshTask.execute();
                            return;
                        }
                        if(Util.isLogin(CementGoodsActivity.this)){
                            sessionLoadData(false);
                        }else{
                            loadData(false);
                        }
                    }
                }
            }
        });
        // 回到顶部的按钮
        mBackTop = (ImageButton) findViewById(R.id.back_top);
        mTitle = (DiDiTitleView) findViewById(R.id.home_title);
        mTitle.setBack(this);
        mTitle.setTitle(getString(R.string.cement_goods));
        mMap = mTitle.getRightImage();

        mMap.setImageResource(R.drawable.map_style);
        //跳转到地图
        mMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent map_intent = new Intent(CementGoodsActivity.this, MapActivity.class);
                Bundle b = new Bundle();
                b.putString("mapType", "RequestLocation");
                map_intent.putExtras(b);
                startActivity(map_intent);
            }
        });
        mGoddsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(Util.isLogin(CementGoodsActivity.this)){
                    //已登录
                    if (mGoodsList.get(position-1).getAuctionType() != CommonRes.FIXED_PRICE ||
                            mGoodsList.get(position-1).getInnerGoods() != 1 ||
                            (mGoodsList.get(position-1).getGoodsAmountSp() != null
                                    && Double.valueOf(mGoodsList.get(position-1).getGoodsAmountSp()) > 0)) {
                        //不是一口价或者有剩余量
                        Intent intent = new Intent(CementGoodsActivity.this, AuctionDetailsActivity.class);
                        intent.putExtra("auctionId", mGoodsList.get(position - 1).getAuctionId());
                        CementGoodsActivity.this.startActivity(intent);
                    }
                }else{
                    ToastUtil.show(CementGoodsActivity.this, "请登录");
                    Intent login_intent = new Intent(CementGoodsActivity.this,LoginActivity.class);
                    startActivity(login_intent);
                }
            }
        });
        setOnClickListener();
        init();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CommonRes.RefreshGoodsList);
        intentFilter.addAction(CommonRes.RefreshData);
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);

    }

    private void init(){
        startAreaText.setText("出发地");
        endAreaText.setText("目的地");

        currentStartArea.setAreaCode("");
        currentStartArea.setAreaText(getResources().getString(R.string.all));
        currentEndArea.setAreaCode("");
        currentEndArea.setAreaText(getResources().getString(R.string.all));

        currentPage = 0;
        if(Util.isLogin(CementGoodsActivity.this)){
            sessionLoadData(true);
        }else{
            loadData(true);
        }
    }

    /**
     *     用户已经登录调用此接口
     */
    private void sessionLoadData(final boolean isRefresh ) {
        AjaxParams params = new AjaxParams();
        AuctionQueryModel auctionQueryModel = new AuctionQueryModel();
        if(!latitude.equals("")&&!longitude.equals("")){
            auctionQueryModel.setLng(longitude);
            auctionQueryModel.setLat(latitude);
        }
//        if(!isRefresh){
//            params.put("last", lastId);
//        }
        if(startAreaText.getText().equals("全部")){
            auctionQueryModel.setSenderAreaId("_");
        }else {
            auctionQueryModel.setSenderAreaId(currentStartArea.getAreaCode());
        }
        auctionQueryModel.setRecipientAreaId(currentEndArea.getAreaCode());
        auctionQueryModel.setCurrentPage(currentPage + 1);
        auctionQueryModel.setPageSize(pageSize);
        auctionQueryModel.setFromType(2);
        auctionQueryModel.setGoodsType("-1");
        params.put("auctionQueryJson", JSON.toJSONString(auctionQueryModel));
        DidiApp.getHttpManager().sessionPost(CementGoodsActivity.this, goods_url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                isLoginData(t,isRefresh);
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                mPullToRefreshListView.onRefreshComplete();
                noData();
            }
        });
    }
    //未登录
    private void loadData(final boolean isRefresh ) {
        AjaxParams params = new AjaxParams();
        AuctionQueryModel auctionQueryModel = new AuctionQueryModel();
        if(!latitude.equals("")&&!longitude.equals("")){
            auctionQueryModel.setLng(longitude);
            auctionQueryModel.setLat(latitude);
        }
//        if(!isRefresh){
//            params.put("last", lastId);
//        }
        if(startAreaText.getText().equals("全部")){
            auctionQueryModel.setSenderAreaId("_");
        }else {
            auctionQueryModel.setSenderAreaId(currentStartArea.getAreaCode());
        }
        auctionQueryModel.setRecipientAreaId(currentEndArea.getAreaCode());
        auctionQueryModel.setCurrentPage(currentPage + 1);
        auctionQueryModel.setPageSize(pageSize);
        auctionQueryModel.setFromType(2);
        params.put("auctionQueryJson", JSON.toJSONString(auctionQueryModel));
        DidiApp.getHttpManager().sessionPost(CementGoodsActivity.this, goods_url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                isLoginData(t,isRefresh);
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                mPullToRefreshListView.onRefreshComplete();
                noData();
            }
        });
    }

    /**
     * 初始化数据加载
     * */
    private void isLoginData(String t, boolean isRefresh){
        JSONObject jsonObject;
        try {
            if(isRefresh){
                mGoodsList.clear();
                isEnd = false;
                lastId = "";
                tagNumbber = 0;
                mPullToRefreshListView.onRefreshComplete();
            }
            jsonObject = new JSONObject(t);
            JSONObject body = jsonObject.getJSONObject("body");
            String senProName = body.optString("senProName");
            if(senProName != null && !senProName.equals("")){
                startAreaText.setText(senProName);
            }
            String all = body.getString("items");
            //判断是否还有下一页
            isEnd = body.getBoolean("end");
            currentPage = body.getInt("currentPage");
            List<GoodsModel> tempList = JSON.parseArray(all,GoodsModel.class);
            if(isRefresh){
                mGoodsList = tempList;
                mHomeAdapter = new GoodsAdapter(CementGoodsActivity.this, mGoodsList,true);
                mGoddsListview.setAdapter(mHomeAdapter);
            }else{
                mHomeAdapter.addGoodsList(tempList);
            }
            if(mGoodsList.size() <= 0){
                noData();
            }else{
                mPullToRefreshListView.setVisibility(View.VISIBLE);
                nullDataView.setVisibility(View.GONE);
                lastId = mGoodsList.get(mGoodsList.size() - 1).getAuctionId();
            }
            tagNumbber = mGoodsList.size();
            if(tagNumbber>10){
                //大于10条信息显示
                mBackTop.setVisibility(View.VISIBLE);
            }else {
                mBackTop.setVisibility(View.GONE);
            }
            mPullToRefreshListView.onRefreshComplete();
        } catch (JSONException e) {
            e.printStackTrace();
            mPullToRefreshListView.onRefreshComplete();
            noData();
        }
    }
    private void setOnClickListener() {
        mBackTop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ChooseAreaCallBack cacb = new ChooseAreaCallBack() {
            @Override
            public void getResult(AreaModel result, ChooseAreaPopupWindow.AreaType tag) {
                switch (tag) {
                    case start:
                        currentPage = 0;
                        startAreaText.setText(result.getAreaText());
                        currentStartArea = result;
                        break;

                    default:
                        currentPage = 0;
                        endAreaText.setText(result.getAreaText());
                        currentEndArea = result;
                        break;
                }
                if(Util.isLogin(CementGoodsActivity.this)){
                    sessionLoadData(true);
                }else{
                    loadData(true);
                }
            }
        };

        switch (v.getId()) {
            case R.id.back_top:
                mGoddsListview.setSelection(0);
                break;
            case R.id.filter_start_layout:
                ChooseAreaPopupWindow.AreaType tag = ChooseAreaPopupWindow.AreaType.start;
                chooseStartAreaPop = new ChooseAreaPopupWindow(CementGoodsActivity.this, tag, cacb);
                chooseStartAreaPop.setArea(endAreaText.getText().toString());
                chooseStartAreaPop.setFocusable(true);
                chooseStartAreaPop.setOutsideTouchable(true);
                chooseStartAreaPop.showAsDropDown(filterStartLayout);
                break;
            case R.id.filter_end_layout:
                ChooseAreaPopupWindow.AreaType tag1 = ChooseAreaPopupWindow.AreaType.end;
                chooseEndAreaPop = new ChooseAreaPopupWindow(CementGoodsActivity.this, tag1, cacb);
                chooseEndAreaPop.setArea(startAreaText.getText().toString());
                chooseEndAreaPop.setFocusable(true);
                chooseEndAreaPop.setOutsideTouchable(true);
                chooseEndAreaPop.showAsDropDown(filterEndLayout);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRefreshBroadcastReceiver);
    }

    private void noData(){
        mPullToRefreshListView.setVisibility(View.GONE);
        nullDataView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }
}
