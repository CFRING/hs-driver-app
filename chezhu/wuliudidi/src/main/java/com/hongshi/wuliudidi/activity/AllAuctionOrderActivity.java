package com.hongshi.wuliudidi.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
import com.hongshi.wuliudidi.adapter.OneTruckHistoryAdapter;
import com.hongshi.wuliudidi.adapter.TransitTruckAdapter;
import com.hongshi.wuliudidi.adapter.TransportationTaskAdapter;
import com.hongshi.wuliudidi.dialog.DateDialog;
import com.hongshi.wuliudidi.dialog.GoodTypeDialog;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.RefreshAdapterCallBack;
import com.hongshi.wuliudidi.impl.SetDateCallBack;
import com.hongshi.wuliudidi.model.AllAuctionModel;
import com.hongshi.wuliudidi.model.GoodsTypeModel;
import com.hongshi.wuliudidi.model.OneTruckHistoryModel;
import com.hongshi.wuliudidi.model.TransitAmountModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.CloseRefreshTask;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.hongshi.wuliudidi.view.ImageItemView;
import com.hongshi.wuliudidi.view.TaskOrderTitleView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author huiyuan
 */
public class AllAuctionOrderActivity extends Activity implements View.OnClickListener {
    private PullToRefreshListView mPullToRefreshListView;
    private final int DAYTAG = 0;
    private final int MONTHTAG = 1;
    private final int YEARTAG = 2;
    private final int TRANSITTAG = 3;
    private final int PROVIDETAG = 4;
    private int tabTag = -1;
    private ListView mOrderList;
    private String all_url = GloableParams.HOST + "carrier/bid/allconsign.do?";
    /**
     *  每日统计的接口
     */
    private final String URL = GloableParams.HOST + "carrier/poundBillQuery/findAndPage4App.do";
    private List<AllAuctionModel> mAuctionList = new ArrayList<AllAuctionModel>();
    private List<AllAuctionModel> mMoreAuctionList = new ArrayList<AllAuctionModel>();
    private List<AllAuctionModel> mList = new ArrayList<AllAuctionModel>();
    private TransportationTaskAdapter mTransportationTaskAdapter;
    private boolean isEnd = false;

    /**
     * 加载更多，是否最后一页，在请求里去掉加载的状态
     */
    private boolean tag = false;
    private TaskOrderTitleView mDayView, mMonthView, mYearView;
    private Button mTransitBtn, mProvideBtn;
    private ImageView mBackImageview;
    private RelativeLayout mCountLayout;
    private RelativeLayout mShaixuan;
    private SetDateCallBack callBack;
    private DateDialog mDateDialog;
    //筛选显示数据的布局
    private LinearLayout shaixuan_data_layout;
    //选择日期的布局
    private RelativeLayout mDeteLayou;
    private TextView mDateText,mDate,mTruckAmount,mTransitAmount;
    //统计的标记 0，为每日统计；1，为月度统计；2，为年度统计
    private int statisticsTag = -1;
    private String yearStr,monthStr;
    //货品类型list
    private List<GoodsTypeModel> goods_type_list;
    private int mSelectPos = -1;
    private String mGoodsId;
    //货品类型list
    private List<String>  goodslist = new ArrayList<String>();
    private String goodsTypeJson = "",truckNumberJson = "";
    private PromptManager mPromptManager;
    //查看年度和月度的车辆列表
    private List<TransitAmountModel> mTruckList;

    //当前页
    private int currentPage = 0;
    //每页条数
    private final int PAGE_SIZE = 10;
    private List<OneTruckHistoryModel> mDayList;
    private OneTruckHistoryAdapter mAdapter;
    //是否以显示所有条目
    private Boolean isDayEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setContentView(R.layout.all_auction_order_activity);

        initViews();
        loadData();
    }

    private void initViews(){
        mBackImageview = (ImageView) findViewById(R.id.back);
        mTransitBtn = (Button) findViewById(R.id.transit_btn);
        mProvideBtn = (Button) findViewById(R.id.provide_btn);
        mCountLayout = (RelativeLayout) findViewById(R.id.count_layout);
        mShaixuan = (RelativeLayout) findViewById(R.id.shaixuan_layout);

        mDayView = (TaskOrderTitleView) findViewById(R.id.day_view);
        mMonthView = (TaskOrderTitleView) findViewById(R.id.month_view);
        mYearView = (TaskOrderTitleView) findViewById(R.id.year_view);
        shaixuan_data_layout = (LinearLayout) findViewById(R.id.shaixuan_data_layout);
        mDeteLayou = (RelativeLayout) findViewById(R.id.date_layout);
        mDateText = (TextView) findViewById(R.id.date_text);
        mDate = (TextView) findViewById(R.id.date);
        mTruckAmount = (TextView) findViewById(R.id.truck_amount);
        mTransitAmount = (TextView) findViewById(R.id.transit_amount);
        mDayView.setTitleText("每日统计");
        mMonthView.setTitleText("月度统计");
        mYearView.setTitleText("年度统计");
        mDayView.setTextColor(getResources().getColor(R.color.white), true);
        mMonthView.setTextColor(getResources().getColor(R.color.provide_white), false);
        mYearView.setTextColor(getResources().getColor(R.color.provide_white), false);
        mBackImageview.setOnClickListener(this);
        mTransitBtn.setOnClickListener(this);
        mProvideBtn.setOnClickListener(this);
        mDayView.setOnClickListener(this);
        mMonthView.setOnClickListener(this);
        mYearView.setOnClickListener(this);
        mShaixuan.setOnClickListener(this);
        mDeteLayou.setOnClickListener(this);

        mPromptManager = new PromptManager();
        Calendar c = Calendar.getInstance();
        yearStr = String.valueOf(c.get(Calendar.YEAR));
        monthStr = String.valueOf(c.get(Calendar.MONTH)+1);

        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.all_auction_listview);
        mOrderList = mPullToRefreshListView.getRefreshableView();
        showRefreshStyle(true);
        mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mPullToRefreshListView.isRefreshing()) {
                    if (mPullToRefreshListView.isHeaderShown()) {
                        if (tabTag == PROVIDETAG) {
                            mDayList.clear();
                            currentPage = 0;
                            getData(false);
                        } else if (tabTag == TRANSITTAG) {
                            loadData();
                        }
                    } else if (mPullToRefreshListView.isFooterShown()) {
                        // 加载更多
                        if(tabTag == PROVIDETAG){
                            if(statisticsTag == DAYTAG){
                                if (isDayEnd){
                                    Toast.makeText(AllAuctionOrderActivity.this, getString(R.string.end_page), Toast.LENGTH_SHORT).show();
                                    CloseRefreshTask closeRefreshTask = new CloseRefreshTask(mPullToRefreshListView);
                                    closeRefreshTask.execute();
                                } else {
                                    getData(false);
                                }
                            }else{
                                CloseRefreshTask closeRefreshTask = new CloseRefreshTask(mPullToRefreshListView);
                                closeRefreshTask.execute();
                            }
                        }else if(tabTag == TRANSITTAG){
                            if (isEnd) {
                                tag = true;
                                ToastUtil.show(AllAuctionOrderActivity.this, "已经是最后一页");
                                CloseRefreshTask closeRefreshTask = new CloseRefreshTask(mPullToRefreshListView);
                                closeRefreshTask.execute();
                                return;
                            }
                            loadMoreData();
                        }
                    }
                }
            }


        });
        mOrderList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(tabTag == PROVIDETAG){
                    if(statisticsTag == MONTHTAG || statisticsTag == YEARTAG){
                        TransitAmountModel mTransitAmountModel =  mTruckList.get(position - 1);
                        String truckNumber = mTransitAmountModel.getTruckNumber();
                        Intent intent = new Intent(AllAuctionOrderActivity.this, OneTruckHistoryActivity.class);
                        intent.putExtra("truckNumber",truckNumber);
                        startActivity(intent);
                    }
                }else if(tabTag == TRANSITTAG){
                    AllAuctionModel mModel = mList.get(position - 1);
                    //1待审核, 2审核中, 3已取消, 4审核未通过, 5运输未开始, 6运输中, 7已完成
                    ArrayList<Integer> jumpToJJD = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
                    ArrayList<Integer> jumpToTYD = new ArrayList<Integer>(Arrays.asList(5, 6, 7));

                    //1待审核, 2审核中, 3已取消, 4审核未通过  四种状态的竞价项目点击跳转到竞价单详情页面
                    if (jumpToJJD.contains(mModel.getStatus())) {
                        Intent intent = new Intent(AllAuctionOrderActivity.this, AuctionDetailsActivity.class);
                        intent.putExtra("auctionId", mModel.getAuctionId());
                        startActivity(intent);
                    } else if (jumpToTYD.contains(mModel.getStatus())) {
                        //5运输未开始, 6运输中, 7已完成 三种状态的竞价项目点击跳转到托运详情页面
                        Intent intent = new Intent(AllAuctionOrderActivity.this, WinBidActivity.class);
                        intent.putExtra("AuctionId", mModel.getBidItemId());
                        startActivity(intent);
                    }
                }
            }

        });
    }

    //每日统计
    private void getData(final Boolean isFirst){
        AjaxParams params = new AjaxParams();
        final PromptManager promptManager = new PromptManager();
        if (isFirst) {
            promptManager.showProgressDialog1(AllAuctionOrderActivity.this, "加载中");
        }
        Intent intent = getIntent();
        params.put("currentPage", currentPage + 1 + "");
        params.put("pageSize", PAGE_SIZE + "");
        String s[] = {intent.getStringExtra("truckNumber")};
        params.put("truckNumberJson", Util.enCode(JSON.toJSONString(s)));
//        params.put("yearOrMonthSum", "0");
        DidiApp.getHttpManager().sessionPost(AllAuctionOrderActivity.this, URL, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                if (isFirst) {
                    promptManager.closeProgressDialog();
                } else {
                    mPullToRefreshListView.onRefreshComplete();
                }
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(t);
                com.alibaba.fastjson.JSONObject body = jsonObject.getJSONObject("body");
                currentPage = body.getInteger("currentPage");
                isDayEnd = body.getBoolean("end");
                mDayList.addAll(JSON.parseArray(body.getString("items"), OneTruckHistoryModel.class));
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                if (isFirst) {
                    promptManager.closeProgressDialog();
                }else {
                    mPullToRefreshListView.onRefreshComplete();
                }
            }
        });
    }
    private void loadMoreData() {
        AjaxParams params = new AjaxParams();
        if (mAuctionList.size() > 0) {
            params.put("last", mAuctionList.get(mAuctionList.size() - 1).getBidItemId());
        }
        DidiApp.getHttpManager().sessionPost(AllAuctionOrderActivity.this, all_url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                JSONObject jsonObject;
                if (tag) {
                    mPullToRefreshListView.onRefreshComplete();
                } else {
                    try {
                        mPullToRefreshListView.onRefreshComplete();
                        jsonObject = new JSONObject(t);
                        JSONObject body = jsonObject.getJSONObject("body");
                        String all = body.getString("items");
                        mMoreAuctionList = JSON.parseArray(all, AllAuctionModel.class);
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
        DidiApp.getHttpManager().sessionPost(AllAuctionOrderActivity.this, all_url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                isEnd = false;
                tag = false;
                mPullToRefreshListView.onRefreshComplete();
                JSONObject jsonObject;
                if (mAuctionList.size() > 0) {
                    mAuctionList.clear();
                }
                if (mList.size() > 0) {
                    mList.clear();
                }
                try {
                    jsonObject = new JSONObject(t);
                    JSONObject body = jsonObject.getJSONObject("body");
                    String all = body.getString("items");
                    mAuctionList = JSON.parseArray(all, AllAuctionModel.class);
                    mList.addAll(mAuctionList);
                    mTransportationTaskAdapter = new TransportationTaskAdapter(AllAuctionOrderActivity.this, mAuctionList, new RefreshAdapterCallBack() {
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

    private void requestTag(){
        if(statisticsTag == YEARTAG){
            trucks(goodsTypeJson,"",truckNumberJson,yearStr,"");
            transitAmount("", goodsTypeJson, "", yearStr);
        }else if(statisticsTag == MONTHTAG){
            trucks(goodsTypeJson,monthStr,truckNumberJson,yearStr,"");
            transitAmount("",goodsTypeJson,monthStr,yearStr);
        }else if(statisticsTag == DAYTAG){

        }
    }
    private void showRefreshStyle(boolean isRefresh){
        if(isRefresh){
            mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
            mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");
            mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载更多");

            mPullToRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel("松开刷新");
            mPullToRefreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel("刷新中");
            mPullToRefreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel("下拉刷新");
            mOrderList.setDividerHeight(getResources().getDimensionPixelOffset(R.dimen.height_7));
        }else{
            mPullToRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel("");
            mPullToRefreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel("");
            mPullToRefreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel("");

            mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("");
            mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("");
            mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("");
            mOrderList.setDividerHeight(0);
        }
    }
    //获取货品类型
    private void goodsType(final int possion){
        final PromptManager mPromptManager = new PromptManager();
        mPromptManager.showProgressDialog1(AllAuctionOrderActivity.this, "加载中");
        String goods_type_url = GloableParams.HOST  +"carrier/commonLine/findGoodsType.do";
        AjaxParams params = new AjaxParams();
        DidiApp.getHttpManager().sessionPost(getApplicationContext(), goods_type_url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                mPromptManager.closeProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    String body = jsonObject.getString("body");
                    goods_type_list = JSON.parseArray(body, GoodsTypeModel.class);
                    if (goods_type_list!= null && goods_type_list.size()>0){
                        GoodTypeDialog mGoodTypeDialog = new GoodTypeDialog(AllAuctionOrderActivity.this,R.style.data_filling_dialog,
                                goods_type_list,possion);
                        mGoodTypeDialog.setOnSureBtnClickListener(new GoodTypeDialog.OnSureBtnClickListener() {
                            @Override
                            public void onClick(int position) {
                                mSelectPos = position;
                                //根据脚标获取货品ID
                                if (mSelectPos != -1){
                                    mGoodsId = goods_type_list.get(position).getKey();
                                    if(goodslist.size()>0){
                                        goodslist.clear();
                                    }
                                    goodslist.add(mGoodsId);
                                    goodsTypeJson =  JSON.toJSONString(goodslist);
                                }else{
                                    goodsTypeJson = "";
                                }
                                requestTag();
                            }
                        });
                        mGoodTypeDialog.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {

            }
        });
    }
    /**
     *年度、月度获取车辆运输记录
     * @param goodsTypeJson   货品类型，支持多选
     * @param monthStr        月份
     * @param truckNumberJson 车辆id Json  支持多选
     * @param yearStr         年
     * @param yearOrMonthSum  当前月度统计 0  年度统计 1
     */
    private void trucks(String goodsTypeJson ,String monthStr,String truckNumberJson ,String yearStr ,String yearOrMonthSum){
        String url =GloableParams.HOST + "/carrier/poundBillQuery/findSummaryAndPage4App.do";
        AjaxParams params = new AjaxParams();
        params.put("goodsTypeJson",goodsTypeJson);
        params.put("monthStr",monthStr);
        params.put("truckNumberJson",truckNumberJson);
        params.put("yearStr",yearStr);
        params.put("currentPage","1");
        params.put("pageSize", "100");
        if(!yearOrMonthSum.equals("")){
            params.put("yearOrMonthSum",yearOrMonthSum);
        }
        DidiApp.getHttpManager().sessionPost(getApplicationContext(), url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(t);
                    JSONObject body = jsonObject.getJSONObject("body");
                    String all = body.getString("items");
                    mTruckList = JSON.parseArray(all,TransitAmountModel.class);
                    TransitTruckAdapter mTransitTruckAdapter = new TransitTruckAdapter(AllAuctionOrderActivity.this,mTruckList);
                    mOrderList.setAdapter(mTransitTruckAdapter);
                    mPullToRefreshListView.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {

            }
        });

    }

    /**
     * 运输总次数
     * @param yearOrMonthSum  当前月度统计 0  年度统计 1
     */
    private void transitAmount(String yearOrMonthSum,String goodsTypeJson ,String monthStr ,String yearStr){
        mPromptManager.showProgressDialog1(AllAuctionOrderActivity.this,"请稍等");
        String url = GloableParams.HOST + "/carrier/poundBillQuery/sumSummary4App.do";
        AjaxParams params = new AjaxParams();
        if(!yearOrMonthSum.equals("")){
            params.put("yearOrMonthSum",yearOrMonthSum);
        }
        params.put("goodsTypeJson",goodsTypeJson);
        params.put("monthStr",monthStr);
        params.put("yearStr",yearStr);
        DidiApp.getHttpManager().sessionPost(getApplicationContext(), url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(t);
                    String body = jsonObject.getString("body");
                    TransitAmountModel mTransitAmountModel = JSON.parseObject(body, TransitAmountModel.class);
                    if(statisticsTag == MONTHTAG){
                        mDate.setText(mTransitAmountModel.getYearStr()+"年"+mTransitAmountModel.getMonthStr()+"月");
                    }else if(statisticsTag == YEARTAG){
                        mDate.setText(mTransitAmountModel.getYearStr()+"年");
                    }
                    mTruckAmount.setText(mTransitAmountModel.getTruckCount() + "辆");
                    mTransitAmount.setText(mTransitAmountModel.getTransitCount() + "次");
                    mPromptManager.closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                mPromptManager.closeProgressDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.date_layout:
                if (statisticsTag == MONTHTAG){
                    //月度
                    mDateDialog = new DateDialog(AllAuctionOrderActivity.this, R.style.data_filling_dialog,
                            getDialogCallBack(1), DateDialog.YearMonth, "请选择月份");
                    UploadUtil.setAnimation(mDateDialog, 0, true);
                    mDateDialog.show();
                }else if(statisticsTag == YEARTAG){
                    //年度
                    mDateDialog = new DateDialog(AllAuctionOrderActivity.this, R.style.data_filling_dialog,
                            getDialogCallBack(0), DateDialog.Year, "请选择年份");
                    UploadUtil.setAnimation(mDateDialog, 0, true);
                    mDateDialog.show();
                }
                break;
            case R.id.transit_btn:
                tabTag = TRANSITTAG;
                mCountLayout.setVisibility(View.GONE);
                mPullToRefreshListView.setVisibility(View.VISIBLE);
                shaixuan_data_layout.setVisibility(View.GONE);

                mTransitBtn.setBackgroundResource(R.drawable.left_white);
                mTransitBtn.setTextColor(getResources().getColor(R.color.home_text_press));
                mProvideBtn.setBackgroundResource(R.drawable.right_red);
                mProvideBtn.setTextColor(getResources().getColor(R.color.white));
                showRefreshStyle(true);
                loadData();
                break;
            case R.id.provide_btn:
                tabTag = PROVIDETAG;
//                mPullToRefreshListView.setVisibility(View.GONE);
                mCountLayout.setVisibility(View.VISIBLE);
                mTransitBtn.setBackgroundResource(R.drawable.left_red);
                mTransitBtn.setTextColor(getResources().getColor(R.color.white));
                mProvideBtn.setBackgroundResource(R.drawable.right_white);
                mProvideBtn.setTextColor(getResources().getColor(R.color.home_text_press));
                //默认每日统计
                mDayView.setTextColor(getResources().getColor(R.color.white), true);
                mMonthView.setTextColor(getResources().getColor(R.color.provide_white), false);
                mYearView.setTextColor(getResources().getColor(R.color.provide_white), false);
                shaixuan_data_layout.setVisibility(View.GONE);
                mOrderList.setDividerHeight(0);
                mDayList = new ArrayList<>();
                mAdapter = new OneTruckHistoryAdapter(AllAuctionOrderActivity.this, mDayList);
                mOrderList.setAdapter(mAdapter);
                getData(true);
                break;
            case R.id.day_view:
                statisticsTag = DAYTAG;
                mDayView.setTextColor(getResources().getColor(R.color.white), true);
                mMonthView.setTextColor(getResources().getColor(R.color.provide_white), false);
                mYearView.setTextColor(getResources().getColor(R.color.provide_white), false);
                //隐藏日期筛选统计的数据布局
                shaixuan_data_layout.setVisibility(View.GONE);
                showRefreshStyle(true);
                mDayList = new ArrayList<>();
                mAdapter = new OneTruckHistoryAdapter(AllAuctionOrderActivity.this, mDayList);
                mOrderList.setAdapter(mAdapter);
                getData(false);
                break;
            case R.id.month_view:
                statisticsTag = MONTHTAG;
                mDayView.setTextColor(getResources().getColor(R.color.provide_white), false);
                mMonthView.setTextColor(getResources().getColor(R.color.white), true);
                mYearView.setTextColor(getResources().getColor(R.color.provide_white), false);
                //显示筛选日期统计的数据布局
                shaixuan_data_layout.setVisibility(View.VISIBLE);
                mDateText.setText("月度统计");
                showRefreshStyle(false);
                transitAmount("0","","","");
                trucks("","","","","0");
                break;
            case R.id.year_view:
                statisticsTag = YEARTAG;
                mDayView.setTextColor(getResources().getColor(R.color.provide_white), false);
                mMonthView.setTextColor(getResources().getColor(R.color.provide_white), false);
                mYearView.setTextColor(getResources().getColor(R.color.white), true);
                shaixuan_data_layout.setVisibility(View.VISIBLE);
                mDateText.setText("年度统计");
                showRefreshStyle(false);
                transitAmount("1","","","");
                trucks("","","","","1");
                break;
            case R.id.shaixuan_layout:
                //获取货品类型
                goodsType(mSelectPos);
                break;
            default:
                break;
        }

    }
    private SetDateCallBack getDialogCallBack(int type){//0:年度 1:月度 2:每日统计起始时间 3:每日统计截止时间
        SetDateCallBack callBack;
        switch (type){
            case 0:
                //年度
                callBack = new SetDateCallBack() {
                    @Override
                    public void date(long date) {
                        Date mDate1 = new Date(date);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(mDate1);
                        mDate.setText(String.valueOf(cal.get(Calendar.YEAR)) + "年");
                        yearStr = String.valueOf(cal.get(Calendar.YEAR));
                        requestTag();
                    }
                };
                break;
            case 1:
                //月度
                callBack = new SetDateCallBack() {
                    @Override
                    public void date(long date) {
                        Date mDate1 = new Date(date);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(mDate1);
                        mDate.setText(String.valueOf(cal.get(Calendar.YEAR)) + "年" + String.valueOf(cal.get(Calendar.MONTH) + 1) + "月");
                        if((cal.get(Calendar.MONTH) + 1)<10){
                            monthStr = "0"+ String.valueOf(cal.get(Calendar.MONTH) + 1);
                        }else{
                            monthStr = String.valueOf(cal.get(Calendar.MONTH) + 1);
                        }
                        yearStr = String.valueOf(cal.get(Calendar.YEAR));
                        requestTag();
                    }
                };
                break;
            default:
                callBack = new SetDateCallBack() {
                    @Override
                    public void date(long date) {

                    }
                };
                break;
        }
        return callBack;
    }
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
}
