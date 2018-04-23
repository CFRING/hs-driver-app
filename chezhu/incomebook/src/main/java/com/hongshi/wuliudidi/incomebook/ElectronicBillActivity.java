package com.hongshi.wuliudidi.incomebook;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by huiyuan on 2016/8/11.
 */
public class ElectronicBillActivity extends Activity implements View.OnClickListener{

    private DiDiTitleView titleView;
    private TextView year,month,electronic_bill_total,wait_for_handle_cnt,agree_cnt,disagree_cnt;
    private ImageView arrow_click_view;
    private PullToRefreshListView electronic_bill_listview;
    private ListView electronic_bill_items_listview;
    private BillDataAdapter billDataAdapter;
    private final String BILL_HEADER_URL = "https://cz.redlion56.com" + "/gwcz/" + "/carrier/reconciliation/findTimes.do";
    private final String BILL_ITEMS_URL = "https://cz.redlion56.com" + "/gwcz/" + "/carrier/reconciliation/findAndPage4IndexItems.do";
    private List<BillItemModel> billListData = new ArrayList();
    private int currentPage = 0;
    private int pageSize = 10;
    private boolean isEnd = false;
//    private String lastId = "";
    private String sessionID = "";
    private String monthStr = "";
    private String yearStr = "";
    private int total_bill_count,wait_for_handling_count,
    agree_bill_count,disagree_bill_count;
    private boolean isFirstRequest = true;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("refreshBillData")){
                getBillHeaderData();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionID = getIntent().getStringExtra("sessionId");
        setContentView(R.layout.electronic_bill);
        initView();
        this.registerReceiver(receiver,new IntentFilter("refreshBillData"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver);
    }

    private void initView(){
        titleView = (DiDiTitleView) findViewById(R.id.title_view);
        titleView.setTitle(getString(R.string.raw_electronic_bill_title));
        titleView.setBack(this);
        year = (TextView) findViewById(R.id.year);
        month = (TextView) findViewById(R.id.month);
        electronic_bill_total = (TextView) findViewById(R.id.electronic_bill_total);
        wait_for_handle_cnt = (TextView) findViewById(R.id.wait_for_handle_cnt);
        agree_cnt = (TextView) findViewById(R.id.agree_cnt);
        disagree_cnt = (TextView) findViewById(R.id.disagree_cnt);
        electronic_bill_listview = (PullToRefreshListView) findViewById(R.id.electronic_bill_listview);
        arrow_click_view = (ImageView) findViewById(R.id.arrow_click_view);
        year.setOnClickListener(this);
        month.setOnClickListener(this);
        arrow_click_view.setOnClickListener(this);
        electronic_bill_listview.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.pull_up_load_more));
        electronic_bill_listview.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.loading));
        electronic_bill_listview.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.loosen_to_load_more));

        electronic_bill_listview.getLoadingLayoutProxy(true, false).setPullLabel(getString(R.string.loosen_to_refresh));
        electronic_bill_listview.getLoadingLayoutProxy(true, false).setRefreshingLabel(getString(R.string.refreshing));
        electronic_bill_listview.getLoadingLayoutProxy(true, false).setReleaseLabel(getString(R.string.pull_down_refresh));
        electronic_bill_items_listview = electronic_bill_listview.getRefreshableView();
        electronic_bill_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (electronic_bill_listview.isRefreshing()) {
                    if (electronic_bill_listview.isHeaderShown()) {
                        currentPage = 0;
                        sessionLoadData(true);
                    } else if (electronic_bill_listview.isFooterShown()) {
                        // 加载更多
                        if (isEnd) {
                            Toast.makeText(ElectronicBillActivity.this, "已经是最后一页", Toast.LENGTH_SHORT).show();
                            CloseRefreshTask closeRefreshTask = new CloseRefreshTask(electronic_bill_listview);
                            closeRefreshTask.execute();
                            return;
                        }
                        sessionLoadData(false);
                    }
                }
            }
        });
        electronic_bill_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BillItemModel mBillItemModel = billListData.get(position - 1);
                Intent intent = new Intent(ElectronicBillActivity.this, ElectronicBillDetailActivity.class);
                if(mBillItemModel.getStyle() == 2 ||
                        (mBillItemModel.getStyleTxt() != null && mBillItemModel.getStyleTxt().equals("补单"))){

                }else if (mBillItemModel != null && mBillItemModel.getFormType() != null
                        && mBillItemModel.getFormType().equals("2")) {//红狮叫车对账单
                    intent.putExtra("isHSJCBill", true);
                    startBillDetailActivity(intent,mBillItemModel);

                } else if(mBillItemModel.getFormType() != null && mBillItemModel.getFormType().equals("1")){//合同运输对账单
                    intent.putExtra("isHSJCBill", false);
                    startBillDetailActivity(intent,mBillItemModel);
                }
            }
        });
        electronic_bill_listview.setVisibility(View.GONE);
        getBillHeaderData();
    }

    private void startBillDetailActivity(Intent intent, BillItemModel mBillItemModel){
        intent.putExtra("sessionId", sessionID);
        intent.putExtra("startTimeStr", mBillItemModel.getBeginDateStr());
        intent.putExtra("endTimeStr", mBillItemModel.getEndDateStr());
        intent.putExtra("totalMoney", Util.formatDoubleToString(mBillItemModel.getTotalAmount(), mBillItemModel.getMoneyUnitText()));
        intent.putExtra("realMoney",Util.formatDoubleToString(mBillItemModel.getRealAmount(), mBillItemModel.getMoneyUnitText()));
        intent.putExtra("weight",Util.formatDoubleToString(mBillItemModel.getWeightTotal(), mBillItemModel.getWeightUnitText()));
        intent.putExtra("transitTimes",String.valueOf(mBillItemModel.getTransitTimes()));
        intent.putExtra("truckCount",String.valueOf(mBillItemModel.getTruckCount()));
        intent.putExtra("checkMan", mBillItemModel.getOppIdText());
        intent.putExtra("billCycleId", mBillItemModel.getBillCycleId());
        intent.putExtra("goodsStationUid", mBillItemModel.getGoodsStationUid());
        intent.putExtra("receiptStatus", mBillItemModel.getReceiptStatus());
        intent.putExtra("receiptStatusText", mBillItemModel.getReceiptStatusText());
//                    intent.putExtra("truckInfoList",mBillItemModel.getTruckVoList());
        if(mBillItemModel != null && mBillItemModel.getTruckVoList() != null){
            int size = mBillItemModel.getTruckVoList().size();
            if(size > 0){
                String truckId[] = new String[size];
                String truckArr[] = new String[size];
                for(int i = 0; i < size; i++){
                    truckId[i] = mBillItemModel.getTruckVoList().get(i).getTruckId();
                    truckArr[i] = mBillItemModel.getTruckVoList().get(i).getTruckNumber();
                }
                intent.putExtra("truckId",truckId);
                intent.putExtra("truckArr",truckArr);
            }
        }
        startActivity(intent);
    }

    private PromptManager mPromptManager = new PromptManager();
    private void sessionLoadData(final boolean isRefresh ) {
        mPromptManager.showProgressDialog1(ElectronicBillActivity.this,"请稍等");
        AjaxParams params = new AjaxParams();
        params.put("monthStr", monthStr);
        params.put("yearStr", yearStr);
        params.put("currentPage", String.valueOf(currentPage + 1));
        params.put("pageSize", String.valueOf(pageSize));
        Util.sessionPost(ElectronicBillActivity.this, BILL_ITEMS_URL, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                mPromptManager.closeProgressDialog();
                Log.d("huiyuan", "电子对账单列表数据 = " + t);
                getBillListData(t, isRefresh);
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                electronic_bill_listview.onRefreshComplete();
                mPromptManager.closeProgressDialog();
            }
        }, sessionID);
    }

    private void getBillListData(String t, boolean isRefresh) {
        JSONObject jsonObject;
        try {
            if (isRefresh) {
                billListData.clear();
                isEnd = false;
//                lastId = "";
                electronic_bill_listview.onRefreshComplete();
            }
            jsonObject = new JSONObject(t);
            JSONObject body = jsonObject.getJSONObject("body");
            String all = body.optString("items");
            //判断是否还有下一页
            isEnd = body.optBoolean("end");
            currentPage = body.optInt("currentPage");
//            pageSize = body.optInt("pageSize");

            List<BillItemModel> tempList = JSON.parseArray(all, BillItemModel.class);
            if(isRefresh){
                billListData = tempList;
                billDataAdapter = new BillDataAdapter(this, billListData);
                electronic_bill_items_listview.setAdapter(billDataAdapter);
            }else{
                billDataAdapter.addGoodsList(tempList);
            }
            if(billListData.size() <= 0){
                Toast.makeText(this, "无账单数据", Toast.LENGTH_LONG);
            }else{
                electronic_bill_listview.setVisibility(View.VISIBLE);
//                lastId = billListData.get(billListData.size() - 1).getLastId();
                if(!isRefresh)
                billDataAdapter.notifyDataSetChanged();
            }
            electronic_bill_listview.onRefreshComplete();

        } catch (JSONException e) {
            e.printStackTrace();
            electronic_bill_listview.onRefreshComplete();
            Toast.makeText(this,"无账单数据",Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.year:
            case R.id.month:
            case R.id.arrow_click_view:
                showDateChooseDialog();
            break;
            default:
                break;
        }
    }

    private void showDateChooseDialog(){
        DateDialog mDateDialog = new DateDialog(ElectronicBillActivity.this, R.style.data_filling_dialog,
                new SetDateCallBack() {
                    @Override
                    public void date(long date) {
                        Date mDate1 = new Date(date);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(mDate1);
                        if((cal.get(Calendar.MONTH) + 1)<10){
                            monthStr = "0"+ String.valueOf(cal.get(Calendar.MONTH) + 1);
                        }else{
                            monthStr = String.valueOf(cal.get(Calendar.MONTH) + 1);
                        }
                        yearStr = String.valueOf(cal.get(Calendar.YEAR));
                        year.setText(yearStr + "年");
                        month.setText(monthStr + "月");
                        getBillHeaderData();
//                        sessionLoadData(true);
                    }
                }, DateDialog.YearMonth, getString(R.string.choose_month));
        UploadUtil.setAnimation(mDateDialog, 0, true);
        mDateDialog.show();
    }

    private void getBillHeaderData(){
        AjaxParams params = new AjaxParams();
        params.put("monthStr", monthStr);
        params.put("yearStr", yearStr);
        Util.sessionPost(ElectronicBillActivity.this, BILL_HEADER_URL, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                Log.d("huiyuan","电子对账单头部数据 = " + t);
                if (t != null && !t.equals("")) {
                    try {
                        JSONObject object1 = new JSONObject(t);
                        String all = object1.optString("body");
                        JSONObject jsonObject = new JSONObject(all);
                        yearStr = jsonObject.optString("yearStr");
                        monthStr = jsonObject.optString("monthStr");
                        total_bill_count = jsonObject.optInt("count");
                        wait_for_handling_count = jsonObject.optInt("waitReceipt");
                        agree_bill_count = jsonObject.optInt("agree");
                        disagree_bill_count = jsonObject.optInt("disagree");

                        if(isFirstRequest){
                            if(yearStr.equals("")){
                                year.setText("xxxx");
                            }else year.setText(yearStr + "年");
                            if(monthStr.equals("")){
                                month.setText("xx");
                            }else month.setText(monthStr + "月");

                            isFirstRequest = false;
                        }

                        electronic_bill_total.setText("共" + total_bill_count + "条");
                        wait_for_handle_cnt.setText(wait_for_handling_count + "条");
                        agree_cnt.setText(agree_bill_count + "条");
                        disagree_cnt.setText(disagree_bill_count + "条");
                        if (total_bill_count > 0) {
                            electronic_bill_listview.setVisibility(View.VISIBLE);
                            currentPage = 0;
                            sessionLoadData(true);
                        }else {
                            electronic_bill_listview.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {

            }
        }, sessionID);
    }
}
