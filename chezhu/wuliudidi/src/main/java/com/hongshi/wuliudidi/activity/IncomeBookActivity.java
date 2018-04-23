package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.IncomeBookAdapter;
import com.hongshi.wuliudidi.dialog.DateDialog;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.SetDateCallBack;
import com.hongshi.wuliudidi.model.ClassfyStatVO;
import com.hongshi.wuliudidi.model.TransitInfoStatVO;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.view.NoScrollListView;
import com.hongshi.wuliudidi.view.ObservableScrollView;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.hongshi.wuliudidi.CommonRes.TYPE_BOTTOM;

/**
 * @author huiyuan
 * Created by huiyuan on 2017/8/22.
 */

public class IncomeBookActivity extends Activity implements View.OnClickListener,
        ObservableScrollView.ScrollViewListener{

    private ImageView back,choose_date_arrow;
    private TextView date_text,transit_count_text,
            transit_weight_text,transit_trucks_text,transit_materia_text,sort_by_truck_text,
            sort_by_goods_text,sort_by_truck_head_text,sort_by_goods_head_text,
            cash_handled_count,consume_handled_count,oil_handled_count,tyre_handled_count,total_money_text;
    private View truck_sort_bottom_line,goods_sort_bottom_line,
            truck_sort_bottom_line_head,goods_sort_bottom_line_head;
    private LinearLayout middle_tab_bar,head_tab_bar;
    private RelativeLayout sort_by_truck_container,sort_by_goods_container,
            sort_by_truck_container_head,sort_by_goods_container_head,title_view;
    private ObservableScrollView scroll_view;
    private NoScrollListView noScrollListView;
    private DateDialog mDateDialog;

    private IncomeBookAdapter incomeBookAdapter;
    private List<ClassfyStatVO> inComeBookModelList = new ArrayList<>();

    private String incomeBookUrl = GloableParams.HOST + "carrier/app/bill/transitInfoStat.do";
    private int titleViewBottomLocation;
    private int classifyStatType = 1;
    private String userId = "";
    private String beginDate = "";
    private String endDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getSharedPreferences("config",MODE_PRIVATE).getString("userId","");
        setContentView(R.layout.income_book_activity);
        initView();
    }

    private void initView(){

        back = (ImageView) findViewById(R.id.back);
        choose_date_arrow = (ImageView) findViewById(R.id.choose_date_arrow);
        date_text = (TextView) findViewById(R.id.date_text);
        total_money_text = (TextView) findViewById(R.id.total_money_text);
        cash_handled_count = (TextView) findViewById(R.id.cash_handled_count);
        consume_handled_count = (TextView) findViewById(R.id.consume_handled_count);
//        cash_unhandled_count = (TextView) findViewById(R.id.cash_unhandled_count);
        oil_handled_count = (TextView) findViewById(R.id.oil_handled_count);
//        oil_unhandled_count = (TextView) findViewById(R.id.oil_unhandled_count);
        tyre_handled_count = (TextView) findViewById(R.id.tyre_handled_count);
//        tyre_unhandled_count = (TextView) findViewById(R.id.tyre_unhandled_count);
        transit_count_text = (TextView) findViewById(R.id.transit_count_text);
        transit_weight_text = (TextView) findViewById(R.id.transit_weight_text);
        transit_trucks_text = (TextView) findViewById(R.id.transit_trucks_text);
        transit_materia_text = (TextView) findViewById(R.id.transit_materia_text);
        sort_by_truck_text = (TextView) findViewById(R.id.sort_by_truck_text);
        sort_by_goods_text = (TextView) findViewById(R.id.sort_by_goods_text);
        sort_by_truck_head_text = (TextView) findViewById(R.id.sort_by_truck_head_text);
        sort_by_goods_head_text = (TextView) findViewById(R.id.sort_by_goods_head_text);
        truck_sort_bottom_line = findViewById(R.id.truck_sort_bottom_line);
        goods_sort_bottom_line = findViewById(R.id.goods_sort_bottom_line);
        truck_sort_bottom_line_head = findViewById(R.id.truck_sort_bottom_line_head);
        goods_sort_bottom_line_head = findViewById(R.id.goods_sort_bottom_line_head);
        middle_tab_bar = (LinearLayout) findViewById(R.id.middle_tab_bar);
        head_tab_bar = (LinearLayout) findViewById(R.id.head_tab_bar);
        sort_by_truck_container = (RelativeLayout) findViewById(R.id.sort_by_truck_container);
        sort_by_goods_container = (RelativeLayout) findViewById(R.id.sort_by_goods_container);
        sort_by_truck_container_head = (RelativeLayout) findViewById(R.id.sort_by_truck_container_head);
        sort_by_goods_container_head = (RelativeLayout) findViewById(R.id.sort_by_goods_container_head);
        title_view = (RelativeLayout) findViewById(R.id.title_view);
        scroll_view = (ObservableScrollView) findViewById(R.id.scroll_view);
        noScrollListView = (NoScrollListView) findViewById(R.id.no_scroll_listView);

        back.setOnClickListener(this);
        choose_date_arrow.setOnClickListener(this);
        date_text.setOnClickListener(this);
        sort_by_truck_container.setOnClickListener(this);
        sort_by_goods_container.setOnClickListener(this);
        sort_by_truck_container_head.setOnClickListener(this);
        sort_by_goods_container_head.setOnClickListener(this);

        //for test
//        InComeBookModel model = new InComeBookModel();
//        model.setCash_text("111111");
//        model.setOil_text("222222");
//        model.setTotal_money("123434.0");
//        model.setTruck_number_text("浙A 12345");
//        model.setTyre_text("333333");
//        inComeBookModelList.add(model);
//
//        model = new InComeBookModel();
//        model.setCash_text("111111");
//        model.setOil_text("222222");
//        model.setTotal_money("123434.0");
//        model.setTruck_number_text("浙B 12345");
//        model.setTyre_text("333333");
//        inComeBookModelList.add(model);
//
//        model = new InComeBookModel();
//        model.setCash_text("111111");
//        model.setOil_text("222222");
//        model.setTotal_money("123434.0");
//        model.setTruck_number_text("浙C 12345");
//        model.setTyre_text("333333");
//        inComeBookModelList.add(model);
//
//        model = new InComeBookModel();
//        model.setCash_text("111111");
//        model.setOil_text("222222");
//        model.setTotal_money("123434.0");
//        model.setTruck_number_text("浙D 12345");
//        model.setTyre_text("333333");
//        inComeBookModelList.add(model);
//
//        model = new InComeBookModel();
//        model.setCash_text("111111");
//        model.setOil_text("222222");
//        model.setTotal_money("123434.0");
//        model.setTruck_number_text("浙E 12345");
//        model.setTyre_text("333333");
//        inComeBookModelList.add(model);
//
//        model = new InComeBookModel();
//        model.setCash_text("111111");
//        model.setOil_text("222222");
//        model.setTotal_money("123434.0");
//        model.setTruck_number_text("浙F 12345");
//        model.setTyre_text("333333");
//        inComeBookModelList.add(model);
//
//        model = new InComeBookModel();
//        model.setCash_text("111111");
//        model.setOil_text("222222");
//        model.setTotal_money("123434.0");
//        model.setTruck_number_text("浙G 12345");
//        model.setTyre_text("333333");
//        inComeBookModelList.add(model);

        incomeBookAdapter = new IncomeBookAdapter(IncomeBookActivity.this,inComeBookModelList);
        noScrollListView.setAdapter(incomeBookAdapter);
        scroll_view.setScrollViewListener(this);
        noScrollListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(IncomeBookActivity.this,TransitRecordDetailActivity.class);
                if(inComeBookModel != null) {
                    intent.putExtra("billCycleJsonStr",inComeBookModel.getBillCycleJsonStr());
                    intent.putExtra("classifyStatType",classifyStatType);
                    intent.putExtra("periodText",inComeBookModel.getPeriod());
                }
                ClassfyStatVO classfyStatVO = inComeBookModelList.get(position);
                intent.putExtra("classfyStatVO",classfyStatVO);
                intent.putExtra("showCompany",true);
                startActivity(intent);
            }
        });
        noScrollListView.setFocusable(false);
        getIncomeDataList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.choose_date_arrow:
            case R.id.date_text:
                mDateDialog = new DateDialog(IncomeBookActivity.this, R.style.data_filling_dialog,
                        new SetDateCallBack() {
                            @Override
                            public void date(long date) {
                                Date mDate = new Date(date);
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(mDate);
                                date_text.setText(String.valueOf(cal.get(Calendar.YEAR)) + "年"
                                        + String.valueOf(cal.get(Calendar.MONTH) + 1) + "月");
                                beginDate =  String.valueOf(cal.get(Calendar.YEAR) +
                                        "-" + String.valueOf(cal.get(Calendar.MONTH) + 1));
                                endDate = String.valueOf(cal.get(Calendar.YEAR) +
                                        "-" + String.valueOf(cal.get(Calendar.MONTH) + 1));
                                getIncomeDataList();
                            }
                        }, DateDialog.YearMonth, "请选择查询月份");
                UploadUtil.setAnimation(mDateDialog, TYPE_BOTTOM, true);
                mDateDialog.show();
                break;
            case R.id.sort_by_truck_container:
            case R.id.sort_by_truck_container_head:
                sort_by_truck_text.setTextColor(getResources().getColor(R.color.theme_color));
                sort_by_goods_text.setTextColor(getResources().getColor(R.color.gray));
                truck_sort_bottom_line.setVisibility(View.VISIBLE);
                goods_sort_bottom_line.setVisibility(View.GONE);
                sort_by_truck_head_text.setTextColor(getResources().getColor(R.color.theme_color));
                sort_by_goods_head_text.setTextColor(getResources().getColor(R.color.gray));
                truck_sort_bottom_line_head.setVisibility(View.VISIBLE);
                goods_sort_bottom_line_head.setVisibility(View.GONE);
                classifyStatType = 1;
                getIncomeDataList();
                break;
            case R.id.sort_by_goods_container:
            case R.id.sort_by_goods_container_head:
                sort_by_truck_text.setTextColor(getResources().getColor(R.color.gray));
                sort_by_goods_text.setTextColor(getResources().getColor(R.color.theme_color));
                truck_sort_bottom_line.setVisibility(View.GONE);
                goods_sort_bottom_line.setVisibility(View.VISIBLE);
                sort_by_truck_head_text.setTextColor(getResources().getColor(R.color.gray));
                sort_by_goods_head_text.setTextColor(getResources().getColor(R.color.theme_color));
                truck_sort_bottom_line_head.setVisibility(View.GONE);
                goods_sort_bottom_line_head.setVisibility(View.VISIBLE);
                classifyStatType = 2;
                getIncomeDataList();
                break;
            default:
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        int[] location = new int[2];
        title_view.getLocationOnScreen(location);
        int locationY = location[1];
        titleViewBottomLocation = locationY + title_view.getHeight();
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        int[] location = new int[2];
        middle_tab_bar.getLocationOnScreen(location);
        int locationY = location[1];
//        Log.i("locationY", locationY + "  " + "标题栏底部的Y坐标是：" + titleViewBottomLocation);

        if (locationY <= titleViewBottomLocation && (head_tab_bar.getVisibility() == View.GONE || head_tab_bar.getVisibility() == View.INVISIBLE)) {
            head_tab_bar.setVisibility(View.VISIBLE);
        }

        if (locationY > titleViewBottomLocation && head_tab_bar.getVisibility() == View.VISIBLE) {
            head_tab_bar.setVisibility(View.GONE);
        }
    }

    private TransitInfoStatVO inComeBookModel;
    private boolean isFirstReq = true;
    private void getIncomeDataList(){
        AjaxParams params = new AjaxParams();
        if(!isFirstReq){
            params.put("userId",userId);
            params.put("beginDate",beginDate);
            params.put("endDate",endDate);
            params.put("classifyStatType","" + classifyStatType);
        }
        DidiApp.getHttpManager().sessionPost(IncomeBookActivity.this, incomeBookUrl,
                params, new ChildAfinalHttpCallBack() {
                    @Override
                    public void data(String t) {
                        if(inComeBookModelList != null && inComeBookModelList.size() > 0){
                            inComeBookModelList.clear();
                        }
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(t);
                            String body = jsonObject.optString("body");
                            inComeBookModel = JSON.parseObject(
                                    body, TransitInfoStatVO.class);
                            if(isFirstReq){
                                String period = inComeBookModel.getPeriod();
                                String year = period.substring(0,4);
                                int monthPosition = period.indexOf("月");
                                String month = period.substring(5,monthPosition);
                                beginDate = year + "-" + month;
                                endDate = year + "-" + month;
                                date_text.setText(inComeBookModel.getPeriod());
                            }
                            total_money_text.setText(inComeBookModel.getTotalMoney());
                            cash_handled_count.setText(inComeBookModel.getTotalSalaryMoney());
                            consume_handled_count.setText(inComeBookModel.getTotalConsumptionMoney());
                            oil_handled_count.setText(inComeBookModel.getTotalOilMoney());
                            tyre_handled_count.setText(inComeBookModel.getTotalTyreMoney());
                            transit_count_text.setText(inComeBookModel.getTransitTimes() + "车");
                            if (inComeBookModel.getTotalWeight() != null){
                                transit_weight_text.setText(inComeBookModel.getTotalWeight() + "吨");
                            }else {
                                transit_weight_text.setText( "0吨");
                            }
                            transit_trucks_text.setText(inComeBookModel.getTruckCount() + "辆");
                            transit_materia_text.setText(inComeBookModel.getGoodsTypeCount() + "种");

                            List<ClassfyStatVO> list = inComeBookModel.getClassfyStatVOList();
                            if(list == null || (list != null && list.size() <= 0)){
                                incomeBookAdapter = new IncomeBookAdapter(IncomeBookActivity.this,
                                        inComeBookModelList);
                                noScrollListView.setAdapter(incomeBookAdapter);
                            }else {
                                inComeBookModelList.addAll(list);
                                incomeBookAdapter.notifyDataSetChanged();
                            }
                            isFirstReq = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                            if(inComeBookModelList != null && inComeBookModelList.size() > 0){
                                inComeBookModelList.clear();
                                incomeBookAdapter = new IncomeBookAdapter(IncomeBookActivity.this,
                                        inComeBookModelList);
                                noScrollListView.setAdapter(incomeBookAdapter);
                            }
                        }
                    }

                    @Override
                    public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                        ToastUtil.show(IncomeBookActivity.this, errMsg);
                    }
                });
    }

}
