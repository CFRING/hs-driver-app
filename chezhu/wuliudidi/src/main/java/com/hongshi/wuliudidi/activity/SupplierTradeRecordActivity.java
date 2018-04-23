package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.SupplierTradeRecordAdapter;
import com.hongshi.wuliudidi.dialog.DateDialog;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.SetDateCallBack;
import com.hongshi.wuliudidi.model.SupplierTradeRecordItemModel;
import com.hongshi.wuliudidi.model.SupplierTradeRecordModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.CloseRefreshTask;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.view.DiDiTitleView;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.hongshi.wuliudidi.CommonRes.TYPE_BOTTOM;

/**
 * @author Created by huiyuan on 2017/9/14.
 */

public class SupplierTradeRecordActivity extends Activity {

    private DiDiTitleView title_view;
//    private ImageView date_choose;
    private PullToRefreshListView orders_list;
    private ListView listView;
    private DateDialog mDateDialog;

    private final String recordUrl = GloableParams.HOST + "carrier/qrcodepay/business/trade/list.do?";
//    private final String recordUrl = "http://192.168.158.33:8080/gwcz/carrier/qrcodepay/business/trade/list.do?";
    private int pageNo = 1;
    private boolean isEnd = false;
    private String yearAndMonth = "";
    private String payeeUid;
    private List<SupplierTradeRecordModel> supplierTradeRecordModels = new ArrayList<>();
    private List<SupplierTradeRecordItemModel> supplierTradeRecordItemModelList = new ArrayList<>();
    private SupplierTradeRecordAdapter supplierTradeRecordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payeeUid = getIntent().getStringExtra("payeeUid");
        setContentView(R.layout.supplier_trade_record_activity);
        initViews();
    }

    private void initViews(){
        title_view = (DiDiTitleView) findViewById(R.id.title_view);
        title_view.setBack(this);
        title_view.setTitle("交易记录");

//        date_choose = (ImageView) findViewById(R.id.date_choose);
        orders_list = (PullToRefreshListView) findViewById(R.id.orders_list);

        title_view.getRightImage().setImageResource(R.drawable.date_choose_image);
        title_view.getRightImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateDialog = new DateDialog(SupplierTradeRecordActivity.this, R.style.data_filling_dialog,
                        new SetDateCallBack() {
                            @Override
                            public void date(long date) {
                                Date mDate = new Date(date);
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(mDate);
                                yearAndMonth = String.valueOf(cal.get(Calendar.YEAR)) +
                                        "-" + String.valueOf(cal.get(Calendar.MONTH) + 1);
                                getRecordData(true);
                            }
                        }, DateDialog.YearMonth, "请选择时间");
                UploadUtil.setAnimation(mDateDialog, TYPE_BOTTOM, true);
                mDateDialog.show();
            }
        });

        orders_list.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        orders_list.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");
        orders_list.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载更多");

        orders_list.getLoadingLayoutProxy(true, false).setPullLabel("松开刷新");
        orders_list.getLoadingLayoutProxy(true, false).setRefreshingLabel("刷新中");
        orders_list.getLoadingLayoutProxy(true, false).setReleaseLabel("下拉刷新");

        listView = orders_list.getRefreshableView();
        orders_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (orders_list.isRefreshing()) {
                    if (orders_list.isHeaderShown()){
                        getRecordData(true);
                    } else if (orders_list.isFooterShown()) {
                        // 加载更多
                        if(isEnd){
                            Toast.makeText(SupplierTradeRecordActivity.this, "已经是最后一页", Toast.LENGTH_SHORT).show();
                            CloseRefreshTask closeRefreshTask = new CloseRefreshTask(orders_list);
                            closeRefreshTask.execute();
                            return;
                        }
                        pageNo = pageNo + 1;
                        getRecordData(false);
                    }
                }
            }
        });

//        testData();

        supplierTradeRecordAdapter =
                new SupplierTradeRecordAdapter(SupplierTradeRecordActivity.this,supplierTradeRecordItemModelList);
        listView.setAdapter(supplierTradeRecordAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SupplierTradeRecordItemModel itemModel = supplierTradeRecordItemModelList.get(position - 1);
                Intent intent = new Intent(SupplierTradeRecordActivity.this,OrderDetailActivity.class);
                intent.putExtra("orderId",itemModel.getId());
                startActivity(intent);
            }
        });

        getRecordData(true);
    }

//    private void testData(){
//        //for test
//        SupplierTradeRecordModel model = new SupplierTradeRecordModel();
//        model.setDateTime("2017-09");
//        model.setMoneyStr("-12");
//        model.setPayDateTime("9月12日 10:21");
//        model.setSupplierShowNickName("兰溪易捷");
//        supplierTradeRecordModels.add(model);
//
//        model = new SupplierTradeRecordModel();
//        model.setDateTime("2017-09");
//        model.setMoneyStr("-13");
//        model.setPayDateTime("9月13日 10:22");
//        model.setSupplierShowNickName("兰溪易捷");
//        supplierTradeRecordModels.add(model);
//
//        model = new SupplierTradeRecordModel();
//        model.setDateTime("2017-08");
//        model.setMoneyStr("-14");
//        model.setPayDateTime("8月12日 10:21");
//        model.setSupplierShowNickName("兰溪易捷");
//        supplierTradeRecordModels.add(model);
//
//        model = new SupplierTradeRecordModel();
//        model.setDateTime("2017-08");
//        model.setMoneyStr("-15");
//        model.setPayDateTime("8月13日 10:22");
//        model.setSupplierShowNickName("兰溪易捷");
//        supplierTradeRecordModels.add(model);
//
//        model = new SupplierTradeRecordModel();
//        model.setDateTime("2017-07");
//        model.setMoneyStr("-16");
//        model.setPayDateTime("7月12日 10:21");
//        model.setSupplierShowNickName("兰溪易捷");
//        supplierTradeRecordModels.add(model);
//
//        model = new SupplierTradeRecordModel();
//        model.setDateTime("2017-07");
//        model.setMoneyStr("-17");
//        model.setPayDateTime("7月13日 10:22");
//        model.setSupplierShowNickName("兰溪易捷");
//        supplierTradeRecordModels.add(model);
//    }

    private void getRecordData(final boolean isRefresh){
        if(isRefresh){
            pageNo = 1;
        }
        AjaxParams params = new AjaxParams();
        params.put("dateTime",yearAndMonth);
        params.put("payeeUid",payeeUid);
        params.put("pageNo","" + pageNo);
        params.put("pageSize","10");

        DidiApp.getHttpManager().sessionPost(SupplierTradeRecordActivity.this, recordUrl, params,
                new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                try {
                    isLoginData(t,isRefresh);
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {

            }
        });
    }

    private void isLoginData(String t, boolean isRefresh){
        JSONObject jsonObject;
        try {
            if(isRefresh){
                supplierTradeRecordModels.clear();
                isEnd = false;
                orders_list.onRefreshComplete();
            }
            jsonObject = new JSONObject(t);
            JSONObject body = jsonObject.getJSONObject("body");
            String all = body.getString("items");
            //判断是否还有下一页
            isEnd = body.getBoolean("end");
            pageNo = body.getInt("currentPage");
            List<SupplierTradeRecordModel> tempList = JSON.parseArray(all,SupplierTradeRecordModel.class);
            if(isRefresh){
                supplierTradeRecordModels = tempList;
                dataSort();
                supplierTradeRecordAdapter = new SupplierTradeRecordAdapter(SupplierTradeRecordActivity.this,
                        supplierTradeRecordItemModelList);
                listView.setAdapter(supplierTradeRecordAdapter);
            }else{
                supplierTradeRecordModels.addAll(tempList);
                dataSort();
                supplierTradeRecordAdapter.notifyDataSetChanged();
            }
            orders_list.onRefreshComplete();
        } catch (Exception e) {
            orders_list.onRefreshComplete();
            if(supplierTradeRecordItemModelList!= null){
                supplierTradeRecordItemModelList.clear();
            }
            supplierTradeRecordAdapter = new SupplierTradeRecordAdapter(SupplierTradeRecordActivity.this,
                    supplierTradeRecordItemModelList);
            listView.setAdapter(supplierTradeRecordAdapter);
        }
    }

    private void dataSort(){
        supplierTradeRecordItemModelList.clear();
        for(int i = 0; i < supplierTradeRecordModels.size(); i++){
            String dateTime = supplierTradeRecordModels.get(i).getDateTime();
            List<SupplierTradeRecordItemModel> tempList = supplierTradeRecordModels.get(i).getQrCodeOrderQueryVOs();
            int size = tempList.size();
            for(int j = 0; j < size; j ++){
                tempList.get(j).setCreateDate(dateTime);
            }
            supplierTradeRecordItemModelList.addAll(tempList);
        }
    }
}
