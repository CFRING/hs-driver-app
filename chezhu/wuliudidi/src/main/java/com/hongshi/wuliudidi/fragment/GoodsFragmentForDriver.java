package com.hongshi.wuliudidi.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.AuctionDetailsActivity;
import com.hongshi.wuliudidi.activity.ChooseAddressActivity;
import com.hongshi.wuliudidi.activity.DriverChooseGoodsTypeActivity;
import com.hongshi.wuliudidi.activity.LoginActivity;
import com.hongshi.wuliudidi.activity.MainActivity;
import com.hongshi.wuliudidi.adapter.BannerAdapter;
import com.hongshi.wuliudidi.adapter.GoodsAdapter;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.AreaModel;
import com.hongshi.wuliudidi.model.AuctionQueryModel;
import com.hongshi.wuliudidi.model.GoodsModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.plugin.Util.PluginUtil;
import com.hongshi.wuliudidi.plugin.bean.PluginParams;
import com.hongshi.wuliudidi.share.BannerModelList;
import com.hongshi.wuliudidi.utils.CloseRefreshTask;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.CircleFlowIndicator;
import com.hongshi.wuliudidi.view.MyViewFlow;
import com.hongshi.wuliudidi.view.NullDataView;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author huiyuan
 * Created by huiyuan on 2017/3/22.
 */

public class GoodsFragmentForDriver extends Fragment implements OnClickListener {

    private View mView;
    private ListView mGoddsListview;
    private List<GoodsModel> mGoodsList = new ArrayList<GoodsModel>();
    private GoodsAdapter mHomeAdapter;
    private ImageView mMap;
    private ImageButton mBackTop;
    private PullToRefreshListView mPullToRefreshListView;
//    private TextView recg_goods,all_goods;
    private String goods_url = GloableParams.HOST + "carrier/auction/getAuctionByType.do";
    public List<GoodsModel> AllAccount = new ArrayList<GoodsModel>();
    //	private long gmtPublish;
    private int fromType = 0;
    private boolean hasSelected = false;
    private boolean isEnd = false;
    private boolean isFirst = true;
    private SharedPreferences sharedPreferences;
    private String latitude;
    private String longitude;
    /**
     * 出发地id，eg:001_002_003
     */
    private static String startAddressId = "";
    /**
     * 目的地id，eg:001_002_003
     */
    private static String endAddressId = "";
    /**
     * 出发地名称，eg:杭州市
     */
    private static String startAddressName = "出发地";
    /**
     * 目的地名称，eg:金华市
     */
    private static String endAddressName = "目的地";
    /**
     * 1.9.4 收货公司ID
     */
    private static String recipCopId = "";
    /**
     * 1.9.4 发货公司ID
     */
    private static String senderCopId = "";
    /**
     * 1.9.4 收货公司名称
     */
    private static String recipCopName = "";
    /**
     * 1.9.4 发货公司名称
     */
    private static String senderCopName = "";
    /**
     * 记录竞价条目tag
     */
    private int tagNumbber = 0;
    private RelativeLayout mTitle;
    private NullDataView nullDataView;
    private RelativeLayout filterStartLayout, filterEndLayout,filter_goods_type_layout,my_route_entry;
    private TextView startAreaText, endAreaText,filter_goods_type_text;
    private FrameLayout frameLayout;
    private MyViewFlow view_flow;
    private CircleFlowIndicator indic;
    private int currentPage = 0;
    private final int pageSize = 10;
    private AreaModel currentStartArea = new AreaModel(), currentEndArea = new AreaModel();

    /**
     * 物品类型ID，eg: 001_002_003
     */
    private String goodsTypeId = "";
    /**
     * 所选择的物品名称
     */
    private String goodsTypeName = "货物";
    /**
     * 物品过滤关键字
     */
    private String goodsName = "";
    private boolean isSendCopSelected = false;
    private BannerAdapter bannerAdapter;

    /**
     * 刷新页面的广播接收
     */

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(CommonRes.RefreshGoodsList)) {
                //刷新
                init();
            }else if(action.equals(CommonRes.RefreshData)){
//                init();
            }else if(CommonRes.RefreshGoodsListWithStartAddr.equals(action)){
//                setTextViewUnSelectedStyle(all_goods,recg_goods);
                startAddressId = intent.getStringExtra("startAddressId");
                startAddressName = intent.getStringExtra("startAddressName");
                startAreaText.setText(startAddressName);
                senderCopId = "";
                hasSelected = true;
                isSendCopSelected = false;
                isFirst = false;
                refreshGoodsList();
            }else if(CommonRes.RefreshGoodsListWithEndAddr.equals(action)){
//                setTextViewUnSelectedStyle(all_goods,recg_goods);
                endAddressId = intent.getStringExtra("endAddressId");
                endAddressName = intent.getStringExtra("endAddressName");
                endAreaText.setText(endAddressName);
                recipCopId = "";
                hasSelected = true;
                isFirst = false;
                refreshGoodsList();
            }else if(CommonRes.RefreshGoodsListWithStartCorp.equals(action)){
//                setTextViewUnSelectedStyle(all_goods,recg_goods);
                senderCopId = intent.getStringExtra("senderCopId");
                senderCopName = intent.getStringExtra("senderCopName");
                startAreaText.setText(senderCopName);
                isSendCopSelected = true;
                startAddressId = "";
                hasSelected = true;
                isFirst = false;
                refreshGoodsList();
            }else if(CommonRes.RefreshGoodsListWithEndCorp.equals(action)){
//                setTextViewUnSelectedStyle(all_goods,recg_goods);
                recipCopId = intent.getStringExtra("recipCopId");
                recipCopName = intent.getStringExtra("recipCopName");
                endAreaText.setText(recipCopName);
                endAddressId = "";
                hasSelected = true;
                isFirst = false;
                refreshGoodsList();
            }else if(CommonRes.RefreshGoodsListWithGoodsType.equals(action)){
//                setTextViewUnSelectedStyle(all_goods,recg_goods);
                goodsTypeId = intent.getStringExtra("goodsTypeId");
                goodsTypeName = intent.getStringExtra("goodsTypeName");
                goodsName = intent.getStringExtra("goodsName");
                filter_goods_type_text.setText(goodsTypeName);
                hasSelected = true;
                isFirst = false;
                refreshGoodsList();
            }else if("get_banner_list_success".equals(action)){
                setBannerView();
            }else if("push_goods_from_server".equals(action)){
                refreshGoodsList();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.goods_fragment_for_driver, null);
        filterStartLayout = (RelativeLayout) mView.findViewById(R.id.filter_start_layout);
        filterStartLayout.setOnClickListener(this);
        filterEndLayout = (RelativeLayout) mView.findViewById(R.id.filter_end_layout);
        filterEndLayout.setOnClickListener(this);
        filter_goods_type_layout = (RelativeLayout)mView.findViewById(R.id.filter_goods_type_layout);
        my_route_entry = (RelativeLayout) mView.findViewById(R.id.my_route_entry);
        my_route_entry.setOnClickListener(this);
        filter_goods_type_layout.setOnClickListener(this);
        startAreaText = (TextView) mView.findViewById(R.id.start_area_text);
        endAreaText = (TextView) mView.findViewById(R.id.end_area_text);
        filter_goods_type_text = (TextView) mView.findViewById(R.id.filter_goods_type_text);
        sharedPreferences = getActivity().getSharedPreferences("config",Context.MODE_PRIVATE);
        latitude = sharedPreferences.getString("latitude", "");
        longitude = sharedPreferences.getString("longitude", "");
        nullDataView = (NullDataView) mView.findViewById(R.id.no_data_view);
        nullDataView.setInfoHint("很抱歉，暂无货源信息");
        nullDataView.setInfo("请后续关注");
        mPullToRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.goods_list);
        mPullToRefreshListView.setMode(Mode.BOTH);

        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载更多");

        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel("松开刷新");
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel("刷新中");
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel("下拉刷新");

        mGoddsListview = mPullToRefreshListView.getRefreshableView();
        mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mPullToRefreshListView.isRefreshing()) {
                    if (mPullToRefreshListView.isHeaderShown()){
                        currentPage = 1;
                        sessionLoadData(true,fromType);
                    } else if (mPullToRefreshListView.isFooterShown()) {
                        // 加载更多
                        if(isEnd){
                            Toast.makeText(getActivity(), "已经是最后一页", Toast.LENGTH_SHORT).show();
                            CloseRefreshTask closeRefreshTask = new CloseRefreshTask(mPullToRefreshListView);
                            closeRefreshTask.execute();
                            return;
                        }
                        currentPage = currentPage + 1;
                        sessionLoadData(false,fromType);
                    }
                }
            }
        });
        // 回到顶部的按钮
        mBackTop = (ImageButton) mView.findViewById(R.id.back_top);
        mTitle = (RelativeLayout) mView.findViewById(R.id.home_title);
        frameLayout = (FrameLayout) mView.findViewById(R.id.frame_layout);
        view_flow = (MyViewFlow) mView.findViewById(R.id.view_flow);
        indic = (CircleFlowIndicator) mView.findViewById(R.id.viewflowindic);

        mGoddsListview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(Util.isLogin(getActivity())){
                    //已登录
                    if(mGoodsList.get(position-1).getAuctionType() == CommonRes.LONG_TRANSPORT &&
                            (mGoodsList.get(position-1).getGoodsAmountSp() != null
                                    && Double.valueOf(mGoodsList.get(position-1).getGoodsAmountSp()) <= 0)){

                    }else if (mGoodsList.get(position-1).getAuctionType() != CommonRes.FIXED_PRICE ||
                            mGoodsList.get(position-1).getInnerGoods() != 1 ||
                            (mGoodsList.get(position-1).getGoodsAmountSp() != null
                                    && Double.valueOf(mGoodsList.get(position-1).getGoodsAmountSp()) > 0)) {
                        //不是一口价、接单运输或者有剩余量auctionId
                        Intent intent = new Intent(getActivity(), AuctionDetailsActivity.class);
                        intent.putExtra("auctionId", mGoodsList.get(position - 1).getAuctionId());
                        getActivity().startActivity(intent);
                    }
                }else{
                    ToastUtil.show(getActivity(), "请登录");
                    Intent login_intent = new Intent(getActivity(),LoginActivity.class);
                    getActivity().startActivity(login_intent);
                }
            }
        });
        setOnClickListener();
        init();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CommonRes.RefreshGoodsList);
        intentFilter.addAction(CommonRes.RefreshData);
        intentFilter.addAction(CommonRes.RefreshGoodsListWithStartAddr);
        intentFilter.addAction(CommonRes.RefreshGoodsListWithEndAddr);
        intentFilter.addAction(CommonRes.RefreshGoodsListWithStartCorp);
        intentFilter.addAction(CommonRes.RefreshGoodsListWithEndCorp);
        intentFilter.addAction(CommonRes.RefreshGoodsListWithGoodsType);
        intentFilter.addAction("get_banner_list_success");
        intentFilter.addAction("push_goods_from_server");
        getActivity().registerReceiver(mRefreshBroadcastReceiver, intentFilter);

        setBannerView();
        return mView;
    }

    private void setBannerView(){
        WindowManager windowManager = getActivity().getWindowManager();
        int width = windowManager.getDefaultDisplay().getWidth();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width,1 * width/5);
        layoutParams.addRule(RelativeLayout.BELOW,R.id.home_title);
        frameLayout.setLayoutParams(layoutParams);
        // 轮播图
        if(BannerModelList.goodsListBannerList.size() > 0){
            frameLayout.setVisibility(View.VISIBLE);
            bannerAdapter = new BannerAdapter(getActivity(),BannerModelList.goodsListBannerList,"goods_list_page");
            FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(width,1 * width/5);
            view_flow.setLayoutParams(layoutParams1);
            view_flow.setAdapter(bannerAdapter);
            /**
             * 实际图片张数
             */
            view_flow.setmSideBuffer(BannerModelList.goodsListBannerList.size());
            view_flow.setViewPager(MainActivity.mPager);
            if(BannerModelList.goodsListBannerList.size() <= 1){
                indic.setVisibility(View.GONE);
            }else {
                view_flow.setFlowIndicator(indic);
                view_flow.setTimeSpan(4000);
                view_flow.setSelection(3 * 1000);
                view_flow.startAutoFlowTimer();
            }
        }else {
            frameLayout.setVisibility(View.GONE);
        }
    }

    private void refreshGoodsList(){
//        currentPage = 1;
//        if(Util.isLogin(getActivity())){
            currentPage = 1;
            sessionLoadData(true,fromType);
//        }
    }

    private void init(){
//        setTextViewUnSelectedStyle(recg_goods,all_goods);
//        BannerModelList.getAdvertList(getActivity());
        if(isFirst){
            hasSelected = false;
            startAddressId = "";
            endAddressId = "";
            startAddressName = "出发地";
            endAddressName = "目的地";
            recipCopId = "";
            senderCopId = "";
            recipCopName = "";
            senderCopName = "";
        }

        startAreaText.setText(startAddressName);
        endAreaText.setText(endAddressName);
        filter_goods_type_text.setText(goodsTypeName);

        currentStartArea.setAreaCode("");
        currentStartArea.setAreaText(getResources().getString(R.string.all));
        currentEndArea.setAreaCode("");
        currentEndArea.setAreaText(getResources().getString(R.string.all));

            currentPage = 1;
            sessionLoadData(true,fromType);
//        }
    }

    //用户已经登录调用此接口
    private void sessionLoadData(final boolean isRefresh , int fromType) {
        goods_url = GloableParams.HOST + "carrier/auction/getAuctionByType.do";

        AjaxParams params = new AjaxParams();
        AuctionQueryModel auctionQueryModel = new AuctionQueryModel();

        latitude = sharedPreferences.getString("latitude", "");
        longitude = sharedPreferences.getString("longitude", "");
//        if(fromType == 0){
            if(!latitude.equals("") && !longitude.equals("")){
                auctionQueryModel.setLng(longitude);
                auctionQueryModel.setLat(latitude);
            }
            auctionQueryModel.setSenderAreaId(startAddressId);
            auctionQueryModel.setRecipientAreaId(endAddressId);
            auctionQueryModel.setCurrentPage(currentPage);
            auctionQueryModel.setPageSize(pageSize);
            auctionQueryModel.setGoodsType(goodsTypeId);
            auctionQueryModel.setGoodsName(goodsName);
            auctionQueryModel.setFromType(fromType);
            auctionQueryModel.setSenderCopId(senderCopId);
            auctionQueryModel.setRecipCopId(recipCopId);
//        }

            params.put("auctionQueryJson", JSON.toJSONString(auctionQueryModel));

        DidiApp.getHttpManager().sessionPost(getActivity(), goods_url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                isLoginData(t, isRefresh);
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                mPullToRefreshListView.onRefreshComplete();
                noData();
            }
        });
    }

    //初始化数据加载
    private void isLoginData(String t, boolean isRefresh){
        JSONObject jsonObject;
        try {
            if(isRefresh){
                mGoodsList.clear();
                isEnd = false;
                tagNumbber = 0;
                mPullToRefreshListView.onRefreshComplete();
            }
            jsonObject = new JSONObject(t);
            JSONObject body = jsonObject.getJSONObject("body");
            String senProName = body.optString("senProName");
//            Log.d("huiyuan","定位 = " + senProName + "hasSelected = " + hasSelected + " startAddressName = " + startAddressName
//            + " endAddressName = " + endAddressName);
            if(senProName != null && !senProName.equals("") && !hasSelected){
                startAreaText.setText(senProName);
            }else if(isSendCopSelected){
                startAreaText.setText(senderCopName);
            }else if(startAddressName == null){
                if(senProName.equals("")){
                    startAreaText.setText("出发地");
                }else {
                    startAreaText.setText(senProName);
                }
            }else {
                startAreaText.setText(startAddressName);
            }
            String all = body.getString("items");
            //判断是否还有下一页
            isEnd = body.getBoolean("end");
            currentPage = body.getInt("currentPage");
            List<GoodsModel> tempList = JSON.parseArray(all,GoodsModel.class);
            if(isRefresh){
                mGoodsList = tempList;
                mHomeAdapter = new GoodsAdapter(getActivity(), mGoodsList,false);
                mGoddsListview.setAdapter(mHomeAdapter);
            }else{
                mHomeAdapter.addGoodsList(tempList);
            }
            if(mGoodsList.size() <= 0){
                noData();
            }else{
                mPullToRefreshListView.setVisibility(View.VISIBLE);
                nullDataView.setVisibility(View.GONE);
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
        switch (v.getId()) {
            case R.id.back_top:
                mGoddsListview.setSelection(0);
                break;
            case R.id.my_route_entry:
                if(Util.isLogin(getActivity())){
                    String sessionID = sharedPreferences.getString("session_id", "");
                    String userRole = sharedPreferences.getString("user_role", "车主");
                    HashMap<String,String> params = new HashMap<>();
                    params.put("sessionId",sessionID);
                    if("司机".equals(userRole)){
                        params.put("userRole","sj");
                    }else {
                        params.put("userRole","cz");
                    }
                    PluginUtil.startPlugin(getActivity().getApplicationContext(),
                            PluginParams.MY_ROUTE_ACTION, PluginParams.MY_ROUTE, params);
                }else {
                    Intent login_intent = new Intent(getActivity(),LoginActivity.class);
                    startActivity(login_intent);
                }
                break;
            case R.id.filter_start_layout:
                isFirst = false;
                Intent intent_start = new Intent(getActivity(), ChooseAddressActivity.class);
                intent_start.putExtra("addressType","start");
                getActivity().startActivity(intent_start);
                break;
            case R.id.filter_end_layout:
                isFirst = false;
                Intent intent_end = new Intent(getActivity(), ChooseAddressActivity.class);
                intent_end.putExtra("addressType","end");
                getActivity().startActivity(intent_end);
                break;
            case R.id.filter_goods_type_layout:
                Intent intent = new Intent(getActivity(), DriverChooseGoodsTypeActivity.class);
                getActivity().startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mRefreshBroadcastReceiver);
    }

    private void noData(){
        mPullToRefreshListView.setVisibility(View.GONE);
        nullDataView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("GoodsFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("GoodsFragment");
    }

}
